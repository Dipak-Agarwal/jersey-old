/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * http://www.opensource.org/licenses/cddl1.php
 * See the License for the specific language governing
 * permissions and limitations under the License.
 */

/*
 * AbstractSubResourceMethod.java
 *
 * Created on October 5, 2007, 1:34 PM
 *
 */
package com.sun.ws.rest.api.model;

import java.lang.reflect.Method;
import java.util.List;

/**
 *
 * @author mh124079
 */
public class AbstractSubResourceMethod extends AbstractResourceMethod implements UriTemplated {

    private UriTemplateValue uriTemplate;

    public AbstractSubResourceMethod(Method method, UriTemplateValue uriTemplate, String httpMethod) {
        super(method, httpMethod);
        this.uriTemplate = uriTemplate;
    }

    public UriTemplateValue getUriTemplate() {
        return uriTemplate;
    }

    @Override
    public void accept(AbstractModelVisitor visitor) {
        visitor.visitAbstractSubResourceMethod(this);
    }
    
    @Override
    public String toString() {
        return "AbstractSubResourceMethod(" 
                + getMethod().getDeclaringClass().getSimpleName() + "#" + getMethod().getName() + ")";
    }
}
