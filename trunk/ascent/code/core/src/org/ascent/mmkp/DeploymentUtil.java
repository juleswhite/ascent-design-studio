package org.ascent.mmkp;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeploymentUtil {

	public void saveAsDeployment(MMKPProblem mmkp, File target){
		String out = "";
		
		Map<Integer,List<Integer>> sets = mmkp.getSetsMap();
		for(Integer set : sets.keySet()){
			out += "Component"+set+" {\n";
				for(Integer node : sets.get(set)){
					
				}
			out += "}\n";
		}
	}
}
