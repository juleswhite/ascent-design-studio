import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class ScheduleOptimizer {
	ArrayList<SchedulableTask> tasks_;
	int maxRate_;
	ArrayList<String> appNames_;
	ArrayList<ArrayList> fullTasks = new ArrayList();
	//HashMap<String,ArrayList<SchedulableTask>> fullTasks = new HashMap();
	ArrayList<SchedulableTask> finalTasks_ = new ArrayList();
	ArrayList<SchedulableTask> finalTasksNoDoubles_ = new ArrayList();
	ArrayList<SchedulableTask> allFinalTasks_ = new ArrayList();
	
	public ScheduleOptimizer(ArrayList<SchedulableTask> tasks, int maxRate){
		tasks_ = tasks;
		maxRate_ = maxRate;
		appNames_ = new ArrayList();
		appNames_.add("blueApp");
		appNames_.add("yellowApp");
		appNames_.add("redApp");
		appNames_.add("greenApp");
		appNames_.add("purpleApp");
		/*appNames_.add("orangeApp");
		appNames_.add("brownApp");
		appNames_.add("blackApp");
		appNames_.add("whiteApp");
		appNames_.add("pinkApp");*/
	}
	
	public ArrayList<SchedulableTask> getTasks_(){
		return finalTasks_;
	}
	
	public void optimize(){
		int currentRate =1;
		boolean rateChanged = false;
		int newRate = -1;
		int rateChangeIndex = -1;
		int startIndex = 0;
		int currentIndex =0;
		int moverSize = 0;
		int run =0;
		ArrayList<SchedulableTask> movers; //= new ArrayList();
		while( currentRate <= maxRate_ && tasks_.size()>0){
			//System.out.println(" CurrentRate = " + currentRate +" and maxRate = " + maxRate_);
			movers = new ArrayList();
			run++;
			for(String appName : appNames_){
				rateChanged = false;
				//currentIndex = startIndex;
				currentIndex =0;
				while(!rateChanged && tasks_.size()>0 && currentIndex< tasks_.size()){
					
				//	System.out.println("tasks.size= " + tasks_.size());
				//	System.out.println("current index = " + currentIndex);
					SchedulableTask currTask = tasks_.get(currentIndex);
					if(currTask.getRateInt_() == currentRate){
						if(currTask.getAppName_() == appName){
							movers.add(currTask);
						//	System.out.println(" adding task to movers");
							tasks_.remove(currentIndex);
						}
						else{
							currentIndex++;
						}
					}
					else{
						rateChanged = true;
						newRate = currTask.getRateInt_();
						rateChangeIndex = currentIndex;
						
					}
					
				}
				
			}
				startIndex = currentIndex;
			//	fullTasks.put(currentRate+ "Run"+run +"", movers);
				fullTasks.add(movers);
			//	System.out.println(" Just added " + movers.size() + " movers");
				moverSize += movers.size();
				currentRate = newRate;
				for(SchedulableTask s : movers){
				//	System.out.println(" OPPY " + s.getTaskName_());
				}
				
			}
		System.out.println(" MOVERS SIZE = " + moverSize);
	/*	Set<String> s = fullTasks.keySet();
		Iterator it = s.iterator();
		while(it.hasNext()){
			String str = (String)it.next();
			ArrayList<SchedulableTask> tn = fullTasks.get(str);
			
	*/		//System.out.println (tn + " : " + fullTasks.get(tn));
		System.out.println("fullTasks length " + fullTasks.size());
		for(ArrayList<SchedulableTask>tn : fullTasks){
			System.out.println("Number of tasks in each fulltask" + tn.size());
			for( SchedulableTask t : tn){
					//System.out.println("FULL TASK: " + t.getTaskName_() +" rate "+ t.getRateInt_());
					finalTasks_.add(t);
			}
		}
		System.out.println(" Task totals:" +finalTasks_.size() );
		
	}

	/*public void removeDoubles(){//Ignore. We originally though that removing single consecutive instances of tasks would be helpful.
		int rate = finalTasks_.get(0).getRateInt_();
		int currentIndex =0;
		int lastIndex = 0;
		HashMap<String,ArrayList<Integer>> map= new HashMap();
		String lastAppName = "";
		boolean rateChanged = true;
		/*
		 * Map of app names for each rate. The array list has the count of each App run steak, the index it starts, and the index it stops
		 * This will tell us where the doubles are so we can take care of them.
		 
		int currentRate = 1;
		while(currentIndex < finalTasks_.size() && currentRate <= maxRate_){
			map= new HashMap();
			finalTasksNoDoubles_ = new ArrayList();
			lastIndex = currentIndex;
			System.out.println("currentIndex " + currentIndex );
			 currentRate = finalTasks_.get(currentIndex).getRateInt_();
			 int doubleMoves =0; 
			while( currentRate == rate && currentIndex< finalTasks_.size()){
				SchedulableTask currTask = finalTasks_.get(currentIndex);
				
				if(!map.containsKey(currTask.getAppName_())){
					
					ArrayList appInfo = new ArrayList();
					if(appInfo.size() == 3){
						appInfo.clear();
						
					}
					appInfo.add(0);
					appInfo.add(currentIndex);
					appInfo.add(lastIndex);
					if(map.size()==0){
						lastAppName = currTask.getAppName_();
					}
					
					map.put(currTask.getAppName_(), appInfo);
					
				}
				int count = map.get(currTask.getAppName_()).get(0);
				count++;
				ArrayList appInfo = map.get(currTask.getAppName_());
				//System.out.println(" The start = " + appInfo.get(1));
				appInfo.remove(0);
				appInfo.add(0,count);
				map.put(currTask.getAppName_(), appInfo);
				if(lastAppName != currTask.getAppName_() ){
					rateChanged = false;
					ArrayList lastAppInfo = map.get(lastAppName);
					lastAppInfo.remove(2);
					lastAppInfo.add(2,currentIndex-1);
					//System.out.println("Last appname =" +lastAppName+", currAN = "+currTask.getAppName_()+"Start = "+lastAppInfo.get(1)+" stop = "+lastAppInfo.get(2));
					
					map.put(lastAppName,lastAppInfo);
					lastAppName = currTask.getAppName_();
				}
				
				
				
				currentIndex++;
				if(currentIndex != finalTasks_.size()){
					currentRate = finalTasks_.get(currentIndex).getRateInt_();
				}
			}
			ArrayList lastAppInfo = map.get(lastAppName);
			if(lastAppInfo != null){
				lastAppInfo.remove(2);
				lastAppInfo.add(2,currentIndex-1);
				map.put(lastAppName,lastAppInfo);
			}
			Set<String> s = map.keySet();
			
			Iterator it = s.iterator();
			boolean movedPrevious = false;
			boolean movedToFirst = false;
			ArrayList<SchedulableTask> movingTasks = new ArrayList();
			ArrayList<SchedulableTask> movingFrontTasks = new ArrayList();
			ArrayList<SchedulableTask> movingLastTasks = new ArrayList();
			String movedAppName = "";
			
			while(it.hasNext()){
				String str = (String)it.next();
				ArrayList<Integer> appInfo = map.get(str);
				
				/*
				 * There are three different double moves that can be made:
				 * 1.) Move them to the start if they match up with the previous rate's final app
				 * 2.) Move them to the bottom so hopefully the next rate will have at least one of that app's type
				 * *Note: I"m assumming we'll hit more than miss on this and that having a run of 3 is cancels
				 * out the penalty of two
				 * 3.)split them up within the rate, without splitting any runs of other applications. Last resort.
				 * our int value of doubleMoves corresponds to the moves we've done.
				 *
				 *
				if(appInfo.get(0) ==2){
					if(finalTasksNoDoubles_.size()==0){
						doubleMoves =1;
					}
					
					if(doubleMoves == 0 && (finalTasksNoDoubles_.get(finalTasksNoDoubles_.size()-1).getAppName_()) == finalTasksNoDoubles_.get(appInfo.get(1)).getAppName_()){
						if((finalTasksNoDoubles_.get(finalTasksNoDoubles_.size()-1).getAppName_()) == finalTasksNoDoubles_.get(appInfo.get(1)).getAppName_()){
							int start = appInfo.get(1);
							int last = appInfo.get(2);
							for(int j = start; j <= last; j++){
								movingFrontTasks.add(0,finalTasks_.get(j));
							}
							doubleMoves = 1;
						}
					}
					
					else if(doubleMoves ==1){
						int start = appInfo.get(1);
						int last = appInfo.get(2);
						//for(int j = start; j <= last; j++){
						movingLastTasks.add(0,finalTasks_.get(start));
						movingLastTasks.add(1,finalTasks_.get(last));
						doubleMoves =2;
					}
					
					else {
						int start = appInfo.get(1);
						int last = appInfo.get(2);
						//for(int j = start; j <= last; j++){
						movingFrontTasks.add(movingFrontTasks.size(),finalTasks_.get(start));
						movingLastTasks.add(0,finalTasks_.get(last));
						doubleMoves =2;
					}
				}
				else{
					int start = appInfo.get(1);
					int last = appInfo.get(2);
					for(int j = start; j<= last; j++){
						movingTasks.add(finalTasks_.get(j));
					}
				}
			
			}
			
			finalTasksNoDoubles_.addAll(movingFrontTasks);
			finalTasksNoDoubles_.addAll(movingTasks);
			finalTasksNoDoubles_.addAll(movingLastTasks);
		//	System.out.println("rate = " +rate );
		//	System.out.println(" ftnd count = " + finalTasksNoDoubles_.size());
			allFinalTasks_.addAll(finalTasksNoDoubles_);
		
			
			
			rate = currentRate;
			rateChanged = true;
		}
		System.out.println("allFinalTasks_ size = " + allFinalTasks_.size());
		for(SchedulableTask ftd : allFinalTasks_){
		//	System.out.println("FTND____" + ftd.getTaskName_() + "rate "+ ftd.getRateInt_());
		}
		
	}*/
		
	public ArrayList<SchedulableTask> getAllFinalTasks(){
		return allFinalTasks_;
	}
	public ArrayList<SchedulableTask> getFinalTasks(){
		return finalTasks_;
	}	
		
	
	/*public void optimize(){
		
		int currentRate = 1;
		
		int rateStartPosition = 0;
		int currentPosition =0;
		while( currentRate < maxRate_){
			boolean groupTwoFound = false;
			String lastAppName = "";
			String foundDoubleName ="";
			//lastAppName = 
			
			
			int  twoPosition = -1;
			boolean inBiggerRate = false;
			int biggerRatePosition = -1;
			/*
			 * Look right until we hit a task of a higher rate
			 *
			
			while(inBiggerRate == false && currentPosition < tasks_.size()){
				System.out.println("current position = " + currentPosition);
				
				if(groupTwoFound){
					/*
					 * So we have the indexes of two consecutive tasks of the same app
					 * They are at position "twoPosition" and "twoPosition"+1.
					 * 
					 * Now we have to see if there is a singlet in the same rate we can cozy them up next to
					 *(Remember, we can't place them against themselves !)
					 *
					int singletPosition = -1;
					boolean edge = false;
					int i = 0;
					int edgePosition = 0;
					while(!edge){
						
						System.out.println("In edge");
						if( i != twoPosition && i != twoPosition+1){
							if(tasks_.get(edgePosition).getAppName_() == foundDoubleName){
								singletPosition = edgePosition;
								if(singletPosition < twoPosition){
									SchedulableTask mover = tasks_.get(singletPosition);
									tasks_.remove(singletPosition);
									tasks_.add(twoPosition-1,mover);
								}
								else if(singletPosition > twoPosition){
									SchedulableTask mover = tasks_.get(singletPosition);
									tasks_.remove(singletPosition);
									tasks_.add(twoPosition+1,mover);
								}
							}
						}
						i++;
						System.out.println("current position " + edgePosition +" & rate there is " +tasks_.get(edgePosition).getRateInt_() +" and current rate = " +currentRate +" and name is " + tasks_.get(edgePosition).getTaskName_());
						if(tasks_.get(edgePosition).getRateInt_() > currentRate){
							edge = true;
						}
						edgePosition++;
					}
					groupTwoFound = false;
					currentPosition++;
				}
				
				else{
					
				
					if(tasks_.get(currentPosition).getAppName_() == lastAppName){
						groupTwoFound = true; 
						foundDoubleName = lastAppName;
						System.out.println("Found a group of two with appName "+ lastAppName);
					}
					else{
						System.out.println("LastApp = " +lastAppName +" and this: " +tasks_.get(currentPosition).getAppName_());
						lastAppName = tasks_.get(currentPosition).getAppName_();
						//currentPosition++;
					}
					if(tasks_.get(currentPosition).getRateInt_()> currentRate){
						inBiggerRate =true;
						biggerRatePosition = currentPosition;
						rateStartPosition = currentPosition;
					}
					currentPosition++;
				}
			//find a group of two
			
		
		
			}
			currentRate = currentRate *2;
			//we should now be == to max rate
		
		
		
		}
		
	}
	*/
	public void printSchedule(){
		for(SchedulableTask st : tasks_){
			System.out.println("Post-OPT st info: name: "+ st.getTaskName_()+" rate = " + st.getRate_()+" current=" + st.getCurrent_()+ " stride=" +st.getStride_()+" pass ="+st.getPass_());
		    
		}
	}
	
	
}
