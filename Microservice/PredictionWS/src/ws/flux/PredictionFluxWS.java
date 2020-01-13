package ws.flux;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("prediction")
public class PredictionFluxWS {
	
	//http://localhost:8080/PredictionFluxWS/insaRessources/prediction/flow?day=2&station_id=3184&data=/home/constance/Documents/5A/Projet_Integrateur/ProjetIntegrateur/RExtractor/output/JC-201811-citibike-tripdata.csv&station=/home/constance/Documents/5A/Projet_Integrateur/ProjetIntegrateur/RExtractor/output/stationTable.csv
	
	@GET
	@Path("flow")
	@Produces("image/png")
	//@Produces(MediaType.TEXT_PLAIN)
	public Response getFlow (@QueryParam(value = "day") final int day,
		    @QueryParam(value = "station_id") final int station_id, 
		    @QueryParam("data") File data,
		    @QueryParam("station") File station){
		String str = null;
		try {
			//Process p = Runtime.getRuntime().exec("python3 ../../../../../../ML_researches/test_flux.py day station_id data station");
			Process p = Runtime.getRuntime().exec("python3 /home/constance/Documents/5A/Projet_Integrateur/ProjetIntegrateur/ML_researches/test_flux.py "+day+" "+station_id+" "+data+" "+station);
		
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			//BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			
			//retrieve the hexa string returned by the python program
	        str = stdInput.readLine();
	        // read any errors from the attempted command
	        //while ((s = stdError.readLine()) != null) {
	           //System.out.println(s);
	        //}
	        
	        //convert the hexa string into a byte array
	        byte[] val = new byte[str.length() / 2];
	        for (int i = 0; i < val.length; i++) {
	           int index = i * 2;
	           int j = Integer.parseInt(str.substring(index, index + 2), 16);
	           val[i] = (byte) j;
	        }
	      
	        //return the png image
		    return Response.ok(val).build();
	       
		} catch (IOException e) {
			return null;
		}
	   
	}
		
	@GET
	@Path("test")
	@Produces(MediaType.TEXT_PLAIN)
	public String getLongueur(@QueryParam(value = "day") final int day,
		    @QueryParam(value = "station_id") final int station_id, 
		    @QueryParam("data") File data,
		    @QueryParam("station") File station){
			  
		int i;
		try {
			FileInputStream fin=new FileInputStream(data);  
			i = fin.read();
			fin.close();
		
			String ans = "Day is : "+ day+" station is: "+station_id+ " data : "+ data.getName();
			ans = ans + " gfdsg "+ (char)i;
			return ans;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getInt(){
		return "200";
	}
}
