package codegen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InputAdaptor extends CodegenOperator {
  
  private DataType[] colTypes;
  private int rowSize = 0;
  private List<InternalRow> inputs = null;
  
  private int valInt = 1;
  private Long valLong = 1L;
  
  
  public InputAdaptor(DataType[] colTypes, int rowSize) {
    super(null);
    this.colTypes = colTypes;
    this.rowSize = rowSize;
  }
  
  public InputAdaptor(DataType[] colTypes) {
    this(colTypes, 1);
  }
  
  public BoundReference[] output() {
    BoundReference[] refs = new BoundReference[colTypes.length];
    for (int i = 0; i < colTypes.length; i++) {
      refs[i] = new BoundReference(i, colTypes[i], false);
    }
    return refs;
  }

  public Object produceColumn(DataType type) {
    if (type == DataType.IntType) {
      this.valInt ++;
      return this.valInt;
    } else if (type == DataType.LongType) {
      this.valLong ++;
      return this.valLong;
    } else {
      return this.valInt;
    }
  }
  
  public InternalRow produceRow(int rowNumber) {
    Object[] values = new Object[colTypes.length];
    for (int i = 0; i < values.length; i++) {
      values[i] = produceColumn(colTypes[i]);
    }
    return new InternalRow(values);
  }
  
  @Override
  public Iterator<InternalRow> inputIterators() {
    if (inputs == null) {
      inputs = new ArrayList<InternalRow>();
      for (int i = 0; i < rowSize; i++) {
        inputs.add(produceRow(i));
      }
    }
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
    
//    ExprCode[] inputVars = new ExprCode[this.output().length];
//    for (int i = 0; i < inputVars.length; i++) {
//      inputVars[i] = this.output()[i].genCode(ctx);
//    }
    String consumeCode = this.consume(ctx, null, row);
    String code = 
        "while (" + input +".hasNext()) { \n" +
            " InternalRow " + row + " = (InternalRow)" + input + ".next(); \n" +
            consumeCode + "\n" +
        "} \n";
    return code;
  }

  @Override
  public Iterator<InternalRow> execution() {
    // TODO Auto-generated method stub
    return this.inputIterators();
  }

  @Override
  public String doConsume(CodegenContext ctx, ExprCode[] input, String row) {
    // TODO Auto-generated method stub
    return null;
  }

}
