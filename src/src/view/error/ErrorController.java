package view.error;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;

/**
 * Controller responsible fot the display of error-messages.
 */
public class ErrorController {

  @FXML
  private Text errorMessage;

  @FXML
  private Button okButton;

  /**
   * Displays an error-message with custom text.
   * @param message text of the error-message
   */
  @FXML
  public void setErrorMessage(String message) {
    errorMessage.setText(message);
  }

  @FXML
  private void handleOkButton() {
    ((Stage) okButton.getScene().getWindow()).close();
  }
}
