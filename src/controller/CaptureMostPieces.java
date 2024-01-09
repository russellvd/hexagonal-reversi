package controller;

import java.util.ArrayList;
import java.util.Map;

import model.Cell;
import model.Coordinate;
import model.ReversiModel;

/**
 * This Function Object represents strategy 1 of the assignment:
 * The most ideal move is the move that captures the most amount of pieces.
 */
public class CaptureMostPieces implements ReversiStrategies {

  /**
   * This Function Object represents strategy 1 of the assignment:
   * The most ideal move is the move that captures the most amount of pieces.
   */
  public CaptureMostPieces(ReversiModel model) {
    // empty constructor just for initiualization.
  }

  @Override
  public Coordinate decideMove(ReversiModel model) {
    int maximum_score = 0;
    Coordinate ideal_move = new Coordinate(1, 1);
    Map<Coordinate, Cell> temp_board = model.returnBoard();
    ArrayList<Coordinate> all_moves = model.getAllMoves();

    if (model.getAllMoves().isEmpty()) {
      return ideal_move;
    }
    // iterate over the board to find the maximum score move.
    for (Coordinate m: all_moves) {
      model.move(m.getQ(), m.getR());
      int score = model.getScore(model.getColor());
      if (score > maximum_score) {
        maximum_score = score;
        ideal_move = m;
      }
    }
    return ideal_move;
  }

}
