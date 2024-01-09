package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.awt.Insets;
import java.awt.Color;

import model.Coordinate;
import model.ReadOnlyReversiModel;

/**
 * Represents a Mock rep of BasicReversiView - a GUI representation of a BasicReversiGame.
 * param: size - side length of the hexagonal frame.
 * takes in: model -> a readonly model to prevent mutation.
 */
public class MockBasicReversiView extends JFrame implements ReversiView {
  private final int size; // depicts the length of each hexagonal side.
  private final int height; // depicts the height of the board.
  private final ReadOnlyReversiModel model;
  private ArrayList<ButtonView> board;
  private Color background_color = new Color(38, 38, 38);
  private int x;
  private int y;

  /**
   * The ReversiView class represents all methods pertaining to the Frame!
   * that represent the game pieces of the board.
   * All methods implemented strictly *DO NOT* effect the model via mutation - they only represent -
   * the model's current state.
   * In other words: what the Frame ought to be capable of!
   */
  public MockBasicReversiView(ReadOnlyReversiModel model, int x, int y) {
    this.size = model.getBoardSize();
    this.height = this.size * 2 - 1;
    this.model = model;
    this.x = x;
    this.y = y;
    this.board = new ArrayList<ButtonView>();
    initializeRows();
    initializeButtonCoordinates();
    addMouseListener(new MouseAdapter() {

    });
    setFocusable(true);
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
    return;
  }

  @Override
  public void resetFocus() {
    return;
  }

  @Override
  public void showErrorMessage(String error) {
    return;
  }

  @Override
  public ButtonView returnSelected() {
    return null;
  }

  @Override
  public ReadOnlyReversiModel returnModel() {
    return null;
  }

  @Override
  public void refresh() {
    return;
  }

  @Override
  public void addFeatures(Features features) {
    return;
  }

  @Override
  public void display() {
    return;
  }

  @Override
  public void displayColorLabel() {
    return;
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

  /**
   * Public Method used for testing.
   */
  public Coordinate handleButtonClick() {

    boolean clicked_on_button = false;
    for (ButtonView button : board) {
      if (button.getBounds().contains(this.x, this.y)) {
        clicked_on_button = true;
        deselectAllButOne(button);
        return button.getCoordinates();
      }
    }
    if (!clicked_on_button) {
      deselectAll();
    }
    return new Coordinate(1, 1);
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
    ArrayList<ButtonView> row_buttons = new ArrayList<ButtonView>();
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