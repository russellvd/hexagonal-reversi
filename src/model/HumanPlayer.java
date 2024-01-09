package model;

import java.util.Optional;

/**
 * model.HumanPlayer represents an human player.
 * as of right now, this is a stub implementation, but more stuff can be added as specs increase.
 */
public class HumanPlayer implements Player {

  private PlayColor color;

  /**
   * this constructor initializes the color for the human player. this cannot change.
   */
  public HumanPlayer() {
    this.color = null;
  }

  @Override
  public PlayColor getColor() {
    return this.color;
  }

  @Override
  public boolean aiPlayer() {
    return false;
  }

  @Override
  public void setColor(PlayColor color) {
    this.color = color;
  }

  @Override
  public Optional<Coordinate> chooseNextMove(ReversiModel model) {
    return Optional.empty();
  }

}
