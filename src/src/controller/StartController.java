package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.ClientModel;

import java.util.Observer;

public class StartController {

    private ClientModel model;

    @FXML private TextField nameField;
    @FXML private TextField ageField;
    @FXML private Button joinButton;
    @FXML private Button hostButton;
    Observer observer;

    @FXML
    void joinGame() {
        model.setName(nameField.getText());
        model.setAge(Integer.parseInt(ageField.getText()));

    }

    @FXML
    void hostGame() {

    }
}
