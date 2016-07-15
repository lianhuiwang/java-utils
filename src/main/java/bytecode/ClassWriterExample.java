package bytecode;

import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicLong;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ClassWriterExample {

  private static final AtomicLong CLASS_ID = new AtomicLong();

  public static class Foo {
    public static void execute1() {
      System.out.println("Foo.execute1()");
    }
    
    public void execute() {
      System.out.println("Foo.execute()");
    }
  }
  
  public static void generateStaticMethod() {
    ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
    String className =
        ClassWriterExample.class.getPackage().getName() + "/GenTest"
            + CLASS_ID.incrementAndGet();
    
    classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, className, null,
        Type.getInternalName(Object.class), null);
    
    // add execute() contents
    MethodVisitor methodvisitor =
        classWriter.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
            "execute", "()V", null, null);
    
    methodvisitor.visitFieldInsn(Opcodes.GETSTATIC, Type.getInternalName(System.class),
        "out", Type.getDescriptor(PrintStream.class));
    methodvisitor.visitLdcInsn("Before execute");
    methodvisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
        Type.getInternalName(PrintStream.class), "println",
        "(Ljava/lang/String;)V");
    
    // static methodFoo.execute1();
//    methodvisitor.visitMethodInsn(Opcodes.INVOKESTATIC, 
//        "bytecode/ClassWriteExample$Foo", "execute1", "()V");
    
//    Foo foo = new Foo();
//    foo.execut();
    
    methodvisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out",
        "Ljava/io/PrintStream;");
    methodvisitor.visitLdcInsn("End execute");
    methodvisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream",
        "println", "(Ljava/lang/String;)V");

    methodvisitor.visitInsn(Opcodes.RETURN);
    methodvisitor.visitMaxs(-1, -1);
    methodvisitor.visitEnd();

    DynamicClassLoader classLoader = new DynamicClassLoader();

    Class<?> clazz =
        classLoader.defineClass(className.replace('/', '.'), classWriter.toByteArray());
    try {
      clazz.getMethod("execute").invoke(null);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    ClassWriterExample.generateStaticMethod();
  }
}
