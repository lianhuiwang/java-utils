package vector;

public class LongColumnVector {

  public long[] vector;
  
  public LongColumnVector(int rowBatchSize) {
    vector = new long[rowBatchSize];
  }
}
