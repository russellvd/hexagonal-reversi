package controller;

import model.Coordinate;
import model.ReversiModel;

/**
 * This interface represents the strategies for the AI or Player.
 * For example, these strategies all constitute what is a "right move".
 */
public interface ReversiStrategies {

  /**
   * This returns the coordinate for the most "ideal" move according to whatever.
   * Strategy one chooses.
   * The ReversiModel passed into this method is an alias and not the actual model.
   * Therefore, no mutation will occur.
   */
  Coordinate decideMove(ReversiModel model);
}
