package vector;

/**
 * @auther: lianhui
 * @date: 16/10/9
 */
public abstract class VectorExpression {

	protected VectorExpression [] childExpressions = null;

	public abstract int getOutputColumn();

	public abstract void evaluate(VectorizedRowBatch batch);

	public void setChildExpressions(VectorExpression [] ve) {

		childExpressions = ve;
	}

	final protected void evaluateChildren(VectorizedRowBatch vrg) {
		if (childExpressions != null) {
			for (VectorExpression ve : childExpressions) {
				ve.evaluate(vrg);
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(this.getClass().getSimpleName());
		b.append("[");
		b.append(this.getOutputColumn());
//		b.append(":");
//		b.append(this.getOutputType());
		b.append("]");
		if (childExpressions != null) {
			b.append("(");
			for (int i = 0; i < childExpressions.length; i++) {
				b.append(childExpressions[i].toString());
				if (i < childExpressions.length-1) {
					b.append(" ");
				}
			}
			b.append(")");
		}
		return b.toString();
	}

}
