package model;



/**
 * This Class represents the listener methods that the controller utilizes to determine whose -
 * turn it is, and whether or not the game is over.
 */
public class ReversiStatus implements ModelStatus {
  private Status status;


  /**
   * This constructor initializes the model that the listener pertains to.
   * Initializes the status as not srated yet.
   */
  public ReversiStatus() {
    this.status = Status.NotStarted;
  }

  @Override
  public void updateStatus(ReadOnlyReversiModel model) {
    if (model.isGameOver()) {
      this.status = Status.END;
    }
    else {
      this.status = Status.InProgress;
    }
  }

  @Override
  public Status getStatus() {
    return this.status;
  }
}
