package vector;

public class BenchVector extends Benchmark<VectorizedRowBatch[]> {

  private int rowBatchSize;
  
  public BenchVector() {
    super();
  }
  
  protected VectorizedRowBatch[] dataGen() {
    rowBatchSize = VectorizedRowBatch.DEFAULT_SIZE; // 1024
    int numRowBatches = rowCount / rowBatchSize;
    VectorizedRowBatch[] rowBatches = new VectorizedRowBatch[numRowBatches];
    for (int i = 0; i < numRowBatches; i++) {
      rowBatches[i] = new VectorizedRowBatch(colCount + 1, rowBatchSize);
      for (int j = 0 ; j < colCount; j++) {
        LongColumnVector columnVector = new LongColumnVector(rowBatchSize);
        for (int k = 0; k < rowBatchSize; k ++) {
          columnVector.vector[k] = baseValues[j] + i * rowBatchSize + k;
        }
        rowBatches[i].cols[j] = columnVector;
      }
      // Set up the last column for storing results.
      LongColumnVector columnVector = new LongColumnVector(rowBatchSize);
      rowBatches[i].cols[colCount] = columnVector;
    }    
    return rowBatches;
  }
  
  @Override
  public void col1AddCol3() {
    long start = System.nanoTime();
    for (int j = 0; j < repetition; j ++) {
      for (int i = 0; i < rowBatches.length; i ++) {
        for (int k = 0; k < rowBatchSize; k ++) {
          rowBatches[i].cols[colCount].vector[k] = rowBatches[i].cols[0].vector[k]
              + rowBatches[i].cols[2].vector[k];
          // System.out.println(rowBatches[i].cols[colCount].vector[k]);
        }
      }
    }
    long end = System.nanoTime();
    System.out.println("Total time: " + (end - start) / 1000000.0 /repetition + " ms/repetition");
  }
  
}
