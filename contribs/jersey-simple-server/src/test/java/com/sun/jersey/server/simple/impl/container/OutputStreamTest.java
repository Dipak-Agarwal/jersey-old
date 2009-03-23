/*
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://jersey.dev.java.net/CDDL+GPL.html
 * or jersey/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at jersey/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.jersey.server.simple.impl.container;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.core.HttpRequestContext;
import com.sun.jersey.api.core.HttpResponseContext;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class OutputStreamTest extends AbstractSimpleServerTester {
    @Path("/output")
    public static class TestResource { // implements WebResource {

        @Produces("text/plain")
        @GET
        public void get(HttpRequestContext requestContext, 
                HttpResponseContext responseContext) throws IOException {
            assertEquals("GET", requestContext.getMethod());
            
            responseContext.getOutputStream().
                    write("RESOURCE".getBytes());
        }
        
        @Produces("text/plain")
        @POST
        public void post(HttpRequestContext requestContext, 
                HttpResponseContext responseContext) throws IOException {
            assertEquals("POST", requestContext.getMethod());
            
            String s = requestContext.getEntity(String.class);
            assertEquals("RESOURCE", s);
     
            responseContext.getOutputStream().
                    write("RESOURCE".getBytes());
        }
    }
    
    public OutputStreamTest(String testName) {
        super(testName);
    }
    
    public void testGet() {
        startServer(TestResource.class);
                
        WebResource r = Client.create().resource(getUri().path("output").build());
        assertEquals("RESOURCE", r.get(String.class));        
    }
    
    public void testPost() {        
        startServer(TestResource.class);
                
        WebResource r = Client.create().resource(getUri().path("output").build());
        assertEquals("RESOURCE", r.post(String.class, "RESOURCE"));
    }
    
    public void testAll() {
        startServer(TestResource.class);
                
        WebResource r = Client.create().resource(getUri().path("output").build());
        assertEquals("RESOURCE", r.get(String.class));        
        
        r = Client.create().resource(getUri().path("output").build());
        assertEquals("RESOURCE", r.post(String.class, "RESOURCE"));
    }
}
