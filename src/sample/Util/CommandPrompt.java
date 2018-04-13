package sample.Util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class CommandPrompt {
	File currentDir;

	public CommandPrompt() {
		currentDir = new File(".");
	}

	public CommandPrompt(String path) {
		currentDir = new File(path);
	}

	public void exec(String statement) throws IOException {
		String command = null;
		String option = null;
		String msg;
		
		int endIdx = statement.trim().indexOf(' ');
		if (endIdx > 0) {
			command = statement.substring(0, endIdx).trim();
			option = statement.substring(endIdx + 1).trim();
		} else
			command = statement;

		switch (command.toLowerCase()) {
		case "cd":
			changeDir(option);
			break;
		case "dir":
			listFiles(option);
			break;
		case "freespace":
			msg = String.format("The free space is %,d bytes.", checkFreespace(option));
			System.out.println(msg);
			break;
		default:
			msg = String.format("'%s' is not recognized as an command.", command);
			System.out.println(msg);
			break;
		}
	}

	private long checkFreespace(String path) throws IOException {
		File dir;
		if (path == null)
			dir = currentDir;
		else if (path.startsWith("/") || path.startsWith("\\") || path.contains(":"))
			dir = new File(path);
		else
			dir = new File(currentDir.getCanonicalPath() + "/" + path);
		
		return dir.getFreeSpace();
	}

	public void changeDir(String path) throws IOException {
		if (path == null) {
			System.out.println(currentDir.getCanonicalPath());
			return;
		}
		
		File dir;
		if (path.startsWith("/") || path.startsWith("\\") || path.contains(":"))
			dir = new File(path);
		else
			dir = new File(currentDir.getCanonicalPath() + "/" + path);
		
		if (!dir.exists() || dir.isFile()) {
			System.out.println("The system cannot find the path specified.");
			return;
		}
		
		currentDir = dir;
	}

	private String getInfo(File f) {
		Date date = new Date(f.lastModified());
		String ld = new SimpleDateFormat("MMM dd, yyyy").format(date);
		if (f.isFile()) {
			return String.format("%dKB\t%s\t%s", (int) Math.ceil((float) f.length() / 1024), ld, f.getName());
		} else
			return String.format("<DIR>\t%s\t%s", ld, f.getName());
	}


	public void listFiles(String path) {
		File dir;

		if (path == null)
			dir = currentDir;
		else
			dir = new File(path);
		
		if (!dir.exists()) {
			System.out.println("File / directory does not exist.\n" + dir);
			return;
		}

		if (dir.isFile())
			System.out.println(getInfo(dir));
		else
		{
			File[] fileList = dir.listFiles();
			String info = "";
			for (int i = 0; i < fileList.length; i++)
				info += getInfo(fileList[i]) + "\n";
			System.out.println(info);
		}		
	}



}
