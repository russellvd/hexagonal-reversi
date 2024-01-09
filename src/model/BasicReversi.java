package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BasicReversi represents an instance of ReversiModel containing the logic and computations
 * for a 2-player game of Reversi.
 * The model has no visual representation without the TextualView
 * and does not take live input without the Controller.
 */
public final class BasicReversi implements ReversiModel {
  private PlayColor playColor; // this represents color of the player and tracks whose turn it is
  private Map<Coordinate, Cell> board;
  private final int boardSize; // the length of each side of the board, in cells
  // INVARIANT: boardSize must be at least 3
  private int passed; // keeps track of how many times the players have passed in a row
  private ModelStatus status;
  private List<Listener> listeners;



  /**
   * Constructs a BasicReversi instance of ReversiModel where the boardSize determines the length
   * of the sides of the board.
   * Throws an IllegalArgumentException if the boardSize is less than 3 because a reasonable
   * game of Reversi cannot be played with any smaller board.
   * Initializes the playColor to BLACK because black plays first.
   * Initializes the boardSize to the given size.
   * Initializes the board by first generating a square grid of Cells based on the boardSize and
   * removing any cells that do not belong on the hexagonal board based on their q and r values.
   * Initializes passed to 0 since no players can possibly have passed yet.
   *
   * @param boardSize represents length in Cells of every side of the board
   */
  public BasicReversi(int boardSize) {
    if (boardSize < 3) { // reset this to 3
      throw new IllegalArgumentException("board size must be at least 3");
    }
    this.boardSize = boardSize;
    this.playColor = PlayColor.BLACK;
    this.board = new HashMap<>();
    // q and r represent the values of the Coordinates for the grid
    // using an axial coordinate system as described on the provided website
    for (int q = -boardSize + 1; q < boardSize; q++) {
      for (int r = -boardSize + 1; r < boardSize; r++) {
        // if the absolute value of q + r is greater than or equal to board size,
        // those cells do not fit in the hexagonal grid, so they will not be put on the board
        if (Math.abs(q + r) < boardSize) {
          board.put(new Coordinate(q, r), Cell.EMPTY);
        }
      }
    }
    // initialize pass to false since no one has passed yet
    this.passed = 0;
    // initialize first cells on the board to provide a fair start to both players
    board.replace(new Coordinate(1, 0), Cell.BLACK);
    board.replace(new Coordinate(0, -1), Cell.BLACK);
    board.replace(new Coordinate(-1, 1), Cell.BLACK);
    board.replace(new Coordinate(1, -1), Cell.WHITE);
    board.replace(new Coordinate(-1, 0), Cell.WHITE);
    board.replace(new Coordinate(0, 1), Cell.WHITE);
  }

  /**
   * The BasicReversi class represents the core logic for a simplified version
   * of the Reversi (Othello) game. It initializes the game with a specified board size,
   * default play color (BLACK), and a provided ModelStatus to track the game state.
   * The class ensures that the board size is valid (at least 3), throwing an
   * IllegalArgumentException if an invalid size is provided. It utilizes a HashMap to
   * manage the game board, where each hexagon is represented by its coordinates.
   * The playColor is initially set to BLACK, and the listeners are initialized as an
   * empty ArrayList to store objects interested in receiving updates about the game state.
   * This class is part of the broader Reversi game implementation and provides essential
   * functionality for initializing and managing the game state.
   * @param boardSize The size of the game board, must be at least 3.
   * @param status    The ModelStatus object to track the overall game state.
   */
  public BasicReversi(int boardSize, ModelStatus status) {
    if (boardSize < 3) { // reset this to 3
      throw new IllegalArgumentException("board size must be at least 3");
    }
    this.boardSize = boardSize;
    this.playColor = PlayColor.BLACK;
    this.board = new HashMap<>();
    this.status = status;
    this.listeners = new ArrayList<>();

    // q and r represent the values of the Coordinates for the grid
    // using an axial coordinate system as described on the provided website
    for (int q = -boardSize + 1; q < boardSize; q++) {
      for (int r = -boardSize + 1; r < boardSize; r++) {
        // if the absolute value of q + r is greater than or equal to board size,
        // those cells do not fit in the hexagonal grid, so they will not be put on the board
        if (Math.abs(q + r) < boardSize) {
          board.put(new Coordinate(q, r), Cell.EMPTY);
        }
      }
    }
    // initialize pass to false since no one has passed yet
    this.passed = 0;
    // initialize first cells on the board to provide a fair start to both players
    board.replace(new Coordinate(1, 0), Cell.BLACK);
    board.replace(new Coordinate(0, -1), Cell.BLACK);
    board.replace(new Coordinate(-1, 1), Cell.BLACK);
    board.replace(new Coordinate(1, -1), Cell.WHITE);
    board.replace(new Coordinate(-1, 0), Cell.WHITE);
    board.replace(new Coordinate(0, 1), Cell.WHITE);
  }


  @Override
  public void move(int q, int r) throws IllegalArgumentException, IllegalStateException {
    if (!validMoveArgs(q, r)) { // invalid arguments checked in helper method
      throw new IllegalArgumentException("Arguments are out of valid board range");
    }
    if (this.hasNoMoves(this.playColor)) { // player is forced to pass if no moves available
      pass();
    } else if (!validMoveLogic(q, r, this.playColor)) { // exception thrown if move not allowed
      throw new IllegalStateException("Move is not allowed");
    } else { // this case means arguments are valid, player has at least 1 valid move
      Coordinate coord = new Coordinate(q, r);
      this.passed = 0; // this is the most recent move, so the pass counter is reset
      Cell target; // the Cell that the player wants to make a move onto
      if (playColor.equals(PlayColor.BLACK)) {
        target = Cell.BLACK;
      } else {
        target = Cell.WHITE;
      }
      /* flip all necessary cells, based on any "flows" that are valid
      where a "flow" is a direct line from the target cell to another cell of the same color
      with cells of the opposite color in between and no empty cells in between.
       */
      // flip any valid flows based on q value
      this.flipQ(coord, target, q, r);
      // flip any valid flows based on s value
      // where 's' is a third coordinate value that is determined by [s = -q - r]
      this.flipS(coord, target, q, r);
      // flip any valid flows based on r value
      this.flipR(coord, target, q, r);
      this.switchTurn(); // switch the turn to the other PlayerTurn
      status.updateStatus(this);
      for (Listener listener : listeners) {
        listener.update();
      }
    }
  }

  /**
   * The flipQ method is a helper for the move() method that, if a valid "flow" is found
   * where the other cell of the same color has the same Q value as the target cell.
   *
   * @param coord  the Coordinate key of the target cell
   * @param target the Cell value of the target cell, which is either BLACK, WHITE, or EMPTY
   * @param q      the q value of the target cell
   * @param r      the r value of the target cell
   */
  private void flipQ(Coordinate coord, Cell target, int q, int r) {
    Map<Coordinate, Cell> matches;
    // only perform inside content if there is at least 1 match in Q flow to target from above
    if (!this.findQPathsUp(coord, target).isEmpty()) {
      matches = this.findQPathsUp(coord, target); // list of matches in this direction
      for (int curQ = -boardSize + 1; curQ < boardSize; curQ++) { // q value to iterate
        for (int curR = -boardSize + 1; curR < boardSize; curR++) { // r value to iterate
          if (matches.containsKey(new Coordinate(curQ, curR))) {
            Coordinate match = new Coordinate(curQ, curR); // Coordinate of the match cell
            int matchS = -match.getQ() - match.getR(); // s value of the match cell
            int matchR = match.getR(); // r value of the match cell
            while (matchS >= -q - r && matchR <= r) {
              this.changeCell(-matchS - matchR, matchR, target);
              matchS--; // decrease s value and increase r value to move back down to target
              matchR++;
            }
          }
        }
      }
    }
    // only perform inside content if there is at least 1 match in Q flow to target from below
    if (!this.findQPathsDown(coord, target).isEmpty()) {
      matches = this.findQPathsDown(coord, target);
      for (int curQ = -boardSize + 1; curQ < boardSize; curQ++) {
        for (int curR = -boardSize + 1; curR < boardSize; curR++) {
          if (matches.containsKey(new Coordinate(curQ, curR))) {
            Coordinate match = new Coordinate(curQ, curR);
            int matchS = -match.getQ() - match.getR();
            int matchR = match.getR();
            while (matchS <= -q - r && matchR >= r) {
              this.changeCell(-matchS - matchR, matchR, target);
              matchS++; // increase s value and decrease r value to move back up to target
              matchR--;
            }
          }
        }
      }
    }
  }

  /**
   * The flipS method is a helper for the move() method that, if a valid "flow" is found
   * where the other cell of the same color has the same S value as the target cell.
   *
   * @param coord  the Coordinate key of the target cell
   * @param target the Cell value of the target cell, which is either BLACK, WHITE, or EMPTY
   * @param q      the q value of the target cell
   * @param r      the r value of the target cell
   */
  private void flipS(Coordinate coord, Cell target, int q, int r) {
    Map<Coordinate, Cell> matches;
    // only perform inside content if there is at least 1 match in S flow to target from above
    if (!this.findSPathsUp(coord, target).isEmpty()) {
      matches = this.findSPathsUp(coord, target);
      for (int curQ = -boardSize + 1; curQ < boardSize; curQ++) {
        for (int curR = -boardSize + 1; curR < boardSize; curR++) {
          if (matches.containsKey(new Coordinate(curQ, curR))) {
            Coordinate match = new Coordinate(curQ, curR);
            int matchQ = match.getQ();
            int matchR = match.getR();
            while (matchQ >= q && matchR <= r) {
              this.changeCell(matchQ, matchR, target);
              matchQ--; // decrease q value and increase r value to move back down to target
              matchR++;
            }
          }
        }
      }
    }
    // only perform inside content if there is at least 1 match in s flow to target from below
    if (!this.findSPathsDown(coord, target).isEmpty()) {
      matches = this.findSPathsDown(coord, target);
      for (int curQ = -boardSize + 1; curQ < boardSize; curQ++) {
        for (int curR = -boardSize + 1; curR < boardSize; curR++) {
          if (matches.containsKey(new Coordinate(curQ, curR))) {
            Coordinate match = new Coordinate(curQ, curR);
            int matchQ = match.getQ();
            int matchR = match.getR();
            while (matchQ <= q && matchR >= r) {
              this.changeCell(matchQ, matchR, target);
              matchQ++; // increase q value and decrease r value to move back up to target
              matchR--;
            }
          }
        }
      }
    }
  }

  /**
   * The flipR method is a helper for the move() method that, if a valid "flow" is found
   * where the other cell of the same color has the same R value as the target cell.
   *
   * @param coord  the Coordinate key of the target cell
   * @param target the Cell value of the target cell, which is either BLACK, WHITE, or EMPTY
   * @param q      the q value of the target cell
   * @param r      the r value of the target cell
   */
  private void flipR(Coordinate coord, Cell target, int q, int r) {
    Map<Coordinate, Cell> matches;
    // only perform inside content if there is at least 1 match in R flow to target from right
    if (!this.findRPathsRight(coord, target).isEmpty()) {
      matches = this.findRPathsRight(coord, target);
      for (int curQ = -boardSize + 1; curQ < boardSize; curQ++) {
        for (int curR = -boardSize + 1; curR < boardSize; curR++) {
          if (matches.containsKey(new Coordinate(curQ, curR))) {
            Coordinate match = new Coordinate(curQ, curR);
            int matchQ = match.getQ();
            int matchS = -match.getQ() - match.getR();
            while (matchQ >= q && matchS <= -q - r) {
              this.changeCell(matchQ, -matchS - matchQ, target);
              matchQ--; // decrease q value and increase s value to move back left to target
              matchS++;
            }
          }
        }
      }
    }
    // only perform inside content if there is at least 1 match in R flow to target from left
    if (!this.findRPathsLeft(coord, target).isEmpty()) {
      matches = this.findRPathsLeft(coord, target);
      for (int curQ = -boardSize + 1; curQ < boardSize; curQ++) {
        for (int curR = -boardSize + 1; curR < boardSize; curR++) {
          if (matches.containsKey(new Coordinate(curQ, curR))) {
            Coordinate match = new Coordinate(curQ, curR);
            int matchQ = match.getQ();
            int matchS = -match.getQ() - match.getR();
            while (matchQ <= q && matchS >= -q - r) {
              this.changeCell(matchQ, -matchS - matchQ, target);
              matchQ++; // increase q value and decrease s value to move back right to target
              matchS--;
            }
          }
        }
      }
    }
  }

  @Override
  public boolean validMoveLogic(int q, int r, PlayColor p) {
    Cell turnColor; // the necessary color of the other cell based on the player whose turn it is
    if (p.equals(PlayColor.BLACK)) {
      turnColor = Cell.BLACK;
    } else {
      turnColor = Cell.WHITE;
    }
    // if the target cell is not empty, the move is automatically invalid
    if (!this.getCell(q, r).equals(Cell.EMPTY)) {
      return false;
    }
    // this section checks all possible flows for the target
    // to check if there are any Q flows
    if (!this.findQPathsUp(new Coordinate(q, r), turnColor).isEmpty()
            || !this.findQPathsDown(new Coordinate(q, r), turnColor).isEmpty()) {
      return true;
    }
    // to check if there are any R flows
    if (!this.findRPathsLeft(new Coordinate(q, r), turnColor).isEmpty()
            || !this.findRPathsRight(new Coordinate(q, r), turnColor).isEmpty()) {
      return true;
    }
    // to check if there are any S flows, otherwise there are no flows so false is returned
    return !this.findSPathsUp(new Coordinate(q, r), turnColor).isEmpty()
            || !this.findSPathsDown(new Coordinate(q, r), turnColor).isEmpty();
  }

  /**
   * The findQPathsDown method is a helper for the validMoveLogic method which determines
   * if there are any flows leading from the target downwards.
   *
   * @param coord  the Coordinate key of the target
   * @param target the Cell value of the target
   * @return a HashMap of Coordinate, Cell containing any cells with the same color as the target in
   *         the proper direction with cells of the opposite color in between and no empty cells in
   *         between. We chose to use a HashMap instead of just one Coordinate because it is
   *         possible for multiple cells of the matching color to exist in the flow without being
   *         the last cell.
   */

  @Override
  public Map<Coordinate, Cell> findQPathsDown(Coordinate coord, Cell target) {
    Map<Coordinate, Cell> matches = new HashMap<>();
    Cell opposite;
    if (target.equals(Cell.BLACK)) {
      opposite = Cell.WHITE;
    } else {
      opposite = Cell.BLACK;
    }
    int numOpposite = 0;
    int targetR = coord.getR() + 1;
    int targetS = -coord.getQ() - coord.getR() - 1;
    while (targetR < boardSize && targetS > -boardSize) {
      if (this.verify(-targetS - targetR, targetR)) {
        // if the next square is not the opposite color
        Cell neighborCell = board.get(new Coordinate(-targetS - targetR, targetR));
        if (!neighborCell.equals(opposite)) {
          if (neighborCell.equals(target)) {
            matches.put(new Coordinate(-targetS - targetR, targetR), neighborCell);
          } else {
            break;
          }
        } else {
          numOpposite++;
        }
      }
      targetS--;
      targetR++;
    }
    if (numOpposite == 0) {
      matches.clear();
    }
    return matches;
  }

  /**
   * The findQPathsUp method is a helper for the validMoveLogic method which determines
   * if there are any flows leading from the target upwards.
   *
   * @param coord  the Coordinate key of the target
   * @param target the Cell value of the target
   * @return a HashMap of Coordinate, Cell containing any cells with the same color as the target in
   *         the proper direction with cells of the opposite color in between and no empty cells in
   *         between. We chose to use a HashMap instead of just one Coordinate because it is
   *         possible for multiple cells of the matching color to exist in the flow without being
   *         the last cell.
   */
  @Override
  public Map<Coordinate, Cell> findQPathsUp(Coordinate coord, Cell target) {
    Map<Coordinate, Cell> matches = new HashMap<>();
    Cell opposite;
    if (target.equals(Cell.BLACK)) {
      opposite = Cell.WHITE;
    } else {
      opposite = Cell.BLACK;
    }
    int numOpposite = 0;
    int targetR = coord.getR() - 1;
    int targetS = -coord.getQ() - coord.getR() + 1;
    while (targetR > -boardSize && targetS < boardSize) {
      if (this.verify(-targetS - targetR, targetR)) {
        // if the next square is not the opposite color
        Cell neighborCell = board.get(new Coordinate(-targetS - targetR, targetR));
        if (!neighborCell.equals(opposite)) {
          if (neighborCell.equals(target)) {
            matches.put(new Coordinate(-targetS - targetR, targetR), neighborCell);
          } else {
            break;
          }
        } else {
          numOpposite++;
        }
      }
      targetS++; // INCREASE S
      targetR--; // DECREASE R
    }
    if (numOpposite == 0) {
      matches = new HashMap<>();
    }
    return matches;
  }

  /**
   * The findRPathsRight method is a helper for the validMoveLogic method which determines
   * if there are any flows leading from the target towards the right.
   *
   * @param coord  the Coordinate key of the target
   * @param target the Cell value of the target
   * @return a HashMap of Coordinate, Cell containing any cells with the same color as the target in
   *         the proper direction with cells of the opposite color in between and no empty cells in
   *         between. We chose to use a HashMap instead of just one Coordinate because it is
   *         possible for multiple cells of the matching color to exist in the flow without being
   *         the last cell.
   */
  @Override
  public Map<Coordinate, Cell> findRPathsRight(Coordinate coord, Cell target) {
    Map<Coordinate, Cell> matches = new HashMap<>();
    Cell opposite;
    if (target.equals(Cell.BLACK)) {
      opposite = Cell.WHITE;
    } else {
      opposite = Cell.BLACK;
    }
    int numOpposite = 0; // tracks whether any opposite-color cells have been found
    int targetS = -coord.getQ() - coord.getR() - 1;
    int targetQ = coord.getQ() + 1;
    while (targetQ < boardSize && targetS > -boardSize) {
      if (this.verify(targetQ, -targetS - targetQ)) {
        // if the next square is not the opposite color
        Cell neighborCell = board.get(new Coordinate(targetQ, -targetS - targetQ));
        if (!neighborCell.equals(opposite)) {
          if (neighborCell.equals(target)) {
            matches.put(new Coordinate(targetQ, -targetS - targetQ), neighborCell);
          } else {
            break;
          }
        } else {
          numOpposite++;
        }
      }
      targetS--;
      targetQ++;
    }
    if (numOpposite == 0) {
      matches = new HashMap<>();
    }
    return matches;
  }

  /**
   * The findRPathsLeft method is a helper for the validMoveLogic method which determines
   * if there are any flows leading from the target towards the left.
   *
   * @param coord  the Coordinate key of the target
   * @param target the Cell value of the target
   * @return a HashMap of Coordinate, Cell containing any cells with the same color as the target in
   *         the proper direction with cells of the opposite color in between and no empty cells in
   *         between. We chose to use a HashMap instead of just one Coordinate because it is
   *         possible for multiple cells of the matching color to exist in the flow without being
   *         the last cell.
   */

  @Override
  public Map<Coordinate, Cell> findRPathsLeft(Coordinate coord, Cell target) {
    Map<Coordinate, Cell> matches = new HashMap<>();
    Cell opposite;
    if (target.equals(Cell.BLACK)) {
      opposite = Cell.WHITE;
    } else {
      opposite = Cell.BLACK;
    }
    int numOpposite = 0; // tracks whether any opposite-color cells have been found
    int targetS = -coord.getQ() - coord.getR() + 1;
    int targetQ = coord.getQ() - 1;
    while (targetQ > -boardSize && targetS < boardSize) {
      // if the next square is not the opposite color
      if (this.verify(targetQ, -targetS - targetQ)) {
        Cell neighborCell = board.get(new Coordinate(targetQ, -targetS - targetQ));
        if (!neighborCell.equals(opposite)) {
          if (neighborCell.equals(target)) {
            matches.put(new Coordinate(targetQ, -targetS - targetQ), neighborCell);
          } else {
            break;
          }
        } else {
          numOpposite++;
        }
      }
      targetS++; // INCREASE S
      targetQ--; // DECREASE Q
    }
    if (numOpposite == 0) {
      matches = new HashMap<>();
    }
    return matches;
  }

  /**
   * The findSPathsUp method is a helper for the validMoveLogic method which determines
   * if there are any flows leading from the target upwards.
   *
   * @param coord  the Coordinate key of the target
   * @param target the Cell value of the target
   * @return a HashMap of Coordinate, Cell containing any cells with the same color as the target in
   *         the proper direction with cells of the opposite color in between and no empty cells in
   *         between. We chose to use a HashMap instead of just one Coordinate because it is
   *         possible for multiple cells of the matching color to exist in the flow without being
   *         the last cell.
   */

  @Override
  public Map<Coordinate, Cell> findSPathsUp(Coordinate coord, Cell target) {
    Map<Coordinate, Cell> matches = new HashMap<>();
    Cell opposite;
    if (target.equals(Cell.BLACK)) {
      opposite = Cell.WHITE;
    } else {
      opposite = Cell.BLACK;
    }
    int numOpposite = 0; // tracks whether any opposite-color cells have been found
    int targetQ = coord.getQ() + 1;
    int targetR = coord.getR() - 1;
    while (targetQ < boardSize && targetR > -boardSize) {
      // if the next square is not the opposite color
      if (this.verify(targetQ, targetR)) {
        Cell neighborCell = board.get(new Coordinate(targetQ, targetR));
        if (!neighborCell.equals(opposite)) {
          if (neighborCell.equals(target)) {
            matches.put(new Coordinate(targetQ, targetR), neighborCell);
          } else {
            break;
          }
        } else {
          numOpposite++;
        }
      }
      targetR--;
      targetQ++;
    }
    if (numOpposite == 0) {
      matches = new HashMap<>();
    }
    return matches;
  }

  /**
   * The findSPathsUp method is a helper for the validMoveLogic method which determines
   * if there are any flows leading from the target downwards.
   *
   * @param coord  the Coordinate key of the target
   * @param target the Cell value of the target
   * @return a HashMap of Coordinate, Cell containing any cells with the same color as the target in
   *         the proper direction with cells of the opposite color in between and no empty cells in
   *         between. We chose to use a HashMap instead of just one Coordinate because it is
   *         possible for multiple cells of the matching color to exist in the flow without being
   *         the last cell.
   */
  @Override
  public Map<Coordinate, Cell> findSPathsDown(Coordinate coord, Cell target) {
    Map<Coordinate, Cell> matches = new HashMap<>();
    Cell opposite;
    if (target.equals(Cell.BLACK)) {
      opposite = Cell.WHITE;
    } else {
      opposite = Cell.BLACK;
    }
    int numOpposite = 0; // tracks whether any opposite-color cells have been found
    int targetQ = coord.getQ() - 1;
    int targetR = coord.getR() + 1;
    /* iterates through all until either a match is found or the move is invalid
    move is invalid if: no target cell at the end, no opposite cell in between,
    empty cell anywhere
    only the 'match' cell (same color as target) is added to the matches HashMap
     */
    while (targetQ > -boardSize && targetR < boardSize) {
      // if the next square is not the opposite color
      if (this.verify(targetQ, targetR)) {
        Cell neighborCell = board.get(new Coordinate(targetQ, targetR));
        if (!neighborCell.equals(opposite)) {
          if (neighborCell.equals(target)) {
            matches.put(new Coordinate(targetQ, targetR), neighborCell);
          } else {
            break;
          }
        } else {
          numOpposite++;
        }
      }
      targetR++; // DECREASE R
      targetQ--; // INCREASE Q
    }
    if (numOpposite == 0) {
      matches = new HashMap<>();
    }
    return matches;
  }

  @Override
  public void pass() {
    this.switchTurn();
    this.passed += 1;
    status.updateStatus(this);
    for (Listener listener : listeners) {
      listener.update();
    }
  }

  @Override
  public void switchTurn() {
    if (playColor.equals(PlayColor.BLACK)) {
      this.playColor = PlayColor.WHITE;
    } else if (playColor.equals(PlayColor.WHITE)) {
      this.playColor = PlayColor.BLACK;
    }
  }

  @Override
  public void startGame() {
    this.playColor = PlayColor.BLACK;
    status.updateStatus(this);
    for (Listener listener : listeners) {
      listener.update();
    }
  }

  /**
   * The changeCell method is used mainly in the move() method to flip a cell to a different Cell,
   * such as from BLACK to WHITE, WHITE to BLACK, EMPTY to WHITE, etc.
   *
   * @param q       the q-value of the Coordinate key for the cell to be changed
   * @param r       the r-value of the Coordinate key for the cell to be changed
   * @param newCell the Cell value that the cell will be changed to
   */
  private void changeCell(int q, int r, Cell newCell) {
    board.replace(new Coordinate(q, r), newCell);
  }

  /**
   * The hasNoMoves method is a helper to determine if a player of the given PlayColor has
   * ANY physically possible moves. It checks every cell and every relation to that cell to ensure
   * that it is thorough and accurate.
   *
   * @param p the PlayColor of the player whose potential moves are being checked
   * @return true if the player has NO possible moves that can be made, otherwise false
   */
  @Override
  public boolean hasNoMoves(PlayColor p) {
    if (!board.containsValue(Cell.EMPTY)) { // if there are no empty cells, there are no moves
      return true;
    }
    for (Map.Entry<Coordinate, Cell> cell : this.board.entrySet()) {
      if (this.validMoveLogic(cell.getKey().getQ(), cell.getKey().getR(), p)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean isGameOver() {
    // game is over if one player just passed and the other has no moves, or both have no moves,
    // or both players pass consecutively
    return (this.hasNoMoves(this.playColor) && passed == 1)
            || (this.hasNoMoves(PlayColor.BLACK) && this.hasNoMoves(PlayColor.WHITE))
            || passed == 2;
  }

  /**
   * The validMoveArgs method is a helper for move() that determines if the arguments for q and r
   * fall within the proper range. For example, if the board size is 3, q and r
   * MUST be between -2 and 2 (inclusive). In addition, absolute value of q + r must be less than
   * the boardSize. For example, if the board size is 3, q = 2 and r = 2 would be invalid
   *
   * @param q the q-value of the Coordinate for the target
   * @param r the r-value of the Coordinate for the target
   * @return true if the q and r values meet all the validity requirements, otherwise false
   */
  private boolean validMoveArgs(int q, int r) {
    return q > -boardSize && q < boardSize && r > -boardSize && r < boardSize
            && this.verify(q, r);
  }

  @Override
  public int getScore(PlayColor p) {
    Cell c; // set Cell to the color associated with the given PlayColor
    if (p.equals(PlayColor.BLACK)) {
      c = Cell.BLACK;
    } else {
      c = Cell.WHITE;
    }
    int score = 0;
    // iterate through all cells on the board and increase the score if the color matches
    for (Map.Entry<Coordinate, Cell> cell : this.board.entrySet()) {
      if (cell.getValue().equals(c)) {
        score++;
      }
    }
    return score;
  }

  @Override
  public Cell getCell(int q, int r) throws IllegalArgumentException {
    if (board.containsKey(new Coordinate(q, r))) {
      return board.get(new Coordinate(q, r));
    }
    throw new IllegalArgumentException("board does not contain this cell");
  }

  @Override
  public int getBoardSize() {
    return this.boardSize;
  }

  /**
   * The verify method is a helper for multiple methods to confirm that the extraneous q and r
   * values are excluded from various processes. The absolute value of q + r must be less than
   * the boardSize because of the mathematics of a hexagonal board.
   *
   * @param q the q-value of the Coordinate key
   * @param r the r-value of the Coordinate key
   * @return true if q + r falls within the correct range, otherwise false
   */
  @Override
  public boolean verify(int q, int r) {
    return Math.abs(q + r) < boardSize;
  }

  @Override
  public Map<Coordinate, Cell> returnBoard() {
    return this.board;
  }

  @Override
  public ArrayList<Coordinate> getAllMoves() {
    ArrayList<Coordinate> moves = new ArrayList<>();
    for (Map.Entry<Coordinate, Cell> entry : this.board.entrySet()) {
      Coordinate key = entry.getKey();
      Cell value = entry.getValue();
      if (validMoveLogic(key.getQ(), key.getR(), playColor)) {
        moves.add(key);
      }
    }
    return moves;
  }

  @Override
  public PlayColor getColor() {
    return this.playColor;
  }

  @Override
  public void addListener(Listener listener) {
    listeners.add(listener);
  }

  @Override
  public Map<Coordinate, Cell> getCopyBoard() {
    Map<Coordinate, Cell> deep_copy = new HashMap<>(this.board);
    return deep_copy;
  }

  /**
   * Mock method to access what the current turn is to test pass() and other methods.
   * @return the current playColor which is either BLACK or WHITE
   */
  public PlayColor getTurn() {
    return playColor;
  }

  /**
   * Mock method to access the total number of cells in the board to test the constructor
   * and preservation of the board size.
   * @return the total number of cells in the board.
   */
  public int boardTotal() {
    return board.size();
  }

  /**
   * Mock method to access the value for passed to test that a player is forced to pass if they
   * have no moves.
   * @return the number of consecutive passes
   */
  public int getPassed() {
    return passed;
  }

}
