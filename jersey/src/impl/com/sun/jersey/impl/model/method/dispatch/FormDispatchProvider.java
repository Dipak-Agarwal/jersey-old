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

package com.sun.jersey.impl.model.method.dispatch;

import javax.ws.rs.WebApplicationException;
import com.sun.jersey.api.container.ContainerException;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.model.AbstractResourceMethod;
import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.api.representation.FormParam;
import com.sun.jersey.impl.ResponseBuilderImpl;
import com.sun.jersey.spi.resource.InjectableProviderContext;
import com.sun.jersey.spi.dispatch.RequestDispatcher;
import com.sun.jersey.spi.inject.Injectable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class FormDispatchProvider implements ResourceMethodDispatchProvider {
                
    static abstract class FormParamInInvoker extends ResourceJavaMethodDispatcher {        
        final private List<Injectable> is;
        
        FormParamInInvoker(AbstractResourceMethod abstractResourceMethod, 
                List<Injectable> is) {
            super(abstractResourceMethod);
            this.is = is;
        }

        protected final Object[] getParams(HttpContext context) {
            Form form = context.getRequest().getEntity(Form.class);
            context.getProperties().put("com.sun.jersey.api.representation.form", form);
            
            final Object[] params = new Object[is.size()];
            try {
                int index = 0;
                for (Injectable i : is) {
                    params[index++] = i.getValue(context);                        
                }
                return params;
            } catch (WebApplicationException e) {
                throw e;
            } catch (RuntimeException e) {
                throw new ContainerException("Exception injecting parameters to Web resource method", e);
            }
        }        
    }
    
    static final class VoidOutInvoker extends FormParamInInvoker {
        VoidOutInvoker(AbstractResourceMethod abstractResourceMethod, List<Injectable> is) {
            super(abstractResourceMethod, is);
        }

        @SuppressWarnings("unchecked")
        public void _dispatch(Object resource, HttpContext context) 
        throws IllegalAccessException, InvocationTargetException {
            final Object[] params = getParams(context);
            method.invoke(resource, params);
        }
    }
    
    static final class TypeOutInvoker extends FormParamInInvoker {
        TypeOutInvoker(AbstractResourceMethod abstractResourceMethod, List<Injectable> is) {
            super(abstractResourceMethod, is);
        }

        @SuppressWarnings("unchecked")
        public void _dispatch(Object resource, HttpContext context)
        throws IllegalAccessException, InvocationTargetException {
            final Object[] params = getParams(context);
            
            Object o = method.invoke(resource, params);
            if (o != null) {
                MediaType mediaType = getAcceptableMediaType(context.getRequest());
                Response r = new ResponseBuilderImpl().status(200).entity(o).type(mediaType).build();
                context.getResponse().setResponse(r);
            }
        }
    }
    
    static final class ResponseOutInvoker extends FormParamInInvoker {
        ResponseOutInvoker(AbstractResourceMethod abstractResourceMethod, List<Injectable> is) {
            super(abstractResourceMethod, is);
        }

        @SuppressWarnings("unchecked")
        public void _dispatch(Object resource, HttpContext context)
        throws IllegalAccessException, InvocationTargetException {
            final Object[] params = getParams(context);

            Response r = (Response)method.invoke(resource, params);
            if (r != null) {
                MediaType mediaType = getAcceptableMediaType(context.getRequest());
                context.getResponse().setResponse(r, mediaType);
            }
        }
    }
    
    static final class ObjectOutInvoker extends FormParamInInvoker {
        ObjectOutInvoker(AbstractResourceMethod abstractResourceMethod, List<Injectable> is) {
            super(abstractResourceMethod, is);
        }

        public void _dispatch(Object resource, HttpContext context)
        throws IllegalAccessException, InvocationTargetException {
            final Object[] params = getParams(context);
            
            Object o = method.invoke(resource, params);
            
            MediaType mediaType = getAcceptableMediaType(context.getRequest());
            if (o instanceof Response) {
                Response r = (Response)o;
                context.getResponse().setResponse(r, mediaType);
            } else if (o != null) {
                Response r = new ResponseBuilderImpl().status(200).entity(o).type(mediaType).build();
                context.getResponse().setResponse(r);
            }            
        }
    }

    @Context InjectableProviderContext ipc;
    
    public RequestDispatcher create(AbstractResourceMethod abstractResourceMethod) {
        if ("GET".equals(abstractResourceMethod.getHttpMethod())) {
            return null;
        }
        
        List<Injectable> is = processParameters(abstractResourceMethod);
        if (is == null)
            return null;
        
        Class<?> returnType = abstractResourceMethod.getMethod().getReturnType();
        if (Response.class.isAssignableFrom(returnType)) {
            return new ResponseOutInvoker(abstractResourceMethod, is);                
        } else if (returnType != void.class) {
            if (returnType == Object.class) {
                return new ObjectOutInvoker(abstractResourceMethod, is);
            } else {
                return new TypeOutInvoker(abstractResourceMethod, is);
            }
        } else {
            return new VoidOutInvoker(abstractResourceMethod, is);
        }
    }
    
    private List<Injectable> processParameters(AbstractResourceMethod method) {        
        if (method.getParameters().isEmpty()) {
            return null;
        }
        
        boolean hasFormParam = false;
        for (int i = 0; i < method.getParameters().size(); i++) {
            Parameter parameter = method.getParameters().get(i);
            if (parameter.getAnnotation() != null)
                hasFormParam = parameter.getAnnotation().annotationType() == FormParam.class;            
        }
        if (!hasFormParam)
            return null;
        
        List<Injectable> is = new ArrayList<Injectable>(method.getParameters().size());
        for (int i = 0; i < method.getParameters().size(); i++) {
            Parameter parameter = method.getParameters().get(i);
            
            if (Parameter.Source.ENTITY == parameter.getSource()) {
                return null;
            }
            
            Injectable injectable = ipc.getInjectable(parameter);
            if (injectable == null)
                return null;
            
            is.add(injectable);
        }
        
        return is;
    }
}