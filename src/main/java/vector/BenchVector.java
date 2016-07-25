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
        OnHeapColumnVector columnVector = new OnHeapColumnVector(rowBatchSize);
        for (int k = 0; k < rowBatchSize; k ++) {
          columnVector.vector[k] = baseValues[j] + i * rowBatchSize + k;
        }
        rowBatches[i].cols[j] = columnVector;
      }
      // Set up the last column for storing results.
      OnHeapColumnVector columnVector = new OnHeapColumnVector(rowBatchSize);
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

  @Override
  public void col1AddCol3AddOne() {
    long start = System.nanoTime();
    long temp1;
    long temp2;
    long temp3;
    long temp4;
    long temp5;
    long temp6;
    for (int j = 0; j < repetition; j ++) {
      for (int i = 0; i < rowBatches.length; i ++) {
        for (int k = 0; k < rowBatchSize; k ++) { 
          temp1 = rowBatches[i].cols[0].vector[k] + 5;
          temp2 = rowBatches[i].cols[2].vector[k] + 1;
          temp3 = rowBatches[i].cols[0].vector[k] + 5;
          temp4 = rowBatches[i].cols[2].vector[k] + 1;
          temp5 = rowBatches[i].cols[0].vector[k] + 5;
          temp6 = rowBatches[i].cols[2].vector[k] + 1;
          rowBatches[i].cols[colCount].vector[k] = 
              temp1 + temp2 + temp3 + temp4 + temp5 + temp6;
          // System.out.println(rowBatches[i].cols[colCount].vector[k]);
        }
      }
    }
    long end = System.nanoTime();
    System.out.println("Total time: " + (end - start) / 1000000.0 /repetition + " ms/repetition");
  }

  @Override
  public void col1AddCol3AddOne2() {
    long start = System.nanoTime();
    long[] temp1 = new long[rowBatchSize];
    long[] temp2 = new long[rowBatchSize];
    long[] temp3 = new long[rowBatchSize];
    long[] temp4 = new long[rowBatchSize];
    long[] temp5 = new long[rowBatchSize];
    long[] temp6 = new long[rowBatchSize];
    for (int j = 0; j < repetition; j ++) {
      for (int i = 0; i < rowBatches.length; i ++) {
        for (int k = 0; k < rowBatchSize; k ++) { 
          temp1[k] = rowBatches[i].cols[0].vector[k] + 5;
        }
        for (int k = 0; k < rowBatchSize; k ++) { 
          temp2[k] = rowBatches[i].cols[2].vector[k] + 1;
        }
        for (int k = 0; k < rowBatchSize; k ++) { 
          temp3[k] = rowBatches[i].cols[0].vector[k] + 5;
        }
        for (int k = 0; k < rowBatchSize; k ++) { 
          temp4[k] = rowBatches[i].cols[2].vector[k] + 1;
        }
        for (int k = 0; k < rowBatchSize; k ++) { 
          temp5[k] = rowBatches[i].cols[0].vector[k] + 5;
        }
        for (int k = 0; k < rowBatchSize; k ++) { 
          temp6[k] = rowBatches[i].cols[2].vector[k] + 1;
        }
        
        for (int k = 0; k < rowBatchSize; k ++) { 
          rowBatches[i].cols[colCount].vector[k] = 
              temp1[k] + temp2[k] + temp3[k] + temp4[k] + temp5[k] + temp6[k];
          // System.out.println(rowBatches[i].cols[colCount].vector[k]);
        }
      }
    }
    long end = System.nanoTime();
    System.out.println("Total time: " + (end - start) / 1000000.0 /repetition + " ms/repetition");
  }
  
}
