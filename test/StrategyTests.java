import org.junit.Assert;
import org.junit.Test;

import controller.AdvoidCorners;
import controller.CaptureMostPieces;
import model.BasicReversi;
import model.Coordinate;

/**
 * The purpose of the StrategyTests is to test the Strategy Function Objects.
 */


public class StrategyTests {

  @Test
  public void testCaptureMostPieces() {
    BasicReversi model = new BasicReversi(3);
    Coordinate move_1 = new Coordinate(-2, 1);
    Coordinate move_2 = new Coordinate(2, -1);
    Coordinate optimal = new Coordinate(1, 1);

    model.move(move_1.getQ(), move_1.getR());
    model.move(move_2.getQ(), move_2.getR());
    CaptureMostPieces opt = new CaptureMostPieces(model);
    Assert.assertEquals(optimal, opt.decideMove(model));
  }

  @Test
  public void testAdvoidCorners() {
    BasicReversi model = new BasicReversi(3);
    Coordinate move_1 = new Coordinate(-2, 1);
    Coordinate move_2 = new Coordinate(2, -1);
    Coordinate move_3 = new Coordinate(3, -1);

    Coordinate optimal = new Coordinate(1, 1);

    model.move(move_1.getQ(), move_1.getR());
    model.move(move_2.getQ(), move_2.getR());
    model.move(move_3.getQ(), move_3.getR());
    AdvoidCorners opt = new AdvoidCorners(model);
    Assert.assertEquals(optimal, opt.decideMove(model));
  }
}
