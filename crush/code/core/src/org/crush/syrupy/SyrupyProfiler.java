package org.crush.syrupy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;

import org.crush.deployment.SoloMonitor;


public class SyrupyProfiler implements org.crush.deployment.Profiler {

	private String iterations_;
	private String pid_;
	private String outputFile_;
	private String execString_;
	private SyrupyParser sp_;
	private SoloMonitor sm_ = new SoloMonitor();
	private static String systemExecString_;
	
	public SyrupyProfiler(){
		
	}
	
	public SyrupyProfiler(String iterations, String pid){
		iterations_ = iterations;
		pid_ = pid;
		
		sp_ = new SyrupyParser();
	}
	
	
	
	public void constructExecString(){
		execString_ = "";
		execString_ += "top -l";
		execString_ += " 2";
		execString_ += "  -pid "+pid_;
		//execString_ += "  > " + outputFile_;
		systemExecString_ = execString_;
	}
	
	
	
	public void execute(){
		try 
		{
			int total = 0;
			CmdExec cmd = new CmdExec();
		    System.out.println(cmd.run("ls -a"));
			while(total < Integer.parseInt(iterations_)){
				
				System.out.println(cmd.run(execString_));
				
			    //Thread.sleep(3000);
			    
			    sp_.parseInput(cmd.run(execString_));
				
				//Thread.sleep(2000);
				HashMap<String,String> map = (HashMap) sp_.processTopPID();
				System.out.println("#####About to examine System info ####");
				sp_.processTopSystemInfo();
				sm_.updateProcess(map);
				sm_.updateAllSystemData(sp_.processTopSystemInfo());
				
				total++;
			}
			System.out.println(sm_.getAllData_());
			sm_.writeAllData("allData"+pid_+".txt");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	

	public String getExecString_() {
		return execString_;
	}
	
	public static void main( String args[]){
		SyrupyProfiler sp = new SyrupyProfiler(args[0],args[1]);
		sp.constructExecString();
		System.out.println(" Exec string = " + sp.getExecString_());
		sp.execute();
	}
}
