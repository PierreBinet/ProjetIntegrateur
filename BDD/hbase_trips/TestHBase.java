package hbase_trips;

import java.io.IOException;

public class TestHBase {
	
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
            System.out.println("Usage : java TestHBase <month> <station_id>");
            System.exit(0);
        }
		int month = Integer.parseInt(args[0]);
		int station_id = Integer.parseInt(args[1]);
		HBaseGetData hb = new HBaseGetData(month, station_id);
		hb.createCsv();
	}

}
