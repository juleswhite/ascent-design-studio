import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class ExecutionMaker {
	String projectDirectory;
	ArrayList<String> appNames_;
	
	public ExecutionMaker(ArrayList<String> appNames){
		appNames_ = appNames;
	}
	

	public ArrayList writeSchedule(ArrayList<SchedulableTask> sched, String pd, boolean optimized,String scheduleName, String bw){
    	String outputSchedule = "#include <sched.h>\n";
    	outputSchedule += "#include <iostream>\n";
    	outputSchedule += "#include <fstream>\n";
    	outputSchedule += "#include <time.h>\n";
    	outputSchedule += "#include <ctime>\n";
    	outputSchedule += "#include <sys/time.h>\n";
    	outputSchedule += "#include <map>\n";
    	outputSchedule += "#include \"rdtsc.h\"\n";
    	outputSchedule += "#include \"Execute.h\"\n";
    	//outputSchedule +="#include \"CacheTrasher.h\"\n";
    	for(String appName : appNames_){
    		outputSchedule += "#include \"Application"+appName+".h\"\n";
    	}
    	outputSchedule +="using namespace std;\n";
		outputSchedule += "void Execute::executeTasks(int executions){\n";
		outputSchedule += "\t struct sched_param sched_p;\n";
		outputSchedule += "\t long long unsigned int startClockTicks, finishClockTicks, elapseClockTicks, elapseClockNs;\n";
    	outputSchedule +="\t clock_t startClock,finishClock;\n";
		outputSchedule +="\t double timeCount;\n";
		outputSchedule +="\t startClock = clock();\n\t";
		outputSchedule +="std::map<string, std::map<int, long> > timeMap;\n\t";
		int overlaps =0;
		for(String appName : appNames_){
			outputSchedule += "Application"+appName+" " +appName+";\n\t";
		}
    	
    	Scheduler scheduler = new Scheduler(sched);
    	scheduler.clearSchedule();
    	System.out.println("schedduler tasks in EM = " + scheduler.getTasks_().size());
    	projectDirectory = pd;
    	int numTasks =0;
    	ArrayList<SchedulableTask> schedulableTasks = new ArrayList();
    	schedulableTasks = scheduler.scheduleTasks(optimized);
    	System.out.println(" About to enter the optimized segment of EM");
    	if(optimized&& bw ==""){
	    	ScheduleOptimizer so = new ScheduleOptimizer(schedulableTasks,8);
			System.out.println(" Optimized Schedule length = " + schedulableTasks.size());
			so.printSchedule();
			so.optimize();
		//	so.removeDoubles(); //I don't think this should be here, but haven't tested it since.
			schedulableTasks = so.getFinalTasks();
			System.out.println("After final tasks " + schedulableTasks.size());
			//so.printSchedule();
    	}
    	if( bw == "best"){
    		HashMap<String,ArrayList<SchedulableTask>> bestTasks = new HashMap();
 
    		for(SchedulableTask st : schedulableTasks){
    			String appName = st.getAppName_();
    			ArrayList<SchedulableTask> currentTaskList;
    			if(!bestTasks.containsKey(appName)){
    				currentTaskList = new ArrayList();
    			}
    			else{
    				currentTaskList = bestTasks.get(appName);
    			}
    			currentTaskList.add(st);
    			bestTasks.put(appName,currentTaskList);
    			
    		}
    		schedulableTasks.clear();
    		Set<String> s = bestTasks.keySet();
			Iterator it = s.iterator();
			while(it.hasNext()){
				String appName = (String) it.next();
				ArrayList<SchedulableTask> theseTasks = bestTasks.get(appName);
				for(SchedulableTask st: theseTasks){
					schedulableTasks.add(st);
				}
			}
    	}
    	if(bw =="worst"){
    		
    		HashMap<String,ArrayList<SchedulableTask>> bestTasks = new HashMap();
    		 
    		for(SchedulableTask st : schedulableTasks){
    			String appName = st.getAppName_();
    			ArrayList<SchedulableTask> currentTaskList;
    			if(!bestTasks.containsKey(appName)){
    				currentTaskList = new ArrayList();
    			}
    			else{
    				currentTaskList = bestTasks.get(appName);
    			}
    			currentTaskList.add(st);
    			bestTasks.put(appName,currentTaskList);
    			
    		}
    		int totalTasks = schedulableTasks.size();
    		schedulableTasks.clear();
    		ArrayList<ArrayList<SchedulableTask>> tasks = new ArrayList();
	    	Set<String> s = bestTasks.keySet();
			Iterator it = s.iterator();
			while(it.hasNext()){
				String appName = (String) it.next();
				tasks.add(bestTasks.get(appName));
			}
			int currentIndex = 0;
			while(schedulableTasks.size() < totalTasks){
				
				ArrayList<SchedulableTask> currentTasks = tasks.get(currentIndex);
				if(currentTasks.size()>0){
					schedulableTasks.add(currentTasks.get(0));
					currentTasks.remove(0);
					tasks.remove(currentIndex);
					tasks.add(currentIndex, currentTasks);
				}
				currentIndex++;
				if(currentIndex == tasks.size()){
					currentIndex =0;
					
				}
				
 			}
			
			
			/*
			 * Now we have to take care of the case in which there's extra's at the end that are of the same app
			 * 
			 */
			for(int i = 1; i < schedulableTasks.size(); i++){
				SchedulableTask st1 = schedulableTasks.get(i-1);
				SchedulableTask st2 = schedulableTasks.get(i);
				if(st1.getAppName_() == st2.getAppName_()){
					schedulableTasks.remove(i);
					for(int j =1; j<schedulableTasks.size(); j++){
						SchedulableTask left = schedulableTasks.get(j-1);
						SchedulableTask right = schedulableTasks.get(j);
						if(st2.getAppName_() != right.getAppName_() && st2.getAppName_() != left.getAppName_()){
							schedulableTasks.add(j, st2);
							j = schedulableTasks.size();
						}
					}
				}
			}
			
    		
    	}
    	for(int i = 1; i < schedulableTasks.size(); i++){
			if( schedulableTasks.get(i-1).getAppName_() == schedulableTasks.get(i).getAppName_()){
				overlaps++;
			}
		}
    	outputSchedule +="int i = 0; \n \t " +
		"ofstream myfile;\n\t"+
		"myfile.open(\"output"+scheduleName+"-Overlaps:+"+overlaps+"\");\n\t"+
		"ofstream excelOutput;\n\t"+
		"excelOutput.open(\"excelOutput-"+scheduleName+".txt\");\n\t"+
		//"CacheTrasher c;\n\t"+
		//"c.CacheFlusherSetup(12000000,512);\n\t" +
		"int totalExec = 5; \n\t"+
		"clock_t midStartClock, midFinishClock;\n\t"+
		"long long unsigned int midStartClockTicks, midFinishClockTicks, midElapseClockTicks, midElapseClockNs;\n\t"+
		"calculate_ticks_per_usec();\n\t"+
		"sched_p.sched_priority = 10;\n\t"+
		"if (sched_setscheduler(getpid(), SCHED_FIFO, &sched_p) <0){\n\t"+
		"  perror(\"Could not changed to fixed priority. Use \\\"sudo <cmd>\\\"\");\n\t}\n\t"+
		"while(totalExec >0){\n\t\t"+
		"startClockTicks=rdtsc();\n\t\t\t"+
		"startClock = rdtsc();\n\t\t"+
		"i =0;\n\t\t"+
		"while(i < executions){\n\t\t\t";
    	System.out.println("schedulableTasks size = " + schedulableTasks.size());
    	for(SchedulableTask st : schedulableTasks ){
    		numTasks++;
    		//outputSchedule += "midStartClock = clock();\n\t\t\t";
    		outputSchedule += "midStartClockTicks = rdtsc();\n\t\t\t";
    		
    		//outputSchedule += "startClock= clock();\n\t\t\t";
    		outputSchedule += st.getAppName_()+"."+st.getTaskName_()+"();//rate"+st.getRate_()+"\n\n\t\t\t";
    		outputSchedule += "midFinishClockTicks=rdtsc();\n\t\t\t";
    		outputSchedule += "midElapseClockTicks = midFinishClockTicks - midStartClockTicks;\n\t\t\t";
    		outputSchedule += "midElapseClockNs = ticks2ns(midElapseClockTicks);\n\t\t\t";
    		//outputSchedule += "midFinishClock = clock();\n\t\t\t"+
			outputSchedule += "timeMap[\""+st.getAppName_()+"."+st.getTaskName_()+"-"+st.getRate_()+"\"][i] = midElapseClockNs;\n\t\t\t";// midFinishClock-midStartClock;\n\t\t\t";
			
    		
    		//outputSchedule +="c.CacheFlush();\n\t\t\t";
    	}
    	scheduler.printTotal();
    	System.out.println("num tasks = "+numTasks);
    	outputSchedule += "\n\t\t\ti++;\n\t\t}\n" +
    			"\t\t finishClock = rdtsc();\n"+
    			"\t\t myfile<<(finishClock-startClock)/1000<<std::endl;\n\t\t"+
    			"totalExec--;}\n\t"+
    	"std::map<std::string, std::map<int,long> >::iterator iter;\n\t" +
    	"std::map<int,long>::iterator insideIter;\n\t"+
    	"std::string taskName;\n\t"+
    	"excelOutput<<\"Task Name, Average Exe Time, Min Exe Time, Max Exe Time\";\n\t"+
    	"excelOutput<<std::endl;\n\t"+
    	"for (iter = timeMap.begin(); iter != timeMap.end(); iter++) {\n\t\t"+
    	"double totalTime = 0;\n\t\t"+
    	"int maxTime = 0;\n\t\t"+
    	"int minTime = 5000000;\n\t\t"+
    	"int currentTime;\n\t\t"+
    	"double averageTime;\n\t\t"+
    	"taskName = iter->first;\n\t\t"+
    	"excelOutput<<taskName;\n\t\t"+
    	"for (insideIter = iter->second.begin(); insideIter != iter->second.end(); insideIter++) {\n\t\t\t"+
    	"currentTime = insideIter->second;\n\t\t\t"+
    			//excelOutput<<",";
    			//excelOutput<<insideIter->second;
    	"if(currentTime > maxTime){\n\t\t\t\t"+
    	"maxTime = currentTime;\n\t\t\t"+
    	"}\n\t\t\t"+
    	"if(currentTime < minTime){\n\t\t\t\t"+
    	"minTime = currentTime;\n\t\t\t"+
    	"}\n\t\t\t"+
    	"totalTime = totalTime + insideIter->second;\n\t\t"+
    	"}\n\t\t"+
    	"averageTime = totalTime/timeMap[taskName].size();\n\t\t"+
    	"excelOutput<<\",\";\n\t\t"+
    	"excelOutput<<averageTime;\n\t\t"+
    	"excelOutput<<\",\";\n\t\t"+
    	"excelOutput<<minTime;\n\t\t"+
    	"excelOutput<<\",\";\n\t\t"+
    	"excelOutput<<maxTime;\n\t\t"+
    	"excelOutput<<std::endl;\n\t"+
    	"}\n\t"+
    	"excelOutput.close();\n\t"+
    	"myfile.close();\n\t"+
    	"\n}\n";
    	Application.writeFile(scheduleName+".cpp", outputSchedule, projectDirectory);
    	String executeHeader = "#ifndef EXECUTE_H\n"+
    	"#define EXECUTE_H\n"+
    	"#include <iostream>\n"+
    	"class Execute{\n\t"+
    	"public:\n\t"+
    	"void executeTasks(int);\n"+
    	"};\n"+
    	"#endif";
    	Application.writeFile("Execute.h", executeHeader, projectDirectory);
    	writeMakeFile(scheduleName);
    	try {
			Copy("rdtsc.h", projectDirectory+"/rdtsc.h");
			Copy("Launcher.cpp", projectDirectory+"/Launcher.cpp");
			Copy("HistoryMaker.py",projectDirectory+"/HistoryMaker.py");
			Copy("ResultsFetcher.py",projectDirectory+"/ResultsFetcher.py");
			Copy("exeExtender.py",projectDirectory+"/exeExtender.py");
			Copy("VTune-RF.py",projectDirectory+"/VTune-RF.py");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//return outputSchedule;
		
    	return schedulableTasks;
    }
	/* From http://java.sun.com/docs/books/tutorial/index.html */
	/*
	 * Copyright (c) 2006 Sun Microsystems, Inc. All Rights Reserved.
	 *
	 * Redistribution and use in source and binary forms, with or without
	 * modification, are permitted provided that the following conditions are met:
	 *
	 * -Redistribution of source code must retain the above copyright notice, this
	 *  list of conditions and the following disclaimer.
	 *
	 * -Redistribution in binary form must reproduce the above copyright notice,
	 *  this list of conditions and the following disclaimer in the documentation
	 *  and/or other materials provided with the distribution.
	 *
	 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
	 * be used to endorse or promote products derived from this software without
	 * specific prior written permission.
	 *
	 * This software is provided "AS IS," without a warranty of any kind. ALL
	 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
	 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
	 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN")
	 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
	 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
	 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
	 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
	 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
	 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE,
	 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
	 *
	 * You acknowledge that this software is not designed, licensed or intended
	 * for use in the design, construction, operation or maintenance of any
	 * nuclear facility.
	 */
	public void Copy(String file1, String file2) throws IOException {
		    File inputFile = new File(file1);
		    File outputFile = new File(file2);
		    FileReader in = new FileReader(inputFile);
		    FileWriter out = new FileWriter(outputFile);
		    int bte;
		    while ((bte = in.read()) != -1){
		      out.write(bte);
		    }
		    in.close();
		    out.close();
	}
	
	private void writeMakeFile(String scheduleName){
		String makeContent = "";
		makeContent = scheduleName +".exe: " + scheduleName + ".o"  + " Launcher.o ";
		for(String appName : appNames_){
    		makeContent += "Application"+appName+".o "; 
    	}
		makeContent += "\n\t\t g++ " + scheduleName + ".o"  + " Launcher.o ";
		for(String appName : appNames_){
    		makeContent += "Application"+appName+".o "; 
    	}
		makeContent += " -o "+scheduleName + "\n\n";
		for(String appName : appNames_){
    		makeContent += "Application"+appName+".o: " + "Application"+appName+".h\n\t\t"+"g++ -c " + "Application"+appName+".cpp\n\n"; 
    	}
		makeContent += "\n"+scheduleName+".o:" + " Execute.h\n\t\t" + "g++ -c "+scheduleName +".cpp" + " -o "+scheduleName+".o\n\n";
		makeContent += "\nLauncher.o:" + " Launcher.cpp Execute.h\n\t\t" + "g++ -c Launcher.cpp";
		
		Application.writeFile("make"+scheduleName, makeContent, projectDirectory);
	}
		
}
