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
  private Path currentRelativePath;

  public FileClient(String ip, int port, String pw) throws IOException {
    this.ip = ip;
    this.port = port;
    this.pw = pw;
    this.currentRelativePath = Paths.get("");

  }

  /**
   * @param path the relative path of the folder to get information
   */
  public ObservableList<SFile> requestFileList(String path) throws IOException {
    DataOutputStream out;
    DataInputStream in;
    ObservableList<SFile> files = FXCollections.observableArrayList();
    Long nFiles;

    Socket socket = new Socket(ip, port);
    out = new DataOutputStream(socket.getOutputStream());
    in = new DataInputStream(socket.getInputStream());

    // User can't cd .. in the root of share directory
    if (!Paths.get(currentRelativePath.toString(), path).normalize().startsWith("..")) {
      currentRelativePath = Paths.get(currentRelativePath.toString(), path).normalize();
    }

    // Send operation code and path
    out.writeInt(002);
    out.writeLong(currentRelativePath.toString().length());
    out.writeBytes(currentRelativePath.toString());

    // Get How many Files
    nFiles = in.readLong();

    for (int i = 0; i < nFiles; i++) {
      String temp = receiveString(in);
      String[] info = temp.split("\\$");

      files.add(new SFile(info[0], info[1], Integer.parseInt(info[2])));
    }

    return files;
  }


  public boolean requestAuthentication(String pw) {
    boolean result = false;
    try (Socket socket = new Socket(ip, port)) {
      DataOutputStream out = new DataOutputStream(socket.getOutputStream());
      DataInputStream in = new DataInputStream(socket.getInputStream());

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


  /**
   *
   * @param filenames
   * @param topath
   * @throws IOException
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

  private void receiveFile(Socket socket, String filepath, String topath) throws IOException {
    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
    DataInputStream in = new DataInputStream(socket.getInputStream());
    out.writeInt(003);
    sendFilename(out, filepath);



    // filecount is the number of files in the directory
    // filecount = 0 if it's a file but not a directory
    long filecount = in.readLong();


    // It's a file
    if (filecount == -1) {

      byte[] buffer = new byte[1024];
      long count = 0, size;
      int len;
      File file = new File(topath + "/" + filepath);
      file.getParentFile().mkdirs(); // Create any non-exist parent folder
      FileOutputStream fout = new FileOutputStream(new File(topath + "/" + filepath));

      // receive the size of the file
      size = in.readLong();
      count = 0;

      // receive the file
      while (count < size) {
        len = in.read(buffer, 0, (int) Math.min(buffer.length, size - count));
        count += len;
        fout.write(buffer, 0, len);

        // System.out.printf("%s\t download: %d\n",Thread.currentThread().getName(), count*100/size);
      }
      // System.out.printf("%s\t download: %d\n",Thread.currentThread().getName(), count*100/size);
      fout.close();
    }

    // It's a directory.
    else {
      // handles empty directory
      if (filecount==0){
        File file = new File(topath + "/" + filepath);
        file.getParentFile().mkdirs(); // Create any non-exist parent folder
        file.mkdir();
      }
      List<String> filenames = new ArrayList<>();
      for (long i = 0; i < filecount; i++) {
        String temp = filepath + "/" + receiveString(in);
        filenames.add(temp);
      }
      receiveFiles(filenames, topath);
    }
    in.close();
    socket.close();
  }


  private void sendFilename(DataOutputStream out, String filename) throws IOException {
    out.writeLong(filename.length());
    out.writeBytes(filename);
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

  public String getPath() {
    return currentRelativePath.toString();
  }

  public static void main(String[] args) {
    try {
      FileClient fileClient = new FileClient("192.168.56.1", 9001, "123");
      List<String> filenames = new ArrayList<>();
      filenames.add("folder2");
      fileClient.receiveFiles(filenames, "C:/Users/Vincent/Desktop/downloadFolder");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
