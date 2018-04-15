package sample.Util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import sample.Model.SFile;

/**
 * Created by Vincent on 3/20/2018.
 */
public class FileClient {

  private String ip;
  private int port;
  private String pw;
  // private String currentRelativePath;
  private Path currentRelativePath;

  public FileClient(String ip, int port, String pw) throws IOException {
    this.ip = ip;
    this.port = port;
    this.pw = pw;
    // this.currentRelativePath = "";
    this.currentRelativePath = Paths.get("");

  }

  public ObservableList<SFile> requestFileList(String path) throws IOException {
    Socket socket = new Socket(ip, port);
    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
    DataInputStream in = new DataInputStream(socket.getInputStream());

    // if (currentRelativePath.getPath() == 0) {
    //   currentRelativePath += path;
    // } else {
    //   currentRelativePath += "\\" + path;
    // }
    // System.out.println("client's current path: " + currentRelativePath);

    // if (currentRelativePath == null)
    //   currentRelativePath = new File(path);
    // else {
    //   currentRelativePath = new File(currentRelativePath.getPath() + "\\" + path);
    // }
    if (!Paths.get(currentRelativePath.toString(), path).normalize().startsWith("..")) {
      currentRelativePath = Paths.get(currentRelativePath.toString(), path).normalize();
    }



    out.writeInt(002);
    out.writeLong(currentRelativePath.toString().length());
    out.writeBytes(currentRelativePath.toString());

    return receiveFileList(in);
  }

  private ObservableList<SFile> receiveFileList(DataInputStream in) throws IOException {
    ObservableList<SFile> files = FXCollections.observableArrayList();

    // Get How many Files
    Long nFiles = in.readLong();

    for (int i = 0; i < nFiles; i++) {
      String temp = receiveString(in);
      String[] info = temp.split("\\$");

      files.add(new SFile(info[0], info[1], Integer.parseInt(info[2])));
    }

    return files;
  }

  private String receiveString(DataInputStream in) throws IOException {
    byte[] buffer = new byte[1024];
    int len;
    long counter = 0;
    long stringSize = in.readLong();
    String str = "";
    while (counter < stringSize) {
      len = in.read(buffer, 0, (int) Math.min(buffer.length, stringSize - counter));
      counter += len;
      str += new String(buffer, 0, len);
    }
    return str;
  }

  public boolean requestAuthentication(String pw) {
    boolean result = false;
    try (Socket socket = new Socket(ip, port);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream())) {
      result = false;
      out.writeInt(001);
      out.writeLong(pw.length());
      out.writeBytes(pw);

      // Receive result
      result = in.readBoolean();

    } catch (IOException e) {
      // server ip incorrect
      return false;
    }

    return result;
  }

  void sendFilename(DataOutputStream out, String filename) throws IOException {
    out.writeLong(filename.length());
    out.writeBytes(filename);
  }

  /**
   * @param filenames a list that contains the names of all files to download
   */
  public void receiveFiles(List<String> filenames, String topath) throws IOException {
    for (String filename : filenames) {
      Socket socket = new Socket(ip, port);
      new Thread(() -> {
        try {
          receiveFile(socket, filename, topath);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }).start();
    }

  }

  public void receiveFile(Socket socket, String filepath, String topath) throws IOException {
    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
    out.writeInt(003);
    sendFilename(out, filepath);

    DataInputStream in = new DataInputStream(socket.getInputStream());
    long filecount = in.readLong();

    // Base case: file count = -1
    if (filecount == -1) {

      byte[] buffer = new byte[1024];
      long count = 0, size;
      int len;
      File file;
      FileOutputStream fout;
      file = new File(topath + "\\" + filepath);
      file.getParentFile().mkdirs();
      fout = new FileOutputStream(new File(topath + "\\" + filepath));

      // receive the size of the file
      size = in.readLong();
      count = 0;

      // receive the file
      while (count < size) {
        len = in.read(buffer, 0, (int) Math.min(buffer.length, size - count));
        count += len;
        fout.write(buffer, 0, len);
      }
      fout.close();
    }

    // It's a directory.
    else {

      List<String> filenames = new ArrayList<>();
      for (long i = 0; i < filecount; i++) {
        String temp = filepath + "\\" + receiveString(in);
        filenames.add(temp);
      }
      receiveFiles(filenames, topath);
    }
    socket.close();
  }


  public static void main(String[] args) {
    try {
      FileClient fileClient = new FileClient("192.168.56.1", 9001, "123");
      List<String> filenames = new ArrayList<>();
      filenames.add("folder1");
      fileClient.receiveFiles(filenames, "C:/Users/Vincent/Desktop/downloadFolder");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
