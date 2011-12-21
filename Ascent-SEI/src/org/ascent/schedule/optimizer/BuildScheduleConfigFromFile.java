package org.ascent.schedule.optimizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BuildScheduleConfigFromFile {

	private String directory_;
	private ArrayList<Application> applications_;
	private ArrayList<SchedulableTask> tasks_;
	public BuildScheduleConfigFromFile(String d){
		
		tasks_ = new ArrayList();
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
	
	public ArrayList<SchedulableTask> makeTasksAndApps(ArrayList<String> appFileNames){
		ArrayList<SchedulableTask> sts = new ArrayList();
		ArrayList<String> taskNames = new ArrayList();
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
						taskNames.add(taskLabel);
						sts.add(st);
						//System.out.println(taskLabel);
					}
				}
			}
		}
		tasks_=sts;
		return sts;
		
		
	}
	
	public ArrayList<Application> getApplications_() {
		return applications_;
	}

	public void setApplications_(ArrayList<Application> applications_) {
		this.applications_ = applications_;
	}

	public ArrayList<SchedulableTask> getTasks_() {
		return tasks_;
	}

	public void setTasks_(ArrayList<SchedulableTask> tasks_) {
		this.tasks_ = tasks_;
	}

	public static void main( String args[]){
		
		String directory = "/Users/brian/home/brian/Old/SEI-WithVTune";
		BuildScheduleConfigFromFile builder = new BuildScheduleConfigFromFile(directory);
		
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
		for(SchedulableTask st: builder.getTasks_()){
			dataStats = builder.getDataWrittenRead(appFileNames, st);
			st.setDataRead_(dataStats[1]);
			st.setDataWritten_(dataStats[0]);
		}
		
		System.out.println("Tasks_ = " + builder.getTasks_());
		System.out.println("Applications_= " + builder.getApplications_());		
		SchedulableTask [] stArray= new SchedulableTask[builder.getTasks_().size()];
		int index =0;
		for(SchedulableTask st : builder.getTasks_()){
			stArray[index]= st;
			index++;
		}
		index =0;
		Application [] appArray= new Application[builder.getApplications_().size()];
		
		for(Application app : builder.getApplications_()){
			appArray[index]= app;
			index++;
		}
		ScheduleConfig builtScheduleConfig = new ScheduleConfig(stArray, appArray);
		System.out.println(" Schedule config = " + builtScheduleConfig);
		
	}
}
