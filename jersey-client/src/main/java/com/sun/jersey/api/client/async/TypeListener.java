/*
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
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
package com.sun.jersey.api.client.async;

import com.sun.jersey.api.client.AsyncUniformInterface;
import com.sun.jersey.api.client.AsyncWebResource;
import com.sun.jersey.api.client.GenericType;

/**
 * A listener to be implemented by clients that wish to receive callback
 * notification of the completion of requests invoked asynchronously.
 * <p>
 * This listener is a helper class providing implementions for the methods
 * {@link ITypeListener#getType() } and {@link ITypeListener#getGenericType() }.
 * <p>
 * Instances of this class may be passed to appropriate methods on
 * {@link AsyncWebResource} (or more specifically methods on 
 * {@link AsyncUniformInterface}). For example,
 * <blockquote><pre>
 *     AsyncWebResource r = ..
 *     Future&lt;String&gt; f = r.get(new TypeListener&lt;String&gt;(String.class) {
 *         public void onComplete(Future&lt;String&gt; f) throws InterruptedException {
 *             try {
 *                 String s = f.get();
 *             } catch (ExecutionException ex) {
 *                 // Do error processing
 *                 if (t instanceof UniformInterfaceException) {
 *                     // Request/response error
 *                 } else
 *                     // Error making request e.g. timeout
 *                 }
 *
 *             }
 *         }
 *     });
 * </pre></blockquote>
 *
 * @param <T> the type of the response.
 */
public abstract class TypeListener<T> implements ITypeListener<T> {

    private final Class<T> type;

    private final GenericType<T> genericType;

    // TODO
//    public TypeListener() {
//        // determine type or genericType from reflection
//    }

    /**
     * Construct a new listener defining the class of the response to receive.
     *
     * @param type the class of the response.
     */
    public TypeListener(Class<T> type) {
        this.type = type;
        this.genericType = null;
    }

    /**
     * Construct a new listener defining the generic type of the response to
     * receive.
     *
     * @param genericType the generic type of the response.
     */
    public TypeListener(GenericType<T> genericType) {
        this.type = genericType.getRawClass();
        this.genericType = genericType;
    }

    public Class<T> getType() {
        return type;
    }

    public GenericType<T> getGenericType() {
        return genericType;
    }
}