package bytecode;

import java.io.PrintStream;

import org.objectweb.asm.*;

/**
 * https://en.wikipedia.org/wiki/Java_bytecode_instruction_listings
 * https://github.com/eroshenkoam/asm-examples
 * javap -c *.class
 * @author lianhui
 *
 */
public class HelloWorldExample {

  private static String HELLO_WORLD_CLASS_NAME = "bytecode/HelloWorld";

  public static Class<?> generateHelloWorldClassViaMethodVisitor(String message) {
    ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
    classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, HELLO_WORLD_CLASS_NAME,
        null, Type.getInternalName(Object.class), null);

//    MethodVisitor constructor = 
//        classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
//    constructor.visitVarInsn(Opcodes.ALOAD, 0);
//    constructor.visitMethodInsn(Opcodes.INVOKESPECIAL,
//        Type.getInternalName(Object.class), "<init>", "()V");
//    constructor.visitInsn(Opcodes.RETURN);
//    constructor.visitMaxs(1, 1);
//    constructor.visitEnd();
    
    MethodVisitor main =
      classWriter.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
          "execute", "()V", null, null);

    main.visitFieldInsn(Opcodes.GETSTATIC, Type.getInternalName(System.class),
        "out", Type.getDescriptor(PrintStream.class));
    main.visitLdcInsn(message);
    main.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
        Type.getInternalName(PrintStream.class), "println",
        "(Ljava/lang/String;)V");
    main.visitInsn(Opcodes.RETURN);
    main.visitMaxs(2, 2);
    main.visitEnd();

    DynamicClassLoader classLoader = 
        new DynamicClassLoader(HelloWorldExample.class.getClassLoader());
    return classLoader.defineClass(HELLO_WORLD_CLASS_NAME.replace('/', '.'),
        classWriter.toByteArray());
  }

  public static void main(String[] args) {
    Class<?> clazz =
        HelloWorldExample.generateHelloWorldClassViaMethodVisitor("Hello, World");
    try {
      clazz.getMethod("execute").invoke(null);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
