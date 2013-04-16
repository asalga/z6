package z6;

import processing.core.PApplet;
import processing.core.PImage;

public class Tile {
	public int id;

	/*
	 * String path = "C:\\Users\\asalga\\Dropbox\\demos\\orchard\\";
	 * 
	 * PImage water = loadImage(path + "water.png");
	 */
	// C:\\Users\\asalga\\workspace\\z6\\src\\data\\grass.png
	public static PImage grass = Renderer.loadImage("data\\grass.png");
	public static PImage water = Renderer.loadImage("data\\water.png");
	public static PImage trees = Renderer.loadImage("data\\trees.png");
	public static PImage cloud1 = Renderer.loadImage("data\\cloud1.png");
	public static PImage cloud2 = Renderer.loadImage("data\\cloud2.png");
	public static PImage ship = Renderer.loadImage("data\\ship.png");

	public Tile(int i) {
		id = i;
	}

	PImage getImage() {
		switch (id) {
		case Constants.GRASS:
			return grass;
		case Constants.WATER:
			return water;
		case Constants.TREES:
			return trees;
		case Constants.CLOUD1:
			return cloud1;
		case Constants.CLOUD2:
			return cloud2;
		case Constants.SHIP:
			return ship;
		}
		return null;
	}
}