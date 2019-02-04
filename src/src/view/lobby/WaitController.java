package view.lobby;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import view.MainController;
import view.ViewConstants;
import view.music.Audio;

public class WaitController implements Initializable {

//  private RequestBuilder requestBuilder;
//  private NetworkController networkController;
  private MainController mainController;

//  private ClientModel model;

  @FXML
  private Text waitingState;

  @FXML
  private Text ipAddress;

  @FXML
  private Button startGameButton;

  @FXML
  private Text player0;

  @FXML
  private Text player1;

  @FXML
  private Text player2;

  @FXML
  private Text player3;

  @FXML
  private Button notMuteButton;

  @FXML
  private Button muteButton;

  @FXML
  private HBox ipArea;

  private Stage stage;

  @FXML
  private void startGame() {
    mainController.sendStartRequest();
  }

  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  public Stage getStage() {
    return stage;
  }

  public void setPlayerNames(List<String> names) {
    System.out.println("From WaitCtrl.: setting names.. " + names);
    switch (names.size()) { //TODO: Make readable
      case 4:
        player0.setText(names.get(0));
        player1.setText(names.get(1));
        player2.setText(names.get(2));
        player3.setText(names.get(3));
        break;
      case 3:
        player0.setText(names.get(0));
        player1.setText(names.get(1));
        player2.setText(names.get(2));
        player3.setText(ViewConstants.NO_PLAYER_SYMBOL);
        break;
      case 2:
        player0.setText(names.get(0));
        player1.setText(names.get(1));
        player2.setText(ViewConstants.NO_PLAYER_SYMBOL);
        player3.setText(ViewConstants.NO_PLAYER_SYMBOL);
        break;
      case 1:
        player0.setText(names.get(0));
        player1.setText(ViewConstants.NO_PLAYER_SYMBOL);
        player2.setText(ViewConstants.NO_PLAYER_SYMBOL);
        player3.setText(ViewConstants.NO_PLAYER_SYMBOL);
        break;
      default:
        break;
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }


  @FXML
  private void mute() {
    Audio.muteSoundOfWait();
    muteButton.setVisible(false);
    notMuteButton.setVisible(true);
  }

  @FXML
  private void unMute() {
    Audio.playMusicNow();
    notMuteButton.setVisible(false);
    muteButton.setVisible(true);
  }

  @FXML private void quitWaiting() {
    System.out.println("From QUIT in WaitCtrl.: disconnect client!");
    mainController.handleQuitPressed();
  }

  public void setServerIP(String serverIP) {
    ipAddress.setText(serverIP);
  }

  @FXML
  private void showHelpScene() {
    mainController.showHelpScene();
  }
}