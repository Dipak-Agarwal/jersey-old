/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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
package com.sun.jersey.impl.http.header;

import com.sun.jersey.core.header.ContentDisposition;
import com.sun.jersey.core.header.HttpDateFormat;
import com.sun.jersey.core.header.reader.HttpHeaderReader;
import java.text.ParseException;
import java.util.Date;
import junit.framework.TestCase;

/**
 *
 * @author Imran@SmartITEngineering.Com
 */
public class ContentDispositionTest extends TestCase {

    protected String contentDispositionType;

    public ContentDispositionTest(String testName) {
        super(testName);
        contentDispositionType = "inline";
    }

    public void testCreate() {
        System.out.println("create");
        ContentDisposition contentDisposition = ContentDisposition.type(null).
            build();
        assertNotNull(contentDisposition);
        assertEquals(null, contentDisposition.getType());
        contentDisposition = ContentDisposition.type(contentDispositionType).
            build();
        assertNotNull(contentDisposition);
        assertEquals(contentDispositionType, contentDisposition.getType());
        Date date = new Date();
        contentDisposition = ContentDisposition.type(contentDispositionType).
            fileName(
            "test.file").creationDate(date).modificationDate(date).readDate(date).
            size(1222).build();
        assertContentDisposition(contentDisposition, date);
        String header = new StringBuilder(contentDispositionType).toString();
        try {
            contentDisposition = new ContentDisposition(contentDisposition.
                toString());
            contentDisposition = new ContentDisposition(header);
            assertNotNull(contentDisposition);
            assertEquals(contentDispositionType, contentDisposition.getType());
            String dateString = HttpDateFormat.getPreferedDateFormat().format(
                date);
            header = new StringBuilder(contentDispositionType).append(
                ";filename=\"test.file\";creation-date=\"").append(
                dateString).append("\";modification-date=\"").append(dateString).
                append("\";read-date=\"").append(dateString).append(
                "\";size=1222").toString();
            contentDisposition = new ContentDisposition(header);
            assertContentDisposition(contentDisposition, date);
            contentDisposition = new ContentDisposition(HttpHeaderReader.
                newInstance(header));
            assertContentDisposition(contentDisposition, date);
        }
        catch (ParseException ex) {
            fail(ex.getMessage());
        }
        try {
            contentDisposition = new ContentDisposition((HttpHeaderReader) null);
        }
        catch (ParseException exception) {
            fail(exception.getMessage());
        }
        catch (NullPointerException exception) {
            //expected
        }
    }

    public void testToString() {
        Date date = new Date();
        ContentDisposition contentDisposition = ContentDisposition.type(
            contentDispositionType).
            fileName("test.file").creationDate(date).modificationDate(date).
            readDate(date).size(1222).build();
        String dateString = HttpDateFormat.getPreferedDateFormat().format(
            date);
        String header = new StringBuilder(contentDispositionType).append(
            "; filename=\"test.file\"; creation-date=\"").append(
            dateString).append("\"; modification-date=\"").append(dateString).
            append("\"; read-date=\"").append(dateString).append(
            "\"; size=1222").toString();
        assertEquals(header, contentDisposition.toString());
    }

    protected void assertContentDisposition(
        ContentDisposition contentDisposition,
        Date date) {
        assertNotNull(contentDisposition);
        assertEquals(contentDispositionType, contentDisposition.getType());
        assertEquals("test.file", contentDisposition.getFileName());
        assertEquals(date.toString(), contentDisposition.getModificationDate().
            toString());
        assertEquals(date.toString(),
            contentDisposition.getReadDate().toString());
        assertEquals(date.toString(), contentDisposition.getCreationDate().
            toString());
        assertEquals(1222, contentDisposition.getSize());
    }
}