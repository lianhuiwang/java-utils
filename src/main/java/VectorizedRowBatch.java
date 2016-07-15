
public class VectorizedRowBatch {

  public static final int DEFAULT_SIZE = 1024;
  
  public static void testNoVector(int time){
    long start = System.nanoTime();
    int column_size = 100;
    int[][] vectors1 = new int[DEFAULT_SIZE][column_size];
    int[][] vectors2 = new int[DEFAULT_SIZE][column_size];
    int[][] result = new int[DEFAULT_SIZE][column_size];
    for(int t =0 ; t < time; t++) {
      for (int i = 0; i < DEFAULT_SIZE; i++) {
        for (int j = 0 ; j < column_size; j++) {
          result[i][j] = vectors1[i][j] + vectors2[i][j];
        }
      }
    }

    System.out.println((System.nanoTime()) - start);
  }
  
  public static void testVector(int time){
    long start = System.nanoTime();
    int column_size = 100;
    int[][] vectors1 = new int[column_size][DEFAULT_SIZE];
    int[][] vectors2 = new int[column_size][DEFAULT_SIZE];
    int[][] result = new int[column_size][DEFAULT_SIZE];
    for(int t =0 ; t < time; t++) {
      for (int i = 0; i < column_size; i++) {
        for (int j = 0 ; j < DEFAULT_SIZE; j++) {
          result[i][j] = vectors1[i][j] + vectors2[i][j];
        }
      }
    }

    System.out.println((System.nanoTime()) - start);
  }
  
  public static void main(String[] args) {
    // TODO Auto-generated method stub

    int time = 10000;
    testNoVector(time);
    testVector(time);
  }

}
