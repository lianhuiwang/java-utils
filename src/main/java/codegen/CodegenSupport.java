package codegen;

import java.util.Iterator;

public abstract class CodegenSupport {

  protected CodegenSupport child = null;
  protected CodegenSupport parent = null;
  
  public Iterator<Row> inputIterators() {
    return null;
  }
  
  public CodegenSupport(CodegenSupport child) {
    this.child = child;
  }
      
  public String produce(CodegenContext ctx, CodegenSupport parent) {
    this.parent = parent;
    return this.doProduce(ctx);
  }
  
  public String doProduce(CodegenContext ctx) {
    return this.child.produce(ctx, this);
  }
  
  public String consume(CodegenContext ctx, ExprCode[] input) {
    return this.parent.doConsume(ctx, input);
  }
  
  public abstract String doConsume(CodegenContext ctx, ExprCode[] input);
}
