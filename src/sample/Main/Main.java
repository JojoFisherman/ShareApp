package sample.Main;

import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/Views/mainLayout.fxml"));
    AnchorPane pane = loader.load();
    Scene scene = new Scene(pane);
    primaryStage.setScene(scene);
    primaryStage.setTitle("SHARE");
    primaryStage.getIcons().addAll(
        new Image("/sample/src/icon.jpg"));
    primaryStage.setResizable(false);

    primaryStage.show();


  }


  public static void main(String[] args) {
    launch(args);
  }
}
