import controller.Controller;
import controller.ControllerListener;
import model.BasicReversi;
import model.HumanPlayer;
import model.ModelStatus;
import model.Player;
import model.ReversiStatus;
import view.BasicReversiView;

/**
 * This is the Final class - simply used to run the view.
 */
public final class Reversi {
  /**
   * This is to run the code.
   */
  public static void main(String[] args) {
    ModelStatus status = new ReversiStatus();
    BasicReversi model = new BasicReversi(6, status);
    Player humanPlayer1 = new HumanPlayer();
    BasicReversiView view = new BasicReversiView(model);
    Player humanPlayer2 = new HumanPlayer();
    BasicReversiView view2 = new BasicReversiView(model);
    Controller controller = new Controller(model, view, humanPlayer2, status);
    Controller controller2 = new Controller(model, view2, humanPlayer1, status);
    ControllerListener listeners = new ControllerListener();
    listeners.subscribe(controller);
    listeners.subscribe(controller2);
    model.addListener(listeners);
    model.startGame();
  }
}
