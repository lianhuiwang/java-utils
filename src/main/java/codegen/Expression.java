package codegen;

import vector.VectorizedRowBatch;

public abstract class Expression {

  public abstract DataType[] inputTypes();
  public abstract DataType dataType();
  
  public abstract Object eval(InternalRow input);
  public abstract Object evalVector(VectorizedRowBatch input, int k);
  
  public abstract ExprCode genCode(CodegenContext ctx);
 
}
