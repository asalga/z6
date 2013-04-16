package z6;

/**
 * Cannon
 * MiniGun
 * Laser
 * Plasma
 * Missile
 * Pulse
 */
public class ShotFactory {

	private ShotFactory() {}

	/**
	 * 
	 * @param id
	 * @return IShot shot
	 */
	public static IShot create(int id) {
		IShot shot = null;

		switch (id) {
		case Constants.CANNON_SHOT_ID:
			shot = new CannonShot();
			break;
			
		case Constants.MINIGUN_SHOT_ID:
			shot = new MiniGunShot();
			break;
		
		case Constants.LASER_SHOT_ID:
			shot = new LaserShot();
			break;
		}
		
		if(shot == null){
			Renderer.println("Shot Factory created null ref shot.");
		}
		
		return shot;
	}
}