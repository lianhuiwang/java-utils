package bytecode;

/**
 * http://web.cs.ucla.edu/~msb/cs239-tutorial/
 * $ javac TestInstrumented.java
 * $ java -cp .:asm-all-5.0.3.jar org.objectweb.asm.util.ASMifier TestInstrumented
 * translate TestInstrumented into ASM API calls
 * @author lianhui
 *
 */
public class TestInstrumented{
    public static void main(String[] args) {
        System.err.println("CALL printOne");
        printOne();
        System.err.println("RETURN printOne");

        System.err.println("CALL printOne");
        printOne();
        System.err.println("RETURN printOne");

        System.err.println("CALL printTwo");
        printTwo();
        System.err.println("RETURN printTwo");
    }
    
    public static void printOne() {
        System.err.println("CALL println");
        System.out.println("Hello World");
        System.err.println("RETURN println");
    }
    
    public static void printTwo() {
        System.err.println("CALL printOne");
        printOne();
        System.err.println("RETURN printOne");

        System.err.println("CALL printOne");
        printOne();
        System.err.println("RETURN printOne");
    }
}
