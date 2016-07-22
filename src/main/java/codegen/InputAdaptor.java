package codegen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InputAdaptor extends CodegenSupport {

  public InputAdaptor(CodegenSupport child) {
    super(child);
  }

  @Override
  public Iterator<Row> inputIterators() {
    List<Row> inputs = new ArrayList<Row>();
    inputs.add(new Row(0));
    inputs.add(new Row(1));
    inputs.add(new Row(2));
    inputs.add(new Row(3));
    return inputs.iterator();
  }

  @Override
  public String doProduce(CodegenContext ctx) {
    String input = ctx.freshName("input");
    String thisInput = ctx.addReferenceObj("inputPlan", this, null);
        // Right now, InputAdapter is only used when there is one input RDD.
    ctx.addMutableState("java.util.Iterator", input, 
        input + " = " + thisInput + ".inputIterators();");
    String row = ctx.freshName("row");
    ExprCode[] inputVars = new ExprCode[1];
    inputVars[0] = new ExprCode("", "false", row);
    String consumeCode = this.consume(ctx, inputVars);
    String code = 
        "while (" + input +".hasNext()) { \n" +
            " Row " + row + " = (Row)" + input + ".next(); \n" +
            consumeCode + "\n" +
        "} \n";
    return code;
  }

  @Override
  public String doConsume(CodegenContext ctx, ExprCode[] input) {
    // TODO Auto-generated method stub
    return null;
  }

}
