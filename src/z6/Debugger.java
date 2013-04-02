package z6;

import processing.core.PApplet;
import processing.core.PFont;
import java.util.ArrayList;

/* *
 * Prints text on top of everything for real-time object tracking.
 */
class Debugger{
  private ArrayList strings;
  private PFont font;
  private int fontSize;
  private boolean isOn;
  private PApplet pApplet;
  
  public Debugger(PApplet pApplet){
	this.pApplet = pApplet;
    isOn = true;
    strings = new ArrayList();
    fontSize = 15;
    font = this.pApplet.createFont("Arial", fontSize);
  }
  
  public void addString(String s){
    if(isOn){
      strings.add(s);
    }
  }
  
  /*
   * Should be called after every frame
   */
  public void clear(){
    strings.clear();
  }
  
  /**
    If the debugger is off, it will ignore calls to addString and draw saving
    some processing time.
  */
  public void toggle(){
    isOn = !isOn;
  }
  
  public void draw(){
    if(isOn){
      int y = 20;
      pApplet.fill(255);
      for(int i = 0; i < strings.size(); i++, y += fontSize){
        pApplet.textFont(font);
        pApplet.text((String)strings.get(i), 0, y);
      }
    }
  }
}
