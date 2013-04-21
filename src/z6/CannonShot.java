package z6;

import z6.Math.Vec2;

/*
 * TODO: add tags?
 */
public class CannonShot implements IShot, ICollidable{
	private final float DEATH_AGE_IN_SEC = 3.0f;
	private final int DIAMETER = 6;

	private int collisionLayer;

	private Vec2 position;
	private Vec2 velocity;
	private boolean isAlive;
	
	// alive, dying, dead
	private float lifeTime;
	private int id;
	
	private float power;
	private boolean collidable;

	/**
	 * 
	 */
	public CannonShot() {
		setPosition(new Vec2(0, 0));
		setVelocity(new Vec2(0, 0));
		isAlive = true;
		lifeTime = 0;
		collisionLayer = -1;
		id = ID.next();
		power = 2;
		collidable = true;
	}

	public int getID() {
		return id;
	}
	
	public boolean isCollidable(){
		return collidable;
	}

	public Vec2 getPosition() {
		return position;
	}
	
	public float getPower(){
		return power;
	}
	
	
	public void setVelocity(Vec2 v) {
		velocity = v;
	}

	public void setPosition(Vec2 p) {
		position = p;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void update(float deltaTime) {
		lifeTime += deltaTime;

		if (lifeTime >= DEATH_AGE_IN_SEC) {
			isAlive = false;
			return;
		}

		Vec2 deltaMove = Vec2.scale(velocity, deltaTime);
		position = Vec2.add(position, deltaMove);

		// We can remove the shot if it went past the game board bounds.
		if (position.x < 0 || position.y < 0) {
			isAlive = false;
		}
	}

	public void setLayer(int layerID) {
		collisionLayer = layerID;
	}

	public int getLayer() {
		return collisionLayer;
	}

	public void render() {
		Renderer.pushStyle();
		Renderer.strokeWeight(2);
		Renderer.stroke(0, 0, 0);
		Renderer.fill(200, 255);

		// Convert world coords to screen coords
		// Vec2 camPos = gameCam.getTopLeft();
		// println(" " + (position.x - camPos.x) + ", " + (position.y -
		// camPos.y));

		// ellipse(position.x - camPos.x, position.y - camPos.y , DIAMETER,
		// DIAMETER);
		Renderer.ellipse(position.x, position.y, DIAMETER, DIAMETER);
		Renderer.popStyle();
	}

	public String toString() {
		return "pos(" + position.x + ", " + position.y + ")" + " vel("
				+ velocity.x + ", " + velocity.y + ")";
	}

	public void onCollision(ICollidable collider){
		isAlive = false;
	}
	
	public int getObjectType(){
		return 1;
	}

	public Rectangle getBoundingRectangle() {
		return new Rectangle(position.x, position.y, DIAMETER / 2, DIAMETER / 2);
	}

	private void Destroy() {
		Renderer.println("Bullet being destroyed");
	}
}