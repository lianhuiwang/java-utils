package vector;

public class VectorizedRowBatch {

  public static int DEFAULT_SIZE = 1024;
  public LongColumnVector[] cols;
  public int rowBatchSize;
  
  // from Hive
  public int numCols;
  public int size;              // number of rows that qualify (i.e. haven't been filtered out)
  public int[] selected;        // array of positions of selected values
  public int[] projectedColumns;
  public int projectionSize;

  private int dataColumnCount;
  private int partitionColumnCount;
  
  public VectorizedRowBatch(int colCount, int rowBatchSize) {
    this.cols = new LongColumnVector[colCount];
    this.rowBatchSize = rowBatchSize;
  }
}
