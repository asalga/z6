package z6;

import z6.Math.Vec2;

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
  
  public void setShotSpeed(float shotSpeed){}
  
  public void setShotType(int shotType){}
  public float getShotSpeed(){return 0f;}
  
  public void setFireBehaviour(FireBehaviour fireBehaviour){}
  public void setTarget(Node _target){}
  
  /*
  public void setPosition(PVector pos){
  }
  
  public PVector getPosition(){
    return getZeroVector();
  }
  public void addChild(Node n){
  }*/
}