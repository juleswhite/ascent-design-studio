package org.ascent.excel;

import java.io.File;
import java.util.Hashtable;

import jxl.Cell;
import jxl.CellFeatures;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExConnector {
	
	private int totalCount_ = 0;
	
	private Hashtable<String, Object[]> cells_ = new Hashtable<String, Object[]>();
	
	public void readInput(String loc, String names) {
		
		File f = new File(loc);
		
		totalCount_ = 0;
		cells_ = new Hashtable<String, Object[]>();

		try {
			Workbook workbook = Workbook.getWorkbook(f);
			
			WorksheetManipulator manip = new WorksheetManipulator();
			Sheet s = workbook.getSheet(0);
			int numCols = manip.getColumnCount(s);
			int numRows = manip.getRowCount(s);
			
			//Cell c = workbook.findCellByName("MyCell");
			//System.out.println("MyCell.value = " + c.getContents());

			for(int i = 0; i < numRows; i++) {
				for(int j = 0; j < numCols; j++) {
					Cell c = s.getCell(j,i);
					CellFeatures cf = c.getCellFeatures();
					if (cf != null) {
						String comment = cf.getComment();
						System.out.println("The comment is: " + comment);
						String name = "";
						if (comment.contains("\n")) {
							// This is necessary because the first line of the comment
							// is the author, not the name.
							name = comment.substring(comment.indexOf("\n")+1, 
									comment.indexOf("\n", comment.indexOf("\n")+1));
						}
						else {
							name = comment;
						}
						String contents = s.getCell(j,i).getContents();
						Object[] obj = new Object[1];
						obj[0] = contents;
						cells_.put(name, obj);
						totalCount_++;
						System.out.println("Added cell with name " + name + ".");
						System.out.println("and contents " + obj[0]);
					}
				}
			}
			
			workbook.close();
		}
		catch (Exception e) {
			System.out.print("Unhandled exception when opening excel file ");
			System.out.println(loc + " with exception message: ");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return;
		}
	}
	
	public void execute() {
		
		/**********************************************************************
		 * Add code here to modify the Hashtable cells_ in any way desired before
		 * it is written to the output excel file
		 **********************************************************************/
		
	}
	
	public void writeOutput(String loc, String names) {
		// Create the new excel file
		try {
			WritableWorkbook out_workbook = Workbook.createWorkbook(new File(loc));
			WritableSheet out_sheet = out_workbook.createSheet("First Sheet", 0);
			
			for(int i = 0; i < totalCount_; i++) {
				// Change this so it doesn't have to be hard coded.
				Object[] val = cells_.get("naomi_put.attr[C,1]");
				// Change this to test for different types of content.
				String contents = (val[0]).toString();
				System.out.println("About to write the value " + contents);
				Label label = new Label(0, i, contents);
				out_sheet.addCell(label);
			}
			
			out_workbook.write();
			out_workbook.close();
		}
		catch (Exception e) {
			System.out.print("Unhandled exception creating new excel file ");
			System.out.println("output.xls with exception message: ");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return;
		}
	}

}
