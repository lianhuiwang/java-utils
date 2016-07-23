package codegen;

public class Literal extends Expression {

  private Object value;
  private DataType dataType;
  
  public Literal(Object value, DataType dataType) {
    this.value = value;
    this.dataType = dataType;
  }
  @Override
  public DataType[] inputTypes() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DataType dataType() {
    // TODO Auto-generated method stub
    return this.dataType;
  }

  @Override
  public Object eval(InternalRow input) {
    // TODO Auto-generated method stub
    return this.value;
  }
  
  @Override
  public ExprCode genCode(CodegenContext ctx) {
    ExprCode ev = new ExprCode("", "false", "");
    switch(this.dataType) {
    case IntType:
      ev.value = value.toString();
      break;
    case LongType:
      ev.value = value.toString() + "L";
      break;
    }
    return ev;
  }

}
