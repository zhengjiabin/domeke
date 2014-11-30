/**
 * 
 */
package com.domeke.app.utils;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author lijiasen@domeke.com
 *
 */
public class VideoKit {

	private static Logger logger = LoggerFactory.getLogger(VideoKit.class);

	private static String FFMEPG_PATH;

	public static int COMMOND_VIDEO = 0;

	public static int COMMOND_IMAGE = 1;

	private static String toDirectory = "";

	static {
		FFMEPG_PATH = PropKit.getString("ffmepgPath");
	}

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

	public static String getVideoPath(File file) {
		String filepath = file.getAbsolutePath();
		int filePrefixIndex = filepath.lastIndexOf(".");
		String filePathAndName = "";
		if (toDirectory == null || toDirectory.length() == 0) {
			filePathAndName = filepath.substring(0, filePrefixIndex + 1);
		} else {
			File toCatalogueFile = new File(toDirectory);
			if (!toCatalogueFile.exists()) {
				toCatalogueFile.mkdirs();
			}
			int fileNameIndex = filepath.lastIndexOf("\\");
			filePathAndName = filepath.substring(fileNameIndex + 1, filePrefixIndex + 1);
		}
		filePathAndName = toDirectory + filePathAndName + "flv";
		return filePathAndName;
	}

	public static String getImagePath(File file) {
		String filepath = file.getAbsolutePath();
		int filePrefixIndex = filepath.lastIndexOf(".");
		String filePathAndName = "";
		if (toDirectory == null || toDirectory.length() == 0) {
			filePathAndName = filepath.substring(0, filePrefixIndex + 1);
		} else {
			File toCatalogueFile = new File(toDirectory);
			if (!toCatalogueFile.exists()) {
				toCatalogueFile.mkdir();
			}
			int fileNameIndex = filepath.lastIndexOf("\\");
			filePathAndName = filepath.substring(fileNameIndex + 1, filePrefixIndex + 1);
		}
		filePathAndName = toDirectory + filePathAndName + "png";
		return filePathAndName;
	}

	public static String compressVideo(File file, String toDirectory) {
		VideoKit.toDirectory = toDirectory;
		String targePath = process(file, COMMOND_VIDEO);
		process(file, COMMOND_IMAGE);
		return targePath;
	}

	private static Map<String, Object> buildCompressCommand(File file) {
		Map<String, Object> resultMap = Maps.newHashMap();
		List<String> command = Lists.newArrayList();
		String videoPath = getVideoPath(file);
		command.add(FFMEPG_PATH);
		command.add("-i");
		command.add(file.getAbsolutePath());
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
		resultMap.put("command", command);
		resultMap.put("targetPath", videoPath);
		return resultMap;
	}

	private static Map<String, Object> buildImageCommond(File file) {
		Map<String, Object> resultMap = Maps.newHashMap();
		List<String> command = Lists.newArrayList();
		String imagePath = getImagePath(file);
		command.add(FFMEPG_PATH);
		command.add("-i");
		command.add(file.getAbsolutePath());
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
		resultMap.put("command", command);
		resultMap.put("targetPath", imagePath);
		return resultMap;
	}

	@SuppressWarnings("unchecked")
	public static String process(File file, int commType) {
		Map<String, Object> resultMap = Maps.newHashMap();
		List<String> command = Lists.newArrayList();
		if (COMMOND_IMAGE == commType) {
			resultMap = buildImageCommond(file);
		} else {
			resultMap = buildCompressCommand(file);
		}
		command = (List<String>) resultMap.get("command");
		try {
			ProcessBuilder builder = new ProcessBuilder();
			logger.info("*********************视频压缩 start **************************");
			StringBuffer infoBuffer = new StringBuffer();
			for (Iterator iterator = command.iterator(); iterator.hasNext();) {
				String comm = (String) iterator.next();
				infoBuffer.append(comm + " ");
			}
			logger.info("视频压缩命令{}", infoBuffer.toString());
			builder.command(command);
			builder.redirectErrorStream(true);
			builder.start();
			logger.info("*********************视频压缩 end **************************");
		} catch (Exception e) {
			logger.error("转换视频失败", e);
		}
		return (String) resultMap.get("targetPath");
	}

	public static void main(String[] args) {
		VideoKit.compressVideo(new File("G:\\1.mp4"), "G:\\upload\\");
	}
}
