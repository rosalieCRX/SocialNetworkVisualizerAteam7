/**
 * Filename: GraphADT.java Project: SocialNetworkVisualizerAteam7 Authors: ateam7 Due date: Dec 3
 * 11:59pm Other source credits: none Known bugs: none
 * 
 * Social network visualizer
 */

package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javafx.scene.canvas.GraphicsContext;


/**
 * Undirected graph implementation
 */
public class Graph implements GraphADT {

  private HashMap<String, GraphNode> allNodes; // a list of all graph nodes (i.e., vertices) in the
                                               // graph
  private Set<String> allPersons; // a Set that contains all the vertices in the graph

  /**
   * Default no-argument constructor
   */
  public Graph() {
    allNodes = new HashMap<String, GraphNode>();
    allPersons = new HashSet<String>();
  }

  /**
   * Private inner class representing a vertex (graph node) in the graph
   */
  private class GraphNode {
    String name; // person's name
    Person ps; // the person
    List<GraphNode> adjList; // successors
    

    /*
     * Constructor of the inner class
     */
    private GraphNode(String name) {
      this.name = name;
      this.ps = new Person(name);
      adjList = new ArrayList<GraphNode>();
    }
  }
  
  
  /**
   * Add new person to the graph.
   *
   * If name is null or already exists, method ends without adding a person or throwing an
   * exception. Method returns false.
   * 
   * Valid argument conditions: 1. name is non-null 2. person is not already in the graph
   * 
   * @param name the person to be added
   * @return true if the person is successfully added; false otherwise
   */
  @Override
  public boolean addNode(String name) {
    if (name != null && !allPersons.contains(name)) {
      GraphNode node = new GraphNode(name);
      allNodes.put(name, node);
      allPersons.add(name);
      return true; // person is successfully added
    }
    return false;
  }

  /**
   * Remove a person and all associated edges from the graph.
   * 
   * If name is null or the corresponding person does not exist, method ends without removing a
   * person, edges, or throwing an exception. Method returns false.
   * 
   * Valid argument conditions: 1. name is non-null 2. person is already in the graph
   * 
   * @param name the person to be removed
   * @return true if the person is successfully removed; false otherwise
   */
  @Override
  public boolean removeNode(String name) {
    if (name != null && allPersons.contains(name)) {
      int numEdges = allNodes.get(name).adjList.size(); // all outgoing edges from vertex
      ArrayList<String> vertices = new ArrayList<String>(allPersons);
      // remove all incoming edges to vertex
      for (int i = 0; i < vertices.size(); ++i) {
        if (allNodes.get(vertices.get(i)).adjList.contains(allNodes.get(name))) {
          allNodes.get(vertices.get(i)).adjList.remove(allNodes.get(name));
          numEdges -= 1;
        }
      }
      allNodes.remove(name);
      allPersons.remove(name);
      return true;
    }
    return false;
  }

  /**
   * Add the edge between name1 and name2 to this graph. (edge is undirected)
   * 
   * If either person does not exist, PERSON IS ADDED and then edge is created. No exception is
   * thrown. Method returns true.
   *
   * If the edge exists in the graph, no edge is added and no exception is thrown. Method returns
   * false.
   * 
   * Valid argument conditions: 1. neither person is null 2. both persons are in the graph 3. the
   * edge is not in the graph
   * 
   * @param name1 the first person
   * @param name2 the second person
   * @return true if edge successfully added; false otherwise
   */
  @Override
  public boolean addEdge(String name1, String name2) {
    if (name1 != null && name2 != null) {
      addNode(name1);
      addNode(name2);
      // if name2 is not already in the adjacency list of name1, or vice versa, then add them to
      // each other's list
      if (!allNodes.get(name1).adjList.contains(allNodes.get(name2))
          || !allNodes.get(name2).adjList.contains(allNodes.get(name1))) {
        // add name2 to the adjacency list of name1
        allNodes.get(name1).adjList.add(allNodes.get(name2));
        // add name1 to the adjacency list of name2
        allNodes.get(name2).adjList.add(allNodes.get(name1));
        return true;
      }
    }
    return false;
  }

  /**
   * Remove the edge between name1 and name2 from this graph. (edge is undirected) If either person
   * does not exist, or if an edge between name1 and name2 does not exist, no edge is removed and no
   * exception is thrown. Method returns false.
   * 
   * Valid argument conditions: 1. neither person is null 2. both persons are in the graph 3. the
   * edge name1 and name2 is in the graph
   * 
   * @param name1 the first person
   * @param name2 the second person
   * @return true if edge is successfully removed; false otherwise
   */
  @Override
  public boolean removeEdge(String name1, String name2) {
    if (name1 != null && name2 != null && allPersons.contains(name1)
        && allPersons.contains(name2)) { // if neither name is null and both are in the graph
      GraphNode node2 = allNodes.get(name2);
      GraphNode node1 = allNodes.get(name1);
      if (node1.adjList.contains(node2)) { // if name2 is name1's successor
        node1.adjList.remove(node2); // remove name2 from name1's list of successors
        return true;
      }
      if (node2.adjList.contains(node1)) { // if name1 is name2's successor
        node2.adjList.remove(node1); // remove name1 from name2's list of successors
        return true;
      }
    }
    return false;
  }

  /**
   * Returns a Set that contains all the persons
   * 
   * @return a Set<Person> which contains all the persons in the graph
   */
  @Override
  public Set<Person> getAllNodes() {
    Set<Person> persons = new HashSet<Person>();
    for (String name : allPersons) {
      persons.add(allNodes.get(name).ps);
    }
    return persons;
  }

  /**
   * Get all the neighbor (adjacent-dependencies) of a person
   * 
   * @param name the specified person
   * @return an Set<Person> of all the adjacent vertices for specified person
   */
  @Override
  public Set<Person> getNeighbors(String name) {
    Set<Person> neighbors = new HashSet<Person>();
    GraphNode node = allNodes.get(name); // find the graph node given the string key
    for (int i = 0; i < node.adjList.size(); ++i) { // add node's successors to its neighbor list
      neighbors.add(node.adjList.get(i).ps);
    }
    Iterator<String> persons = allPersons.iterator();
    if (persons.hasNext()) { // add node's predecessors to its neighbor list
      String neighborName = persons.next();
      if (allNodes.get(neighborName).adjList.contains(allNodes.get(name))) {
        neighbors.add(allNodes.get(neighborName).ps);
      }
    }
    return neighbors;
  }
  
  /**
   * Get the person given his/her name
   * 
   * @param name the name of the person of interest
   * @return the person
   */
  @Override
  public Person getNode(String name) {    
    return allNodes.get(name).ps;
  }

}
