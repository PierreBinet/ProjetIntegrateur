package hbase_trips;

import org.apache.hadoop.hbase.util.Bytes;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.*;
import org.apache.hadoop.hbase.filter.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;

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
	      // Instantiating configuration class
	      Configuration config = HBaseConfiguration.create();
	      
	      // Instantiating HTable class
	      HTable table = new HTable(config, "trips");
	
	      // Instantiating the Scan class
	      Scan scan = new Scan();
	
	      // Scanning the required columns
	   /*   scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("tripduration"));
	      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("start_station_id"));
	      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("end_station_id"));
	      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("bikeid"));
	      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("usertype"));
	      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("birth.year"));
	      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("gender"));
	      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("start_day"));
	      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("start_wday"));
	      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("start_month"));
	      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("start_year"));
	      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("start_hour"));
	      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("start_minute"));
	      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("start_second"));
	      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("end_day"));
	      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("end_wday"));
	      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("end_month"));
	      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("end_year"));
	      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("end_hour"));
	      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("end_minute"));
	      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("end_second"));
	*/
/*
 		// filter
		SingleColumnValueFilter filtre1 = ;
		QualifierFilter filtre2 = ;
		FilterList conjonction = new FilterList(FilterList.Operator.MUST_PASS_ALL, filtre1, filtre2);
		scan.setFilter(conjonction); */
	      
	      // Get the scan result
	      ResultScanner scanner = table.getScanner(scan);
	      
	      //FileOutputStream f = new FileOutputStream(new File("trips.csv"));
			//ObjectOutputStream o = new ObjectOutputStream(f);
	
	      // write labels in csv file
	      this.fw.write("\"tripduration\",\"start_station_id\",\"end_station_id\",\"bikeid\",\"usertype\",\"birth_year\",\"gender\",\"start_day\",\"start_wday\",\"start_month\",\"start_year\",\"start_hour\",\"start_minute\",\"start_second\",\"end_day\",\"end_wday\",\"end_month\",\"end_year\",\"end_hour\",\"end_minute\",\"end_second\"\n");
	      
	      int count = 0;
	      // Read values from scan result
	      for (Result result : scanner) {
	    	  //System.out.println("Found row : " + result);
	    	  System.out.println("Found row : ");
				Trip trip = new Trip(result);
				trip.writeTripInFile(this.fw);
				count += 1;
				if (count==10) {
					break;
				}
	      }
	      
	      this.fw.close();
	      
	      //close the scanner
    	  scanner.close();
	      
	     // o.close();
			//f.close();
				
	      } catch (FileNotFoundException e) {
				System.out.println("File not found");
	      } catch (IOException e) {
				System.out.println("Error initializing stream");
	      }
	      
		}
	
	public static void main(String[] args) throws IOException {
		HBaseGetData hb = new HBaseGetData(11, 1);
		hb.createCsv();
	}

	
}
