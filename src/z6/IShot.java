package z6;

import z6.Math.Vec2;

/**
 * 
 */
public interface IShot{
  public int getID();
  public void setVelocity(Vec2 v);  
  public void setPosition(Vec2 pos);
  public Vec2 getPosition();
  public boolean isAlive();
  public void update(float t);
  public void setLayer(int id);
  public int getLayer();
  public void render();
  public String toString();
  public float getPower();
  
  //public Rectangle getBoundingRectangle();
  //public void onCollision(ICollidable col);
  //public void Destroy();
}