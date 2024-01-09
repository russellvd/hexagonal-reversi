package view;

import model.Coordinate;

/**
 * This interface represents all the actions the controller can do to the view.
 * More specifically, the controller can:
 * 1) move.
 * 2) pass.
 */
public interface Features {

  /**
   * Create a move given a coordinate.
   */
  void playerMove(Coordinate c);

  /**
   * In the turn, the player passes.
   */
  void playerPass();
}
