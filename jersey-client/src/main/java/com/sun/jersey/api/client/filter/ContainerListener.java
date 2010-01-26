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

package com.sun.jersey.api.client.filter;

/**
 * ContainerListener abstract class
 *
 * @author pavel.bucek@sun.com
 */
public abstract class ContainerListener {

    /**
     * Called when any amount of bytes from request entity is written (sent).
     *
     * @param bytes sum of send bytes
     */
    public void onSent(long delta, long bytes) {};

    /**
     * Called when receive starts.
     *
     * @param totalBytes entity size in response (taken from http header
     * "Content-Length"; set to -1 if it's missing)
     */
    public void onReceiveStart(long totalBytes) {};

    /**
     * Called when any amount of bytes is read from responses entity
     *
     * @param bytes sum of received bytes
     */
    public void onReceived(long delta, long bytes) {};

    /**
     * Called when last byte of response entity is read or (if entity is not
     * present in response) after sending request.
     *
     */
    public void onFinish() {};

}
