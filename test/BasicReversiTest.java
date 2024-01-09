import org.junit.Assert;
import org.junit.Test;

import model.BasicReversi;
import model.Cell;
import model.ReversiModel;
import model.PlayColor;

/**
 * The purpose of BasicReversiTest is to contain the tests for the BasicReversi class using
 * instances from that class as well as instances of ReversiMock, a mock class for BasicReversi.
 */
public class BasicReversiTest {

  ReversiModel reversi;
  BasicReversi mock;

  private void init() {
    reversi = new BasicReversi(3);
    mock = new BasicReversi(3);
  }

  // testing constructor
  @Test
  public void testBoardInitialization() {
    this.init();
    Assert.assertEquals(Cell.BLACK, reversi.getCell(1,0));
    Assert.assertEquals(Cell.BLACK, reversi.getCell(-1,1));
    Assert.assertEquals(Cell.BLACK, reversi.getCell(0,-1));
    Assert.assertEquals(Cell.WHITE, reversi.getCell(1,-1));
    Assert.assertEquals(Cell.WHITE, reversi.getCell(0,1));
    Assert.assertEquals(Cell.WHITE, reversi.getCell(-1,0));
    Assert.assertEquals(Cell.EMPTY, reversi.getCell(0,0));
    Assert.assertEquals(Cell.EMPTY, reversi.getCell(-1,-1));
    Assert.assertEquals(Cell.EMPTY, reversi.getCell(0,2));
  }

  @Test
  public void testInitialBoardValues() {
    this.init();
    ReversiModel biggerReversi = new BasicReversi(7);
    Assert.assertEquals(3, reversi.getScore(PlayColor.BLACK));
    Assert.assertEquals(3, reversi.getScore(PlayColor.WHITE));
    Assert.assertEquals(3, reversi.getBoardSize());
    Assert.assertEquals(3, biggerReversi.getScore(PlayColor.BLACK));
    Assert.assertEquals(3, biggerReversi.getScore(PlayColor.WHITE));
    Assert.assertEquals(7, biggerReversi.getBoardSize());
  }

  @Test
  public void testInitialBoardTurn() {
    this.init();
    Assert.assertEquals(PlayColor.BLACK, mock.getTurn());
  }

  @Test
  public void testInitialBoardSize() {
    this.init();
    Assert.assertEquals(19, mock.boardTotal());
    BasicReversi mock2 = new BasicReversi(4);
    Assert.assertEquals(37, mock2.boardTotal());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testReversiConstructorTooSmall() {
    ReversiModel badReversi = new BasicReversi(2);
    badReversi.getScore(PlayColor.BLACK);
  }

  // testing pass
  @Test
  public void testPassSwitchesTurns() {
    this.init();
    mock.pass();
    Assert.assertEquals(PlayColor.WHITE, mock.getTurn());
    mock.pass();
    Assert.assertEquals(PlayColor.BLACK, mock.getTurn());
    Assert.assertTrue(mock.isGameOver());
  }

  @Test
  public void testForcedPass() {
    this.init();
    mock.move(2, -1);
    mock.pass();
    mock.move(-1, -1);
    mock.pass();
    mock.move(1, 1);
    mock.move(0, 0);
    Assert.assertEquals(1, mock.getPassed());
  }

  // test move
  @Test (expected = IllegalStateException.class)
  public void testMoveCellNotEmpty() {
    this.init();
    reversi.move(0, 1);
  }

  @Test
  public void testValidMoveWhiteSUp() {
    this.init();
    reversi.pass();
    reversi.move(2, -1);
    Assert.assertEquals(Cell.WHITE, reversi.getCell(2, -1));
    Assert.assertEquals(Cell.WHITE, reversi.getCell(1, 0));
    Assert.assertEquals(5, reversi.getScore(PlayColor.WHITE));
    Assert.assertEquals(2, reversi.getScore(PlayColor.BLACK));
  }

  @Test
  public void testValidMoveWhiteRLeft() {
    this.init();
    reversi.pass();
    reversi.move(-1, -1);
    Assert.assertEquals(Cell.WHITE, reversi.getCell(-1, -1));
    Assert.assertEquals(Cell.WHITE, reversi.getCell(0, -1));
    Assert.assertEquals(5, reversi.getScore(PlayColor.WHITE));
    Assert.assertEquals(2, reversi.getScore(PlayColor.BLACK));
  }

  @Test
  public void testValidMoveWhiteQDown() {
    this.init();
    reversi.pass();
    reversi.move(1, 1);
    Assert.assertEquals(Cell.WHITE, reversi.getCell(1, 1));
    Assert.assertEquals(Cell.WHITE, reversi.getCell(1, 0));
    Assert.assertEquals(5, reversi.getScore(PlayColor.WHITE));
    Assert.assertEquals(2, reversi.getScore(PlayColor.BLACK));
  }

  @Test
  public void testValidMoveBlackQUp() {
    this.init();
    reversi.move(1, -2);
    Assert.assertEquals(Cell.BLACK, reversi.getCell(1, -2));
    Assert.assertEquals(Cell.BLACK, reversi.getCell(1, -1));
    Assert.assertEquals(2, reversi.getScore(PlayColor.WHITE));
    Assert.assertEquals(5, reversi.getScore(PlayColor.BLACK));
  }

  @Test
  public void testValidMoveBlackRRight() {
    this.init();
    reversi.move(2, -1);
    Assert.assertEquals(Cell.BLACK, reversi.getCell(2, -1));
    Assert.assertEquals(Cell.BLACK, reversi.getCell(1, -1));
    Assert.assertEquals(2, reversi.getScore(PlayColor.WHITE));
    Assert.assertEquals(5, reversi.getScore(PlayColor.BLACK));
  }

  @Test
  public void testValidMoveBlackSDown() {
    this.init();
    reversi.move(-2, 1);
    Assert.assertEquals(Cell.BLACK, reversi.getCell(-2, 1));
    Assert.assertEquals(Cell.BLACK, reversi.getCell(-1, 0));
    Assert.assertEquals(2, reversi.getScore(PlayColor.WHITE));
    Assert.assertEquals(5, reversi.getScore(PlayColor.BLACK));
  }

  @Test
  public void testFullGame() {
    this.init();
    reversi.move(2, -1);
    Assert.assertEquals(5, reversi.getScore(PlayColor.BLACK));
    Assert.assertEquals(2, reversi.getScore(PlayColor.WHITE));
    reversi.move(1, -2);
    Assert.assertEquals(4, reversi.getScore(PlayColor.BLACK));
    Assert.assertEquals(4, reversi.getScore(PlayColor.WHITE));
    reversi.move(-1, -1);
    Assert.assertEquals(7, reversi.getScore(PlayColor.BLACK));
    Assert.assertEquals(2, reversi.getScore(PlayColor.WHITE));
    reversi.move(1, 1);
    Assert.assertEquals(5, reversi.getScore(PlayColor.BLACK));
    Assert.assertEquals(5, reversi.getScore(PlayColor.WHITE));
    reversi.move(-1, 2);
    Assert.assertEquals(8, reversi.getScore(PlayColor.BLACK));
    Assert.assertEquals(3, reversi.getScore(PlayColor.WHITE));
    reversi.move(-2, 1);
    Assert.assertEquals(4, reversi.getScore(PlayColor.BLACK));
    Assert.assertEquals(8, reversi.getScore(PlayColor.WHITE));
    Assert.assertTrue(reversi.isGameOver());
  }

  @Test (expected = IllegalStateException.class)
  public void illegalBlackMove() {
    this.init();
    reversi.move(2,0);
  }

  @Test (expected = IllegalStateException.class)
  public void illegalWhiteMove() {
    this.init();
    reversi.pass();
    reversi.move(-1,0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void invalidQ() {
    this.init();
    reversi.move(3, -1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void invalidR() {
    this.init();
    reversi.move(0, 5);
  }

  @Test (expected = IllegalArgumentException.class)
  public void out() {
    this.init();
    reversi.move(1, 2);
  }

  // test isGameOver
  @Test
  public void testGameOverInitialGame() {
    this.init();
    Assert.assertFalse(reversi.isGameOver());
  }

  @Test
  public void testGameOverDoublePass() {
    this.init();
    reversi.pass();
    reversi.pass();
    Assert.assertTrue(reversi.isGameOver());
  }

  // getScore is tested in other tests
  // test getCell
  @Test (expected = IllegalArgumentException.class)
  public void testInvalidGetCell() {
    this.init();
    reversi.getCell(4, 3);
  }

  @Test
  public void testValidGetCell() {
    this.init();
    Assert.assertEquals(Cell.EMPTY, reversi.getCell(0, 0));
    Assert.assertEquals(Cell.BLACK, reversi.getCell(1, 0));
    Assert.assertEquals(Cell.WHITE, reversi.getCell(0, 1));
  }

  @Test
  public void testProgressiveGameOverWithPasses() {
    this.init();
    reversi.move(2, -1);
    Assert.assertFalse(reversi.isGameOver());
    reversi.pass();
    Assert.assertFalse(reversi.isGameOver());
    reversi.move(-1, -1);
    Assert.assertFalse(reversi.isGameOver());
    reversi.pass();
    Assert.assertFalse(reversi.isGameOver());
    reversi.move(1, 1);
    Assert.assertTrue(reversi.isGameOver());
  }

  // test getBoardSize
  @Test
  public void testProgressiveBoardSizeDoesNotChange() {
    this.init();
    mock.move(2, -1);
    Assert.assertFalse(mock.isGameOver());
    Assert.assertEquals(3, mock.getBoardSize());
    Assert.assertEquals(19, mock.boardTotal());
    mock.move(1, -2);
    Assert.assertFalse(mock.isGameOver());
    Assert.assertEquals(3, mock.getBoardSize());
    Assert.assertEquals(19, mock.boardTotal());
    mock.move(-1, -1);
    Assert.assertFalse(mock.isGameOver());
    Assert.assertEquals(3, mock.getBoardSize());
    Assert.assertEquals(19, mock.boardTotal());
    mock.move(1, 1);
    Assert.assertFalse(mock.isGameOver());
    Assert.assertEquals(3, mock.getBoardSize());
    Assert.assertEquals(19, mock.boardTotal());
    mock.move(-1, 2);
    Assert.assertFalse(mock.isGameOver());
    Assert.assertEquals(3, mock.getBoardSize());
    Assert.assertEquals(19, mock.boardTotal());
    mock.move(-2, 1);
    Assert.assertTrue(mock.isGameOver());
    Assert.assertEquals(3, mock.getBoardSize());
    Assert.assertEquals(19, mock.boardTotal());
  }

  // example for explaining how the model works
  @Test
  public void exampleComprehensiveModel() {
    ReversiModel model = new BasicReversi(3);
    model.move(2, -1);
    Assert.assertEquals(5, model.getScore(PlayColor.BLACK));
    Assert.assertEquals(2, model.getScore(PlayColor.WHITE));
    model.pass();
    Assert.assertFalse(model.isGameOver());
    model.move(1, 1);
    Assert.assertEquals(Cell.BLACK, model.getCell(0, 1));
    Assert.assertEquals(3, model.getBoardSize());
    model.pass();
    model.pass();
    Assert.assertTrue(model.isGameOver());
  }
}