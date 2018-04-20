package sample.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class homeController implements Initializable {

  @FXML
  private Pane rootPane;
  private Pane clientPane, serverPane;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/sample/Views/clientSetup.fxml"));
      clientPane = loader1.load();

      rootPane.getChildren().addAll(clientPane);
      FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), clientPane);
      fadeTransition.setFromValue(1);
      fadeTransition.setToValue(0);
      fadeTransition.play();
      fadeTransition.setOnFinished(event -> {
        clientPane.setVisible(false);
      });
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/sample/Views/serverSetup.fxml"));
      serverPane = loader1.load();

      rootPane.getChildren().addAll(serverPane);
      FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), serverPane);
      fadeTransition.setFromValue(1);
      fadeTransition.setToValue(0);
      fadeTransition.play();
      fadeTransition.setOnFinished(event -> {
        serverPane.setVisible(false);
      });
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @FXML
  private void changeClient() {
    clientPane.setVisible(true);
    FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), clientPane);
    fadeTransition1.setFromValue(0);
    fadeTransition1.setToValue(1);
    fadeTransition1.play();
  }
  @FXML
  private void changeServer() {
    serverPane.setVisible(true);
    FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), serverPane);
    fadeTransition1.setFromValue(0);
    fadeTransition1.setToValue(1);
    fadeTransition1.play();
  }

}
