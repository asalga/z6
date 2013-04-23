package z6;

import java.util.ArrayList;
import z6.Constants;
import z6.Math.Vec2;

/**
 * TODO: fix col,row / position
 * A turret is a stationary sprite that fires at the user if they reach a certain distance
 *
 * How to do user tracking? - If user gets a certain distance from turret, start pointing towards user at
 * a certain speed
 * turn machine gun towards user
 * Vector userPosition
 */
public class Turret implements Node, ISubscriber, ICollidable{

	ArrayList<Node> nodes;
	ArrayList<IGun> guns;
	
	Vec2 shipPos;

	Node parent;
	Node target;

	// TODO: Fix this
	Vec2 position;
	int col;
	int row;

	int sightRadius;

	int health;
	boolean isAlive;

	// Only turrets that were rendered on screen can successfully have
	// setTarget called on them.
	public boolean renderedLastFrame;

	public Turret() {
		guns = new ArrayList<IGun>();
		nodes = new ArrayList<Node>();
		parent = null;
		target = null;

		position = new Vec2(0, 0);
		col = row = 0;
		sightRadius = Constants.TILE_SIZE * 6;

		isAlive = true;
		health = 100;

		shipPos = new Vec2(0,0);
		renderedLastFrame = false;
	}

	// TODO: Fix this
	public void setPosition(int row, int col) {
		position.x = col;
		position.y = row;
		this.col = col;
		this.row = row;
	}

	public int getObjectType(){
		return 2;
	}
	
	/**
	 */
	public void setTarget(Node _target) {
		 
		if (isAlive == false) {
			return;
		}

		Vec2 targetPos = _target.getPosition().clone();

		// HACK: fix this
		targetPos.x -= Constants.TILE_SIZE/2;
		targetPos.y -= Constants.TILE_SIZE/2;

		Vec2 targetToGun = Vec2.sub(targetPos, new Vec2(position.x
				* Constants.TILE_SIZE, position.y * Constants.TILE_SIZE));

		float distance = targetToGun.magnitude();
		if (distance <= getSightRange()) {
			this.target = _target;

			for (IGun g : guns) {
				//Vec2 targetPos = target.getPosition().clone();

				//targetPos = Vec2.sub(targetPos,
				//	new Vec2(position.x * Constants.TILE_SIZE, position.y
				//		* Constants.TILE_SIZE));

				g.setTarget(this.target);
				//g.setDirection(targetToGun.clone());
			}
		} else {
			this.target = null;
		}
	}

	public void setPosition(Vec2 position) {
		this.position = position;
	}

	public void setLayer(int layerID) {
		// print("todo");
	}
	
	/**
	 * 
	 */
	public void updatePosition(Vec2 _shipPos){
		shipPos = _shipPos.clone();
	}

	/**
	 * 
	 */
	public void render() {
		renderedLastFrame = true;

		if (isAlive) {

			Renderer.pushStyle();
			Renderer.noStroke();
			Renderer.fill(128, 0, 0,255);
			Renderer.rect(position.x * Constants.TILE_SIZE, position.y
					* Constants.TILE_SIZE, Constants.TILE_SIZE,
					Constants.TILE_SIZE);

			if(target != null){
				
				Renderer.stroke(0);
				Renderer.strokeWeight(3);
				//Renderer.println("" + position);
				//Renderer.println("" + target.getPosition());
				
				Vec2 gunToTarget = new Vec2(
						target.getPosition().x -
						position.x * Constants.TILE_SIZE + 16,
						
						target.getPosition().y -
						position.y * Constants.TILE_SIZE + 16);
				gunToTarget.normalize();
				gunToTarget.scale(5);
				
				Renderer.line(	position.x * Constants.TILE_SIZE + 16, 
								position.y * Constants.TILE_SIZE + 16,
								position.x * Constants.TILE_SIZE + 16 + gunToTarget.x, 
								position.y * Constants.TILE_SIZE + 16 + gunToTarget.y);				
			}
			//Renderer.ellipse(position.x * Constants.TILE_SIZE + 16, position.y
				//	* Constants.TILE_SIZE + 16, 5, 5);

			for (int i = 0; i < nodes.size(); i++) {
				nodes.get(i).render();
			}

			//
			for (int i = 0; i < guns.size(); i++) {
				guns.get(i).render();
			}

			// Draw sight Radius
			if(false){
			Renderer.noFill();
			Renderer.stroke(255, 0, 0);
			//Renderer.noStroke();
			//Renderer.fill(255, 0, 0, 64);
			Renderer.ellipse(position.x * Constants.TILE_SIZE + Constants.TILE_SIZE/2, position.y *
					Constants.TILE_SIZE + Constants.TILE_SIZE/2, sightRadius * 2, sightRadius * 2);
			}
			
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
	
	public boolean isCollidable(){
		return true;
	}

	/*
	 * Even if this turret is dead, it still needs to call update on the bullets
	 * that it launched.
	 */
	public void update(float deltaTime) {

		if(renderedLastFrame){
			renderedLastFrame = false;
		}
		else{
			target = null;
		}


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

		if(target != null){
			for (int i = 0; i < guns.size(); i++) {
				guns.get(i).fire();
			}
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

	public void onCollision(ICollidable collidable){
		
		if(collidable.getObjectType() == 1){
			IShot shot = (IShot)collidable;
			float power = shot.getPower();
			
			//Renderer.println("turret hit with power: " + shot.getPower());
			
			health -= power;
			
			if(health <= 0){
				 health = 0;
				 destroy();
			 }			
		}
	}
}