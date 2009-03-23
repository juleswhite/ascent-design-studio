package org.ascent.excel;

public class naomi_excel_xls {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// Change this to read in the filename from the command line
		// so it doesn't have to be hard-coded.
		String file_prefix = "";	// Change this to be whatever is necessary on your machine.
		String in_file = file_prefix + "\\test.xls";
		//String in_names = "";				//just a place holder for now
		String out_file = file_prefix + "\\output.xls";
		//String out_names = "";				//just a place holder for now
		String utils_loc = file_prefix + "\\Artifacts";
		
		ExConnector ex = new ExConnector();
		ex.readInput(in_file);
		ex.readAttributes(utils_loc);
		ex.execute();
		ex.writeOutput(out_file);
		
	}

}
