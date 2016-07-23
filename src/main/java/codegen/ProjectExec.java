package codegen;

import java.util.Iterator;
import java.util.ListIterator;

public class ProjectExec extends CodegenOperator {
  private Expression[] projectList;
  
  public ProjectExec(CodegenOperator child, Expression[] projectList) {
    super(child);
    this.projectList = projectList;
  }
  
  public Iterator<InternalRow> execution() {
    final Iterator<InternalRow> childIter = this.child.execution();
    return new Iterator<InternalRow>() {
      @Override
      public boolean hasNext() {
        return childIter.hasNext();
      }

      @Override
      public InternalRow next() {
        InternalRow input = childIter.next();
        Object[] values = new Object[projectList.length];
        for (int i = 0; i < values.length; i++) {
          values[i] = projectList[i].eval(input);
        }
        return new InternalRow(values);
      }
    };
  }

  @Override
  public String doConsume(CodegenContext ctx, ExprCode[] input, String row) {
    System.out.println("ProjectExec doConsume()");
    ExprCode[] resultVars = new ExprCode[projectList.length];
    for (int i = 0; i < projectList.length; i++) {
      resultVars[i] = projectList[i].genCode(ctx);
    }
    String consumeCode = this.consume(ctx, resultVars, row);
    System.out.println("ProjectExec consumeCode=" + consumeCode);
    return consumeCode;
  }

}
