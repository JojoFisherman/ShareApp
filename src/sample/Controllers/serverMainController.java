package sample.Controllers;

import com.jfoenix.controls.JFXTextField;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

public class serverMainController implements Initializable {
  @FXML
  private AnchorPane rootPane;

  @FXML
  private JFXTextField textfield_ip;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      textfield_ip.setText(InetAddress.getLocalHost().getHostAddress());
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }
}
