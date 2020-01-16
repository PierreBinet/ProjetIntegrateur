package controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.apache.commons.codec.binary.Base64;

import gui.HomePanel;
import javafx.scene.image.Image;


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
	
	
	
	public static BufferedImage MyGETRequest() throws IOException {

	    URL urlForGetRequest = new URL("http://localhost:8080/PredictionWS/insaRessources/prediction/flow?day=2&station_id=3184&data=/home/katran/Bureau/5SDBD/ProjetIntegrateur/RExtractor/output/JC-201811-citibike-tripdata.csv");
	    String readLine = null;
	    HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
	    conection.setRequestMethod("GET");
	    int responseCode = conection.getResponseCode();
	    
	    if (responseCode == HttpURLConnection.HTTP_OK) {
	        
	    	BufferedImage bf = ImageIO.read(conection.getInputStream());
	        
	        return bf;

	    } else {
	    	return null;
	    }

	}

}
