package application;

/**
 * a person class that holds personal info
 * 
 * @author rosaliecarrow
 *
 */
public class Person {
  private String name;
  private boolean isCentralUser;
  private double x_axis;
  private double y_axis;

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @param name
   */
  public Person(String name) {
    super();
    this.name = name;
  }

  /**
   * Set if a person is the central user
   * 
   * @param value
   */
  public void setCentralUser(boolean value) {
    this.isCentralUser = value;
  }

  /**
   * Returns if this person is a central user
   * 
   * @return
   */
  public boolean isCentralUser() {
    return isCentralUser;
  }

  @Override
  public boolean equals(Object p) {
    if (this.getName().equals(((Person) p).getName())) {
      return true;
    } else {
      return false;
    }
  }
  
  /**
   * @return the x_axis
   */
  public double getX_axis() {
    return x_axis;
  }

  /**
   * @param x_axis the x_axis to set
   */
  public void setX_axis(double x_axis) {
    this.x_axis = x_axis;
  }

  /**
   * @return the y_axis
   */
  public double getY_axis() {
    return y_axis;
  }

  /**
   * @param y_axis the y_axis to set
   */
  public void setY_axis(double y_axis) {
    this.y_axis = y_axis;
  }

}
