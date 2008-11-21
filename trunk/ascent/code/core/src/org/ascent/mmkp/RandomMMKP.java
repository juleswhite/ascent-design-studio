package org.ascent.mmkp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.ascent.binpacking.ValueFunction;

public class RandomMMKP extends GreedyMMKP {

	private List<MMKPSet> toProcess_;

	public RandomMMKP(MMKPProblem problem) {
		super(problem);
		toProcess_ = new ArrayList<MMKPSet>(getSets());
	}

	public List<Item> solve(ValueFunction<Collection> goal) {
		init(goal);

		ArrayList<Item> sol = new ArrayList<Item>(toProcess_.size());

		for (MMKPSet s : toProcess_) {
			Item item = null;
			int retry = 4;
			while (retry > 0) {
				int it = random(0, s.getItems().size() - 1);
				item = s.getItems().get(it);
				if (fits(item)) {
					break;
				} else {
					retry--;
				}
			}
			if (s.getCurrentItem() == null) {
				int it = random(0, s.getItems().size() - 1);
				item = s.getItems().get(it);
			}

			SwapData rslt = swapIn(item);
			List<Item> nsol = rslt.getNewSolution();
			double nval = goal.getValue(nsol);

			rslt.doSwap();
			setSolution(nsol);
			currentValue_ = nval;

		}
		return makeFeasible(getSolution());
	}
	
	public List<Item> makeFeasible(List<Item> sol){
		int[] resid = new int[getResources().length];
		for(int i = 0; i < getResources().length; i++){
			resid[i] = residualResourceValue(sol, i);				
		}
		
		Item swapIn = null;
		Item swapOut = null;
		List<Item[]> swaps = new ArrayList<Item[]>();
		for(int i = 0; i < getResources().length; i++){
			int fixes = 10;
			while(resid[i] < 0 && fixes > 0){
				for(Item it : sol){
					for(Item c : it.getSet().getItems()){
						int[] delta = new int[getResources().length];
						if(c != it){
							boolean valid = true;
							for(int j = 0; j < getResources().length; j++){
								int a = getConsumedResources()[it.getIndex()][j];
								int b = getConsumedResources()[c.getIndex()][j];
								if(a < b && resid[j] < b){
									valid = false;
									break;
								}
								else {
									delta[j] = b - a;
								}
							}
							if(valid){
								for(int j = 0; j < getResources().length; j++){
									resid[j] -= delta[j];
								}
								swaps.add(new Item[]{it,c});
								break;
							}
						}
					}
				}
				for(Item[] sw : swaps){
					int index = sol.indexOf(sw[0]);
					sol.set(index, sw[1]);
				}
				swaps.clear();
				fixes--;
			}
		}
		return sol;
	}

	public int random(int min, int max) {
		double rand = Math.random();
		double range = max - min;
		double delta = range * rand;
		return (int) Math.rint((min + delta));
	}

	public static void main(String[] args) {
		// for(int i = 10; i <= 100; i += 10){
		int i = 10;
		MMKPProblem p = MMKPProblem.genWithOpt(i, 20, 60, 2, 50, 250, 5, 50, 5,
				50);
		// System.out.println(p);
		RandomMMKP solver = new RandomMMKP(p);
		List<Item> solution = solver.solve(DEFAULT_GOAL);

		System.out.println("Sets:" + i + " Opt:"
				+ ((solver.getCurrentValue() / (i * 50)) * 100) + "%");
		// }
	}
}
