package view;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class ErrorController {

    @FXML
    Text errorMessage;
    private String message;

    @FXML
    public void initialize() {
        this.errorMessage.setText(message);
    }

    public void initData(String message) {
        this.message = message;
    }

    @FXML
    void handleOkButton() {

    }
}
