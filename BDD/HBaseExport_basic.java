import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.util.Bytes;

import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;

import org.apache.hadoop.conf.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class HBaseExport_basic {
	int month;
	int station;
	
	public HBaseExport_basic(int month, int station) {
		this.month = month;
		this.station = station;
	}

	public void run() throws IOException {
      // Instantiating configuration class
      Configuration config = HBaseConfiguration.create();
      
      // Instantiating HTable class
      HTable table = new HTable(config, "trips");

      // Instantiating the Scan class
      Scan scan = new Scan();

      // Scanning the required columns
    /*  scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("tripduration"));
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
      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("end_second"));*/

      // Getting the scan result
      ResultScanner scanner = table.getScanner(scan);

      // Reading values from scan result
      for (Result result = scanner.next(); result != null; result = scanner.next()) {
    	  System.out.println("Found row : " + result);
      }

      //closing the scanner
	  scanner.close();
	      
	  }
	
	public static void main(String[] args) throws IOException {
		HBaseExport_basic exportObj = new HBaseExport_basic(11, 1);
		exportObj.run();
	}

	
}
