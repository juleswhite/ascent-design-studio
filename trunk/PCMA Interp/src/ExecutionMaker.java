import java.util.ArrayList;


public class ExecutionMaker {
	String projectDirectory;
	ArrayList<String> appNames_;
	
	public ExecutionMaker(ArrayList<String> appNames){
		appNames_ = appNames;
	}
	

	public String writeSchedule(ArrayList<SchedulableTask> sched, String pd, boolean optimized){
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
		for(String appName : appNames_){
			outputSchedule += "Application"+appName+" " +appName+";\n\t";
		}
    	outputSchedule +="int i = 0; \n \t " +
    			"ofstream myfile;\n\t"+
    			"myfile.open(\"output.txt\");\n\t"+
    			"CacheTrasher c;\n\t"+
    			"c.CacheFlusherSetup(12000000,512);\n\t" +
    			"int totalExec = 50; \n\t"+
    			"while(totalExec >0){\n\t\t"+
    			"startClock = clock();\n\t\t"+
    			"i =0;\n\t\t"+
    			"while(i < executions){\n\t\t\t";
    	Scheduler scheduler = new Scheduler(sched);
    	scheduler.clearSchedule();
    	System.out.println("schedduler tasks in EM = " + scheduler.getTasks_().size());
    	projectDirectory = pd;
    	int numTasks =0;
    	ArrayList<SchedulableTask> schedulableTasks = new ArrayList();
    	schedulableTasks = scheduler.scheduleTasks(optimized);
    	if(optimized){
	    	ScheduleOptimizer so = new ScheduleOptimizer(schedulableTasks,8);
			System.out.println(" Optimized Schedule length = " + schedulableTasks.size());
			so.printSchedule();
			so.optimize();
			so.removeDoubles();
			schedulableTasks = so.getAllFinalTasks();
			//so.printSchedule();
    	}
    	System.out.println("schedulableTasks size = " + schedulableTasks.size());
    	for(SchedulableTask st : schedulableTasks ){
    		numTasks++;
    		outputSchedule += st.getAppName_()+"."+st.getTaskName_()+"();//rate"+st.getRate_()+"\n\t\t\t";
    		
    		outputSchedule +="c.CacheFlush();\n\t\t\t";
    	}
    	scheduler.printTotal();
    	System.out.println("num tasks = "+numTasks);
    	outputSchedule += "\n\t\t\ti++;\n\t\t}\n" +
    			"\t\t finishClock = clock();\n"+
    			"\t\t myfile<<(finishClock-startClock)/1000<<std::endl;\n\t\t"+
    			"totalExec--;}\n\t"+
    			"myfile.close();\n\t"+
    			"\n}\n";
    	PartitionNames.writeFile("Execute.cpp", outputSchedule, projectDirectory);
    	String executeHeader = "#ifndef EXECUTE_H\n"+
    	"#define EXECUTE_H\n"+
    	"#include <iostream>\n"+
    	"class Execute{\n\t"+
    	"public:\n\t"+
    	"void executeTasks(int);\n"+
    	"};\n"+
    	"#endif";
    	PartitionNames.writeFile("Execute.h", executeHeader, projectDirectory);
    	return outputSchedule;
    }
}
