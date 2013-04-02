package z6;

/**
 * 
 * @author asalga
 * 
 */
public class ShotFactory {

	private ShotFactory() {
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static IShot create(int id) {
		IShot shot = null;

		switch (id) {
		case Constants.CANNON_SHOT_ID:
			shot = new CannonShot();
			break;
		/*
		 * case Constants.PLASMA_SHOT_ID: shot = new PlasmaShot(); break;
		 */
		default:
			// println("fix me");
			break;
		}
		
		if(shot == null){
			Renderer.println("Shot Factory created null ref shot.");
		}
		
		return shot;
	}
}