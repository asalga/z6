package z6;

/**
 * Creates Turrets!
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
				//gun.setShotType(Constants.CANNON_SHOT_ID);
				
				Renderer.println("Fix me");
				
				gun.setShotSpeed(300);
				turret.addGun(gun);
	
				//IGun cannonGun = new CannonGun(); turret.addGun(cannonGun);
			}break;
			
			case Constants.MINIGUN_GUN_ID:{
				IGun gun = new Gun();
				gun.setShotSpeed(450);
				gun.setFireRatePerSec(15);
				
				//gun.setShotType(Constants.MINIGUN_SHOT_ID);
				FireBehaviour miniGunFireBehaviour = new MinigunFireBehaviour();
				gun.setFireBehaviour(miniGunFireBehaviour);
				turret.addGun(gun);
			};break;
			
			case Constants.LASER_GUN_ID:{
				IGun gun = new Gun();
				gun.setFireRatePerSec(1);
				
				//gun.setShotType(Constants.MINIGUN_SHOT_ID);
				FireBehaviour laserFireBehaviour = new LaserFireBehaviour();
				gun.setFireBehaviour(laserFireBehaviour);
				
				Node gunNode = (Node)gun;
				gunNode.setParent(turret);
				
				turret.addGun(gun);				
			};break;
			
			default:{
				Renderer.println("fix me in turret factory:" + id);
			};break;
		}
		return turret;
	}
}