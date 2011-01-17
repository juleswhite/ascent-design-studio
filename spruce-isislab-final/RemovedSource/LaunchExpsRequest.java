

package org.vanderbilt.spruce.emulab;



public class LaunchExpsRequest implements Runnable{
	private String url_;
	private String user_;
	private String pass_;
	
	
	
	public LaunchExpsRequest(String url, String user, String pass) {
		super();
		
		pass_ = pass;;
		user_ = user;
		url_ = url;
	}



	public void run() {
	//	IsisLabUtil.getUserId(url_, user_, pass_);
	}

}