package hbase_trips;

import org.apache.hadoop.hbase.util.Bytes;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.*;
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
			// Instantiating configuration class
			Configuration config = HBaseConfiguration.create();

			// Instantiating HTable class
			HTable table = new HTable(config, "trips");

			// Instantiating the Scan class
			Scan scan = new Scan();
			/*
 		// filter
			 
 		BinaryComparator binlimite = new BinaryComparator(rawlimite);
 		SingleColumnValueFilter filtre = new SingleColumnValueFilter(rawfam, rawcol, CompareOp.GREATER_OR_EQUAL, binlimite);

		SingleColumnValueFilter filtre1 = ;
		QualifierFilter filtre2 = ;
		FilterList conjonction = new FilterList(FilterList.Operator.MUST_PASS_ALL, filtre1, filtre2);
		scan.setFilter(conjonction); */

			// Get the scan result
			ResultScanner scanner = table.getScanner(scan);

			// write labels in csv file
			this.fw.write("\"tripduration\",\"start_station_id\",\"end_station_id\",\"bikeid\",\"usertype\",\"birth_year\",\"gender\",\"start_day\",\"start_wday\",\"start_month\",\"start_year\",\"start_hour\",\"start_minute\",\"start_second\",\"end_day\",\"end_wday\",\"end_month\",\"end_year\",\"end_hour\",\"end_minute\",\"end_second\"\n");

			int count = 0;

			// Read values from scan result
			for (Result result : scanner) {
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

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws IOException {
		HBaseGetData hb = new HBaseGetData(11, 1);
		hb.createCsv();
	}


}
