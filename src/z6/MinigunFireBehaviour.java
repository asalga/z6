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
public class MinigunFireBehaviour implements FireBehaviour{
	
	private Node target;
	
	public void setTarget(Node _target){
		target = _target;
	}
	
	public void Fire(Gun gun){
		
		IShot shot = ShotFactory.create(Constants.MINIGUN_SHOT_ID);
		// shot.setLayer(bulletCollisionLayer);
		//Vec2 dir = gun.getDirection();
		
		Vec2 targetPos = target.getPosition().clone();

		// HACK: fix this
		//targetPos.x -= Constants.TILE_SIZE/2;
		//targetPos.y -= Constants.TILE_SIZE/2;

		Vec2 targetToGun = Vec2.sub(targetPos, gun.getPosition());
		targetToGun.normalize();
		
				//new Vec2(position.x
				//* Constants.TILE_SIZE, position.y * Constants.TILE_SIZE));

		Vec2 bulletVelocity = Vec2.scale(targetToGun, gun.getShotSpeed());
		shot.setPosition(gun.getPosition());
		shot.setVelocity(bulletVelocity);
		Z6.AddBullet(shot);
	}
}