package hbase_trips;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.*;

/* trip row : "tripduration","start_station_id","end_station_id","bikeid",
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
	protected String usertype;
	protected String birth_year;
	protected String gender;
	protected String start_day;
	protected String start_wday;
	protected String start_month;
	protected String start_year;
	protected String start_hour;
	protected String start_minute;
	protected String start_second;
	protected String end_day;
	protected String end_wday;
	protected String end_month;
	protected String end_year;
	protected String end_hour;
	protected String end_minute;
	protected String end_second;
	
	protected byte[] rawfam = Bytes.toBytes("info");
	
	public Trip(Result result) {
		this.tripduration = Bytes.toString(result.getValue(this.rawfam, Bytes.toBytes("tripduration")));
		this.start_station_id = Bytes.toString(result.getValue(this.rawfam, Bytes.toBytes("start_station_id")));
		this.end_station_id = Bytes.toString(result.getValue(this.rawfam, Bytes.toBytes("end_station_id")));
		this.bikeid = Bytes.toString(result.getValue(this.rawfam, Bytes.toBytes("bikeid")));
		this.usertype = Bytes.toString(result.getValue(this.rawfam, Bytes.toBytes("usertype")));
		this.birth_year = Bytes.toString(result.getValue(this.rawfam, Bytes.toBytes("birth_year")));
		this.gender = Bytes.toString(result.getValue(this.rawfam, Bytes.toBytes("gender")));
		this.start_day = Bytes.toString(result.getValue(this.rawfam, Bytes.toBytes("start_day")));
		this.start_wday = Bytes.toString(result.getValue(this.rawfam, Bytes.toBytes("start_wday")));
		this.start_month = Bytes.toString(result.getValue(this.rawfam, Bytes.toBytes("start_month")));
		this.start_year = Bytes.toString(result.getValue(this.rawfam, Bytes.toBytes("start_year")));
		this.start_hour = Bytes.toString(result.getValue(this.rawfam, Bytes.toBytes("start_hour")));
		this.start_minute = Bytes.toString(result.getValue(this.rawfam, Bytes.toBytes("start_minute")));
		this.start_second = Bytes.toString(result.getValue(this.rawfam, Bytes.toBytes("start_second")));
		this.end_day = Bytes.toString(result.getValue(this.rawfam, Bytes.toBytes("end_day")));
		this.end_wday = Bytes.toString(result.getValue(this.rawfam, Bytes.toBytes("end_wday")));
		this.end_month = Bytes.toString(result.getValue(this.rawfam, Bytes.toBytes("end_month")));
		this.end_year = Bytes.toString(result.getValue(this.rawfam, Bytes.toBytes("end_year")));
		this.end_hour = Bytes.toString(result.getValue(this.rawfam, Bytes.toBytes("end_hour")));
		this.end_minute = Bytes.toString(result.getValue(this.rawfam, Bytes.toBytes("end_minute")));
		this.end_second = Bytes.toString(result.getValue(this.rawfam, Bytes.toBytes("end_second")));
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
