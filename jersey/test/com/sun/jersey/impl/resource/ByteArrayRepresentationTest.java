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

package com.sun.jersey.impl.resource;

import com.sun.jersey.impl.AbstractResourceTester;
import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
@SuppressWarnings("unchecked")
public class ByteArrayRepresentationTest extends AbstractResourceTester {
    
    public ByteArrayRepresentationTest(String testName) {
        super(testName);
    }

    @Path("/")
    static public class Resource { 
        @POST
        public byte[] doPost(byte[] in) {
            assertEquals("CONTENT", new String(in));
            return "CONTENT".getBytes();
        }
    }
    
    public void testPut() {
        initiateWebApplication(Resource.class);
        WebResource r = resource("/");
        
        assertEquals("CONTENT", r.post(String.class, "CONTENT"));
    }
}