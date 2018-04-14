package sample.Controllers;

import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.omg.CORBA.INITIALIZE;
import sample.Model.SFile;

public class clientMainController implements Initializable {

  @FXML
  private AnchorPane rootPane;

  @FXML
  private AnchorPane holderPane;

  @FXML
  private JFXTreeTableView<SFile> table_files;

  @FXML
  private TreeTableColumn<SFile, String> column_type;

  @FXML
  private TreeTableColumn<SFile, String> column_name;

  @FXML
  private TreeTableColumn<SFile, Number> column_size;

  private ObservableList<SFile> files = FXCollections.observableArrayList();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    column_name.setCellValueFactory(cellData -> cellData.getValue().getValue().nameProperty());
    column_size.setCellValueFactory(cellData -> cellData.getValue().getValue().sizeProperty());
    column_type.setCellValueFactory(cellData -> cellData.getValue().getValue().typeProperty());
    column_type.setCellFactory(column -> {
      final Image fileIcon = new Image("/sample/src/file.png");
      final Image folderIcon = new Image("/sample/src/folder.png");
      final ImageView imageview = new ImageView();
      imageview.setFitHeight(50);
      imageview.setFitWidth(50);

      TreeTableCell<SFile, String> cell = new TreeTableCell<SFile, String>() {
        public void updateItem(String item, boolean empty) {
          if (item != null) {
            if (item.equals("d")) {
              imageview.setImage(folderIcon);
            } else {
              imageview.setImage(fileIcon);
            }
          }
        }
      };
      cell.setGraphic(imageview);
      return cell;
    });

    // sample data
    // files.add(new SFile("d", "school", 50));
    // files.add(new SFile("f", "lab01.pdf", 27));
    // for (int i = 0; i < 50; i++)
    //   files.add(new SFile("f", "lab01.pdf", 27));
    // TreeItem<SFile> root = new RecursiveTreeItem<SFile>(files, RecursiveTreeObject::getChildren);
    // table_files.setRoot(root);
    // table_files.setShowRoot(false);


  }


}
