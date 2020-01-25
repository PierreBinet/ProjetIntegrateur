package fr.insa.proj.HBaseWS.controller;

import fr.insa.proj.HBaseWS.trips.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;

@RestController
public class TripRessource {
	
	@GetMapping(value = "/data", produces = ("text/csv"))
	public @ResponseBody Response getData (@RequestParam(value = "month") final int month,
			@RequestParam(value = "station_id") final int station_id){
		HBaseGetData hb = new HBaseGetData(month, station_id);
		hb.createCsv();
		File file = new File("trips.csv");
        ResponseBuilder response = Response.ok((Object) file);
        return response.build();
	}

	
}
