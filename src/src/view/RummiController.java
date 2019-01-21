package view;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import network.client.RequestBuilder;
import network.client.RummiClient;
import network.server.RummiServer;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;

public class RummiController {

  @FXML private StackPane gameView;
  @FXML private StackPane startView;
  @FXML private StackPane waitView;

  private RummiClient client;
  private RummiServer server;
  private Stage stage;
  private Main main;
  private ClientModel model;

  @FXML private TextField nameField;
  @FXML private TextField ageField;
  @FXML private TextField ipField;

  @FXML private AnchorPane errorPane;
  @FXML private Text errorMessage;

  @FXML
  public void initialize() {
//    waitView.setVisible(false);

  }

  @FXML
  void returnToStart() {
    try {
      main.start(stage);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  void joinGame() throws IOException {
    join(new ClientModel(false));
  }

  @FXML
  void hostGame() throws IOException {
    server = new RummiServer();
    server.start();
    try {
      ipField.setText(Inet4Address.getLocalHost().getHostAddress());
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    join(new ClientModel(true));
  }

  @FXML
  private void switchToErrorView(String message) throws IOException {
    errorMessage.setText(message);
    errorPane.setVisible(true);
    startView.setVisible(false);
  }

  private void join(ClientModel model) throws IOException {
      this.model = model;
      // Create local the Client and then pass it to: RequestBuilder and NetworkController
      client = new RummiClient(ipField.getText());
      model.setServerIP(ipField.getText());
      // Create a RequestBuilder
      RequestBuilder reqBuilder = new RequestBuilder(client);
      System.out.println("Client:" + nameField.getText() + " started");
      // send request to set a player
      reqBuilder.sendSetPlayerRequest(nameField.getText(), Integer.parseUnsignedInt(ageField.getText()));
      startView.setVisible(false);
      waitView.setVisible(true);
  }

  void killThreads() {
    if (client != null) {
      client.disconnect();
    }
    if (server != null) {
      server.suicide();
    }
  }

  @FXML
  void handleOkButton() {
    waitView.setVisible(true);
  }

  public void setMain(Main main) {
    this.main = main;
  }

  @FXML
  void startGame() {

  }

  @FXML
  void drawStone() {

  }
}
