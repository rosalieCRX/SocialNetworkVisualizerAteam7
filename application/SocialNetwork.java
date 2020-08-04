package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

/**
 * Implements the interaction of an user on a social network system
 * 
 * @author rosaliecarrow
 *
 */
public class SocialNetwork implements SocialNetworkADT {
  // stores the relationships between users
  private Graph graph = new Graph();

  /**
   * add two person as friends
   * 
   * @param person1 friend 1
   * @param person2 friend 2
   * @return true if successful, false if failed
   */
  @Override
  public void addFriends(String person1, String person2) {
    graph.addEdge(person1, person2);
  }

  /**
   * remove two person who are friends
   * 
   * @param person1 friend 1
   * @param person2 friend 2
   * @return true if successful, false if failed
   */
  @Override
  public void removeFriends(String person1, String person2) {
    graph.removeEdge(person1,person2);
  }

  /**
   * add an user
   * 
   * @param person user to add
   * @return true if successful, false if failed
   */
  @Override
  public void addUser(String person) {
    graph.addNode(person);
  }

  /**
   * remove an user
   * 
   * @param person user to remove
   * @return true if successful, false if failed
   */
  @Override
  public void removeUser(String person) {
   graph.removeNode(person);
  }

  /**
   * get all friends of an user
   * 
   * @param person the user focused on
   * @return a set of the user's friends
   */
  @Override
  public Set<Person> getFriends(String person) {
    return graph.getNeighbors(person);
  }

  /**
   * find the mutual friend between two people
   * 
   * @param person1 friend 1
   * @param person2 friend 2
   * @return a set of their mutual friends
   */
  @Override
  public Set<Person> getMutualFriends(String person1, String person2) {
    Set<Person> mutualFriends = this.getFriends(person1);//get all friends of the first person
    for(Person x: this.getFriends(person2)) {
      if(!mutualFriends.contains(x)) {
        mutualFriends.remove(x);
      }
    }//remove all friends of person 2 who are not firends with person 1
    
    return mutualFriends;
  }

  /**
   * inner class to make 
   * 
   * @author XAZ
   *
   */
  private class P implements Comparable<P>{
    int totalWeight;
    Person key;
    
    public P(Person key, int totalWeight) {
      this.totalWeight = totalWeight;
      this.key = key;
    }
    
    @Override
    public int compareTo(P o) {
      return (this.totalWeight - o.totalWeight);
    }
  }
  
  /**
   * find the shortest path in friendship
   * 
   * @param person1
   * @param person2
   * @return
   */
  @Override
  public List<Person> getShortestPath(String person1, String person2) {
//    Map<Person,Person> pathAtPerson1 = new HashMap();
//    PriorityQueue<Person> pq = new PriorityQueue();
//  //get an adjacent list
//    Set<Person> allPerson = graph.getAllNodes();
//    Map<String, Set<Person>> adjList = new HashMap();
//    for(Person x:allPerson) {
//      adjList.put(x.getName(), graph.getNeighbors(x.getName()));
//    }
    
    
    Map<Person, Boolean> visited = new HashMap<>();
    Map<Person, Integer> totalWeight = new HashMap<>();
    Map<Person, Person> predecessor = new HashMap<>();
    
    // set unvisited, total weight to infinity, predecessor to null
    for (Person temp : graph.getAllNodes()) {
      visited.put(temp, false);
      totalWeight.put(temp, Integer.MAX_VALUE);
      predecessor.put(temp, null);
    }
    
    // set start vertex's total weight to 0
    totalWeight.put(graph.getNode(person1), 0);
    
    PriorityQueue<P> pq = new PriorityQueue<>();    
    // add start vertex to pq
    pq.add(new P(graph.getNode(person1), totalWeight.get(graph.getNode(person1)))); 
    
    while(! pq.isEmpty()) {
      P c = pq.remove();
      visited.put(c.key, true);
      
      for (Person temp : graph.getNeighbors(c.key.getName())) {
        if (totalWeight.get(temp) > (totalWeight.get(c.key) + 1)) {
          totalWeight.put(temp, (totalWeight.get(c.key) + 1));
          
          if (!pq.contains(temp)) {
            predecessor.put(temp, c.key);
            pq.add(new P(temp, totalWeight.get(temp)));            
          }
        }
      }            
    }
    
    
    List<Person> shortestPath = new LinkedList<Person>();
    shortestPath.add(graph.getNode(person2));
    Person pred = predecessor.get(graph.getNode(person2));
    // find the shortest path from the end to start
    while(!pred.getName().equals(person1)) {
      shortestPath.add(pred);
      // check if some node is reachable
      if ((predecessor.get(pred) == null) && (!pred.getName().equals(person1))) { 
        return null; // person2 is unreachable
      }
      pred = predecessor.get(pred);
    }
    
    Collections.reverse(shortestPath);
    return shortestPath;
  }
  
  
  /**
   * Get all connected users
   * 
   * @return a set of connected users
   */
  @Override
  public Set<Graph> getConnectedComponents() {
    Set<Graph> connectedComponents = new HashSet<>();
    
//    for (Person temp: graph.getAllNodes()) {
//      List<Person> withinGraph = new ArrayList<>();
//      withinGraph.add(temp);
//      for (Person t:graph.getAllNodes()) {
//        if (getShortestPath(temp.getName(), t.getName()) != null) { // reachable
//          withinGraph.add(t);
//        }
//      }
//      
//      // make a new graph based on the withinGraph list
//      Graph g1 = new Graph();
//      for (Person tp: withinGraph) {
//        g1.addNode(tp.getName()); // add itself to the graph
//        
//        // add edges
//        for (Person nb: graph.getNeighbors(tp.getName())) {
//          g1.addEdge(tp.getName(), nb.getName());
//        }
//      }
//      
//      // add g1 to connectedComponents
//      connectedComponents.add(g1);
//    }
    
    HashMap<Person, Boolean> visited = new HashMap<>();
    
    // stores the vertex in the same graph
    ArrayList<ArrayList<String>> connected = new ArrayList<>();
    
    // set all person unvisited
    for (Person temp : graph.getAllNodes()) {
      visited.put(temp, false);
    }
    
    for (Person temp : graph.getAllNodes()) {
      if (visited.get(temp) == false) {
        ArrayList<String> withinGraph = new ArrayList<>(); 
        withinGraph = DFSUtil(temp, visited, withinGraph); 
        connected.add(withinGraph);
      }
    }
    
    // make new graphs based on withinGraph
    for (ArrayList<String> temp: connected) {
      Graph g = new Graph();
      for (int i = 0; i < temp.size(); i++) {
        g.addNode(temp.get(i));
        
        // establish edges with its neighbors
        for (Person t : graph.getNeighbors(temp.get(i))) {
          g.addEdge(temp.get(i), t.getName());
        }
      }
      connectedComponents.add(g);
    }
    
    return connectedComponents;
  }

  
  /**
   * Helper method for getConnectedComponents()
   * 
   * @param person
   */
  private ArrayList<String> DFSUtil(Person person, 
      HashMap<Person, Boolean> visited, ArrayList<String> withinGraph) {
    
    visited.put(person, true); // set the vertex visited
    withinGraph.add(person.getName()); // add to the withinGraph
    for (Person temp: graph.getNeighbors(person.getName())) {
      if (visited.get(temp) == false) {
        DFSUtil(temp, visited, withinGraph);
      }
    }
    
    return withinGraph;
  }  
  
  /**
   * Return all Persons in this graph, helper method
   * 
   * @return ArrayList<Person>
   */
  private ArrayList<Person> getAllPersons() {
    ArrayList<Person> personList = new ArrayList<>(); // stores all person in this network

    // get all Persons
    Set<Graph> graphSet = this.getConnectedComponents();
    for (Graph temp : graphSet) {
      Set<Person> graphUser = temp.getAllNodes();
      for (Person t : graphUser) {
        personList.add(t); // add to userList
      }
    }
    
    return personList;
  }
  
  /**
   * Load user instructions
   * 
   */
  @Override
  public void loadFromFile(File filename) {
    Scanner scnr = null; // initialize scanner
    // store each line of user instructions
    ArrayList<String[]> userInstructions = new ArrayList<>();
    
    // load instructions
    try {
      scnr = new Scanner(filename); // read file
      while (scnr.hasNextLine()) {
        // split the user instructions into 3 parts
        String[] userInput = scnr.nextLine().split(" ");
        userInstructions.add(userInput); // add to userInstructions
      }
    } catch (FileNotFoundException e) {
      System.out.println("Error: file not found!");
      e.printStackTrace();
    } finally {
      if (scnr != null) {
        scnr.close();
      }
    }
    
    // execute instructions
    try {
      // execute instructions
      for (String[] temp: userInstructions) {
        switch (temp[0]) {
          case "a": // add friendship
            if (temp.length != 3) { // not enough information
              throw new Exception();
            }
            
            // add friendship
            this.addUser(temp[1]);
            this.addUser(temp[2]);
            this.addFriends(temp[1], temp[2]);              
            break;
          case "r": // remove friendship
            if (temp.length != 3) { // not enough information
              throw new Exception();
            }
            
            // Remove friendship
            this.removeFriends(temp[1], temp[2]);
            break;
          case "s": // set the user as center
            this.addUser(temp[1]);
            for (Person t: getAllPersons()) {
              if (t.getName().equals(temp[1])) {
                t.setCentralUser(true); // set the user as the central user
              }
            }
            
            break;
          case "ap": // add person
            if (temp.length != 2) { // wrong format
              throw new Exception();
            }
            
            this.addUser(temp[1]);
          break;
          case "rp": // remove person
            if (temp.length != 2) { // wrong format
              throw new Exception();
            }
            
            this.removeUser(temp[1]);
          break;
          default:
            break;
        }
      }
    } catch (Exception e) {
      Alert alert = new Alert(AlertType.WARNING, "Not enough information or wrong format!");
      alert.showAndWait().filter(r->r==ButtonType.CLOSE);
    }
  }

  /**
   * Save the social network to a file
   * 
   */
  @Override
  public void saveToFile(File filename) {
    PrintWriter writer = null; // initialize PrinterWritter
    ArrayList<String> userList = new ArrayList<>(); // stores all users
        
    // get all users
    Set<Graph> graphSet= this.getConnectedComponents();
    for (Graph temp : graphSet) {
      Set<Person> graphUser = temp.getAllNodes();
      for (Person t: graphUser) {
        userList.add(t.getName()); // add to userList
      }
    }
           
    // stores the network
    HashMap<String, java.util.Set<Person>> net = new HashMap<>();
       
    // collect network information
    for(String temp : userList) {
      net.put(temp, this.getFriends(temp));
    }
    
    try {
      writer = new PrintWriter(filename); // may throw IOException
      // print to the file
      for(String temp : userList) {
        writer.print(temp + ": ");
        for (Person t : net.get(temp)) {
          writer.print(t.getName() + "| ");
        }
        writer.println();
      }
    } catch (Exception e) {
      Alert alert = new Alert(AlertType.WARNING, "Unexpected Error!");
      alert.showAndWait().filter(r->r==ButtonType.CLOSE);
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
    
  }
  
  /**
   * main class for testing purpose
   * 
   * @param args
   */
  public static void main(String[] args) {
    SocialNetwork a = new SocialNetwork();

    // test for loadFromFile()
    a.loadFromFile(new File("sample_input_instructions.txt"));
    
    a.saveToFile(new File("sn.txt"));
    
    // test for getConnectedComponents() and getShortestPath()

    a.addFriends("1", "2");
    a.addFriends("1", "3");
    a.addFriends("1", "4");
    a.addFriends("2", "3");
    a.addFriends("2", "8");
    
    a.addUser("5");
    a.addFriends("5", "6");
    a.addFriends("6", "7");
    a.addFriends("5", "7");
    
    Set<Graph> b = a.getConnectedComponents();
    List<Person> c= a.getShortestPath("1", "8");
    int d = 2 + 1;
  }
}




























