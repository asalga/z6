package z6;

import java.util.ArrayList;

import z6.Math.Vec2;

/* 
 * Main purpose of a gun is to limit the amount of times it can fire per second.
 *
 * We need a way to keep the gun general, but we don't want duplicate code
 * Add a setFireSpeed() method to Gun. Factory sets this when gun is created.
 * 
 * 
 * Look into:
 * Fix creating and placing bullet in right position after GC? (Allow head start?)
 * Tunneling issues
 */
public class Gun implements IGun, Node {
	
	private Vec2 position;
	private Vec2 direction;

	private float delayPerShot;
	private float accumDelay;
	private int shotsFired;
	private boolean canFire;
	private boolean isActive;

	private float shotSpeed;
	
	private FireBehaviour fireBehaviour;
	
	//private int shotType;

	private int bulletCollisionLayer;

	private Node target;
	private Node parent;

	// private ArrayList<CannonShot> bullets;
	//private ArrayList<IShot> bullets;

	public Gun() {
		setPosition(new Vec2(0, 0));
		setDirection(new Vec2(0, 1));

		parent = null;
		//bullets = new ArrayList<CannonShot>();
		//bullets = new ArrayList<IShot>();

		setFireRatePerSec(1);
		setShotSpeed(0);
		
		accumDelay = 0.0f;
		shotsFired = 0;
		isActive = true;
		canFire = true;

		bulletCollisionLayer = -1;

		//shotType = Constants.NULL_SHOT_ID;
	}

	/*
	 * direction - does not have to be normalized, the function will normalize
	 * the vector
	 */
	public void setDirection(Vec2 direction) {
		this.direction = direction;
		this.direction.normalize();
	}
	
	//public void setTarget(Vec2 _target){
	//}

	/*public void setShotType(int id) {
		if (id >= Constants.NULL_SHOT_ID && id < Constants.NUM_SHOT_TYPES) {
			shotType = id;
		}
	}*/
	
	/**
	 * 
	 */
	public void setFireBehaviour(FireBehaviour _fireBehaviour){
		fireBehaviour = _fireBehaviour;
	}

	public void setTag(int tag) {
	}

	/*
	 * Does the gun need to be shown?
	 */
	public void render() {
		//Renderer.stroke(255, 0, 0);
		//Renderer.rect(parent.getPosition().x, parent.getPosition().y, 5, 5);
	}

	public void setPosition(Vec2 position) {
		this.position = position;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	/**
	 * 
	 */
	public Vec2 getPosition() {
		Vec2 parentPos = new Vec2(0, 0);

		if (parent != null) {
			parentPos = parent.getPosition();
		}
		
		Vec2 rVec = Vec2.add(parentPos, position);
		return rVec;
	}

	public int getID() {
		return Constants.CANNON_GUN_ID;
	}

	public void setShotSpeed(float _shotSpeed){
		if(_shotSpeed >= 0){
			shotSpeed = _shotSpeed;
		}
	}
	
	public float getShotSpeed(){
		return shotSpeed;
	}
	
	/*
	 * Set how many times this gun can fire every second.
	 * 
	 * @param shotsPerSecond must be greater than 0.
	 */
	public void setFireRatePerSec(float shotsPerSecond) {
		if (shotsPerSecond > 0) {
			// Calculate delay between shots
			delayPerShot = 1.0f / shotsPerSecond;
		}
	}

	/**
	 * 
	 */
	public void setTarget(Node _target){
		target = _target;

		if(fireBehaviour != null){
			fireBehaviour.setTarget(target);
		}
	}
	
	/**
	 * 
	 */
	public void update(float deltaTime) {
		accumDelay += deltaTime;
		if (accumDelay >= delayPerShot) {
			canFire = true;
		}

		// Moved this to main class
		/*
		// Why can't I make ArrayList<int> ??
		// Can't change list while we are iterating over it, so create another list
		ArrayList<CannonShot> bulletsToRemove = new ArrayList<CannonShot>();
		ArrayList<IShot> bulletsToRemove = new ArrayList<IShot>();

		for (IShot b : bullets) {
			b.update(deltaTime);

			if (b.isAlive() == false) {
				bulletsToRemove.add(b);
			}
		}

		for (IShot b : bulletsToRemove) {
			bullets.remove(b);
			removeCollider(b);
		}*/
	}

	/*
	 * Does nothing, you can't add children to a CannonGun.
	 */
	public void addChild(Node node) {
		// Do nothing.
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public void setLayer(int layerID) {
		bulletCollisionLayer = layerID;
	}

	/*
	 * Try to fire a shot. We can only do so if enough time has passed since the
	 * last time the user fired. Also, the gun must be active.
	 */
	public void fire() {
		if (canFire && isActive) {
			canFire = false;
			accumDelay = 0;

			fireBehaviour.Fire(this);
		}
	}
}
