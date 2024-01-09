package controller;

import java.util.HashMap;
import java.util.Map;

import model.Cell;
import model.Coordinate;
import model.ReversiModel;

/**
 * This Function Object represents 2 + 3 of the assignment strategies.
 * This will advoid placing moves NEXT TO corners, as well as prioritize moves -
 * that place IN the corners.
 */
public class AdvoidCorners implements ReversiStrategies {

  /**
   * This Function Object represents 2 + 3 of the assignment strategies.
   * This will advoid placing moves NEXT TO corners, as well as prioritize moves -
   * that place IN the corners.
   */
  public AdvoidCorners(ReversiModel model) {
    // empty constructor just for initiualization.
  }

  @Override
  public Coordinate decideMove(ReversiModel model) {
    // represents the scores of all the moves
    Map<Coordinate, Integer> scores = new HashMap<Coordinate, Integer>();
    Map<Coordinate, Cell> temp_board = model.getCopyBoard();
    // represents the optimal move. currently stores as the midmove.
    Coordinate optimal = new Coordinate(0, 0);
    // represents scores not next to corners.
    Map<Coordinate, Integer> scores_non_corners = new HashMap<Coordinate, Integer>();
    ReversiModel temp = model;

    // initialize the scores hashmap in order to procede.
    for (Coordinate c: temp_board.keySet()) {
      // CHECK IF THE MOVE IS VALID BEFORE PROCEDING.
      if (model.validMoveLogic(c.getQ(), c.getR(), model.getColor())) {
        int score_before = 0;
        temp.move(c.getQ(), c.getR());
        score_before = temp.getScore(model.getColor());
        scores.put(c, score_before);
      }
    }

    for (Coordinate c: scores.keySet()) {
      // if the position is NOT in the corner...
      if (!this.isAdjacentCorner(model, c)) {
        scores_non_corners.put(c, scores.get(c));
      }
    }

    // finally, choose the best move using CaptureMostPieces.
    if (!scores_non_corners.isEmpty()) {
      int max_score = 0;
      for (Map.Entry<Coordinate, Integer> entry : scores_non_corners.entrySet()) {
        Coordinate key = entry.getKey();
        Integer value = entry.getValue();
        if (value > max_score) {
          max_score = value;
          optimal = key;
        }
      }
    }
    return optimal;
  }

  /**
   * This helper method determines if, given the model and coordinates, if the position.
   * Is next to a "corner" where a corner is one of the six side-postions of the hexagon.
   */
  private boolean isAdjacentCorner(ReversiModel model, Coordinate coords) {
    int midpoint = Math.floorDiv(model.getBoardSize() * 2 - 1, 2);

    return (Math.abs(coords.getQ()) == midpoint && Math.abs(coords.getS()) == midpoint - 1) ||
            (Math.abs(coords.getS()) == midpoint && Math.abs(coords.getR()) == midpoint - 1) ||
            (Math.abs(coords.getR()) == midpoint && Math.abs(coords.getQ()) == midpoint - 1) ||
            (Math.abs(coords.getS()) == midpoint && Math.abs(coords.getQ()) == midpoint - 1) ||
            (Math.abs(coords.getR()) == midpoint && Math.abs(coords.getS()) == midpoint - 1) ||
            (Math.abs(coords.getQ()) == midpoint && Math.abs(coords.getR()) == midpoint - 1);




  }
}
