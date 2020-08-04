/**
 * Filename: Main.java Project: SocialNetworkVisualizerAteam7 Authors: ateam7 Due date: Dec 3
 * 11:59pm Other source credits: none known bugs: none
 * 
 * Social network visualizer
 */

package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.chrono.MinguoChronology;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Social network visualizer of ateam 7
 * 
 * @author ateam7
 */
public class Main extends Application {
  private ArrayList<String> history = new ArrayList();// save all changes
  private static SocialNetwork socialNetwork = new SocialNetwork(); // stores social network

  private List<String> args;
  private static final int WINDOW_WIDTH = 1024;
  private static final int WINDOW_HEIGHT = 768;

  private static final int CANVAS_WIDTH = 600;
  private static final int CANVAS_HEIGHT = 600;

  Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);// Create a canvas
  GraphicsContext gc = canvas.getGraphicsContext2D();
  Alert alert = new Alert(AlertType.NONE);// a general alert for this class

  private static final String APP_TITLE = "Social Network 007";

  public static MenuBar menuBar = new MenuBar();// general data manipulation
  public static VBox rightBox = new VBox();
  public static VBox leftBox = new VBox();// general data search
 
  /**
   * set up the right box
   */
  private void setRightBox() {
    rightBox.setBackground(
        new Background(
          (new BackgroundFill(
                    Color.CORNFLOWERBLUE, 
                    new CornerRadii(500), 
                    new Insets(10)))
           ));
    // right side button of design
    Button viewHistory = new Button("View History");
    viewHistory.setOnAction(E -> {
      alert.setAlertType(AlertType.NONE);
      alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
      alert.setContentText(history.toString());
      alert.showAndWait();
    });
    
    rightBox.getChildren().add(viewHistory);
  }

  /**
   * set up left dispaly and search
   */
  private void setLeftBox() {
    leftBox.setPrefWidth(180);
leftBox.setBackground(
    new Background(
      (new BackgroundFill(
                Color.BURLYWOOD, 
                new CornerRadii(100), 
                new Insets(10)))
       ));

    // set center--------------------------------------------
    Button bSetCenter = new Button("Set Center");
    bSetCenter.setMinWidth(leftBox.getPrefWidth());
    bSetCenter.setOnAction(export -> {
      String name= null;
      setSelectedUser(name =popUpWindow("Who to set center?"));
      if(name!=null) history.add("s "+name);
    });

    // display--------------------------------------------
    Button bDisplay = new Button("Display");
    bDisplay.setMinWidth(leftBox.getPrefWidth());
    bDisplay.setOnAction(export -> {
      drawGraph(gc);
    });


    // foldable structure for search
    // # of friends--------------------------------------------
    Button bNumber = new Button("Number of Friend");
    bNumber.setOnAction(export -> {
      alert.setAlertType(AlertType.INFORMATION);
      alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
      alert.setContentText(
          "The number of friend is " + socialNetwork.getConnectedComponents().size());;
      alert.showAndWait();
    });


    // friend--------------------------------------------
    Button bFriend = new Button("Friend");

    // mutual friend--------------------------------------------
    Button bMutualFriend = new Button("Mutual Friend");
    bMutualFriend.setOnAction(e -> {
      String p1 = popUpWindow("first person");
      String p2 = popUpWindow("second person");

      // if there is a user input, add to graph in sn
      if (p1 != null && p2 != null) {
        ArrayList<String> a = new ArrayList();
        for (Person x : socialNetwork.getMutualFriends(p1, p2)) {
          a.add(x.getName());
        }
        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        alert.setContentText(a.toString());
        alert.showAndWait();

      } else {
        alert.setAlertType(AlertType.ERROR);
        alert.setContentText("Invalid name input");
        alert.showAndWait();
      }
    });


    // display shortest path--------------------------------------------
    Button bShortestPath = new Button("Shortest Path");
    bShortestPath.setOnAction(e -> {
      String p1 = popUpWindow("first person");
      String p2 = popUpWindow("second person");

      // if there is a user input, add to graph in sn
      if (p1 != null && p2 != null) {
        ArrayList<String> a = new ArrayList();
        for (Person x : socialNetwork.getShortestPath(p1, p2)) {
          a.add(x.getName());
        }
        alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        alert.setContentText(a.toString());
        alert.showAndWait();

      } else {
        alert.setAlertType(AlertType.ERROR);
        alert.setContentText("Invalid name input");
        alert.showAndWait();
      }
    });


    VBox foldtp = new VBox(bNumber, bFriend, bMutualFriend, bShortestPath);

    TitledPane tp = new TitledPane("Search", foldtp);

    leftBox.getChildren().addAll(bSetCenter, bDisplay, tp);
  }

  
  /**
   * set the user with a name to center
   * 
   * @param name
   */
  private void setSelectedUser(String name) {    
    for (Person t: getAllPersons()) {
      if (t.getName().equals(name)) {
        t.setCentralUser(true);
        gc.fillOval(t.getX_axis(), t.getY_axis(),77, 77);
        // set the user as the central user
        return;
        
        
      }
    }
    
    drawGraph(gc);
    
    alert.setAlertType(AlertType.ERROR);
    alert.setContentText("Invalid name input");
    alert.showAndWait();
  }



  /**
   * Get the name from a space in canvas when clicked
   * 
   * @return the name
   */
  private String getNameFromCoordinates() {
    String name = null;
    // double x = new MouseEvent().getX();
    return name;

    // TODO Auto-generated method stub

  }

  /*
   * set up the top menu bar
   */
  private void setUpMenuBar() {
    menuBar.setBackground(
        new Background(
          (new BackgroundFill(
                    Color.GAINSBORO, 
                    new CornerRadii(500), 
                    new Insets(10)))
           ));
    // File menu - new, save, exit-----------------------------------------
    Menu fileMenu = new Menu("File");
    // instantiate items
    MenuItem mitSave = new MenuItem("Save");
    MenuItem mitLoad = new MenuItem("Load");
    MenuItem mitExport = new MenuItem("Export");
    MenuItem mitExit = new MenuItem("Exit");

    // add all items
    fileMenu.getItems().addAll(mitSave, mitLoad, mitExport, mitExit);

    // set items on action
    // ---save
    mitSave.setOnAction(save -> {
      try (PrintWriter pw = new PrintWriter(new File("log.txt"))) {
        // write every change to log txt
        for (String l : history) {
          pw.println(l);
        }

      } catch (Exception e) {
        // nothing
      }
    });

    // ---load
    mitLoad.setOnAction(load -> {
      String name = null;
      try{if ((name = popUpWindow("Input file name")) != null) {
        socialNetwork.loadFromFile(new File(name));
      }}
      catch(Exception e) {
        //nothing
      }
    });

    // ---export
    mitExport.setOnAction(export -> {
      String name = null;
      if ((name = popUpWindow("Input file name")) != null) {

        try (PrintWriter pw = new PrintWriter(new File(name))) {
          // write every change to log txt
          for (String l : history) {
            pw.println(l);
          }

        } catch (Exception e) {
          // nothing
        }
      }
    });

    // ---exit
    mitExit.setOnAction(actionEvent -> Platform.exit());


    // Add menu - new, save, exit-----------------------------------------
    Menu mAdd = new Menu("Add");
    MenuItem mitAddVertex = new MenuItem("Add person");
    mitAddVertex.setOnAction(EH -> {
      String name = popUpWindow("Add person");
      // if there is a user input, add to graph in sn
      if (name != null) {
        socialNetwork.addUser(name);
        history.add("a "+name);
        // drawGraph(gc);
      } else {
        alert.setAlertType(AlertType.ERROR);
        alert.setContentText("Invalid name input");
        alert.showAndWait();
      }
    });
    MenuItem mitAddEdge = new MenuItem("Add relationship");
    mitAddEdge.setOnAction(EH -> {
      String p1 = popUpWindow("Add first person");
      String p2 = popUpWindow("Add second person");

      // if there is a user input, add to graph in sn
      if (p1 != null && p2 != null) {
        socialNetwork.addFriends(p1, p2);

history.add("a "+p1+" "+ p2);
        // drawGraph(gc);
      }

      else {
        alert.setAlertType(AlertType.ERROR);
        alert.setContentText("Invalid name input");
        alert.showAndWait();
      }
      
    });
    mAdd.getItems().addAll(mitAddVertex, mitAddEdge);

    // remove menu - new, save, exit-----------------------------------------
    Menu mRemove = new Menu("Remove");
    MenuItem mitRemoveVertex = new MenuItem("Remove person");
    MenuItem mitRemoveEdge = new MenuItem("Remove relationship");

    // set on action
    mitRemoveVertex.setOnAction(EH -> {
      String name = popUpWindow("Remove person");
      // if there is a user input, add to graph in sn
      if (name != null) {
        socialNetwork.removeUser(name);
        history.add("r "+name);  
        // drawGraph(gc);
      } else {
        alert.setAlertType(AlertType.ERROR);
        alert.setContentText("Invalid name input");
        alert.showAndWait();
      }
    
      
    });
    mitRemoveEdge.setOnAction(EH -> {
      String p1 = popUpWindow("Remove first person");
      String p2 = popUpWindow("Remove second person");

      // if there is a user input, add to graph in sn
      if (p1 != null && p2 != null) {
        socialNetwork.removeFriends(p1, p2);
        history.add("r "+p1+" "+ p2);
        // drawGraph(gc);
      }

      else {
        alert.setAlertType(AlertType.ERROR);
        alert.setContentText("Invalid name input");
        alert.showAndWait();
      }
      
   
    });
    mRemove.getItems().addAll(mitRemoveVertex, mitRemoveEdge);

    // remove menu - new, save, exit-----------------------------------------
    Menu mClear = new Menu("Clear");
    MenuItem mitClear = new MenuItem("Clear");
    // set on action
    mitClear.setOnAction(EH -> {
      gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    });
    mClear.getItems().addAll(mitClear);


    // help menu - new, save, exit-----------------------------------------
    Menu mHelp = new Menu("Help");
    MenuItem mitHelp = new MenuItem("Help");
    mitHelp.setOnAction(e->{
      alert.setAlertType(AlertType.INFORMATION);
      alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
      alert.setContentText(" Top box:\n save history, load from file, export to file, exit the program, add/remove users/relationship, clear canvas, and show current status\n "
          + "\n Left box:\n search users, display graph\n "
          + "\n Right box:\n view history of all instructions");
      alert.showAndWait();
    });
    mHelp.getItems().add(mitHelp);


    // help menu - new, save, exit-----------------------------------------
    Menu mCurrentStatus = new Menu("Current Status");
    MenuItem mitCurrentStatus = new MenuItem("Current Status");
    mitCurrentStatus.setOnAction(e->{
      alert.setAlertType(AlertType.INFORMATION);
      alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
      if(history.size()!=0) {
        alert.setContentText("Your last instruction: "+ history.get(history.size()-1)); 
        
      }
      else {
        alert.setContentText("You are just starting");
      }
      alert.showAndWait();
    });
    mCurrentStatus.getItems().addAll(mitCurrentStatus);

    // this line should be updated after all menu item completed
    menuBar.getMenus().addAll(fileMenu, mAdd, mRemove, mClear, mHelp, mCurrentStatus);

  }


  /**
   * Draws node to the canvas
   * 
   * @param gc
   * @param name
   * @param x
   * @param y
   */
  private void drawNode(GraphicsContext gc, String name, double x, double y) {
    gc.strokeText(name, x, y);
  }

  /**
   * Draws edges between 2 nodes
   * 
   * @param gc
   * @param x1
   * @param y1
   * @param x2
   * @param y2
   */
  private void drawEdge(GraphicsContext gc, double x1, double y1, double x2, double y2) {
    gc.strokeLine(x1, y1, x2, y2);

  }

  /**
   * Draw the complete picture of the social network
   * 
   * @param gc
   */
  private void drawGraph(GraphicsContext gc) {
    ArrayList<Person> personList = getAllPersons(); // get all Persons   
    ArrayList<Person> visited = new ArrayList<>(); // Persons have been the center
    Person cu = null;
    
    // find central user
    for (Person t: personList) {
      if (t.isCentralUser()) {
        // set the position of the central user
        cu = t;
        return;
      }
    }
    
    // if no central user is set, then use the first Person as the central user
    if (cu == null) {
      cu = personList.get(0);
    }
        
    System.out.println(cu.getName() + 200 + 200); //FIXME
    
    gc.strokeText(cu.getName(), CANVAS_HEIGHT / 3, CANVAS_WIDTH / 3); // draw node
    drawGraphHelper(cu, visited, new ArrayList<>(), CANVAS_HEIGHT / 3,
        CANVAS_WIDTH / 3); // draw the rest graphs

    // draw other connected components
    Set<Graph> graphSet = socialNetwork.getConnectedComponents();
    for (Graph temp : graphSet) {
      Set<Person> graphUser = temp.getAllNodes();
      ArrayList<Person> usersInOneGraph = new ArrayList<>();
      for (Person t: graphUser) {
        usersInOneGraph.add(t);
      }   
      
      if (usersInOneGraph.contains(cu)) { // the graph already been drawn
        continue;
      }
      else {
        gc.strokeText(usersInOneGraph.get(0).getName(), CANVAS_HEIGHT / 6,
            CANVAS_WIDTH / 6); // draw node
        drawGraphHelper(usersInOneGraph.get(0), visited, new ArrayList<>(),
            CANVAS_HEIGHT / 6, CANVAS_WIDTH / 6); // draw the rest graphs
      }
    }
    
  }
  
  /**
   * Helper method to draw the network
   * 
   * @param cu
   * @param visited
   */
  private void drawGraphHelper(Person cu, ArrayList<Person> visited,
      ArrayList<Person> onGraph, double cux, double cuy) {
    //ArrayList<Person> personList = getAllPersons(); // all Persons
    Set<Person> friendList = socialNetwork.getFriends(cu.getName()); // neighbors
    int numFriendes = socialNetwork.getFriends(cu.getName()).size(); // number of friends   
    int i = 0; // counts the number of friends has been drawn
    
    visited.add(cu); // set the central user as visited
    onGraph.add(cu); // this node appeared on the graph
    
    ArrayList<Person> toBeRemoved = new ArrayList<>();
    
    for (Person t: visited) {
      Iterator<Person> iterator = friendList.iterator();
      while (iterator.hasNext()) {
        Person person = (Person) iterator.next();
        if (person.getName().equals(t.getName())) {
          toBeRemoved.add(person);
          
          //numFriendes--;
        }
      }     
    }
    
    // move former central users off the friend list
    for (Person t: toBeRemoved) {
      friendList.remove(t);
    }
    
    
    // draw all the neighbors of cu
    if (numFriendes != 0) {
      for (Person t: friendList) {
        // if this node did not appear on the graph, put it at a new position
        if (!onGraph.contains(t)) {
          double tx = cux + 100*Math.cos(i * (2 * Math.PI/numFriendes));
          double ty = cuy + 100*Math.sin(i * (2 * Math.PI/numFriendes));
          t.setX_axis(tx);
          t.setY_axis(ty);
          i++;
          System.out.println(t.getName() + tx + ty);//FIXME
          if (t.getName() != null) {
            gc.strokeText(t.getName(), tx, ty); // draw the node
            gc.strokeLine(cux, cuy, tx, ty); // draw the edge
            onGraph.add(t);
          }
        }
        else { // this node appeared on the graph
          if (t.getName() != null) {
            drawCurve(cux, cuy, t.getX_axis(), t.getY_axis());
            onGraph.add(t);
          }
        }
        
        if(t.isCentralUser()) {
          gc.fillOval(t.getX_axis(),  t.getY_axis(), 3,3);
        }
        
     
      }
      
      for (Person t: friendList) {
        //recursively draw the neighbors of this node
        if (!visited.contains(t)) {         
          drawGraphHelper(t, visited, onGraph, t.getX_axis(), t.getY_axis());
        } 
      }
    }
    
    
  }
  
  /**
   * Draws a fold line between 2 points
   * 
   * @param x1
   * @param y1
   * @param x2
   * @param y2
   */
  private void drawCurve(double x1, double y1, double x2, double y2) {
    double dx = Math.abs(x1 - x2);
    double dy = Math.abs(y1 - y2);
    double midx = 0.5*(x1 + x2) + 0.25*dx;
    double midy = 0.5*(y1 + y2) + 0.25*dy;
    
    gc.strokeLine(x1, y1, midx, midy);
    gc.strokeLine(midx, midy, x2, y2);
    
  }
  
  
  /**
   * Return all Persons in this graph, helper method
   * 
   * @return ArrayList<Person>
   */
  private ArrayList<Person> getAllPersons() {
    ArrayList<Person> personList = new ArrayList<>(); // stores all person in this network

    // get all Persons
    Set<Graph> graphSet = socialNetwork.getConnectedComponents();
    for (Graph temp : graphSet) {
      Set<Person> graphUser = temp.getAllNodes();
      for (Person t : graphUser) {
        personList.add(t); // add to userList
      }
    }
    
    return personList;
  }
  
  /**
   * PopUpWIndow
   * 
   * @param text
   * @return
   */
  private String popUpWindow(String text) {
    TextInputDialog dia = new TextInputDialog("Interactive text field");
    dia.setHeaderText(text);
    dia.setContentText("Enter");

    try {
      Optional<String> result = dia.showAndWait();
      String input = result.get();
      if (input != null && input.trim().length() != 0)
        return input;
      else {
        throw new Exception();
      }
    } catch (Exception e) {
      return null;
    }

  }

  
  
  /**
   * Method for loading the network form a file
   * 
   * @param filename
   */
  private void parser(String filename) {
    Scanner scnr = null; // initialize scanner
    // store each line of user instructions
    ArrayList<String[]> userInstructions = new ArrayList<>();
    
    // load instructions
    try {
      scnr = new Scanner(new File(filename)); // read file
      while (scnr.hasNextLine()) {
        // split the user instructions into 3 parts
        String[] userInput = scnr.nextLine().split(" ");
        userInstructions.add(userInput); // add to userInstructions
      }
    } catch (Exception e) {
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
            socialNetwork.addUser(temp[1]);
            socialNetwork.addUser(temp[2]);
            socialNetwork.addFriends(temp[1], temp[2]);              
            break;
          case "r": // remove friendship
            if (temp.length != 3) { // not enough information
              throw new Exception();
            }
            
            // Remove friendship
            socialNetwork.removeFriends(temp[1], temp[2]);
            break;
          case "s": // set the user as center
            socialNetwork.addUser(temp[1]);
            setSelectedUser(temp[1]);
            
            break;
          case "ap": // add person
            if (temp.length != 2) { // wrong format
              throw new Exception();
            }
            
            socialNetwork.addUser(temp[1]);
          break;
          case "rp": // remove person
            if (temp.length != 2) { // wrong format
              throw new Exception();
            }
            
            socialNetwork.removeUser(temp[1]);
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
   * Method for saving the network to a file
   *
   */
  private void saveNetwork() {
    PrintWriter writer = null; // initialize PrinterWritter
    ArrayList<String> userList = new ArrayList<>(); // stores all users
        
    // get all users
    Set<Graph> graphSet= socialNetwork.getConnectedComponents();
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
      net.put(temp, socialNetwork.getFriends(temp));
    }
    
    try {
      writer = new PrintWriter("Social Network.txt"); // may throw IOException
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
   * The class will be called by launch(args) in main method
   * 
   * @param primaryStage
   * @throws FileNoteFoundException
   */
  @Override
  public void start(Stage primaryStage) throws FileNotFoundException {
    // save args
    args = this.getParameters().getRaw();
    gc.setFill(Color.BLUE);
    
    // Main layout is Border Pane
    // (this includes top,left,center,right,bottom)
    BorderPane root = new BorderPane();

    // setting up bars
    setUpMenuBar();
    menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
    setLeftBox();
    setRightBox();
    VBox bottomBox = new VBox();

    // add to pane
    root.setTop(menuBar);
    root.setLeft(leftBox);
    root.setRight(rightBox);
    root.setCenter(canvas);
    


    // set scene
    Scene mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

    // display the primary stage
    primaryStage.setTitle(APP_TITLE);
    primaryStage.setScene(mainScene);
    primaryStage.show();

  }

  /**
   * Main method
   * 
   * @param args
   */
  public static void main(String[] args) {
    launch(args); // start the GUI, calls start method
  }

}
