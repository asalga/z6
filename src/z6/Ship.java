package z6;

import java.util.ArrayList;
import z6.Math.Vec2;

/**
 * TODO:
 * fix deg_to_rad
 * allow getting hit
 * 
 * 
 * @author asalga
 *
 */
public class Ship implements Node, IBroadcaster, ICollidable {

	private Vec2 position;
	private Vec2 direction;

	private float degreesPerSecond;
	private float rotation;

	private float speed;
	private ArrayList<Node> children;
	//private ArrayList<IGun> guns;
	
	private ArrayList<ISubscriber> subscribers;

	//TransformNode child;

	public Ship() {
		//guns = new ArrayList<IGun>();
		children = new ArrayList<Node>();
		subscribers = new ArrayList<ISubscriber>();
		
		setSpeed(0);
		setRotateSpeed(0);
		rotation = 0f;
		setPosition(new Vec2(0, 0));
		setPosition(Vec2.makeZeroVector());
		setDirection(new Vec2(1,1));

		//child = new TransformNode();
		// child.setPosition(scaleVec(getUpVector(), 5));

		// SceneNode test = new SpriteNode();
		// child.addChild(test);
	}

	// TODO: fix me
	public int getLayer() {
		return 2;
	}

	public void setLayer(int layerID) {

	}

	public Rectangle getBoundingRectangle() {
		return new Rectangle(position.x - 16, position.y - 16, 32, 32);
	}
	
	public void onCollision(ICollidable collider) {
		Renderer.println("ship was hit");
	}

	public void onCollision() {
	}

	public void render() {
		// Ticker perfChecker = new Ticker();
		// perfChecker.tick();

		Renderer.pushMatrix();

		float xPos = Renderer.WIDTH / 2;
		float yPos = Renderer.HEIGHT / 2;

		if (position.x < 250) {
			xPos = position.x;
		}
		if (position.y < 250) {
			yPos = position.y;
		}

		//Renderer.fill(128, 64);
		Renderer.translate(xPos, yPos);
		Renderer.rotate(-rotation * 0.01745329238f);
		
		//Renderer.rect(-16,-16, 32, 32);
		Renderer.image(new Tile(Constants.SHIP).getImage(), -16, -16);
		Renderer.popMatrix();

		/*Renderer.pushMatrix();
		Renderer.translate(width / 2, height / 2);
		child.render();
		Renderer.popMatrix();*/

		/*pushMatrix();
		PVector camPos = gameCam.getTopLeft();
		translate(-camPos.x, -camPos.y);
		for (IGun g : guns) {
			g.render();
		}
		popMatrix();*/

		// pushStyle();
		// noFill();
		// stroke(255,0,0);
		// rect(width/2-16,height/2-16,32,32);
		// popStyle();
		// perfChecker.tick();
		// debug.addString("Ship render: " + perfChecker.getDeltaSec());
	}

	public void setRotateSpeed(float degPerSec) {
		degreesPerSecond = degPerSec;
	}

	public String toString() {
		return "pos(" + position.x + ", " + position.y + ")";// + " vel(" +
																// velocity.x +
																// ", " +
																// velocity.y +
																// ")";
	}

	public void update(float deltaTime) {

		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			setSpeed(150f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			setSpeed(0f);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			setRotateSpeed(180f);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			setRotateSpeed(-180f);
		} else {
			setRotateSpeed(0f);
		}

		//for (IGun g : guns) {
		//	g.update(deltaTime);
		//}

		position.x += speed * deltaTime * Math.sin(rotation * 0.01745329238f);// direction.x;
		position.y += speed * deltaTime * Math.cos(rotation * 0.01745329238f);// direction.y;

		rotation += degreesPerSecond * deltaTime;

		setDirection(new Vec2((float)Math.sin(rotation * 0.01745329238f), (float)Math.cos(rotation
				* 0.01745329238f)));

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			//for (IGun g : guns) {
			//	g.fire();
			//}
		}
		notifySubscribers();
	}

	public void addChild(Node node) {
	}

	public void setParent(Node node) {
	}

	public void setPosition(Vec2 position) {
		this.position = position;
	}

	public void setDirection(Vec2 dir) {
		this.direction = dir;
		this.direction.normalize();

		//for (IGun g : guns) {
		//	g.setDirection(this.direction);
		//}
	}

	/**
	 * 
	 */ 
	public void notifySubscribers(){
		for(int i = 0; i < subscribers.size(); i++){
			// TODO: check this
			subscribers.get(i).updatePosition(this.position);
		}
	}
	
	public void addSubscriber(ISubscriber subscriber){
		subscribers.add(subscriber);
	}
	
	public void removeSubscriber(ISubscriber subscriber){
		subscribers.remove(subscriber);
	}
	
	public Vec2 getPosition() {
		return position;
		// return new PVector(position.x - width/2, position.y - height/2);
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void addGun(IGun gun) {
		/*IGun g = gun;
		guns.add(gun);
		((Node) gun).setParent((Node) this);
		((Node) gun).setPosition(getZeroVector());

		g.setLayer(1);

		// gun.getID()
		//if (g.getID() == CANNON_GUN_ID) {
		//	g.setFireRatePerSec(2);
		//}*/
	}

	/*
	 * Assign all guns a specific tag
	 */
	private void setTagForGuns(int tag) {
		// gun.setTag(tag);
	}
}