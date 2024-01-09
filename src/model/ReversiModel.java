package model;

/**
 * ReversiModel represents the primary model interface for a 2-player game of Reversi.
 * The model has no visual representation on its own and does not take any user input on its own.
 */
public interface ReversiModel extends ReadOnlyReversiModel {

  /**
   * The move method takes in an attempt by the player whose turn it is to place their tile
   * on the given location, where q and r are related to a coordinate system determined by the
   * hexagonal shape of the Reversi game.
   * The turn of the players is controlled by the game, so it is impossible for the same player
   * to move twice in a row.
   * If a player does not have any moves available to them, the move method will automatically make
   * a pass() call on their turn.
   * @param q the q-value for the Coordinate of the location of the desired cell
   * @param r the r-value for the Coordinate of the location of desired cell
   */
  public void move(int q, int r) throws IllegalArgumentException, IllegalStateException;

  /**
   * The pass method represents either the choice by the user to pass on their turn, or a forced
   * pass by the game if the user has no moves available to them.
   * The pass method switches the turn back over to the other player.
   * If both players pass, the game is over.
   */
  public void pass();


  /**
   * The switchTurn() method is a helper used in multiple methods that changes the playColor
   * of the current Reversi game over to the other player. This method helps ensure that the
   * same player will never play twice in a row and eliminate the need to input which player's turn
   * it is when a player makes a move.
   */
  void switchTurn();

  /**
   * marks the start of the game for the controller and the view.
   * @throws IllegalStateException if the game has not started
   */
  void startGame();




}
