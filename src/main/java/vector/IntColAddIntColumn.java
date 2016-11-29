package vector;

/**
 * @auther: lianhui
 * @date: 16/10/9
 */
public class IntColAddIntColumn extends VectorExpression {
	int colNum1;
	int colNum2;
	int outputColumn;

	public IntColAddIntColumn(int colNum1, int colNum2, int outputColumn) {
		this.colNum1 = colNum1;
		this.colNum2 = colNum2;
		this.outputColumn = outputColumn;
	}

	@Override
	public int getOutputColumn() {
		return outputColumn;
	}

	@Override
	public void evaluate(VectorizedRowBatch batch) {
		int[] vector1 = batch.getCols(colNum1).intVector;
		int[] vector2 = batch.getCols(colNum2).intVector;
		int[] vector3 = batch.getCols(outputColumn).intVector;
		for (int k = 0; k < batch.numRows(); k++) {
			vector3[k] = vector1[k] + vector2[k];
		}
	}
}
