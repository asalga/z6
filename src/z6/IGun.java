package z6;

/**
 * Common interface for all guns.
 */
interface IGun{

  // All guns know how to fire whatever they need to fire
  public void fire();

  // All guns know how often they can fire shots
  public void setFireRatePerSec(float shotsPerSecond);
  
  // 
  public int getID();
  
  // Shots fired from each gun will be assigned a specific tag for collision detection
  public void setTag(int tag);
  
  public void setLayer(int layerID);
  
 // public void setActive(boolean active);

  // If the gun is active, it can shoot. This is used when a turret is dead, it still has guns,
  // and those guns manage bullets, but they can no longer shoot.
  public void setActive(boolean active);

  // Game loop
  public void update(float deltaTimeInSeconds);
  public void render();

  public void setShotType(int shotType);
  
  public void setDirection(Vec2 direction);
  //public void setPosition(PVector2D p);???????
}