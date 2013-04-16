package z6;

/**
 * Problem: rendering cloud octree was slow - because of alpha blending
 * 
 * 
 * Things have to hurt ship
 * Ship needs to shoot
 * Add short delay to guns so they don't fire exactly at the same time
 * Bullets should use quadtrie
 * 
 * 
 * Problem: When inserting nodes into the quadtree, if there are many levels deep we go,
 * the tile becomes larger than the cell which returns false.
 * 1 - Make quadrants able to hold cells
 * 2 - Prevent letting cells be made less than 32x32
 * 3 -
 * 4 - 
 * 
 */

import processing.core.PApplet;
import z6.Math.Vec2;
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

	private Quadtree terrainQuadtrie;
	private Quadtree spriteQuadtrie;
	private Quadtree cloudQuadtrie;

	private Debugger debug;
	private Ticker spriteTicker;
	private Ship ship;
	
	// Viewports
	private GameCam gameCam;
	private Rectangle cloudViewport;
	
	private int NUM_TURRETS = 5000;
	
	private int GAME_WIDTH  = Constants.MAP_WIDTH_IN_TILES  * Constants.TILE_SIZE;
	private int GAME_HEIGHT = Constants.MAP_HEIGHT_IN_TILES * Constants.TILE_SIZE;
	
	private int GAME_W_IN_TILES = Constants.MAP_WIDTH_IN_TILES;
	private int GAME_H_IN_TILES = Constants.MAP_HEIGHT_IN_TILES;

	private Ticker perfChecker = new Ticker();
	
	/*
	 * public static void main(String args[]) { PApplet.main(new String[] {
	 * z6.Z6.class.getName() }); }
	 */

	public static void AddBullet(IShot s) {
		bullets.add(s);
	}

	/**
	 * 
	 */
	public void setup() {
		size(500, 500);

		Renderer.setup(this);

		// P - pause 
		// R - report on quadtree
		// C - Draw clouds
		Keyboard.lockKeys(new int[] { Keyboard.KEY_R, Keyboard.KEY_P, Keyboard.KEY_C });

		debug = new Debugger(this);
		spriteTicker = new Ticker();

		bullets = new ArrayList<IShot>();

		// Don't make these too small, otherwise we can't place tiles in them.
		cloudQuadtrie  = new Quadtree(GAME_WIDTH, GAME_HEIGHT, 6);
		spriteQuadtrie = new Quadtree(GAME_WIDTH, GAME_HEIGHT, 6);
		terrainQuadtrie = new Quadtree(GAME_WIDTH, GAME_HEIGHT, 6);

		// TURRETS
		turrets = new ArrayList<Node>();
		for (int i = 0; i < NUM_TURRETS; i++) {
			
			//Turret turret = TurretFactory.create(Constants.CANNON_GUN_ID);
			Turret turret = TurretFactory.create(Constants.LASER_GUN_ID);
			
			turret.setPosition(
			 Utils.getRandomInt(0, Constants.MAP_WIDTH_IN_TILES - 32 ),
			 Utils.getRandomInt(0, Constants.MAP_HEIGHT_IN_TILES - 32  ));
			
			// Update factory also.
			IGun gun = new Gun();
			gun.setFireBehaviour(new MinigunFireBehaviour());
			gun.setShotSpeed(300);
			gun.setFireRatePerSec(10);
			turret.addGun(gun);
			
			turrets.add(turret);
			spriteQuadtrie.insert(turret);
			
			// collidables.add(turret); ADD THIS!!!
		}
		// collidables.add(ship); ADD THIS!!
		
		Renderer.noiseSeed(1);
		///////////////////////////
		for(int c = 0; c < GAME_WIDTH/32;c++){
			for(int r = 0; r < GAME_HEIGHT/32; r++){
				
				float noiseVal = Renderer.noise(r/10f, c/10f);
				  
				if(noiseVal > 0.46){
					SpriteNode cloud = new SpriteNode(c * 32, r * 32);
					cloud.setImage(Constants.CLOUD1);
					cloudQuadtrie.insert(cloud);
				}
			}
		}
		cloudQuadtrie.prune();
		
		
		//////////////////////////////	
		
		Renderer.println("quadrants before prune:  " + spriteQuadtrie.getNumQuadrants());
		Renderer.println( "num sprites: "   + spriteQuadtrie.getNumSprites());
		spriteQuadtrie.prune();
		Renderer.println("" + spriteQuadtrie.getNumSprites());
		Renderer.println("quadrants after prune: " + spriteQuadtrie.getNumQuadrants());
		
		ship = new Ship();
		ship.setPosition(new Vec2(256 * 1, 256 * 1));

		int numParticles = Utils.getRandomInt(4, 5);


		gameCam = new GameCam();
		gameCam.setTarget((Node) ship);
		gameCam.setViewport(new Rectangle(0, 0, width, height));
		
		cloudViewport = new Rectangle(0, 0, width, height);
		
		
		/////////   TERRAIN   /////////
		IMapLoader loader = new ProcMapGenLoader( GAME_W_IN_TILES, GAME_H_IN_TILES);
		loader.load();

		for(int r = 0; r < loader.getWidth(); r++){
			for(int c = 0; c < loader.getHeight(); c++){
				Tile t = loader.getTile(r, c);
				SpriteNode n = new SpriteNode(r*32, c*32);
				n.setImage(t.id);
				terrainQuadtrie.insert(n);
			}
		}
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
			Node turretNode = (Node)t;
			
			if(t.renderedLastFrame){
				t.setTarget((Node) ship);
			}
		}
		
		//
		// Get Input
		//
		if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
			spriteTicker.pause();
		} else {
			spriteTicker.resume();
		}

		// Use 'R' key to toggle reporting
		if (Keyboard.isKeyDown(Keyboard.R)) {
			cloudQuadtrie.debug(true);
		} else {
			cloudQuadtrie.debug(false);
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
		
		ArrayList<IShot> bulletsToRemove = new ArrayList<IShot>();

		for (IShot b : bullets) {
			b.update(deltaTime);
			if (b.isAlive() == false) {
				bulletsToRemove.add(b);
			}
		}
		
		for(IShot toRemove : bulletsToRemove){
			bullets.remove(toRemove);
		}
		
		gameCam.update(deltaTime);
		debug.addString("FPS: " + Math.floor(Renderer.frameRate()));
	}

	
	
	/**
	 * 
	 */
	public void draw() {
		update();
		perfChecker.reset();
		background(0);

		Rectangle viewport = gameCam.getViewport();
		cloudViewport.x = viewport.x * 1.3f;
		cloudViewport.y = viewport.y * 1.3f;

		// Draw ground
		perfChecker.tick();
		pushMatrix();
			translate(-viewport.x, -viewport.y);
			terrainQuadtrie.draw(viewport);
			perfChecker.tick();		
			debug.addString("terrain perf: " + perfChecker.getDeltaSec());
						
			spriteQuadtrie.draw(viewport);
			
			// Render all the bullets
			for (IShot s : bullets) {
				s.render();
			}
		popMatrix();
		
		perfChecker.tick();
		
		//debug.addString("Tiles Rendered: " + terrain.getNumTilesRendered()
		//		+ " in " + perfChecker.getDeltaSec() * 1000 + "ms");

		//debug.addString("Num bullets:" + bullets.size());
		debug.addString("Ship: " + ship.getPosition());
		
		debug.addString("Sprites rendered: " + spriteQuadtrie.getNumLeafsRendered() + " /" + NUM_TURRETS);
		//debug.addString("SpriteQT numQuadrants: " + spriteQuadtrie.getNumQuadrants());
		
		debug.addString("bullet count: " + bullets.size());
		
		// Draw User
		ship.render();

		// Draw Clouds, mostly for parallax
		perfChecker.tick();
		if(Keyboard.isKeyDown(Keyboard.KEY_C)){
			pushMatrix();
			translate(-cloudViewport.x, -cloudViewport.y);
			cloudQuadtrie.draw(cloudViewport);
			popMatrix();
		}
		perfChecker.tick();
		
		debug.addString("Cloud perf: " + perfChecker.getDeltaSec());
		debug.addString("Cloud tiles drawn: " + cloudQuadtrie.getNumTilesRendered());
		debug.addString("Cloud leaves drawn: " + cloudQuadtrie.getNumLeafsRendered());

		//debug.addString("Terrain tiles drawn: " + terrain.getNumTilesRendered());		
		//debug.addString("Terrain leaves drawn: " + terrain.getNumLeafsRendered());
		// psys.draw();

		// Draw debugging stuff on top of everything else.
		debug.draw();
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


//
/*IMapLoader cloudsLoader = new ProcCloudGenLoader(
		Constants.MAP_WIDTH_IN_TILES, Constants.MAP_HEIGHT_IN_TILES);
cloudsLoader.load();*/

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
