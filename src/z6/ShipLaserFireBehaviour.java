package z6;

import z6.Math.Vec2;

/**
 * 
 * 
 */
public class ShipLaserFireBehaviour implements FireBehaviour{
	
	private Node target;
	
	public ShipLaserFireBehaviour(){
		target = null;
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
		//if(laserShot == null || (laserShot != null && laserShot.isAlive() == false)){
		
		
		// The laser relies on having a target. The user does not control it directly.
		if(target == null){
			return;
		}
	
		LaserShot laserShot = (LaserShot)ShotFactory.create(Constants.LASER_SHOT_ID);
		laserShot.setOriginShot(gun.getPosition());
		laserShot.setPosition(target.getPosition().clone());
		laserShot.setLayer(gun.getLayer());
		
		Z6.AddBullet(laserShot);
	
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