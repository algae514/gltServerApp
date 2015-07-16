package com.utils;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author toshiba
 */
public class OrientDBUtil {

    /**
     * RUN BELOW COMMANDS BEFORE SETTING UP
     *
     *
     * alter database custom useLightweightEdges=false
     *
     * alter database custom useClassForEdgeLabel=false
     *
     * alter database custom useClassForVertexLabel=false
     *
     * alter database custom useVertexFieldsForEdgeLabels=false
     *
     */
    public final static OrientGraphFactory factory = new OrientGraphFactory(GLTConstants.dbURL2).setupPool(1, 100);

    public static OrientGraph getGraph() {
        return factory.getTx();
    }

    public static Edge addEdgeToNode(String edgeName, Vertex fromVertex, Vertex toVertex) {
//        OrientGraphFactory factory = new OrientGraphFactory(Constants.dbURL).setupPool(1, 100);
        OrientGraphFactory factory = new OrientGraphFactory(GLTConstants.dbURL2).setupPool(1, 100);
        OrientGraph graph = factory.getTx();
//        OrientGraph graph = OrientDBUtil.getGraph();
        Edge newEdge = null;
        try {
//            System.out.println("edgeName = " + edgeName);
//            System.out.println("fromVertex = " + fromVertex);
//            System.out.println("toVertex = " + toVertex);
            boolean exists = factory.exists();
            int availableInstancesInPool = factory.getAvailableInstancesInPool();
            ODatabaseDocumentTx database = factory.getDatabase();
//            System.out.println("database = " + database);
//
//            System.out.println("availableInstancesInPool = " + availableInstancesInPool);
//            System.out.println("exists = " + exists);
            if (graph.isClosed()) {
                graph = factory.getTx();
                System.out.println(" GRAH IS CLOSED");

            }
            newEdge = graph.addEdge(null, fromVertex, toVertex, edgeName);
            graph.commit();
//            System.out.println("Edge Id = " + newEdge.getId());
        } catch (Exception e) {
            System.out.println(" Transanction failed, rollingback !!!");
            graph.rollback();
            e.printStackTrace();
        } finally {
            graph.shutdown();
        }
        System.out.println("newEdge = " + newEdge);
        return newEdge;
    }

    public static void getAllEdges() {
        OrientGraphFactory factory = new OrientGraphFactory(GLTConstants.dbURL2).setupPool(1, 100);
        OrientGraph graph = factory.getTx();
        Object id = graph.getVertex("#12:04").getEdges(Direction.IN, "hasTop");
        System.out.println("id = " + id);

        GremlinPipeline pipe = new GremlinPipeline();
        GremlinPipeline property = pipe.start(graph.getVertex(1)).outV();
        property.iterate();
        System.out.println("property = " + property);
    }

    /**
     *
     * @param fromvert
     * @param type
     * @param edgeName
     * @param depth
     */
    public static List<Vertex> getChildren(Vertex fromvert, String edgeName) {
//        System.out.println("\n Getting immedieate Vertices \nType:" + fromvert + "\nedgeLabel:" + edgeName + "\n\n");
        List<Vertex> list = new ArrayList<Vertex>();
        OrientGraph graph = OrientDBUtil.getGraph();
        GremlinPipeline pipe = new GremlinPipeline();
        GremlinPipeline property = pipe.start(fromvert).outE(edgeName).cast(Edge.class).inV().cast(Vertex.class);
        while (property.hasNext()) {
            Vertex next = (Vertex) property.next();
//            System.out.println("next = " + next);
            list.add(next);
            // RMS
        }
        return list;
    }

    /**
     *
     * @param fromvert
     * @param edgeName
     * @param key
     * @param value
     * @return
     */
    public static List<Vertex> getChildrenByProperty(Vertex fromvert, String edgeName, String key, Object value) {
//        System.out.println("\n Getting immedieate Vertices \nType:" + fromvert + "\nedgeLabel:" + edgeName + "\n\n");
        List<Vertex> list = new ArrayList<Vertex>();
        OrientGraph graph = OrientDBUtil.getGraph();
        GremlinPipeline pipe = new GremlinPipeline();
        GremlinPipeline property = pipe.start(fromvert).outE(edgeName).cast(Edge.class).inV().has(key, value).cast(Vertex.class);
        while (property.hasNext()) {
            Vertex next = (Vertex) property.next();
//            System.out.println("next = " + next);
            list.add(next);
            // RMS
        }
        return list;
    }

    public static List<Vertex> getAllParents(Vertex childVertex) {
        Vertex parent = null;
//        System.out.println("\n Getting immedieate Vertices \nType:" + childVertex + "\n\n");
        List<Vertex> list = new ArrayList<Vertex>();
        OrientGraph graph = OrientDBUtil.getGraph();
        GremlinPipeline pipe = new GremlinPipeline();
        GremlinPipeline property = pipe.start(childVertex).inE().cast(Edge.class).outV().cast(Vertex.class);
        while (property.hasNext()) {
            parent = (Vertex) property.next();
            list.add(parent);
        }
        return list;

    }




    public static List<Vertex> getAllParents(Vertex childVertex, String edge) {
            Vertex parent = null;
//        System.out.println("\n Getting immedieate Vertices \nType:" + childVertex + "\n\n");
        List<Vertex> list = new ArrayList<Vertex>();
        OrientGraph graph = OrientDBUtil.getGraph();
        GremlinPipeline pipe = new GremlinPipeline();
        GremlinPipeline property = pipe.start(childVertex).inE(edge).cast(Edge.class).outV().cast(Vertex.class);
        while (property.hasNext()) {
            parent = (Vertex) property.next();
            list.add(parent);
        }
        return list;

    }




    public static Vertex getParentTillTypeMatch(Vertex childVertex, String type) {
        Vertex parent = null;

        System.out.println("---------------------------------------------------------------\n Getting Parent Vertices \nType:" + childVertex + "\n\n");


        List<Vertex> list = new ArrayList<Vertex>();

//        OrientGraphFactory factory = new OrientGraphFactory(Constants.dbURL).setupPool(1, 100);
//        OrientGraph graph = factory.getTx();

        GremlinPipeline pipe = new GremlinPipeline();
        GremlinPipeline property = pipe.start(childVertex).inE().cast(Edge.class).outV().cast(Vertex.class);
        System.out.println("property = " + property.size());

        while (property.hasNext()) {
            Vertex next = (Vertex) property.next();
//            int frm = next.toString().indexOf("(");
//            int to = next.toString().indexOf(")");
//            String typeOfPar = next.toString().substring(frm + 1, to);
//            System.out.println("property = " + typeOfPar);
            System.out.println("next = " + next);
            String typeOfPar = getTypeOfVertex(next);
            System.out.println("typeOfPar = " + typeOfPar);

            if (typeOfPar.equals(type)) {
                System.out.println("SAME TYPE property = " + typeOfPar);
                parent = next;
            } else {
                property = pipe.start(childVertex).inE().cast(Edge.class).outV().cast(Vertex.class);
                System.out.println(" GOING TO ITS PARENT");
            }

        }
        return parent;

    }

    public static void printVertex(Vertex vertex) {
    	if(vertex==null){
    		System.out.println("-----------PRINTING VERTEX " + vertex + " --------------------");
    		System.out.println("NULL");
    		System.out.println(" ------------------ VERTEX PRINTED ------------------");

    		return;
    	}

        Set<String> propertyKeys = vertex.getPropertyKeys();
        System.out.println("-----------PRINTING VERTEX " + vertex + " --------------------");
        for (Iterator<String> it = propertyKeys.iterator(); it.hasNext();) {
            String string = it.next();
            System.out.println(string + " = " + vertex.getProperty(string));
        }
        System.out.println(" ------------------ VERTEX PRINTED ------------------");
    }

    public static Vertex UpdateVertex(Vertex vertexToBeUpdated, HashMap<String, Object> newValuesMap) {
                OrientGraphFactory factory = new OrientGraphFactory(GLTConstants.dbURL2).setupPool(1, 100);
        OrientGraph graph = factory.getTx();


        for (Map.Entry<String, Object> entry : newValuesMap.entrySet()) {
            String string = entry.getKey();
            Object object = entry.getValue();
            vertexToBeUpdated.setProperty(string, object);

        }
        graph.commit();
        return vertexToBeUpdated;
    }

    public static void deleteVertex(OrientVertex vert) {
        OrientGraph graph = OrientDBUtil.getGraph();
        System.out.println(" REMOVING VERTEX ");
        printVertex(vert);
        graph.removeVertex(vert);
        graph.commit();
        System.out.println(" VERTEX REMOVED" + vert);
    }

    public static Vertex getVertexByUID(String key, Object value) {
//        System.out.println("value = " + value);
//        System.out.println("key = " + key);
        Iterable<Vertex> vertices = getGraph().getVertices(key, value);

        Iterator<Vertex> it = vertices.iterator();

        System.out.println(" vertices.iterator() "+(it.hasNext()));

        if(vertices.iterator().hasNext())
        return vertices.iterator().next();
        else
        	return null;
    }

    public static List<Vertex> getAllChildernByType(Vertex fromvertex, String type) {
        List<Vertex> lst;
        lst = new ArrayList<Vertex>();
        lst.addAll(exploreVertices(fromvertex, type));
        return lst;
    }

    public static String getTypeOfVertex(Vertex vertex) {
        int frm = vertex.toString().indexOf("(");
        int to = vertex.toString().indexOf(")");
        return vertex.toString().substring(frm + 1, to);
    }

    public static Set<Vertex> exploreVertices(Vertex parent, String typeOfVertex) {
//        System.out.println("parent = " + parent);
//        System.out.println(" ------------------- EXPLAORING -----------------------");

        Set<Vertex> collectedVertices = new HashSet<Vertex>();
        List<Vertex> children = getChildren((OrientVertex) parent);

        for (int i = 0; i < children.size(); i++) {
            Vertex next = children.get(i);
            Set<Vertex> exploreVertices = exploreVertices(next, typeOfVertex);
            collectedVertices.addAll(exploreVertices);

            if (getTypeOfVertex(next).equals(typeOfVertex)) {
//                System.out.println(" MATCHING AND ADDING " + next);
                collectedVertices.add(next);
            }
        }


        return collectedVertices;
    }

    public static List<Vertex> getChildren(OrientVertex get) {
        OrientGraphFactory factory = new OrientGraphFactory(GLTConstants.dbURL2).setupPool(1, 100);
        OrientGraph graph = factory.getTx();
        List<Vertex> list = new ArrayList<Vertex>();

        try {
//            System.out.println("\n Getting immedieate Vertices \nType:" + get + "\n\n\n");
//        OrientGraph graph = OrientDBUtil.getGraph();
            GremlinPipeline pipe = new GremlinPipeline();
            GremlinPipeline property = pipe.start(get).outE().cast(Edge.class).inV().cast(Vertex.class);
            while (property.hasNext()) {
                Vertex next = (Vertex) property.next();
//            System.out.println("next = " + next);
                list.add(next);
                // RMS
            }

        } catch (Exception e) {
            System.out.println(" Transanction failed, rollingback !!!");
            graph.rollback();
            e.printStackTrace();
        } finally {
            graph.shutdown();
        }

        return list;

    }

    public static List<Edge> getAllEdges(Vertex get) {
        OrientGraphFactory factory = new OrientGraphFactory(GLTConstants.dbURL2).setupPool(1, 100);
        OrientGraph graph = factory.getTx();
        List<Edge> list = new ArrayList<Edge>();

        try {
//            System.out.println("\n Getting immedieate Vertices \nType:" + get + "\n\n\n");
//        OrientGraph graph = OrientDBUtil.getGraph();
            GremlinPipeline pipe = new GremlinPipeline();
            GremlinPipeline property = pipe.start(get).bothE().cast(Edge.class);
            while (property.hasNext()) {
                Edge next = (Edge) property.next();
//            System.out.println("next = " + next);
                list.add(next);
                // RMS
            }

        } catch (Exception e) {
            System.out.println(" Transanction failed, rollingback !!!");
            graph.rollback();
            e.printStackTrace();
        } finally {
//            graph.shutdown();
        }

        return list;

    }

    public static Vertex getParentOfTypeMatch(Vertex childVertex, String answer) {

        OrientGraphFactory factory = new OrientGraphFactory(GLTConstants.dbURL2).setupPool(1, 100);
        OrientGraph graph = factory.getTx();

        try {
//            System.out.println("\n Getting immedieate Vertices \nType:" + get + "\n\n\n");
//        OrientGraph graph = OrientDBUtil.getGraph();
            GremlinPipeline pipe = new GremlinPipeline();
            GremlinPipeline property = pipe.start(childVertex).inE().cast(Edge.class).outV().cast(Vertex.class);
            System.out.println("property = " + property.size());


            while (property.hasNext()) {
            	System.out.println(" in while");
                Vertex next = (Vertex) property.next();
                OrientDBUtil.printVertex(next);
                String typeOfVertex = getTypeOfVertex(next);
                if (typeOfVertex.equalsIgnoreCase(answer)) {
                    return next;
                }
            }

        } catch (Exception e) {
            System.out.println(" Transanction failed, rollingback !!!");
            graph.rollback();
            e.printStackTrace();
        } finally {
//            graph.shutdown();
        }

        return null;

    }


	public static void printAllEdges(OrientVertex vertex) {
		// TODO Auto-generated method stub

        System.out.println("\n\n\n\n PRINTING ALL EDGES OF  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+vertex);
        List<Edge> allEdges = OrientDBUtil.getAllEdges(vertex);
        for (Iterator iterator = allEdges.iterator(); iterator.hasNext();) {
			Edge edge = (Edge) iterator.next();
			System.out.println(edge);
		}

        System.out.println("\n\n\n\n PRINTING ALL EDGES COMPLETED >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+vertex);

	}

	public static List<Vertex> getParentbyEdge(OrientVertex fromVertex,String edgeName) {
		List<Vertex> list = new ArrayList<Vertex>();
        OrientGraph graph = OrientDBUtil.getGraph();
        GremlinPipeline pipe = new GremlinPipeline();
        GremlinPipeline property = pipe.start(fromVertex).inE(edgeName).cast(Edge.class).outV().cast(Vertex.class);
        while (property.hasNext()) {
            Vertex next = (Vertex) property.next();
//            System.out.println("next = " + next);
            list.add(next);
            // RMS
        }
        return list;
	}

	public static int getChildrenVerticesCount(Vertex fromvert, String edgeName) {
		// TODO Auto-generated method stub
	    List<Vertex> list = new ArrayList<Vertex>();
        OrientGraph graph = OrientDBUtil.getGraph();
        GremlinPipeline pipe = new GremlinPipeline();
        int count = pipe.start(fromvert).outE(edgeName).cast(Edge.class).inV().cast(Vertex.class).size();
        return count;
	}

    public OrientVertex getVertexByID(String string) {
        return getGraph().getVertex(string);
        
    }


}