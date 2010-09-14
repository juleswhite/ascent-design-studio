package org.vanderbilt.spruce.emulab;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.*;

public class Scp {
	String pass_;
	String user_;
	String host_;
	
	public Scp(String source, String destination, String pass, String user, String host){
	/*parseDocument(inputFile);
	printData();
	prepareForm();*/
	FileOutputStream fos = null;
	FileInputStream fis = null;
	String prefix=null;
	String lfile = source;
	String rfile =destination;
	pass_ = pass;
	user_ = user;
	host_ = host;
	try{
		JSch jsch=new JSch();
		java.util.Properties config = new java.util.Properties(); 
		config.put("StrictHostKeyChecking", "no");
		Session s = jsch.getSession(user_,host_); //Session("sprcebot","users.isislab.vanderbilt.edu",22);
		s.setConfig(config);
		s.setPassword(pass_);//s.setPassword("v_a_n_d_y");
		System.out.println("About to connect for scp");
		s.connect();
		System.out.println("connected");
		System.out.println(" about to copy "+ lfile +" to " + rfile);
	
		// exec 'scp -f rfile' remotely
		String command="scp -p -t "+rfile;
	    Channel channel=s.openChannel("exec");
	    ((ChannelExec)channel).setCommand(command);
	
	    // get I/O streams for remote scp
	    OutputStream out=channel.getOutputStream();
	    InputStream in=channel.getInputStream();
	
	    channel.connect();
	    if(checkAck(in)!=0){
	    	System.exit(0);
	          }

	          // send "C0644 filesize filename", where filename should not include '/'
	          long filesize=(new File(lfile)).length();
	          command="C0644 "+filesize+" ";
	          if(lfile.lastIndexOf('/')>0){
	            command+=lfile.substring(lfile.lastIndexOf('/')+1);
	          }
	          else{
	            command+=lfile;
	          }
	          command+="\n";
	          out.write(command.getBytes()); out.flush();
	          if(checkAck(in)!=0){
	    	System.exit(0);
	          }

	          // send a content of lfile
	          fis=new FileInputStream(lfile);
	          byte[] buf=new byte[1024];
	          while(true){
	            int len=fis.read(buf, 0, buf.length);
	    	if(len<=0) break;
	            out.write(buf, 0, len); //out.flush();
	          }
	          fis.close();
	          fis=null;
	          // send '\0'
	          buf[0]=0; out.write(buf, 0, 1); out.flush();
	          if(checkAck(in)!=0){
	    	System.exit(0);
	          }
	          out.close();

	          channel.disconnect();
	          s.disconnect();

	          //System.exit(0);
	        }
	        catch(Exception e){
	          System.out.println(e);
	          try{if(fis!=null)fis.close();}catch(Exception ee){}
	        }
	      }

      static int checkAck(InputStream in) throws IOException{
        int b=in.read();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //          -1
        if(b==0) return b;
        if(b==-1) return b;

        if(b==1 || b==2){
          StringBuffer sb=new StringBuffer();
          int c;
          do {
    	c=in.read();
    	sb.append((char)c);
          }
          while(c!='\n');
          if(b==1){ // error
    	System.out.print(sb.toString());
          }
          if(b==2){ // fatal error
    	System.out.print(sb.toString());
          }
        }
        return b;
      }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scp scp = new Scp(args[0], args[1], args[2], args[3], args[4]);
	}

}
