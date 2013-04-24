package z6;

import java.util.ArrayList;
import processing.core.PApplet;

/**
 * Sparse Quadtree
 * 
 * TODO: - Make getNumQuadrants O(1) if node fits - Allow adding nodes that are
 * larger than the smallest (leaf) nodes - Allow adding sprites between nodes?
 * 
 * Selectively render parts of a tilegrid depending on the viewport position and
 * dimensions. When specifying the number of levels, we start at 1 Levels 1 - 1
 * leaf 2 - 4 leafs 3 - 16 leafs 4 - 64 leafs 5 - 256 leafs 6 - 1024 leafs 7 -
 * 4096 leafs 8 - 16,384 leafs 9 - 65,536 leafs
 */
public class Quadtree {
	private Quadrant root;
	private int maxLevels;
	private boolean debugOn;

	private final int NE = 0;
	private final int NW = 1;
	private final int SE = 2;
	private final int SW = 3;

	// Prevent needing to traverse the entire tree to get these counts.
	// private int numSprites;
	private int numQuadrants;
	private int numLeaves;
	private int numSpritesInTree;

	// The number of tiles rendered in the last frame.
	// A leaf node may have many tiles associated with it
	private int numTilesRendered;
	private int numLeafsRendered;

	/**
	 * 
	 * 
	 */
	public Quadtree(int width, int height, int _maxLevels) {

		// replace with clamp?
		if (_maxLevels < 1) {
			maxLevels = 1;
		} else {
			maxLevels = _maxLevels;
		}

		Rectangle rootBounds = new Rectangle(0, 0, width, height);
		root = new Quadrant(rootBounds, 1);

		debugOn = false;
		numLeafsRendered = 0;
		numTilesRendered = 0;

		numSpritesInTree = 0;
		numQuadrants = 0;
		numLeaves = 0;
	}

	/**
	 * Returns the number of sprites in the tree in O(1) time.
	 * 
	 * @return the number of sprites inside the quadtree.
	 * 
	 */
	public int getNumSprites() {
		return numSpritesInTree;
		// return root.getNumSpritesInChildren();
	}

	/**
	 * Traverse the tree and find any branches that do not hold any leaf nodes
	 * and mark those as null to get GC'ed.
	 */
	public void prune() {
		// First we need to get the latest count. This will fill
		// in a value for each quadrant. Then find any quadrants with
		// zero and get rid of those.
		root.getNumSpritesInChildren();
		root.prune();
	}

	/**
	 * 
	 */
	private class Quadrant {
		Rectangle bounds;
		int level;
		Quadrant[] quadrants;
		ArrayList<Node> nodes;
		int numSpritesInSubtree;

		/**
		 * Lazily create an array of 4 Quadrants
		 * 
		 * @param x
		 * @param y
		 * @param w
		 * @param h
		 * @param level
		 */
		public Quadrant(Rectangle bounds, int level) {
			quadrants = null;
			this.level = level;
			this.bounds = bounds.clone();
			numSpritesInSubtree = 0;
		}

		/**
		 * TODO: comment
		 */
		public int getNumSpritesInChildren() {
			numSpritesInSubtree = 0;

			// Base case, we reached a leaf node, set its count and
			// pass it up to the calling quadrant.
			if (isLeaf() && nodes != null) {
				numSpritesInSubtree = nodes.size();
				return nodes.size();
			} else {
				if (quadrants == null) {
					return 0;
				}

				for (int i = 0; i < 4; i++) {
					if (quadrants[i] != null) {
						numSpritesInSubtree += quadrants[i]
								.getNumSpritesInChildren();
					}
				}
			}
			return numSpritesInSubtree;
		}

		/**
		 * Traverses the subtree to get the count of quadrants.
		 */
		public int getNumQuadrants() {
			if (isLeaf() || quadrants == null) {
				return 1;
			}

			int num = 0;
			for (int i = 0; i < 4; i++) {
				if (quadrants[i] != null) {
					num += quadrants[i].getNumQuadrants();
				}
			}

			// +1 to include the parent itself
			return 1 + num;
		}

		/**
		 * Visit each node, and if any of the children have count == 0, remove
		 * TODO: add comment that doesn't suck.
		 */
		public void prune() {

			if (quadrants == null) {
				return;
			}

			for (int i = 0; i < 4; i++) {
				if (quadrants[i] != null) {
					if (quadrants[i].numSpritesInSubtree == 0) {
						quadrants[i] = null;
					} else {
						quadrants[i].prune();
					}
				}
			}
		}

		/**
		 * Draw the lines that delineate the nodes
		 */
		private void drawDebugLines(Rectangle viewport) {
			if (Utils.testCollision(bounds, viewport) == false) {
				return;
			}

			// We are drawing the lines that show the children,
			// so if this actually doesn't have children, it dosen't make sense
			// to draw the lines.
			if (debugOn == false) {
				return;
			}

			float x = bounds.x;
			float y = bounds.y;
			float w = bounds.w;
			float h = bounds.h;

			Renderer.pushStyle(); // Renderer.fill(0, 50 * level);
			// Renderer.textSize(150 - (level * 30));
			// Renderer.text("" +level, x + (w / 2), y + (h / 2));

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
			case 6:
				Renderer.stroke(256, 0, 256);
				break;
				
			default:
				Renderer.stroke(0, 255, 255);
				break;
			}

			Renderer.noFill();

			Renderer.strokeWeight((maxLevels - level)*1);

			// vertical lines that show the children
			//Renderer.line(x + (w /2), y, x + (w / 2), y + h);

			Renderer.rect(x, y, w, h);

			// horizontal
			//Renderer.line(x, y + (h / 2), x + w, y + (h /2));

			Renderer.popStyle();
		}

		/**
		 * If this node is a leaf, find all the tiles that intersect with it and
		 * draw them. If this node is a parent, recursively draw the children.
		 */
		public void draw(Rectangle viewport) {

			// If the viewport intersects with the node, we can need to draw it.
			if (Utils.testCollision(viewport, bounds)) {
				// If this node is a leaf, we can render all the tiles that lay
				// inside it.
				if (isLeaf()) {
					numLeafsRendered++;

					if (nodes != null) {
						for (int i = 0; i < nodes.size(); i++) {
							nodes.get(i).render();
							numTilesRendered++;
						}
					}
				} else {

					if (quadrants == null) {
						return;
					}

					for (int i = 0; i < 4; i++) {
						if (quadrants[i] != null) {
							quadrants[i].draw(viewport);
							quadrants[i].drawDebugLines(viewport);
						}
					}
				}
			}
		}

		/**
		 * 
		 * @return
		 */
		public boolean isLeaf() {
			return level == maxLevels;
		}

		/**
		 * 
		 * @param n
		 * @param x
		 * @param y
		 */
		public boolean insert(Node n) {
			int nx = (int) n.getPosition().x;
			int ny = (int) n.getPosition().y;

			// If the node fits into this Quadrant.
			if (Utils
					.isRectInsideRect(
							new Rectangle(n.getPosition().x, n.getPosition().y,
									32, 32), bounds)) {
				if (isLeaf()) {

					// Lazily create the list of nodes
					if (nodes == null) {
						nodes = new ArrayList<Node>();
					}
					nodes.add(n);
					numSpritesInTree++;
					return true;
				}

				// If this node isn't a leaf, we need to
				else {

					// We know that the node fits in the parent. We need to now
					// find out which child it fits inside, but we want to
					// avoid creating the children if

					for (int i = 0; i < 4; i++) {
						if (fitsInChild(n, i)) {

							if (quadrants == null) {
								quadrants = new Quadrant[] { null, null, null,
										null };
							}

							if (quadrants[i] == null) {
								quadrants[i] = new Quadrant(getChildBounds(
										this, i), level + 1);
							}
							quadrants[i].insert(n);
							return true;
						}
					}

					// if(quadrantsSetup == false){
					// subdivide();
					// }

					// We just tried subdividing the node, but realized
					// we reached the max depth, we need to add it to the leaf
					// list.
					/*
					 * if(isLeaf){ nodes = new ArrayList<Node>(); nodes.add(n);
					 * numSpritesInTree++; return true; }
					 */

					/*
					 * for(int i = 0; i < 4; i++){ if(quadrants[i] != null){
					 * if(quadrants[i].insert(n)){ return true; } } }
					 */

				}
			}
			return false;
		}

		/**
		 * 
		 * @param parent
		 * @param quadrant
		 * @return
		 */
		private Rectangle getChildBounds(Quadrant parent, int quadrant) {
			Rectangle childBounds;

			float x = parent.bounds.x;
			float y = parent.bounds.y;
			float w = parent.bounds.w;
			float h = parent.bounds.h;

			switch (quadrant) {
			case NE:
				childBounds = new Rectangle(x + w / 2, y, w / 2, h / 2);
				break;
			case NW:
				childBounds = new Rectangle(x, y, w / 2, h / 2);
				break;
			case SE:
				childBounds = new Rectangle(x + w / 2, y + h / 2, w / 2, h / 2);
				break;
			case SW:
				childBounds = new Rectangle(x, y + h / 2, w / 2, h / 2);
				break;
			// suppress editor warning that childBounds may not be created.
			default:
				childBounds = new Rectangle(0, 0, 0, 0);
			}
			return childBounds;
		}

		/*
		 * @returns true if the given node fits inside one of the given child
		 * nodes.
		 */
		public boolean fitsInChild(Node n, int quadrant) {
			Rectangle childBounds = getChildBounds(this, quadrant);
			Rectangle nodeRect = new Rectangle(n.getPosition().x,
					n.getPosition().y, 32, 32);
			return Utils.isRectInsideRect(nodeRect, childBounds);
			// quadrants[NE] = new Quadrant(northEastBounds, level + 1);
			// quadrants[NW] = new Quadrant(northWestBounds, level + 1);
			// quadrants[SE] = new Quadrant(southEastBounds, level + 1);
			// quadrants[SW] = new Quadrant(southWestBounds, level + 1);
			// quadrantsSetup = true;
			// return false;
		}

		/**
		 * Subdivide this node into 4 children.
		 */
		/*
		 * public void subdivide() {
		 * 
		 * // If we reached the number of levels the user wanted, we // declare
		 * this node to be a leaf and back out. if (maxLevels == level) { isLeaf
		 * = true; return; }
		 * 
		 * float x = bounds.x; float y = bounds.y; float w = bounds.w; float h =
		 * bounds.h;
		 * 
		 * Rectangle northEastBounds = new Rectangle(x + w / 2, y, w / 2, h /
		 * 2); Rectangle northWestBounds = new Rectangle(x, y, w / 2, h / 2);
		 * Rectangle southEastBounds = new Rectangle(x + w / 2, y + h / 2, w /
		 * 2, h / 2); Rectangle southWestBounds = new Rectangle(x, y + h / 2, w
		 * / 2, h / 2);
		 * 
		 * quadrants[NE] = new Quadrant(northEastBounds, level + 1);
		 * quadrants[NW] = new Quadrant(northWestBounds, level + 1);
		 * quadrants[SE] = new Quadrant(southEastBounds, level + 1);
		 * quadrants[SW] = new Quadrant(southWestBounds, level + 1);
		 * 
		 * quadrantsSetup = true;
		 * 
		 * // uncomment to create a full quadtree. /*southEast.subdivide();
		 * southWest.subdivide(); northEast.subdivide(); northWest.subdivide();
		 * }
		 */
	}

	/**
	 * Insert a node into the tree.
	 * 
	 * @param n
	 * @param x
	 * @param y
	 * 
	 * @return true if node was inserted into the tree. A Node would not be
	 *         inserted if it didn't collide with the tree.
	 */
	public boolean insert(Node n) {
		return root.insert(n);
	}

	/*
	 * Get the number of leaf nodes rendered on the last frame.
	 */
	public int getNumLeafsRendered() {
		return numLeafsRendered;
	}

	/**
	 * It can be useful to know how many tiles were actually rendered in the
	 * last frame.
	 */
	public int getNumTilesRendered() {
		return numTilesRendered;
	}

	/**
	 * Quadrant can be either a leaf or a node that contains branches to more
	 * quadrants.
	 * 
	 * @return
	 */
	public int getNumQuadrants() {
		return root.getNumQuadrants();
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

		if (debugOn) {
			root.drawDebugLines(viewport);
		}
	}
}
