package assignment2;

import java.awt.Point;

public class Pawn implements Cloneable {

	Color color;
	Board board;
	Point position;
	boolean captured = false;

	/**
	 * Constructs a new pawn
	 */
	public Pawn(Color color, Board board, Point position) {
		this.color = color;
		this.board = board;
		this.position = position;
	}

	/**
	 * Constructs a deep clone of the given pawn
	 */
	public Pawn(Pawn pawn) {
		this.color = pawn.color;
		this.board = pawn.board;
		this.position = new Point(pawn.position.x, pawn.position.y);
		this.captured = pawn.captured;
	}

	/**
	 * Moves the pawn forward on the board
	 */
	public void forward() {
		assert (this.canMove()) : "Illegal move";

		board.positions[this.position.x][this.position.y] = null;
		if (this.isBlack()) {
			this.position.y++;
		} else // (this.isWhite())
		{
			this.position.y--;

		}
		board.positions[this.position.x][this.position.y] = this;
	}

	/**
	 * Moves the pawn forward and to the left, killing an enemy pawn
	 */
	public void captureLeft() {
		assert (this.canCaptureLeft()) : "Illegal move";

		board.positions[this.position.x][this.position.y] = null;
		if (this.isBlack()) {
			this.position.x--;
			this.position.y++;
		} else // (this.isWhite())
		{
			this.position.x--;
			this.position.y--;
		}
		board.positions[this.position.x][this.position.y].capture();
		board.positions[this.position.x][this.position.y] = this;
	}

	/**
	 * Moves a pawn forward and to the right, killing an enemy pawn
	 */
	public void captureRight() {
		assert (this.canCaptureRight()) : "Illegal move";

		board.positions[this.position.x][this.position.y] = null;
		if (this.isBlack()) {
			this.position.x++;
			this.position.y++;
		} else // (this.isWhite())
		{

			this.position.x++;
			this.position.y--;
		}
		board.positions[this.position.x][this.position.y].capture();
		board.positions[this.position.x][this.position.y] = this;
	}

	/**
	 * Kills this pawn
	 */
	public void capture() {
		captured = true;
	}

	/**
	 * Returns this pawn's position
	 */
	public Point getPosition() {
		return this.position;
	}

	/**
	 * Sets this pawn's position to the given point
	 */
	public void setPosition(Point position) {
		this.position = position;
	}

	/**
	 * Sets this pawn's position to the given point
	 */
	public void setPosition(int x, int y) {
		this.position.x = x;
		this.position.y = y;
	}

	/**
	 * Returns true if this pawn can move forward, false otherwise
	 */
	public boolean canMove() {
		return !captured
				&& ((this.isBlack()
						&& board.inRange(position.x, position.y + 1) && board
						.getPawn(position.x, position.y + 1) == null) || (this
						.isWhite() && board.inRange(position.x, position.y - 1) && board
						.getPawn(position.x, position.y - 1) == null));
	}

	/**
	 * Returns true if this pawn forward and to the left, killing an enemy pawn,
	 * false otherwise
	 */
	public boolean canCaptureLeft() {
		return !captured
				&& ((this.isBlack()
						&& board.inRange(position.x - 1, position.y + 1)
						&& board.getPawn(position.x - 1, position.y + 1) != null && board
						.getPawn(position.x - 1, position.y + 1).isWhite()) || (this
						.isWhite()
						&& board.inRange(position.x - 1, position.y - 1)
						&& board.getPawn(position.x - 1, position.y - 1) != null && board
						.getPawn(position.x - 1, position.y - 1).isBlack()));
	}

	/**
	 * Returns true if this pawn forward and to the right, killing an enemy
	 * pawn, false otherwise
	 */
	public boolean canCaptureRight() {
		return !captured
				&& ((this.isBlack()
						&& board.inRange(position.x + 1, position.y + 1)
						&& board.getPawn(position.x + 1, position.y + 1) != null && board
						.getPawn(position.x + 1, position.y + 1).isWhite()) || (this
						.isWhite()
						&& board.inRange(position.x + 1, position.y - 1)
						&& board.getPawn(position.x + 1, position.y - 1) != null && board
						.getPawn(position.x + 1, position.y - 1).isBlack()));
	}

	/**
	 * Returns true if this pawn is alive, false otherwise
	 */
	public boolean isCaptured() {
		return captured;
	}

	/**
	 * Returns true if this pawn is black, false otherwise
	 */
	public boolean isBlack() {
		return this.color == Color.BLACK;
	}

	/**
	 * Returns true if this pawn is white, false otherwise
	 */
	public boolean isWhite() {
		return this.color == Color.WHITE;
	}

	/**
	 * Returns a deep clone of a pawn
	 */
	public Pawn clone() {
		return new Pawn(this);
	}

}
