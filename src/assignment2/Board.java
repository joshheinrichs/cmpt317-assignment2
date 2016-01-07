package assignment2;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Board implements Cloneable {

	static final int BOARD_WIDTH = 5;
	static final int BOARD_HEIGHT = 6;

	Pawn[][] positions = new Pawn[BOARD_WIDTH][BOARD_HEIGHT];

	Pawn[] blackPawns = new Pawn[BOARD_WIDTH];
	Pawn[] whitePawns = new Pawn[BOARD_WIDTH];

	Color turn = Color.WHITE;

	/**
	 * Constructs a new board
	 */
	public Board() {
		for (int x = 0; x < BOARD_WIDTH; x++) {
			blackPawns[x] = new Pawn(Color.BLACK, this, new Point(x, 0));
			positions[x][0] = blackPawns[x];

			whitePawns[x] = new Pawn(Color.WHITE, this, new Point(x,
					BOARD_HEIGHT - 1));
			positions[x][BOARD_HEIGHT - 1] = whitePawns[x];
		}
	}

	/**
	 * Constructs a new board from the given board
	 */
	public Board(Board board) {
		for (int i = 0; i < BOARD_WIDTH; i++) {
			blackPawns[i] = board.blackPawns[i].clone();
			blackPawns[i].board = this;
			if (!blackPawns[i].isCaptured())
				positions[blackPawns[i].position.x][blackPawns[i].position.y] = blackPawns[i];

			whitePawns[i] = board.whitePawns[i].clone();
			whitePawns[i].board = this;
			if (!whitePawns[i].isCaptured())
				positions[whitePawns[i].position.x][whitePawns[i].position.y] = whitePawns[i];
		}

		this.turn = board.turn;
	}

	public void copy(Board board) {
		this.positions = board.positions;
		for (int i = 0; i < BOARD_WIDTH; i++) {
			blackPawns[i] = board.blackPawns[i];
			blackPawns[i].board = this;

			whitePawns[i] = board.whitePawns[i];
			whitePawns[i].board = this;
		}

		this.turn = board.turn;
	}

	public void reset() {
		for (int x = 0; x < BOARD_WIDTH; x++) {
			for (int y = 0; y < BOARD_HEIGHT; y++) {
				positions[x][y] = null;
			}
		}

		for (int x = 0; x < BOARD_WIDTH; x++) {
			blackPawns[x].setPosition(x, 0);
			positions[x][0] = blackPawns[x];

			whitePawns[x].setPosition(x, BOARD_HEIGHT - 1);
			positions[x][BOARD_HEIGHT - 1] = whitePawns[x];
		}

		this.turn = Color.WHITE;
	}

	public void move(Action action) {
		switch (action.move) {
		case FORWARD:
			getPawn(action.position).forward();
			break;
		case CAPTURE_LEFT:
			getPawn(action.position).captureLeft();
			break;
		case CAPTURE_RIGHT:
			getPawn(action.position).captureRight();
			break;
		case PASS:
			break;
		}

		nextTurn();
	}

	public void nextTurn() {
		if (turn == Color.WHITE) {
			turn = Color.BLACK;
		} else // (turn == Color.BLACK)
		{
			turn = Color.WHITE;
		}
	}

	/**
	 * Returns true if the game has finished, whether
	 */
	public boolean isFinished() {
		return (!canMove(Color.BLACK) && !canMove(Color.WHITE))
				|| this.winner() != null;
	}

	public boolean canMove(Color color) {
		if (color == Color.WHITE) {
			for (int i = 0; i < BOARD_WIDTH; i++) {
				if (whitePawns[i].canMove() || whitePawns[i].canCaptureLeft()
						|| whitePawns[i].canCaptureRight()) {
					return true;
				}
			}
			return false;
		} else // (color == Color.BLACK)
		{
			for (int i = 0; i < BOARD_WIDTH; i++) {
				if (blackPawns[i].canMove() || blackPawns[i].canCaptureLeft()
						|| blackPawns[i].canCaptureRight()) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Returns Color.WHITE if white has won, Color.BLACK if black has won, and
	 * null otherwise.
	 */
	public Color winner() {
		if (!canMove(Color.BLACK) && !canMove(Color.WHITE)) {
			int numBlackPawns = 0;
			int numWhitePawns = 0;

			for (int i = 0; i < BOARD_WIDTH; i++) {
				if (!blackPawns[i].isCaptured())
					numBlackPawns++;
				if (!whitePawns[i].isCaptured())
					numWhitePawns++;
			}

			if (numBlackPawns > numWhitePawns) {
				return Color.BLACK;
			} else if (numWhitePawns > numBlackPawns) {
				return Color.WHITE;
			} else {
				return null;
			}
		}

		for (int x = 0; x < BOARD_WIDTH; x++) {
			if (positions[x][0] != null && positions[x][0].isWhite()) {
				return Color.WHITE;
			}
			if (positions[x][BOARD_HEIGHT - 1] != null
					&& positions[x][BOARD_HEIGHT - 1].isBlack()) {
				return Color.BLACK;
			}
		}

		return null;
	}

	/**
	 * Returns true if the given point is within the board, false otherwise
	 */
	public boolean inRange(int x, int y) {
		return x >= 0 && x < BOARD_WIDTH && y >= 0 && y < BOARD_HEIGHT;
	}

	/**
	 * Returns true if the given point is within the board, false otherwise
	 */
	public boolean inRange(Point point) {
		return inRange(point.x, point.y);
	}

	/**
	 * Returns a pawn at the specified position on the board
	 */
	public Pawn getPawn(int x, int y) {
		return positions[x][y];
	}

	/**
	 * Returns a pawn at the specified position on the board
	 */
	public Pawn getPawn(Point point) {
		return getPawn(point.x, point.y);
	}

	public List<Board> successors() {
		List<Board> successors = new ArrayList<Board>();

		if (!this.isFinished()) {
			if (this.canMove(turn)) {
				Pawn pawns[];
				if (turn == Color.WHITE)
					pawns = this.whitePawns;
				else
					pawns = this.blackPawns;

				for (int i = 0; i < pawns.length; i++) {
					if (pawns[i].canMove()) {
						Board adjacentBoard = this.clone();
						Action action = new Action(pawns[i].position,
								Move.FORWARD);
						adjacentBoard.move(action);
						successors.add(adjacentBoard);
					}
					if (pawns[i].canCaptureLeft()) {
						Board adjacentBoard = this.clone();
						Action action = new Action(pawns[i].position,
								Move.CAPTURE_LEFT);
						adjacentBoard.move(action);
						successors.add(adjacentBoard);
					}
					if (pawns[i].canCaptureRight()) {
						Board adjacentBoard = this.clone();
						Action action = new Action(pawns[i].position,
								Move.CAPTURE_RIGHT);
						adjacentBoard.move(action);
						successors.add(adjacentBoard);
					}
				}
			} else {
				Board adjacentBoard = this.clone();
				Action action = new Action(null, Move.PASS);
				adjacentBoard.move(action);
				successors.add(adjacentBoard);
			}
		}

		return successors;
	}

	public String toString() {
		String result = "";

		result += "  | 0 1 2 3 4 \n";
		result += "--+-----------\n";

		for (int y = 0; y < BOARD_HEIGHT; y++) {
			result += y + " | ";
			for (int x = 0; x < BOARD_WIDTH; x++) {
				if (positions[x][y] == null) {
					result += '.';
				} else if (positions[x][y].isBlack()) {
					result += 'b';
				} else // (positions[i][j].isWhite())
				{
					result += 'w';
				}
				result += ' ';
			}
			result += '\n';
		}
		return result;
	}

	/**
	 * Returns a deep clone of a board
	 */
	public Board clone() {
		return new Board(this);
	}

	public static void main(String[] args) {
		Board board = new Board();
		System.out.println(board);
		List<Board> successors = board.successors();
		for (int i = 0; i < successors.size(); i++) {
			System.out.println(successors.get(i));
		}

		for (int j = 0; j < 10; j++) {
			System.out.println("\n\nTURN " + j + "\n");
			successors = successors.get(0).successors();
			for (int i = 0; i < successors.size(); i++) {
				System.out.println(successors.get(i));
			}
		}
	}
}
