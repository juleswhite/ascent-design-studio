package org.ascent;



public interface ProblemConfig {

	public int[][] getPositionBoundaries();
	
	public boolean isFeasible(VectorSolution sol);
	
	public VectorSolution[] createInitialSolutions(int count);
}
