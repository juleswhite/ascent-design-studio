import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class Application {
	int floatNameIndex = 0;
	int intNameIndex = 0;
	ArrayList <String> intMemoryNames = new ArrayList();
	ArrayList <String> floatMemoryNames = new ArrayList();
	ArrayList<SchedulableTask> schedTasks_ = new ArrayList();
	ArrayList<String> intsReferenced_; 
	ArrayList<String> floatsReferenced_; 
	ArrayList<String> leftSides_ = new ArrayList();
	ArrayList<String> vars_ = new ArrayList();
	ArrayList<String> sharedFloats =new ArrayList();
	ArrayList<String> sharedInts =new ArrayList();
	int maxOffset;
	double shareProbability =100;
	int intNum_ =0;
	int floatNum_ =0;
	HashMap <String, ArrayList<String>> rates = new HashMap();
	String projectDirectory_;
	ArrayList<String> taskNames_ = new ArrayList();
	int [] start_ = new int [2];
	int [] last = new int [2];
	int totalVars =0;
	int sharedVars = 0;
	String appName_;
	int adder_ = 0;
	float multiplier;
	double probShared_;
	public Application(String projectDirectory, double minRate,String appName, int [] start, int adder, float multiply, double probShared){
		projectDirectory_ = projectDirectory;
		floatNameIndex = start[0];
		multiplier = multiply;
		intNameIndex = start[1];
		vars_ = new ArrayList();
		probShared_ = probShared;
		adder_ = adder;
		intsReferenced_ = new ArrayList();
		floatsReferenced_ = new ArrayList();
	//	System.out.println("float index = " +floatNameIndex); 
		//System.out.println("Int index = " + intNameIndex);
		appName_ = appName;
		defineApplicationClass(minRate, appName);
		last[0] = floatNameIndex;
		last[1] = intNameIndex;
		
		//defineApplicationClass(minRate,  appName);
		//defineApplicationClass(minRate, appName);
	}
	
	public void defineApplicationClass(double minRate,String appName){
		String startcontent ="";
		startcontent += "#include \"Application"+appName+".h\"\n";
		startcontent += "#include <iostream>\n";
		//content += "using namespace std;\n";
		
		//content += "class Application {\n";
		/*for ( int i = 0; i < intMemoryNames.size(); i++){
			content += "      int " + intMemoryNames.get(i) + ";\n";
		}*/
		System.out.println("Content in DAC = " + startcontent);
		ArrayList<String> taskContents = makeTaskMethods(appName, (float)probShared_);
		
		String taskContent = taskContents.get(0);
		makeSchedule(minRate);
		String partitionContent = makePartition("P1");
		//content += partitionContent;
		
		//content += writeSchedule();
		//content += "};";
		System.out.println(" intNum = " + intNum_);
		System.out.println(" floatNum = " + floatNum_);
	/*	for ( int i = 0; i <intsReferenced_.size()-1; i++){
			startcontent += "    int intVar" + i + ";\n";
		}
		for ( int i = 0; i < floatsReferenced_.size()-1; i++){
			startcontent += "    float floatVar" + i + ";\n";
		}*/
		String headContent = "";
		for(String var : vars_){
			headContent += var;
			//startcontent += var;//System.out.println("var = " +var);
		}
		startcontent += taskContent;
		String allHeader = taskContents.get(1)+headContent+partitionContent + taskContents.get(2);
		writeFile("Application"+appName+".h", allHeader,projectDirectory_);
		writeFile("Application"+appName+".cpp", startcontent, projectDirectory_);
	}
	
	
	

	
	
	public ArrayList<String> makeTaskMethods(String appName, float sp){
		String content = "";
		ArrayList<String> results = new ArrayList();
		String headerContent = "#ifndef APPLICATION"+appName+"_H\n";
		headerContent += "#define APPLICATION"+appName+"_H\n";
		headerContent += "#include <iostream>\n";
		headerContent += "class Application"+appName+"{\n";
		headerContent += "\tpublic:\n";
		 //float multiplier = (float) 3;
		 
		String headerContent3 = "";
		ArrayList<String> contents = new ArrayList();
		int taskSize = 5000;
		contents = DefineTask(appName, "01", "P1", taskSize*multiplier, sp);
		content += "\n"+contents.get(0);
		headerContent3 += "\t"+contents.get(1);
		//content += DefineTask(appName, "01", "P1", 4000, 0.05f);
		contents = DefineTask(appName, "02", "P1", taskSize*multiplier, sp);
		content += "\n\t"+contents.get(0);
		headerContent3 += "\t"+contents.get(1);
		/*contents = DefineTask(appName, "03", "P1", taskSize*multiplier, sp);
		content += "\n\t"+contents.get(0);
		headerContent3 += "\t"+contents.get(1);  
		contents = DefineTask(appName, "04", "P1", taskSize*multiplier, sp);
		content += "\n"+contents.get(0);
		headerContent3 += "\t"+contents.get(1);
		//content += DefineTask(appName, "01", "P1", 4000, 0.05f);
		contents = DefineTask(appName, "05", "P1", taskSize*multiplier, sp);
		content += "\n\t"+contents.get(0);
		headerContent3 += "\t"+contents.get(1);*//*
		contents = DefineTask(appName, "06", "P1", taskSize*multiplier, sp);
		content += "\n\t"+contents.get(0);
		headerContent3 += "\t"+contents.get(1);
		contents = DefineTask(appName, "07", "P1", taskSize*multiplier, sp);
		content += "\n"+contents.get(0);
		headerContent3 += "\t"+contents.get(1);
		//content += DefineTask(appName, "01", "P1", 4000, 0.05f);
		contents = DefineTask(appName, "08", "P1", taskSize*multiplier, sp);
		content += "\n\t"+contents.get(0);
		headerContent3 += "\t"+contents.get(1);
		contents = DefineTask(appName, "09", "P1", taskSize*multiplier, sp);
		content += "\n\t"+contents.get(0);
		headerContent3 += "\t"+contents.get(1);
		contents = DefineTask(appName, "10", "P1", taskSize*multiplier, sp);
		content += "\n"+contents.get(0);
		headerContent3 += "\t"+contents.get(1);*/
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
			//System.out.println("FloatNameIndex = " + floatNameIndex);
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
				//System.out.println("reference Name = " + referenceName);
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
	          System.out.println("CAUGHT EXCEPTION " +e);
	     }
	}
	
	public ArrayList<String> DefineTask(String ApplicationName, String TaskNumber, String PartitionName, float MemoryTotal, float SharedProbability)
	{
		ArrayList<String> results = new ArrayList();
		String headerContent = "";
		String taskName;
		String taskFileName;
		String partitionMemoryPrefix;
		intsReferenced_ = new ArrayList();
		floatsReferenced_ = new ArrayList();
		String referenceType;
		String referenceName;
		String content = "";
		taskName = "Task"+ApplicationName+TaskNumber;
		taskNames_.add(taskName);
		int maxShared = (int)(MemoryTotal*SharedProbability);
		System.out.println(" max shared = " + maxShared);
		int sharedCount = 0;
		headerContent += "void " + taskName +"(void);\n";
		taskFileName = taskName + ".txt";
		
		for (int i = 0; i < MemoryTotal; i++) {
			if(Math.random() < .5){
				referenceType = "float";
			}
			else{
				referenceType = "int";
			}
			if ( Math.random() < SharedProbability && sharedCount<maxShared){
				//System.out.println("less than shared Probability");
				totalVars++;
				sharedVars++;
				sharedCount++;
				//if()
				referenceName = makeRandomPartition(referenceType);
				if( referenceType =="float"){
					if(floatsReferenced_.isEmpty()){
						sharedFloats.add(referenceName);
					}
					else{
						referenceName = floatsReferenced_.get((int) (Math.random() *floatsReferenced_.size()));
						//System.out.println("Shared float from floats referenced is " + referenceName);
						sharedFloats.add(referenceName);
					}
				}
				else{	
					if(intsReferenced_.isEmpty()){
						sharedInts.add(referenceName);
					}
					else{
						referenceName = intsReferenced_.get((int) (Math.random() *intsReferenced_.size()));
						//System.out.println("Shared int from ints referenced is " + referenceName);
						sharedInts.add(referenceName);
					}
				}
				//System.out.println(" Probability.Adding "+referenceName);
			}
			else{
				//System.out.println("GREATER than shard Probability");
				referenceName = definePartitionMemory(referenceType);
				totalVars++;
				if( referenceType =="float"){
					while(sharedFloats.contains(referenceName) ){
						referenceName = makeRandomPartition(referenceType);
					}
				}
				else{
					while(sharedInts.contains(referenceName) ){
						referenceName = makeRandomPartition(referenceType);
					}
				}
				//System.out.println("GREATER than shard Probability. Adding "+referenceName);
			}
			//System.out.println("About to add Reference: " + referenceName);
			if( referenceType == "float"){
				
				
				floatsReferenced_.add(referenceName);
			}
			else{
				intsReferenced_.add(referenceName);
			}
		}
		/*
		 * Russell had some stuff in here to "save" all the taskVariables (to file it looks like) but I don't see
		 * it used. Leaving it out for now
		 */
		Collections.shuffle(intsReferenced_);/*this is a post probability thing. might need to uncomment*/
		Collections.shuffle(floatsReferenced_);
		content += "void Application"+ApplicationName+"::" + taskName +"(void)\n";
		content+= "{\n";
	/*	for ( int i = 0; i <intsReferenced_.size()-1; i++){
			content += "    int intVar" + i + ";\n";
		}
		for ( int i = 0; i < floatsReferenced_.size()-1; i++){
			content += "    float floatVar" + i + ";\n";
		}*/
		String memberContent2 = new String(content);
		String memberContent = new String(content);
		content = "";
		//System.out.pr
		int intNum = 0; 
		int floatNum = 0;
	//	System.out.println("Int Name Index = " + intNameIndex +" and floatnameindex = " + floatNameIndex);
		int count;
		String computation ="";
		while((intNum < intsReferenced_.size()-1) && (floatNum  < floatsReferenced_.size()-1)){
			count = 0;
			while(intNum < intsReferenced_.size()-1 && count < 10){
				//computation = computation + " + I." + intsReferenced_.get(intNum);
				computation = computation  + " + " + intsReferenced_.get(intNum);
				
				count++;
				intNum++;
			}
			if(computation != ""){
				char[] chars = computation.toCharArray();//
				chars[1] = '=';
				int taskVar = intNum+adder_;
				computation = new String(chars);
			//	String memberVariable = "intVar" + intnum;
				if(!memberContent.contains("intVar"+taskVar)){
					memberContent += "int intVar"+taskVar+";\n";
					if(!vars_.contains("\tint intVar"+taskVar+";\n")){
						//System.out.println(" adding");
						vars_.add("\tint intVar"+taskVar+";\n");
					}
				}
				content += "    intVar" +  taskVar+ computation + " ;\n" ;
				leftSides_.add("intVar"+taskVar);
			}
			computation ="";
			count = 0;
			while(floatNum < floatsReferenced_.size()-1 && count < 8){
				//computation = computation + " + F." + floatsReferenced_.get(floatNum);
				computation = computation  + " + " + floatsReferenced_.get(floatNum);
				
				count++;
				floatNum++;
			}
			if(computation != ""){
				char[] chars = computation.toCharArray();//
				chars[1] = '=';
				int taskVar = floatNum+adder_;
				computation = new String(chars);
				if(!memberContent.contains("floatVar"+taskVar)){
					memberContent += "float floatVar"+taskVar+";\n";
					if(!vars_.contains("\tfloat floatVar"+taskVar+";\n")){
						vars_.add("\tfloat floatVar"+taskVar+";\n");
					}
				}
				content += "    floatVar" +  taskVar+ computation + " ;\n" ;
				leftSides_.add("floatVar"+taskVar);
			}
			
		}
		intNum_=intNum;
		floatNum_ = floatNum;
		content += "}\n";
		//memberContent_ = memberContent;
	    content = memberContent2 + content;
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
		double probShared = 0.5;
		ArrayList<SchedulableTask> allTasks= new ArrayList();
		//ArrayList<SchedulableTask> unoptSched = unopt
		double start = 0.5;
		while(probShared <=0.54){
			DecimalFormat df = new DecimalFormat("#.##");
			double d = new Double(df.format(probShared)).doubleValue();
			String destinationDirectory = "/Users/Brian/SEI-TM10-2tpa-"+d;
			//PartitionNames pn = new PartitionNames("/Users/briandougherty",8.0);
			int [] go = {2000000,2000000};//multiplied go by 100
			float multi = (float) 10.0;
			Application pn1 = new Application(destinationDirectory, 8.0, "redApp",go,0,multi, probShared);
			pn1.makeSchedule(8.0);
			 
			
			//pn1.writeSchedule();
			System.out.println("Last[0] " +pn1.getLast()[0] +" and the other "+ pn1.getLast()[1]);
			Application pn2 = new Application(destinationDirectory, 8.0, "blueApp", pn1.getLast(),100000000,multi,probShared);
			/*##Multiplied all starters by 100 */
			pn2.makeSchedule(8.0);
			
			Application pn3 = new Application(destinationDirectory, 8.0, "yellowApp", pn2.getLast(),200000000,multi, probShared);
			pn3.makeSchedule(8.0);
			ArrayList<String> appNames = new ArrayList();
			//pn2.writeSchedule();
			System.out.println("Last[0] " +pn2.getLast()[0] +" and the other "+ pn3.getLast()[1]);
			Application pn4 = new Application(destinationDirectory, 8.0, "greenApp", pn3.getLast(),300000000, multi, probShared);
			pn4.makeSchedule(8.0); 
			
			//pn2.writeSchedule();
			System.out.println("Last[0] " +pn4.getLast()[0] +" and the other "+ pn4.getLast()[1]);
			Application pn5 = new Application(destinationDirectory, 8.0, "purpleApp", pn4.getLast(),400000000,multi,probShared);
			pn5.makeSchedule(8.0);
			if(probShared == start){
				allTasks.addAll(pn1.getSchedTasks_());
				allTasks.addAll(pn2.getSchedTasks_());
				allTasks.addAll(pn3.getSchedTasks_());
				allTasks.addAll(pn4.getSchedTasks_());
				allTasks.addAll(pn5.getSchedTasks_());
			}
			/*
			//pn2.writeSchedule();
			System.out.println("Last[0] " +pn5.getLast()[0] +" and the other "+ pn5.getLast()[1]);
			PartitionNames pn6 = new PartitionNames(destinationDirectory, 8.0, "orangeApp", pn5.getLast(),500000);
			pn6.makeSchedule(8.0);
			allTasks.addAll(pn6.getSchedTasks_());
			//pn2.writeSchedule();
			System.out.println("Last[0] " +pn6.getLast()[0] +" and the other "+ pn6.getLast()[1]);
			PartitionNames pn7 = new PartitionNames(destinationDirectory, 8.0, "brownApp", pn6.getLast(),600000);
			pn7.makeSchedule(8.0);
			allTasks.addAll(pn7.getSchedTasks_());
			//pn2.writeSchedule();
			System.out.println("Last[0] " +pn7.getLast()[0] +" and the other "+ pn7.getLast()[1]);
			PartitionNames pn8 = new PartitionNames(destinationDirectory, 8.0, "blackApp", pn7.getLast(),700000);
			pn8.makeSchedule(8.0);
			allTasks.addAll(pn8.getSchedTasks_());
			//pn2.writeSchedule();
			System.out.println("Last[0] " +pn8.getLast()[0] +" and the other "+ pn8.getLast()[1]);
			PartitionNames pn9 = new PartitionNames(destinationDirectory, 8.0, "whiteApp", pn8.getLast(),800000);
			pn9.makeSchedule(8.0);
			allTasks.addAll(pn9.getSchedTasks_());
			//pn2.writeSchedule();
			System.out.println("Last[0] " +pn9.getLast()[0] +" and the other "+ pn9.getLast()[1]);
			PartitionNames pn10 = new PartitionNames(destinationDirectory, 8.0, "pinkApp", pn9.getLast(),900000);
			pn9.makeSchedule(8.0);
			allTasks.addAll(pn10.getSchedTasks_());*/
			
			
			appNames.add("redApp");
			appNames.add("blueApp");
			appNames.add("yellowApp");
			appNames.add("greenApp");
			appNames.add("purpleApp");
		/*	appNames.add("orangeApp");
			appNames.add("brownApp");
			appNames.add("blackApp");
			appNames.add("whiteApp");
			appNames.add("pinkApp");
			*/
			//appNames.add("redApp");
			//appNames.add("blueApp");
			//appNames.add("yellowApp");
			
			
			ExecutionMaker em = new ExecutionMaker(appNames);
			ArrayList<SchedulableTask> st ;//= new ArrayList();
			st = new ArrayList();
			for(SchedulableTask schedTask : allTasks){
				SchedulableTask s = new SchedulableTask(schedTask.getTaskName_(), schedTask.getStride_(), schedTask.getRate_(), schedTask.getAppName_());
				st.add(s);
			}
			System.out.println("BEFORE st == allTasks: "+st.equals(allTasks));
		/*	
			*/em.writeSchedule(allTasks,destinationDirectory, true, "optimizedEx","");
			System.out.println("allTasks size " + allTasks.size());
			//allTasks = new ArrayList();
			System.out.println("DURING st == allTasks: "+st.equals(allTasks));
			em.writeSchedule(st,destinationDirectory, false,"unoptimizedEx", "");
			System.out.println("AFTER st == allTasks: "+st.equals(allTasks));
			System.out.println(" St size = " + st.size());
			em.writeSchedule(st,destinationDirectory, false,"BextEx", "best");
			
			em.writeSchedule(st,destinationDirectory, false,"WorstEx", "worst");
			System.out.println(" shshared vars " + pn1.getSharedVars()+" and total vars = "+ pn1.getTotalVars()+" shared ratio = " +(float)pn1.getSharedVars()/(float)pn3.getTotalVars());
			
			System.out.println(" shshared vars " + pn2.getSharedVars()+" and total vars = "+ pn2.getTotalVars()+" shared ratio = " +(float)pn2.getSharedVars()/(float)pn3.getTotalVars());
			
			System.out.println(" shshared vars " + pn3.getSharedVars()+" and total vars = "+ pn3.getTotalVars()+" shared ratio = " +(float)pn3.getSharedVars()/(float)pn3.getTotalVars());
		//	em.writeSchedule(allTasks,"/Users/briandougherty/brian2/optimized/", true);
		//	em.writeSchedule(allTasks,"/Users/briandougherty/brian2/optimized/", false);
			//pn3.writeSchedule(allTasks);
			probShared +=.05;
		}
		

		
		//pn.makePartition("P1");*/
	}

	public int getTotalVars() {
		return totalVars;
	}

	public void setTotalVars(int totalVars) {
		this.totalVars = totalVars;
	}

	public int getSharedVars() {
		return sharedVars;
	}

	public void setSharedVars(int sharedVars) {
		this.sharedVars = sharedVars;
	}
}