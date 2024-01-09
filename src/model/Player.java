package model;

import java.util.Optional;

import model.PlayColor;

/**
 * model.Player represents a player who can interact with the game
 * The model.Player interface allows for the potential differentiation between an AI/computer player
 * vs. a human player.
 * A model.Player will have a PlayColor attribute to determine which color they can control.
 */
public interface Player {

  /**
   * The getColor method accesses the PlayColor attribute of the model.Player.
   * @return the value of the model.Player's PlayColor
   */
  PlayColor getColor();

  /**
   * returns true if the player is a ai.
   * returns.
   */
  boolean aiPlayer();

  /**
   * Sets the player's color to the given color.
   * EFFECT: changes the Player's color to the given color...
   */
  void setColor(PlayColor color);

  /**
   * chooses the next move for the model automatically.
   * returns the move for the model.
   */
  Optional<Coordinate> chooseNextMove(ReversiModel model);
}
