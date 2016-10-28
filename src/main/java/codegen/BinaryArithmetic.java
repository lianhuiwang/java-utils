package codegen;

import vector.VectorizedRowBatch;

public class BinaryArithmetic extends Expression {

  public Expression left;
  public Expression right;
  private Symbol symbol;
  
  public enum Symbol {

    ADD, MULTIPLY
  }
  
  public BinaryArithmetic(Expression left, Expression right, Symbol symbol) {
    this.left = left;
    this.right = right;
    this.symbol = symbol;
  }
  
  @Override
  public DataType[] inputTypes() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DataType dataType() {
    return this.left.dataType();
  }
  
  private Object compute(Object value1, Object value2) {
    switch(symbol) {
    case ADD:
      switch(this.dataType()) {
      case IntType:
        return (int)value1 + (int)value2;
        
      case LongType:
        return (long)value1 + (long)value2;
      }
    case MULTIPLY:
      switch(this.dataType()) {
      case IntType:
        return (int)value1 * (int)value2;
        
      case LongType:
        return (long)value1 * (long)value2;
      }
    }
    return null;
  }

  @Override
  public Object eval(InternalRow input) {
    Object value1 = this.left.eval(input);
    Object value2 = this.right.eval(input);
    return compute(value1, value2);
  }
  
  @Override
  public Object evalVector(VectorizedRowBatch input, int k) {
    Object value1 = this.left.evalVector(input, k);
    Object value2 = this.right.evalVector(input, k);
    return compute(value1, value2);
  }
  

  @Override
  public ExprCode genCode(CodegenContext ctx) {
    ExprCode expr1 = this.left.genCode(ctx);
    ExprCode expr2 = this.right.genCode(ctx);
    String isNull = ctx.freshName("isNull");
    String value = ctx.freshName("value");
    ExprCode result = new ExprCode("", isNull, value);
    switch(this.dataType()) {
    case IntType:
      ctx.addMutableState("int", value, value +" = 0;");
      break;
    case LongType:
      ctx.addMutableState("long", value, value +" = 0;");
      break;
    }
    String operator = "";
    switch(symbol) {
    case ADD:
      operator = " + ";
      break;
    case MULTIPLY:
      operator = " * ";
      break;
    }
    String code = expr1.code + "\n" +
        expr2.code + "\n" +
        value + " = " + expr1.value + operator + expr2.value+"; \n";
    result.code = code;
    return result;
  }

}
