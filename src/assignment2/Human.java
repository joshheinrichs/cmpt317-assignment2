package assignment2;

import java.awt.Point;
import java.util.Scanner;

public class Human extends Player {

	Scanner in;

	/**
	 * Constructs a new human player
	 * 
	 * @param color
	 *            Color of the human's pieces
	 * @param board
	 *            The board on which the human is playing
	 * @param in
	 *            The scanner from which user input is recieved
	 */
	Human(Color color, Board board, Scanner in) {
		super(color, board);
		this.in = in;
		assert (board != null);
	}

	/**
	 * {@inheritDoc} In this case, user input is taken to make moves.
	 */
	@Override
	void move() {

		while (true) {
			if (board.canMove(super.color)) {
				System.out.print("Select a piece (x y): ");

				int x = in.nextInt();
				int y = in.nextInt();

				Point point = new Point(x, y);

				if (board.inRange(point) && board.getPawn(point) != null
						&& board.getPawn(point).color == super.color) {
					Pawn pawn = board.getPawn(point);
					System.out.print("Select a move (f/l/r): ");
					String response = in.next();
					if (response.equals("f") && pawn.canMove()) {
						Action action = new Action(point, Move.FORWARD);
						board.move(action);
						break;
					} else if (response.equals("l") && pawn.canCaptureLeft()) {
						Action action = new Action(point, Move.CAPTURE_LEFT);
						board.move(action);
						break;
					} else if (response.equals("r") && pawn.canCaptureRight()) {
						Action action = new Action(point, Move.CAPTURE_RIGHT);
						board.move(action);
						break;
					} else {
						System.out.println("Error: Invalid move");
					}

				} else {
					System.out.println("Error: Invalid coordinate");
				}
			} else {
				System.out.println("No possible moves. Passing.");
			}
		}

	}

}
