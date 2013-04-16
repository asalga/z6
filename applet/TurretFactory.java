package z6;

/**
 * 
 */
public final class TurretFactory {

	private TurretFactory() {
	}

	public static Turret create(int id) {

		Turret turret = new Turret();

		switch (id) {
			case Constants.CANNON_GUN_ID: {
				// IGun gun = new Gun();
				// gun.setShotType(1);
				// turrent.addGun(gun);
	
				/*
				 * IGun cannonGun = new CannonGun(); turret.addGun(cannonGun);
				 */
				// println("fix me");
	
			}break;
		}
		return turret;
	}
}