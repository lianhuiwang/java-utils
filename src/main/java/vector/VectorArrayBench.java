package vector;

public class VectorArrayBench {

public static final int DEFAULT_SIZE = 1024;
public static final int COLUMN_SIZE = 10;
  
  public static void testNoVector(int repetition){
    int[][] vectors1 = new int[DEFAULT_SIZE][COLUMN_SIZE];
    int[][] vectors2 = new int[DEFAULT_SIZE][COLUMN_SIZE];
    int[][] result = new int[DEFAULT_SIZE][COLUMN_SIZE];
    long start = System.nanoTime();
    for(int k =0 ; k < repetition; k++) {
      for (int i = 0; i < DEFAULT_SIZE; i++) {
        for (int j = 0 ; j < COLUMN_SIZE; j++) {
          result[i][j] = vectors1[i][j] + vectors2[i][j];
        }
      }
    }

    long end = System.nanoTime();
    System.out.println("Total time: " + (end - start) / 1000000.0 + " ms");
  }
  
  public static void testVector(int repetition){
    int[][] vectors1 = new int[COLUMN_SIZE][DEFAULT_SIZE];
    int[][] vectors2 = new int[COLUMN_SIZE][DEFAULT_SIZE];
    int[][] result = new int[COLUMN_SIZE][DEFAULT_SIZE];
    long start = System.nanoTime();
    for(int k =0 ; k < repetition; k++) {
      for (int i = 0; i < COLUMN_SIZE; i++) {
        for (int j = 0 ; j < DEFAULT_SIZE; j++) {
          result[i][j] = vectors1[i][j] + vectors2[i][j];
        }
      }
    }
    long end = System.nanoTime();
    System.out.println("Total time: " + (end - start) / 1000000.0 + " ms");
  }
  
  public static void main(String[] args) {
    // TODO Auto-generated method stub

    int repetition = 1 * 1024 * 1024;
    System.out.println("No Vectorized test.");
    testNoVector(repetition);
    System.out.println("Vectorized test.");
    testVector(repetition);
  }

}
