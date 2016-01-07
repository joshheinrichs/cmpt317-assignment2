package assignment2;

public class ComputerB extends Computer {

	ComputerB(Color color, Board board, int depth) {
		super(color, board, depth);
	}

	/**
	 * {@inheritDoc} This evaluation function employs pawn counting. If it has
	 * won, it returns {@link Double#MAX_VALUE}, if it has lost, it returns -
	 * {@link Double#MAX_VALUE}, otherwise, it returns its number of pawns minus
	 * its enemy's number of pawns.
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
					whitePresence++;
				}
			}
			for (int i = 0; i < board.blackPawns.length; i++) {
				if (!board.blackPawns[i].isCaptured()) {
					blackPresence++;
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
