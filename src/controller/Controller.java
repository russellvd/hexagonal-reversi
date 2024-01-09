package controller;



import model.Coordinate;
import model.ModelStatus;
import model.PlayColor;
import model.Player;
import model.ReversiModel;
import view.Features;
import view.ReversiView;

/**
 * This class defines the controller for a manual game of Reversi.
 * This controller takes in the model, view, ModelStatus, and Player to intersect -
 * with the appropriate classes.
 */
public class Controller implements Features {
  private final ReversiModel model;
  private final ReversiView view;
  private final ModelStatus status;
  private final Player player;


  /**
   * This constructor initializes the controller for the user.
   * Must take in all the stated parameters.
   */
  public Controller(ReversiModel model, ReversiView view, Player player,
                    ModelStatus status) {
    this.model = model;
    this.view = view;
    this.player = player;
    view.addFeatures(this);
    view.display();
    this.status = status;
  }

  /**
   * Updates the view so that the controller and model state is accurately depicted.
   * Checks for game over scenario.
   */
  public void update() {
    view.refresh();
    if (status.getStatus() == ModelStatus.Status.END) {
      PlayColor winner = model.getColor();
      boolean win = winner == player.getColor();
      view.showErrorMessage("GAME IS OVER!");
      return;
    }
  }

  /**
   * Returns the player guiding the controller.
   * Returns the color, specifically.
   */
  public Player returnPlayer() {
    return player;
  }

  @Override
  public void playerMove(Coordinate c) {
    try {
      if (status.getStatus() == ModelStatus.Status.END) {
        view.showErrorMessage("GAME IS OVER!");
        return;
      }
      if (player.getColor() != model.getColor()) {
        view.showErrorMessage("Not your turn.");
        return;
      }
      model.move(c.getQ(), c.getR());
      view.deselectAll();
      view.refresh();
    } catch (IllegalStateException | IllegalArgumentException e) {
      String message = "";
      if (c == null) {
        message = "you are not selecting a hexagon!";
      } else {
        message = "INCORRECT MOVE: (" + c.getQ() + "," + c.getR() + ") IS NOT VALID";
      }
      view.showErrorMessage(message);
    }
  }

  @Override
  public void playerPass() {
    if (status.getStatus() == ModelStatus.Status.END) {
      return;
    }
    if (player.getColor() != model.getColor()) {
      return;
    }
    model.pass();
    view.deselectAll();
    view.refresh();
  }
}
