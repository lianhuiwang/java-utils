package codegen;

public abstract class Expression {

  public abstract DataType[] inputTypes();
  public abstract DataType dataType();
  
  public abstract Object eval(InternalRow input);
  
  public abstract ExprCode genCode(CodegenContext ctx);
}
