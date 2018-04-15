package sample.Controllers;

import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.omg.CORBA.INITIALIZE;
import sample.Model.SFile;
import sample.Util.FileClient;

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
  private FileClient fileClient;

  public void setFiles(ObservableList<SFile> files) {
    this.files = files;
    TreeItem<SFile> root = new RecursiveTreeItem<SFile>(files, RecursiveTreeObject::getChildren);
    table_files.setRoot(root);
    table_files.setShowRoot(false);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    table_files.getSelectionModel().setSelectionMode(
        SelectionMode.MULTIPLE
    );
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
          else {
            imageview.setImage(null);
          }
        }
      };
      cell.setGraphic(imageview);
      return cell;
    });

    column_size.setCellFactory(column -> {
      TreeTableCell<SFile, Number> cell = new TreeTableCell<SFile, Number>() {
        public void updateItem(Number item, boolean empty) {
          if (item != null && item.intValue() != 0) {
            setText(item.toString() + " KB");
          } else {
            setText(null);
          }
        }
      };
      return cell;
    });

    // sample data
    // files.add(new SFile("d", "school", 50));
    // files.add(new SFile("f", "lab01.pdf", 27));
    // for (int i = 0; i < 50; i++)
    //   files.add(new SFile("f", "lab01.pdf", 27));
    TreeItem<SFile> root = new RecursiveTreeItem<SFile>(files, RecursiveTreeObject::getChildren);
    table_files.setRoot(root);
    table_files.setShowRoot(false);


    table_files.setRowFactory( e -> {
      TreeTableRow<SFile> row = new TreeTableRow<>();
      row.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
          SFile rowData = row.getItem();
          String filename = rowData.getName();
          // System.out.println(rowData.getName());
          try {
            ObservableList<SFile> files = fileClient.requestFileList(filename);
            System.out.println("files in " + filename);
            for (SFile file : files) {
              System.out.println("\t\t" + file.getName());
            }
            this.setFiles(files);
          } catch (IOException e1) {
            e1.printStackTrace();
          }

        }
      });
      return row;
    });
  }

  public void setFileClient(FileClient fileClient) {
    this.fileClient = fileClient;
  }

  @FXML
  private void downloadFiles() {
    String selectedDirectory = getFolder();

    ObservableList<TreeItem<SFile>> selectedItems = table_files.getSelectionModel()
        .getSelectedItems();
    List<String> filenames = new ArrayList<>();

    for (TreeItem<SFile> temp : selectedItems) {
      filenames.add(temp.getValue().getName());

      try {
        fileClient.receiveFiles(filenames, selectedDirectory);
      } catch (IOException e) {
        e.printStackTrace();
      }


    }
  }

  private String getFolder() {
    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setTitle("SHARE");
    File selectedDirectory = chooser.showDialog(rootPane.getScene().getWindow());
    return selectedDirectory.getAbsolutePath();
  }
}
