/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
package org.ascent.configurator;

import java.util.List;
import java.util.Map;

import org.ascent.configurator.conf.RefreshProblem;
import org.ascent.configurator.conf.debug.SolutionConsistencyChecker;

/**
 * This class provides a number of static
 * utility methods for dealing with solutions
 * from a <code>RefreshCore</code>.
 * 
 * @author Jules White
 *
 */
public class Solutions {

	private Solutions(){}
	
	/**
	 * This method checks that the specified solution
	 * is consistent with all of the constraints in
	 * the specified <code>RefreshProblem</code>. If
	 * the solution is not consistent, a <code>RuntimeException</code>
	 * is thrown. A solution is consistent if all of the source
	 * and target items from the solution are defined in the
	 * problem and all of the mapping constraints from the
	 * problem are observed by the mapping in the solution.
	 * 
	 * @param problem the RefreshProblem that the solution must be
	 *                consistent with
	 * @param solution the solution from the RefreshCore that is being
	 *                 checked for consistency
	 */
	public static void assertConsistent(RefreshProblem problem, Map<Object,List> solution){
		SolutionConsistencyChecker c = new SolutionConsistencyChecker(problem);
		c.assertConsistent(solution);
	}
}
