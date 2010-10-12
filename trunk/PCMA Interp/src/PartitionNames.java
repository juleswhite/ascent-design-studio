import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class PartitionNames {
	int floatNameIndex = 0;
	int intNameIndex = 0;
	ArrayList <String> intMemoryNames = new ArrayList();
	ArrayList <String> floatMemoryNames = new ArrayList();
	ArrayList<SchedulableTask> schedTasks_ = new ArrayList();
	
	HashMap <String, ArrayList<String>> rates = new HashMap();
	String projectDirectory_;
	ArrayList<String> taskNames_ = new ArrayList();
	int [] start_ = new int [2];
	int [] last = new int [2];
	String appName_;
	
	public PartitionNames(String projectDirectory, double minRate,String appName, int [] start){
		projectDirectory_ = projectDirectory;
		floatNameIndex = start[0];
		intNameIndex = start[1];
		System.out.println("float index = " +floatNameIndex); 
		System.out.println("Int index = " + intNameIndex);
		appName_ = appName;
		defineApplicationClass(minRate, appName);
		last[0] = floatNameIndex;
		last[1] = intNameIndex;
		//defineApplicationClass(minRate,  appName);
		//defineApplicationClass(minRate, appName);
	}
	
	public void defineApplicationClass(double minRate,String appName){
		String content ="";
		content += "#include \"Application"+appName+".h\"\n";
		content += "#include <iostream>\n";
		//content += "using namespace std;\n";
		
		//content += "class Application {\n";
		/*for ( int i = 0; i < intMemoryNames.size(); i++){
			content += "      int " + intMemoryNames.get(i) + ";\n";
		}*/
		
		ArrayList<String> taskContents = makeTaskMethods(appName);
		String taskContent = taskContents.get(0);
		makeSchedule(minRate);
		String partitionContent = makePartition("P1");
		//content += partitionContent;
		content += taskContent;
		//content += writeSchedule();
		//content += "};";
		String allHeader = taskContents.get(1)+partitionContent + taskContents.get(2);
		writeFile("Application"+appName+".h", allHeader,projectDirectory_);
		writeFile("Application"+appName+".cpp", content, projectDirectory_);
	}
	
	
	

	
	
	public ArrayList<String> makeTaskMethods(String appName){
		String content = "";
		ArrayList<String> results = new ArrayList();
		String headerContent = "#ifndef APPLICATION"+appName+"_H\n";
		headerContent += "#define APPLICATION"+appName+"_H\n";
		headerContent += "#include <iostream>\n";
		headerContent += "class Application"+appName+"{\n";
		headerContent += "\tpublic:\n";
		 float multiplier = (float) 1;
		String headerContent3 = "";
		ArrayList<String> contents = new ArrayList();
		contents = DefineTask(appName, "01", "P1", 4000*multiplier, 0.05f);
		content += "\n"+contents.get(0);
		headerContent3 += "\t"+contents.get(1);
		//content += DefineTask(appName, "01", "P1", 4000, 0.05f);
		contents = DefineTask(appName, "02", "P1", 2500*multiplier, 0.1f);
		content += "\n\t"+contents.get(0);
		headerContent3 += "\t"+contents.get(1);
		contents = DefineTask(appName, "03", "P1", 1000*multiplier, 0.1f);
		content += "\n\t"+contents.get(0);
		headerContent3 += "\t"+contents.get(1);
		contents = DefineTask(appName, "04", "P1", 3000*multiplier, 0.01f);
		content += "\n"+contents.get(0);
		headerContent3 += "\t"+contents.get(1);
		//content += DefineTask(appName, "01", "P1", 4000, 0.05f);
		contents = DefineTask(appName, "05", "P1", 4000*multiplier, 0.1f);
		content += "\n\t"+contents.get(0);
		headerContent3 += "\t"+contents.get(1);
		contents = DefineTask(appName, "06", "P1", 2000*multiplier, 0.1f);
		content += "\n\t"+contents.get(0);
		headerContent3 += "\t"+contents.get(1);
		contents = DefineTask(appName, "07", "P1", 500*multiplier, 0.01f);
		content += "\n"+contents.get(0);
		headerContent3 += "\t"+contents.get(1);
		//content += DefineTask(appName, "01", "P1", 4000, 0.05f);
		contents = DefineTask(appName, "08", "P1", 400*multiplier, 0.01f);
		content += "\n\t"+contents.get(0);
		headerContent3 += "\t"+contents.get(1);
		contents = DefineTask(appName, "09", "P1", 1000*multiplier, 0.01f);
		content += "\n\t"+contents.get(0);
		headerContent3 += "\t"+contents.get(1);
		contents = DefineTask(appName, "10", "P1", 600*multiplier, 0.01f);
		content += "\n"+contents.get(0);
		headerContent3 += "\t"+contents.get(1);
		headerContent3 += "\tvoid executeTasks(int);\n";
		headerContent3 += "};\n#endif";
		writeFile("Application"+appName+".h",headerContent, projectDirectory_);
		results.add(content);
		results.add(headerContent);
		results.add(headerContent3);
		/*
		content += "\n"+
		content += "\n"+
		content += "\n"+DefineTask("A", "07", "P1", 500, 0.01f);

		

		content += "\n"+DefineTask("A", "08", "P1", 400, 0.01f);
		content += "\n"+
		content += "\n"+
		content += "\n"+DefineTask("A", "11", "P1", 1000, 0.1f);
		content += "\n"+DefineTask("A", "12", "P1", 700, 0.1f);
		content += "\n"+DefineTask("A", "13", "P1", 2000, 0.05f);
		content += "\n"+DefineTask("A", "14", "P1", 1000, 0.05f);
		content += "\n"+DefineTask("A", "15", "P1", 1000, 0.1f);
		content += "\n"+DefineTask("A", "16", "P1", 100, 0.05f);*/
		return results;
	}
	
	public int[] getLast() {
		return last;
	}
	
	public ArrayList<SchedulableTask> getSchedTasks_() {
		return schedTasks_;
	}
	
	public String definePartitionMemory(String referenceType){
		
		String referenceName = "";
		if(referenceType.equalsIgnoreCase("float")){
			referenceName = "floatVar"+floatNameIndex;
			floatMemoryNames.add(referenceName);
			floatNameIndex++;
		}
		else{
			referenceName = "intVar"+intNameIndex;
			intMemoryNames.add(referenceName);
			intNameIndex++;
		}
		return referenceName;
	
	}
	
	public String makePartition(String partitionName){
		String partitionFileName;
		System.out.println(" In make Partition.txt");
		partitionFileName = partitionName +"Memory.txt";
		String content ="";
	//	content += "struct{\n";
		for ( int i = 0; i < intMemoryNames.size(); i++){
			content += "      int " + intMemoryNames.get(i) + ";\n";
		}
		
	//	content += "}" + "I;\n\n";
	//	content += "struct{\n";
		for ( int i = 0; i < floatMemoryNames.size(); i++){
			content += "      float " + floatMemoryNames.get(i) + ";\n";
		}
		//content += "}" + "F;\n";
		System.out.println(" ABout to write to " + partitionFileName);
		//writeFile(partitionFileName, content, projectDirectory_);
		return content;
	}
	
	public String makeRandomPartition(String referenceType){
		String referenceName = "";
		if( referenceType.equalsIgnoreCase("float")){
			if (floatMemoryNames.isEmpty()){
				referenceName = definePartitionMemory(referenceType);
			}
			else{
				int rand = (int) Math.round(floatMemoryNames.size() * Math.random());
				if (rand == floatMemoryNames.size()){
					rand--;
				}
				referenceName =  floatMemoryNames.get(rand);				
			}
		}
		else{
			if (intMemoryNames.isEmpty()){
				referenceName = definePartitionMemory(referenceType);
			}
			else{
				int rand = (int) Math.round(intMemoryNames.size() * Math.random());
				if (rand == intMemoryNames.size()){
					rand--;
				}
				referenceName =  intMemoryNames.get(rand);				
			}
		}
		return referenceName;
		
	}
	
	public static void writeFile (String name, String content, String strDirectory){
		 FileOutputStream Output;
	     PrintStream file;
	 //    System.out.println(" about to write file");
	     new File(strDirectory).mkdir();
	          //must use a try/catch statement for it to work
	  //   System.out.println("Directory made");
	     try
	     {     
	    //	 System.out.print ("StrDirectory =  " + strDirectory);
	          Output = new FileOutputStream(strDirectory+"/"+name,false);
	          file = new PrintStream(Output);
	          file.print(content);
	      //  System.out.println("ContentWritten");
	     }
	     catch(Exception e)
	     {
	          
	     }
	}
	
	public ArrayList<String> DefineTask(String ApplicationName, String TaskNumber, String PartitionName, float MemoryTotal, float SharedProbability)
	{
		ArrayList<String> results = new ArrayList();
		String headerContent = "";
		String taskName;
		String taskFileName;
		String partitionMemoryPrefix;
		ArrayList<String> intsReferenced = new ArrayList();
		ArrayList<String> floatsReferenced = new ArrayList();
		String referenceType;
		String referenceName;
		String content = "";
		taskName = "Task"+ApplicationName+TaskNumber;
		taskNames_.add(taskName);
		
		headerContent += "void " + taskName +"(void);\n";
		taskFileName = taskName + ".txt";
		for (int i = 0; i < MemoryTotal; i++) {
			if(Math.random() < .5){
				referenceType = "float";
			}
			else{
				referenceType = "int";
			}
			if ( Math.random() < SharedProbability){
				//System.out.println("less than shard Probability");
				referenceName = makeRandomPartition(referenceType);
			}
			else{
				//System.out.println("GREATER than shard Probability");
				referenceName = definePartitionMemory(referenceType);
			}
			if( referenceType == "float"){
				floatsReferenced.add(referenceName);
			}
			else{
				intsReferenced.add(referenceName);
			}
		}
		/*
		 * Russell had some stuff in here to "save" all the taskVariables (to file it looks like) but I don't see
		 * it used. Leaving it out for now
		 */
		Collections.shuffle(intsReferenced);
		Collections.shuffle(floatsReferenced);
		content += "void Application"+ApplicationName+"::" + taskName +"(void)\n";
		content+= "{\n";
		for ( int i = 0; i <intsReferenced.size()-1; i++){
			content += "    int intVar" + i + ";\n";
		}
		for ( int i = 0; i < floatsReferenced.size()-1; i++){
			content += "    float floatVar" + i + ";\n";
		}
		String memberContent = new String(content);
		content = "";
		//System.out.pr
		int intNum =0;//intNameIndex; 
		int floatNum = 0;//floatNameIndex;
		System.out.println("Int Name Index = " + intNameIndex +" and floatnameindex = " + floatNameIndex);
		int count;
		String computation ="";
		while((intNum < intsReferenced.size()-1) && (floatNum  < floatsReferenced.size()-1)){
			count = 0;
			while(intNum < intsReferenced.size()-1 && count < 8){
				//computation = computation + " + I." + intsReferenced.get(intNum);
				computation = computation  + " + " + floatsReferenced.get(floatNum);
				
				count++;
				intNum++;
			}
			if(computation != ""){
				char[] chars = computation.toCharArray();//
				chars[1] = '=';
				computation = new String(chars);
			//	String memberVariable = "intVar" + intnum;
				if(!memberContent.contains("intVar"+intNum)){
					memberContent += "int intVar"+intNum+";\n";
				}
				content += "    intVar" + intNum + computation + " ;\n" ;
			}
			computation ="";
			count = 0;
			while(floatNum < floatsReferenced.size()-1 && count < 8){
				//computation = computation + " + F." + floatsReferenced.get(floatNum);
				computation = computation  + " + " + floatsReferenced.get(floatNum);
				
				count++;
				floatNum++;
			}
			if(computation != ""){
				char[] chars = computation.toCharArray();//
				chars[1] = '=';
				computation = new String(chars);
				if(!memberContent.contains("floatVar"+floatNum)){
					memberContent += "float floatVar"+floatNum+";\n";
				}
				content += "    floatVar" + floatNum + computation + " ;\n" ;
			}
			
		}
		content += "}\n";
		content = memberContent + content;
	//	System.out.println("Content = " + content);
		//writeFile(taskFileName, content, projectDirectory_);
		results.add(content);
		results.add(headerContent);
		return results;
	}
	
	public void makeSchedule(double minRate){
		/*
		 * 
		 */
		double rate = minRate;
		int divideCount = 0;
		while(rate >=1){
			ArrayList<String> n = new ArrayList();
			if(divideCount == 0){
				rates.put("n", n);
				
			}
			else{
				String ratelabel = "n/"+(int)Math.pow(2, divideCount);
				rates.put( ratelabel, n);
			}
			rate = rate/2;
			divideCount++;
		}
		Set<String> e = rates.keySet();
		   //iterate through Hashtable keys Enumeration
		Iterator it = e.iterator();
		ArrayList<SchedulableTask> schedTasks = new ArrayList();
	    while(it.hasNext()){
	    	String current = (String) it.next();
	    	ArrayList<String> aList = rates.get(current);
	    	//System.out.print("rate "+current+" tasks =");
	    	for(String a : aList){
	    	 // System.out.print(a +", ");
	    	}
		      System.out.println();
	    }
		for(String imn : taskNames_){
			double r = Math.random();
			if( r < .4){
				ArrayList<String> n = rates.get("n");
				n.add(imn);
				rates.put("n", n);
				SchedulableTask st = new SchedulableTask(imn,1,"N",appName_);
				schedTasks.add(st);
			}
			else if ( r>=.4 && r< .6){
				ArrayList<String> n = rates.get("n/2");
				n.add(imn);
				rates.put("n/2", n);
				SchedulableTask st = new SchedulableTask(imn,2,"N/2",appName_);
				schedTasks.add(st);
			}
			else if ( r>=.6 && r< .85){
				ArrayList<String> n = rates.get("n/4");
				n.add(imn);
				rates.put("n/4", n);
				SchedulableTask st = new SchedulableTask(imn,4,"N/4",appName_);
				schedTasks.add(st);
			}
			else{
				ArrayList<String> n = rates.get("n/8");
				n.add(imn);
				rates.put("n/8", n);
				SchedulableTask st = new SchedulableTask(imn,8, "N/8",appName_);
				schedTasks.add(st);
			}
		}
		schedTasks_ = schedTasks;
		//schedule = sc.scheduleTasks();
	
	}

    public String writeSchedule(ArrayList<SchedulableTask> sched){
    	String outputSchedule = "void Application::executeTasks(int executions){\n";
    	outputSchedule +="\t int i = 0; \n \t while(i < executions){\n\t\t";
    	for(SchedulableTask st : sched){
    		outputSchedule += st.getTaskName_()+"();//rate"+st.getRate_()+"\n\t\t";
    	}
    	outputSchedule += "\n\t\ti++;\n\t}\n}\n";
    	//writeFile("executionOrder.txt", outputSchedule, projectDirectory_);
    	return outputSchedule;
    }
	public static void main( String args[]){
		//PartitionNames pn = new PartitionNames("/Users/briandougherty",8.0);
		int [] go = {20000,20000};
		PartitionNames pn1 = new PartitionNames("/Users/briandougherty/PCMAEXP4", 8.0, "redApp",go);
		pn1.makeSchedule(8.0);
		ArrayList<SchedulableTask> allTasks= new ArrayList(); 
		allTasks.addAll(pn1.getSchedTasks_());
		//pn1.writeSchedule();
		System.out.println("Last[0] " +pn1.getLast()[0] +" and the other "+ pn1.getLast()[1]);
		PartitionNames pn2 = new PartitionNames("/Users/briandougherty/PCMAEXP4", 8.0, "blueApp", pn1.getLast());
		pn2.makeSchedule(8.0);
		allTasks.addAll(pn2.getSchedTasks_());
		//pn2.writeSchedule();
		System.out.println("Last[0] " +pn2.getLast()[0] +" and the other "+ pn2.getLast()[1]);
		
		PartitionNames pn3 = new PartitionNames("/Users/briandougherty/PCMAEXP4", 8.0, "yellowApp", pn2.getLast());
		pn3.makeSchedule(8.0);
		ArrayList<String> appNames = new ArrayList();
		appNames.add("redApp");
		appNames.add("blueApp");
		appNames.add("yellowApp");
		//appNames.add("redApp");
		//appNames.add("blueApp");
		//appNames.add("yellowApp");
		allTasks.addAll(pn3.getSchedTasks_());
		ExecutionMaker em = new ExecutionMaker(appNames);
		ArrayList<SchedulableTask> st = new ArrayList();
		st = allTasks;
		
		em.writeSchedule(allTasks,"/Users/briandougherty/PCMAEXP4/optimized", true);
		System.out.println("allTasks size " + allTasks.size());
		allTasks = new ArrayList();
		em.writeSchedule(st,"/Users/briandougherty/PCMAEXP4", false);
		System.out.println(" St size = " + st.size());
	//	em.writeSchedule(allTasks,"/Users/briandougherty/brian2/optimized/", true);
	//	em.writeSchedule(allTasks,"/Users/briandougherty/brian2/optimized/", false);
		//pn3.writeSchedule(allTasks);

		

		
		//pn.makePartition("P1");*/
	}
}