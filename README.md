*OVERVIEW*

Reversi is a strategy board game for two players, typically played on an 8x8 uncheckered board. However, the following code creates reversi on a hexagonal plane, instead.
This creates more strategy, and generally, more fun! The drawback, of course, is the complexity of coding the typical game. 

The coordinate system, and some directional formulas used throughout the code (also credited within the Javadocs) is taken from: https://www.redblobgames.com/grids/hexagons/ 

The rules of Reversi are:
1) The board starts with 4 discs placed in the center positions.
2) Players take turns placing discs of their color on the board, with the goal of surrounding opponent's discs between their own discs horizontally, vertically or diagonally.
3) A disc is flipped when it is surrounded on two opposite sides by the opponent's color disc. All discs between the flanking discs are flipped to the color of the player who placed the last disc.
4) Valid moves are to place a disc on an empty square, flipping at least one opposing disc. If a player has no valid moves, they pass their turn.
5) The game ends when neither player has any valid moves. The player with the most discs of their color on the board wins.

For more, visit: https://en.wikipedia.org/wiki/Reversi

*CODE OVERVIEW* 

This project was undertaken with the adhering of OOP principles. As such, the following UML diagram could be useful in navigating the code (if one is curious about doing so).
Method signatures, fields, are purposefully asbent due to the lengthy nature of the game. Instead, summaries about what each components do are offered. 

[Blank diagram (2).pdf](https://github.com/russellvd/hexagonal-reversi/files/13867713/Blank.diagram.2.pdf)

The project followed the Model–view–controller pattern for its design. 

*GAMEPLAY*

To play, run the ReversiGame jar file. 

To highlight a cell, simply click on it. When a cell is highlighted, one can indicate a move by pressing enter. Otherwise, if you want to pass, 
simply press space bar. 

Screenshot of the beginning of the game:
![start_game_screenshot](https://github.com/russellvd/hexagonal-reversi/assets/60278300/da2376a9-e04e-447d-9de1-b7b73c460b12)

Screenshot of the two-player UI: 
![side-by-side games](https://github.com/russellvd/hexagonal-reversi/assets/60278300/fbfe54ae-1556-43ac-930a-a12d6ef40a9d)


*OTHER CONSIDERATIONS*

There are still features I am looking to add onto:
1) Scoring System
2) High score menu
3) Multiplayer capabilities








