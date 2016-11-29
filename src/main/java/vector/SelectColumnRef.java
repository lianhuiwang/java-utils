package vector;

/**
 * @auther: lianhui
 * @date: 16/10/9
 */
public class SelectColumnRef extends VectorExpression {
	int outputColumn;

	public SelectColumnRef(int outputColumn) {
		this.outputColumn = outputColumn;
	}

	@Override
	public int getOutputColumn() {
		return outputColumn;
	}

	@Override
	public void evaluate(VectorizedRowBatch batch) {
		if (childExpressions != null) {
			this.evaluateChildren(batch);
		}
	}
}
