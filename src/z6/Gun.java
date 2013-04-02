package z6;

import java.util.ArrayList;

/*
 * Shoots small bullets
 * 
 * Fix creating and placing bullet in right position after GC? (Allow head start?)
 * 
 * Tunneling issues?
 */
public class Gun implements IGun, Node {
	private Vec2 position;
	private Vec2 direction;

	private float delayPerShot;
	private float accumDelay;
	private int shotsFired;
	private boolean canFire;
	private boolean isActive;

	private int shotType;

	// private GunShot shot;

	private int bulletCollisionLayer;

	private Node parent;

	// private ArrayList<CannonShot> bullets;
	private ArrayList<IShot> bullets;

	public Gun() {
		setPosition(new Vec2(0, 0));
		setDirection(new Vec2(0, 1));

		parent = null;
		// bullets = new ArrayList<CannonShot>();
		bullets = new ArrayList<IShot>();

		setFireRatePerSec(1);
		accumDelay = 0.0f;
		shotsFired = 0;
		isActive = true;
		canFire = true;

		bulletCollisionLayer = -1;

		shotType = Constants.NULL_SHOT_ID;
	}

	/*
	 * direction - does not have to be normalized, the function will normalize
	 * the vector
	 */
	public void setDirection(Vec2 direction) {
		this.direction = direction;
		this.direction.normalize();
	}

	public void setShotType(int id) {
		if (id >= Constants.NULL_SHOT_ID && id < Constants.NUM_SHOT_TYPES) {
			shotType = id;
		}
	}

	public void setTag(int tag) {
	}

	/*
	 * Render all the bullets
	 */
	public void render() {
		// for(CannonShot b : bullets){
		for (IShot b : bullets) {
			b.render();
		}

		Renderer.stroke(255, 0, 0);
		Renderer.rect(parent.getPosition().x + 10, parent.getPosition().y + 10,
				6, 6);
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

		return Vec2.add(parentPos, position);
	}

	public int getID() {
		return Constants.CANNON_GUN_ID;
	}

	/*
	 * Set how many times this gun can fire every second. shotsPerSecond must be
	 * greater than 0.
	 */
	public void setFireRatePerSec(float shotsPerSecond) {
		if (shotsPerSecond > 0) {
			// Calculate delay between shots
			delayPerShot = 1.0f / shotsPerSecond;
		}
	}

	/*
   * 
   */
	public void update(float deltaTime) {
		accumDelay += deltaTime;
		if (accumDelay >= delayPerShot) {
			canFire = true;
		}

		// Why can't I make ArrayList<int> ??
		// Can't change list while we are iterating over it, so create another
		// list
		// ArrayList<CannonShot> bulletsToRemove = new ArrayList<CannonShot>();
		ArrayList<IShot> bulletsToRemove = new ArrayList<IShot>();

		// for(CannonShot b : bullets){
		for (IShot b : bullets) {
			b.update(deltaTime);

			if (b.isAlive() == false) {
				bulletsToRemove.add(b);
			}
		}

		// for(CannonShot b : bulletsToRemove){
		for (IShot b : bulletsToRemove) {
			// bullets.remove(b);
			// removeCollider(b);
		}
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

			IShot shot = ShotFactory.create(shotType);
			// shot.setLayer(bulletCollisionLayer);

			Vec2 bulletVelocity = Vec2.scale(direction, 300.0f);
			shot.setVelocity(bulletVelocity);
			shot.setPosition(getPosition());

			/*
			 * CannonShot shot = new CannonShot();
			 * shot.setLayer(bulletCollisionLayer);
			 */

			Z6.AddBullet(shot);
			// collidables.add(shot);
		}
	}
}
