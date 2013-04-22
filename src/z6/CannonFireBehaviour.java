package z6;

import z6.Math.Vec2;

/**
 * FireBehaviour
 * - Gun
 * - Target
 * - SetGun
 * - setTarget
 * 
 */
public class CannonFireBehaviour implements FireBehaviour{
	
	private Node target;
	
	public void setTarget(Node _target){
		target = _target;
	}
	
	public void Fire(Gun gun){		
		IShot shot = ShotFactory.create(Constants.CANNON_SHOT_ID);
		
		target = gun.getTarget();
		//
		if(target != null){
			Vec2 targetToGun = Vec2.sub(target.getPosition(), gun.getPosition());
			targetToGun.normalize();
			Vec2 bulletVelocity = Vec2.scale(targetToGun, gun.getShotSpeed());
			shot.setVelocity(bulletVelocity);
		}
		else{
			shot.setVelocity(new Vec2(0, 100));
		}
		
		shot.setPosition(gun.getPosition());
		shot.setLayer(gun.getLayer());
		Z6.AddBullet(shot);
	}
}