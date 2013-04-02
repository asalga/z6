package z6;

import processing.core.PApplet;

public class SpriteNode{// extends SceneNode{
  private PApplet parent;
  
  public SpriteNode(PApplet p){
  	parent = p;
  }
  
  public void render(){
    parent.rect(0,0,10,10);
  }
}