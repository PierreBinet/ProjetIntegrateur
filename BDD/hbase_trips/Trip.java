package hbase_trips;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.*;

/* row : "tripduration","start_station_id","end_station_id","bikeid",
 * "usertype","birth_year","gender",
 * "start_day","start_wday","start_month","start_year","start_hour","start_minute","start_second",
 * "end_day","end_wday","end_month","end_year","end_hour","end_minute","end_second",
 * KEY
*/

public class Trip {
	protected String tripduration;
	protected String start_station_id;
	protected String end_station_id;
	protected String bikeid;
	protected static byte[] rawfam = Bytes.toBytes("info");
	
	public Trip(Result result) {
		this.tripduration = Bytes.toString(result.getValue(this.rawfam, Bytes.toBytes("tripduration")));
		this.start_station_id = Bytes.toString(result.getValue(this.rawfam, Bytes.toBytes("start_station_id")));
		this.end_station_id = Bytes.toString(result.getValue(this.rawfam, Bytes.toBytes("end_station_id")));
		this.bikeid = Bytes.toString(result.getValue(this.rawfam, Bytes.toBytes("bikeid")));
	}
	
	public Trip(String tripduration, String start_station_id, String end_station_id, String bikeid) {
		this.tripduration = tripduration;
		this.start_station_id = start_station_id;
		this.end_station_id = end_station_id;
		this.bikeid = bikeid;
	}
	
	public void writeTripInFile(FileWriter fw) {
		try {
			fw.write(this.tripduration + ",");
			fw.write(this.start_station_id + ",");
			fw.write(this.end_station_id + ",");
			fw.write(this.bikeid + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
