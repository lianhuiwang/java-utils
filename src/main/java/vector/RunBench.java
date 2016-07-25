package vector;

public class RunBench {

  public static void main(String[] args) {
    System.out.println("Vectorized evaluation.");
    System.out.println("=================================");
    Benchmark vector = new BenchVector();
    vector.col1AddCol3();
    
    System.out.println("Vectorized evaluation.");
    System.out.println("=================================");
    Benchmark vector2 = new BenchVector();
    vector2.col1AddCol3();
    
    System.out.println("No Vector evaluation.");
    System.out.println("=================================");
    Benchmark noVector = new BenchNoVector();
    noVector.col1AddCol3();
  }
}
