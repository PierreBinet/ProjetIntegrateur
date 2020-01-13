package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import gui.HomePanel;


public class RessourceManager {
	private HomePanel homePanel;
	
	//Constructor
	public void RessouceManager(){
		this.homePanel = new HomePanel(this);
	}
	
	public HomePanel getHomePanel() {
		return homePanel;
	}

	public void setHomePanel(HomePanel homePanel) {
		this.homePanel = homePanel;
	}

	public static String getFillRates( String day, String startStation, String tripsDatasetName, String stationsDatasetName) throws IOException {

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
