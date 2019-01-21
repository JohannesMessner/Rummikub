package view;

import communication.gameinfo.StoneInfo;
import communication.request.RequestID;
import communication.request.SimpleRequest;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import network.client.GameInfoHandler;
import network.client.RequestBuilder;
import network.client.RummiClient;
import network.server.RummiServer;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * Class acting as the controller before a game has been started.
 * Connects the Start-view to the model.
 */
public class RummiController implements Controller {

  private Stage stage;

  private RummiClient client;
  private ClientModel model = new ClientModel();
  private RequestBuilder requestBuilder;

  // start
  @FXML private TextField nameField;
  @FXML private TextField ageField;
  @FXML private TextField ipField;

  // wait
  @FXML private Label waitingState;
  @FXML private Text ipAddress;
  @FXML private Text player0;
  @FXML private Text player1;
  @FXML private Text player2;
  @FXML private Text player3;
  @FXML private Button startGameButton;

  // game
  @FXML Pane opponentMid;
  @FXML Text timer;
  @FXML GridPane table;
  @FXML GridPane handGrid;

//  @Override
//  public void initialize(URL location, ResourceBundle resources) {
//    model = new ClientModel();
//  }

  @FXML private void hostGame() {
    new RummiServer().start();
    try {
      ipField.setText(Inet4Address.getLocalHost().getHostAddress());
    } catch (UnknownHostException e) {
      // error
      e.printStackTrace();
    }
    model.notifyHost();
    initConnection();
  }

  @FXML private void joinGame() {
    initConnection();
  }

  private void initConnection() {
    client = new RummiClient(ipField.getText());
    client.setGameInfoHandler(new GameInfoHandler(this));
    client.start();
    requestBuilder = new RequestBuilder(client);
    try {
      switchScene(new FXMLLoader(getClass().getResource("wait.fxml")));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void switchScene(FXMLLoader loader) throws IOException {
    stage.setScene(new Scene(loader.load()));
  }

  @Override public void setPlayerNames(List<String> names) {
    model.setPlayerNames(names);
  }

  @Override
  public void setHandSizes(List<Integer> sizes) {
    model.setHandSizes(sizes);
  }

  @Override public void setTable(StoneInfo[][] table) {
    model.setTable(table);
  }

  @Override public void setPlayerHand(StoneInfo[][] hand) {
    model.setHand(hand);
  }

  @Override public void notifyTurn() {
    model.notifyTurn();
  }

  @Override public void notifyGameStart() {
    // change to game scene
    try {
      switchScene(new FXMLLoader(getClass().getResource("game.fxml")));
    } catch (IOException e) {
      e.printStackTrace();
    }
    // switchToGameScene();
    model.notifyGameStart();
  }

  @Override public void notifyCurrentPlayer(int playerID) {
    // show in game view
  }

  @Override public void notifyInvalidMove() {
    // error view
  }

  @Override public void setBagSize(int bagSize) {
    model.setBagSize(bagSize);
  }

  void setState(Stage primaryStage) {
    stage = primaryStage;
  }


  @FXML public void sendStartRequest() {
    System.out.println("sending start request...");
    requestBuilder.sendStartRequest();
  }

  @FXML public void sendDrawRequest() {
    client.sendRequest(new SimpleRequest(RequestID.DRAW));
  }

  @FXML public void handleOkButton() {
    client.sendRequest(new SimpleRequest(RequestID.CONFIRM_MOVE));
  }
}

