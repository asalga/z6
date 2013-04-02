package z6;

/**
 * 
 */
public final class TurretFactory {

	private TurretFactory() {
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static Turret create(int id) {

		Turret turret = new Turret();

		switch (id) {
			case Constants.CANNON_GUN_ID: {
				
				IGun gun = new Gun();
				gun.setShotType(Constants.CANNON_SHOT_ID);
				turret.addGun(gun);
	
				//IGun cannonGun = new CannonGun(); turret.addGun(cannonGun);
			}break;
		}
		return turret;
	}
}