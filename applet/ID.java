package z6;

/*
 * Generates unique ID's starting at zero.
 */
public abstract class ID{
  private ID(){}
  private static int nextID = -1;
  
  public static int next(){
    return nextID++;
  }
}