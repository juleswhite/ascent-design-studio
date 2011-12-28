package org.ascent.schedule.optimizer;

public class PeriodicTask extends SchedulableTask {

	double rate_ = -1;
	public PeriodicTask(int id, String label, int[] resources) {
		super(id, label, resources);
		// TODO Auto-generated constructor stub
	}

	public PeriodicTask(int id, String name, int[] iarray, Application app,
			double dr, double dw) {
		super(id, name, iarray, app, dr, dw);
		// TODO Auto-generated constructor stub
	}
	public PeriodicTask(int id, String name, int[] iarray, Application app,
			double dr, double dw, double rate) {
		
		super(id, name, iarray, app, dr, dw);
		rate_ = rate;
		// TODO Auto-generated constructor stub
	}

	public PeriodicTask(SchedulableTask st) {
		super(st.getId(),st.getLabel(),st.getResources(), st.getApplication_(), st.getDataRead_(), st.getDataWritten_());
		
		
		// TODO Auto-generated constructor stub
	}

	public double getRate_() {
		return rate_;
	}

	public void setRate_(double rate_) {
		this.rate_ = rate_;
	}

}
