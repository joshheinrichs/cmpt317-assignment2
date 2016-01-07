package assignment2;

import java.util.Scanner;

public class Game {

	static Board board = new Board();

	static Player black;
	static Player white;

	/**
	 * Sets up a game and handles taking turns.
	 */
	public static void main(String args[]) {
		System.out.println("Welcome to Pawned!\n");

		Scanner in = new Scanner(System.in);

		while (true) {
			System.out.print("Is the white player human (y/n)?: ");

			String answer = in.next();

			if (answer.equals("y")) {
				Game.white = new Human(Color.WHITE, board, in);
				break;
			} else if (answer.equals("n")) {

				System.out.print("Please enter the computer's difficutly: ");
				int difficulty = in.nextInt();

				System.out
						.print("Please enter the computer's heuristic (a/b/c): ");
				String heuristic = in.next();

				if (heuristic.equals("a")) {
					Game.white = new ComputerA(Color.WHITE, board, difficulty);
					break;
				} else if (heuristic.equals("b")) {
					Game.white = new ComputerB(Color.WHITE, board, difficulty);
					break;
				} else if (heuristic.equals("c")) {
					Game.white = new ComputerC(Color.WHITE, board, difficulty);
					break;
				} else {
					System.out
							.println("ERROR: Invalid value entered for heursitic");
				}
			} else {
				System.out
						.println("ERROR: Please enter 'y' for yes, or 'n' for no.");
			}
		}

		while (true) {
			System.out.print("Is the black player human (y/n)?: ");

			String answer = in.next();

			if (answer.equals("y")) {
				Game.black = new Human(Color.BLACK, board, in);
				break;
			} else if (answer.equals("n")) {
				System.out.print("Please enter the computer's difficutly: ");
				int difficulty = in.nextInt();
				System.out
						.print("Please enter the computer's heuristic (a/b/c): ");
				String heuristic = in.next();

				if (heuristic.equals("a")) {
					Game.black = new ComputerA(Color.BLACK, board, difficulty);
					break;
				} else if (heuristic.equals("b")) {
					Game.black = new ComputerB(Color.BLACK, board, difficulty);
					break;
				} else if (heuristic.equals("c")) {
					Game.black = new ComputerC(Color.BLACK, board, difficulty);
					break;
				} else {
					System.out
							.println("ERROR: Invalid value entered for heursitic");
				}
			} else {
				System.out
						.println("ERROR: Please enter 'y' for yes, or 'n' for no.");
			}
		}

		System.out.println("\n" + Game.board);

		while (!Game.board.isFinished()) {
			if (Game.board.turn == Color.WHITE) {
				System.out.println("WHITE'S TURN");
				Game.white.move();
			} else // (Game.board.turn == Color.BLACK)
			{
				System.out.println("BLACK'S TURN");
				Game.black.move();
			}

			System.out.println("\n" + Game.board);
		}

		Color winner = Game.board.winner();
		if (winner == null) {
			System.out.println("TIED!");
		} else {
			System.out.println(winner + " WON!");
		}
		
		in.close();
	}
}
