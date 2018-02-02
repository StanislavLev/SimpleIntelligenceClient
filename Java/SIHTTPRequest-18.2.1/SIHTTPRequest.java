package com.simpleintelligence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

/** 
*This class helps to send request from client side to API, hosted on SimpleIntelligence.com.<br/> 
*There are two functions in the class:<br/> 
*sendImgRequest - converts image to base64 encoded string and sends request.<br/>
*sendStrRequest - encode string to base64 format and sends request.
*/
public class SIHTTPRequest {

	private String data;
	private String baseSimpleIntelURL = "https://simpleintelligence.com/ServingWebService/rest/apis/model/";
	private String fullSimpleIntelURL;
	
	/**
	 * Constructs URL for HTTP request and initializes data. 
	 * @param userKey - received from simpleintelligence.com.
	 * @param APItitle   - received from simpleintelligence.com.
	 * @param data       - path to your file or input string depend on used API.
	 */
	public SIHTTPRequest(String userKey, String APItitle, String data){

		this.data = data;
		this.fullSimpleIntelURL = baseSimpleIntelURL + APItitle + "/" + userKey;
	}
	
	/**
	 * Sends HTTP request with URL, created by constructor.
	 * @param inputString - Parsed input data.
	 * @return              Response from a server after HTTP request. 
	 * @throws Exception
	 */
	private String sendRequest(String inputString) throws Exception
	{
		//Create HTTP connection 
		URL url = new URL(fullSimpleIntelURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		
		//Define output stream
		OutputStream os = conn.getOutputStream();
		os.write(inputString.getBytes());
		os.flush();

		//Read input stream after request and build string to return  
		BufferedReader br = new BufferedReader(new InputStreamReader(
			(conn.getInputStream())));
		StringBuilder sb = new StringBuilder();
		String output;
		while ((output = br.readLine()) != null) {
			sb.append(output);
		}
		conn.disconnect();
		return sb.toString();
  	}
	
	/**
	 * This function converts image to base64 encoded string.
     * Creates appropriate JSON format and sends HTTP request.
	 * @return Response from a server after HTTP request. 
	 * @throws Exception
	 */
	public String sendImgRequest() throws Exception {
		File f = new File(this.data); 
		FileInputStream fis = new FileInputStream(f);
		byte byteArray[] = new byte[(int)f.length()];
		fis.read(byteArray);
		String imageString = Base64.getEncoder().encodeToString(byteArray);
		fis.close();
		String inputString = "{\"inputAsStrings\": [\"" + imageString + "\"]}";
		return sendRequest(inputString);
	}
	
	/**
	 * Encodes input string to base64 format and sends HTTP request as JSON. 
	 * @return Response from a server after HTTP request.
	 * @throws Exception
	 */
	public String sendStrRequest() throws Exception {
		String encodedinput = Base64.getEncoder().encodeToString(data.getBytes());
		String inputString = "{\"inputAsStrings\": [\"" + encodedinput + "\"]}";
		return sendRequest(inputString);
	}

}
