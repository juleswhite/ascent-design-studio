package org.vanderbilt.spruce.emulab;

import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
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
			
			System.out.println(url + "/login.php3");
			
			HtmlPage page1 = (HtmlPage) webClient.getPage(url + "/login.php3");

			// Get the form that we are dealing with and within that form,
			// find the submit button and the field that we want to change.
			HtmlForm loginform = getFormByAction(page1, url, "login.php3");

			HtmlSubmitInput button = (HtmlSubmitInput) loginform
					.getInputByName("login");
			HtmlTextInput userField = (HtmlTextInput) loginform
					.getInputByName("uid");
			HtmlPasswordInput passField = (HtmlPasswordInput) loginform
					.getInputByName("password");

			// Change the value of the text field
			userField.setValueAttribute(user);
			passField.setValueAttribute(pass);

			// Now submit the form by clicking the button and get back the
			// second page.
			HtmlPage page2 = (HtmlPage) button.click();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return webClient;
	}

	public static boolean swapinExperiment(String url, String user,
			String pass, String eidnumber) {
		WebClient webClient = login(url, user, pass);
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
			System.out.println(page.asText());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
