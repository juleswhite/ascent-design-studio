package org.vanderbilt.spruce.emulab;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class IsisLabUtil {

	public static HtmlAnchor getAnchorByText(HtmlPage page, String text) {
		HtmlAnchor anch = null;

		for (HtmlAnchor anchor : page.getAnchors()) {
			if (anchor.getFirstChild().getTextContent().equals(text)) {
				return anchor;
			}
		}
		return anch;
	}

	public static ArrayList<String> getAnchorByPrefix(HtmlPage page, String text) {
		HtmlAnchor anch = null;
		ArrayList anchors = new ArrayList();	
		for (HtmlAnchor anchor : page.getAnchors()) {
			if (anchor.getFirstChild() != null){
				//System.out.println("\n\n anch = " + anchor.getFirstChild().getTextContent());
				//System.out.println(" firstChild= " + anchor.getFirstChild());
				//System.out.println("anchor = " + anchor);
				//System.out.println("anchor.gethrefattribute =" + anchor.getHrefAttribute()+"\n\n");
				
				if(anchor.getHrefAttribute().contains(text) && !anchor.getHrefAttribute().contains("showexp_list")) {
					//System.out.println("\n adding anchor "+anchor + " \n");
					anchors.add(anchor.getHrefAttribute());
				}
			}
		}
		return anchors;
	}
	
	public static HtmlInput getInputByName(HtmlPage page, String text) {
		HtmlInput anch = null;

		for (HtmlForm form : page.getForms()) {
			for (HtmlInput input : form.getInputsByName(text)) {
				if (input.getNameAttribute().equals(text)) {
					return input;
				}
			}
		}
		return null;
	}
    
	
	public static HtmlForm getFormByAction(HtmlPage page, String url,
			String action) {
		HtmlForm sform = null;
		for (HtmlForm form : page.getForms()) {
			String fact = form.getActionAttribute();
			// println fact;
			if (fact.trim().endsWith(action) || fact.equals(url + "/" + action)
					|| fact.endsWith("/" + action)) {
				sform = form;
			}
		}
		;

		return sform;
	}

	public static WebClient login(String url, String user, String pass) {

		Protocol easyhttps = new Protocol("https",
				new EasySSLProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", easyhttps);

		WebClient webClient = new WebClient();
		webClient.setCookiesEnabled(true);
       
		System.out.println("Logging in...");
		// Get the first page
		
		try {
			
			//System.out.println(url + "/login.php3");
			
			HtmlPage page1 = (HtmlPage) webClient.getPage(url + "/login.php3");

			// Get the form that we are dealing with and within that form,
			// find the submit button and the field that we want to change.
			HtmlForm loginform = getFormByAction(page1, url, "login.php3");

			HtmlSubmitInput button = (HtmlSubmitInput) loginform
					.getInputByName("login");
			HtmlTextInput userField = (HtmlTextInput) loginform
					.getInputByName("uid");
			//System.out.println("userField =" +userField);
			HtmlPasswordInput passField = (HtmlPasswordInput) loginform
					.getInputByName("password");

			// Change the value of the text field
			userField.setValueAttribute(user);
			passField.setValueAttribute(pass);
			//webClient.getPage("http://afrl-gift.dre.vanderbilt.edu:8090/expList");
			
			// Now submit the form by clicking the button and get back the
			// second page.
			HtmlPage page2 = (HtmlPage) button.click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return webClient;
	}
			
	public static ArrayList<String> login2(String url, String user, String pass) {

		Protocol easyhttps = new Protocol("https",
				new EasySSLProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", easyhttps);

		WebClient webClient = new WebClient();
		webClient.setCookiesEnabled(true);
       
		System.out.println("Logging in...");
		// Get the first page
		
		try {
			
			//System.out.println(url + "/login.php3");
			
			HtmlPage page1 = (HtmlPage) webClient.getPage(url + "/login.php3");

			// Get the form that we are dealing with and within that form,
			// find the submit button and the field that we want to change.
			HtmlForm loginform = getFormByAction(page1, url, "login.php3");

			HtmlSubmitInput button = (HtmlSubmitInput) loginform
					.getInputByName("login");
			HtmlTextInput userField = (HtmlTextInput) loginform
					.getInputByName("uid");
			//System.out.println("userField =" +userField);
			HtmlPasswordInput passField = (HtmlPasswordInput) loginform
					.getInputByName("password");

			// Change the value of the text field
			userField.setValueAttribute(user);
			passField.setValueAttribute(pass);
			//webClient.getPage("http://afrl-gift.dre.vanderbilt.edu:8090/expList");
			
			// Now submit the form by clicking the button and get back the
			// second page.
			HtmlPage page2 = (HtmlPage) button.click();
		
		
			System.out.print("page2 anchors = "+ page2.getAnchors());
			ArrayList<String> anchs = getAnchorByPrefix(page2, "showexp");
		
			System.out.println("\n\n about to print anchs\n");
			for(String anch : anchs){
				System.out.println(" exp anch = " + anch+"\n");
				
			}
			HtmlPage expPage;
			ArrayList<HashMap<String,String>> expIds = new ArrayList();
			for(String anchorUrl : anchs){
				String projectName = getProjectName(anchorUrl);
				HashMap expData = new HashMap();
				expData.put("pName", projectName);
				String expName = getExpName(anchorUrl);
				expData.put("expName", expName);
				System.out.println(" ProjectName is " + projectName + " and expName is " + expName);
				String eaurl = "https://www.isislab.vanderbilt.edu/"+anchorUrl;
				System.out.println("eaurl =  " + eaurl);
				expPage = (HtmlPage) webClient.getPage(eaurl);
				ArrayList<String>expAnchs = getAnchorByPrefix( expPage, "swapexp.php3");
				for( String expAnch : expAnchs){
					System.out.println("\n expAnch =" + expAnch +"\n ");
					String expId = getExpId(expAnch);
					System.out.println("\n expId =" + expId +"\n ");
					expData.put("eid", expId);
					expData.put(projectName,expId);
				}
				expIds.add(expData);
			}
			//System.out.println("\n expIds = " + expIds);
			//System.out.println("page2 as text = "+ page2.asText());
			//System.out.println("Page2 html elements by name " + page2.getHtmlElementsByName("<a href>"));
			//System.out.println(expIds);
			/*post
			 * 
			 * */
			HttpClient client = new HttpClient();
			
		    client.getParams().setParameter("http.useragent", "Test Client");
		    String expPostString = "expNames=";
		    String pnamePostString = "";
		    String eidPostString = "";
		    for(HashMap exp : expIds){
		    	expPostString = expPostString +(String) exp.get("expName")+",";
		    	pnamePostString = pnamePostString +(String) exp.get("pName")+",";
		    	eidPostString = eidPostString +(String) exp.get("eid")+",";
		    }
		    expPostString = (String) expPostString.subSequence(0, expPostString.length()-1);
		    System.out.println(expPostString);
		    pnamePostString = (String) pnamePostString.subSequence(0, pnamePostString.length()-1);
		    System.out.println(pnamePostString);
		    eidPostString = (String) eidPostString.subSequence(0, eidPostString.length()-1);
		    System.out.println(eidPostString);
		    ArrayList<String> expOutput = new ArrayList();
		    expOutput.add(expPostString);
		    expOutput.add(pnamePostString);
		    expOutput.add(eidPostString);
		   
		    return expOutput;
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return new ArrayList<String>();
		
	}
 
	public static ArrayList<String> login3(String url, String user, String pass) {

		Protocol easyhttps = new Protocol("https",
				new EasySSLProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", easyhttps);

		WebClient webClient = new WebClient();
		webClient.setCookiesEnabled(true);
       
		System.out.println("Logging in...");
		// Get the first page
		
		try {
			
			//System.out.println(url + "/login.php3");
			
			HtmlPage page1 = (HtmlPage) webClient.getPage(url + "/login.php3");

			// Get the form that we are dealing with and within that form,
			// find the submit button and the field that we want to change.
			HtmlForm loginform = getFormByAction(page1, url, "login.php3");

			HtmlSubmitInput button = (HtmlSubmitInput) loginform
					.getInputByName("login");
			HtmlTextInput userField = (HtmlTextInput) loginform
					.getInputByName("uid");
			//System.out.println("userField =" +userField);
			HtmlPasswordInput passField = (HtmlPasswordInput) loginform
					.getInputByName("password");

			// Change the value of the text field
			userField.setValueAttribute(user);
			passField.setValueAttribute(pass);
			//webClient.getPage("http://afrl-gift.dre.vanderbilt.edu:8090/expList");
			
			// Now submit the form by clicking the button and get back the
			// second page.
			HtmlPage page2 = (HtmlPage) button.click();
		
		
			System.out.print("page2 anchors = "+ page2.getAnchors());
			ArrayList<String> anchs = getAnchorByPrefix(page2, "showexp");
		
			System.out.println("\n\n about to print anchs\n");
			for(String anch : anchs){
				System.out.println(" exp anch = " + anch+"\n");
				
			}
			HtmlPage expPage;
			ArrayList<HashMap<String,String>> expIds = new ArrayList();
			for(String anchorUrl : anchs){
				String projectName = getProjectName(anchorUrl);
				HashMap expData = new HashMap();
				expData.put("pName", projectName);
				String expName = getExpName(anchorUrl);
				expData.put("expName", expName);
				System.out.println(" ProjectName is " + projectName + " and expName is " + expName);
				String eaurl = "https://www.isislab.vanderbilt.edu/"+anchorUrl;
				System.out.println("eaurl =  " + eaurl);
				expPage = (HtmlPage) webClient.getPage(eaurl);
				ArrayList<String>expAnchs = getAnchorByPrefix( expPage, "swapexp.php3");
				for( String expAnch : expAnchs){
					System.out.println("\n expAnch =" + expAnch +"\n ");
					String expId = getExpId(expAnch);
					System.out.println("\n expId =" + expId +"\n ");
					expData.put("eid", expId);
					expData.put(projectName,expId);
					
					
				}
				expIds.add(expData);
			}
			System.out.println("\n expIds = " + expIds);
			//System.out.println("page2 as text = "+ page2.asText());
			//System.out.println("Page2 html elements by name " + page2.getHtmlElementsByName("<a href>"));
			//System.out.println(expIds);
			/*post
			 * 
			 * */
			HttpClient client = new HttpClient();
			
		    client.getParams().setParameter("http.useragent", "Test Client");
		    String expPostString = "expNames=";
		    String pnamePostString = "";
		    String eidPostString = "";
			
		    System.out.println("IN util");
		    for(HashMap exp : expIds){
		    	expPostString = expPostString +(String) exp.get("expName")+",";
		    	pnamePostString = pnamePostString +(String) exp.get("pName")+",";
		    	eidPostString = eidPostString +(String) exp.get("eid")+",";
		    }
		    expPostString = (String) expPostString.subSequence(0, expPostString.length()-1);
		    System.out.println(expPostString);
		    System.out.println("pnamePostString = "+pnamePostString);
		    pnamePostString = (String) pnamePostString.subSequence(0, pnamePostString.length()-1);
		    System.out.println(pnamePostString);
		    eidPostString = (String) eidPostString.subSequence(0, eidPostString.length()-1);
		    System.out.println(eidPostString);
		    ArrayList<String> expOutput = new ArrayList();
		    expOutput.add(expPostString);
		    expOutput.add(pnamePostString);
		    expOutput.add(eidPostString);
		    return expOutput;
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return new ArrayList<String>();
		
	}
	private static String getExpId(String expAnch){
		char [] expChars = expAnch.toCharArray();
		boolean notExpYet = true;
		int i = 0;
		String expId = "";
		while( i < expChars.length && notExpYet == true){
			
			if(expChars[i] == '='){
				notExpYet = false;
				int j = i+1;
				while(expChars[j] != '&' && j < expChars.length){
					expId = expId + expChars[j];
					j++;
				}
				
			}
			i++;
		}
		return expId;
	}
	
	private static String getProjectName(String expAnch){
		char [] expChars = expAnch.toCharArray();
		boolean notExpYet = true;
		int eqSignCount = 0;
		int i = 0;
		String expId = "";
		while( i < expChars.length && notExpYet == true){
			//System.out.println("char= " + expChars[i]);
			if(expChars[i] == '='){
				//System.out.println("eqSignCount = " + eqSignCount);
				eqSignCount++;
				if(eqSignCount == 1){
					notExpYet = false;
					//System.out.println("expChars Length = " + expChars.length);
					//System.out.println(" last char = " + expChars[expChars.length-1]);
					int j = i+1;
					while(expChars[j] != '&' && j < expChars.length){
						expId = expId + expChars[j];
						j++;
					}
				}
				
			}
			i++;
		}
		return expId;
	}
	
	private static String getExpName(String expAnch){
		char [] expChars = expAnch.toCharArray();
		boolean notExpYet = true;
		int eqSignCount = 0;
		int i = 0;
		String expId = "";
		while( i < expChars.length && notExpYet == true){
			//System.out.println("eqSignCount = " + eqSignCount);

			if(expChars[i] == '='){
				eqSignCount++;
				//System.out.println("expChars Length = " + expChars.length);
				//System.out.println(" last char = " + expChars[expChars.length-1]);
				if(eqSignCount == 2){
					notExpYet = false;
					int j = i+1;
					while( j < expChars.length){
						expId = expId + expChars[j];
						j++;
					}
				}
				
			}
			i++;
		}
		return expId;
	}
	
	/*public static boolean getUserId(String url, String user, String pass){
		WebClient webClient = login2(url, user, pass);
		////System.out.println("wc homepage" + webClient.g//.getHomePage());
		try {
			
		/*	String eurl = "https://afrl-gift.dre.vanderbilt.edu:8090/expList";
			//System.out.println(eurl);
			HtmlPage epage = (HtmlPage) webClient.getPage(eurl);
			HtmlInput anchor = getInputByName(epage, "confirmed");

			if (anchor == null) {
				//System.out.println("Experiment not found");
				//System.out.println(epage.asText());
				return false;
			}

			HtmlPage page = (HtmlPage)anchor.click();
			//System.out.println(page.asText());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	
	}*/
	
	public static boolean swapinExperiment(String url, String user,
			String pass, String eidnumber) {
		WebClient webClient = login(url, user, pass);
		System.out.println("User = " + user);
		System.out.println(" pass = "+ pass);
		
		try {

			String eurl = "https://www.isislab.vanderbilt.edu/swapexp.php3?experiment="+eidnumber+"&inout=in";
			System.out.println(eurl);
			HtmlPage epage = (HtmlPage) webClient.getPage(eurl);
			HtmlInput anchor = getInputByName(epage, "confirmed");

			if (anchor == null) {
				System.out.println("Experiment not found");
				System.out.println(epage.asText());
				return false;
			}

			HtmlPage page = (HtmlPage)anchor.click();
			System.out.println("Page = " +page.asText());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
