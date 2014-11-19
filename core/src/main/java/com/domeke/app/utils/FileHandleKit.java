/**
 * 
 */
package com.domeke.app.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

public class FileHandleKit {

	private volatile static FileHandleKit fileHandleKit;

	/** 日志 */
	private Logger logger;
	
	/** ffmepg视频压缩工具 */
	private String ffmepgPath;
	
	/** mencoder视频压缩工具 */
	private String mencoderPath;

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
					
					String ffmepgPath = PropKit.getString("ffmepgPath");
					fileHandleKit.setFfmepgPath(ffmepgPath);
					
					String mencoderPath = PropKit.getString("mencoderPath");
					fileHandleKit.setMencoderPath(mencoderPath);
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
			virtualDirectory = fileHandleKit.compressImage(file, toDirectory);
		}else if(fileHandleKit.isVideo(fileName)){
			virtualDirectory = fileHandleKit.compressVideo(file, toDirectory);
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
		String virtualDirectory = buildImageCompressCommond(file, toDirectory);
		return virtualDirectory;
	}
	
	/**
	 * 压缩视频
	 * @param file
	 * @param toDirectory
	 * @return
	 */
	private String compressVideo(File file, String toDirectory){
		String virtualDirectory = null;
		String fileName = file.getName();
		boolean isCompressVideo = isCompressVideo(fileName);
		if(isCompressVideo){
			virtualDirectory = buildVideoCompressCommand(file, toDirectory);
		}else{
			fileCopy(file, toDirectory);
			virtualDirectory = getFileTypeDirectory(toDirectory, fileName, null);
		}
		return virtualDirectory;
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
	 * 构造视频压缩信息
	 * @param file
	 * @return
	 */
	private String buildVideoCompressCommand(File file, String toDirectory) {
		handleFilePath(toDirectory);
		String fileName = file.getName();
		boolean isDirectoryCompressVideo = isDirectCompressVideo(fileName);
		if(isDirectoryCompressVideo){
			processFLV(file, toDirectory);
		}else{
			String directory = processAVI(file, toDirectory);
			processFLV(file, directory);
		}
		String virtualDirectory = processPNG(file, toDirectory);
		return virtualDirectory;
	}
	
	/**
	 * 构造图片压缩信息
	 * @param file
	 * @return
	 */
	private String buildImageCompressCommond(File file, String toDirectory) {
		return processPNG(file, toDirectory);
	}
	
	/**
	 * 将图片压缩成.PNG格式
	 * @param file
	 * @param toDirectory
	 * @return
	 */
	private String processPNG(File file, String toDirectory){
		String fileName = file.getName();
		String ffmepgPath = getFfmepgPath();
		String imagePath = getFileTypeDirectory(toDirectory, fileName, ".png");
		List<String> command = Lists.newArrayList();
		command.add(ffmepgPath);
		command.add("-ss");
		command.add("3");
		command.add("-i");
		command.add(file.getAbsolutePath());
		command.add(imagePath);
		command.add("-r");
		command.add("1");
		command.add("-vframes");
		command.add("1");
		command.add("-an");
		command.add("-vcodec");
		command.add("mjpeg");
		logger.info("视频截图==={}", imagePath);
		process(command);
		return imagePath;
	}
	
	/**
	 * 将视频压缩成.FLV格式
	 * @return
	 */
	private String processFLV(File file, String toDirectory){
		String fileName = file.getName();
		String ffmepgPath = getFfmepgPath();
		String videoPath = getFileTypeDirectory(toDirectory, fileName, ".flv");
		List<String> command = Lists.newArrayList();
		command.add(ffmepgPath);
		command.add("-i");
		command.add(file.getAbsolutePath());
		// 音频码率 32 64 96 128
		command.add("-ab");
		command.add("64");
		// 设置声道数,缺省为1
		command.add("-ac");
		command.add("2");
		// 设置音频采样率
		command.add("-ar");
		command.add("22050");
		// -b bitrate 设置比特率,缺省200kb/s
		command.add("-b");
		command.add("250");
		// 设置帧频,缺省25
		command.add("-r");
		command.add("30");
		// 指定转换的质量 6 4
		command.add("-qscale");
		command.add("6");
		// 指定将覆盖已存在的文件
		command.add("-y");
		command.add(videoPath);
		
		logger.info("视频转换==={}", videoPath);
		process(command);
		return videoPath;
	}
	
	/**
	 * 将视频压缩成.AVI格式
	 * @param type
	 * @return
	 */
	private String processAVI(File file, String toDirectory) {
		handleFilePath(toDirectory);
		String fileName = file.getName();
		String mencoderPath = getMencoderPath();
		String videoPath = getFileTypeDirectory(toDirectory, fileName, ".avi");
		List<String> command = Lists.newArrayList();
		command.add(mencoderPath);
		command.add(file.getAbsolutePath());
		// 设置音频编码器
		command.add("-oac");
		command.add("lavc");
		// 设置音频编码器的码率等参数
		command.add("-lavcopts");
		command.add("acodec=mp3:abitrate=64");
		// 设置视频编码器
		command.add("-ovc");
		command.add("xvid");
		// 设置视频编码器的码率等参数
		command.add("-xvidencopts");
		command.add("bitrate=600");
		command.add("-of");
		command.add("avi");
		// 输出视频全路径
		command.add("-o");
		command.add(videoPath);

		logger.info("视频转换==={}", videoPath);
		process(command);
		return videoPath;
    }
	
	/**
	 * 处理不存在的文件路径
	 */
	private void handleFilePath(String directory){
		File fileDirectory = new File(directory);
		if (!fileDirectory.exists()) {
			fileDirectory.mkdirs();
		}
	}
	
	/**
	 * 文件拷贝
	 * @param directory
	 * @param file
	 */
	private File fileCopy(File file, String directory){
		handleFilePath(directory);
		File newFile = new File(directory, file.getName());
		fileCopyByChannel(file, newFile);
		return newFile;
	}
	
	/**
	 * 文件复制
	 * 
	 * @param srcFile
	 *            源文件
	 * @param tagFile
	 *            目标文件
	 */
	private void fileCopyByChannel(File srcFile, File tagFile) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(srcFile);
			fo = new FileOutputStream(tagFile);
			// 得到对应的文件通道
			in = fi.getChannel();
			// 得到对应的文件通道
			out = fo.getChannel();
			// 连接两个通道，并且从in通道读取，然后写入out通道
			in.transferTo(0, in.size(), out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 是否处理视频
	 * @return
	 */
	private boolean isCompressVideo(String fileName){
		String fileType = fileName.substring(fileName.lastIndexOf("."),fileName.length());
		if (fileType.matches(".flv")) {
			return false;
		}
		return true;
	}
	
	/**
	 * 是否直接压缩视频
	 * @param fileName
	 * @return
	 */
	private boolean isDirectCompressVideo(String fileName){
		String fileType = fileName.substring(fileName.lastIndexOf("."),fileName.length());
		//支持解析的文件类型
		if(fileType.matches(".asx|.asf|.mpg|.wmv|.3pg|.mp4|.mov|.avi|.flv")){
			return true;
		}
		return false;
	}
	
	/**
	 * 文件是否视频
	 * 
	 * @param fileName
	 * @return
	 */
	private boolean isVideo(String fileName) {
		String fileType = fileName.substring(fileName.lastIndexOf("."),fileName.length());
		if (fileType.matches(".avi|.rm|.rmvb|.mpg|.mpe|.mpeg|.dat|.asf|.wmv|.mov|.3gp|.flv|.mp4")) {
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
	
	/**
	 * 获取浏览地址根路径
	 * @param request
	 * @return
	 */
	private String getBasePath(HttpServletRequest request){
		StringBuffer url = request.getRequestURL();
		String uri = request.getRequestURI();
		String basePath = StrKit.replace(url.toString(), uri, "");
		basePath = basePath + request.getContextPath();
		return basePath;
	}
	
	/**
	 * 获取虚拟上传路径
	 */
	private String getVirtualDirectory(String directory, String fileName, Controller ctrl){
		String basePath = getBasePath(ctrl.getRequest());
		directory = getDirectory(basePath, directory);
		directory = getDirectory(directory, fileName);
		return directory;
	}
	
	/**
	 * 获取指定文件类型的文件路径
	 * @param directory
	 * @param fileName
	 * @param fileType
	 * @return
	 */
	private String getFileTypeDirectory(String originalDirectory, String fileName, String fileType){
		String directory = null;
		if(StrKit.isBlank(fileType)){
			directory = getDirectory(originalDirectory, fileName);
			return directory;
		}
		String fileNamePrefix = fileName.substring(0, fileName.lastIndexOf("."));
		directory = getDirectory(originalDirectory, fileNamePrefix);
		return directory + fileType;
	}

	/**
	 * 拼接路径路径
	 * 
	 * @return
	 */
	private String getDirectory(String directory, String fileName) {
		directory = directory.replaceAll("\\\\", "/");
		if(StrKit.isBlank(fileName)){
			return directory;
		}
		fileName = fileName.replaceAll("\\\\", "/");
		if(directory.endsWith("/") && fileName.startsWith("/")){
			fileName = fileName.replaceFirst("/", "");
		}else if(!directory.endsWith("/") && !fileName.startsWith("/")){
			fileName = "/" + fileName;
		}
		return directory + fileName;
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

	private String getMencoderPath() {
		return mencoderPath;
	}

	private void setMencoderPath(String mencoderPath) {
		this.mencoderPath = mencoderPath;
	}
	
}
