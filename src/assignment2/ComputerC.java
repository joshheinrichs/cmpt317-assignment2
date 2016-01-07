package assignment2;

public class ComputerC extends Computer {

	/**
	 * Constructs a new computer
	 * 
	 * @param color
	 *            Color of the computer's pieces
	 * @param board
	 *            Board on which the computer plays
	 * @param depth
	 */
	ComputerC(Color color, Board board, int depth) {
		super(color, board, depth);
	}

	/**
	 * {@inheritDoc} This evaluation function evaluates board state. If it has
	 * won, it returns {@link Double#MAX_VALUE}, if it has lost, it returns -
	 * {@link Double#MAX_VALUE}, otherwise, it returns the value of this
	 * computer's board position minus its opponent's board position.
	 */
	public double evaluate(Board board) {
		if (board.isFinished() && board.winner() == super.color) {
			return Double.MAX_VALUE;
		} else if (board.isFinished() && board.winner() != super.color) {
			return -Double.MAX_VALUE;
		} else {
			double whitePresence = 0;
			double blackPresence = 0;

			for (int i = 0; i < board.whitePawns.length; i++) {
				if (!board.whitePawns[i].isCaptured()) {
					double centerWeight = 1;

					if (i == 1 || i == 3) {
						centerWeight = 1.5;
					}
					if (i == 2) {
						centerWeight = 2.0;
					}

					whitePresence += 5
							+ (Board.BOARD_HEIGHT - board.whitePawns[i].position.y)
							* centerWeight;
				}
			}
			for (int i = 0; i < board.blackPawns.length; i++) {
				if (!board.blackPawns[i].isCaptured()) {
					double centerWeight = 1;

					if (i == 1 || i == 3) {
						centerWeight = 1.5;
					}
					if (i == 2) {
						centerWeight = 2.0;
					}
					blackPresence += 5 + board.blackPawns[i].position.y
							* centerWeight;
				}
			}

			if (super.color == Color.WHITE) {
				return whitePresence - blackPresence;
			} else // (super.color == Color.BLACK)
			{
				return blackPresence - whitePresence;
			}
		}
	}

}
