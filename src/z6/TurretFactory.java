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

		IGun gun = new Gun();
		gun.setLayer(2);

		switch (id) {
			case Constants.CANNON_GUN_ID: {
				//gun.setShotType(Constants.CANNON_SHOT_ID);
				Renderer.println("Fix me");
				gun.setShotSpeed(10);
				//IGun cannonGun = new CannonGun(); turret.addGun(cannonGun);
			}break;
			
			case Constants.MINIGUN_GUN_ID:{
				gun.setShotSpeed(150);
				gun.setFireRatePerSec(20);
				gun.setFireBehaviour(new MinigunFireBehaviour());
				//turret.addGun(gun);
			};break;
			
			case Constants.LASER_GUN_ID:{
				gun.setFireRatePerSec(0.5f);
				gun.setFireBehaviour(new LaserFireBehaviour());
				
				Node gunNode = (Node)gun;
				gunNode.setParent(turret);
				
				turret.addGun(gun);				
			};break;
			
			default:{
				Renderer.println("fix me in turret factory:" + id);
			};break;
		}

		turret.addGun(gun);
		
		return turret;
	}
}