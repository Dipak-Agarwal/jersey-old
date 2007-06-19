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

package com.sun.ws.rest.samples.atomserver;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class Main {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        if (args.length == 2) {
            // <"GET" | "DELETE" | "PUT"> <URI>
            String uri = args[1];
            if (args[0].equalsIgnoreCase("GET")) {
                // Collection operations
                // Get all collections
                byte[] content = get(uri);
                System.out.println(new String(content));
            } else if (args[0].equalsIgnoreCase("DELETE")) {
                // Collection operation
                // Delete a collection
                delete(uri);
            } else if (args[0].equalsIgnoreCase("PUT")) {
                // Collection operation
                // Create a collection (if not already created)
                put(uri);
            } else {
                throw new IllegalArgumentException();
            }
        } else if (args.length == 3) {
            // PUT <URI> <mediaType>
            String uri = args[1];
            if (args[0].equalsIgnoreCase("PUT")) {
                // Item operation
                // Create an item, or update if already created
                put(uri, args[2], new BufferedInputStream(System.in));                
            } else if (args[0].equalsIgnoreCase("POST")) {
                post(uri, args[2], new BufferedInputStream(System.in));                
            } else {
                throw new IllegalArgumentException();
            }
        } else if (args.length == 4) {
            // PUT <URI> <mediaType> <file>
            String uri = args[1];
            if (args[0].equalsIgnoreCase("PUT")) {
                // Item operation
                // Create an item, or update if already created
                put(uri, args[2], new BufferedInputStream(new FileInputStream(args[3])));                
            } else if (args[0].equalsIgnoreCase("POST")) {
                post(uri, args[2], new BufferedInputStream(new FileInputStream(args[3])));                
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    private void printUsage() {
        
    }
    
    private static byte[] get(String uri) throws IOException {
        URL u = new URL(uri);
        HttpURLConnection uc = (HttpURLConnection)u.openConnection();
        uc.setRequestMethod("GET");
        
        int status = uc.getResponseCode();
        String mediaType = uc.getContentType();
        
        InputStream in = uc.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int r;
        while ((r = in.read(buffer)) != -1) {
            baos.write(buffer, 0, r);
        }
        
        return baos.toByteArray();        
    }    
    
    private static void put(String uri) throws IOException {
        URL u = new URL(uri);
        HttpURLConnection uc = (HttpURLConnection)u.openConnection();
        uc.setRequestMethod("PUT");
        
        int status = uc.getResponseCode();
        System.out.println("Status: " + status);
    }    
    
    private static void put(String uri, String mediaType, InputStream in) throws IOException {
        URL u = new URL(uri);
        HttpURLConnection uc = (HttpURLConnection)u.openConnection();
        uc.setRequestMethod("PUT");
        uc.setRequestProperty("Content-Type", mediaType);        
        uc.setDoOutput(true);
        
        OutputStream out = uc.getOutputStream();
        
        byte[] data = new byte[2048];
        int read;
        while ((read = in.read(data)) != -1)
            out.write(data, 0, read);
        out.close();
        
        int status = uc.getResponseCode();
        System.out.println("Status: " + status);
    }    
    
    private static void post(String uri, String mediaType, InputStream in) throws IOException {
        URL u = new URL(uri);
        HttpURLConnection uc = (HttpURLConnection)u.openConnection();
        uc.setRequestMethod("POST");
        uc.setRequestProperty("Content-Type", mediaType);        
        uc.setDoOutput(true);
        
        OutputStream out = uc.getOutputStream();
        
        byte[] data = new byte[2048];
        int read;
        while ((read = in.read(data)) != -1)
            out.write(data, 0, read);
        out.close();
        
        int status = uc.getResponseCode();
        System.out.println("Status: " + status);
    }    
    
    private static void delete(String uri) throws IOException {
        URL u = new URL(uri);
        HttpURLConnection uc = (HttpURLConnection)u.openConnection();
        uc.setRequestMethod("DELETE");
        
        int status = uc.getResponseCode();
        System.out.println("Status: " + status);
    }    
}
