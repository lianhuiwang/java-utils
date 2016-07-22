package codegen;

public class CodegenExec extends CodegenSupport{
  
  public CodegenExec(CodegenSupport child) {
    super(child);
  }

  @Override
  public String doConsume(CodegenContext ctx, ExprCode[] input) {
    System.out.println("CodegenExec doConsume()");
    ctx.currentVars = input;
    String isNull = ctx.freshName("isNull");
    String value = ctx.freshName("value");
    //String thisValue = ctx.addReferenceObj("codegenExec", this, null);
    String code = input[0].code + "\n"+
     "this.collect(" + input[0].value + ");\n";
    System.out.println("CodegenExec consumeCode=" + code);
    return code;
  }

  public void doCodeGenExec() {
    CodegenContext ctx = new CodegenContext();
    String code = this.doProduce(ctx);
    String source = "public Object generate(Object[] references) { \n" +
          " return new GeneratedIterator(references);\n" +
         "} \n "+
         "class GeneratedIterator implements codegen.Collector {\n" +
         "private Object[] references; \n" +
         "private codegen.Collector outputCollector; \n" +
         ctx.declareMutableStates() + "\n \n" +
         "public void init() { \n" +
         ctx.initMutableStates() + "\n" +
         "} \n" +
         
         "public GeneratedIterator(Object[] references) {\n" +
           "this.references = references; \n"+
          "} \n" +
          "public void setup(Collector outputCollector) { \n" +
            " this.outputCollector = outputCollector; \n" +
          "}\n" +
           "public void collect(Object record) { \n" +
            "this.outputCollector.collect(record); \n" +
          "} \n" +
          ctx.declareAddedFunctions() + "\n" +
          "public void processRecord() { \n" +
            code +
          "} \n" +
         "} \n";
    System.out.println("SoureCode = " + source);
    CodeGenerator codeGen = new CodeGenerator();
    String className = "codegen.GeneratedClassChild";
    try {
      GeneratedClass object = codeGen.compiler(GeneratedClass.class, className, 
          "generator.java", source);
      Object[] references = ctx.references.toArray();
      Collector collect = (Collector) object.generate(references);
      collect.init();
      collect.setup(new PrintCollector());
      collect.processRecord();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } 
  }
  
  public static void main(String[] args) {
    // (col1 + 2) * 3 
    InputAdaptor input = new InputAdaptor(null);
    AddExec add = new AddExec(input, 2);
    MultiplyExec multiply = new MultiplyExec(add, 3);;
    CodegenExec exec = new CodegenExec(multiply);
    exec.doCodeGenExec();
  }

}
