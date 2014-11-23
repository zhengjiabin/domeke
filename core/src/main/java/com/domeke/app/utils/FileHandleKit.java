/**
 * 
 */
package com.domeke.app.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.jfinal.core.JFinal;
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
	 * @return (fileName,directory)
	 */
	public static Map<String,String> compressFile(File file, String toDirectory) {
		FileHandleKit fileHandleKit = FileHandleKit.getInstance();
		String fileName = file.getName();
		Map<String, String> virtualDirectory = null;
		if(FileKit.isImage(fileName)){
			virtualDirectory = fileHandleKit.compressImage(file, toDirectory);
		}else if(FileKit.isVideo(fileName)){
			virtualDirectory = fileHandleKit.compressVideo(file, toDirectory);
		}
		return virtualDirectory;
	}
	
	/**
	 * 压缩图片
	 * @param file
	 * @param toDirectory
	 * @return (fileName,directory)
	 */
	private Map<String, String> compressImage(File file, String toDirectory){
		Map<String,String> directorys = new HashMap<String,String>();
		String imageDirectory = processPNG(file, toDirectory);
		String fileName = getFileName(imageDirectory);
		directorys.put(fileName, imageDirectory);
		return directorys;
	}
	
	/**
	 * 压缩视频
	 * @param file
	 * @param toDirectory
	 * @return
	 */
	private Map<String, String> compressVideo(File file, String toDirectory){
		Map<String, String> directorys = new HashMap<String, String>();
		String fileName = file.getName();
		String directory = null;
		boolean isCompressVideo = isCompressVideo(fileName);
		if(isCompressVideo){
			directory = buildVideoCompressCommand(file, toDirectory);
			fileName = getFileName(directory);
		}else{
			File newFile = fileCopy(file, toDirectory);
			fileName = newFile.getName();
			directory = newFile.getAbsolutePath();
		}
		directorys.put(fileName, directory);
		
		directory = processPNG(file, toDirectory);
		fileName = getFileName(directory);
		directorys.put(fileName, directory);
		return directorys;
	}
	
	/**
	 * 压缩信息
	 */
	private void process(List<String> command,boolean isWait) {
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(command);
			builder.redirectErrorStream(true);
			Process p = builder.start();
			
			if(isWait){
				doWait(p);
			}
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
		String directory = null;
		handleFilePath(toDirectory);
		String fileName = file.getName();
		String descDirectory = file.getAbsolutePath();
		boolean isDirectoryCompressVideo = isDirectCompressVideo(fileName);
		if(isDirectoryCompressVideo){
			directory = processFLV(descDirectory, toDirectory);
		}else{
			descDirectory = processAVI(descDirectory, toDirectory);
			directory = processFLV(descDirectory, toDirectory);
		}
		return directory;
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
		process(command,false);
		return imagePath;
	}
	
	/**
	 * 将视频压缩成.FLV格式
	 * @return
	 */
	private String processFLV(String descDirectory, String toDirectory){
		String fileName = getFileName(descDirectory);
		String ffmepgPath = getFfmepgPath();
		String videoPath = getFileTypeDirectory(toDirectory, fileName, ".flv");
		List<String> command = Lists.newArrayList();
		command.add(ffmepgPath);
		command.add("-i");
		command.add(descDirectory);
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
		process(command,false);
		return videoPath;
	}
	
	/**
	 * 将视频压缩成.AVI格式
	 * @param type
	 * @return 物理路径
	 */
	private String processAVI(String descDirectory, String toDirectory) {
		handleFilePath(toDirectory);
		String fileName = getFileName(descDirectory);
		String mencoderPath = getMencoderPath();
		String videoPath = getFileTypeDirectory(toDirectory, fileName, ".avi");
		List<String> command = Lists.newArrayList();
		command.add(mencoderPath);
		command.add(descDirectory);
		command.add("-o");
		command.add(videoPath);
		command.add("-of");
		command.add("avi");
		
		command.add("-oac");
		command.add("mp3lame");
		command.add("-lameopts");
		command.add("abr:br=56");
		command.add("-ovc");
		command.add("xvid");
		command.add("-xvidencopts");
		command.add("bitrate=600");

		logger.info("视频转换==={}", videoPath);
		process(command,true);
		return videoPath;
    }
	
	/**
	 * 等待当前视频处理完成
	 * @param p
	 * @throws InterruptedException
	 */
	private void doWait(Process p) throws InterruptedException {
		final InputStream is1 = p.getInputStream();
		final InputStream is2 = p.getErrorStream();
		new Thread() {
			public void run() {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						is1));
				try {
					String lineB = br.readLine();
					while (lineB != null) {
						lineB = br.readLine();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
		new Thread() {
			public void run() {
				BufferedReader br2 = new BufferedReader(new InputStreamReader(
						is2));
				try {
					String lineC = br2.readLine();
					while (lineC != null) {
						lineC = br2.readLine();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
		p.waitFor();
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
		boolean isDevMode = isDevMode();
		if(isDevMode){
			return file;
		}
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
	 * 是否开发模式
	 * @return
	 */
	private boolean isDevMode(){
		return JFinal.me().getConstants().getDevMode();
	}
	
	/**
	 * 是否处理视频
	 * @return
	 */
	private boolean isCompressVideo(String fileName){
		String fileType = fileName.substring(fileName.lastIndexOf("."),fileName.length());
		if (fileType.matches(".flv|.mp4")) {
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
		if(fileType.matches(".asx|.asf|.mpg|.wmv|.3gp|.mp4|.mov|.avi|.flv")){
			return true;
		}
		return false;
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
	 * 获取文件名
	 * @param descDirectory 物理路径
	 * @return 文件名
	 */
	private String getFileName(String descDirectory){
		if(StrKit.isBlank(descDirectory)){
			return null;
		}
		descDirectory = descDirectory.replaceAll("\\\\", "/");
		return descDirectory.substring(descDirectory.lastIndexOf("/") + 1, descDirectory.length());
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
