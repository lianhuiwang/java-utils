package codegen;

import vector.VectorizedRowBatch;

public class BoundReference extends Expression {

  int index;
  DataType dataType;
  boolean nullable;
  
  
  public BoundReference(int index, DataType dataType, boolean nullable) {
    this.index = index;
    this.dataType = dataType;
    this.nullable = nullable;
  }

  public DataType dataType() {
    return this.dataType;
  }


  @Override
  public DataType[] inputTypes() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Object eval(InternalRow input) {
    return input.get(this.index);
  }
  
  @Override
  public Object evalVector(VectorizedRowBatch input, int k) {
    return input.getCols(this.index).getLong(k);
  }

  @Override
  public ExprCode genCode(CodegenContext ctx) {
    if (ctx.currentVars != null && ctx.currentVars[this.index] != null) {
      ExprCode oev = ctx.currentVars[this.index];
      ExprCode ev = new ExprCode(oev.code, oev.isNull, oev.value);
      oev.code = "";
      return ev;
    } else {
      String input = ctx.INPUT_ROW;
      String javaType = ctx.javaType(this.dataType);
      String typeName = ctx.primitiveTypeName(this.dataType);
//      String isNull = ctx.freshName("isNull");
//      String value = ctx.freshName("value");
//      ExprCode ev = new ExprCode("", isNull, value);
//      ev.code = javaType + " " + ev.value + " = " +  
//          input + ".get" + typeName +"(" + this.index + ");";
      ExprCode ev = new ExprCode("", "false", "");
      if (ctx.isVector) {
        String rowId = ctx.INPUT_ROW_ID;
        ev.value = input +".getCols(" + this.index + ").get" + typeName +"(" + rowId + ")";
      } else {
        ev.value = input + ".get" + typeName +"(" + this.index + ")";
      }
     
      return ev;
    }
  }
}
