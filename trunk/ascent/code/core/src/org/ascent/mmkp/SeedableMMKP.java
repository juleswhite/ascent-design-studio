package org.ascent.mmkp;

import java.util.ArrayList;
import java.util.List;

public class SeedableMMKP extends MMKP {

	private List<List<Item>> seeds_ = new ArrayList<List<Item>>();

	public SeedableMMKP(MMKPProblem problem) {
		super(problem);
	}

	public List<List<Item>> getSeeds() {
		return seeds_;
	}

	public void setSeeds(List<List<Item>> seeds) {
		seeds_ = seeds;
	}
	
	
}
