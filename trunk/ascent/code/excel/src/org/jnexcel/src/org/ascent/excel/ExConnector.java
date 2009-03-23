package org.ascent.excel;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import jxl.Cell;
import jxl.CellFeatures;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.lmco.atl.naomi.attributes.Resource;
import com.lmco.atl.naomi.api.NaomiAttribute;
import com.lmco.atl.naomi.api.NaomiFileUtils;

public class ExConnector {
	
	private int totalCount_ = 0;
	
	private Hashtable<String, Object[]> cells_ = new Hashtable<String, Object[]>();
	
	public void readInput(String loc) {
		
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
	
	// This function finds the current values of the resources for the attributes
	// requested by the xls sheet by comments that start with naomi_get.attrName[].
	// It puts the values of the resources in the hashtable, but does not change
	// them in the spreadsheet.
	// It also sets the current values of the resources for the attributes from
	// the xls sheet with comments that start with naomi_put.attrName[].  It changes
	// the attribute files, but not the hashtable or spreadsheet in this case
	public void readAttributes(String utils_loc) {
		
		try {
			// Create the NaomiFileUtils object
			NaomiFileUtils utils = new NaomiFileUtils(new File(utils_loc));
			List<File> attributes = utils.getAttributeFiles();
			
			// Get the names from the hash table we need to work with
			Enumeration<String> keys = cells_.keys();
			while (keys.hasMoreElements())
			{
				String current_name = keys.nextElement();
				if (current_name.startsWith("naomi_")) {
					String attr_name = current_name.substring(
							current_name.indexOf('.')+1, current_name.indexOf('['));
					System.out.print("Using the attribute " + attr_name);
					System.out.println(" from utils.");
					for (int i = 0; i < attributes.size(); i++) {
						// Get the actual attribute we want to read in
						File f = attributes.get(i);
						if (f.getName().equals(attr_name)) {
							NaomiAttribute actual_attr = new NaomiAttribute(f);
							Resource[] arr = actual_attr.getResource();
							// Assume the resource we want is the first one, may
							// change this later.
							if (current_name.startsWith("naomi_get.")) {
								Object[] objs = new Object[1];
								objs[0] = arr[0];
								cells_.put(current_name, objs);
								System.out.print("Modified cell with name "+current_name);
								System.out.println(" and contents " + objs[0]);
							}
							else if (current_name.startsWith("naomi_put.")) {
								Object[] objs = cells_.get(current_name);
								Resource r = new Resource();
								r.setName(arr[0].getName());
								r.setChecksum(arr[0].getChecksum());
								r.setUri(objs[0].toString());
								// Switch this line for the next two to make it
								// append the modified resource instead of changing
								// the original.
								//actual_attr.addResource(r);
								arr[0] = r;
								actual_attr.setResource(arr);
								actual_attr.save();
								System.out.print("Modified attribute with name "+current_name);
								System.out.println(" and contents " + objs[0]);
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			System.out.print("Unhandled exception getting the attributes from ");
			System.out.println("the utils object.");
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
	
	public void writeOutput(String loc) {
		// Create the new excel file
		try {
			WritableWorkbook out_workbook = Workbook.createWorkbook(new File(loc));
			WritableSheet out_sheet = out_workbook.createSheet("First Sheet", 0);
			
			Enumeration<String> keys = cells_.keys();
			int i = 0;
			while (keys.hasMoreElements())
			{
				String current_name = keys.nextElement();
				Object[] val = cells_.get(current_name);
				// Change this to test for different types of content.
				String contents = (val[0]).toString();
				System.out.println("About to write the value " + contents);
				Label label = new Label(0, i, contents);
				i++;
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
