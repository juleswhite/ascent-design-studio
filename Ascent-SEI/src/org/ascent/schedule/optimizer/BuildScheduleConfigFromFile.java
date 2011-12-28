package org.ascent.schedule.optimizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BuildScheduleConfigFromFile {

	protected String directory_;
	private ArrayList<Application> applications_;
	protected HashMap<String,SchedulableTask> tasks_;
	public BuildScheduleConfigFromFile(String d){
		
		tasks_ = new HashMap();
		applications_ = new ArrayList();
		directory_ = d;
		
	}
	
	public ArrayList<String> readFileLines(String filePath) throws IOException{
		ArrayList<String> stringArray = new ArrayList();
		File f = new File(filePath);
	     if (!f.exists() && f.length() < 0) {
	             System.out.println("The specified file does not exist");
	     } 
	     else {
	             FileReader fr = new FileReader(f);
	             BufferedReader reader = new BufferedReader(fr);
	             String st = "";
	             while ((st = reader.readLine()) != null) {
	                     stringArray.add(st);
	             }
	            
	     }
		return stringArray;
	}
	
	public ArrayList<String> getFileNames(String directory){
		File folder = new File(directory);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> fileNames = new ArrayList();
		for(File file: listOfFiles){
			fileNames.add(file.getName());
		}
		return fileNames; 
		
	}
	
	public int[] getDataWrittenRead(ArrayList<String> appFileNames, SchedulableTask st){
		int totalDataWritten = 0; 
		int totalDataRead =0;
		int [] returnArray = new int[2];
		String taskName = st.getLabel();
		String appName  = st.getApplication_().getName_();
		ArrayList<String> applicationFileLines = new ArrayList();
		for(String appFileName : appFileNames){
			if( appFileName.contains(appName)){
				try {
					for(String line : readFileLines(directory_+"/"+appFileName)){
						applicationFileLines.add(line);
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		//System.out.println("ApplicationFileLines = " + applicationFileLines);
		boolean readingTask = false;
		ArrayList<String> taskLines = new ArrayList();
		for( String line : applicationFileLines){
			if( line.contains(taskName)){
				readingTask =true;
			}
			else if ( readingTask == true && line.contains("}")){
				readingTask = false;
				break;
			}
			
			if(readingTask == true){
				//System.out.println("adding " + line);
				taskLines.add(line);
			}
			
		}
		
	    //System.out.println("tasklines " + taskLines);
		for(String line : taskLines){
			if(line.contains("=")){
				totalDataWritten++;
			}
			if( line.contains("+")){
				totalDataRead = totalDataRead+line.split("\\+").length;
			}
			
		}
			
		returnArray[0] = totalDataWritten;
		returnArray[1] = totalDataRead;
		
		return returnArray;
		
		
	}
	
	public void makeTasksAndApps(ArrayList<String> appFileNames){
	
		int taskCount =0;
		int appCount = 0;
		ArrayList<Application> apps = new ArrayList();
		ArrayList<String> appNameString = new ArrayList();
		HashMap<String,Application> appMap = new HashMap(); 
		for(String afn : appFileNames){
			ArrayList<String> appFileLines = new ArrayList();
			System.out.println(" AppFileName = " + afn);
			String appName = afn.trim().split("\\.")[0];
			appName = appName.split("Application")[1];
			System.out.println(" App name = " + appName);
			if(!appNameString.contains(appName)){
				Application app = new Application (appCount, appName);
				app.setSharedPercentage(calculateSharedPercentage(app));
				applications_.add(app);
				apps.add(app);
				appMap.put(appName, app);
				appCount++;
			}
			
			
			try {
				appFileLines = readFileLines(directory_+"/"+afn);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(appFileLines.size() >0 ){
				for(String afl : appFileLines){
					if(afl.contains("Task")){
						//System.out.println("Line including \"Task\": "+ afl);
						String taskLabel = afl.split("\\:\\:")[1].split("\\(")[0];
						SchedulableTask st = new SchedulableTask(taskCount, taskLabel, new int []{0,0,0},appMap.get(appName),-1,-1);
						tasks_.put(taskLabel, st);
						taskCount++;
						
						//System.out.println(taskLabel);
					}
				}
			}
		}
		
		
	}
	
	private double calculateSharedPercentage(Application app) {
		// TODO Auto-generated method stub
		return .33;
	}

	public void addRates(double baseRate, String baseScheduleFileName){
		try {
			ArrayList<String> fileLines = readFileLines(directory_ + "/" + baseScheduleFileName);
			
			for(String fileLine: fileLines){
				if( fileLine.contains("//TASK_TAG")){
					String taskLabel = fileLine.split("\\(")[0].split("\\.")[0];
					String rate = fileLine.split("\\/N\\/")[1];
					Double dRate = Double.valueOf(rate);
					System.out.println(" Looking up tag "+ taskLabel +" to add "+ dRate);
					if(tasks_.containsKey(taskLabel)){
						System.out.println("Tasks " + taskLabel +" FOUND");
						SchedulableTask st = tasks_.get(taskLabel);
						PeriodicTask pt = new PeriodicTask(st);
						pt.setRate_(baseRate/dRate);
						tasks_.put(taskLabel, pt);
					}
					
					
				}
				else{
				//	System.out.println("NO TASK TAG FOUND IN FILE");
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.print(" Error reading file" + e);
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<Application> getApplications_() {
		return applications_;
	}

	public void setApplications_(ArrayList<Application> applications) {
		this.applications_ = applications;
	}

	public HashMap<String, SchedulableTask> getTasks_() {
		return tasks_;
	}

	public void setTasks_(HashMap<String, SchedulableTask> tasks) {
		this.tasks_ = tasks;
	}

	public static void main( String args[]){
		
		String directory = "/Users/brian/home/brian/Old/SEI-WithVTune";
		BuildPeriodicScheduleConfigFromFile builder = new BuildPeriodicScheduleConfigFromFile(directory);
		
		String fileToRead = "/Users/brian/home/brian/Old/SEI-WithVTune/TrialRun1.cpp";
		ArrayList<String> linesFromFile = new ArrayList();
		try {
			linesFromFile = builder.readFileLines(fileToRead);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<String> fileNames = builder.getFileNames(directory);
		ArrayList<String> appFileNames = new ArrayList();
		for(String fileName: fileNames){
			//System.out.println("Filename = "+fileName);
			if(fileName.contains("Application") && fileName.endsWith("cpp")){
				appFileNames.add(fileName.trim());
			}
		}
		
	/*	for(String appFileName : appFileNames){
			//System.out.println(appFileName);
		}*/
		int [] dataStats = new int [2];
		//dataStats = builder.getDataWrittenRead(appFileNames, task2);
		//task2.setDataRead_(dataStats[1]);
		//task2.setDataWritten_(dataStats[0]);
		//System.out.println(" Task 2 reads " + task2.getDataRead_() +"vars and writes "+task2.getDataWritten_() + " vars" );
		System.out.println("appFileNames = " + appFileNames);
		builder.makeTasksAndApps(appFileNames);
		HashMap<String, SchedulableTask> bTasks = builder.getTasks_();
		for(String  stName: bTasks.keySet()){
			SchedulableTask st = bTasks.get(stName);
			dataStats = builder.getDataWrittenRead(appFileNames, st);
			st.setDataRead_(dataStats[1]);
			st.setDataWritten_(dataStats[0]);
		}
		
		System.out.println("Tasks_ = " + builder.getTasks_());
		System.out.println("Applications_= " + builder.getApplications_());		
		
		int index =0;
		SchedulableTask [] stArray = new SchedulableTask[builder.getTasks_().size()];
		Application [] appArray= new Application[builder.getApplications_().size()];
		
		for(String taskName : builder.getTasks_().keySet()){
			SchedulableTask st = builder.getTasks_().get(taskName);
			stArray[index] = st;
			index ++;
			
		}
		index =0;
		for(Application app : builder.getApplications_()){
			appArray[index]= app;
			index++;
		}
		
		
		builder.addRates(1, "TrialRun2-Tester.cpp");
		HashMap<String, PeriodicTask> pTasks = builder.getPeriodicTasks_();
		PeriodicTask [] ptArray= new PeriodicTask[builder.getTasks_().size()];
		index =0;
		for(String pTaskname : pTasks.keySet()){
			ptArray[index]= pTasks.get(pTaskname);
			index++;
		}
		
		
		
		
		for(String  stName: pTasks.keySet()){
			System.out.println("About to attempt to cast " + pTasks.get(stName));
			PeriodicTask st = (PeriodicTask) pTasks.get(stName);
			System.out.println(" The periodic task rate is " + st.getRate_());
		}
		ScheduleConfig builtScheduleConfig = new ScheduleConfig(stArray, appArray);
		for(int i = 0; i < ptArray.length; i++ ){
			
			System.out.println( "ptArray[i] = " + ptArray[i]);
		}
		PeriodicScheduleConfig builtPeriodicScheduleConfig = new PeriodicScheduleConfig(ptArray, appArray);
		System.out.println(" Schedule config tasks = " + builtScheduleConfig.getTasks_().length);
		
		CAMSSchedulePlanner cms = new CAMSSchedulePlanner(builtPeriodicScheduleConfig);
		System.out.println(" cms tasklist = " + cms.getTaskList_().size());
		
		PSOScheduler pso = new PSOScheduler();
		
		
		Schedule sched = pso.schedule(cms); 
		System.out.println(" Schedule = " + sched +" and has score "+ cms.scoreSchedule(sched));
		
		
		//System.out.println(" CAMS result = " + cms.scoreSchedule(sched));
		
	}
}
