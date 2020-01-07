package fr.insa.soa;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("prediction")
public class PredictionFluxWS {
	
	@GET
	@Path("flow")
	@Produces("image/png")
	//@Produces(MediaType.TEXT_PLAIN)
	public Response getFlow (@QueryParam(value = "day") final int day,
		    @QueryParam(value = "station_id") final int station_id, 
		    @QueryParam("data") File data,
		    @QueryParam("station") File station){
		BufferedImage image = null;
		String s = null;
		try {
			//Process p = Runtime.getRuntime().exec("python3 ../../../../../../ML_researches/test_flux.py day station_id data station");
			Process p = Runtime.getRuntime().exec("python3 /home/constance/Documents/5A/Projet_Integrateur/ProjetIntegrateur/ML_researches/test_flux.py "+day+" "+station_id+" "+data+" "+station);
			
			
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			//BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			
	        s = stdInput.readLine();
	        System.out.println(s);
	       
	            
	        // read any errors from the attempted command
	        //while ((s = stdError.readLine()) != null) {
	           //System.out.println(s);
	        //}
			
		    image = ImageIO.read(new File(s));
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    ImageIO.write(image, "png", baos);
		    byte[] imageData = baos.toByteArray(); 
		    System.out.println(imageData);
		    
		    return Response.ok(imageData).build();
	       
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
}
