package vector;

import codegen.DataType;

public class OnHeapColumnVector {

  public long[] vector;
  public int[] intVector;
  
  public OnHeapColumnVector(int rowBatchSize) {
    vector = new long[rowBatchSize];
  }

  public OnHeapColumnVector(int rowBatchSize, DataType dataType) {
   switch(dataType) {
   case IntType:
     intVector = new int[rowBatchSize];
     break;
   case LongType:
     vector = new long[rowBatchSize];
     break;
   }
  }
  
  public void putLong(int rowId, long value) {
    vector[rowId] = value;
  }
  
  public void putInt(int rowId, int value) {
    intVector[rowId] = value;
  }
  
  public int getInt(int rowId) {
    return intVector[rowId];
  }

  public long getLong(int rowId) {
    return vector[rowId];
  }
}
