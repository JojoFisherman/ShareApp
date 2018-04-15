package sample.Util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import sample.Model.SFile;

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
			listFiles(option, true);
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

	private String getInfo(File f, boolean isFullInfo) {
		if (isFullInfo) {
			if (f.isFile()) {
				return String
						.format("%s$%s$%d", "f", f.getName(), f.length());
			} else {
				return String.format("%s$%s$%d", "d", f.getName(), getFolderSize(f));
			}
		}
		else {
			if (f.isFile()) {
				return f.getName();
			} else {
				return f.getName();
			}
		}
	}


	public List<String> listFiles(String path, boolean isFullInfo) {
		File dir;
		List <String> res = new ArrayList<>();

		if (path == null || path.equals(""))
			dir = currentDir;
		else
			dir = new File(path);
		
		if (!dir.exists()) {
			System.out.println("File / directory does not exist.\n" + dir);
			return null;
		}

		if (dir.isFile())
			System.out.println(getInfo(dir, isFullInfo));
		else
		{
			File[] fileList = dir.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				res.add(getInfo(fileList[i], isFullInfo));
			}
		}
		return res;
	}

	// Get Folder Size
	private long getFolderSize(File folder) {
		long size = 0;
		File[] files = folder.listFiles();


		for (File file : files) {
			if (file.isFile()) {
				size += file.length();
			}
			else {
				size += getFolderSize(file);
			}
		}
		return size;
	}


	public static void main(String [] args) throws IOException {
		CommandPrompt cp = new CommandPrompt();
		System.out.println(cp.getFolderSize(new File("C:\\Users\\Vincent\\Desktop\\shareFolder"))/1000);
		System.out.println(cp.checkFreespace("C:\\Users\\Vincent\\Desktop\\shareFolder")/1000000);
		// List<String> files = cp.listFiles("C:\\Users\\Vincent\\Desktop\\shareFolder", true);
		// for (String file : files) {
		// 	System.out.println(file);
		// }

	}



}
