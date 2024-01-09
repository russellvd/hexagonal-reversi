package view;

import java.io.IOException;

/**
 * The TextualView class represents a textual visualization of the model that is passed through
 * to it. It does not have knowledge of the functionality within the model, nor does it have
 * knowledge of the controller that will use it.
 * The TextualView benefits the model because it allows for a GUI that helps visualize
 * different states of the model.
 */
public interface TextualView {

  /**
   * The render() method does not take in an input or produce an output. Its purpose
   * is to update the Appendable object since that is more time-efficient and effective
   * than updating a string variable.
   * @throws IOException if the appendable attribute is corrupted
   */
  public void render() throws IOException;

}
