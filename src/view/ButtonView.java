package view;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Path2D;

import model.Coordinate;


/**
 * The ButtonView interface represents all methods pertaining to the individual "button" -
 * that represent the game pieces of the board.
 * All methods implemented strictly *DO NOT* effect the model via mutation - they only represent -
 * the model's current state.
 * In other words: what the button ought to be capable of!
 */
public interface ButtonView {

  /**
   * This returns true if the given Point is in the visual component of the Button.
   * I.e: if the point is within the actual visual component of the button's dimensions.
   */
  boolean isPointInButton(Point point);

  /**
   * Returns the visual representation of the Button as a Path.
   */
  Path2D getButton();

  /**
   * This method updates a Button's Coordinates.
   * EFFECT: updates the "coordinates" field to the given Coordinate.
   */
  void initializeCoordinates(Coordinate c);

  /**
   * This method updates the color of the Button.
   * EFFECT: updates the button field to display gray, and calls repaint to update the view.
   */
  void deselect();

  /**
   * This method updates the color of the Button.
   * EFFECT: updates the button field to display light blue, and calls repaint to update the view.
   */
  void select();

  /**
   * Returns the color of the corresponding Cell to draw.
   * Effect: returns.
   */
  Color getPlayerColor();

  /**
   * Returns the coordinates of the Button.
   * Effect: returns.
   */
  Coordinate getCoordinates();

  /**
   * Returns the bounds of a Button.
   * Effect: returns.
   */
  Rectangle getBounds();

  /**
   * Returns if the button is selected or not.
   * Effect: returns.
   */
  boolean isSelected();


}
