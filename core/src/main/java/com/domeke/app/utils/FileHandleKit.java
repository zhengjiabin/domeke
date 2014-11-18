/**
 * 
 */
package com.domeke.app.utils;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class FileHandleKit {

	private volatile static FileHandleKit fileHandleKit;

	/** 日志 */
	private Logger logger;
	
	private String ffmepgPath = "G:\\ffmepg\\bin\\ffmpeg.exe";

	private FileHandleKit() {

	}

	/**
	 * 单例模式初始化
	 * 
	 * @return
	 */
	public static FileHandleKit getInstance() {
		if (fileHandleKit == null) {
			synchronized (FileLoadKit.class) {
				if (fileHandleKit == null) {
					fileHandleKit = new FileHandleKit();
					fileHandleKit.setLogger(LoggerFactory.getLogger(FileHandleKit.class));
					
					String ffmepgPath = PropKit.getString("ffmepg_path");
					fileHandleKit.setFfmepgPath(ffmepgPath);
				}
			}
		}
		return fileHandleKit;
	}
	
	/**
	 * 压缩文件
	 * @param file
	 * @param toDirectory
	 * @return
	 */
	public static String compressFile(File file, String toDirectory) {
		FileHandleKit fileHandleKit = FileHandleKit.getInstance();
		String fileName = file.getName();
		String virtualDirectory = null;
		if(fileHandleKit.isImage(fileName)){
			fileHandleKit.compressImage(file, toDirectory);
		}else if(fileHandleKit.isVideo(fileName)){
			fileHandleKit.compressVideo(file, toDirectory);
		}
		return virtualDirectory;
	}
	
	/**
	 * 压缩图片
	 * @param file
	 * @param toDirectory
	 * @return
	 */
	private String  compressImage(File file, String toDirectory){
		return null;
	}
	
	/**
	 * 压缩视频
	 * @param file
	 * @param toDirectory
	 * @return
	 */
	private String compressVideo(File file, String toDirectory){
		List<String> command = Lists.newArrayList();
		Map<String, Object> resultMap = buildCompressCommand(file);
		command = (List<String>) resultMap.get("command");
		process(command);
		return (String) resultMap.get("targetPath");
	}
	
	/**
	 * 压缩信息
	 */
	private void process(List<String> command) {
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(command);
			builder.redirectErrorStream(true);
			builder.start();
		} catch (Exception e) {
			logger.error("转换视频失败", e);
		}
	}
	
	/**
	 * 构造压缩信息
	 * @param file
	 * @return
	 */
	private Map<String, Object> buildCompressCommand(File file) {
		Map<String, Object> resultMap = Maps.newHashMap();
		List<String> command = Lists.newArrayList();
//		String videoPath = getVideoPath(file);
//		command.add(FFMEPG_PATH);
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
//		command.add(videoPath);
//		logger.info("视频转换==={}", videoPath);
//		resultMap.put("command", command);
//		resultMap.put("targetPath", videoPath);
		return resultMap;
	}
	
	/**
	 * 文件是否视频
	 * 
	 * @param fileName
	 * @return
	 */
	private boolean isVideo(String fileName) {
		String fileType = fileName.substring(fileName.lastIndexOf("."),fileName.length());
		if (fileType.matches(".avi|.rm|.rmvb|.mpg|.mpe|.mpeg|.dat|.asf|.wmv|.mov|.3gp|.flv")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 文件是否图片
	 * 
	 * @param fileName
	 * @return
	 */
	private boolean isImage(String fileName) {
		String fileType = fileName.substring(fileName.lastIndexOf("."),fileName.length());
		if (fileType.matches(".jpg|.gif|.png|.bmp")) {
			return true;
		}
		return false;
	}

	private void setLogger(Logger logger) {
		this.logger = logger;
	}

	private String getFfmepgPath() {
		return ffmepgPath;
	}

	private void setFfmepgPath(String ffmepgPath) {
		this.ffmepgPath = ffmepgPath;
	}
}
