package z6;

/**
 * Problem: rendering cloud octree was slow - because of alpha blending
 * 
 * Add short delay to guns so they don't fire exactly at the same time
 * Bullets should use quadtree
 * 
 * Guns get assigned a layer to target.
 * 
 * Remove turrets from being collidable after being destroyed since it may interfered
 * with shooting other nearby turrets
 * 
 * fix 256x256 map bug
 * 
 * Problem: When inserting nodes into the quadtree, if there are many levels deep we go,
 * the tile becomes larger than the cell which returns false.
 * 1 - Make quadrants able to hold cells
 * 2 - Prevent letting cells be made less than 32x32
 * 3 - Add toggle to hold these nodes
 * 4 - 
 */

import processing.core.PApplet;
import z6.Math.Vec2;
import z6.ParticleSystem.FlameViewBehaviour;
import z6.ParticleSystem.ParticleSystem;

import java.lang.System;
import java.util.ArrayList;

import org.apache.tools.ant.types.resources.Intersect;

/**
 * 
 */
public class Z6 extends PApplet {

	// Allow to easily access all the turrets in the game
	ArrayList<Node> turrets;
	static ArrayList<IShot> bullets;
	static ArrayList<ICollidable> bulletsC;
	private static ArrayList<ICollidable> collidables;
	
	ParticleSystem psys;

	private Quadtree terrainQuadtree;
	private Quadtree spriteQuadtree;
	private Quadtree cloudQuadtree;

	private Debugger debug;
	private Ticker spriteTicker;
	private Ship ship;
	
	// Viewports
	private GameCam gameCam;
	private Rectangle cloudViewport;
	
	private int NUM_TURRETS = 1250;
	
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
		//collidables.add(s);
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
		collidables = new ArrayList<ICollidable>();
		//collidableTurrets = new ArrayList<ICollidable>();

		// Don't make these too small, otherwise we can't place tiles in them.
		cloudQuadtree  = new Quadtree(GAME_WIDTH, GAME_HEIGHT, 6);
		spriteQuadtree = new Quadtree(GAME_WIDTH, GAME_HEIGHT, 10);
		terrainQuadtree = new Quadtree(GAME_WIDTH, GAME_HEIGHT, 5);

		Ticker ti = new Ticker();
		ti.tick();
		
		// TURRETS
		turrets = new ArrayList<Node>();
		for (int i = 0; i < NUM_TURRETS; i++) {
			
			int randTurret = Utils.getRandomInt(0, 2);
			
			Turret turret;
			
			switch(randTurret){
				case 0: turret = TurretFactory.create(Constants.MINIGUN_GUN_ID);break;
				case 1: turret = TurretFactory.create(Constants.CANNON_GUN_ID);break;
				case 2: turret = TurretFactory.create(Constants.LASER_GUN_ID);break;
				
				default:turret = TurretFactory.create(Constants.MINIGUN_GUN_ID);break;
			}
						
			turret.setPosition(
					Utils.getRandomInt(0, Constants.MAP_WIDTH_IN_TILES - 32 ),
					Utils.getRandomInt(0, Constants.MAP_HEIGHT_IN_TILES - 32  )
			 );
			
			// Update factory also.
			//IGun gun = new Gun();
			//gun.setFireBehaviour(new MinigunFireBehaviour());
			//gun.setShotSpeed(100);
			//gun.setFireRatePerSec(1.5f);
			//turret.addGun(gun);
			
			// Problem unlike bullets that just 
			// hit a shit, damage it and are destroyed, 
			// a laser damages a ship over time.
			

			
			turret.setLayer(1);
			
			turrets.add(turret);
			spriteQuadtree.insert(turret);
			
			collidables.add(turret);
		}
		ti.tick();
		Renderer.println(">>" + ti.getDeltaSec());
		
		Renderer.noiseSeed(1);
		///////////////////////////
		for(int c = 0; c < GAME_WIDTH/32;c++){
			for(int r = 0; r < GAME_HEIGHT/32; r++){
				
				float noiseVal = Renderer.noise(r/10f, c/10f);
				  
				if(noiseVal > 0.5f){
					SpriteNode cloud = new SpriteNode(c * 32, r * 32);
					cloud.setImage(Constants.CLOUD1);
					cloudQuadtree.insert(cloud);
				}
			}
		}
		cloudQuadtree.prune();
		
		
		//////////////////////////////	
		
		Renderer.println("quadrants before prune:  " + spriteQuadtree.getNumQuadrants());
		Renderer.println( "num sprites: "   + spriteQuadtree.getNumSprites());
		spriteQuadtree.prune();
		Renderer.println("" + spriteQuadtree.getNumSprites());
		Renderer.println("quadrants after prune: " + spriteQuadtree.getNumQuadrants());
		
		ship = new Ship();
		ship.setPosition(new Vec2(256 * 12, 256 * 12));
		
		
		//
		IGun miniGun = new Gun();
		
		MinigunFireBehaviour fb = new MinigunFireBehaviour();
		miniGun.setFireBehaviour(fb);
		miniGun.setLayer(1);
		miniGun.setShotSpeed(450);
		miniGun.setFireRatePerSec(10);
		
		IShot templateShot = new MiniGunShot();
		templateShot.setPower(50);
		fb.setShot(templateShot);
		
		ship.addGun(miniGun);
		
		/*IGun laserGun = new Gun();
		laserGun.setFireBehaviour(new ShipLaserFireBehaviour());
		laserGun.setLayer(1);
		laserGun.setTarget(turrets.get(0));
		ship.addGun(laserGun);*/
		
		collidables.add(ship);
		
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
				//Renderer.println("r: " +r + " c:" + c);
				Tile t = loader.getTile(r, c);
				SpriteNode n = new SpriteNode(r*32, c*32);
				n.setImage(t.id);
				terrainQuadtree.insert(n);
			}
		}
	}

	/**
	 * 
	 */
	public void update() {
		debug.clear();
				
		// Get Input
		if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
			spriteTicker.pause();
		} else {
			spriteTicker.resume();
		}

		spriteTicker.tick();
		float deltaTime = spriteTicker.getDeltaSec();

		// Every frame we add to this list all the turrets that
		// were rendered, which means they can be collidable.
		collidables.clear();
		collidables.add(ship);

		for (int i = 0; i < NUM_TURRETS; i++) {
			Turret t = (Turret) turrets.get(i);
			Node turretNode = (Node)t;
						
			if(t.renderedLastFrame){
				t.setTarget((Node) ship);
				collidables.add(t);
			}
		}
		//debug.addString("# collidable turrets" + collidables.size());
		


		// Use 'R' key to toggle reporting
		if (Keyboard.isKeyDown(Keyboard.R)) {
			//cloudQuadtrie.debug(true);
			spriteQuadtree.debug(true);
		} else {
			//cloudQuadtrie.debug(false);
			spriteQuadtree.debug(false);
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
		
		for(int obj = 0; obj < collidables.size(); obj++){
			
			for(int b = 0; b < bullets.size(); b++){
				 
				Rectangle objRect = collidables.get(obj).getBoundingRectangle();
				//Renderer.println("" + turretC.toString());

				ICollidable c = (ICollidable)bullets.get(b);
				Rectangle cb = c.getBoundingRectangle();

				if(c.isCollidable() == false){
					continue;
				}
				//Renderer.println("ship/turret: " + collidables.get(ct).getLayer());
				
				if (c.getLayer() != collidables.get(obj).getLayer()){
					continue;
				}
				
				 if( Utils.testCollision(objRect, cb)){
					ICollidable tc = collidables.get(obj);
					tc.onCollision(c);
					c.onCollision(tc);
				 }
				//Rectangle r2 =  bullets.get(i).getBoundingRectangle();
			}
		}
		
		
		// Iterate over all the collidables. If a collision occured,
		// send a message to the sprites.
		
		  ////////////////////
		  /*if(collidables.size() > 0){
		   // Rectangle currRect = collidables.get(0);
		    int currIndex = 0;
		    
		    // Do not compare static objects against each other
		    // 
		    // 
		    
		    // Get all turrets that are visible
		    
		    // compare each visible turret to each bullet
		    
		    
		    /*for(currIndex = 0; currIndex < collidables.size(); currIndex++){
		      for(int i = 0; i < collidables.size(); i++){
		    	
		        if(i == currIndex){continue;}
		        else{
		          Rectangle r1 = collidables.get(currIndex).getBoundingRectangle();
		          //Renderer.println("" + collidables.size());
		          Rectangle r2 =  collidables.get(i).getBoundingRectangle();
		          if(collidables.get(i).getLayer() == collidables.get(currIndex).getLayer() &&
		            Utils.testCollision(r1, r2)){
		        	  //testCollision( r1, r2)){
		        	 // print("HIT");
		            
		         //   collidables.get(i).onCollision(collidables.get(currIndex));
		          //  collidables.get(currIndex).onCollision(collidables.get(i)); 
		          }
		        }
		      }
		    }
		  }*/
		  
		  ////////
		
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
			terrainQuadtree.draw(viewport);
			perfChecker.tick();		
			//debug.addString("terrain perf: " + perfChecker.getDeltaSec());
						
			spriteQuadtree.draw(viewport);
			
			// Render all the bullets
			for (IShot s : bullets) {
				s.render();
			}
		popMatrix();
		
		perfChecker.tick();
		
		//debug.addString("Tiles Rendered: " + terrain.getNumTilesRendered()
		//		+ " in " + perfChecker.getDeltaSec() * 1000 + "ms");

		debug.addString("Num bullets:" + bullets.size());
		//debug.addString("Ship: " + ship.getPosition());
		
		debug.addString("Sprites rendered: " + spriteQuadtree.getNumLeafsRendered() + "/" + NUM_TURRETS);
		//debug.addString("SpriteQT numQuadrants: " + spriteQuadtrie.getNumQuadrants());
		
		debug.addString("Ship health: " + ship.getHealth());
		
		// Draw User
		ship.render();

		// Draw Clouds, mostly for parallax
		perfChecker.tick();
		if(Keyboard.isKeyDown(Keyboard.KEY_C)){
			pushMatrix();
			translate(-cloudViewport.x, -cloudViewport.y);
			cloudQuadtree.draw(cloudViewport);
			popMatrix();
		}
		perfChecker.tick();
		
		//debug.addString("Cloud perf: " + perfChecker.getDeltaSec());
		//debug.addString("Cloud tiles drawn: " + cloudQuadtrie.getNumTilesRendered());
		//debug.addString("Cloud leaves drawn: " + cloudQuadtrie.getNumLeafsRendered());

		debug.addString("Terrain tiles drawn: " + terrainQuadtree.getNumTilesRendered());		
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

/*IGun laserGun = new Gun();
laserGun.setFireBehaviour(new LaserFireBehaviour());
laserGun.setFireRatePerSec(2f);
turret.addGun(laserGun);*/