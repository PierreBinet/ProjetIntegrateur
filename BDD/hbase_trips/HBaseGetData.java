package hbase_trips;

import org.apache.hadoop.hbase.util.Bytes;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;

import java.io.FileWriter;
import java.io.IOException;

public class HBaseGetData {
	private int month;
	private int station;
	private FileWriter fw;

	public HBaseGetData(int month, int station) {
		this.month = month;
		this.station = station;

		try {
			this.fw = new FileWriter("trips.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createCsv() {

		try {
			
			// instantiate the trips table
			Configuration config = HBaseConfiguration.create();			
			HTable table = new HTable(config, "trips");

			// instantiate a scan
			Scan scan = new Scan();
			
	 		/* filter the station and the month */
			String fam = "info";
			
			// filter start OR end station
			Filter filter_start_station = new SingleColumnValueFilter(Bytes.toBytes(fam), Bytes.toBytes("start_station_id"),
	                CompareFilter.CompareOp.EQUAL, Bytes.toBytes(String.valueOf(this.station)));
	        Filter filter_end_station = new SingleColumnValueFilter(Bytes.toBytes(fam), Bytes.toBytes("end_station_id"),
	                CompareFilter.CompareOp.EQUAL, Bytes.toBytes(String.valueOf(this.station)));
	        FilterList filterListStation = new FilterList(FilterList.Operator.MUST_PASS_ONE);
	        filterListStation.addFilter(filter_start_station);
	        filterListStation.addFilter(filter_end_station);
	        
	        // filter station AND month
	        Filter filter_month = new SingleColumnValueFilter(Bytes.toBytes(fam), Bytes.toBytes("start_month"),
	                CompareFilter.CompareOp.EQUAL, Bytes.toBytes(String.valueOf(this.month)));
	        FilterList filterListGlobal = new FilterList(FilterList.Operator.MUST_PASS_ALL);
	        filterListGlobal.addFilter(filter_month);
	        filterListGlobal.addFilter(filterListStation);
	        
	        // add the filter to the scan
	        scan.setFilter(filterListGlobal); 
	        
			// get the scan result
			ResultScanner scanner = table.getScanner(scan);

			// write labels in csv file
			this.fw.write("\"tripduration\",\"start_station_id\",\"end_station_id\",\"bikeid\",\"usertype\",\"birth_year\",\"gender\",\"start_day\",\"start_wday\",\"start_month\",\"start_year\",\"start_hour\",\"start_minute\",\"start_second\",\"end_day\",\"end_wday\",\"end_month\",\"end_year\",\"end_hour\",\"end_minute\",\"end_second\"\n");

			// write values from scan result, in csv file
			for (Result result : scanner) {
				Trip trip = new Trip(result);
				trip.writeTripInFile(this.fw);
			}

			// close
			this.fw.close();
			scanner.close();
			table.close();
			
			System.out.println("Trips from/to the station " + station + " in month " + month);
			System.out.println(" written in file named trips.csv");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
