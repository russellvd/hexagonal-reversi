package model;



/**
 * This interface defines the listeners that pertain to the model.
 * More specifically, the ModelStatus calls methods that return the state of the game by:
 * 1. updating the status of the given model.
 * 2. return the status of the given model.
 * *NONE* of these methods mutate the model.
 */
public interface ModelStatus {

  /**
   * this method updates the given model's status.
   * effect: updates given model's status.
   */
  void updateStatus(ReadOnlyReversiModel model);


  /**
   * this method returns the status of a model.
   * effect: returns the status.
   */
  Status getStatus();

  /**
   * This enum represents all possible statuses of the game.
   * END (game is over)
   * InProgress (there are eligible moves to play)
   * NotStarted (game hasn't started)
   *
   */
  enum Status {
    END, InProgress, NotStarted
  }



}
