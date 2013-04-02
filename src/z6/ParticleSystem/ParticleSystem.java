package z6.ParticleSystem;

import java.util.ArrayList;

import z6.Renderer;
import z6.Utils;
import z6.Vec2;

/**
 * 
 */
public class ParticleSystem {

	public class cParticle {

		// movement
		Vec2 position;
		Vec2 velocity;
		Vec2 acceleration;

		// lifetime
		float age;
		float lifeTime;
		boolean alive;

		// appearance

		public cParticle() {

			// position
			position = new Vec2();
			velocity = new Vec2();
			acceleration = new Vec2();

			// lifetime
			age = 0.0f;
			lifeTime = 0.0f;
			alive = true;
		}

		public void update(float deltaTimeInSeconds) {

			// movementBehaviour.update(this);

			velocity.x += acceleration.x * deltaTimeInSeconds;
			velocity.y += acceleration.y * deltaTimeInSeconds;

			position.x += velocity.x * deltaTimeInSeconds;
			position.y += velocity.y * deltaTimeInSeconds;
			age += deltaTimeInSeconds;
			if (age >= lifeTime) {
				alive = false;
			}
		}

		public boolean isAlive() {
			return alive;
		}

		public void setPosition(Vec2 pos) {
			position = pos;
		}

		public void setVelocity(Vec2 vel) {
			velocity = vel;
		}

		public void setAcceleration(Vec2 accel) {
			acceleration = accel;
		}

		public void setLifeTime(float l) {
			if (l > 0.0f) {
				lifeTime = l;
			}
		}

		/*
		 * public void draw() { // renderBehaviour(this); int opacity = (int)
		 * (255 * (1 - (age / lifeTime)));
		 * 
		 * Renderer.noStroke(); Renderer.fill(255, opacity);
		 * Renderer.ellipse(position.x, position.y, 1, 1); }
		 */
	}

	private z6.ParticleSystem.ViewBehaviour viewBehaviour;

	private ArrayList particles;

	private Vec2 position;
	private Vec2 acceleration;
	private Vec2 minVelocityRange;
	private Vec2 maxVelocityRange;

	private int numParticlesAlive;
	private float minLifeTimeRange;
	private float maxLifeTimeRange;

	// color range
	// size range

	public ParticleSystem(int numParticles) {
		viewBehaviour = new NullViewBehaviour();
		
		setPosition(new Vec2());
		setAcceleration(new Vec2());

		numParticlesAlive = numParticles;

		setParticleLifeTime(0, 0);
		setParticleVelocity(new Vec2(), new Vec2());

		particles = new ArrayList();
		for (int i = 0; i < numParticles; i++) {
			cParticle particle = new cParticle();
			particles.add(particle);
		}

		//setViewBehaviour(new NullViewBehaviour());
	}

	public void setPosition(Vec2 pos) {
		position = new Vec2(pos.x, pos.y);
	}

	public void setAcceleration(Vec2 accel) {
		acceleration = accel;
	}

	/**
	 * 
	 * @param viewBeh
	 */
	public void setViewBehaviour(ViewBehaviour viewBeh) {
		viewBehaviour = viewBeh;
	}

	public void setParticleVelocity(Vec2 vel1, Vec2 vel2) {
		minVelocityRange = vel1;
		maxVelocityRange = vel2;
	}

	/**
	 * 
	 * @param deltaTimeInSeconds
	 */
	public void update(float deltaTimeInSeconds) {
		for (int i = 0; i < particles.size(); i++) {
			cParticle particle = (cParticle) particles.get(i);
			particle.update(deltaTimeInSeconds);
		}
	}

	/**
	 * 
	 */
	public void draw() {
		for (int i = 0; i < particles.size(); i++) {
			cParticle particle = (cParticle) particles.get(i);
			if (particle.isAlive()) {
				viewBehaviour.render(particle);
			}
		}
	}

	// public void play(){}

	// public void pause(){}

	// /public void setEmitRate(float numParticlesPerSecond){}

	public void emit(float numParticles) {
		for (int i = 0; i < particles.size(); i++) {
			cParticle particle = (cParticle) particles.get(i);
			particle.setPosition(position.clone());

			particle.setVelocity(Utils.getRandomVec2(minVelocityRange,
					maxVelocityRange));
			particle.setAcceleration(acceleration);

			particle.setLifeTime(Utils.getRandomFloat(minLifeTimeRange,
					maxLifeTimeRange));
		}
	}

	public void setParticleLifeTime(float minLifeTime, float maxLifeTime) {
		minLifeTimeRange = minLifeTime;
		maxLifeTimeRange = maxLifeTime;
	}

	// setParticleColor();
	// setParticleSize();

	public boolean isDead() {

		// if we found at least one particle which is alive,
		// the system isn't dead.
		for (int i = 0; i < particles.size(); i++) {
			cParticle particle = (cParticle) particles.get(i);
			if (particle.isAlive()) {
				return false;
			}
		}
		return true;
	}
}