/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2007 Sun Microsystems, Inc. All rights reserved. 
 * 
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License("CDDL") (the "License").  You may not use this file
 * except in compliance with the License. 
 * 
 * You can obtain a copy of the License at:
 *     https://jersey.dev.java.net/license.txt
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * When distributing the Covered Code, include this CDDL Header Notice in each
 * file and include the License file at:
 *     https://jersey.dev.java.net/license.txt
 * If applicable, add the following below this CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 *     "Portions Copyrighted [year] [name of copyright owner]"
 */

package com.sun.ws.rest.impl.container.servlet;

import com.sun.ws.rest.api.container.ContainerException;
import com.sun.ws.rest.spi.container.AbstractContainerResponse;
import javax.ws.rs.ext.EntityProvider;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.ProviderFactory;

/**
 * Adapts a HttpServletResponse to provide the methods of HttpResponse
 *
 */
public final class HttpResponseAdaptor extends AbstractContainerResponse {
    private final ServletContext context;
            
    private final HttpServletRequest request;
    
    private final HttpServletResponse response;
    
    private RequestDispatcher d;
        
    private OutputStream out;
    
    /* package */ HttpResponseAdaptor(ServletContext context, HttpServletResponse response, 
            HttpServletRequest request, HttpRequestAdaptor requestContext) {
        super(requestContext);
        this.context = context;
        this.response = response;
        this.request = request;
    }

    
    // HttpResponseContextImpl
    
    final class OutputStreamAdapter extends OutputStream {
        OutputStream out;
        
        public void write(int b) throws IOException {
            initiate();
            out.write(b);
        }

        public void write(byte b[]) throws IOException {
            initiate();
            out.write(b);
        }

        public void write(byte b[], int off, int len) throws IOException {
            initiate();
            out.write(b, off, len);
        }

        public void flush() throws IOException {
            initiate();
            out.flush();
        }

        public void close() throws IOException {
            initiate();
            out.close();
        }
        
        void initiate() throws IOException {
            if (out == null)
                out = response.getOutputStream();
        }
    }
    
    protected OutputStream getUnderlyingOutputStream() throws IOException {
        if (out == null)
            out = new OutputStreamAdapter();
        
        return out;
    }

    protected void commitStatusAndHeaders() throws IOException {
        response.setStatus(this.getStatus());
        
        MultivaluedMap<String, Object> headers = this.getHttpHeaders();
        for (Map.Entry<String, List<Object>> e : headers.entrySet()) {
            for (Object v : e.getValue()) {
                response.addHeader(e.getKey(), getHeaderValue(v));
            }
        }
    }

    
    /* package */ void commitAll() throws IOException {
        if (isCommitted()) return;
        
        if (response.isCommitted()) return;
        
        commitStatusAndHeaders();
    
        writeEntity(getUnderlyingOutputStream());
    }    
    
    /* package */ RequestDispatcher getRequestDispatcher() {
        return d;
    }
        
    /* package */ void forwardTo(String path, Object it) {        
        d = context.getRequestDispatcher(path);
        if (d == null) {
            throw new ContainerException("No request dispatcher for: " + path);
        }
        
        d = new RequestDispatcherWrapper(d, it);
        // TODO may need to forward immediately
    }    
}