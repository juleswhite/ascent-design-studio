package org.ascent.schedule.optimizer;

import java.util.ArrayList;
import java.util.List;

public class Application {

	protected List<SchedulableTask> taskList_ = new ArrayList();
	private String name_ = "NOT NAMED";
	private double size_ = 0;
	private int id_ = -1;
	public Application(){
		
	}
	public Application( int id , String name){
		id_ = id;
		name_ = name; 
	}
}
