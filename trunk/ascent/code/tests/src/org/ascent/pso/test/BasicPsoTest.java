package org.ascent.pso.test;

import junit.framework.TestCase;

import org.ascent.ProblemConfigImpl;
import org.ascent.VectorSolution;
import org.ascent.binpacking.ValueFunction;
import org.ascent.pso.Pso;

public class BasicPsoTest extends TestCase {

	public void testSimpleSum(){
		ProblemConfigImpl conf = new ProblemConfigImpl(5,0,1);
		ValueFunction<VectorSolution> fitness = new ValueFunction<VectorSolution>() {
		
			public double getValue(VectorSolution src) {
				int val = 0;
				for(int i = 0; i < src.getPosition().length; i++){
					val += src.getPosition()[i];
				}
				return val;
			}
		};
		Pso pso = new Pso(conf);
		pso.setIterations(10);
		pso.setTotalParticles(20);
		VectorSolution sol = pso.solve(fitness);
		System.out.println(fitness.getValue(sol));
	}
	
	public void testComplexSum(){
		ProblemConfigImpl conf = new ProblemConfigImpl(50,0,4);
		ValueFunction<VectorSolution> fitness = new ValueFunction<VectorSolution>() {
			private int[] values_ = new int[]{7,1,8,2,3};
			
			public double getValue(VectorSolution src) {
				int val = 0;
				for(int i = 0; i < src.getPosition().length; i++){
					val += values_[src.getPosition()[i]];
				}
				return val;
			}
		};
		Pso pso = new Pso(conf);
		pso.setVelocityMax(1);
		pso.setLocalLearningRate(2);
		pso.setGlobalLearningRate(2);
		pso.setInertia(0);
		pso.setIterations(20);
		pso.setTotalParticles(200);
		VectorSolution sol = pso.solve(fitness);
		System.out.println(fitness.getValue(sol));
		sol = pso.solve(fitness);
		System.out.println(fitness.getValue(sol));
		sol = pso.solve(fitness);
		System.out.println(fitness.getValue(sol));
		sol = pso.solve(fitness);
		System.out.println(fitness.getValue(sol));
	}
}
