package vector;

import codegen.BinaryArithmetic;
import codegen.BoundReference;
import codegen.Expression;

/**
 * @auther: lianhui
 * @date: 16/10/9
 */
public class VectorizationContext {

	public VectorExpression getVectorExpression(Expression expression) {
		if (expression instanceof BinaryArithmetic) {
			VectorExpression left = this.getVectorExpression(((BinaryArithmetic)expression).left);
			VectorExpression right = this.getVectorExpression(((BinaryArithmetic)expression).right);
			VectorExpression vectorExpression = new IntColAddIntColumn(left.getOutputColumn(), right.getOutputColumn(), 3);
			return vectorExpression;
		} else if (expression instanceof BoundReference) {
			return new SelectColumnRef(((BoundReference) expression).getIndex());
		}
		return null;
	}
}
