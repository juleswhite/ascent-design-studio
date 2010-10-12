import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class Scheduler  {
	ArrayList<SchedulableTask> schedule = new ArrayList();
	ArrayList<SchedulableTask> tasks_ = new ArrayList();
	
	public ArrayList<SchedulableTask> getTasks_() {
		return tasks_;
	}

	public void setTasks_(ArrayList<SchedulableTask> tasks) {
		tasks_ = tasks;
	}

	public Scheduler(ArrayList<SchedulableTask> tasks){
		tasks_ = tasks;
	}
	
	public class byStride implements Comparator{

		
		public byStride(){
			
		}
		public int compare(Object task1, Object task2){
			//parameter are of type Object, so we have to downcast it to Employee objects
			int task1Stride =  ((SchedulableTask) task1).getPass_();
			int task2Stride = ((SchedulableTask) task2).getPass_();
			
			if( task1Stride > task2Stride)
			return 1;
			else if( task1Stride < task2Stride )
			return -1;
			else
			return 0;
			}
		
	}
	
	public void printSchedule(ArrayList<SchedulableTask> ts){
		for(SchedulableTask t : ts){
			System.out.print(t.getTaskName_() + " "+t.getStride_()+"\n ");
		}
		System.out.println();
	}
	
	public SchedulableTask getNextTask(boolean optimized){
		SchedulableTask st = tasks_.get(0);
		schedule.add(st);
	    st.setPass_(st.getPass_()+st.getStride_());
	   // System.out.println("st info current=" + st.getCurrent_()+ " stride=" +st.getStride_()+" pass ="+st.getPass_());
	    tasks_.remove(0);
	    tasks_.add(0,st);
	    if(!optimized){
	    	Collections.shuffle(tasks_);
	    }
	    Collections.sort(tasks_,new byStride());
	    return st;
	}
	
	public ArrayList<SchedulableTask> scheduleTasks(boolean optimized){
		
		Collections.sort(tasks_,new byStride());//sort(list, c)sort(tasks_);
		ArrayList<String> scheduled = new ArrayList();
		String taskname = "";
	//	int count = 100;
		//printSchedule(tasks_);
		while(scheduled.size()<tasks_.size() ){//&& count > 0){
			
			taskname = getNextTask(optimized).getTaskName_();
			if(!scheduled.contains(taskname)){
				scheduled.add(taskname);
			}
			//count--;
		}
		
		System.out.println("schedule size = " + scheduled.size());
		
		//printSchedule(schedule);
		
		return schedule;
	}
}

