package org.vanderbilt.spruce.emulab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;

//import com.sun.net.httpserver.Authenticator;
//import com.sun.net.httpserver.HttpExchange;


public class MyAuthenticator extends Authenticator {
 // This method is called when a password-protected URL is accessed
	
	
	protected PasswordAuthentication getPasswordAuthentication() {
		// Get information about the request
		String promptString = getRequestingPrompt();
		String hostname = getRequestingHost();
		InetAddress ipaddr = getRequestingSite();
		int port = getRequestingPort();

		// Get the username from the user...
		String username = "sprcebot";

     // Get the password from the user...
		String password = "brianjules";

     // Return the information
		return new PasswordAuthentication(username, password.toCharArray());
 }




public static void main(String args[]){
	Authenticator.setDefault(new MyAuthenticator());

//Access the page
	try {
	 // Create a URL for the desired page
		String filename="460/done.txt" ;
	//RLRequest request = new URLRequest("https://www.isislab.vanderbilt.edu/checkFile.php?id="+filename);
	  
	 URL url = new URL("http://www.isislab.vanderbilt.edu/checkFile.php?id="+filename);
	  
	
	 // Read all the text returned by the server
	 BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	 String str;
	 while ((str = in.readLine()) != null) {
		 System.out.println("str = "+str);
	     // str is one line of text; readLine() strips the newline character(s)
	 }
	 in.close();
	} 
	//System.out.println(in.)
	catch (MalformedURLException e) {
		System.out.println(e);
	} 
	catch (IOException e) {
		System.out.println(e);
	}
	}
}