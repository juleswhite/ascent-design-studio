package org.ascent.pso;

public interface ParticleGroup {

	public Particle getBestLocalPosition(Particle p);
	public void moved(Particle p);

}
