package z6;

public class NullGun implements IGun{
  public void fire(){}
  public void setFireRatePerSec(float shotsPerSecond){}
  public void setTag(int tag){}
  public void setLayer(int layerID){}
  public void update(float deltaTimeInSeconds){}
  public void render(){}
  public void setDirection(Vec2 direction){}
  public void setActive(boolean active){}
  public int getID(){return Constants.NULL_GUN_ID;}
  
  //remove?
  public void setShotType(int shotType){}
  
  /*
  public void setPosition(PVector pos){
  }
  
  public PVector getPosition(){
    return getZeroVector();
  }
  public void addChild(Node n){
  }*/
}