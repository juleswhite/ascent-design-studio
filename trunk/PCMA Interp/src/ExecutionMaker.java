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
    	String outputSchedule = "#include <iostream>\n";
    	outputSchedule += "#include <fstream>\n";
    	outputSchedule += "#include <time.h>\n";
    	outputSchedule += "#include <ctime>\n";
    	outputSchedule += "#include <sys/time.h>\n";
    	outputSchedule += "#include \"Execute.h\"\n";
    	outputSchedule +="#include \"CacheTrasher.h\"\n";
    	for(String appName : appNames_){
    		outputSchedule += "#include \"Application"+appName+".h\"\n";
    	}
    	outputSchedule +="using namespace std;\n";
		outputSchedule += "void Execute::executeTasks(int executions){\n";
    	
    	outputSchedule +="\t clock_t startClock,finishClock;\n";
		outputSchedule +="\t double timeCount;\n";
		outputSchedule +="\t startClock = clock();\n\t";
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
    	if(optimized&& bw ==""){
	    	ScheduleOptimizer so = new ScheduleOptimizer(schedulableTasks,8);
			System.out.println(" Optimized Schedule length = " + schedulableTasks.size());
			so.printSchedule();
			so.optimize();
			so.removeDoubles();
			schedulableTasks = so.getAllFinalTasks();
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
		//"CacheTrasher c;\n\t"+
		//"c.CacheFlusherSetup(12000000,512);\n\t" +
		"int totalExec = 50; \n\t"+
		"while(totalExec >0){\n\t\t"+
		"startClock = clock();\n\t\t"+
		"i =0;\n\t\t"+
		"while(i < executions){\n\t\t\t";
    	System.out.println("schedulableTasks size = " + schedulableTasks.size());
    	for(SchedulableTask st : schedulableTasks ){
    		numTasks++;
    		outputSchedule += st.getAppName_()+"."+st.getTaskName_()+"();//rate"+st.getRate_()+"\n\n\t\t\t";
    		
    		//outputSchedule +="c.CacheFlush();\n\t\t\t";
    	}
    	scheduler.printTotal();
    	System.out.println("num tasks = "+numTasks);
    	outputSchedule += "\n\t\t\ti++;\n\t\t}\n" +
    			"\t\t finishClock = clock();\n"+
    			"\t\t myfile<<(finishClock-startClock)/1000<<std::endl;\n\t\t"+
    			"totalExec--;}\n\t"+
    			"myfile.close();\n\t"+
    			"\n}\n";
    	PartitionNames.writeFile(scheduleName, outputSchedule, projectDirectory);
    	String executeHeader = "#ifndef EXECUTE_H\n"+
    	"#define EXECUTE_H\n"+
    	"#include <iostream>\n"+
    	"class Execute{\n\t"+
    	"public:\n\t"+
    	"void executeTasks(int);\n"+
    	"};\n"+
    	"#endif";
    	PartitionNames.writeFile("Execute.h", executeHeader, projectDirectory);
    	//return outputSchedule;
    	return schedulableTasks;
    }
}
