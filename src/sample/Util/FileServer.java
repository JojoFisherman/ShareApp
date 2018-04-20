package sample.Util;

import java.io.*;
import java.net.*;
import java.nio.file.Paths;
import java.util.List;

public class FileServer {

  private String path;
  private String password;
  private CommandPrompt commandPrompt;

  public FileServer(int port, String path, String password) throws IOException {
    ServerSocket sSocket = new ServerSocket(port);
    this.password = password;
    this.path = path;
    this.commandPrompt = new CommandPrompt(path);

    while (true) {
      Socket cSocket = sSocket.accept();

      new Thread(() -> {
        try {
          doOperation(cSocket);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }).start();
    }
  }

  private void doOperation(Socket socket) throws IOException {
    DataInputStream in = new DataInputStream(socket.getInputStream());
    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
    int opCode = in.readInt();
    switch (opCode) {
      // Authentication
      case 001:
        authentication(in, out);
        break;

      // Change directory and list files
      case 002:
        String path = receiveString(in);
        sendFileList(out, path);
        break;

      // Download
      case 003:
        String filepath = receiveString(in);
        sendFile(out, filepath);
        break;

    }
    in.close();
    out.close();
    socket.close();
  }

  private void authentication(DataInputStream in, DataOutputStream out) throws IOException {
    String pw = receiveString(in);
    out.writeBoolean(isPwCorrect(pw));

  }


  /**
   *
   * @param out
   * @param relativepath
   * @throws IOException
   */
  private void sendFileList(DataOutputStream out, String relativepath) throws IOException {
    // commandPrompt.changeDir(path);


    List<String> files = commandPrompt.listFiles(path + "\\" + relativepath, true);
    // write how many files
    out.writeLong(files.size());
    for (String file : files) {
      out.writeLong(file.length());
      out.writeBytes(file);
    }

  }

  private void sendFile(DataOutputStream out, String filename) throws IOException {
    File file = new File(path + "\\" + filename);
    if (file.isDirectory()) {
      sendFilenames(out, file.getCanonicalPath());
    } else {
      out.writeLong(0);
      FileInputStream in = new FileInputStream(file);
      byte[] buffer = new byte[1024];
      int len;
      long counter = 0;
      long filesize = file.length();
      out.writeLong(filesize);
      while (counter < filesize) {
        len = in.read(buffer, 0, buffer.length);
        counter += len;
        out.write(buffer, 0, len);
        out.flush();
      }
    }
  }

  private void sendFilenames(DataOutputStream out, String path) throws IOException {
    // commandPrompt.changeDir(path);
    List<String> filenames = commandPrompt.listFiles(path, false);
    // write how many files
    out.writeLong(filenames.size());
    for (String filename : filenames) {
      out.writeLong(filename.length());
      out.writeBytes(filename);
    }

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




  private boolean isPwCorrect(String inputPassword) {
    return inputPassword.equals(password);
  }


  public static void main(String[] args) {
    try {
      FileServer fileServer = new FileServer(9001, "C:/Users/Vincent/Desktop/shareFolder", "123");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}

