package assignment2;

public class ComputerA extends Computer {

	ComputerA(Color color, Board board, int depth) {
		super(color, board, depth);
	}

	/**
	 * {@inheritDoc} This evaluation function is rather simple. If it has won,
	 * it returns {@link Double#MAX_VALUE}, if it has lost, it returns -
	 * {@link Double#MAX_VALUE}, otherwise it returns 0.
	 */
	public double evaluate(Board board) {
		if (board.isFinished() && board.winner() == super.color) {
			return Double.MAX_VALUE;
		} else if (board.isFinished() && board.winner() != super.color) {
			return -Double.MAX_VALUE;
		} else {
			return 0;
		}
	}

}
