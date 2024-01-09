package view;

import java.io.IOException;

import model.ReadOnlyReversiModel;
import model.Cell;

/**
 * The ReversiTextualView class represents a textual visualization of the model that is passed
 * through to it. It does not have knowledge of the functionality within the model, nor does it have
 * knowledge of the controller that will use it.
 * The ReversiTextualView benefits the model because it allows for a GUI that helps visualize
 * different states of the model.
 */
public class ReversiTextualView implements TextualView {
  private ReadOnlyReversiModel model;
  private Appendable a;

  /**
   * Constructs a ReversiTextualView object with the fields ReversiModel model and Appendable a
   * where 'model' is the ReversiModel that will be represented with this view
   * and 'a' is an Appendable object that can be updated based on the state of the model.
   * @param model the ReversiModel specific to this ReversiTextualView that will be represented
   *              visually by the ReversiTextualView.
   */
  public ReversiTextualView(ReadOnlyReversiModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Null model");
    }
    this.model = model;
    this.a = new StringBuilder();
  }

  @Override
  public void render() throws IOException {
    if (this.a == null) {
      throw new IOException("Null appendable object");
    }
    this.a.append(this.toString());
  }

  /**
   * The toString method for the ReversiTextualView visualizes BLACK cells as Xs,
   * WHITE cells as Os, and EMPTY cells as _. It accounts for spacing by adding a new line
   * between rows and adding spacing to the beginning of the line depending on which row it is.
   * @return the String that contains the entire visualization of the state of the model.
   */
  @Override
  public String toString() {
    String print;
    StringBuilder result = new StringBuilder();
    for (int r = -model.getBoardSize() + 1; r < model.getBoardSize(); r++) { // row of the board
      // add a different number of spaces based on the row number
      result.append(" ".repeat(Math.abs(r))); // used string repeat function to multiply a String
      for (int q = -model.getBoardSize() + 1; q < model.getBoardSize(); q++) { // q value of cell
        if (Math.abs(q + r) < model.getBoardSize()) {
          if (model.getCell(q, r).equals(Cell.EMPTY)) {
            result.append("_ "); // representation for an EMPTY cell
          }
          else if (model.getCell(q, r).equals(Cell.BLACK)) {
            result.append("X "); // representation for a BLACK cell
          }
          else {
            result.append("O "); // representation for a WHITE cell
          }
        }
      }
      result.append(System.lineSeparator()); // add a new line after the row is done
    }
    print = result.toString();
    print = print.stripTrailing(); // remove trailing whitespace
    return print;
  }
}
