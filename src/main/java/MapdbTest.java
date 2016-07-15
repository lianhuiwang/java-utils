import java.io.File;
import java.util.Random;
import java.util.concurrent.ConcurrentNavigableMap;

import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;


public class MapdbTest {

  public static void main(String[] args) {
    // TODO Auto-generated method stub

    DB db = DBMaker
        .newMemoryDB()
        //.newFileDB(new File("./mapdb.data"))
        .deleteFilesAfterClose()
        .asyncFlushDelay(1000)
        .make();
    
  //open an collection, TreeMap has better performance then HashMap
    ConcurrentNavigableMap<Integer,Long[]> map = db.getTreeMap("collectionName");
    int count = 10000000;
    Random r = new Random();

    long start = System.currentTimeMillis();
    for (int i= 0;i<= count;i++) {
      String key = "key" + i;
      Long[] values = new Long[5000];
      for(int j=0; j<5000; j++){
        values[j] = r.nextLong();
      }
  
      String value = "val" + i;
      if (i%100000 == 0) {
        System.out.println("insert i=" + i);
      }
      map.put(i, values);
//      db.commit();  //persist changes into disk
    }
    
    System.out.println("mapdb put=" + (System.currentTimeMillis() - start));


     start = System.currentTimeMillis();
    for (int i= 0;i<= count;i++) {
      String key = "key" + i;
      //String value = "val" + i;
//      if (i%100000 == 0) {
//        System.out.println("insert i=" + i);
//      }
      Long[] value = map.get(i);
//      db.commit();  //persist changes into disk
    }
    
    System.out.println("mapdb get=" + (System.currentTimeMillis() - start));

    db.close();
    
  }

}
