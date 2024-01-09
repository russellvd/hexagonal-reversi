package controller;

import java.util.ArrayList;
import java.util.List;

import model.Listener;
import model.PlayColor;

/**
 * This ControllerListener class represents a Listener for the model, that interacts with the  -
 * controller by feeding it information.
 */
public class ControllerListener implements Listener {
  List<Controller> controllers;

  /**
   * initialize the ControllerListeners and set controllers to empty.
   */
  public ControllerListener() {
    controllers = new ArrayList<>();
  }


  @Override
  public void update() {
    if (controllers.get(0).returnPlayer().getColor() == null
            && controllers.get(1).returnPlayer().getColor() == null) {
      controllers.get(0).returnPlayer().setColor(PlayColor.BLACK);
      controllers.get(1).returnPlayer().setColor(PlayColor.WHITE);
    }
    for (Controller c : controllers) {
      c.update();
    }
  }

  /**
   * Subscribe the given controller into the list of listeners.
   */
  public void subscribe(Controller controller) {
    controllers.add(controller);
  }


}
