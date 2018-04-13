package sample.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import sample.Util.FileServer;

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
  private FileServer fileServer;
  private Pane serverMainPane;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    dirPath = null;

    try {
      FXMLLoader loader1 = new FXMLLoader(getClass().getResource("../Views/serverMain.fxml"));
      serverMainPane = loader1.load();

      rootPane.getChildren().addAll(serverMainPane);
      FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), serverMainPane);
      fadeTransition.setFromValue(1);
      fadeTransition.setToValue(0);
      fadeTransition.play();
      fadeTransition.setOnFinished(event -> {
        serverMainPane.setVisible(false);
      });
    } catch (Exception e) {
      e.printStackTrace();
    }

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

  private boolean isPwValid() {
    return passwordfield_pw.getLength() > 0;

  }

  @FXML
  private void createServer() {
    if (!isPathValid()) {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setHeaderText("Wrong Path!!");
      alert.setContentText("The share folder path is not correct! Please try again.");
      alert.showAndWait();
    }
    else if (!isPwValid()) {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setHeaderText("Invalid Password!!");
      alert.setContentText("Please enter a password.");
      alert.showAndWait();
    }
    else {
      new Thread(() -> {
        try {
          fileServer = new FileServer(9001, dirPath, passwordfield_pw.getText());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }).start();
      changeServerMain();


    }

  }

  private void changeServerMain() {
    serverMainPane.setVisible(true);
    FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), serverMainPane);
    fadeTransition1.setFromValue(0);
    fadeTransition1.setToValue(1);
    fadeTransition1.play();
  }

}
