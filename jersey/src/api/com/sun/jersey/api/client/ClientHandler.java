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

package com.sun.jersey.api.client;

/**
 * A client handler that handles a HTTP request and returns the HTTP response.
 * 
 * @author Paul.Sandoz@Sun.Com
 */
public interface ClientHandler {
    /**
     * Handle a HTTP request as a {@link ClientRequest} and return the HTTP
     * response as a {@link ClientResponse}.
     * 
     * @param cr the HTTP request.
     * @return the HTTP response.
     * @throws com.sun.jersey.api.client.ClientHandlerException if the client
     * handler fails to process the request or response.
     */
    ClientResponse handle(ClientRequest cr) throws ClientHandlerException;
}