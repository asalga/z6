package z6;

/**
 * 
 * 
 */
public final class Constants {
	private Constants() {
	}
	
	public static void test(){}

	public static final int TILE_SIZE = 32;
	
	//  THESE TWO VALUES MUST BE THE SAME!!!!!!!!
	public static final int MAP_WIDTH_IN_TILES = 256;
	public static final int MAP_HEIGHT_IN_TILES = 256;

	public static final int WATER = 0;
	public static final int GRASS = 1;
	public static final int TREES = 2;
	public static final int CLOUD1 = 3;
	public static final int CLOUD2 = 4;
	public static final int SHIP = 5;

	public static final int NULL_GUN_ID = -1;
	public static final int CANNON_GUN_ID = 0;
	public static final int MINIGUN_GUN_ID = 1;
	public static final int PLASMA_GUN_ID = 2;
	public static final int LASER_GUN_ID = 3;

	public static final int NULL_SHOT_ID = -1;
	public static final int CANNON_SHOT_ID = 0;
	public static final int MINIGUN_SHOT_ID = 1;
	public static final int PLASMA_SHOT_ID = 2;
	public static final int LASER_SHOT_ID = 3;
	public static final int LASER_BURST_SHOT_ID = 4;
	
	//public static final int HYPERWAVE_SHOT_ID = 4;
	//public static final int DELTAWAVE_SHOT_ID = 5;
	//public static final int MISSLE_SHOT_ID = 6;
	

	
	//
	// Laser
	// Hyperwave
	// Deltawave
	//
	
	
	public static final int NUM_SHOT_TYPES = 3;
}