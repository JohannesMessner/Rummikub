package view;

import javafx.fxml.FXML;

import javax.xml.soap.Text;

public class ErrorController {

    @FXML
    Text errorMessage;

    @FXML
    public void initialize(String errorMassage) {
        this.errorMessage.setValue(errorMassage);
    }

    @FXML
    void handleOkButton() {

    }
}
