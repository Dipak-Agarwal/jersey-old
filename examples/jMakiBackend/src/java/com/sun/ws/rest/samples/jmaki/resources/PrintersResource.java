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
package com.sun.ws.rest.samples.jmaki.resources;

import com.sun.ws.rest.samples.jmaki.beans.Printer;
import com.sun.ws.rest.samples.jmaki.beans.PrinterTableModel;
import com.sun.ws.rest.samples.jmaki.beans.TreeModel;
import com.sun.ws.rest.spi.resource.Singleton;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.ws.rs.ConsumeMime;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.ProduceMime;

/**
 *
 * @author japod
 */
@Singleton
@Path("/printers")
public class PrintersResource {

    Map<String, Printer> printers;

    @Path("/jMakiTable")
    @GET
    @ProduceMime("application/json")
    public PrinterTableModel getTable() {
        return new PrinterTableModel(getPrinters().values());
    }

    @Path("/jMakiTree")
    @GET
    @ProduceMime("application/json")
    public TreeModel getTree() {
        TreeModel model = new TreeModel();
        model.root = new TreeModel.Node("printers");
        if (!getPrinters().isEmpty()) {
            model.root.children = new LinkedList<TreeModel.Node>();
            model.root.expanded = true;
            Map<String, TreeModel.Node> byModel = new HashMap<String, TreeModel.Node>();
            for (Printer p : getPrinters().values()) {
                if (null != p.model) {
                    if (!byModel.containsKey(p.model)) {
                        TreeModel.Node newModelNode = new TreeModel.Node("Model " + p.model);
                        newModelNode.children = new LinkedList<TreeModel.Node>();
                        newModelNode.expanded = true;
                        model.root.children.add(newModelNode);
                        byModel.put(p.model, newModelNode);
                    }
                    byModel.get(p.model).children.add(new TreeModel.Node(p.id + " @ " + p.location));
                }
            }
        }
        return model;
    }

    @Path("/ids/{printerid}")
    @GET
    @ProduceMime({"application/json", "application/xml"})
    public Printer getPrinter(
            @PathParam("printerid") String printerId) {
        return getPrinters().get(printerId);
    }

    @Path("/ids/{printerid}")
    @PUT
    @ConsumeMime({"application/json", "application/xml"})
    public void putPrinter(
            @PathParam("printerid") String printerId,  Printer printer) {
        getPrinters().put(printerId, printer);
    }

    @Path("/ids/{printerid}")
    @DELETE
    public void deletePrinter(
            @PathParam("printerid") String printerId) {
        getPrinters().remove(printerId);
    }

    public Map<String, Printer> getPrinters() {
        if (null == printers) {
            printers = new HashMap<String, Printer>();
            printers.put("P01", new Printer("P01", "OKI123", "lpd://p01", "room 1"));
            printers.put("P02", new Printer("P02", "OKI123", "lpd://p02", "room 12"));
            printers.put("P03", new Printer("P03", "OKI123", "lpd://p03", "room 133"));
            printers.put("P05", new Printer("P05", "Xerox345", "lpd://p05", "room 543"));
            printers.put("P06", new Printer("P06", "Xerox345", "lpd://p06", "room 203"));
        }
        return printers;
    }
}