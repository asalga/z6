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
	private IShot myshot;
	
	public void setTarget(Node _target){
		target = _target;
	}
	
	public void setShot(IShot shot){
		this.myshot = shot;
	}
	
	public void Fire(Gun gun){
		IShot shot = ShotFactory.create(Constants.MINIGUN_SHOT_ID);

		target = gun.getTarget();
		
		if(target != null){
		
			Vec2 targetPos = target.getPosition().clone();
	
			// HACK: fix this
			//targetPos.x -= Constants.TILE_SIZE/2;
			//targetPos.y -= Constants.TILE_SIZE/2;
	
			Vec2 targetToGun = Vec2.sub(targetPos, gun.getPosition());
			targetToGun.normalize();
			
					//new Vec2(position.x
					//* Constants.TILE_SIZE, position.y * Constants.TILE_SIZE));
	
			Vec2 bulletVelocity = Vec2.scale(targetToGun, gun.getShotSpeed());
	
			//IShot newShot = shot.clone();
			
			shot.setVelocity(bulletVelocity);
		}
		else{
			Vec2 dir = gun.getDirection();
			Vec2 bulletVelocity = Vec2.scale(dir, gun.getShotSpeed());
			
			shot = myshot.clone();
			shot.setVelocity(bulletVelocity);
		}
		
		shot.setPosition(gun.getPosition());
		shot.setLayer(gun.getLayer());
		Z6.AddBullet(shot);
	}
}