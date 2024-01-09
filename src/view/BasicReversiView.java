package view;


import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import model.Coordinate;
import model.ReadOnlyReversiModel;

/**
 * Represents a BasicReversiView - a GUI representation of a BasicReversiGame.
 * param: size - side length of the hexagonal frame.
 * takes in: model -> a readonly model to prevent mutation.
 */
public class BasicReversiView extends JFrame implements ReversiView {
  private final int size; // depicts the length of each hexagonal side.
  private final int height; // depicts the height of the board.
  private final ReadOnlyReversiModel model;
  private ArrayList<HexagonButton> board;
  private Color background_color = new Color(38, 38, 38);
  private final JLabel turn; // depicts whose turn it is.

  /**
   * The ReversiView class represents all methods pertaining to the Frame!
   * that represent the game pieces of the board.
   * All methods implemented strictly *DO NOT* effect the model via mutation - they only represent -
   * the model's current state.
   * In other words: what the Frame ought to be capable of!
   */
  public BasicReversiView(ReadOnlyReversiModel model) {
    this.size = model.getBoardSize();
    this.height = this.size * 2 - 1;
    this.model = model;
    this.board = new ArrayList<HexagonButton>();
    JPanel displayPanels = new JPanel();
    turn = new JLabel("");
    displayPanels.add(turn);
    displayColorLabel();
    initializeRows();
    initializeButtonCoordinates();
    setFocusable(true);
    add(displayPanels, BorderLayout.NORTH);
    this.pack();
  }

  @Override
  public Map<Coordinate, ButtonView> boardMap() {
    Map<Coordinate, ButtonView> output = new LinkedHashMap<>();
    for (int i = 0; i < board.size(); i++) {
      Coordinate key = getAllCoordinates().get(i);
      ButtonView value = board.get(i);
      output.put(key, value);
    }
    return output;
  }

  @Override
  public void initializeButtonCoordinates() {
    for (Map.Entry<Coordinate, ButtonView> entry : boardMap().entrySet()) {
      Coordinate key = entry.getKey();
      ButtonView value = entry.getValue();
      value.initializeCoordinates(key);
    }
  }

  @Override
  public void deselectAll() {
    for (ButtonView b: this.board) {
      b.deselect();
    }
  }

  @Override
  public void deselectAllButOne(ButtonView button) {
    for (ButtonView b: this.board) {
      if (!(button.equals(b))) {
        b.deselect();
      }
    }
  }



  @Override
  public void setListeners(ActionListener clicks, KeyListener keys) {
    this.addKeyListener(keys);
    for (HexagonButton b: this.board) {
      b.addActionListener(clicks);
      b.setActionCommand("Cell");
    }

  }

  @Override
  public void resetFocus() {
    this.setFocusable(true);
    this.requestFocus();
  }

  @Override
  public void showErrorMessage(String error) {
    JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public ButtonView returnSelected() {
    for (ButtonView b: this.board) {
      if (b.isSelected()) {
        return b;
      }
    }
    throw new IllegalStateException("No button selected.");
  }

  @Override
  public ReadOnlyReversiModel returnModel() {
    return this.model;
  }

  @Override
  public void refresh() {
    this.displayColorLabel();
    this.repaint();
  }

  @Override
  public void addFeatures(Features features) {
    this.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        // EMPTY NO NEEDED
      }

      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          System.out.println("Key Pressed Debug!");
          features.playerMove(returnSelected().getCoordinates());
          System.out.println(returnSelected().getCoordinates().getQ());
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
          features.playerPass();
        }
      }

      @Override
      public void keyReleased(KeyEvent e) {
        // EMPTY NO NEEDED
      }
    });
  }

  @Override
  public void display() {
    this.setVisible(true);
  }

  @Override
  public void displayColorLabel() {
    turn.setText("Turn: " + model.getColor());
  }

  /**
   * Initializes the rows of the game to reflect a Hexagonal Plane.
   * This function maintains as a private method due to its specificity to BasicReversi.
   */
  private void initializeRows() {
    setTitle("Dynamic Hexagonal Board");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel boardPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(0, 0, -this.size * 3, 0);

    int column = 0;
    for (int count: rowConfiguration()) {
      gbc.gridy = column;
      JPanel row_panel = createRowPanel(count);
      boardPanel.add(row_panel, gbc);
      column += 1;
    }
    boardPanel.setBackground(background_color);
    add(boardPanel);
    pack();

  }

  private void handleButtonClick(int x, int y) {
    boolean clicked_on_button = false;
    for (ButtonView button : board) {
      if (button.getBounds().contains(x, y)) {
        clicked_on_button = true;
        deselectAllButOne(button);
        break;
      }
    }
    if (!clicked_on_button) {
      deselectAll();
    }
  }


  /**
   * Calculates the width of the hexagonal plane at a given row.
   * This function maintains as a private method due to its specificity to BasicReversi.
   */
  private int calculateWidth(int row, int size) {
    int midpoint = size - 1;
    int differential = row - midpoint;

    if (row <= midpoint) {
      return size + row;
    } else {
      return size * 2 - 1 - differential;
    }
  }

  /**
   * Returns a given row of Buttons as a Jpanel.
   * This function maintains as a private method due to its specificity to BasicReversi.
   * Additionally, this method adds each corresponding button to a Hashmap that stores -
   * its coordinates.
   */
  private JPanel createRowPanel(int count) {
    JPanel row_panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    ArrayList<HexagonButton> row_buttons = new ArrayList<HexagonButton>();
    Map<Coordinate, JButton> system = new HashMap<>();

    // set the board up.
    for (int i = 0; i < count; i++) {
      // NOTE: these paddings are strictly aesthetic they have no effect on row spacing.
      gbc.ipady = this.size * 10;
      gbc.ipadx = this.size * 5;
      HexagonButton hexagon = new HexagonButton(this.model);
      row_buttons.add(hexagon);
      row_panel.add(hexagon, gbc);
      row_panel.setOpaque(false);
    }

    // add the row_buttons to the main board...
    board.addAll(row_buttons);
    return row_panel;
  }

  /**
   * Returns the row configuration of the Hexagonal Plane.
   * I.e: calculates how many columns are in each row.
   * This function maintains as a private method due to its specificity to BasicReversi.
   */
  private int[] rowConfiguration() {
    int[] output = new int[this.height];
    for (int i = 0; i < this.height; i++) {
      output[i] = calculateWidth(i, this.size);
    }
    return output;
  }

  /**
   * Returns the coordinates for the top of the Hexagon board.
   * This function maintains as a private method due to its specificity to BasicReversi.
   */
  private ArrayList<Coordinate> getTopHalfCoords() {
    ArrayList<Coordinate> output = new ArrayList<>();
    int[] row_config = new int[Math.floorDiv(this.height, 2)]; // width of each row
    int height_start = -(this.size - 1);
    int row_start = 0;

    for (int i = 0; i < Math.floorDiv(this.height, 2); i++) {
      row_config[i] = rowConfiguration()[i];
    }

    for (int width: row_config) {
      for (int col = row_start; col < this.size; col++) {
        output.add(new Coordinate(col, height_start));
      }
      height_start += 1;
      row_start -= 1;
    }
    return output;
  }

  /**
   * Returns the coordinates for the midline of the Hexagon board.
   * This function maintains as a private method due to its specificity to BasicReversi.
   */
  private ArrayList<Coordinate> getMidlineCoords() {
    ArrayList<Coordinate> output = new ArrayList<>();
    int[] row_config = new int[1]; // width of each row
    int row_start = -Math.floorDiv(this.height, 2);

    for (int i = row_start; i < -row_start + 1; i++) {
      output.add(new Coordinate(i, 0));
    }

    return output;
  }

  /**
   * Returns the coordinates for the bottom of the Hexagon board.
   * This function maintains as a private method due to its specificity to BasicReversi.
   */
  private ArrayList<Coordinate> getBottomHalfCoords() {
    ArrayList<Coordinate> output = new ArrayList<>();
    int[] row_config = new int[Math.floorDiv(this.height, 2)]; // width of each row
    int height_start = 1;
    int row_end = this.size - 1;
    int row_indexing = 0;

    for (int i = Math.floorDiv(this.height, 2) + 1; i < this.height; i++) {
      row_config[row_indexing] = rowConfiguration()[i];
      row_indexing += 1;
    }

    for (int width: row_config) {
      for (int col = -Math.floorDiv(this.height, 2); col < row_end; col++) {
        output.add(new Coordinate(col, height_start));
      }
      height_start += 1;
      row_end -= 1;
    }

    return output;
  }

  /**
   * Returns all the coordinates from top to bottom in a single ArrayList.
   * This function maintains as a private method due to its specificity to BasicReversi.
   */
  private ArrayList<Coordinate> getAllCoordinates() {
    ArrayList<Coordinate> all_coordinates = new ArrayList<Coordinate>();
    // store the hexagons in coordinates.
    all_coordinates.addAll(getTopHalfCoords());
    all_coordinates.addAll(getMidlineCoords());
    all_coordinates.addAll(getBottomHalfCoords());
    return all_coordinates;
  }


}

