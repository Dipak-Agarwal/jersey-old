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

package com.sun.ws.rest.impl.container.httpserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsServer;
import com.sun.ws.rest.spi.container.AbstractContainerRequest;
import com.sun.ws.rest.impl.http.header.HttpHeaderFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map.Entry;
import javax.ws.rs.core.MultivaluedMap;

/**
 * A HTTP request adapter for {@link HttpExchange}.
 *
 * @author Paul.Sandoz@Sun.Com
 */
public final class HttpServerRequestAdaptor extends AbstractContainerRequest {
    
    private final HttpExchange exchange;
    
    public HttpServerRequestAdaptor(HttpExchange exchange) throws IOException {
        super(exchange.getRequestMethod(), exchange.getRequestBody());
        this.exchange = exchange;
        
        initiateUriInfo();
        copyHttpHeaders();
    }
    
    private void initiateUriInfo() {
        /**
         * This is a URI that contains the path, query and
         * fragment components.
         */
        final URI exchangeUri = exchange.getRequestURI();
        
        /**
         * The base path specified by the HTTP context of the HTTP handler.
         * It is in decoded form.
         */
        String decodedBasePath = exchange.getHttpContext().getPath();
        if (!decodedBasePath.endsWith("/")) {
            decodedBasePath += "/";                
        }

        /*
         * The following is madness, there is no easy way to get 
         * the complete URI of the HTTP request!!
         *
         * TODO this is missing the user information component, how
         * can this be obtained?
         */
        HttpServer server = exchange.getHttpContext().getServer();
        String scheme = (server instanceof HttpsServer) ? "https" : "http";
        InetSocketAddress addr = exchange.getLocalAddress();
        try {
            this.baseUri = new URI(scheme, null, addr.getHostName(), addr.getPort(), 
                    decodedBasePath, null, null);
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
            
        this.completeUri = this.baseUri.resolve(exchangeUri);
    }
    
    
    private void copyHttpHeaders() {
        MultivaluedMap<String, String> headers = getRequestHeaders();
        
        Headers eh = exchange.getRequestHeaders();
        for (Entry<String, List<String>> e : eh.entrySet()) {
            headers.put(e.getKey(), e.getValue());
            if (e.getKey().equalsIgnoreCase("cookie")) {
                for (String headerValue: e.getValue()) {
                    getCookies().addAll(HttpHeaderFactory.createCookies(headerValue));
                }
            }
        }
    }    
}