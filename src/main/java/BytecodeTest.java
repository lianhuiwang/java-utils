
public class BytecodeTest {
  
  /**
   * http://blog.jamesdbloom.com/JavaCodeToByteCode_PartOne.html
   * http://blog.jamesdbloom.com/JavaCodeToByteCode_PartTwo.html
   * http://web.cs.ucla.edu/~msb/cs239-tutorial/
   */
  public static void main(String[] args) {
    System.out.println(factorial(5));
  }

  public static int factorial(int value) {
    if (value <= 0 || value == 1)
      return 1;

    return value * factorial(value - 1);
  }
}
