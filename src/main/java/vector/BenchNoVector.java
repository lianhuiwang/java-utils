package vector;

public class BenchNoVector extends Benchmark<Long[][]> {

  public BenchNoVector() {
    super();
  }
  
  protected Long[][] dataGen() {
    Long[][] rows = new Long[rowCount][colCount];
    for (int i = 0; i < rowCount; i++) {
      for (int j = 0 ; j < colCount; j++) {
        rows[i][j] = baseValues[j] + i;
      }  
    }
    return rows;
  }

  @Override
  public void col1AddCol3() {
    Object[] results = new Object[rowBatches.length];
    long start = System.nanoTime();
    for (int j = 0; j < repetition; j ++) {
      for (int i = 0; i < rowBatches.length; i ++) {
        results[i] = rowBatches[i][0] + rowBatches[i][2];
        // System.out.println(results[i]);
      }
    }
    long end = System.nanoTime();
    System.out.println("Total time: " + (end - start) / 1000000.0 /repetition + " ms/repetition");
  }

  @Override
  public void col1AddCol3AddOne() {
    Object[] results = new Object[rowBatches.length];
    long temp1;
    long temp2;
    long temp3;
    long temp4;
    long temp5;
    long temp6;
    long start = System.nanoTime();
    for (int j = 0; j < repetition; j ++) {
      for (int i = 0; i < rowBatches.length; i ++) {
        temp1 = rowBatches[i][0] + 5;
        temp2 = rowBatches[i][2] + 1;
        temp3 = rowBatches[i][0] + 5;
        temp4 = rowBatches[i][2] + 1;
        temp5 = rowBatches[i][0] + 5;
        temp6 = rowBatches[i][2] + 1;
        results[i] = temp1 + temp2 + temp3 + temp4 + temp5 + temp6;
        // System.out.println(results[i]);
      }
    }
    long end = System.nanoTime();
    System.out.println("Total time: " + (end - start) / 1000000.0 /repetition + " ms/repetition");
  }

  @Override
  public void col1AddCol3AddOne2() {
    // TODO Auto-generated method stub
    
  }
  
}
