package application;

import java.io.File;
import java.util.List;
import java.util.Set;

public interface SocialNetworkADT {
  /**
   * add two person as friends
   * 
   * @param person1 friend 1
   * @param person2 friend 2
   * @return true if successful, false if failed
   */
  public void addFriends(String person1, String person2);

  /**
   * remove two person who are friends
   * 
   * @param person1 friend 1
   * @param person2 friend 2
   * @return true if successful, false if failed
   */
  public void removeFriends(String person1, String person2);

  /**
   * add an user
   * 
   * @param person user to add
   * @return true if successful, false if failed
   */
  public void addUser(String person);

  /**
   * remove an user
   * 
   * @param person user to remove
   * @return true if successful, false if failed
   */
  public void removeUser(String person);

  /**
   * get all friends of an user
   * 
   * @param person the user focused on
   * @return a set of the user's friends
   */
  public Set<Person> getFriends(String person);

  /**
   * find the mutual friend between two people
   * 
   * @param person1 friend 1
   * @param person2 friend 2
   * @return a set of their mutual friends
   */
  public Set<Person> getMutualFriends(String person1, String person2);

  /**
   * find the shortest path in friendship
   * 
   * @param person1
   * @param person2
   * @return
   */
  public List<Person> getShortestPath(String person1, String person2);

  /**
   * Get all connected users
   * 
   * @return a set of connected users
   */
  public Set<Graph> getConnectedComponents();

  /**
   * lode data from file
   * 
   * @param filename name of file to load
   */
  public void loadFromFile(File filename);

  /**
   * save data to file
   * 
   * @param filename name of file to save
   */
  public void saveToFile(File filename);

}
