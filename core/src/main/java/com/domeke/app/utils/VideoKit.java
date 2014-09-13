/**
 * 
 */
package com.domeke.app.utils;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author lijiasen@domeke.com
 *
 */
public class VideoKit {

	private static String FFMEPG_PATH = "G:\\ffmpeg-2.3.3";

	public static boolean checkVideoType(String filepath) {
		int fileSuffixIndex = filepath.lastIndexOf(".");
		int pathLen = filepath.length();
		String type = filepath.substring(fileSuffixIndex + 1, pathLen).toLowerCase();
		if (type.equals("avi")) {
			return true;
		} else if (type.equals("mpg")) {
			return true;
		} else if (type.equals("wmv")) {
			return true;
		} else if (type.equals("3gp")) {
			return true;
		} else if (type.equals("mov")) {
			return true;
		} else if (type.equals("mp4")) {
			return true;
		} else if (type.equals("asf")) {
			return true;
		} else if (type.equals("asx")) {
			return true;
		} else if (type.equals("flv")) {
			return true;
		}
		return false;
	}

	public static boolean checkFile(String filepath) {
		File file = new File(filepath);
		if (!file.isFile()) {
			return false;
		}
		return true;
	}

	public static String getFilePathAndName(String filepath) {
		int filePrefixIndex = filepath.lastIndexOf(".");
		String filePathAndName = filepath.substring(0, filePrefixIndex + 1);
		return filePathAndName;
	}

	public static void compressVideo(String filepath) {
		if (!checkFile(filepath)) {
			return;
		}
		List<String> command = Lists.newArrayList();
		command.add(FFMEPG_PATH);
		command.add("-i");
		command.add(filepath);
		command.add("-ab");
		command.add("64");
		command.add("-acodec");
		command.add("mp3");
		command.add("-ac");
		command.add("2");
		command.add("-ar");
		command.add("22050");
		command.add("-b");
		command.add("230");
		command.add("-r");
		command.add("24");
		command.add("-y");
		command.add(getFilePathAndName(filepath) + ".flv");
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(command);
			builder.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		VideoKit.compressVideo("G:\\大尾鲈鳗_hd.mp4");
	}
}
