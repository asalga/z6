package z6;

import java.util.ArrayList;
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
public class Quadtrie {
	private Octant root;
	private int maxLevels;
	private boolean debugOn;
	private int numLeafsRendered;

	// A leaf node may have many tiles associated with it
	private int numTilesRendered;

	/**
	 * 
	 * 
	 */
	public Quadtrie() {
		root = null;
		maxLevels = 0;
		debugOn = false;
		numLeafsRendered = 0;
		numTilesRendered = 0;
	}

	/**
	 * A Quadtree node
	 * 
	 */
	private class Octant {
		Rectangle bounds;
		int level;
		boolean isLeaf;
		Octant northEast, northWest, southEast, southWest;
		ArrayList <Node> nodes;

		/**
		 * 
		 * @param x
		 * @param y
		 * @param w
		 * @param h
		 * @param level
		 */
		public Octant(Rectangle bounds, int level){
			northEast = northWest = southEast = southWest = null;
			isLeaf = false;
			this.level = level;
			this.bounds = new Rectangle(bounds.x, bounds.y, bounds.w, bounds.h);
		}

		/**
		 * Draw the lines that delineate the nodes
		 */
		private void drawDebugLines(Rectangle viewport) {

			if(false == Utils.testCollision(bounds, viewport)){
				return;
			}
			
			// We are drawing the lines that show the children,
			// so if this actually doesn't have children, it dosen't make sense
			// to draw the lines.
			if (debugOn == false || isLeaf == true) {
				return;
			}

			float x = bounds.x;
			float y = bounds.y;
			float w = bounds.w;
			float h = bounds.h;
			
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
			if (Utils.testCollision(viewport, bounds)) {

				// If this node is a leaf, we can render all the tiles that lay
				// inside it.
				if (isLeaf) {
					numLeafsRendered++;
					
					if(nodes != null){
						for(int i = 0; i < nodes.size(); i++){
							nodes.get(i).render();
						}
					}

					/*
					 * int tileColIndex = (int) x / Constants.TILE_SIZE; int
					 * tileRowIndex = (int) y / Constants.TILE_SIZE;
					 * 
					 * int tileColXoffset = Constants.TILE_SIZE * tileColIndex;
					 * int tileRowXoffset = Constants.TILE_SIZE * tileRowIndex;
					 * 
					 * for (int rowY = tileRowXoffset, rowIdx = tileRowIndex;
					 * rowY < y + h; rowY += Constants.TILE_SIZE, rowIdx++) {
					 * for (int colX = tileColXoffset, colIdx = tileColIndex;
					 * colX < x + w; colX += Constants.TILE_SIZE, colIdx++) {
					 * numTilesRendered++; Tile tile = gameMap.getTile(colIdx,
					 * rowIdx); if (tile != null) { Renderer.strokeWeight(1);
					 * 
					 * // parent.rect(colX, rowY, 32, 32);
					 * Renderer.image(tile.getImage(), colX, rowY); //
					 * image(tile.getImage(), colX, rowY); } } }
					 */
				} else {
					southEast.draw(viewport);
					southEast.drawDebugLines(viewport);

					southWest.draw(viewport);
					southWest.drawDebugLines(viewport);

					northWest.draw(viewport);
					northWest.drawDebugLines(viewport);

					northEast.draw(viewport);
					northEast.drawDebugLines(viewport);
				}
			}
		}
		
		/**
		 * Insert a Node into the tree.
		 * 
		 * @param n
		 * @param x
		 * @param y
		 */
		public boolean insert(Node n, int x, int y){
			
			// If the node fits into this octant
			if(Utils.isPointInBox(n.getPosition(), bounds)){
				
				if(isLeaf){
					if(nodes == null){
						nodes = new ArrayList<Node>();
					}
					nodes.add(n);
					return true;
				}
				else{
					if(northEast.insert(n, x, y)){
						return true;
					}
					
					if(northWest.insert(n, x, y)){
						return true;
					}
					
					if(southEast.insert(n, x, y)){
						return true;
					}
					
					if(southWest.insert(n, x, y)){
						return true;
					}
				}
			}
			
			return false;
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
			
			float x = bounds.x;
			float y = bounds.y;
			float w = bounds.w;
			float h = bounds.h;
			
			Rectangle northEastBounds = new Rectangle(x + w / 2, y, w / 2, h / 2);
			Rectangle northWestBounds = new Rectangle(x, y, w / 2, h / 2);
			Rectangle southEastBounds = new Rectangle(x + w / 2, y + h / 2, w / 2, h / 2);
			Rectangle southWestBounds = new Rectangle(x, y + h / 2, w / 2, h / 2);
			
			northEast = new Octant(northEastBounds, level + 1);
			northWest = new Octant(northWestBounds, level + 1);
			southEast = new Octant(southEastBounds, level + 1);
			southWest = new Octant(southWestBounds, level + 1);

			southEast.subdivide();
			southWest.subdivide();
			northEast.subdivide();
			northWest.subdivide();
		}
	}
	
	/**
	 * Insert a node into the tree.
	 * 
	 * @param n
	 * @param x
	 * @param y
	 */
	public void insert(Node n, int x, int y){
		root.insert(n, x, y);
	}
	
	/**
	 * maxLevels - clamped to 1
	 */
	public void load(int maxLevels) {

		// replace with clamp?
		if (maxLevels < 1) {
			this.maxLevels = 1;
		} else {
			this.maxLevels = maxLevels;
		}
		
		Rectangle rootBounds = new Rectangle(0, 0, 
				Constants.MAP_WIDTH_IN_TILES  * Constants.TILE_SIZE,
				Constants.MAP_HEIGHT_IN_TILES * Constants.TILE_SIZE);
		
		// Root node contains the entire grid
		root = new Octant(rootBounds, 1);
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
	 * Draws only the nodes that intersect with the viewport.
	 */
	public void draw(Rectangle viewport) {

		// Keep track of these for debugging purposes.
		numLeafsRendered = 0;
		numTilesRendered = 0;

		root.draw(viewport);

		// Use 'R' key to toggle reporting
		if (Keyboard.isKeyDown(Keyboard.R)) {
			debug(true);
		} else {
			debug(false);
		}

		if (debugOn) {
			root.drawDebugLines(viewport);
		}
	}
}
