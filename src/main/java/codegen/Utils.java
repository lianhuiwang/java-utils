package codegen;

import vector.OnHeapColumnVector;
import vector.VectorizedRowBatch;

public class Utils {

  public static VectorizedRowBatch createExpressionVector(
      DataType[] outputTypes) {
    VectorizedRowBatch rowBatches = new VectorizedRowBatch(outputTypes.length, VectorizedRowBatch.DEFAULT_SIZE);
    for (int j = 0; j < outputTypes.length; j++) {
      OnHeapColumnVector columnVector = 
          new OnHeapColumnVector(VectorizedRowBatch.DEFAULT_SIZE, outputTypes[j]);
      rowBatches.cols[j] = columnVector;
    }
    return rowBatches;
  }
  
  public static void printVector(VectorizedRowBatch rowBatches, int limit) {
    int count = 0;
    for (int j = 0; j < rowBatches.numRows() && count < limit; j++) {
      count += 1;
      String row = "";
      for (int k = 0; k < rowBatches.numCols(); k++) {
        if (k != 0) {
          row += ",";
        }
        row += rowBatches.getCols(k).getLong(j);
      }
      System.out.println("results=" + row);
    }
  }

}
