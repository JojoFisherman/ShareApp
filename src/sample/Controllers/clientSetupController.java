package sample.Controllers;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.Socket;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import sample.Util.FileClient;

public class clientSetupController implements Initializable {

  @FXML
  private AnchorPane rootPane;

  @FXML
  private JFXButton button_connect;

  @FXML
  private JFXTextField textfield_ip;

  @FXML
  private JFXPasswordField passwordfield_pw;

  private FileClient fileClient;
  private Pane connectingPane;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    fileClient = null;
    try {
      FXMLLoader loader1 = new FXMLLoader(getClass().getResource("../Views/connecting.fxml"));
      connectingPane = loader1.load();

      rootPane.getChildren().addAll(connectingPane);
      FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), connectingPane);
      fadeTransition.setFromValue(1);
      fadeTransition.setToValue(0);
      fadeTransition.play();
      fadeTransition.setOnFinished(event -> {
        connectingPane.setVisible(false);
      });
    } catch (Exception e) {
      e.printStackTrace();
    }


  }

  @FXML
  private void backAction() {
    FadeTransition fadeTransition=new FadeTransition(Duration.seconds(0.5),rootPane);
    fadeTransition.setFromValue(1);
    fadeTransition.setToValue(0);
    fadeTransition.play();
    fadeTransition.setOnFinished(evet -> {
      rootPane.setVisible(false);
    });

  }

  @FXML
  private void connectServer() {
    String ip = textfield_ip.getText();
    String pw = passwordfield_pw.getText();

    connectingPane.setVisible(true);
    FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), connectingPane);
    fadeTransition1.setFromValue(0);
    fadeTransition1.setToValue(1);
    fadeTransition1.play();
    fadeTransition1.setOnFinished(event1 -> {
      new Thread(() -> {
        try {
          fileClient = new FileClient(ip, 9001, pw);
          System.out.println("result" + fileClient.sendPassword(pw));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }).start();

      // boolean result = fileClient.sendPassword(pw);
    });


  }



}
