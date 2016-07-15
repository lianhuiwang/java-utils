
import java.io.UnsupportedEncodingException;
import java.util.Map;

//import org.rocksdb.RocksDB;
//import org.rocksdb.Options;
//import org.rocksdb.RocksDBException;
    
public class RocksdbTest {

  public static void main(String[] args) {
    // TODO Auto-generated method stub

    int count = 10000000;
    long start = System.currentTimeMillis();
    Map map = new java.util.HashMap<String, String>();
    for (int i= 0;i<= count;i++) {
      String key = "key" + i;
      String value = "val" + i;
      map.put(key, value);
    }
    long end1 = System.currentTimeMillis();
    System.out.println("map put=" + (end1 - start));
    
    /*
      // a static method that loads the RocksDB C++ library.
      RocksDB.loadLibrary();
      // the Options class contains a set of configurable DB options
      // that determines the behavior of a database.
      Options options = new Options().setCreateIfMissing(true);
      RocksDB db = null;
        // a factory method that returns a RocksDB instance
        db = RocksDB.open(options, "./rocksdb");
        start = System.currentTimeMillis();
        for (int i= 0;i<= count;i++) {
          String key = "key" + i;
          String value = "val" + i;
          db.put(key.getBytes("UTF-8"), value.getBytes("UTF-8"));
        }
        System.out.println("rocksdb put=" + (System.currentTimeMillis() - start));
  
        start = System.currentTimeMillis();
        for (int i= 0;i<= count;i++) {
          String key = "key" + i;
          String value =(String) map.get(key);
        }
        System.out.println("map gut=" + (System.currentTimeMillis() - start));
        
        start = System.currentTimeMillis();
        for (int i= 0;i<= count;i++) {
          String key = "key" + i;
          String value = db.get(key.getBytes("UTF-8")).toString();
        }
        System.out.println("rocksdb gut=" + (System.currentTimeMillis() - start));
        */
  }

}
