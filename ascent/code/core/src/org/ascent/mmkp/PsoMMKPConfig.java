package org.ascent.mmkp;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.ascent.ProblemConfig;
import org.ascent.VectorSolution;
import org.ascent.binpacking.ValueFunction;
import org.ascent.pso.Particle;
import org.ascent.pso.ParticleGroup;
import org.ascent.pso.ParticleGroupImpl;
import org.ascent.pso.Pso;

public class PsoMMKPConfig implements ProblemConfig {

	private int[][] boundaries_;
	private MMKPProblem problem_;
	private ValueFunction<Collection> mmkpValueFunction_;


	public PsoMMKPConfig(MMKPProblem mmkp, ValueFunction<Collection> vf) {
		super();
		problem_ = mmkp;
		Map<Integer, List<Integer>> map = mmkp.getSetsMap();
		boundaries_ = new int[map.size()][2];
		for(Integer key : map.keySet()){
			boundaries_[key][1] = map.get(key).size()-1;
		}
		mmkpValueFunction_ = vf;
	}

	

	public VectorSolution[] createInitialSolutions(int size) {
		VectorSolution[] particles = new VectorSolution[size];
		for (int i = 0; i < size; i++) {
			// ArrayList<Item> its = new ArrayList<Item>();
			// for (Set s : getSets()) {
			// int it = random(0, s.getItems().size() - 1);
			// its.add(s.getItems().get(it));
			// }
			List<Item> its = ((new RandomMMKP(problem_)))
					.solve(mmkpValueFunction_);
			// pop.add(new Solution(its));
			int[] pos = new int[problem_.getSetsMap().size()];
			for (Item item : its) {
				pos[problem_.getSets()[item.getIndex()]] = item.getSet()
						.getItems().indexOf(item);
			}
			particles[i] = new VectorSolution(pos);
		}
		return particles;
	}

	public int[][] getPositionBoundaries() {
		return boundaries_;
	}

	public boolean isFeasible(VectorSolution sol) {
		return problem_.getOverflow(sol.getPosition())==0;
	}

}
