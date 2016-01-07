package assignment2;

import java.util.List;

public abstract class Computer extends Player {

	/**
	 * The number of turns the computer considers. Must be at least one.
	 */
	int depth;

	/**
	 * Constructs a new computer
	 * 
	 * @param color
	 *            Color of the computer's pieces
	 * @param board
	 *            The board on which the computer plays
	 * @param depth
	 *            The depth to which the computer seached for each turn
	 */
	Computer(Color color, Board board, int depth) {
		super(color, board);
		this.depth = depth;
	}

	/**
	 * {@inheritDoc} In this case, minimax and an evaluation function are used
	 * to determine the next move to be made.
	 */
	@Override
	void move() {
		long startTime = System.nanoTime();
		EvaluatedBoard evaluatedBoard = max(this.board, this.depth,
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		long endTime = System.nanoTime();

		double elapsedTime = (endTime - startTime) / 1000000000.0;

		System.out.println("Estimated value: " + evaluatedBoard.value);
		System.out.println("Time to decide: " + elapsedTime + " seconds");

		this.board.copy(evaluatedBoard.board);
	}

	/**
	 * Returns the worst adjacent board state for this player
	 * 
	 * @param board
	 *            Current board state
	 * @param depth
	 *            How many more turns will be checked
	 * @param alpha
	 *            Best so far
	 * @param beta
	 *            Worst so far
	 * @return
	 */
	EvaluatedBoard min(Board board, int depth, double alpha, double beta) {
		List<Board> successors = board.successors();
		if (successors.isEmpty()) {
			return new EvaluatedBoard(board, evaluate(board));
		} else if (depth == 1) {
			double minValue = Double.POSITIVE_INFINITY;
			Board minBoard = null;
			for (int i = 0; i < successors.size(); i++) {
				double successorValue = evaluate(successors.get(i));
				if (successorValue < minValue) {
					minValue = successorValue;
					minBoard = successors.get(i);
				}
				beta = Math.min(beta, successorValue);
				if (beta <= alpha) {
					break;
				}
			}
			return new EvaluatedBoard(minBoard, minValue);
		} else {
			double minValue = Double.POSITIVE_INFINITY;
			Board minBoard = null;
			for (int i = 0; i < successors.size(); i++) {
				EvaluatedBoard evaluatedBoard = max(successors.get(i),
						depth - 1, alpha, beta);
				double successorValue = evaluatedBoard.value;
				if (successorValue < minValue) {
					minValue = successorValue;
					minBoard = successors.get(i);
				}
				beta = Math.min(beta, successorValue);
				if (beta <= alpha) {
					break;
				}
			}
			return new EvaluatedBoard(minBoard, minValue);
		}
	}

	/**
	 * Returns the best adjacent board state for this player
	 * 
	 * @param board
	 *            Current board state
	 * @param depth
	 *            How many more turns will be checked
	 * @param alpha
	 *            Best so far
	 * @param beta
	 *            Worst so far
	 */
	EvaluatedBoard max(Board board, int depth, double alpha, double beta) {
		List<Board> successors = board.successors();
		if (successors.isEmpty()) {
			return new EvaluatedBoard(board, evaluate(board));
		}
		if (depth == 1) {
			double maxValue = Double.NEGATIVE_INFINITY;
			Board maxBoard = null;
			for (int i = 0; i < successors.size(); i++) {
				double successorValue = evaluate(successors.get(i));
				if (successorValue > maxValue) {
					maxValue = successorValue;
					maxBoard = successors.get(i);
				}
				alpha = Math.max(alpha, successorValue);
				if (beta <= alpha) {
					break;
				}
			}
			return new EvaluatedBoard(maxBoard, maxValue);
		} else {
			double maxValue = Double.NEGATIVE_INFINITY;
			Board maxBoard = null;
			for (int i = 0; i < successors.size(); i++) {
				EvaluatedBoard evaluatedBoard = min(successors.get(i),
						depth - 1, alpha, beta);
				double successorValue = evaluatedBoard.value;
				if (successorValue > maxValue) {
					maxValue = successorValue;
					maxBoard = successors.get(i);
				}
				alpha = Math.max(alpha, successorValue);
				if (beta <= alpha) {
					break;
				}
			}
			return new EvaluatedBoard(maxBoard, maxValue);
		}
	}

	/**
	 * Evaluates the given board state. Returns a positive value if the position
	 * is favorable to this computer, a negative number if the position is
	 * favorable to its opponent, and 0 otherwise.
	 * 
	 * @param board
	 */
	abstract double evaluate(Board board);

	/**
	 * A board and it's associated value as determined by minimax.
	 */
	public static class EvaluatedBoard {
		Board board;
		double value;

		public EvaluatedBoard(Board board, double value) {
			this.board = board;
			this.value = value;
		}
	}

}
