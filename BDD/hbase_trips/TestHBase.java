package hbase_trips;

import java.io.IOException;

public class TestHBase {
	
	public static void main(String[] args) throws IOException {
		HBaseGetData hb = new HBaseGetData(11, 3183);
		hb.createCsv();
	}

}
