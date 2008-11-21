package org.ascent.deployment;

import java.util.List;

import org.ascent.ResourceConsumptionPolicy;

public class RateMonotonicResource implements
		ResourceConsumptionPolicy {

	public int getResourceResidual(List consumers, Object producer,
			int avail, int consumed) {
		int tasks = consumers.size();
		if(tasks != 0 && consumers.get(0) instanceof Schedulable){
			tasks = 0;
			for(Object o : consumers){
				if(o instanceof Schedulable){
					tasks += ((Schedulable)o).getTotalTasks();
				}
				else {
					tasks++;
				}
			}
		}
		if(tasks == 1)
			return avail - consumed;
		else {
			double aavail = 100 * getAvailable(tasks);
			return (int)Math.rint(aavail - consumed);
		}
	}

	public double getAvailable(int consumers){
		return consumers * (Math.pow(2, (1.0/consumers)) - 1);
	}
}
