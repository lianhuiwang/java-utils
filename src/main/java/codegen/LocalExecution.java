package codegen;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vector.OnHeapColumnVector;
import vector.VectorizedRowBatch;
import codegen.BinaryArithmetic.Symbol;

public class LocalExecution {

  //private Logger Log = LoggerFactory.getLogger(LocalExecution.class);
  
  public static int rowCount = 10 * 1024 * 1024;
  
  public static int limit = 3;
  
  public static int repetition = 50;
  
  public void testNoVector(CodegenExec exec, boolean print) {
    long start = System.nanoTime();
    System.out.println("===no vector execution===" + start);
    //Iterator<InternalRow> results = null;
    for (int i = 0; i < repetition; i ++) {
      exec.beginRow();
      while(exec.hasNextRow()) {
        InternalRow row = exec.nextRow();
      }
      //System.out.println("exec.execution==" + i + " finish " + System.nanoTime());
//      results = exec.execution();
//      while (results.hasNext()) {
//        InternalRow row = (InternalRow) results.next();
//      }
    }
    long end = System.nanoTime();
    System.out.println("Total time: " + (end - start) / 1000000.0 /repetition + " ms/repetition");
    if (print) {
      int count = 0;
      exec.beginRow();
      while(exec.hasNextRow() && count < limit) {
        InternalRow row = exec.nextRow();
        count += 1;
        System.out.println("results=" + row);
      }
//      while (results.hasNext() && count < limit) {
//        count += 1;
//        System.out.println("results=" + results.next());
//      }
    }
  }
  
  public void testVector(CodegenExec exec, boolean print) {
    long start = System.nanoTime();
    System.out.println("===vector execution==="  +start);
    //Iterator<VectorizedRowBatch> vectorResults = null;
    for (int i = 0; i < repetition; i ++) {
      exec.beginVector();
      while(exec.hasNextVector()) {
        VectorizedRowBatch row = exec.nextVector();
      }
      //System.out.println("exec.testVector==" + i + " finish " + System.nanoTime());
//      vectorResults = exec.vectorExecution();
//      while (vectorResults.hasNext()) {
//        VectorizedRowBatch row = (VectorizedRowBatch) vectorResults.next();
//      }
    }
    long end = System.nanoTime();
    System.out.println("Total time: " + (end - start) / 1000000.0 /repetition + " ms/repetition");
    if (print) {
      exec.beginVector();
      if(exec.hasNextVector()) {
        VectorizedRowBatch rowBatches = exec.nextVector();
        for (int j = 0; j < rowBatches.numRows() && j < limit; j++) {
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
  }
  
  public void testNoVectorCodegen(CodegenExec exec, boolean print) {
    System.out.println("===no vector codegen execution===");
    long start = System.nanoTime();
    for (int i = 0; i < repetition; i ++) {
      Collector collect = exec.doCodeGenExec(false);
      collect.init();
      collect.setup(new PrintCollector(limit, false));
      collect.processRecord();
    }
    long end2 = System.nanoTime();
    System.out.println("Total time: " + (end2 - start) / 1000000.0 /repetition + " ms/repetition");
    if (print) {
      PrintCollector printCollect = new PrintCollector(limit, true);
      Collector collect = exec.doCodeGenExec(false);
      collect.init();
      collect.setup(printCollect);
      collect.processRecord();
      long end3 = System.nanoTime();
      System.out.println("Time: " + (end3 - end2)  + " nanoseconds, "
          + "resultCount=" + printCollect.resultCount);
    }
  }
  
  public void testSourceCode(InputAdaptor inputPlan, boolean print) {
    System.out.println("===souce code execution===");
    long start = System.nanoTime();
    for (int i = 0; i < repetition; i ++) {
      Iterator input = inputPlan.inputIterators();
//      long value;
//      long value1;
//      long value2;
      long value3;
      while (input.hasNext()) {
        InternalRow row = (InternalRow) input.next();
//        value = row.getLong(1) + 2L;
//        value1 = value + row.getLong(2);
//        value2 = value1 + row.getLong(3);
//        value3 = value2 + row.getLong(5);
        value3 = row.getLong(0) + 2L + row.getLong(1) + row.getLong(2) + row.getLong(0);
      }
    }
    long end2 = System.nanoTime();
    System.out.println("Total time: " + (end2 - start) / 1000000.0 /repetition + " ms/repetition");
    
    if (print) {
      Iterator input = inputPlan.inputIterators();
      int count = 0;
      while (input.hasNext() && count < limit) {
        count += 1;
        InternalRow row = (InternalRow) input.next();
        long value = row.getLong(1) + 2L + row.getLong(2) + row.getLong(3) + row.getLong(5);
        System.out.println("results=" + value);
      }
    }
  }
  
  public void testSourceCode2(InputAdaptor inputPlan, boolean print) {
    System.out.println("===souce code 2 execution===");
    long start = System.nanoTime();
    for (int i = 0; i < repetition; i ++) {
      Iterator input = inputPlan.inputIterators();
      long value;
      long value1;
      long value2;
      long value3;
      while (input.hasNext()) {
        InternalRow row = (InternalRow) input.next();
        value = row.getLong(1) + 2L;
        value1 = value + row.getLong(2);
        value2 = value1 + row.getLong(3);
        value3 = value2 + row.getLong(5);
      }
    }
    long end2 = System.nanoTime();
    System.out.println("Total time: " + (end2 - start) / 1000000.0 /repetition + " ms/repetition");
    
    if (print) {
      Iterator input = inputPlan.inputIterators();
      int count = 0;
      while (input.hasNext() && count < limit) {
        count += 1;
        InternalRow row = (InternalRow) input.next();
        long value = row.getLong(1) + 2L + row.getLong(2) + row.getLong(3) + row.getLong(5);
        System.out.println("results=" + value);
      }
    }
  }
  
  
  public void testVectorSourceCode(InputAdaptor inputPlan, boolean print) {
    System.out.println("===vector souce code execution===");
    long start = System.nanoTime();

    VectorizedRowBatch rowBatches = new VectorizedRowBatch(1, VectorizedRowBatch.DEFAULT_SIZE);
    for (int j = 0; j < 1; j++) {
      OnHeapColumnVector columnVector =
              new OnHeapColumnVector(VectorizedRowBatch.DEFAULT_SIZE, DataType.LongType);
      rowBatches.cols[j] = columnVector;
    }
    for (int i = 0; i < repetition; i ++) {
      Iterator input = inputPlan.vectorExecution();
      long value3;
      while (input.hasNext()) {
        VectorizedRowBatch row = (VectorizedRowBatch) input.next();
        for (int k = 0; k < row.numRows(); k++) {
          //for (int j = 0; j < 1; j++) {
            value3 = row.getCols(0).getLong(k) + 2L + row.getCols(1).getLong(k) +
                    row.getCols(2).getLong(k) + row.getCols(0).getLong(k);
            rowBatches.cols[0].putLong(k, value3);
          //}
        }
      }
    }
    long end2 = System.nanoTime();
    System.out.println("Total time: " + (end2 - start) / 1000000.0 /repetition + " ms/repetition");
    
    if (print) {
      Iterator input = inputPlan.vectorExecution();
      if (input.hasNext()) {
        VectorizedRowBatch row = (VectorizedRowBatch) input.next();
        for (int k = 0; k < row.numRows() && k < limit; k++) {
          for (int j = 0; j < 1; j++) {
            long value = row.getCols(1).getLong(k) + 2L +  row.getCols(2).getLong(k) + 
                row.getCols(3).getLong(k) +  row.getCols(5).getLong(k);
            System.out.println("results=" + value);
          }
        }
        
      }
    }
  }
  public void testVectorCodegen(CodegenExec exec, boolean print) {
    System.out.println("=== vector codegen execution===");
    long start = System.nanoTime();
    for (int i = 0; i < repetition; i ++) {
      Collector collect = exec.doCodeGenExec(true);
      collect.init();
      collect.setup(new PrintCollector(limit, false));
      collect.processRecord();
    }
    long end2 = System.nanoTime();
    System.out.println("Total time: " + (end2 - start) / 1000000.0 / repetition + " ms/repetition");
    if (print) {
      Collector collect = exec.doCodeGenExec(true);
      PrintCollector printCollect = new PrintCollector(limit, true, true);
      collect.init();
      collect.setup(printCollect);
      collect.processRecord();
      long end3 = System.nanoTime();
      
      System.out.println("Time: " + (end3 - end2)  + " nanoseconds."
          + "resultCount=" + printCollect.resultCount);
    }
  }
  
  public static void main(String[] args) {
    
    DataType[] colTypes = new DataType[] { DataType.LongType, DataType.LongType, DataType.LongType};
    InputAdaptor input = new InputAdaptor(colTypes, rowCount);
    input.dataGen();
    
    // col0 + 2 + col1 + col2 + col0, col1 * 2
    Expression[] projList1 = new Expression[] {
        new BinaryArithmetic(
            new BinaryArithmetic(
                new BinaryArithmetic(
                    new BinaryArithmetic(input.output()[0], new Literal(2L, DataType.LongType), Symbol.ADD),
                input.output()[1], Symbol.ADD),
            input.output()[2], Symbol.ADD),
        input.output()[0], Symbol.ADD)
        // new BinaryArithmetic(input.output()[1], input.output()[2], Symbol.MULTIPLY)
    };
    ProjectExec proj1 = new ProjectExec(input, projList1);
    CodegenExec exec = new CodegenExec(proj1);
    
    LocalExecution local = new LocalExecution();
    //CodeGenerator.doDemo();

    System.out.println("start execution");
    boolean print = false;
    local.testSourceCode(input, print);
    // 10m: 141 ms
    local.testNoVector(exec, print);
    // 10m: 268 ms
    local.testNoVectorCodegen(exec, print);
    // 10m: 272 ms
    local.testVectorSourceCode(input, print);
    // 10m: 23 ms
    local.testVector(exec, print);
    // 10m: 30 ms
    local.testVectorCodegen(exec, print);
    // 10m: 37 ms
  }
}
