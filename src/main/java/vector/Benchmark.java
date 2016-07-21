package vector;

public abstract class Benchmark<T> {

  protected int rowCount = 1 * 1024 * 1024;
  protected int colCount = 4;
  
  protected final long[] baseValues;
  protected T rowBatches;
  
  protected final int repetition = 100;
  
  public Benchmark() {
    baseValues = new long[colCount];
    for (int i = 0; i < colCount; i++) {
      baseValues[i] = i + 1;
    }
    long start = System.nanoTime();
    this.rowBatches = dataGen();
    long end = System.nanoTime();
    System.out.println("Total time: " + (end - start) / 1000000.0 + " ms for dataGen");
  }
  
  protected abstract T dataGen();
  
  // evaluate col_1 + col_3
  public abstract void col1AddCol3();
  
  //evaluate col_1 + col_3
  public abstract void col1AddCol3AddOne();
  
  //evaluate col_1 + col_3
  public abstract void col1AddCol3AddOne2();
  
}
