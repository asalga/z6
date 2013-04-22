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
				gun.setShotSpeed(200);
				gun.setFireRatePerSec(1.5f);
				gun.setFireBehaviour(new CannonFireBehaviour());
			}break;
			
			case Constants.MINIGUN_GUN_ID:{
				gun.setShotSpeed(350);
				gun.setFireRatePerSec(10);
				gun.setFireBehaviour(new MinigunFireBehaviour());
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