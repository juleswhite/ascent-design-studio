package org.ascent;

import java.util.List;

public interface ResourceConsumptionPolicy {
	public int getResourceResidual(List consumers, Object producer, int avail, int consumed);
}
