package codegen;

import java.util.Iterator;
import java.util.ListIterator;

import vector.OnHeapColumnVector;
import vector.VectorizedRowBatch;

public class ProjectExec extends CodegenOperator {
  private Expression[] projectList;
  
  private DataType[] outputTypes;
  
  private final VectorizedRowBatch rowBatches;
  
  public ProjectExec(CodegenOperator child, Expression[] projectList) {
    super(child);
    this.projectList = projectList;
    this.outputTypes = new DataType[this.projectList.length];
    for (int i = 0; i < this.projectList.length; i++) {
      this.outputTypes[i] = this.projectList[i].dataType();
    }
    rowBatches = Utils.createExpressionVector(this.outputTypes);
  }
  
  public Iterator<InternalRow> execution() {
    final Iterator<InternalRow> childIter = this.child.execution();
    final Object[] values = new Object[projectList.length];
    return new Iterator<InternalRow>() {
      @Override
      public boolean hasNext() {
        return childIter.hasNext();
      }

      @Override
      public InternalRow next() {
        InternalRow input = childIter.next();
        
        for (int i = 0; i < values.length; i++) {
          values[i] = projectList[i].eval(input);
        }
        return new InternalRow(values);
      }
    };
  }
  
  @Override
  public Iterator<VectorizedRowBatch> vectorExecution() {
    final Iterator<VectorizedRowBatch> childIter = this.child.vectorExecution();
    return new Iterator<VectorizedRowBatch>() {
      @Override
      public boolean hasNext() {
        return childIter.hasNext();
      }

      @Override
      public VectorizedRowBatch next() {
        VectorizedRowBatch input = childIter.next();
        rowBatches.setNumRows(input.numRows);
        for (int k = 0; k < input.numRows(); k++) {
          for (int j = 0; j < projectList.length; j++) {
            rowBatches.getCols(j).putLong(k, (long) projectList[j].evalVector(input, k));
          }
        }
        return rowBatches;
      }
    };
  }
  
  public DataType[] getOutputDataTypes() {
    return this.outputTypes;
  }
 
  @Override
  public String doConsume(CodegenContext ctx, ExprCode[] input, String row) {
    String consumeCode = "";
    if (ctx.isVector) {
      String projectPlan = ctx.addReferenceObj("projPlan", this, null);
      String rowBatches = ctx.freshName("rowBatches");
      ctx.addMutableState("vector.VectorizedRowBatch", rowBatches, 
          rowBatches + " = codegen.Utils.createExpressionVector(" + projectPlan + ".getOutputDataTypes());");
      String isNull = ctx.freshName("isNull");
      ExprCode result = new ExprCode("", isNull, rowBatches);
      String rowId = ctx.freshName("rowId");
      ctx.INPUT_ROW_ID= rowId;
      StringBuilder exprCode = new StringBuilder();
      ExprCode[] exprVars = new ExprCode[projectList.length];
      StringBuilder setResult = new StringBuilder();
      for (int i = 0; i < projectList.length; i++) {
        ExprCode expr = projectList[i].genCode(ctx);
        exprVars[i] = expr;
        exprCode.append(expr.code).append("\n");
        // rowBatches.cols[j].putLong(k, value)
        setResult.append(rowBatches + ".getCols(" + i + ").putLong(" + rowId + ", " + expr.value + "); \n");
      }
      
      result.code = rowBatches + ".setNumRows(" +  ctx.INPUT_ROW + ".numRows()); \n" +
          "for (int " + rowId + " = 0; "+ rowId +" < " +ctx.INPUT_ROW + ".numRows(); " + rowId + "++) {\n" +
             exprCode.toString() +
             setResult.toString() +
          "}\n";
      
      ExprCode[] resultVars = new ExprCode[1];
      resultVars[0] = result;
      consumeCode = this.consume(ctx, resultVars, rowBatches);
    } else {
      ExprCode[] resultVars = new ExprCode[projectList.length];
      for (int i = 0; i < projectList.length; i++) {
        resultVars[i] = projectList[i].genCode(ctx);
      }
      consumeCode = this.consume(ctx, resultVars, row);
    }
    return consumeCode;
  }

}
