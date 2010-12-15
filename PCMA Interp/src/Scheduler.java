import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class Scheduler  {
	ArrayList<SchedulableTask> schedule = new ArrayList();
	ArrayList<SchedulableTask> tasks_ = new ArrayList();
	HashMap<String,Integer>  taskTotals = new HashMap();
	ArrayList<String> lastTask = new ArrayList();
	ArrayList<SchedulableTask> redTasks_ = new ArrayList();
	ArrayList<SchedulableTask> yellowTasks_ = new ArrayList();
	ArrayList<SchedulableTask> blueTasks_ = new ArrayList();
	int blueRate =1;
	int redRate =1;
	int yellowRate =1;
	int topRate = 1;
	public ArrayList<SchedulableTask> getTasks_() {
		return tasks_;
	}

	public void setTasks_(ArrayList<SchedulableTask> tasks) {
		tasks_ = tasks;
		
	}

	public Scheduler(ArrayList<SchedulableTask> tasks){
		tasks_ = tasks;
		for(SchedulableTask t : tasks){
		//	System.out.println(" rate = " + t.getRateInt_());
			if(t.getAppName_() == "redApp"){
				redTasks_.add(t);
			}
			else if(t.getAppName_() == "blueApp"){
				blueTasks_.add(t);
			}
			else if(t.getAppName_() == "yellowApp"){
				yellowTasks_.add(t);
			}
		}
		Collections.sort(redTasks_,new byRate());
		Collections.sort(blueTasks_,new byRate());
		Collections.sort(yellowTasks_,new byRate());
		
	}
	
	public class byStride implements Comparator{

		
		public byStride(){
			
		}
		public int compare(Object task1, Object task2){
			//parameter are of type Object, so we have to downcast it to SchedulableTask objects
			int task1pass =  ((SchedulableTask) task1).getPass_();
			int task2pass = ((SchedulableTask) task2).getPass_();
			int task1stride =  ((SchedulableTask) task1).getStride_();
			int task2stride = ((SchedulableTask) task2).getStride_();
			
			if( task1pass> task2pass)
			return 1;
			else if( task1pass < task2pass )
			return -1;
			else{
				if(task1stride <= task2stride){
					return -1;
				}
				else{
					return 1;
				}
			}
			
			}
		
	}
	
	public class byRate implements Comparator{

		
		public byRate(){
			
		}
		public int compare(Object task1, Object task2){
			//parameter are of type Object, so we have to downcast it to SchedulableTask objects
			int task1rate =  ((SchedulableTask) task1).getRateInt_();
			int task2rate = ((SchedulableTask) task2).getRateInt_();
			int task1stride =  ((SchedulableTask) task1).getStride_();
			int task2stride = ((SchedulableTask) task2).getStride_();
			
			if( task1rate> task2rate)
			return 1;
			else if( task1rate < task2rate )
			return -1;
			else{
				if(task1stride <= task2stride){
					return -1;
				}
				else{
					return 1;
				}
			}
			
			}
		
	}
	
	public void printSchedule(ArrayList<SchedulableTask> ts){
		for(SchedulableTask t : ts){
			System.out.print(t.getTaskName_() + " "+t.getStride_()+"\n ");
		}
		System.out.println();
	}
	
	public SchedulableTask getNextTask(String appName,boolean optimized){
		
		SchedulableTask st = null;
		/*
		 * To change this back, just take out all the if's and make one of the bodies colorTasks_ to tasks_
		 * 
		 */
		/*if(optimized){
			if(appName =="blueApp"){
				st = blueTasks_.get(0);
				
				//SchedulableTask st = tasks_.get(0);
				
				
				schedule.add(st);
				System.out.println("st info: name: "+ st.getTaskName_()+" rate = " + st.getRate_()+" current=" + st.getCurrent_()+ " stride=" +st.getStride_()+" pass ="+st.getPass_());
			    
			    st.setPass_(st.getPass_()+st.getStride_());
			    blueTasks_.remove(0);
			    
			    blueTasks_.add(st);
			    if(!optimized){
			    	Collections.shuffle(blueTasks_);
			    	
			    }
			    Collections.sort(blueTasks_,new byStride());
			}
			if(appName =="redApp"){
				st = redTasks_.get(0);
			
				//SchedulableTask st = tasks_.get(0);
				
				
				schedule.add(st);
				System.out.println("st info: name: "+ st.getTaskName_()+" rate = " + st.getRate_()+" current=" + st.getCurrent_()+ " stride=" +st.getStride_()+" pass ="+st.getPass_());
			    
			    st.setPass_(st.getPass_()+st.getStride_());
			    redTasks_.remove(0);
			    
			    redTasks_.add(st);
			    if(!optimized){
			    	Collections.shuffle(redTasks_);
			    	
			    }
			    Collections.sort(redTasks_,new byStride());
			}
			if(appName =="yellowApp"){
				st = yellowTasks_.get(0);
			
				//SchedulableTask st = tasks_.get(0);
				
				
				schedule.add(st);
				System.out.println("st info: name: "+ st.getTaskName_()+" rate = " + st.getRate_()+" current=" + st.getCurrent_()+ " stride=" +st.getStride_()+" pass ="+st.getPass_());
			    
			    st.setPass_(st.getPass_()+st.getStride_());
			    yellowTasks_.remove(0);
			    
			    yellowTasks_.add(st);
			    if(!optimized){
			    	Collections.shuffle(yellowTasks_);
			    	
			    }
			    Collections.sort(yellowTasks_,new byStride());
			}
		}
		else{*/
			st = tasks_.get(0);
			
			//SchedulableTask st = tasks_.get(0);
			
			
			schedule.add(st);
			//System.out.println("st info: name: "+ st.getTaskName_()+" rate = " + st.getRate_()+" current=" + st.getCurrent_()+ " stride=" +st.getStride_()+" pass ="+st.getPass_());
		    
		    st.setPass_(st.getPass_()+st.getStride_());
		    tasks_.remove(0);
		    
		    tasks_.add(st);
		    if(!optimized){
		    	Collections.shuffle(tasks_);// MIGHT NEED TO UNCOMMENT
		    	
		    }
		    Collections.sort(tasks_,new byStride());
		//}
			
		
	   // System.out.println("Tasks Length in get Next task: "+tasks_.size());
	    return st;
	}
	
	public int getRateCount(ArrayList<SchedulableTask> tasks, int currentTop){
		int rateCount = 0;
		for(SchedulableTask t : tasks ){
			if(t.getRateInt_() == currentTop){
				rateCount++;
			}
		}
		
		return rateCount;
		
		
	}
	
	public ArrayList<SchedulableTask> scheduleTasks(boolean optimized){
		
		Collections.sort(tasks_,new byStride());//sort(list, c)sort(tasks_);
		ArrayList<String> scheduled = new ArrayList();
		String taskname = "";
	//	int count = 100;
		//printSchedule(tasks_);
		System.out.println("tasks.size()= " +tasks_.size());
		while(scheduled.size()<tasks_.size() ){//&& count > 0){
			int blueCurrentRateCount = getRateCount(blueTasks_,topRate);
			int redCurrentRateCount = getRateCount(redTasks_,topRate);
			int yellowCurrentRateCount = getRateCount(yellowTasks_,topRate);
			int totalSize = 0;
			if((blueCurrentRateCount + redCurrentRateCount + yellowCurrentRateCount) == 0){
				topRate = 1;
				blueCurrentRateCount = getRateCount(blueTasks_,topRate);
				redCurrentRateCount = getRateCount(redTasks_,topRate);
				yellowCurrentRateCount = getRateCount(yellowTasks_,topRate);
			}
			//System.out.println("tasks size in loop: " +tasks_.size());
			//if(!optimized){
			
				taskname = getNextTask("blueApp",optimized).getTaskName_();
				if(!scheduled.contains(taskname)){
					scheduled.add(taskname);
					
					//System.out.println(taskname);
				}
	
				if(!taskTotals.containsKey(taskname)){
					taskTotals.put(taskname,1);
				}
				else{
					Integer total = taskTotals.get(taskname);
				//	System.out.println(" task " + taskname+" total = " + total);
					total++;
					taskTotals.put(taskname,total);
				}
			//}
			/*else{
				while((blueRate<= topRate) && (totalSize <= blueCurrentRateCount) ){
					//System.out.println("topRate = " + topRate);
					taskname = getNextTask("blueApp",optimized).getTaskName_();
					if(!scheduled.contains(taskname)){
						scheduled.add(taskname);
						//System.out.println("taskname in blue = " + taskname);
						//System.out.println(taskname);
					}
		
					if(!taskTotals.containsKey(taskname)){
						taskTotals.put(taskname,1);
						totalSize++;
					}
					else{
						Integer total = taskTotals.get(taskname);
					//	System.out.println(" task " + taskname+" total = " + total);
						total++;
						taskTotals.put(taskname,total);
						totalSize++;
					}
					blueRate = blueTasks_.get(0).getRateInt_();
				}
				totalSize =0 ;
				while(redRate<=topRate && (totalSize <= redCurrentRateCount)){
					taskname = getNextTask("redApp",optimized).getTaskName_();
					//System.out.println(" in red");
					if(!scheduled.contains(taskname)){
						scheduled.add(taskname);
						//System.out.println("taskname in red = " + taskname);
						//System.out.println(taskname);
					}
		
					if(!taskTotals.containsKey(taskname)){
						taskTotals.put(taskname,1);
						totalSize++;
					}
					else{
						Integer total = taskTotals.get(taskname);
					//	System.out.println(" task " + taskname+" total = " + total);
						total++;
						taskTotals.put(taskname,total);
						totalSize++;
					}
					redRate = redTasks_.get(0).getRateInt_();
				}
				totalSize =0 ;
				while(yellowRate<= topRate&& (totalSize <= yellowCurrentRateCount) ){
					taskname = getNextTask("yellowApp",optimized).getTaskName_();
					//System.out.println("made it to yellow");
					if(!scheduled.contains(taskname)){
						scheduled.add(taskname);
						//System.out.println("taskname in yellow = " + taskname);
						//System.out.println(taskname);
					}
		
					if(!taskTotals.containsKey(taskname)){
						taskTotals.put(taskname,1);
						totalSize++;
					}
					else{
						Integer total = taskTotals.get(taskname);
					//	System.out.println(" task " + taskname+" total = " + total);
						total++;
						taskTotals.put(taskname,total);
						totalSize++;
					}
					yellowRate = yellowTasks_.get(0).getRateInt_();
				}
				//System.out.println("made it through all tasks");
				topRate = topRate *2;
				blueRate = topRate;
				redRate = topRate;
				yellowRate = topRate;
				//System.out.println("topRate is  " + topRate);
				if(topRate == 0){
					break;
				}
			}
			*/
			//count--;
		}
		//System.out.println("Optimized:" +optimized + "tasks size = " + tasks_.size()); 
		System.out.println("Optimized: "+ optimized +" schedule size = " + schedule.size());
		
		//printSchedule(schedule);
		
		return schedule;
	}
	
	public void printTotal(){
		System.out.println(" Task totals:");
		Set<String> s = taskTotals.keySet();
		Iterator it = s.iterator();
		while(it.hasNext()){
			String tn = (String)it.next();
			System.out.println (tn + " : " + taskTotals.get(tn));
		}
		taskTotals = new HashMap();
	}
	public void clearSchedule(){
		schedule = new ArrayList();
	}
}

