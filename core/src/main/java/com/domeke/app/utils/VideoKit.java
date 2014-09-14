/**
 * 
 */
package com.domeke.app.utils;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * @author lijiasen@domeke.com
 *
 */
public class VideoKit {

	private static Logger logger = LoggerFactory.getLogger(VideoKit.class);

	private static String FFMEPG_PATH = VideoKit.class.getResource("") + "ffmpeg.exe";

	private static int COMMOND_VIDEO = 0;

	private static int COMMOND_IMAGE = 1;

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

	public static String getVideoPath(String filepath) {
		int filePrefixIndex = filepath.lastIndexOf(".");
		String filePathAndName = filepath.substring(0, filePrefixIndex + 1);
		filePathAndName = filePathAndName + "flv";
		return filePathAndName;
	}

	public static String getImagePath(String filepath) {
		int filePrefixIndex = filepath.lastIndexOf(".");
		String filePathAndName = filepath.substring(0, filePrefixIndex + 1);
		filePathAndName = filePathAndName + "png";
		return filePathAndName;
	}

	public static void compressVideo(String filepath) {
		if (!checkFile(filepath)) {
			return;
		}
		process(filepath, COMMOND_VIDEO);
		process(filepath, COMMOND_IMAGE);
	}

	private static List<String> buildCompressCommond(String filepath) {
		List<String> command = Lists.newArrayList();
		String videoPath = getVideoPath(filepath);
		command.add(FFMEPG_PATH);
		command.add("-i");
		command.add(filepath);
		// 音频码率 32 64 96 128
		command.add("-ab");
		command.add("64");
		// 使用codec编解码
		command.add("-acodec");
		command.add("mp3");
		// -ac channels 设置通道,缺省为1
		command.add("-ac");
		command.add("2");
		// -ar freq 设置音频采样率
		command.add("-ar");
		command.add("22050");
		// -b bitrate 设置比特率,缺省200kb/s
		command.add("-b");
		command.add("250");
		// -r fps 设置帧频,缺省25
		command.add("-r");
		command.add("30");
		// -qscale 6或4 使用动态码率来设置
		command.add("-qscale");
		command.add("6");

		command.add("-y");
		command.add(videoPath);
		logger.info("视频转换==={}", videoPath);
		return command;
	}

	private static List<String> buildImageCommond(String filepath) {
		List<String> command = Lists.newArrayList();
		String imagePath = getImagePath(filepath);
		command.add(FFMEPG_PATH);
		command.add("-i");
		command.add(filepath);
		command.add("-y");
		command.add("-f");
		command.add("image2");
		command.add("-ss");
		command.add("30");
		command.add("-t");
		command.add("0.001");
		command.add("-s");
		command.add("350*240");
		command.add(imagePath);
		logger.info("视频截图==={}", imagePath);
		return command;
	}

	public static void process(String filepath, int commType) {
		List<String> command = Lists.newArrayList();
		if (COMMOND_IMAGE == commType) {
			command = buildImageCommond(filepath);
		} else {
			command = buildCompressCommond(filepath);
		}

		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(command);
			builder.redirectErrorStream(true);
			builder.start();
		} catch (Exception e) {
			logger.error("转换视频失败", e);
		}
	}

	public static void main(String[] args) {
		VideoKit.compressVideo("G:\\1.mp4");
	}
}
