package z6;

import java.util.ArrayList;

/**
 * TODO: fix col,row / position
 * A turret is a stationary sprite that fires at the user if they reach a certain distance
 *
 * How to do user tracking? - If user gets a certain distance from turret, start pointing towards user at
 * a certain speed
 * turn machine gun towards user
 * Vector userPosition
 */
public class Turret implements Node {// , ICollidable{

	ArrayList<Node> nodes;
	ArrayList<IGun> guns;

	Node parent;
	Node target;

	// TODO: Fix this
	Vec2 position;
	int col;
	int row;
	
	int sightRadius;

	int health;
	boolean isAlive;

	public Turret() {
		guns = new ArrayList<IGun>();
		nodes = new ArrayList<Node>();
		parent = null;
		target = null;

		position = new Vec2(0, 0);
		col = row = 0;
		sightRadius = Constants.TILE_SIZE * 8;

		isAlive = true;
		health = 100;
	}

	// TODO: Fix this
	public void setPosition(int row, int col) {
		position.x = col;
		position.y = row;
		this.col = col;
		this.row = row;
	}

	/**
	 */
	public void setTarget(Node target) {
		if (isAlive == false) {
			return;
		}

		Vec2 targetToGun = Vec2.sub(target.getPosition(), new Vec2(position.x
				* Constants.TILE_SIZE, position.y * Constants.TILE_SIZE));

		float distance = targetToGun.magnitude();
		if (distance <= getSightRange()) {
			this.target = target;

			for (IGun g : guns) {
				Vec2 targetPos = target.getPosition().clone();
				targetPos = Vec2.sub(targetPos,
						new Vec2(position.x * Constants.TILE_SIZE, position.y
								* Constants.TILE_SIZE));

				g.setDirection(targetPos);
				g.fire();
			}
			/*
			 * Node gunNode = nodes.get(0); if(gunNode != null){ IGun gun =
			 * (IGun)gunNode;
			 * 
			 * PVector targetPos = cloneVec(target.getPosition()); targetPos =
			 * PVector.sub(targetPos, new PVector(position.x * TILE_SIZE,
			 * position.y * TILE_SIZE));
			 * 
			 * gun.setDirection(targetPos); gun.fire(); }
			 */
		} else {
			target = null;
		}
	}

	public void setPosition(Vec2 position) {
		this.position = position;
	}

	public void setLayer(int layerID) {
		// print("todo");
	}

	public void render() {
		if (isAlive) {

			Renderer.pushStyle();
			Renderer.noStroke();
			Renderer.fill(128, 0, 0);
			Renderer.rect(position.x * Constants.TILE_SIZE, position.y
					* Constants.TILE_SIZE, Constants.TILE_SIZE,
					Constants.TILE_SIZE);
			//Renderer.fill(0, 0, 0);
			//Renderer.ellipse(position.x * Constants.TILE_SIZE + 16, position.y
			//		* Constants.TILE_SIZE + 16, 5, 5);

			for (int i = 0; i < nodes.size(); i++) {
				nodes.get(i).render();
			}

			//
			for (int i = 0; i < guns.size(); i++) {
				guns.get(i).render();
			}

			// strokeWeight(1);
			// noFill();
			// stroke(255, 0, 0);
			// ellipse(position.x * TILE_SIZE + TILE_SIZE/2, position.y *
			// TILE_SIZE + TILE_SIZE/2, sightRadius * 2, sightRadius * 2);
			Renderer.popStyle();
		} else {
			Renderer.pushStyle();
			Renderer.noStroke();
			Renderer.fill(0, 0, 0);
			Renderer.rect(position.x * Constants.TILE_SIZE, position.y
					* Constants.TILE_SIZE, Constants.TILE_SIZE,
					Constants.TILE_SIZE);
			Renderer.popStyle();
		}
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public int getLayer() {
		return 1;
	}

	/*
	 * Even if this turret is dead, it still needs to call update on the bullets
	 * that it launched.
	 */
	public void update(float deltaTime) {
		// update guns
		for (Node node : nodes) {
			node.update(deltaTime);
		}

		// Still need to update guns which updates bullets if the turret has
		// been killed.
		for (int i = 0; i < guns.size(); i++) {
			//guns.get(i).fire();
			guns.get(i).update(deltaTime);
		}
	}

	/*
	 * Draws any relevant debugging objects such as sight radius
	 */
	public void setDebugDraw() {
	}

	/*
	  */
	public void setSightRadiusInPx(int sightRad) {
		if (sightRad >= 0) {
			sightRadius = sightRad;
		}
	}

	/*
	  */
	public void addChild(Node node) {
		nodes.add(node);
	}

	/**
	 * 
	 * @param gun
	 */
	public void addGun(IGun gun) {
		if (isAlive == false) {
			return;
		}
		
		//Renderer.println("adding gun");
		
		guns.add(gun);
		((Node) gun).setParent((Node) this);
		((Node) gun).setPosition(new Vec2(16, 16));
		gun.setLayer(2);
		// cannonGun.setPosition(new PVector(TILE_SIZE/2, TILE_SIZE/2));
		// cannonGun.setParent(turret);
		// turret.addChild(cannonGun);
	}

	/* public void removeGun(){} */

	public void destroy() {
		isAlive = false;
		for (IGun gun : guns) {
			gun.setActive(false);
		}
	}

	public Vec2 getPosition() {
		Vec2 parentPos = new Vec2(0, 0);

		if (parent != null) {
			parentPos = parent.getPosition();
		}

		return new Vec2(position.x * Constants.TILE_SIZE + parentPos.x,
				position.y * Constants.TILE_SIZE + parentPos.y);
	}

	public int getSightRange() {
		return sightRadius;
	}

	public Rectangle getBoundingRectangle() {
		return new Rectangle(position.x * 32, position.y * 32,
				Constants.TILE_SIZE, Constants.TILE_SIZE);
	}

	/*
	 * public void onCollision(ICollidable collidable){ //de
	 * println("something hit turret"); //destroy();
	 * 
	 * health -= 25; if(health <= 0){ health = 0; destroy(); } }
	 */
}