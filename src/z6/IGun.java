package z6;

import z6.Math.Vec2;

/**
 * Common interface for all guns.
 */
interface IGun{

  // All guns know how to fire whatever they need to fire
  public void fire();

  public int getLayer();
  
  // All guns know how often they can fire shots
  public void setFireRatePerSec(float shotsPerSecond);
  
  // Set the speed of the shots in pixels/second
  public void setShotSpeed(float shotSpeed);
  public float getShotSpeed();
  
  // 
  public int getID();
  
  // Shots fired from each gun will be assigned a specific tag for collision detection
  //public void setTag(int tag);
  
  public void setLayer(int layerID);

  // If the gun is active, it can shoot. It allows us to suspend firing for a time being
  // rather than removing the guns entirely.
  public void setActive(boolean active);

  // Game loop
  public void update(float deltaTimeInSeconds);
  public void render();

  public void setFireBehaviour(FireBehaviour fireBehaviour);
  
  public void setTarget(Node _target);
  
  public void setDirection(Vec2 direction);
  //public void setPosition(PVector2D p);???????
}