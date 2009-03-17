package org.ascent.excel;

public class naomi_excel_xls {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// Change this to read in the filename from the command line
		// so it doesn't have to be hard-coded.
		String in_file = "test.xls";
		String in_names = "";				//just a place holder for now
		String out_file = "output.xls";
		String out_names = "";				//just a place holder for now
		
		ExConnector ex = new ExConnector();
		ex.readInput(in_file, in_names);
		ex.execute();
		ex.writeOutput(out_file, out_names);
		
	}

}
