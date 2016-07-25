package codegen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import vector.OnHeapColumnVector;
import vector.VectorizedRowBatch;

public class InputAdaptor extends CodegenOperator {
  
  private DataType[] colTypes;
  private int rowSize = 0;
  private List<InternalRow> inputs = null;
  private List<VectorizedRowBatch> inputsVector = null;
  
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
  
  public List<VectorizedRowBatch> inputVectorIterators(List<InternalRow> inputs) {
    int rowBatchSize = VectorizedRowBatch.DEFAULT_SIZE; // 1024
    int numRowBatches = inputs.size() / rowBatchSize;
    List<VectorizedRowBatch> inputVectors = new ArrayList<VectorizedRowBatch>();
    for (int i = 0; i < numRowBatches; i++) {
      VectorizedRowBatch rowBatches = new VectorizedRowBatch(colTypes.length, rowBatchSize);
      for (int j = 0; j < colTypes.length; j++) {
        OnHeapColumnVector columnVector = new OnHeapColumnVector(rowBatchSize, colTypes[j]);
        for (int k = 0; k < rowBatchSize; k++) {
          columnVector.putLong(k, inputs.get(i * rowBatchSize + k).getLong(j));
        }
        rowBatches.cols[j] = columnVector;
      }
      inputVectors.add(rowBatches);
    }
    System.out.println("Vector numRow=" + inputVectors.size() + ", rowBatchSize=" + rowBatchSize);
    return inputVectors;
  }
  
  public void dataGen() {
    if (this.inputs == null) {
      System.out.println("start dataGen rowSize= " + this.rowSize);
      long start = System.nanoTime();
      inputs = new ArrayList<InternalRow>();
      for (int i = 0; i < rowSize; i++) {
        inputs.add(produceRow(i));
      }
      long end = System.nanoTime();
      System.out.println("Total time: " + (end - start) / 1000000.0  + " ms for dataGen");
      if (this.inputsVector == null) {
        this.inputsVector = this.inputVectorIterators(inputs);
        long end2 = System.nanoTime();
        System.out.println("Total time: " + (end2 - end) / 1000000.0  + " ms for dataGen");
      }
    }
  }
  
  @Override
  public Iterator<InternalRow> inputIterators() {
    return this.inputs.iterator();
  }
  
  @Override
  public Iterator<InternalRow> execution() {
    // TODO Auto-generated method stub
    return this.inputIterators();
  }
  
  @Override
  public Iterator<VectorizedRowBatch> vectorExecution() {
    return this.inputsVector.iterator();
  }

  @Override
  public String doProduce(CodegenContext ctx) {
    String input = ctx.freshName("input");
    String thisInput = ctx.addReferenceObj("inputPlan", this, null);
        // Right now, InputAdapter is only used when there is one input RDD.
    if (ctx.isVector) {
      ctx.addMutableState("java.util.Iterator", input, 
          input + " = " + thisInput + ".vectorExecution();");
    } else {
      ctx.addMutableState("java.util.Iterator", input, 
          input + " = " + thisInput + ".inputIterators();");
    }
   
    String row = ctx.freshName("row");
    String consumeCode = this.consume(ctx, null, row);
    String code = "";
    if (ctx.isVector) {
      code = "while (" + input +".hasNext()) { \n" +
          " vector.VectorizedRowBatch " + row + " = (vector.VectorizedRowBatch)" + input + ".next(); \n" +
          consumeCode + "\n" +
      "} \n";
    } else {
      code = "while (" + input +".hasNext()) { \n" +
          " InternalRow " + row + " = (InternalRow)" + input + ".next(); \n" +
          consumeCode + "\n" +
      "} \n";
    }   
    return code;
  }

  @Override
  public String doConsume(CodegenContext ctx, ExprCode[] input, String row) {
    // TODO Auto-generated method stub
    return null;
  }

}
