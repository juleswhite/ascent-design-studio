
public class SchedulableTask{
		String taskName_;
		int current_;
		int stride_; 
		int pass_;
		String rate_;
		String appName_;
		
		public String getAppName_() {
			return appName_;
		}

		public int getPass_() {
			return pass_;
		}

		public void setPass_(int pass) {
			pass_ = pass;
		}

		public SchedulableTask(String taskName, int stride, String rate, String appName){
			taskName_ = taskName;
			stride_ = stride;
			current_ = stride;
			pass_ = stride;
			rate_ = rate;
			appName_ = appName;
		}
		
		public String getTaskName_() {
			return taskName_;
		}

		public void setTaskName_(String taskName) {
			taskName_ = taskName;
		}

		public int getCurrent_() {
			return current_;
		}

		public void setCurrent_(int current) {
			current_ = current;
		}

		public int getStride_() {
			return stride_;
		}

		public void setStride_(int stride) {
			stride_ = stride;
		}
		public String getRate_(){
			return rate_;
		}


		
		
		
}
