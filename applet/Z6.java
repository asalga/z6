package z6;

import processing.core.PApplet;
import java.lang.System;
import java.util.ArrayList;

public class Z6 extends PApplet {

	ArrayList<Node> turrets;

	private Quadtree terrain;
	private Quadtree clouds;

	private Quadtrie test;

	private Rectangle cloudViewport;
	private Debugger debug;

	private Ticker spriteTicker;

	private Ship ship;
	private GameCam gameCam;

	private int NUM_TURRETS = 1000;

	public void setup() {
		size(500, 500);

		Renderer.setup(this);
		debug = new Debugger(this);
		spriteTicker = new Ticker();
		turrets = new ArrayList<Node>();
		cloudViewport = new Rectangle(0, 0, width, height);

		test = new Quadtrie();
		test.load(9);

		for (int i = 0; i < NUM_TURRETS; i++) {
			Turret turret = TurretFactory.create(0);
			turret.setPosition(Utils.getRandomInt(0, 150),
					Utils.getRandomInt(0, 150));
			turrets.add(turret);
			test.insert(turret, (int) turret.getPosition().x,
					(int) turret.getPosition().y);
			// collidables.add(turret); ADD THIS!!!
		}
		// collidables.add(ship); ADD THIS!!

		ship = new Ship();
		ship.setPosition(new Vec2(200, 200));

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

		// for (Node n : turrets) {
		// n.update(deltaTime);
		// }

		// Update user's ship
		ship.update(deltaTime);

		gameCam.update(deltaTime);
	}

	/**
	 * 
	 */
	public void draw() {
		update();

		debug.addString("FPS: " + Math.floor(Renderer.frameRate()));

		Ticker perfChecker = new Ticker();

		background(0);

		Rectangle viewport = gameCam.getViewport();
		cloudViewport.x = viewport.x * 1.1f;
		cloudViewport.y = viewport.y * 1.1f;
		
		// Draw ground
		perfChecker.tick();
		pushMatrix();
		translate(-viewport.x, -viewport.y);
		terrain.draw(viewport);
		test.draw(viewport);

		popMatrix();
		perfChecker.tick();
		debug.addString("Tiles Rendered: " + terrain.getNumTilesRendered()
				+ " in " + perfChecker.getDeltaSec() * 1000 + "ms");

		// Drawing all turrets is too expensive, they were added to an octree.
		// pushMatrix();
		// translate(-viewport.x, -viewport.y);
		// for(Node t : turrets){
		// t.render();
		// }
		// popMatrix();

		// Draw User
		ship.render();

		// Draw Clouds
		pushMatrix();
		translate(-cloudViewport.x, -cloudViewport.y);
		clouds.draw(cloudViewport);
		popMatrix();

		// P - pause
		// R - report octree
		Keyboard.lockKeys(new int[] { Keyboard.KEY_R, Keyboard.KEY_P });

		// Draw debugging stuff on top of everything else.
		debug.draw();
	}

	public void keyPressed() {
		Keyboard.setKeyDown(keyCode, true);
	}

	public void keyReleased() {
		Keyboard.setKeyDown(keyCode, false);
	}

}
