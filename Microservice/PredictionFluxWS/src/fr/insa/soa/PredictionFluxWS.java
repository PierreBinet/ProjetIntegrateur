package fr.insa.soa;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("prediction")
public class PredictionFluxWS {
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getLongueur(){
		return "Hey!";
	}
	
}
