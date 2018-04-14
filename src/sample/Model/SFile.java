package sample.Model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class SFile extends RecursiveTreeObject<SFile> {
  private final StringProperty type;
  private final StringProperty name;
  private final IntegerProperty size;


  public SFile(String type, String name, int size) {
    this.type = new SimpleStringProperty(type);
    this.name = new SimpleStringProperty(name);
    this.size = new SimpleIntegerProperty(size);
  }


  public String getType() {
    return type.get();
  }

  public StringProperty typeProperty() {
    return type;
  }

  public void setType(String type) {
    this.type.set(type);
  }

  public void setSize(int size) {
    this.size.set(size);
  }

  public String getName() {
    return name.get();
  }

  public StringProperty nameProperty() {
    return name;
  }

  public void setName(String name) {
    this.name.set(name);
  }

  public int getSize() {
    return size.get();
  }

  public IntegerProperty sizeProperty() {
    return size;
  }

}
