package z6;

import processing.core.PApplet;

/*
 * Selectively render parts of a tilegrid depending on the viewport position and dimensions.
 * When specifying the number of levels, we start at 1
 * Levels
 * 1 - 1 leaf
 * 2 - 4 leafs
 * 3 - 16 leafs
 * 4 - 64 leafs
 * 5 - 256 leafs
 * 6 - 1024 leafs
 * 7 - 4096 leafs
 * 8 - 16,384 leafs
 * 9 - 65,536 leafs
 * Should Quadtree contain map??
 */
public class Quadtree {
	private Octant root;
	private int maxLevels;
	private Map gameMap;
	private boolean debugOn;
	private int numLeafsRendered;

	// A leaf node may have many tiles associated with it
	private int numTilesRendered;

	/**
	 * 
	 * 
	 */
	public Quadtree() {
		root = null;
		maxLevels = 0;
		gameMap = null;
		debugOn = false;
		numLeafsRendered = 0;
		numTilesRendered = 0;
	}

	private class Octant {
		int x, y, w, h;
		int level;
		boolean isLeaf;
		Octant northEast, northWest, southEast, southWest;

		public Octant(int x, int y, int w, int h, int level) {
			northEast = northWest = southEast = southWest = null;
			isLeaf = false;
			this.level = level;
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}

		/**
		 * Draw the lines that delineate the nodes
		 */
		private void drawDebugLines() {

			// We are drawing the lines that show the children,
			// so if this actually doesn't have children, it dosen't make sense
			// to draw the lines.
			if (debugOn == false || isLeaf == true) {
				return;
			}

			Renderer.pushStyle();
			Renderer.fill(0, 50 * level);
			Renderer.textSize(150 - (level * 30));
			Renderer.text("" + level, x + (w / 2), y + (h / 2));

			// Black, Red, Green, Blue, Grey, ...
			switch (level) {
			case 0:
				Renderer.stroke(0, 0, 0);
				break;
			case 1:
				Renderer.stroke(255, 0, 0);
				break;
			case 2:
				Renderer.stroke(0, 255, 0);
				break;
			case 3:
				Renderer.stroke(0, 0, 255);
				break;
			case 4:
				Renderer.stroke(128, 128, 128);
				break;
			case 5:
				Renderer.stroke(0, 128, 256);
				break;
			default:
				Renderer.stroke(0, 255, 255);
				break;
			}

			Renderer.strokeWeight(maxLevels - level);

			// vertical lines that show the children
			Renderer.line(x + (w / 2), y, x + (w / 2), y + h);

			// horizontal
			Renderer.line(x, y + (h / 2), x + w, y + (h / 2));

			Renderer.popStyle();
		}

		/*
		 * If this node is a leaf, find all the tiles that intersect with it and
		 * draw them. If this node is a parent, recursively draw the children.
		 */
		public void draw(Rectangle viewport) {

			// If the viewport intersects with the node, we can need to draw it.
			if (Utils.testCollision(viewport, new Rectangle(x, y, w, h))) {

				// If this node is a leaf, we can render all the tiles that lay
				// inside it.
				if (isLeaf) {
					numLeafsRendered++;

					int tileColIndex = (int) x / Constants.TILE_SIZE;
					int tileRowIndex = (int) y / Constants.TILE_SIZE;

					int tileColXoffset = Constants.TILE_SIZE * tileColIndex;
					int tileRowXoffset = Constants.TILE_SIZE * tileRowIndex;

					for (int rowY = tileRowXoffset, rowIdx = tileRowIndex; rowY < y
							+ h; rowY += Constants.TILE_SIZE, rowIdx++) {
						for (int colX = tileColXoffset, colIdx = tileColIndex; colX < x
								+ w; colX += Constants.TILE_SIZE, colIdx++) {
							numTilesRendered++;
							Tile tile = gameMap.getTile(colIdx, rowIdx);
							if (tile != null) {
								Renderer.strokeWeight(1);

								// parent.rect(colX, rowY, 32, 32);
								Renderer.image(tile.getImage(), colX, rowY);
								// image(tile.getImage(), colX, rowY);
							}
						}
					}
				} else {
					southEast.draw(viewport);
					southEast.drawDebugLines();

					southWest.draw(viewport);
					southWest.drawDebugLines();

					northWest.draw(viewport);
					northWest.drawDebugLines();

					northEast.draw(viewport);
					northEast.drawDebugLines();
				}
			}
		}

		/*
		 * Subdivide this node into 4 children.
		 */
		public void subdivide() {

			// If we reached the number of levels the user wanted, we
			// declare this node to be a leaf and back out.
			if (maxLevels == level) {
				isLeaf = true;
				return;
			}

			northEast = new Octant(x + w / 2, y, w / 2, h / 2, level + 1);
			northWest = new Octant(x, y, w / 2, h / 2, level + 1);
			southWest = new Octant(x, y + h / 2, w / 2, h / 2, level + 1);
			southEast = new Octant(x + w / 2, y + h / 2, w / 2, h / 2,
					level + 1);

			southEast.subdivide();
			southWest.subdivide();
			northEast.subdivide();
			northWest.subdivide();
		}
	}

	/*
	 * filepath - maxLevels - must be >= 1
	 */
	public void load(Map gameMap, int maxLevels) { // String filepath, int
													// maxLevels){
		this.gameMap = gameMap;

		// replace with clamp?
		if (maxLevels < 1) {
			this.maxLevels = 1;
		} else {
			this.maxLevels = maxLevels;
		}

		// Root node contains the entire grid
		root = new Octant(0, 0, Constants.MAP_WIDTH_IN_TILES
				* Constants.TILE_SIZE, Constants.MAP_HEIGHT_IN_TILES
				* Constants.TILE_SIZE, 1);
		root.subdivide();
	}

	/*
	 * Get the number of leaf nodes rendered on the last frame.
	 */
	public int getNumLeafsRendered() {
		return numLeafsRendered;
	}

	/*
	 * Get the number of tiles drawn on the last frame.
	 */
	public int getNumTilesRendered() {
		return numTilesRendered;
	}

	/*
	 * If debug is on, lines showing nodes will be rendered.
	 */
	public void debug(boolean isOn) {
		debugOn = isOn;
	}

	/**
	 * 
	 */
	public void draw(Rectangle viewport) {
		numLeafsRendered = 0;
		numTilesRendered = 0;

		root.draw(viewport);

		if (Keyboard.isKeyDown(Keyboard.R)) {
			debug(true);
		} else {
			debug(false);
		}

		if (debugOn) {
			root.drawDebugLines();
		}
	}
}
