package assignment2;

public abstract class Player {

	Color color;
	Board board;

	/**
	 * Constructs a new player
	 * 
	 * @param color
	 *            Color of the player, either BLACK or WHITE
	 * @param board
	 *            The board on which the player makes moves
	 */
	Player(Color color, Board board) {
		this.color = color;
		this.board = board;
	}

	/**
	 * Takes an action on the board, moving one of the pieces which matches its
	 * color.
	 */
	abstract void move();

}
