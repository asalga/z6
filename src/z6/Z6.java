package z6;

// - Turrets need to fire at center of ship
// - Add ship shooting
// - Remove bullet when appropriate
// - Create Repository
// - Figure out how gun works
// - Fix creating empty cells in quadtrie
// - Fix GC problems

import processing.core.PApplet;
import z6.ParticleSystem.FlameViewBehaviour;
import z6.ParticleSystem.ParticleSystem;

import java.lang.System;
import java.util.ArrayList;

/**
 * 
 */
public class Z6 extends PApplet {

	// Allow to easily access all the turrets in the game
	ArrayList<Node> turrets;
	static ArrayList<IShot> bullets;

	ParticleSystem psys;

	private Quadtree terrain;
	private Quadtree clouds;

	private Quadtrie spriteQuadtrie;

	private Rectangle cloudViewport;
	private Debugger debug;

	private Ticker spriteTicker;

	private Ship ship;
	private GameCam gameCam;

	private int NUM_TURRETS = 100;

	private Ticker perfChecker = new Ticker();
	
	/*
	 * public static void main(String args[]) { PApplet.main(new String[] {
	 * z6.Z6.class.getName() }); }
	 */

	public static void AddBullet(IShot s) {
		//Renderer.println("adding bullet");
		bullets.add(s);
	}

	public void setup() {
		size(500, 500);

		Renderer.setup(this);
		debug = new Debugger(this);
		spriteTicker = new Ticker();

		bullets = new ArrayList<IShot>();

		cloudViewport = new Rectangle(0, 0, width, height);

		spriteQuadtrie = new Quadtrie();
		spriteQuadtrie.load(9);

		// TURRETS
		turrets = new ArrayList<Node>();
		for (int i = 0; i < NUM_TURRETS; i++) {
			Turret turret = TurretFactory.create(Constants.CANNON_GUN_ID);
			//turret.setPosition(i, i);
			
			turret.setPosition(
			 Utils.getRandomInt(0, 150),
			 Utils.getRandomInt(0, 150));
			
			
			turrets.add(turret);
			spriteQuadtrie.insert(turret, (int) turret.getPosition().x,
					(int) turret.getPosition().y);
			// collidables.add(turret); ADD THIS!!!
		}
		// collidables.add(ship); ADD THIS!!

		ship = new Ship();
		ship.setPosition(new Vec2(200, 200));

		int numParticles = Utils.getRandomInt(4, 5);

		/*
		 * Test Particle system strategy
		 * 
		 * Takes case of how a particle's view changes over time (or distance?)
		 * 
		 * RenderBehaviour flameViewBehaviour = new FlameViewBehaviour();
		 * flameViewBehaviour.setTexture(0, "texture");
		 * flameViewBehaviour.setTexture(1,"something else");
		 * 
		 * RainViewBehaviour rainViewBehaviour = new RainViewBehaviour();
		 * rainViewBehaviour
		 * 
		 * ParticleSystem pSys = new ParticleSystem(); pSys.setOneShot(false);
		 * pSys.setEmitRatePerSec(10); pSys.setPreWarm(false);
		 * 
		 * pSys.update(deltaTime); pSys.render();
		 * 
		 * pSys.setBoundingArea(new Rectangle(0,0, 100, 100));
		 * pSys.setMovementBehaviour(flameMovementBehaviour);
		 * pSys.setRenderBehaviour(flameViewBehaviour);
		 */

		/*
		 * psys = new ParticleSystem(100); //particleSystems.add(psys);
		 * psys.setPosition(new Vec2(40, 40)); psys.setAcceleration(new Vec2(0,
		 * 0)); psys.setParticleLifeTime(0.5f, 5.0f);
		 * psys.setParticleVelocity(new Vec2(-10, -10), new Vec2(10, 10));
		 * psys.setViewBehaviour(new FlameViewBehaviour()); psys.emit(30);
		 */

		gameCam = new GameCam();
		gameCam.setTarget((Node) ship);
		gameCam.setViewport(new Rectangle(0, 0, width, height));

		IMapLoader cloudsLoader = new ProcCloudGenLoader(
				Constants.MAP_WIDTH_IN_TILES, Constants.MAP_HEIGHT_IN_TILES);
		cloudsLoader.load();

		IMapLoader loader = new ProcMapGenLoader(Constants.MAP_WIDTH_IN_TILES,
				Constants.MAP_HEIGHT_IN_TILES);
		loader.load();

		clouds = new Quadtree();
		clouds.load(cloudsLoader.getMap(), 7);

		terrain = new Quadtree();
		terrain.load(loader.getMap(), 8);
	}

	/**
	 * 
	 */
	public void update() {
		debug.clear();

		spriteTicker.tick();
		float deltaTime = spriteTicker.getDeltaSec();

		for (int i = 0; i < NUM_TURRETS; i++) {
			Turret t = (Turret) turrets.get(i);
			t.setTarget((Node) ship);
		}

		//
		// Get Input
		//
		if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
			spriteTicker.pause();
		} else {
			spriteTicker.resume();
		}

		//
		// Move things
		//

		//
		for (Node n : turrets) {
			n.update(deltaTime);
		}

		// Update user's ship
		ship.update(deltaTime);
		// psys.update(deltaTime);

		for (IShot b : bullets) {
			b.update(deltaTime);
		}

		gameCam.update(deltaTime);
		debug.addString("FPS: " + Math.floor(Renderer.frameRate()));
	}

	/**
	 * 
	 */
	public void draw() {
		update();
		
		background(0);

		Rectangle viewport = gameCam.getViewport();
		cloudViewport.x = viewport.x * 1.1f;
		cloudViewport.y = viewport.y * 1.1f;

		// Draw ground
		perfChecker.tick();

		pushMatrix();
		translate(-viewport.x, -viewport.y);
		terrain.draw(viewport);
		spriteQuadtrie.draw(viewport);
		for (IShot s : bullets) {
			s.render();
		}
		popMatrix();
		
		perfChecker.tick();
		
		debug.addString("Tiles Rendered: " + terrain.getNumTilesRendered()
				+ " in " + perfChecker.getDeltaSec() * 1000 + "ms");

		debug.addString("Num bullets:" + bullets.size());
		
		// Drawing all turrets is too expensive, they were added to an quadtree.
		// pushMatrix();
		// translate(-viewport.x, -viewport.y);
		// for(Node t : turrets){
		// t.render();
		// }
		// popMatrix();

		// Draw User
		ship.render();

		// Draw Clouds
		/*pushMatrix();
		translate(-cloudViewport.x, -cloudViewport.y);
		clouds.draw(cloudViewport);
		popMatrix();*/

		// psys.draw();

		// P - pause ,  R - report on quadtree
		Keyboard.lockKeys(new int[] { Keyboard.KEY_R, Keyboard.KEY_P });

		// Draw debugging stuff on top of everything else.
		debug.draw();
		perfChecker.reset();
	}

	public void keyPressed() {
		Keyboard.setKeyDown(keyCode, true);
	}

	public void keyReleased() {
		Keyboard.setKeyDown(keyCode, false);
	}
	
	/*public void removeCollider(ICollidable colliderToRemove) {
		for (int i = 0; i < collidables.size(); i++) {
			if (collidables.get(i) == colliderToRemove) {
				collidables.remove(i);
				break;
			}
		}
	}*/
}
