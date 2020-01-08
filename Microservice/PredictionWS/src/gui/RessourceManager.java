package gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class RessourceManager {
	
	public void RessouceManager(){
		
	}
	
	
	public static String MyGETRequest() throws IOException {

	    URL urlForGetRequest = new URL("http://localhost:8080/PredictionWS/insaRessources/prediction");

	    String readLine = null;

	    HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();

	    conection.setRequestMethod("GET");

	    int responseCode = conection.getResponseCode();

	    if (responseCode == HttpURLConnection.HTTP_OK) {

	        BufferedReader in = new BufferedReader(

	            new InputStreamReader(conection.getInputStream()));

	        StringBuffer response = new StringBuffer();

	        while ((readLine = in .readLine()) != null) {

	            response.append(readLine);

	        } in .close();


	        return response.toString();

	    } else {

	    	return "GET NOT WORKED";

	    }

	}
}
