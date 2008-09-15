package org.ascent.core.root;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.refresh.core.RefreshMatrixCore;
import org.refresh.core.approximation.mmkp.MMKP.Item;
import org.refresh.core.conf.ExpressionParser;

public class MMKPTreesTwo {

	// Brian, we are going to both have
	// to make ourselves start using
	// loggers
	private static Logger logger_ = Logger.getLogger(MMKPTrees.class.getName());

	private double step_; // (SW)
	private double budget_; // in millions
	private MMKPProblem swProblem_;// original. Gets broken after the
	// calculatedMP is found
	private MMKPProblem swProblemCopy_;// used after swProblem gets broken
	private MMKPProblem hwProblem_;
	private MMKPProblem hwProblemCopy_;
	private MMKPProblem combinedProblem_;
	private MMKPProblem swProblemPreCopy_;
	private MMKPProblem hwProblemPreCopy_;
	private List<Item> previousBestSWSolution_ ;
	private List<Item> previousBestHWSolution_;
	private double previousBestValue_ =0;
	private double calculatedMP_ = -1;
	private int solverStrategy_;
	private double[][] outputValues_;
	private double[] resourceRatios_;
	private int resourceCount_ = 3;
	private int outputArrayIndex_;
	int sBudgetInt_;
	private double swBudget_;
	private double hwBudget_;
	private String fileName_;
	private double bestValue_ = 0;
	private List<Item> bestSWSolution_;
	private List<Item> bestHWSolution_;
	private double optvalue_;
	private int correct =1;

	public MMKPTreesTwo(double st, double b, double ov) {

		step_ = st;
		budget_ = b;
		optvalue_ = ov;
	}

	public void execute(MMKPProblem sw, MMKPProblem hw, int ss, int iterations,
			String outputFile, boolean solveLCS) {
		fileName_ = outputFile;
		correct = iterations; // This decides whether the correction (you don't get a worse solution, if the previous config would work) is in effect
		double hwBound = 100;
		double swBound = 0;
		solverStrategy_ = ss; /*Don't think this needs to be used in our tool. 
								It's another project o see if the strategies effect things */
		 											
		swProblemPreCopy_ = new MMKPProblem(sw.getItems(), sw.getSets(), sw
				.getConsumedResources(), sw.getValues(), sw.getResources());
		/*
		 * You have to copy the problems because the values are altered when solved, 
		 * and they need to be solved more than once. 
		 */
		hwProblemPreCopy_ = new MMKPProblem(hw.getItems(), hw.getSets(), hw
				.getConsumedResources(), hw.getValues(), hw.getResources());
		//iterations = 1;
		//for (int i = 0; i < iterations; i++) {
		outputArrayIndex_ = 0;
		outputValues_ = new double[(int) (100 / step_)][2]; //sets up the array to store the solution value for each step 
		System.out.println(" ASCNT solver start time: " + System.currentTimeMillis());
		while (swBound < 100) {
			sw = new MMKPProblem(swProblemPreCopy_.getItems(),
					swProblemPreCopy_.getSets(), swProblemPreCopy_
							.getConsumedResources(), swProblemPreCopy_
							.getValues(), swProblemPreCopy_.getResources()); // copying the problem to make this instance (sw)
			hw = new MMKPProblem(hwProblemPreCopy_.getItems(),
					hwProblemPreCopy_.getSets(), hwProblemPreCopy_
							.getConsumedResources(), hwProblemPreCopy_
							.getValues(), hwProblemPreCopy_.getResources());
				
				// // solve(sw.cloneProblem(), hw.cloneProblem(), swBound,hwBound);
			
				solve(sw,hw,swBound,hwBound);
			
			sw = new MMKPProblem(swProblemPreCopy_.getItems(),
					swProblemPreCopy_.getSets(), swProblemPreCopy_
							.getConsumedResources(), swProblemPreCopy_
							.getValues(), swProblemPreCopy_.getResources());
			hw = new MMKPProblem(hwProblemPreCopy_.getItems(),
					hwProblemPreCopy_.getSets(), hwProblemPreCopy_
							.getConsumedResources(), hwProblemPreCopy_
							.getValues(), hwProblemPreCopy_.getResources());
			//System.out.println("Steppin "+swBound);
		
		//	solveLCS(sw, hw, 200, (int) (.5 * budget_), (int) (.5 * budget_ ));
			
			swBound += step_;
			hwBound -= step_;
			outputArrayIndex_++;
		}
		
		
		swBound = step_;
		hwBound = 100;
	
		System.out.println(" ASCNT solver stop time: " + System.currentTimeMillis());

		
		if(hwProblemCopy_.getConsumedResources()[0][1] < 0){
		((new FCFTools())).applyResourceCoefficients(hwProblemPreCopy_,
				new int[] { 1, -1, -1 });
		}
		/* JHEU solver = new LinearCodesignSolver();
		
		//System.out.println(" lp solver start time: " + System.currentTimeMillis());
		//solver.solve(hw, sw, (int) Math.rint(budget_),-1,-1);
		//System.out.println(" lp solver stop time: " + System.currentTimeMillis());
		//int optvalue = solver.getValue();
	
		
		double opt = ((double) bestValue_) / ((double) optvalue_);
	//THIS IS WHERE YOU CAN JUST WRITE THE OPTIMALITY	//excelOutputLP(""+opt+",");
		
		System.out.println(bestValue_ + "/" + optvalue + " = " + (opt));

		if (opt > 1 || optvalue_ == Integer.MAX_VALUE) {
			System.out.println(swProblemPreCopy_);
			System.out.println(hwProblemPreCopy_);

			System.out.println(bestSWSolution_);
			System.out.println(bestHWSolution_);

			int res[] = new int[bestSWSolution_.get(0).getConsumedResources().length];
			int bremainder = (int)budget_;
			for (Item i : bestSWSolution_) {
				for (int j = 1; j < res.length; j++) {
					res[j] -= i.getConsumedResources()[j];
				}
				bremainder -= i.getConsumedResources()[0];
			}
			for (Item i : bestHWSolution_) {
				for (int j = 1; j < res.length; j++) {
					res[j] += hwProblemPreCopy_.getConsumedResources()[i.getIndex()][j];
				}
				bremainder -= hwProblemPreCopy_.getConsumedResources()[i.getIndex()][0];
			}
			res[0] = bremainder;

			if (!isValid(bestSWSolution_, bestHWSolution_, hwProblemPreCopy_.getConsumedResources(), swProblemPreCopy_.getConsumedResources(), (int) budget_)) {
				System.out.print("Residual:[");
				for (int i = 0; i < res.length; i++) {
					System.out.print(res[i] + ",");
				}
				System.out.print("]\n");
			}
		}*/

	}
	private double solveLCS(MMKPProblem hw, MMKPProblem sw, int wholeBudget, int swBudget, int hwBudget){
		if(hw.getConsumedResources()[0][1] < 0){
			((new FCFTools())).applyResourceCoefficients(hw,
					new int[] { 1, -1, -1 });
		}
		LinearCodesignSolver solver = new LinearCodesignSolver();
		solver.solve(hw, sw, wholeBudget,swBudget,hwBudget);
		int optvalue = solver.getValue();
		outputValues_[outputArrayIndex_][1] = optvalue;
		System.out.println("Solving for LP with sw budget of " + swBudget + " and found the value to be " + optvalue);
		return optvalue;
	}
	
	public boolean isValid(List<Item> swsol, List<Item> hwsol, int[][] prores, int[][] conres, int budget) {
		int res[] = new int[swsol.get(0).getConsumedResources().length];
		for (Item i : swsol) {
			for (int j = 1; j < res.length; j++) {
				res[j] -= conres[i.getIndex()][j];
			}
			budget -= conres[i.getIndex()][0];
		}
		for (Item i : hwsol) {
			for (int j = 1; j < res.length; j++) {
				res[j] += prores[i.getIndex()][j];
			}
			budget -= prores[i.getIndex()][0];
		}

		boolean valid = true;

		if (budget < 0)
			valid = false;
		else
			for (int i = 0; i < res.length; i++) {
				if (res[i] < 0) {
					valid = false;
					break;
				}
			}

		return valid;
	}

	public void solve(MMKPProblem sw, MMKPProblem hw, double sBound,
			double hBound) {
		double sBudgetDoub = budget_ * (sBound / 100);
		double hBudgetDoub = budget_ - sBudgetDoub;
		swBudget_ = sBudgetDoub;
		 hwBudget_ = hBudgetDoub;
		List<Item> solution;
		sBudgetInt_ = (int) Math.rint(sBudgetDoub);
	
		swProblem_ = sw;
		swProblemCopy_ = new MMKPProblem(swProblem_.getItems(), swProblem_
				.getSets(), swProblem_.getConsumedResources(), swProblem_
				.getValues(), swProblem_.getResources());
				hwProblem_ = hw;
				
		hwProblemCopy_ = new MMKPProblem(hwProblem_.getItems(), hwProblem_
				.getSets(), hwProblem_.getConsumedResources(), hwProblem_
				.getValues(), hwProblem_.getResources());

		int[][] rescons = new int[swProblem_.getItems().length][resourceCount_];
		for (int i = 0; i < rescons.length; i++) {
			int[] cons = new int[1];
			cons[0] = swProblem_.getConsumedResources()[i][0]; // cost
			rescons[i] = cons;

		}
		swProblem_.setConsumedResources(rescons);
		int[] availres = new int[1];
		availres[0] = sBudgetInt_;// SW Budget Alloc for this solve;
		swProblem_.setResources(availres);

		JHEU solver = new JHEU(swProblem_);
		solution = solver.solve(MMKP.DEFAULT_GOAL);

		List hwsol = solution;
		System.out.println("hwsol =  " + hwsol);

		double calculatedMP = solver.getCurrentValue();
		System.out.println(" Calulated MP is " + calculatedMP);

		int[] totalResources = sumResources(swProblemCopy_, resourceCount_,
				solution);

		double totalResourcesSum = 0;
		String rval = "";
		for (int i = 0; i < totalResources.length; i++) {
			rval += ("\n  [" + totalResources[i]);
			totalResourcesSum += totalResources[i];
		}
		
		
		/*
		 * The total resources are summed so that each ratio can be calculated
		 */
		


		resourceRatios_ = new double[resourceCount_];
		for (int i = 0; i < resourceRatios_.length; i++) {
			resourceRatios_[i] = totalResources[i] / totalResourcesSum;
		}

		int[] values = hwProblem_.getValues();
		rval = "";
		for (int i = 0; i < hwProblem_.getValues().length; i++) {
			rval += (" " + values[i]);
		}

		int[][] hwpcr = hw.getConsumedResources();
		
		applyRatios(hwProblem_); //change the hardware values to reflect the software needs
		values = hwProblem_.getValues();

		String val = "";
		for (int i = 0; i < hwProblem_.getValues().length; i++) {
			val += " " + values[i];
		}
		int[][] hwConsumedResources = new int[hwProblemCopy_.getItems().length][resourceCount_];
		for (int i = 0; i < hwProblem_.getItems().length; i++) {
			int[] cons = new int[resourceCount_];
			cons[0] = hwProblem_.getConsumedResources()[i][0]; // cost

			for (int k = 1; k < resourceCount_; k++) {
				cons[k] = 0;

			}
			hwConsumedResources[i] = cons;

		}
		hwProblem_.setConsumedResources(hwConsumedResources);

		hwProblem_.setResources(new int[] { (int) Math.rint(hBudgetDoub) });

		softwareOrientedSolver(hwProblem_);

	}

	public void softwareOrientedSolver(MMKPProblem hw) {
		/*
		 * First, solve the hardware problem again now that the ratios have been applied. 
		 * Then we will know what quantities of consumable resources there are for the sw problem.
		 * Next, we set these consumable resources and solve the solve the sw problem.
		 */
		int[][] test = hw.getConsumedResources();
		JHEU solver = new JHEU(hw);
		List<Item> solution = solver.solve(MMKP.DEFAULT_GOAL);
		List<Item> hwsol = new ArrayList<Item>(solution);
		Object[] itemArray = hwProblemCopy_.getItems();
		int[][] cResources = hwProblemCopy_.getConsumedResources();
		int[] resourceSum = new int[resourceCount_];
		for (int i = 0; i < resourceSum.length; i++) {
			resourceSum[i] = 0;
		}
		logger_.fine("HW Setup:" + solution);
		
		while (!solution.isEmpty()) {
			Item item = (Item) solution.get(0);
			int itemIndex = item.getIndex();

			for (int i = 0; i < cResources[itemIndex].length; i++) {
				resourceSum[i] += Math.abs(cResources[itemIndex][i]);
			}
			solution.remove(0);
		}
		
		int[] availableResources = new int[resourceCount_];
		
		for (int i = 0; i < resourceSum.length; i++) {
			availableResources[i] = resourceSum[i];
		}
		
		logger_.finest("s budget int " + sBudgetInt_);
		availableResources[0] = sBudgetInt_;

		swProblemCopy_.setResources(availableResources);

		solver = new JHEU(swProblemCopy_);
		solution = solver.solve(MMKP.DEFAULT_GOAL);
		logger_.finest("The solution found was via software Oriented approach "
				+ solution);
		logger_.finest("The solver value was " + solver.getCurrentValue());
		outputValues_[outputArrayIndex_][0] = solver.getCurrentValue();

		if (solver.getCurrentValue() > bestValue_) { // rests the bestValue_ if current is better than the best so far
			bestValue_ = solver.getCurrentValue();
			
			bestSWSolution_ = solution;
			bestHWSolution_ = hwsol;
		}
	    
		if(solver.getCurrentValue() >= previousBestValue_){
			previousBestValue_ = solver.getCurrentValue();
			previousBestSWSolution_ = solution;
			previousBestHWSolution_ = hwsol;
		}
		else{//if the solution is not better than >= a previous solution, we need to do a check to make sure
			 // the previous solution wouldn't still work. 
		
			Iterator solutionIt = previousBestSWSolution_.iterator();
			int totalCost = 0;
			int hwCost = 0;
			int swCost = 0;
			while(solutionIt.hasNext()){
				Item currentItem = (Item) solutionIt.next();
				int itemIndex = currentItem.getIndex();
				swCost += currentItem.getConsumedResources()[0];
				totalCost += currentItem.getConsumedResources()[0];
				
			}
			
			solutionIt = previousBestHWSolution_.iterator();
			while(solutionIt.hasNext()){
				Item currentItem = (Item) solutionIt.next();
				int itemIndex = currentItem.getIndex();
				hwCost +=currentItem.getConsumedResources()[0];
				totalCost += currentItem.getConsumedResources()[0];
				
			}
			
			if(totalCost <= budget_ && swCost <= swBudget_ && hwCost <= hwBudget_){//Previous solution still valid, and better.
				outputValues_[outputArrayIndex_][0] = previousBestValue_;
				
			}
			else{//previous solution was better, but not alid
			
				if(swCost> swBudget_){
				//	System.out.println(" SOFTWARE OVER BUDGET: "+ swCost + "vs " + swBudget_);
				}
				if(hwCost> hwBudget_){
					//System.out.println(" Hardware OVER BUDGET: " + hwCost + " vs " +hwBudget_);
				}
			}
			
			
		}
		
		excelOutput(fileName_,outputValues_, 2 );

	}


	/*
	 * public void hardwareOrientedSolver(){ List<Item> solution =
	 * softwareOrientedSolver(hwProblem_); if(solution.isEmpty()){
	 * 
	 *  }
	 */
	/*public void hardwareOrientedSolver() {
		// >>>>>>> 1.10
		/*
		 * Solve the software, solve the hardware, see if software fits, if not
		 * reset the modifiers, solve the hardware again. Repeat as necessary
		 * 
		 *

	}*/
/*
	public void combinedSolver() {

		combineProblems();

		// availableResources = combinedProblem.g
		int[][] hwCopyConResources = hwProblemCopy_.getConsumedResources();
		int[] resourceSum = new int[hwCopyConResources[0].length];
		for (int i = 0; i < hwCopyConResources.length; i++) {
			// logger_.finest(" cResources " + i + " is "
			// + cResources[itemIndex][i]);
			for (int k = 1; k < hwCopyConResources[i].length; k++) {
				resourceSum[k] = 0;// Math.abs(hwCopyConResources[i][k]);
			}
		}
		int[] availableResources = resourceSum;
		// int [] summedResources = sumResources():
		availableResources[0] = (int) budget_;

		combinedProblem_.setResources(availableResources);
		JHEU solver = new JHEU(combinedProblem_);
		List<Item> solution = solver.solve(MMKP.DEFAULT_GOAL);
		logger_
				.finest("THe solution found was via Combinedpproach "
						+ solution);
		logger_.finest("The solver value was " + solver.getCurrentValue());

		int val = 0;
		/*
		 * We can't just take the solver value in this case. We have to
		 * calculate the value for the SW features manually here...
		 
		outputValues_[outputArrayIndex_][0] = solver.getCurrentValue();
		/*
		 * Throw all of it together and solve away.
		 * 
		 * 
		 
	}*/

	public void applyRatios(MMKPProblem hp) {
		/*
		 * Modifies the values for hp using the correct modifier as set in resourceRatios
		 */
		Object[] items = hp.getItems();
		int[][] consumedResources = hp.getConsumedResources();
		int[] hpResources = hp.getResources();
		int[] values = new int[items.length];
		for (int i = 0; i < items.length; i++) {
			double nval = 0;
			for (int j = 1; j < hp.getConsumedResources()[i].length; j++) {
				nval += resourceRatios_[j]
						* (Math.abs(consumedResources[i][j]) * 1.0);//Have to use absolute value since harware ratios are initially negative.
			}

			values[i] = (int) nval;
		}
		hp.setValues(values);
		hwProblem_ = hp;

	}

	public void combineProblems() {
		// int [] sets =
		// combineArrays(swProblemCopy.getItems(),hwProblem.getItems());
		int[] swSets = swProblemCopy_.getSets();
		int[] hwSets = hwProblemCopy_.getSets();
		int[] combinedSets = new int[swSets.length + hwSets.length];
		for (int i = 0; i < combinedSets.length; i++) {
			if (i < swSets.length) {
				combinedSets[i] = swSets[i];
			} else {
				combinedSets[i] = hwSets[i - swSets.length];
			}
		}

		int[] swValues = swProblemCopy_.getValues();// .getConsumedResources();
		int[] hwValues = hwProblemCopy_.getValues();
		int[] combinedValues = new int[swValues.length + hwValues.length];
		for (int i = 0; i < combinedValues.length; i++) {
			if (i < swSets.length) {
				combinedValues[i] = swValues[i];
			} else {
				break;
				// combinedValues[i] = hwValues[i - swValues.length];
			}
		}

		int[] swResources = swProblemCopy_.getResources();// .getConsumedResources();\
		// logger_.finest("swResources length is " + swResources.length);
		int[] hwResources = hwProblemCopy_.getResources();
		int[] combinedResources = new int[swResources.length];
		for (int i = 0; i < combinedResources.length; i++) {
			combinedResources[i] = swResources[i];
			// combinedResources[i] += hwResources[i];
		}

		Object[] swItems = swProblemCopy_.getItems();
		Object[] hwItems = hwProblemCopy_.getItems();
		Object[] combinedItems = new Object[swItems.length + hwItems.length];
		for (int i = 0; i < combinedItems.length; i++) {
			if (i < swItems.length) {
				combinedItems[i] = swItems[i];
			} else {
				combinedItems[i] = hwItems[i - swItems.length];
			}
		}

		int[][] swConsumedResources = swProblemCopy_.getConsumedResources();
		int[][] hwConsumedResources = hwProblemCopy_.getConsumedResources();
		int[] hwIn = hwConsumedResources[0];
		int[][] combinedConsumedResources = new int[hwConsumedResources.length
				+ swConsumedResources.length][hwIn.length];
		for (int i = 0; i < combinedConsumedResources.length; i++) {

			for (int k = 0; k < hwIn.length; k++) {
				if (i < swConsumedResources.length) {
					combinedConsumedResources[i] = swConsumedResources[i];
				} else {
					combinedConsumedResources[i] = hwConsumedResources[i
							- swConsumedResources.length];
				}

			}

		}
		combinedProblem_ = new MMKPProblem(combinedItems, combinedSets,
				combinedConsumedResources, combinedValues, combinedResources);
	}

	public int[] sumResources(MMKPProblem p, int rc, List<Item> solution) { 
	/*
	 * Calculates the total amount of each type of resources consumed for a given PP problem. rc (resource count) is the count of unique consumed resources
	 * (i.e cpu, ram, power etc). 
	 */
		int[] totalConsumedResources = new int[rc];
		for (int i = 0; i < totalConsumedResources.length; i++) {
			totalConsumedResources[i] = 0;
		}
		int[][] consumedResources = p.getConsumedResources();
		while (!solution.isEmpty()) {
			Item item = solution.get(0);
			solution.remove(0);
			int index = item.getIndex();
			logger_.finest("" + consumedResources[0].length);
			logger_.finest(" tcr ");
			logger_.finest("Cost " + consumedResources[index][0]);
			for (int k = 0; k < rc; k++) {

				// logger_.finest(consumedResources[j][1]);

				totalConsumedResources[k] += consumedResources[index][k];
			}

		}

		return totalConsumedResources;

	}

	/*
	 * Need to calculate the Maximum Software Value
	 * 
	 */
	public void excelOutput(String fileName, double[][] outputArray, int lpCheck) {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			File file = new File(fileName_);
			System.out.println("writing to " + fileName_);
			FileWriter fstream = new FileWriter(file, true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("\n" + outputValues_[0][0]);
			if (lpCheck != 1) {
				for(int k = 0; k < 2; k++){
					for (int i = 1; i < outputValues_.length; i++) {
						out.write("," + outputValues_[i][k]);
						System.out.println("writing " + outputValues_[i][k]);
					}
					out.write("\n LP: ");
				}
				out.close();
			}
		} catch (Exception e) {
			System.out.println("Write Interface " + e);
		}

	}

	public void writeStringToFile(String st ) {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			File file = new File(fileName_);
			FileWriter fstream = new FileWriter(file, true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(st);
			out.close();
		} 
		catch (Exception e) {
			System.out.println("Write Interface " + e);
		}

	}

	public static void main(String args[]) {

		Level l = Level.FINEST;
		Handler[] handlers = Logger.getLogger("").getHandlers();
		for (int index = 0; index < handlers.length; index++) {
			handlers[index].setLevel(l);
		}
		logger_.setLevel(l);

		int rmin = 5; // Min resource consumption per feature (not MMKP item)
		int rmax = 50; // Max resource consumption per feature
		int vmin = 5; // Min item value
		int vmax = 50; // Max item value
		int vtotal = 100; // Total value that can be distributed per subtree
		int deltav = 10; // Maximum deviation between largest valued feature
		// and
		// optimal feature
		int totalres = 3; // Total resource types
		int rtmin = 100; // Minimum available resource value
		int rtmax = 200; // Maximum available resource value
		int totalf = 100; // Total features
		int xorpercent = 100; // Percentage of features in XOR groups
		int maxchildren = 10; // Max children per feature
		int maxdepth = 75; // Max depth of feature model
		int md = 0; // Percentage of cross-tree constraints
		FCFTools fcf = new FCFTools();
		int stopper = 1;
		int iterations =0;
		MMKPTreesTwo test = new MMKPTreesTwo(2,3,2);
		System.out.println(System.currentTimeMillis());
		while (stopper < 20) {

				MMKPProblem sw = MMKPProblem.gen(stopper, 10, 20, 3, rtmin, rtmax, rmin,
						rmax, vmin, vmax);
				MMKPProblem hw = MMKPProblem.gen(stopper, 10, 20, 3, rtmin, rtmax, rmin,
						rmax, vmin, vmax);
			
		
				fcf.applyResourceCoefficients(hw, new int[] { 1, -1, -1 });
	
				int budget = sw.getResources()[0];   
				
				int optvalue= 0;
				 test = new MMKPTreesTwo(10, budget,optvalue); // / whole percentage of
		
				test.writeStringToFile("\n");
		
				test = new MMKPTreesTwo(10,budget,optvalue);
	
				test.execute(sw, hw, 1, 2, "withCorrector.txt",true);
				System.out.println(" Sets: "+ stopper );
				iterations++;
				

			stopper++;

		}

	}

}