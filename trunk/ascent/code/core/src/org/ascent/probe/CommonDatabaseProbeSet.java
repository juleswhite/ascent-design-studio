/*******************************************************************************
 * Copyright (c) 2007 Jules White. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jules White - initial API and implementation
 ******************************************************************************/
package org.ascent.probe;


public class CommonDatabaseProbeSet extends ProbeSet {

	public CommonDatabaseProbeSet() {
		super();
		getProbes().add(new ClassProbe("DB2","com.ibm.db2.jdbc.app.DB2Driver"));
		getProbes().add(new ClassProbe("MySQL","com.mysql.jdbc.Driver"));
		getProbes().add(new ClassProbe("Oracle","oracle.jdbc.driver.OracleDriver"));
		getProbes().add(new ClassProbe("HSQL","org.hsqldb.jdbcDriver"));
		getProbes().add(new ClassProbe("MSSQL","com.microsoft.jdbc.sqlserver.SQLServerDriver"));
	}

}
