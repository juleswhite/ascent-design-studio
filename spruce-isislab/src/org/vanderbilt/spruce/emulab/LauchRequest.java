package org.vanderbilt.spruce.emulab;



public class LauchRequest implements Runnable{
	private String url_;
	private String user_;
	private String pass_;
	private String launchUrl_;
	
	
	
	public LauchRequest(String url, String user, String pass, String launchurl) {
		super();
		
		pass_ = pass;
		launchUrl_ = launchurl;
		user_ = user;
		url_ = url;
	}



	public void run() {
		IsisLabUtil.swapinExperiment(url_, user_, pass_, launchUrl_);
	}

}
