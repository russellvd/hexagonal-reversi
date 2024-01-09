import org.junit.Assert;
import org.junit.Test;

import model.BasicReversi;
import model.ReversiModel;
import view.ReversiTextualView;
import view.TextualView;

/**
 * The purpose of ReversiTextualViewTest is to contain the tests for the ReversiTextualView class.
 */
public class ReversiTextualViewTest {

  ReversiModel model;
  TextualView view;
  String x;
  String o;

  private void init() {
    model = new BasicReversi(3);
    x = "X";
    o = "O";
  }

  // test constructor
  @Test (expected = IllegalArgumentException.class)
  public void testConstructorNullModel() {
    view = new ReversiTextualView(null);
  }

  @Test
  public void testConstructorValid() {
    this.init();
    view = new ReversiTextualView(model);
    String under = "_";
    Assert.assertEquals(47, view.toString().length());
    // to test the number of occurences of the string
    Assert.assertEquals(3, view.toString().split(x).length - 1);
    Assert.assertEquals(3, view.toString().split(o).length - 1);
    Assert.assertEquals(13, view.toString().split(under).length);
  }

  @Test
  public void playTextViewFullGame() {
    this.init();
    view = new ReversiTextualView(model);
    Assert.assertEquals(3, view.toString().split(x).length - 1);
    Assert.assertEquals(3, view.toString().split(o).length - 1);
    model.move(2, -1);
    Assert.assertEquals(5, view.toString().split(x).length - 1);
    Assert.assertEquals(2, view.toString().split(o).length - 1);
    model.move(1, -2);
    Assert.assertEquals(4, view.toString().split(x).length - 1);
    Assert.assertEquals(4, view.toString().split(o).length - 1);
    model.move(-1, -1);
    Assert.assertEquals(7, view.toString().split(x).length - 1);
    Assert.assertEquals(2, view.toString().split(o).length - 1);
    model.move(1, 1);
    Assert.assertEquals(5, view.toString().split(x).length - 1);
    Assert.assertEquals(5, view.toString().split(o).length - 1);
    model.move(-1, 2);
    Assert.assertEquals(8, view.toString().split(x).length - 1);
    Assert.assertEquals(3, view.toString().split(o).length - 1);
    model.move(-2, 1);
    Assert.assertEquals(4, view.toString().split(x).length - 1);
    Assert.assertEquals(8, view.toString().split(o).length - 1);
    System.out.println(view);
    Assert.assertTrue(model.isGameOver());
  }

  @Test
  public void playTextViewNoWhiteLeft() {
    this.init();
    view = new ReversiTextualView(model);
    System.out.println(view);
    model.move(2, -1);
    System.out.println(view);
    model.pass();
    model.move(-1, -1);
    model.pass();
    model.move(1, 1);
    System.out.println(view);
    Assert.assertEquals(9, view.toString().split(x).length - 1);
    Assert.assertFalse(view.toString().contains(o));
    Assert.assertTrue(model.isGameOver());
  }

  @Test
  public void playTextGiantBoard() {
    this.init();
    ReversiModel giant = new BasicReversi(20);
    view = new ReversiTextualView(giant);
    Assert.assertEquals(3, view.toString().split(x).length - 1);
    Assert.assertEquals(3, view.toString().split(o).length - 1);
    Assert.assertEquals(2699, view.toString().length());
    System.out.println(view);
  }
}