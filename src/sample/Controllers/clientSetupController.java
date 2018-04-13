package sample.Controllers;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class clientSetupController implements Initializable {

  @FXML
  private Pane rootPane;

  @Override
  public void initialize(URL location, ResourceBundle resources) {



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

}
