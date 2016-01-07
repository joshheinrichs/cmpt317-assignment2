# CMPT 371 Assignment 2 - Pawned!
###Problem Description
Pawned! is a simple game played on a 5×6 board. The game has two players, the “white” player, and the “black” player. Each player starts with 5 pawns on their own side, in the formation shown below:
```
b b b b b
. . . . .
. . . . .
. . . . .
. . . . .
w w w w w
```

Where 'b's represent black pawns, 'w's represent white pawns, and '.'s represent empty spaces on the board.

Like in chess, the white player goes first, and each player alternate taking turns which consist of a single move.  Each pawn in Pawned! has up to three movement options available to them on a given turn:
* Move forward
* Capture an enemy pawn diagonally (forward and to the left)
* Capture an enemy pawn diagonally (forward and to the right)

A pawn cannot capture an enemy piece directly in front of it, nor move diagonally if it is not capturing a piece. If none of a player's pawns are capable of moving, the player may pass. If any pawns are capable of moving, the player must move a pawn.

A player wins in the following cases:
* One of their pawns reaches their opponent’s starting row
* Neither player can move, and the they have a larger number of pawns currently on the board
* If neither player can move, and neither of these cases have been met, the game is considered a draw.

The goal of this project is to create an AI capable of playing Pawned! with a respectable degree of competency, and learn about creating AI which can play games with perfect information in the process.


###Solution Description
Pawned! falls within a subset of games which are deterministic, meaning that there is no luck or chance involved, fully observable, meaning that both players have complete information about the game's state, and competitive, meaning that utility values at the end of the game sum to 0.

The most basic approach to creating an AI capable of playing a game of this type is to map the problem to a state space search, where the “solution” is reaching an end-game state where the computer’s utility value is higher than its opponent's (+1 for winning, -1 for losing, 0 for tie). Each possible move on a given turn can be mapped as a branch to an adjacent state, and the resulting tree structure can be searched to find these solution states. 

However, now that we have this tree structure, how do we find a solution? We can't necessarily reach any end-game state which has a utility value of 1, since we do not control the moves made by our opponent. To account for this, we use a strategy called “minimaxing”. The idea is that each level of the tree is associated with a player, either min or max. Max's goal is to maximize the utility value reached at an end-state, and min's goal is to minimize the utility value reached at an end-state. In the case of Pawned!, min and max alternate at each level of the tree, as the two players alternate taking turns. The states chosen by max represent the decisions made by the computer as it attempts to win, and the states chosen by min represent the decisions made by its opponent as it too attempts to win the game.

Given infinite computational power and space, this would be a good solution. We could use minimaxing to represent optimal play on both sides, and find largest reachable utility value which is reachable assuming optimal play by our opponent, and the best solution could be searched for each turn to adapt to suboptimal moves made by our opponent to capitalize on mistakes and reach even better utility values. However even for a relatively simple game such as Pawned!, finding the solution cannot be computed within a reasonable amount of time. A game could take up to theoretical maximum of 40 turns, and with approximately 5 available actions per turn, this would create 5^40 states, far outside the range of computability for an average computer.

To deal with this issue, a few techniques are used. The first is using an iterative deepening search, and an evaluation function. Although we cannot realistically compute a solution, if we limit the depth to which we search to n, we can still play the game, thinking n turns ahead of each move, meaning that if a solution can be reached by the minimax algorithm within n moves, the computer will find and play towards it. However, if a solution cannot be reached within the next n turns, what do we do? This is where an evaluation function comes into play. 

When reaching a terminal state in an iterative deepening search, even if a state is not the end-state, an evaluation function can be used to calculate an estimated utility value for the state, on which a minimax algorithm can be ran. In the case of Pawned!, this can be as simple checking whether you have a greater number of pawns than your opponent, or be more complicated, taking into account the position of various pawns on the board. The more accurate the evaluation function is, the better the computer will play. The combination of an iterative deepening search and an evaluation function, allows the computer to make a reasonable move towards a solution state, even when a solution state cannot be found.

The second technique used to speed up search is alpha beta pruning. The basic idea with this is that you know that you are tying to minimize the utility value on some turns, and maximize them on others, and because of this, certain branches of the tree can be ignored knowing that min or max will never take that path. At each depth of the tree, an alpha and beta value is kept, where alpha represents the best choice for max, and beta represents the best choice for min. If the computer is attempting to minimize, and the value it comes across is less than alpha, it knows that at max, the alpha value will be chosen, and so it will stop. If the computer is attempting to maximize, and the value it comes across is greater than beta, it knows that at min, the beta value will be taken, and so it again will stop. Cutting out these needless computation decreases the exponent of the time complexity by roughly a half, and as such has large ramifications in performance, allowing for deeper searches.

By employing techniques, we can create a program capable of quickly deciding upon good moves by both estimating the value of a board and thinking many turns ahead.

###Implementation Description
To implement my solution, I started by first making a version of Pawned! which was playable by two humans. To do this, I created five major classes: Game, Board, Pawn, Player, and Human, as well as the enums Color and Move. The Game class handled setup of the game, delegating turns to players, and printed out the board after each turn. The Board class represented the game's state, and contained information about the position of pawns. The pawn class contained logic for individual pawns on what movements it could legally make given its position in relation to other pawns on the board. The abstract Player class contained information about the player's color, as well as an abstract function Player.move(), which which dictated the movement of pawns on the board. The Player class was made abstract so that it could be implemented by both a Human and Computer, allowing for flexibile configurations when setting up the game, allowing human versus human, human versus computer, and computer versus computer scenarios. The Human class implemented Player.move(), allowing for user input to dictate movement for pawns on the board which matched the player's color. After the game was implemented and made playable, extensive manual testing was done to ensure that only legal movements as dictated by the rules of Pawned! were possible. 

Once the basic game was ensured to be working without issues, I created a simple successor function for Board, Board.successors(), which would return a list of boards, each of which was the current board with one of the possible legal moves from the current turn applied to it. Because this was based off of the Pawn's legal movement logic which had already been extensively tested, only minimal testing was required for the function.

After that, I created the Computer class which also implemented Player.move(), as well as adding an abstract Computer.evaluate(Board) function, which would allow for multiple versions of the computer, each of which had a unique heuristic. The Computer class contained the minimax algorithm, which was initially implemented with IDS, where there depth was determined upon construction of the computer. To test the minimax algorithm playing Pawned!, I created a basic evaluation function for ComputerA which returned 1 if the computer had won, -1 if its opponent had won, and 0 otherwise. After the minimax algorithm was determined to be functioning as expected, alpha beta pruning was added, and decisions were compared to ensure that behaviour was the same.

In total, three separate evaluation functions for computers were implemented ComputerA, ComputerB, and ComputerC. ComputerA's evaluation function was the most basic, essentially giving a positive value to winning, a negative value to losing, and a value of 0 to all other states. ComputerB employed pawn counting, valuing a state as it's number of pawns minus it's enemies number of pawns if a winning or losing state had not been reached. The third evaluation function implemented in ComputerC tried to add value for board presence by adding value to pawns being pushed up on the board, and weighing central pawns more heavily so the computer would aim for control of the centre of the board.


###Results
Because of the simplicity of Pawned!, even with a very simple evaluation function, only giving value to winning states and preventing losing states, the computer is capable of playing very well, and is able to beat me, as seen in 'output_1.txt'. This is in large part because of the limited branching when compared to other games such as chess, since in Pawned! there are usually only about 5 possible moves to be made. Because of this, when using IDS, ComputerA's evaluation function and alpha beta pruning, the computer is capable of playing 20 turns ahead while still making moves in under 10 seconds. For each increase in depth that the computer is allowed to search, the time roughly doubles. 

With the more complicated evaluation functions found in ComputerB and ComputerC, evaluation seems to take about 4 times longer. This is likely due to less efficient alpha beta pruning, since there are additional possible values, so branches are not ignored as quickly. If I were to attempt this project again, trying to optimize the search for alpha beta pruning would be an interesting area to explore to improve the AI.

As the number of turns decreases, the computer plays more poorly, as it is not able to play as many turns ahead. The lower the number of turns the computer plays ahead, the more important the evaluation function becomes. The evaluation function in ComputerB which counts pawns consistently beats ComputerA when both computers play around 10 turns ahead regardless of color. However when ComputerA thinks about 5 additional turns ahead of ComputerB, it will consistently win, regardless of color as well. The main goal of an evaluation function is to approximate optimal play until a solution state is within range, so if ComputerA with its simple win-loss heuristic can find a solution state at earlier turns, it doesn't really need to worry about trading pawns effectively like ComputerB, or gaining board control like ComputerC, since it's figures out whether it's won or lost before the first pawn is even traded. 

At higher depths, such around 20, black seems to consistently win regardless of its opponent's heuristic or depth. I suspect that this means that the first few movements made do not really matter. I did not have time to max out the computer's depth to the point where it finds a solution on the first turn, but when both computers were increased to a depth of 23, the game was determined to be a win for black by turn 4. When playing at this depth, turns took roughly 40 seconds to compute. I estimate that a solution to the game can be found at a depth of 27, which would take my program several hours to compute.

An example of two AIs playing each other can be seen in 'output_2.txt'. I think this provides an interesting case study into how the AI works, because while it makes moves that look incorrect, it is actually behaving in a logical manner. In this game, two ComputerAs are constructed, both of which are playing 20 moves ahead. After the 5th move is made, the white player realizes that it will lose to optimal play within the next 20 moves. After that, it starts ignoring obvious moves which will lead to its opponents victory, since it doesn't care whether it loses in 1 turn or 20. This may be an interesting area to improve in the future, since the opponent, especially when human, isn't always guaranteed to play optimally, so delaying the victory might give the AI an opportunity to capitalize on a mistake. 

###Conclusions
I think that minimaxing, with IDS, alpha beta pruning, and an evaluation function is a very valid way of creating a computer capable of playing a game, at least in the case of competitive, deterministic games with perfect information. One interesting benefit of using IDS is that it allows for scalable difficulty to arise pretty naturally, since you can change how well a computer plays just by increasing or decreasing its search's depth.

My AI is definitely capable of beating human players, even when restricted to making moves in under 5 seconds since it can make movements while thinking 19 turns ahead. Obviously whether it can beat a human depends upon the human it plays, but I think that it can beat above average human players pretty handily, even without a sophisticated evaluation function, though when the depth is limited to about 10, the evaluation function does play an important role.

Pawned! seems to favour black, as when playing ComputerA against ComputerA where both think 23 turns ahead, black found a winning path within 4 turns. While I didn't have time to push the depth further, I believe if I were to set it to about 27 turns deep, black would find a solution on its first turn, however selecting a move when checking this deep would take the computer roughly 5 hours.

Two interesting areas to expand this project in would be to improve performance of alpha beta pruning for more complicated evaluation functions by changing the order in which the branches that are searched, and improving play against players which are in an advantageous position, but make mistakes that can be capitalized upon.
