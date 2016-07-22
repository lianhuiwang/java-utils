package codegen;

import java.util.Iterator;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.ClassBodyEvaluator;

public class CodeGenerator {

  public<T> T compiler(Class<T> generatedClass, String className, 
      String javaName, String code) 
      throws InstantiationException, IllegalAccessException {
    ClassBodyEvaluator evaluator = new ClassBodyEvaluator();
    // evaluator.setParentClassLoader(this.getClass().getClassLoader());
    if( Thread.currentThread().getContextClassLoader() == null ) {
      Thread.currentThread().setContextClassLoader( this.getClass().getClassLoader() );
    }
    evaluator.setParentClassLoader( Thread.currentThread().getContextClassLoader() );
    evaluator.setClassName(className);
//    evaluator.setDefaultImports( new String[] {
//        "codegen.GeneratedClass"
//    });
//    evaluator.setDefaultImports(Array(
//      classOf[Platform].getName,
//      classOf[InternalRow].getName,
//      classOf[UnsafeRow].getName,
//      classOf[UTF8String].getName,
//      classOf[Decimal].getName,
//      classOf[CalendarInterval].getName,
//      classOf[ArrayData].getName,
//      classOf[UnsafeArrayData].getName,
//      classOf[MapData].getName,
//      classOf[UnsafeMapData].getName,
//      classOf[MutableRow].getName,
//      classOf[Expression].getName
//    ))
    evaluator.setExtendedClass(generatedClass);

    // System.out.println("code==" + code);

    try {
      evaluator.cook(javaName, code);
    } catch (CompileException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return (T) evaluator.getClazz().newInstance();
  }

  public static void main(String[] args) {
    CodeGenerator codeGen = new CodeGenerator();
    String code = "public Object generate(Object[] references) {\n" +
        "return references.length ;\n };" +
        "";
    String className = "codegen.GeneratedClassChild";
    try {
      GeneratedClass object = codeGen.compiler(GeneratedClass.class, className, 
          "generator.java", code);
      Integer[] references = {1, 2 ,3};
      System.out.println(object.generate(references));
      
    } catch (InstantiationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
