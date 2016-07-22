package codegen;

public class AddExec extends CodegenSupport {

  public int value;
  
  public AddExec(CodegenSupport child, int value) {
    super(child);
    this.value = value;
  }

  @Override
  public String doConsume(CodegenContext ctx, ExprCode[] input) {
    System.out.println("AddExec doConsume()");
    ctx.currentVars = input;
    String isNull = ctx.freshName("isNull");
    String value = ctx.freshName("value");
    ExprCode result = new ExprCode("", isNull, value);
    String thisValue = ctx.addReferenceObj("addExec", this, null);
    ctx.addMutableState("int", value, value +" = 0;");
    String code = input[0].code + "\n"+
        value + " = " + input[0].value + ".valI + " + thisValue+".value; \n";
    result.code = code;
    ExprCode[] resultVars = new ExprCode[1];
    resultVars[0] = result;
    String consumeCode = this.consume(ctx, resultVars);
    System.out.println("AddExec consumeCode=" + consumeCode);
    return consumeCode;
  }

}
