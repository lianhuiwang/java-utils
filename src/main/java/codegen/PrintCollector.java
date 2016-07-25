package codegen;

import vector.VectorizedRowBatch;

public class PrintCollector implements Collector {

  private int limit = 0;
  private boolean isPrint = false;
  private int count = 0;
  private boolean isVector;
  
  public int resultCount = 0;
  
  public PrintCollector(int limit, boolean isPrint) {
    this(limit, isPrint, false);
  }
  
  public PrintCollector(int limit, boolean isPrint, boolean isVector) {
    this.limit = limit;
    this.isPrint = isPrint;
    this.count = 0;
    this.isVector = isVector;
    this.resultCount = 0;
  }
  
  @Override
  public void init() {
    // TODO Auto-generated method stub
  }

  @Override
  public void setup(Collector outputCollector) {
    // TODO Auto-generated method stub
  }

  @Override
  public void collect(Object record) {
     if (isPrint) {
       if (this.isVector) {
         VectorizedRowBatch row = (VectorizedRowBatch) record;
         this.resultCount += row.numRows();
         if (this.count < this.limit) {
           Utils.printVector(row, limit);
           this.count = this.limit;
         }
       } else {
         this.resultCount += 1;
         if (this.count < this.limit) {
           this.count += 1;
           System.out.println("record==" + record);
         }
       }
    }
  }

  @Override
  public void processRecord() {
    // TODO Auto-generated method stub

  }

}
