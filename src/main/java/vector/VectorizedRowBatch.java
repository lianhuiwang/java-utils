package vector;

import codegen.DataType;

public class VectorizedRowBatch {

  public static int DEFAULT_SIZE = 1024;
  public OnHeapColumnVector[] cols;
  public int rowBatchSize;
  
  // from Hive
  public int numCols;
  public int numRows;
  public int size;              // number of rows that qualify (i.e. haven't been filtered out)
  public int[] selected;        // array of positions of selected values
  public int[] projectedColumns;
  public int projectionSize;

  private int dataColumnCount;
  private int partitionColumnCount;
  
  public VectorizedRowBatch(int colCount) {
    this(colCount, DEFAULT_SIZE);
    
  }
  
  public VectorizedRowBatch(int colCount, int rowBatchSize) {
    this.cols = new OnHeapColumnVector[colCount];
    this.rowBatchSize = rowBatchSize;
    this.numRows = this.rowBatchSize;
  }
  
  public OnHeapColumnVector getCols(int colId) {
    return this.cols[colId];
  }
  
  public int numCols() { return this.cols.length; }
  
  public int numRows() { return this.numRows; }
  
  public void setNumRows(int numRows) {
    this.numRows = numRows;
  }
  
}
