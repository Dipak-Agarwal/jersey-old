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

package com.sun.ws.rest.impl.subresources;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.SubResources;
import javax.ws.rs.UriParam;
import javax.ws.rs.UriTemplate;
import com.sun.ws.rest.impl.bean.*;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class SubResourceStaticTest extends AbstractBeanTester {
    
    public SubResourceStaticTest(String testName) {
        super(testName);
    }

    @UriTemplate("/parent")
    @SubResources({Child.class})
    static public class Parent { 
        @HttpMethod
        public String getMe() {
            return "parent";
        }
    }
    
    @UriTemplate("child")
    static public class Child { 
        @HttpMethod
        public String getMe() {
            return "child";
        }
    }
    
    public void testSubResourceStatic() {
        String content;
        
        content = (String)callGet(Parent.class, "/parent", "").
                getResponse().getEntity();
        assertEquals("parent", content);
        content = (String)callGet(Parent.class, "/parent/child", "").
                getResponse().getEntity();
        assertEquals("child", content);
    }    
    
    @UriTemplate("/{p}")
    @SubResources({ChildWithTemplates.class})
    static public class ParentWithTemplates { 
        @HttpMethod
        public String getMe(@UriParam("p") String p) {
            return p;
        }
    }
    
    @UriTemplate("child/{c}")
    static public class ChildWithTemplates { 
        @HttpMethod
        public String getMe(@UriParam("c") String c) {
            return c;
        }
    }
    
    public void testSubResourceStaticWithTemplates() {
        String content;
        
        content = (String)callGet(ParentWithTemplates.class, "/parent", "").
                getResponse().getEntity();
        assertEquals("parent", content);
        content = (String)callGet(ParentWithTemplates.class, "/parent/child/first", "").
                getResponse().getEntity();
        assertEquals("first", content);
    }    
}
