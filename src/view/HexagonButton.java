package view;

import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;

import model.Cell;
import model.Coordinate;
import model.ReadOnlyReversiModel;

/**
 * This class represents a hexagon-shaped button for a Reversi game.
 */
public class HexagonButton extends JButton implements ButtonView {

  private final Color BUTTON_COLOR = new Color(176, 232, 176);
  private final Color HOVERED_COLOR = new Color(141, 246, 228);
  private final Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0);
  private final Color BLACK_COLOR = new Color(0, 0, 0);
  private boolean is_clicked = false;
  private int click_count = 0;
  private Coordinate coordinates;
  private ReadOnlyReversiModel model;

  /**
   * This class represents a hexagon-shaped button for a Reversi game.
   */
  public HexagonButton(ReadOnlyReversiModel m) {
    // initialize the button's coordinates to (0, 0);
    this.coordinates = new Coordinate(0, 0);
    this.model = m;
    // make button transparent
    setOpaque(false);
    setContentAreaFilled(false);
    setBorderPainted(false);
    setBackground(new Color(0, 0, 0, 0));
    setFocusable(false);

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (isPointInButton(e.getPoint())) {
          doClick();
          System.out.println("Coordinate of click (q, r) = " + "(" + coordinates.getQ() + ", " +
                  coordinates.getR() + ")");
          click_count += 1;
          select();
          setFocusable(false);
        }
      }
    });
  }

  @Override
  public void initializeCoordinates(Coordinate c) {
    this.coordinates = c;
  }


  @Override
  public boolean isSelected() {
    return this.is_clicked;
  }

  @Override
  public void deselect() {
    this.is_clicked = false;
    this.click_count = 0;
    repaint();
  }

  @Override
  public void select() {
    if (click_count > 1) {
      deselect();
      return;
    }
    this.is_clicked = true;
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();
    Path2D hexagon = getButton();
    hexagon.closePath();
    if (this.is_clicked) {
      g2d.setColor(this.HOVERED_COLOR);
    } else {
      g2d.setColor(this.BUTTON_COLOR);
    }
    g2d.fill(hexagon);
    g2d.setColor(Color.black);
    g2d.draw(hexagon);

    Graphics oval = (Graphics2D) g.create();
    oval.setColor(getPlayerColor());
    int radius = getWidth() / 4;
    oval.fillOval(getWidth() / 2 - radius, getHeight() / 2 - radius, 2 * radius,
            2 * radius);

  }

  @Override
  public Path2D getButton() {
    int width = getWidth();
    int height = getHeight();

    int[] xPoints = {width / 2, width, width, width / 2, 0, 0};
    int[] yPoints = {0, height / 4, height * 3 / 4, height, height * 3 / 4, height / 4};

    Path2D path = new Path2D.Double();
    path.moveTo(xPoints[0], yPoints[0]);

    for (int i = 1; i < xPoints.length; i++) {
      path.lineTo(xPoints[i], yPoints[i]);
    }
    path.closePath();
    return path;
  }

  @Override
  public boolean isPointInButton(Point point) {
    Path2D hexagon = getButton();
    return hexagon.contains(point);
  }

  @Override
  public Color getPlayerColor() {
    Color c = TRANSPARENT_COLOR;
    if (this.model.getCell(this.coordinates.getQ(),
            this.coordinates.getR()) == Cell.WHITE) {
      c = Color.WHITE;
    }
    else if (this.model.getCell(this.coordinates.getQ(),
            this.coordinates.getR()) == Cell.BLACK) {
      c = BLACK_COLOR;
    }
    return c;
  }

  @Override
  public Coordinate getCoordinates() {
    return this.coordinates;
  }

}
