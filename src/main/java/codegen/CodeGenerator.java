package codegen;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.commons.compiler.CompilerFactoryFactory;
import org.codehaus.commons.compiler.IClassBodyEvaluator;
import org.codehaus.janino.ClassBodyEvaluator;
import org.codehaus.janino.ExpressionEvaluator;
import org.codehaus.janino.SimpleCompiler;

public class CodeGenerator {
  
  public static Map<String, GeneratedClass> cached = new HashMap<String, GeneratedClass>();
  public static int classId = 0;
  
  public static GeneratedClass doCompiler(String code) throws Exception {
    return CodeGenerator.doCompiler(code, true);
  }
  
  public static GeneratedClass doCompiler(String code, boolean isCached) throws Exception {
    if (cached.containsKey(code) && isCached) {
      return (GeneratedClass) cached.get(code);
    } else {
      classId += 1;
      CodeGenerator codeGen = new CodeGenerator();
      String className = "codegen.GeneratedClassChild" + classId;
      // System.out.println("code=" + code);
      long start = System.nanoTime();
      // TODO optimize code.compiler() max 1220.260721 ms, min 11ms, avg 30ms
      GeneratedClass object = codeGen.compiler(GeneratedClass.class, className, 
          "generator.java", code);
      long end = System.nanoTime();
      System.out.println("Total time: " + (end - start) / 1000000.0 + " ms for compiler");
      cached.put(code, object);
      return object;
    }
  }

  public<T> T compiler(Class<T> generatedClass, String className, 
      String javaName, String code) 
      throws Exception {
    long start1 = System.nanoTime();
    ClassBodyEvaluator evaluator = new ClassBodyEvaluator();
    // evaluator.setParentClassLoader(this.getClass().getClassLoader());
    long end1 = System.nanoTime();
    System.out.println("Total time: " + (end1 - start1) / 1000000.0 + " ms ClassBodyEvaluator");
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
//    ))
    evaluator.setExtendedClass(generatedClass);
    
    // System.out.println("code==" + code);

    try {
      long start = System.nanoTime();
      evaluator.cook(javaName, code);
      long end = System.nanoTime();
      System.out.println("Total time: " + (end - start) / 1000000.0 + " ms cook");
    } catch (CompileException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    long start2= System.nanoTime();
    T object = (T) evaluator.getClazz().newInstance();
    long end2 = System.nanoTime();
    System.out.println("Total time: " + (end2 - start2) / 1000000.0 + " ms newInstance");
    return object;
  }
  
  public static void doDemo() {
    CodeGenerator codeGen = new CodeGenerator();
    String code = "public Object generate(Object[] references) {\n" +
        "return references.length ;\n };" +
        "";
    try {
      GeneratedClass object = CodeGenerator.doCompiler(code, false);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static void doExpressionEval() {
    ExpressionEvaluator ee = new ExpressionEvaluator();
    try {
      long start = System.nanoTime();
      ee.cook("3 + 4");
      System.out.println(ee.evaluate(null));
      long end = System.nanoTime();
      System.out.println("Total time: " + (end - start) / 1000000.0 + " ms ExpressionEval");
    } catch (CompileException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }

  public static void main(String[] args) {
    for (int i = 0; i < 10; i++) {
      CodeGenerator.doDemo();
      CodeGenerator.doExpressionEval();
    }
    
  }
}
