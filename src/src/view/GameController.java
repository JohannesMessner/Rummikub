package view;

import static game.Stone.Color.JOKER;

import communication.gameinfo.StoneInfo;

import java.util.*;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import view.music.Audio;

/**
 * Controller responsible for the display of the game-view.
 * Handles all update of the GUI while the game is ongoing.
 */
public class GameController {

  private static DataFormat stoneFormat = new DataFormat(ViewConstants.STONE_FORMAT);

  private boolean isMyTurn = false;

  @FXML private HBox opponentRight;
  @FXML private HBox opponentMid;
  @FXML private HBox opponentLeft;
  @FXML private Text ownName;
  @FXML private Text ownHand;
  @FXML private Text leftPlayerName;
  @FXML private Text midPlayerName;
  @FXML private Text rightPlayerName;
  @FXML private Text leftPlayerHand;
  @FXML private Text midPlayerHand;
  @FXML private Text rightPlayerHand;
  @FXML private Text timer;
  @FXML private GridPane tableGrid;
  @FXML private GridPane handGrid;
  @FXML private VBox ownBoard;
  @FXML private Button drawButton;

  private MainController mainController;
  // TIMER
  private Timer timer_countDown;
  private TimerTask timer_task;

  // Ctrl + Drag and Drop
  private boolean isMultiMove;

  /**
   * Connects the GameController to a MainController.
   *
   * @param mainController to be connected to
   */
  void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  private void setTimer() {
    int delay = 1000;
    int period = 1000;
    timer_countDown = new Timer();
    timer_countDown.scheduleAtFixedRate(
      timer_task = new TimerTask() {
        int remainingTime = 60;

        public void run() {
          if (remainingTime == 0) {

            if (isMyTurn) {
              stopTimer();
              sendTimeOutRequest();
              return;
            }
            stopTimer();
            return;
          }
          timer.setText(Integer.toString(remainingTime));
          remainingTime--;
        }
      },
      delay,
      period);
  }

  /**
   * Stops the game-clock.
   */
  void stopTimer() {
    timer_task.cancel();
    timer_countDown.cancel();
  }

  /**
   * Quits the game.
   */
  public void quitGame() {
    System.out.println("From QUIT in GameCtrl.: disconnect client!");
    mainController.handleQuitPressed();
  }

  /**
   * Signals that the player can now play.
   */
  void yourTurn() {
    ownBoard.setStyle(ViewConstants.CURRENTLY_PLAYING_STYLE);
    isMyTurn = true;
  }

  /**
   * Signals that the player can't play anymore until it's his turn again.
   */
  private void endOfYourTurn() {
    ownBoard.setStyle(ViewConstants.NOT_CURRENTLY_PLAYING_STYLE);
    isMyTurn = false;
  }

  /**
   * Method to request stone from server and place it in player's hand
   * event: User clicks draw button
   */
  @FXML
  public void drawStone() {
    Audio.playSoundOf(Audio.Sound.DRAW_STONE);
    mainController.sendDrawRequest();
  }

  /**
   * Method to automatically construct columns, rows, and cells with StackPane in it.
   *
   * @param stoneGrid Matrix array with StoneInfo data as source for view
   * @param grid      FXML GridPane to display the stone data in
   */
  @FXML
  private void constructGrid(StoneInfo[][] stoneGrid, GridPane grid) {
    Platform.runLater(() -> {
      List<Node> gridCells = grid.getChildren();

      if (gridCells.isEmpty()) {
        // Build grid cells when game is started
        int width = stoneGrid.length;
        int height = stoneGrid[0].length;

        for (int x = 0; x < width; x++) {
          for (int y = 0; y < height; y++) {

            StackPane cell = new StackPane();
            StoneInfo stoneInfo;

            if (stoneGrid[x][y] != null) {
              stoneInfo = stoneGrid[x][y];
              putStoneInCell(cell, stoneInfo);
            }

            cell.getStyleClass().add(ViewConstants.CELL_STYLE);
            grid.add(cell, x, y);
            setupDragAndDrop(cell);
          }
        }
      } else {
        // Use existing cells to display the stones in
        for (Node cell : gridCells) {
          if (cell instanceof Pane) {
            ((Pane) cell).getChildren().clear();
            setupDragAndDrop((Pane) cell);
            int x = GridPane.getColumnIndex(cell);
            int y = GridPane.getRowIndex(cell);
            if (stoneGrid[x][y] != null) {
              StoneInfo stoneInfo = stoneGrid[x][y];
              putStoneInCell((Pane) cell, stoneInfo);
            } else {
              ((Pane) cell).getChildren().clear();
            }
          }
        }
      }
    });
  }

  /**
   * Method to setup drag event, content to copy on clipboard, and drop event for a cell.
   *
   * @param cell Pane where the event shall be registered
   */
  private void setupDragAndDrop(Pane cell) {
    // Get cell coordinates
    int thisColumn = GridPane.getColumnIndex(cell);
    int thisRow = GridPane.getRowIndex(cell);

    // Start drag and drop, copy stone to clipboard, delete stone in view
    cell.setOnDragDetected(event -> {
      if (!cell.getChildren().isEmpty()) {
        Audio.playSoundOf(Audio.Sound.PICK_UP_STONE);

        Dragboard dragBoard = cell.startDragAndDrop(TransferMode.ANY);
        Image dragGraphic;
        if (event.isControlDown()) {
          isMultiMove = true;
          dragGraphic = new Image(getClass().getResource(ViewConstants.MULTIPLE_STONES_IMAGE).toString());
        } else {
          // Create drag view without cell styling
          SnapshotParameters snapshotParameters = new SnapshotParameters();
          snapshotParameters.setFill(Color.TRANSPARENT);
          cell.getStyleClass().remove(ViewConstants.CELL_STYLE);
          dragGraphic = cell.snapshot(snapshotParameters, null);
          cell.getStyleClass().add(ViewConstants.CELL_STYLE);
        }
        dragBoard.setDragView(dragGraphic, dragGraphic.getWidth() * 0.5, dragGraphic.getHeight() * 0.7);
        ClipboardContent content = new ClipboardContent();

        // Put dummy stone on clipboard
        StoneInfo dummyStone = new StoneInfo(null, 0);
        content.put(stoneFormat, dummyStone);
        dragBoard.setContent(content);
      }
      event.consume();
    });

    // Enable cell to accept drop
    cell.setOnDragOver(event -> {
      if (event.getDragboard().hasContent(stoneFormat)) {
        Object sourceCell = event.getGestureSource();
        if (sourceCell instanceof StackPane) {
          String targetParentId = cell.getParent().getId();
          String sourceParentId = ((StackPane) sourceCell).getParent().getId();
          if (sourceParentId.equals(ViewConstants.HAND_GRID_ID) ||
                  (sourceParentId.equals(ViewConstants.TABLE_GRID_ID) && !(targetParentId.equals(ViewConstants.HAND_GRID_ID)))) {
            event.acceptTransferModes(TransferMode.ANY);
          }
        }
      }
      event.consume();
    });

    cell.setOnDragEntered(event -> {
      cell.setStyle(ViewConstants.STONE_WHILE_DRAGGING_STYLE);
    });

    cell.setOnDragExited(event -> {
      cell.setStyle(ViewConstants.STONE_STYLE);
    });

    // Put stone in target cell, notify server
    cell.setOnDragDropped(event -> {
      Audio.playSoundOf(Audio.Sound.DROP_STONE);
      Pane sourceCell = (Pane) event.getGestureSource();
      sourceCell.getChildren().clear();
      int sourceColumn = GridPane.getColumnIndex(sourceCell);
      int sourceRow = GridPane.getRowIndex(sourceCell);

      Parent sourceParent = sourceCell.getParent();
      Parent targetParent = cell.getParent();
      if (sourceParent.getId().equals(ViewConstants.HAND_GRID_ID)) {
        if (targetParent.getId().equals(ViewConstants.HAND_GRID_ID)) {
          if (isMultiMove) {
            mainController.sendMoveSetOnHand(sourceColumn, sourceRow, thisColumn, thisRow);
          } else {
            mainController.sendMoveStoneOnHand(sourceColumn, sourceRow, thisColumn, thisRow);
          }
        } else {
          if (isMultiMove) {
            mainController.sendPutSetRequest(sourceColumn, sourceRow, thisColumn, thisRow);
          } else {
            mainController.sendPutStoneRequest(sourceColumn, sourceRow, thisColumn, thisRow);
          }
        }
      } else {

        if (isMultiMove) {
          mainController.sendMoveSetOnTableRequest(sourceColumn, sourceRow, thisColumn, thisRow);
        } else {
          mainController.sendMoveStoneOnTable(sourceColumn, sourceRow, thisColumn, thisRow);
        }
      }
      isMultiMove = false;
      event.consume();
    });
  }

  /**
   * Method for displaying a stone in a cell
   *
   * @param cell  Cell in which the stone shall be displayed
   * @param stone Properties (color, value) of the stone which shall be displayed
   */
  private void putStoneInCell(Pane cell, StoneInfo stone) {
    Platform.runLater(() -> {
      ImageView stoneBackground = new ImageView(getClass().getResource(ViewConstants.STONE_BACKGROUND_IMAGE).toString());
      stoneBackground.getStyleClass().add(ViewConstants.SHADOW_STYLE);
      cell.getChildren().add(stoneBackground);

      String stoneColor = stone.getColor();
      Text stoneText = new Text();
      if (stoneColor.equals(JOKER.toString())) {
        Circle jokerBackground = new Circle(10);
        jokerBackground.getStyleClass().add(ViewConstants.JOKER_BACKGROUND_STYLE);
        cell.getChildren().add(jokerBackground);

        stoneText.setText(ViewConstants.JOKER_SYMBOL);
      } else {
        String stoneValue = Integer.toString(stone.getNumber());
        stoneText.setText(stoneValue);
      }

      stoneText.getStyleClass().addAll(ViewConstants.STONE_VALUE_STYLE, stoneColor);

      cell.getChildren().addAll(stoneText);
    });
  }

  /**
   * Updates the table that the player sees.
   *
   * @param table to be displayed.
   */
  void setTable(StoneInfo[][] table) {
    constructGrid(table, tableGrid);
  }

  /**
   * Updates the hand that the player sees.
   *
   * @param hand to be hand.
   */
  void setPlayerHand(StoneInfo[][] hand) {
    constructGrid(hand, handGrid);
  }

  void notifyInvalidMove() {

  }

  /**
   * Updates the number of stones available in the bag.
   *
   * @param bagSize number of stones available
   */
  void setBagSize(int bagSize) {
    Platform.runLater(() -> {
      String drawButtonComplementFront = "Draw (";
      String drawButtonComplementEnd = " left)";
      drawButton.setText(drawButtonComplementFront + bagSize + drawButtonComplementEnd);
    });
  }


  /**
   * Updates the number of stones each opponent has on his hand.
   *
   * @param sizes number of stones on the hands.
   */
  void setHandSizes(List<Integer> sizes) {
    String handComplement = " Stones";
    ownHand.setText(sizes.get(0) + handComplement);
    switch (sizes.size()) {
      case 2:
        midPlayerHand.setText(sizes.get(1) + handComplement);
        break;
      case 3:
        leftPlayerHand.setText(sizes.get(1) + handComplement);
        rightPlayerHand.setText(sizes.get(2) + handComplement);
        break;
      case 4:
        leftPlayerHand.setText(sizes.get(1) + handComplement);
        midPlayerHand.setText(sizes.get(2) + handComplement);
        rightPlayerHand.setText(sizes.get(3) + handComplement);
        break;
      default:
    }
  }

  /**
   * Updates the names of the opponents.
   *
   * @param names of the opponents
   */
  void setPlayerNames(List<String> names) {

    String nameComplement = ": ";
    switch (names.size()) {
      case 2:
        opponentMid.setVisible(true);
        opponentLeft.setVisible(false);
        opponentRight.setVisible(false);
        midPlayerName.setText(names.get(1) + nameComplement);
        break;
      case 3:
        opponentMid.setVisible(false);
        leftPlayerName.setText(names.get(1) + nameComplement);
        rightPlayerName.setText(names.get(2) + nameComplement);
        break;
      case 4:
        leftPlayerName.setText(names.get(1) + nameComplement);
        midPlayerName.setText(names.get(2) + nameComplement);
        rightPlayerName.setText(names.get(3) + nameComplement);
        break;
      default:
    }
  }

  /**
   * Updates whose opponent turn it is.
   *
   * @param relativeOpponentPosition number of steps (clockwise) until you reach the opponent
   */
  @FXML
  void notifyCurrentPlayer(int relativeOpponentPosition) {
    if (timer_countDown != null) {
      timer_countDown.cancel();
      timer_task.cancel();
    }
    endOfYourTurn();
    setTimer();
    HBox[] opponents = new HBox[]{
            opponentLeft, opponentMid, opponentRight
    };

    int opponentID = toOpponentID(relativeOpponentPosition, opponents);

    //sets the color of all the opponents
    for (int i = 1; i < opponents.length + 1; i++) {
      if (i != opponentID) {
        //styling non-playing opponents
        opponents[i - 1].setBackground(new Background(new BackgroundFill(
                Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
      } else {
        //styling currently playing opponent
        opponents[i - 1].setBackground(new Background((new BackgroundFill(
                Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY))));
      }
    }
  }


  /**
   * Determines how many players are getting displayed.
   *
   * @param opponents possibly displayed opponents
   * @return number of currently displayed players
   */
  private int getNumOfVisiblePlayers(HBox[] opponents) {
    int numOfPlayers = 1;
    for (HBox opponent : opponents) {
      if (opponent.isVisible()) {
        numOfPlayers++;
      }
    }
    return numOfPlayers;
  }

  /**
   * Calculates the id of an opponent based on his relative postion to the player.
   *
   * @param relativeOpponentPosition number of steps (clockwise) until you reach the opponent
   * @return position of the opponent
   * 0 -> self
   * 1 -> left
   * 2 -> middle/top
   * 3 -> right
   */
  private int toOpponentID(int relativeOpponentPosition, HBox[] opponents) {

    int numOfPlayers = getNumOfVisiblePlayers(opponents);
    System.out.println(numOfPlayers);

    int opponentID;
    switch (numOfPlayers) {
      case 0:
        opponentID = 0;
        break;

      case 1:
        opponentID = 0;
        break;

      case 2:
        opponentID = relativeOpponentPosition % 2;
        if (opponentID == 1) {
          opponentID = 2;
        }
        break;

      case 3:
        if (relativeOpponentPosition == 3) {
          opponentID = 0;
        } else if (relativeOpponentPosition == 2) {
          opponentID = 3;
        } else {
          opponentID = relativeOpponentPosition;
        }
        break;

      case 4:
        opponentID = relativeOpponentPosition;
        break;

      default:
        opponentID = relativeOpponentPosition;

    }
    System.out.println("OpponentPosition is " + opponentID);
    return opponentID;
  }

  private void sendTimeOutRequest() {
    mainController.sendTimeOutRequest();
    System.out.println("--------------------------TIME_OUT");
    endOfYourTurn();
  }


  @FXML
  private void sendResetRequest() {
    mainController.sendResetRequest();
  }

  @FXML
  private void sendConfirmMoveRequest() {
    mainController.sendConfirmMoveRequest();
  }

  @FXML
  private void sendSortHandByGroupRequest() {
    mainController.sendSortHandByGroupRequest();
  }

  @FXML
  private void sendSortHandByRunRequest() {
    mainController.sendSortHandByRunRequest();
  }

  @FXML
  private void showHelpScene() {
    mainController.showHelpScene();
  }

  @FXML
  private void sendUndoRequest() {
    mainController.sendUndoRequest();
  }
}
