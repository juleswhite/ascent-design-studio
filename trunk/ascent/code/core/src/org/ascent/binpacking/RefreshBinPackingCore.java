package org.ascent.binpacking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ascent.ResourceConsumptionPolicy;
import org.ascent.configurator.AbstractRefreshCore;
import org.ascent.configurator.RefreshCore;
import org.ascent.configurator.UnsupportedConstraintException;
import org.ascent.configurator.conf.debug.ConstraintReference;
import org.ascent.expr.BinaryExpression;
import org.ascent.expr.Expression;
import org.ascent.util.ParsingUtil;

/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
public abstract class RefreshBinPackingCore extends AbstractRefreshCore
		implements RefreshCore, StateProvider, DependencyManager {

	public static final String MAPPED_COUNT_RESOURCE = "__mapped";

	private Map<Object, Dependencies> dependencyMap_ = new HashMap<Object, Dependencies>();
	private Map<Object, ItemState> sourceStates_ = new HashMap<Object, ItemState>();
	private Map<Object, BinState> targetStates_ = new HashMap<Object, BinState>();
	private Map<Object,ResourceConsumptionPolicy> resourcePolicies_ = new HashMap<Object, ResourceConsumptionPolicy>();
	private boolean resourcesSet_ = false;
	private List resources_ = new ArrayList();
	private List sources_ = new ArrayList();
	private List targets_ = new ArrayList();
	private List usedTargets_;
	private int[] sizeTotals_;
	private int minTargetLowerBound_ = Integer.MAX_VALUE;
	private double optimalityLowerBound_ = Double.MAX_VALUE;
	private boolean allowPartialSolutions_ = false;
	private boolean useMappedCountResource_ = false;

	public Dependencies getDependencies(Object st) {
		Dependencies d = dependencyMap_.get(st);
		if (d == null) {
			d = new Dependencies();
			dependencyMap_.put(st, d);
		}
		return d;
	}

	public ItemState getSourceState(Object o) {
		ItemState state = sourceStates_.get(o);
		if (state == null) {
			state = new ItemState();
			state.setItem(o);
			state.setAllTargets(getTargets());
			sourceStates_.put(o, state);
		}
		return state;
	}

	public BinState getTargetState(Object o) {
		BinState state = targetStates_.get(o);
		if (state == null) {
			state = new BinState();
			state.setItem(o);
			targetStates_.put(o, state);
		}
		return state;
	}

	public ConstraintReference addExcludesMappingConstraint(Object src,
			Object req) {
		getDependencies(src).add(new Excludes(this, src, req));
		getDependencies(req).add(new Excludes(this, req, src));
		return null;
	}

	public ConstraintReference addRequiresMappingConstraint(Object src,
			Object req) {
		getDependencies(src).add(new Requires(this, src, req));
		return null;
	}

	public ConstraintReference addSourceMappedCardinalityConstraint(Object src,
			int min, int max) {
		getSourceState(src).setMaxInstances(max);
		getSourceState(src).setMinInstances(min);
		return null;
	}

	public ConstraintReference requireAllMapped() {
		for (Object src : sources_)
			requireMapped(src);
		return null;
	}

	public ConstraintReference requireAllMappedExactlyOnce() {
		for (Object src : sources_)
			addSourceMappedCardinalityConstraint(src, 1, 1);
		return null;
	}

	public ConstraintReference requireMapped(Object src) {
		getSourceState(src).setMinInstances(1);
		return null;
	}

	public ConstraintReference requireNotMapped(Object src) {
		getSourceState(src).setMaxInstances(0);
		return null;
	}

	public void setValidTargets(Object src, List valid) {
		getSourceState(src).setValid(valid);
	}

	public void setSetsToMap(List srcs, List trgs) {
		sources_ = srcs;
		targets_ = trgs;
		usedTargets_ = new ArrayList(trgs.size());
	}

	public void setSetsToMap(Object[] srcs, Object[] trgs) {
		ArrayList a1 = new ArrayList(srcs.length);
		a1.addAll(Arrays.asList(srcs));
		ArrayList a2 = new ArrayList(trgs.length);
		a2.addAll(Arrays.asList(trgs));
		setSetsToMap(a1, a2);
	}

	protected void preProcess() {
		if (!resourcesSet_) {
			initMappedCountResource();
			setResourceConstraints(collectSourceSizes(), collectTargetSizes());
			clearVariableTables();
		}
	}

	protected void initMappedCountResource() {
		for (Object o : getSources()) {
			getSourceVariableValues(o).put(MAPPED_COUNT_RESOURCE, 1);
		}
		for (Object o : getTargets()) {
			if (getTargetVariableValues(o).get(MAPPED_COUNT_RESOURCE) == null) {
				getTargetVariableValues(o).put(MAPPED_COUNT_RESOURCE,
						getSources().size());
			}
		}
	}

	public Map<Object, List> nextMapping() {
		try {

			preProcess();

			while (!done()) {
				preIterate();
				iterate();
				postIterate();
			}

			postProcess();

			if (!allowPartialSolutions_ && !validSolution())
				return null;

			Map<Object, List> solution = new HashMap<Object, List>();
			for (Object o : sources_)
				solution.put(o, getSourceState(o).getCurrentTargets());

			return solution;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void preIterate() {
	}

	public void postIterate() {
	}

	public void postProcess() {
		optimalityLowerBound_ = ((double) usedTargets_.size())
				/ ((double) minTargetLowerBound_);
	}

	public void iterate() {
		Object src = selectSource();
		Object target = selectTarget(src);

		if (target != null) {
			if (!usedTargets_.contains(target))
				usedTargets_.add(target);
		}

		if (target != null) {
			ItemState state = getSourceState(src);
			BinState ts = getTargetState(target);

			insert(state, ts);
			state.addTarget(target);
			ts.getSources().add(state);
			Dependencies deps = getDependencies(src);
			deps.update(target);
		}
	}
	
	public int getConsumed(BinState ts, int res){
		int cons = 0;
		for(ItemState st : ts.getSources()){
			cons += st.getSize()[res];
		}
		return cons;
	}

	public boolean willFit(ItemState ss, BinState ts) {
		for (int i = 0; i < ss.getSizeWithDependencies().length; i++){
			ResourceConsumptionPolicy policy = getResourcePolicies().get(i);
			if(policy != null){
				ArrayList dep = new ArrayList();
				for(ItemState st : ts.getSources()){
					dep.add(st.getItem());
				}
				dep.add(ss.getItem());
				if(policy.getResourceResidual(dep, ts.getItem(), ts.getSize()[i], getConsumed(ts,i) + ss.getSize()[i]) < 0)
					return false;
			}
			else if (ss.getSizeWithDependencies()[i] > ts.getSize()[i])
				return false;
		}
		return true;
	}

	public void insert(ItemState ss, BinState ts) {
		for (int i = 0; i < ss.getSize().length; i++)
			ts.getSize()[i] -= ss.getSize()[i];
	}

	public void remove(ItemState ss, BinState ts) {
		for (int i = 0; i < ss.getSize().length; i++)
			ts.getSize()[i] += ss.getSize()[i];
	}

	public List getSources() {
		return sources_;
	}

	public void setSources(List sources) {
		sources_ = sources;
	}

	public List getTargets() {
		return targets_;
	}

	public void setTargets(List targets) {
		targets_ = targets;
	}

	public boolean validSolution() {
		for (Object o : sources_)
			if (getSourceState(o).needsFurtherMapping())
				return false;

		return true;
	}

	public void setResourceConstraints(int[][] sourcesizes, int[][] targetsizes) {
		if (sourcesizes != null && targetsizes != null) {

			resourcesSet_ = true;

			if (sourcesizes.length > 0)
				sizeTotals_ = new int[sourcesizes[0].length];
			else
				sizeTotals_ = new int[0];

			for (int i = 0; i < sourcesizes.length; i++) {
				ItemState st = getSourceState(getSources().get(i));
				st.setSize(sourcesizes[i]);
				for (int j = 0; j < sourcesizes[i].length; j++) {
					sizeTotals_[j] += sourcesizes[i][j];
				}
			}
			for (int i = 0; i < targetsizes.length; i++) {
				BinState st = getTargetState(getTargets().get(i));
				st.setSize(targetsizes[i]);
			}

			int[] counts = new int[sizeTotals_.length];
			int[] totals = Arrays.copyOf(sizeTotals_, sizeTotals_.length);
			for (int j = 0; j < sizeTotals_.length; j++) {
				// need to sort targetssizes[i] first!!!
				int[] vals = new int[targetsizes.length];
				for (int i = 0; i < targetsizes.length; i++) {
					vals[i] = targetsizes[i][j];
				}
				Arrays.sort(vals);
				for (int i = vals.length - 1; i >= 0; i--) {
					counts[j]++;
					totals[j] -= vals[i];
					if (totals[j] <= 0)
						break;
				}
			}
			Arrays.sort(counts);

			if (counts.length > 0)
				minTargetLowerBound_ = counts[counts.length - 1];
			else
				minTargetLowerBound_ = 1;
		}
	}

	public int getMinTargetLowerBound() {
		return minTargetLowerBound_;
	}

	public void setMinTargetLowerBound(int minTargetLowerBound) {
		minTargetLowerBound_ = minTargetLowerBound;
	}

	public double getOptimalityLowerBound() {
		return optimalityLowerBound_;
	}

	public void setOptimalityLowerBound(double optimalityLowerBound) {
		optimalityLowerBound_ = optimalityLowerBound;
	}

	public ConstraintReference addTargetIntResourceConstraint(Object target,
			Object var, int val) {
		getTargetVariableValues(target).put(var, val);
		if (!resources_.contains(var))
			resources_.add(var);
		return null;
	}

	protected int[][] collectTargetSizes() {
		int[][] res = new int[getTargets().size()][resources_.size()];
		for (int i = 0; i < getTargets().size(); i++) {
			Object t = getTargets().get(i);
			Map vals = getTargetVariableValues(t);
			for (int j = 0; j < resources_.size(); j++) {
				if(i == 0){
					ResourceConsumptionPolicy policy = getResourcePolicies().get(resources_.get(j));
					if(policy != null){
						getResourcePolicies().put(j, policy);
					}
				}
				Object r = resources_.get(j);
				Object v = vals.get(r);
				if (v != null)
					res[i][j] = ParsingUtil.toInt(v);
				else if (getZeroUndefinedVariables())
					res[i][j] = 0;
				else
					throw new UndefinedAttributeException("The attribute [" + r
							+ "] is not defined for the target [" + t + "]");

			}
		}
		return res;
	}

	protected int[][] collectSourceSizes() {
		int[][] res = new int[getSources().size()][resources_.size()];
		for (int i = 0; i < getSources().size(); i++) {
			Object t = getSources().get(i);
			Map vals = getSourceVariableValues(t);
			for (int j = 0; j < resources_.size(); j++) {
				Object r = resources_.get(j);
				Object v = vals.get(r);
				if (v != null)
					res[i][j] = ParsingUtil.toInt(v);
				else if (getZeroUndefinedVariables())
					res[i][j] = 0;
				else
					throw new UndefinedAttributeException("The attribute [" + r
							+ "] is not defined for the source [" + t + "]");

			}
		}
		return res;
	}

	public Map<Object, List> getHostedMap() {
		Map<Object, List> hmap = new HashMap<Object, List>();
		for (Object o : getSources()) {
			ItemState st = getSourceState(o);
			for (Object t : st.getCurrentTargets()) {
				List thost = hmap.get(t);
				if (thost == null) {
					thost = new ArrayList();
					hmap.put(t, thost);
				}
				thost.add(o);

			}
		}
		return hmap;
	}

	public String toString() {
		Map<Object, List> hmap = getHostedMap();

		String state = "\n{\n";

		for (Object o : getTargets()) {
			state += "Bin " + o + " " + getTargetState(o) + "\n[\n";

			List hosted = hmap.get(o);
			if (hosted != null) {
				for (Object s : hosted)
					state += s + "(" + getSourceState(s) + ")\n";
			}

			state += "\n]\n";
		}

		state += "\n}\n";
		return state;
	}

	public List getUsedTargets() {
		return usedTargets_;
	}

	public void setUsedTargets(List usedTargets) {
		usedTargets_ = usedTargets;
	}

	public boolean isAllowPartialSolutions() {
		return allowPartialSolutions_;
	}

	public void setAllowPartialSolutions(boolean allowPartialSolutions) {
		allowPartialSolutions_ = allowPartialSolutions;
	}

	public abstract Object selectSource();

	public abstract Object selectTarget(Object source);

	public abstract boolean done();

	public ConstraintReference addSelectMappingConstraint(Object src, List set,
			int min, int max) {
		return null;
	}

	public ConstraintReference addSelectMappingConstraint(Object src, List set,
			int exactcard) {
		return null;
	}

	public ConstraintReference addSourceMappingConstraint(Object src,
			BinaryExpression expression) {
		throw new UnsupportedConstraintException(getClass().getName()
				+ " does not support this constraint.");
	}

	public ConstraintReference addTargetMappedSourcesCardinalityConstraint(
			Object target, int min, int max) {
		if (min > 0)
			throw new UnsupportedConstraintException(getClass().getName()
					+ " does not support this constraint.");
		else {
			useMappedCountResource_ = true;
			addTargetIntResourceConstraint(target, MAPPED_COUNT_RESOURCE, max);
		}
		return null;
	}
	
	public Map<Object, ResourceConsumptionPolicy> getResourcePolicies() {
		return resourcePolicies_;
	}

	public void setResourcePolicies(
			Map<Object, ResourceConsumptionPolicy> resourcePolicies) {
		resourcePolicies_ = resourcePolicies;
	}

	public ConstraintReference addTargetMappedSourcesConstraint(Object target,
			int exactmapped) {

		throw new UnsupportedConstraintException(getClass().getName()
				+ " does not support this constraint.");
	}

	public void createIntVariable(Object variable, String op, Expression exp) {
		throw new UnsupportedConstraintException(getClass().getName()
				+ " does not support this constraint.");
	}

	public void createIntVariable(Object ctx, Object variable, String op,
			Expression exp) {
		throw new UnsupportedConstraintException(getClass().getName()
				+ " does not support this constraint.");
	}

	public Map getContextVariableValues(Object ctx) {
		throw new UnsupportedConstraintException(getClass().getName()
				+ " does not support this constraint.");
	}

	public boolean getMaximizeOptimizationFunction() {
		return false;
	}

	public List getSourceTargets(Object src) {
		return null;
	}

	public boolean getUseBranchAndBound() {
		throw new UnsupportedConstraintException(getClass().getName()
				+ " does not support this constraint.");
	}

	public Object getVariableValue(String var) {
		throw new UnsupportedConstraintException(getClass().getName()
				+ " does not support this constraint.");
	}

	public Object getVariableValue(Object ctx, String var) {
		throw new UnsupportedConstraintException(getClass().getName()
				+ " does not support this constraint.");
	}

	public Map<Object, List> nextMappingWithinNodeLimit(int nodes) {
		return null;
	}

	public Map<Object, List> nextMappingWithinTimeLimit(int time) {
		return null;
	}

	public void setInValidTargets(Object src, List invalid) {
	}

	public void setIntVariableValue(Object variable, int value) {
	}

	public void setMaximizeOptimizationFunction(
			boolean maximizeOptimizationFunction) {
	}

	public void setOptimizationFunction(Expression exp) {
	}

	public void setUseBranchAndBound(boolean useBranchAndBound) {
	}

}
