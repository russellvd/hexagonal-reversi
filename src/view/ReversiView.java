package view;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.Map;

import model.Coordinate;
import model.ReadOnlyReversiModel;

/**
 * The ReversiView interface represents all methods pertaining to a GUI-Representation of the -
 * ReversiModel game.
 * All methods implemented strictly *DO NOT* effect the model via mutation - they only represent -
 * the model's current state.
 * In other words: what the frame ought to be capable of!
 */
public interface ReversiView {

  /**
   * Returns a Hashmap of the board that represents the Buttons, and their coordinates.
   * Where key = Coordinate, and value = its corresponding button.
   */
  Map<Coordinate, ButtonView> boardMap();

  /**
   * Iterates over the boardMap() and initializes each button's coordinates.
   * EFFECT: changes each button on the map's coordinates.
   */
  void initializeButtonCoordinates();


  /**
   * Iterates over the boardMap() and deselects every single Button.
   * EFFECT: changes each button's clicked state and highlight.
   */
  void deselectAll();

  /**
   * Iterates over the boardMap() and deselects every single Button BESIDES one.
   * EFFECT: changes each button's clicked state and highlight.
   */
  void deselectAllButOne(ButtonView button);

  /**
   * Sets the listeners that the controller interacts with for each button on the view.
   * EFFECT: adds listeners to each button...
   */
  void setListeners(ActionListener clicks, KeyListener keys);

  /**
   * Reset the focus on the appropriate part of the view that has the keyboard listener attached to
   * it, so that keyboard events will still flow through.
   */
  void resetFocus();

  /**
   * Transmits an error message to the view when the command could not be processed correctly.
   *
   * @param error The error message to be displayed.
   */
  void showErrorMessage(String error);

  /**
   * Returns the selected Button in the view.
   * Effect: returns.
   */
  ButtonView returnSelected();

  /**
   * Returns the current board that is displayed.
   * Effect: returns.
   */
  ReadOnlyReversiModel returnModel();

  /**
   * Repaints the entire board.
   * Effect: repaints.
   */
  void refresh();

  /**
   * Add the features to the view.
   * Effect: adds action options for the controller to the view to create intersection.
   */
  void addFeatures(Features features);

  /**
   * Displays the board.
   * Effect: displays the view.
   */
  void display();

  /**
   * Displays the label for the player color.
   * Effect: displays the label for the player color.
   */
  void displayColorLabel();

}
