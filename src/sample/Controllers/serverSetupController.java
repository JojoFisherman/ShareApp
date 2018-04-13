package sample.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class serverSetupController implements Initializable {

  @FXML
  private Pane rootPane;

  @FXML
  private JFXButton button_choose;

  @FXML
  private JFXTextField textfield_location;

  @FXML
  private JFXPasswordField passwordfield_pw;

  private String dirPath;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    dirPath = null;


  }


  @FXML
  private void backAction() {
    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), rootPane);
    fadeTransition.setFromValue(1);
    fadeTransition.setToValue(0);
    fadeTransition.play();
    fadeTransition.setOnFinished(evet -> {
      rootPane.setVisible(false);
    });

  }

  @FXML
  private void chooseDirectory() {
    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setTitle("JavaFX Projects");
    // File defaultDirectory = new File("c:/dev/javafx");
    // chooser.setInitialDirectory(defaultDirectory);
    File selectedDirectory = chooser.showDialog(rootPane.getScene().getWindow());
    if (selectedDirectory != null) {
      textfield_location.setText(selectedDirectory.getAbsolutePath());
    }

  }

  private boolean isPathValid() {
    dirPath = textfield_location.getText();
    File file = new File(dirPath);
    return file.exists() && file.isDirectory();
  }

  @FXML
  private void createServer() {
    if (!isPathValid()) {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setHeaderText("Wrong Path!!");
      alert.setContentText("The share folder path is not correct! Please try again.");
      alert.showAndWait();
    }
    else if (passwordfield_pw.getLength() == 0) {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setHeaderText("Invalid Password!!");
      alert.setContentText("Please enter a password.");
      alert.showAndWait();
    }
    else {
    }

  }


}
