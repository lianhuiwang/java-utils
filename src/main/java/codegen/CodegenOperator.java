package codegen;

import java.util.Iterator;

public abstract class CodegenOperator {

  protected CodegenOperator child = null;
  protected CodegenOperator parent = null;
  
  public CodegenOperator(CodegenOperator child) {
    this.child = child;
  }
  
  public abstract String doConsume(CodegenContext ctx, ExprCode[] input, String row);
  
  public abstract Iterator<InternalRow> execution();
  
  public BoundReference[] output() {
    return child.output();
  }
  
  public Iterator<InternalRow> inputIterators() {
    return null;
  }

  public String produce(CodegenContext ctx, CodegenOperator parent) {
    this.parent = parent;
    return this.doProduce(ctx);
  }
  
  public String doProduce(CodegenContext ctx) {
    return this.child.produce(ctx, this);
  }
  
  public String consume(CodegenContext ctx, ExprCode[] input, String row) {
    ctx.currentVars = input;
    ctx.INPUT_ROW = row;
    return this.parent.doConsume(ctx, input, row);
  }
  
}
