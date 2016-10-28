package codegen;

import java.util.Iterator;

import vector.VectorizedRowBatch;

public class CodegenExec extends CodegenOperator{
  
  public CodegenExec(CodegenOperator child) {
    super(child);
  }

  @Override
  public String doConsume(CodegenContext ctx, ExprCode[] input, String row) {
    ctx.currentVars = input;
    String code = "";
    if (ctx.isVector) {
      StringBuilder exprCode = new StringBuilder();
      exprCode.append(input[0].code);
      code = exprCode + "this.collect(" + row + ");\n";
    } else {
      String isNull = ctx.freshName("isNull");
      // String value = ctx.freshName("value");
      String result = ctx.freshName("result");
      ctx.addMutableState("InternalRow", result, 
          result + "= new InternalRow(" + input.length + ");");
      StringBuilder exprCode = new StringBuilder();
      for (int i = 0; i < input.length; i++) {
        exprCode.append(input[i].code);
      }
      
      for (int i = 0; i < input.length; i++) {
        exprCode.append(result + ".set(" + i + "," + input[i].value + ");\n");
      }
      
      code = exprCode +
       "this.collect(" + result + ");\n";
    }
    
    return code;
  }

  public Collector doCodeGenExec(boolean isVector) {
    CodegenContext ctx = new CodegenContext();
    ctx.isVector = isVector;
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
    
    try {
      //System.out.println("source=" + source);
      GeneratedClass object = CodeGenerator.doCompiler(source);
      Object[] references = ctx.references.toArray();
      Collector collect = (Collector) object.generate(references);
      return collect;
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } 
    return null;
  }

  @Override
  public Iterator<InternalRow> execution() {
    return this.child.execution();
  }

  @Override
  public Iterator<VectorizedRowBatch> vectorExecution() {
    return this.child.vectorExecution();
  }

  @Override
  public void beginRow() {
    this.child.beginRow();
  }

  @Override
  public boolean hasNextRow() {
    return this.child.hasNextRow();
  }

  @Override
  public InternalRow nextRow() {
    return this.child.nextRow();
  }

  @Override
  public void endRow() {
    this.child.endRow();
  }

  @Override
  public void beginVector() {
    this.child.beginVector();
  }

  @Override
  public boolean hasNextVector() {
    return this.child.hasNextVector();
  }

  @Override
  public VectorizedRowBatch nextVector() {
    return this.child.nextVector();
  }

  @Override
  public void endVector() {
    this.child.endVector();
  }

}
