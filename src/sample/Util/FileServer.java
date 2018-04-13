package sample.Util;

import java.io.*;
import java.net.*;

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
      System.out.println("Listening at port " + port);
      Socket cSocket = sSocket.accept();

      new Thread(() -> {
        try {
          doOperation(cSocket);
          cSocket.close();
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
        break;

      // Download a file
      case 003:
        break;

      // Download an entire folder
      case 004:
        break;
    }
    in.close();
    out.close();
  }

  private void serve(Socket socket) throws IOException {


    // String filename;
    // DataOutputStream out =
    //     new DataOutputStream(socket.getOutputStream());
    // filename = receiveFilename(in);
    // sendFile(out, filename);
    // in.close();
    // out.close();
  }

  private String receiveFilename(DataInputStream in) throws IOException {
    byte[] buffer = new byte[1024];
    int len;
    long counter = 0;
    long filesize = in.readLong();
    String filename = "";
    while (counter < filesize) {
      len = in.read(buffer, 0, buffer.length);
      counter += len;
      filename += new String(buffer, 0, len);
    }
    return filename;
  }


  private void sendFile(DataOutputStream out, String filename) throws IOException {
    File file = new File(filename);
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

  private  boolean isPwCorrect(String inputPassword) {
    return inputPassword.equals(password);
  }

  private void authentication(DataInputStream in, DataOutputStream out) throws IOException {
    String pw = "";
    int len = 0;
    byte[] buffer = new byte[1024];
    int count = 0;
    int size = in.readInt();
    while (count < size) {
      len = in.read(buffer, 0, Math.min(buffer.length, size - count));
      pw += new String(buffer, 0, len);
      count += len;
    }

    System.out.println("pw:" + pw);
    System.out.println("isCorrect:" + isPwCorrect(pw));
    out.writeBoolean(isPwCorrect(pw));




    // while (in.available() > 0) {
    //   len = in.readInt();
    //   in.read(buffer, 0, len);
    //   pw += new String(buffer, 0, len);
    // }

  }
}

