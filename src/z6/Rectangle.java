package z6;

/*
 * 
 */
public class Rectangle{
  public float x, y, w, h;
  
  public Rectangle(float x, float y, float w, float h){
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
  }
  
  public String toString(){
    return x + ", " + y + ", " + w + ", " + h;
  }
}
