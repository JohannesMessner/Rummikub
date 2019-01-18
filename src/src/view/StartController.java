package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import network.client.RummiClient;
import network.server.RummiServer;

import java.io.IOException;

/**
 * Class acting as the controller before a game has been started.
 * Connects the Start-view to the model.
 */
public class StartController {

    private ClientModel model;
    private NetworkController networkController;
    private WaitController waitController;
    private RummiClient client;
    private String username;
    private Integer age;
    private String serverIp;

    @FXML
    private Button joinButton;

    @FXML
    private TextField nameField;
    @FXML
    private TextField ageField;

    @FXML
    private Button hostButton;
    @FXML
    private TextField ipField;

    private Stage stage;



/*  StartController(Main networkController, ClientModel model, RummiClient client) {
    this.networkController = networkController;
    this.model = model;
    this.client = client;
  }*/

    @FXML
    void joinGame() throws IOException {
        join(nameField.getText(), Integer.parseInt(ageField.getText()), ipField.getText());
        this.switchToWait();
    }

    @FXML
    void hostGame() throws IOException {
        new RummiServer().start();
        join(nameField.getText(), Integer.parseInt(ageField.getText()), ipField.getText());
        this.switchToWait();
    }

    /**
     * Constructor connecting controller, model and network.
     * Connects controller, model and network.
     *
     * @param networkController the "master-controller" of the application
     * @param model storing all the relevant data
     * @param client connection-point to the network
     */

    void initialize(NetworkController networkController, ClientModel model, RummiClient client) {
        this.networkController = networkController;
        this.model = model;
        this.client = client;
    }

    private void join(String name, int age, String serverIP) {
        networkController = new NetworkController(name, age, serverIP);
        System.out.println("Client:" + name + " started");
    }


    @FXML
    private void switchToErrorView() throws IOException {
        stage = (Stage) joinButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Error.fxml"));
        Parent root = loader.load();
        Scene errorScene = new Scene(root, 600, 400);
        stage.setScene(errorScene);

    }

    @FXML
    private void switchToWait() throws IOException {

        stage = (Stage) joinButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Wait.fxml"));
        Parent root = loader.load();

        waitController = loader.getController();
        waitController.setNetworkController(networkController);
        networkController.setWaitController(waitController);

        Scene gameScene = new Scene(root, 1024, 768);
        stage.setScene(gameScene);
    }
}