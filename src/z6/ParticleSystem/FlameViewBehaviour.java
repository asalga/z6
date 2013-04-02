package z6.ParticleSystem;

import z6.Renderer;

/**
 * Flame behaviour changes color from orange to red, then to black then to transparent
 * over a given amount of time.
 * 
 * What happens if the particle dies before that happens?
 *   
 *  What controls the particle lifespan? time? distance? behaviours?
 * 
 * @author asalga
 *
 */
public class FlameViewBehaviour implements ViewBehaviour{

	public void render(ParticleSystem.cParticle particle){
		
		Renderer.fill(255, 0);
		Renderer.rect(particle.position.x, particle.position.y, 10, 10);
		
		// Get the particle's age and depending on that, change the color
	}
}
