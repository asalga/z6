package z6;

import z6.Math.Vec2;
//import z6.Math.Vec2; // redundant import statements are ignored.


/*
 * TODO: add tags?
 */
public class LaserShot implements IShot, ISubscriber {// implements ICollidable{
	private final float DEATH_AGE_IN_SEC = 0.1f;
	private final int DIAMETER = 2;

	private int collisionLayer;

	private Vec2 position;
	private Vec2 originShot;
	
	private boolean isAlive;
	
	// alive, dying, dead
	
	// TODO: figure out lifetime...
	private float lifeTime;
	private int id;

	public void updatePosition(Vec2 _targetPos){
		position = _targetPos.clone();
	}
	
	public LaserShot() {
		setPosition(new Vec2(0, 0));
		isAlive = true;
		lifeTime = 0;
		collisionLayer = -1;
		id = ID.next();
		originShot = new Vec2();
	}

	public int getID() {
		return id;
	}

	// remove this?
	public void setVelocity(Vec2 v) {
		//velocity = v;
	}

	public void setPosition(Vec2 p) {
		position = p;
	}
	
	public void setOriginShot(Vec2 _pos){
		originShot = _pos;
	}

	public Vec2 getPosition() {
		return position;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void update(float deltaTime) {
		
		// TODO: fix this range
		// if the distance between from where the laser was shot from
		// and the target is too far, the laser needs to die.
		if( Vec2.magnitude(position, originShot) > 200){
			isAlive = false;
		}
		
		
		lifeTime += deltaTime;

		if (lifeTime >= DEATH_AGE_IN_SEC) {
			isAlive = false;
			return;
		}
/*
		Vec2 deltaMove = Vec2.scale(velocity, deltaTime);
		position = Vec2.add(position, deltaMove);

		// We can remove the shot if it went past the game board bounds.
		if (position.x < 0 || position.y < 0) {
			isAlive = false;
		}*/
	}

	public void setLayer(int layerID) {
		collisionLayer = layerID;
	}

	public int getLayer() {
		return collisionLayer;
	}

	/**
	 * 
	 */
	public void render() {
		
		// Draw a line from the ship the laser shot is observing and
		// the original location from where it was shot.
		Renderer.pushStyle();
		Renderer.stroke(32, 16, 192);
		Renderer.strokeWeight(2);
		Renderer.line(originShot.x, originShot.y, position.x, position.y);
		Renderer.popStyle();

		// Convert world coords to screen coords
		// Vec2 camPos = gameCam.getTopLeft();
		// println(" " + (position.x - camPos.x) + ", " + (position.y - camPos.y));
		// ellipse(position.x - camPos.x, position.y - camPos.y , DIAMETER,
		// DIAMETER);
	}

	public String toString() {
		return "pos(" + position.x + ", " + position.y + ")";
	}

	// public void onCollision(ICollidable collider){
	// isAlive = false;
	// }

	public Rectangle getBoundingRectangle() {
		return new Rectangle(position.x, position.y, DIAMETER / 2, DIAMETER / 2);
	}

	private void Destroy() {
		Renderer.println("Bullet being destroyed");
	}
}