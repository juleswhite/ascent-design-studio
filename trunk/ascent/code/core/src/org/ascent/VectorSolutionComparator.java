package org.ascent;

import java.util.Comparator;

import org.ascent.binpacking.ValueFunction;

public class VectorSolutionComparator implements Comparator<VectorSolution>{

	private ValueFunction<VectorSolution> valueFunction_;
	
	
	public VectorSolutionComparator(ValueFunction<VectorSolution> valueFunction) {
		super();
		valueFunction_ = valueFunction;
	}


	public ValueFunction<VectorSolution> getValueFunction() {
		return valueFunction_;
	}


	public void setValueFunction(ValueFunction<VectorSolution> valueFunction) {
		valueFunction_ = valueFunction;
	}


	public int compare(VectorSolution o1, VectorSolution o2) {
		return (int)Math.rint(valueFunction_.getValue(o1) - valueFunction_.getValue(o2));
	}

}
