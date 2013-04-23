package z6;

import z6.Math.Vec2;

/**
 * 
 */
public class LaserFireBehaviour implements FireBehaviour{
	
	private Node target;
	
	public LaserFireBehaviour(){
		target = null;
	}
	
	public void setTarget(Node _target){
		target = _target;
	}
	
	/**
	 */
	public void  Fire(Gun gun){
		
		// The laserShot marks itself as dead, so the next time Gun calls
		// the Fire() method, we re-create another shot.
		Ship ship = (Ship)gun.getTarget();
		
		LaserShot laserShot = (LaserShot)ShotFactory.create(Constants.LASER_SHOT_ID);
		laserShot.setOriginShot(gun.getPosition());
		laserShot.setPosition(ship.getPosition().clone());
		laserShot.setLayer(2);
		
		// TODO: fix this
		ship.addSubscriber(laserShot);
		
		Z6.AddBullet(laserShot);	
	}
}