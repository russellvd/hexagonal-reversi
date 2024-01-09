package model;

import java.util.Optional;

import controller.ReversiStrategies;

/**
 * model.AIPlayer represents an AI player. this is a distinction between a normal, human player.
 * as of right now, this is a stub implementation, but more stuff can be added as specs increase.
 */
public class AIPlayer implements Player {
  private PlayColor color;
  private final ReversiStrategies strategy;


  /**
   * This constructor initializes the AIPlayer's play color, and its associating model.
   */
  public AIPlayer(ReversiStrategies s) {
    this.color = null;
    this.strategy = s;
  }


  @Override
  public PlayColor getColor() {
    return this.color;
  }

  @Override
  public boolean aiPlayer() {
    return true;
  }

  @Override
  public void setColor(PlayColor color) {
    this.color = color;
  }

  @Override
  public Optional<Coordinate> chooseNextMove(ReversiModel model) {
    return Optional.ofNullable(strategy.decideMove(model));
  }
}
