package codegen;

public class MultiplyExec extends CodegenSupport {

  public int value;
  
  public MultiplyExec(CodegenSupport child, int value) {
    super(child);
    this.value = value;
  }

  @Override
  public String doConsume(CodegenContext ctx, ExprCode[] input) {
    ctx.currentVars = input;
    String isNull = ctx.freshName("isNull");
    String value = ctx.freshName("value");
    ExprCode result = new ExprCode("", isNull, value);
    String thisValue = ctx.addReferenceObj("multiplyExec", this, null);
    ctx.addMutableState("int", value, value +" = 0;");
    String code = input[0].code + "\n"+
        value + " = " + input[0].value + " * " + thisValue+".value; \n";
    result.code = code;
    ExprCode[] resultVars = new ExprCode[1];
    resultVars[0] = result;
    String consumeCode = this.consume(ctx, resultVars);
    return consumeCode;
  }

}
