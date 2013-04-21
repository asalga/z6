package z6;

public interface ICollidable{
	public Rectangle getBoundingRectangle();
	public void onCollision(ICollidable collidable);
	public void setLayer(int layerID);
	public int getLayer();
	public int getObjectType();
	
	
	public boolean isCollidable();
}