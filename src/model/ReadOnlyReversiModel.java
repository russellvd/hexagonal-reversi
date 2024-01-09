package model;


import java.util.ArrayList;
import java.util.Map;

/**
 * This interface represents the READ-ONLY Interface of a Reversi Model.
 * In other words, this interface contains all the observation methods of the model.
 * This Model's purpose is to prevent unwanted mutation, in order to strictly adhere to.
 * the MVC hierarchy.
 */
public interface ReadOnlyReversiModel {

  /**
   * The isGameOver method returns true if there are no moves left for either player. This could
   * mean that a player won and took all or the majority of the tiles. It could also mean there
   * are no available moves left for either player or that both players passed on their last turns.
   * @return true if the game is over based on the criteria listed above, false if any other state
   *         is true (moves still available).
   */
  public boolean isGameOver();

  /**
   * The getScore method returns the score of the given player PlayColor, where PlayColor must be
   * BLACK or WHITE.
   * @param p the PlayColor associated with the color of the cells that will add to the score
   * @return the number of cells with the color matching the player Turn
   */
  public int getScore(PlayColor p);

  /**
   * The getCell method returns the Cell value associated with the cell at the Coordinate
   * given by q and r, where the Cell value must be BLACK, WHITE, or EMPTY.
   * If the cell does not exist on the board, this method will throw an error.
   * @param q the q-value for the Coordinate of the location of the desired cell
   * @param r the r-value for the Coordinate of the location of the desired cell
   * @return if the cell is found, the Cell value of BLACK, WHITE, or EMPTY will be returned
   * @throws IllegalArgumentException if the board does not contain a Cell at the given
   *         Coordinate value.
   */
  public Cell getCell(int q, int r) throws IllegalArgumentException;


  /**
   * The getBoardSize method returns the "size" of the board. In this case, the "size" refers to
   * the side length of the board, which must be at least 3 for the game to be playable.
   * @return the "size" of the board, meaning the uniform side length
   */
  public int getBoardSize();

  /**
   * This just returns a new HashMap of the board's values.
   * There are no mutable objects within the board, so there is no need to create deep copies.
   * Enums are not changeable.
   */
  Map<Coordinate, Cell> getCopyBoard();

  /**
   * The hasNoMoves method is a helper to determine if a player of the given PlayColor has
   * ANY physically possible moves. It checks every cell and every relation to that cell to ensure
   * that it is thorough and accurate.
   *
   * @param p the PlayColor of the player whose potential moves are being checked
   * @return true if the player has NO possible moves that can be made, otherwise false
   */
  boolean hasNoMoves(PlayColor p);

  /**
   * The validMoveLogic method is a helper for the move() method which determines if there are
   * any valid flows from the target cell provided. It will return true if there are any valid
   * flows and false if there are no flows.
   *
   * @param q the q value of the target cell
   * @param r the r value of the target cell
   * @param p the PlayerColor of the player whose move is being checked for logical validity.
   * @return true if there are any possible moves, otherwise false
   */
  boolean validMoveLogic(int q, int r, PlayColor p);

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
  Map<Coordinate, Cell> findQPathsDown(Coordinate coord, Cell target);

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
  Map<Coordinate, Cell> findQPathsUp(Coordinate coord, Cell target);

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
  Map<Coordinate, Cell> findRPathsRight(Coordinate coord, Cell target);

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
  Map<Coordinate, Cell> findRPathsLeft(Coordinate coord, Cell target);

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
  Map<Coordinate, Cell> findSPathsUp(Coordinate coord, Cell target);

  /**
   * The findSPathsDown method is a helper for the validMoveLogic method which determines
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
  Map<Coordinate, Cell> findSPathsDown(Coordinate coord, Cell target);



  /**
   * The verify method is a helper for multiple methods to confirm that the extraneous q and r
   * values are excluded from various processes. The absolute value of q + r must be less than
   * the boardSize because of the mathematics of a hexagonal board.
   *
   * @param q the q-value of the Coordinate key
   * @param r the r-value of the Coordinate key
   * @return true if q + r falls within the correct range, otherwise false
   */
  boolean verify(int q, int r);

  /**
   * Return the board of the given model. Does NOT change anything about the board.
   */
  Map<Coordinate, Cell> returnBoard();

  /**
   * Return an ArrayList of all the coordinates where there are possible moves.
   */
  ArrayList<Coordinate> getAllMoves();

  /**
   * Returns the color of the current player's turn.
   */
  PlayColor getColor();

  /**
   * Adds a listener to the model.
   */
  void addListener(Listener listener);

}
