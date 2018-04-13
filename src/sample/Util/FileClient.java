package sample.Util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Vincent on 3/20/2018.
 */
public class FileClient {
  Socket socket = null;

  public FileClient(String ip, int port, String pw) throws IOException {
    socket = new Socket(ip, port);
    // DataInputStream in = new DataInputStream(socket.getInputStream());
    // DataOutputStream out = new DataOutputStream(socket.getOutputStream());
    // String filename = sendFilename(out);
    // receiveFile(in, filename);
    // in.close();
    // out.close();
    // socket.close();
  }

  public boolean sendPassword(String pw) {
    return this.sendPassword(pw, socket);
  }

  boolean sendPassword(String pw, Socket socket) {
    boolean result = false;
    try(DataOutputStream out = new DataOutputStream(socket.getOutputStream());
    DataInputStream in = new DataInputStream(socket.getInputStream())) {
      out.writeInt(001);
      out.writeInt(pw.length());
      out.writeBytes(pw);

      // Receive result
       result = in.readBoolean();

    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("the result is :" + result);
    return result;
  }

  String sendFilename(DataOutputStream out) throws IOException {
    Scanner in = new Scanner(System.in);
    System.out.println("enter the complete file name");
    String filename = in.next();

    out.writeLong(filename.length());
    byte[] data = filename.getBytes();
    out.write(data);
    return filename;
  }


  void receiveFile(DataInputStream in, String filename) throws IOException {
    byte[] buffer = new byte[1024];
    long count = 0, size;
    int len;
    File file;
    FileOutputStream fout;
    file = new File(filename);
    fout = new FileOutputStream(new File(file.getName()));

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

}
