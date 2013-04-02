package z6;

public interface IShot{
  public Rectangle getBoundingRectangle();
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
  //public void onCollision(ICollidable col);
  //public void Destroy();
}