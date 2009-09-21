package org.crush.syrupy;

public class SyrupyThread implements Runnable{
    
	private Thread t;       
    private  int count_;
    private boolean suspended_;
	private static String execString_;
	
	public SyrupyThread(){
		
	}
	
	@Override
	public void run() {
		try 
		{
			Runtime r = Runtime.getRuntime();
			Process p = r.exec(execString_);
			p.wait();
			

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public void setExecString_(String execString) {
		execString_ = execString;
	}
	

}
