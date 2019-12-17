import java.io.IOException;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.TableName;

import org.apache.hadoop.conf.Configuration;

public class CreateTables {
      
   public static void main(String[] args) throws IOException {

      // Instantiating configuration class
      Configuration con = HBaseConfiguration.create();

      // Instantiating HbaseAdmin class
      HBaseAdmin admin = new HBaseAdmin(con);

      // Instantiating table descriptor class
      HTableDescriptor tableDescriptorTrip = new TableDescriptor(TableName.valueOf("trip"));
      HTableDescriptor tableDescriptorStation = new TableDescriptor(TableName.valueOf("station"));
      
      // Adding column families to table descriptor
      tableDescriptorTrip.addFamily(new HColumnDescriptor("months"));
      tableDescriptorStation.addFamily(new HColumnDescriptor("station_csv"));

      // Execute the table through admin
      admin.createTable(tableDescriptorTrip);
      admin.createTable(tableDescriptorStation);
      System.out.println(" Tables created ");
   }
}