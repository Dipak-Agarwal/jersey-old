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

package com.sun.jersey.api.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;

/**
 * An abstraction for a HTTP request
 */
public interface HttpRequestContext extends HttpHeaders,
        Request, SecurityContext {
    
    /**
     * Get the base URI of the request.
     * 
     * @return the base URI.
     */
    URI getBaseUri();
       
    /**
     * Get the (complete) request URI.
     * 
     * @return the request URI.
     */
    URI getRequestUri();
    
    /**
     * Get the absolute path URI of the request.
     * 
     * @return the absolute URI.
     */
    URI getAbsolutePath();
    
    /**
     * Get a HTTP header value.
     * 
     * @param name the HTTP header
     * @return the HTTP header value. If the HTTP header is not present then
     * null is returned. If the HTTP header is present but has no value then
     * the empty string is returned. If the HTTP header is present more than
     * once then the values of joined together and separated by a ',' character.
     */
    String getHeaderValue(String name);

    /**
     * Select the first media type, from a list of media types, that is most
     * acceptable according to the requested acceptable media types.
     *
     * @param mediaTypes the list of media types
     * @return the most acceptable media type, or null if no media type
     *         was found to be acceptable.
     */
    MediaType getAcceptableMediaType(List<MediaType> mediaTypes);
    
    /**
     * Get the request entity, returns null if the request does not
     * contain an entity body.
     * 
     * @param type the type of entity
     * @return the request entity or null
     * @throws WebApplicationException if the content of the request
     * cannot be mapped to an entity of the requested type
     */
    <T> T getEntity(Class<T> type) 
            throws WebApplicationException;
        
    /**
     * Get the request entity, returns null if the request does not
     * contain an entity body.
     * 
     * @param type the type of entity
     * @param genericType type the generic type of entity, it is the responsibility
     *        of the callee to ensure that the type and generic type are
     *        consistent otherwise the behvaiour of this method is undefned.
     * @param as the annoations associated with the type 
     * @return the request entity or null
     * @throws WebApplicationException if the content of the request
     * cannot be mapped to an entity of the requested type
     */
    <T> T getEntity(Class<T> type, Type genericType, Annotation[] as) 
            throws WebApplicationException;
    
    /**
     * Get the HTTP method name
     * @return the method name as a String
     */
    String getHttpMethod();
    
    /**
     * Get the cookie name value map.
     * @return the cookie bame value map.
     */
    MultivaluedMap<String, String> getCookieNameValueMap();
}