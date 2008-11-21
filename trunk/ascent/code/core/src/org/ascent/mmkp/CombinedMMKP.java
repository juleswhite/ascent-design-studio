package org.ascent.mmkp;

import java.util.Collection;
import java.util.List;

import org.ascent.binpacking.ValueFunction;

public class CombinedMMKP extends MMKP {

	public CombinedMMKP(MMKPProblem problem) {
		super(problem);
	}

	List<Item> solve(ValueFunction<Collection> goal) {
		
		MMKPProblem p = getProblem();
		
		JHEU solver2 = new JHEU(p);
		List<Item> solution2 = solver2.solve(DEFAULT_GOAL);
		
		PsoMMKP psolver = new PsoMMKP(p);
		List<Item> solution3 = psolver.solve(DEFAULT_GOAL);
		
		GeneticMMKP solver = new GeneticMMKP(p,30,300);
		List<Item> solution = solver.solve(DEFAULT_GOAL);
		
		GeneticMMKP solver4 = new GeneticMMKP(p,30,30);
		if(solution3 != null)
			solver.getSeeds().add(solution3);
		if(solution2 != null)
			solver.getSeeds().add(solution2);
		if(solution != null)
			solver.getSeeds().add(solution);
		List<Item> solution4 = solver4.solve(DEFAULT_GOAL);

		double val = goal.getValue(solution);
		if(val < goal.getValue(solution2))
			solution = solution2;
		if(val < goal.getValue(solution3))
			solution = solution3;
		if(val < goal.getValue(solution4))
			solution = solution4;
		
		return solution;
	}

	public static void main(String[] args){
		int i = 10000;
		MMKPProblem p = MMKPProblem.gen(i, 2, 8, 2, 40000, 55000, 20, 40, 5, 50);//MMKPProblem.genWithOpt(i, 2, 8, 2, 50, 250, 0, 5, 5, 50);
//		System.out.println(p);
//		
		long pstart = System.currentTimeMillis();
		CombinedMMKP psolver = new CombinedMMKP(p);
//		psolver.getSeeds().add(solution2);
		List<Item> solution3 = psolver.solve(DEFAULT_GOAL);
		System.out.println("Time:"+(System.currentTimeMillis()-pstart));
		if (solution3 != null)
			System.out.println("Sets:" + i + " Opt:"
				+ ((DEFAULT_GOAL.getValue(solution3) / (i * 50)) * 100)
				+ "%");
		else
			System.out.println("No solution found");
	}
}
