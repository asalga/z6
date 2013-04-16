package z6;
import processing.core.PImage;
import z6.Math.Vec2;

/**
 * 
 * 
 */
public class SpriteNode implements Node{

	public static PImage grass = Renderer.loadImage("data\\grass.png");
	public static PImage water = Renderer.loadImage("data\\water.png");
	public static PImage trees = Renderer.loadImage("data\\trees.png");
	public static PImage cloud1 = Renderer.loadImage("data\\cloud1.png");
	public static PImage cloud2 = Renderer.loadImage("data\\cloud2.png");
	public static PImage ship = Renderer.loadImage("data\\ship.png");
	
	private Vec2 pos;
	private int imageID;
	//private static PImage img;
	
	public SpriteNode(int r, int c){
		pos = new Vec2(r, c);
		// TODO: create null image
	}
	
	public void setImage(int id){
		imageID = id;
	}
	
	public void setParent(Node p){}
	
	public void setPosition(Vec2 pos){
		this.pos = pos.clone();
	}
	
	public Vec2 getPosition(){
		return this.pos;
	}
	
	public void update(float t){}
	
	public void addChild(Node n){}
  
	private PImage getImage() {
		switch (imageID) {
			case Constants.GRASS:
				return grass;
			case Constants.WATER:
				return water;
			case Constants.TREES:
				return trees;
			case Constants.CLOUD1:
				return cloud1;
			case Constants.CLOUD2:
				return cloud2;
			case Constants.SHIP:
				return ship;
		}
		Renderer.println("Problem in SpriteNode: invalid imageID");
		return null;
	}
	
	/**
	 * 
	 */
	public void render(){
		//Renderer.rect(0, 0,  30,  30);
		Renderer.image(getImage(), (int)pos.x, (int)pos.y);
	}
}