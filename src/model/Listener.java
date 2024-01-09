package model;

/**
 * The interface Listener represents a interface that updates only when the model-
 * has undergone a change in its mutable state.
 */
public interface Listener {

  /**
   * if the model has changed, the listener updates...
   */
  void update();
}
