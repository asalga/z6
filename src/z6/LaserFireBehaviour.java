package z6;

import z6.Math.Vec2;

/**
 * 
 * 
 */
public class LaserFireBehaviour implements FireBehaviour{
	
	private Node target;
	private LaserShot laserShot;
	
	public LaserFireBehaviour(){
		target = null;
		laserShot = null;
	}
	
	public void setTarget(Node _target){
		target = _target;
	}
	
	/**
	 * If the lastShot doesn't exist, we can create a new one.
	 */
	public void  Fire(Gun gun){
		
		// The laserShot marks itself as dead, so the next time Gun calls
		// the Fire() method, we re-create another shot.
		if(laserShot == null || (laserShot != null && laserShot.isAlive() == false)){
			laserShot = (LaserShot)ShotFactory.create(Constants.LASER_SHOT_ID);
			laserShot.setOriginShot(gun.getPosition());
			
			Ship ship = (Ship)target;
			ship.addSubscriber(laserShot);
			
			Z6.AddBullet(laserShot);
		}
		
		///Vec2 bulletVelocity = Vec2.scale(new Vec2(0, 1), 300);
		//shot.setPosition(gun.getPosition());
		
		//gun.getTarget();
		//Vec2 bulletVelocity = Vec2.scale(new Vec2(0, 1), 300);
		/*shot.setPosition(gun.getPosition());
		shot.setVelocity(new Vec2(10,0));
		Z6.AddBullet(shot);*/
		
		//IShot shot = ShotFactory.create(Constants.MINIGUN_SHOT_ID);
		// shot.setLayer(bulletCollisionLayer);
		//Vec2 dir = gun.getDirection();	
	}
}