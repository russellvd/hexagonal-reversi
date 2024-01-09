import org.junit.Assert;
import org.junit.Test;

import model.BasicReversi;
import model.Coordinate;
import model.ReadOnlyReversiModel;
import view.MockBasicReversiView;

/**
 * The purpose of ReversiTextualViewTest is to contain the tests for the GUI.
 */
public class ViewTests {

  @Test
  public void testCoordinateFunctionality() {
    ReadOnlyReversiModel model = new BasicReversi(6);
    MockBasicReversiView view = new MockBasicReversiView(model, 25, 30);
    MockBasicReversiView view2 = new MockBasicReversiView(model, 80, 30);
    Assert.assertEquals(new Coordinate(0, -5), view.handleButtonClick());
    Assert.assertEquals(new Coordinate(1, -5), view2.handleButtonClick());

  }
}
