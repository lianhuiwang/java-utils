package codegen;

import java.util.Iterator;

import codegen.BinaryArithmetic.Symbol;

public class LocalExecution {
  
  public static void main(String[] args) {
    
    DataType[] colTypes = new DataType[] { DataType.IntType, DataType.IntType, DataType.IntType};
    InputAdaptor input = new InputAdaptor(colTypes, 5);
    
    // col1 + 2 + col2, col1 * 2
    Expression[] projList1 = new Expression[] {
        new BinaryArithmetic(
        new BinaryArithmetic(input.output()[1], new Literal(2, DataType.IntType), Symbol.ADD),
        input.output()[2], Symbol.ADD),
        new BinaryArithmetic(input.output()[1], input.output()[2], Symbol.MULTIPLY)
    };
    ProjectExec proj1 = new ProjectExec(input, projList1);
    CodegenExec exec = new CodegenExec(proj1);
    exec.doCodeGenExec();
    Iterator<InternalRow> results = exec.execution();
    while (results.hasNext()) {
      System.out.println("results=" + results.next());
    }
  }
}
