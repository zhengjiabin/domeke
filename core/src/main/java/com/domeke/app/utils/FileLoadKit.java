package com.domeke.app.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.domeke.app.cos.multipart.FilePart;
import com.domeke.app.model.User;
import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;
import com.jfinal.kit.FileKit;
import com.jfinal.kit.StrKit;
import com.jfinal.upload.UploadFile;

public class FileLoadKit {

	private volatile static FileLoadKit fileUploadKit;

	/** 日志 */
	private Logger logger;

	/** 文件上传的本地根目录 */
	private String baseDirectory;
	
	/** 文件上传的本地备份根目录 */
	private String backupsDirectory;

	/** 图片上传的临时目录 */
	private String imageDirectory;

	/** 视频上传的临时目录 */
	private String videoDirectory;

	/** 文件上传的临时目录 */
	private String fileDirectory;

	private FileLoadKit() {

	}

	/**
	 * 单例模式初始化
	 * 
	 * @return
	 */
	public static FileLoadKit getInstance() {
		if (fileUploadKit == null) {
			synchronized (FileLoadKit.class) {
				if (fileUploadKit == null) {
					fileUploadKit = new FileLoadKit();
					fileUploadKit.setLogger(LoggerFactory.getLogger(FileLoadKit.class));
					
					String baseDirectory = PropKit.getString("basePath");
					fileUploadKit.setBaseDirectory(baseDirectory);
					
					String backupsDirectory = PropKit.getString("backupsPath");
					fileUploadKit.setBackupsDirectory(backupsDirectory);

					String imageDirectory = PropKit.getString("imagePath");
					fileUploadKit.setImageDirectory(imageDirectory);

					String videoDirectory = PropKit.getString("videoPath");
					fileUploadKit.setVideoDirectory(videoDirectory);

					String fileDirectory = PropKit.getString("filePath");
					fileUploadKit.setFileDirectory(fileDirectory);
				}
			}
		}
		return fileUploadKit;
	}

	/**
	 * 图片上传
	 * 
	 * @param ctrl
	 * @param parameterName
	 * @param saveFolderName
	 * @param maxPostSize
	 * @param encoding
	 * @return
	 */
	public static String uploadImg(Controller ctrl, String parameterName, String saveFolderName, Integer maxPostSize, String encoding) {
		Map<String, String> imgDirectory = uploadImgs(ctrl, saveFolderName, maxPostSize, encoding);
		if(MapKit.isBlank(imgDirectory)){
			return null;
		}
		return imgDirectory.get(parameterName);
	}
	
	/**
	 * 一次上传多张图片
	 * （若parameterName对应多张图片，则返回父级路径）
	 * @param ctrl
	 * @param saveFolderName
	 * @param maxPostSize
	 * @param encoding
	 * @return (parameterName,directory)
	 */
	public static Map<String, String> uploadImgs(Controller ctrl, String saveFolderName, Integer maxPostSize, String encoding){
		Map<String, Collection<String>> imgDirectory = uploadImgsAll(ctrl, saveFolderName, maxPostSize, encoding);
		if(MapKit.isBlank(imgDirectory)){
			return null;
		}
		Map<String, String> imgPath = new HashMap<String, String>();
		Collection<String> directorys = null;
		String virtualDirectory = null;
		for(String key : imgDirectory.keySet()){
			directorys = imgDirectory.get(key);
			virtualDirectory = fileUploadKit.getVirtualDirectory(directorys);
			imgPath.put(key, virtualDirectory);
		}
		return imgPath;
		
	}
	
	/**
	 * 一次上传多张图片
	 * 
	 * @return (parameterName,directory)
	 */
	public static Map<String, Collection<String>> uploadImgsAll(Controller ctrl, String saveFolderName, Integer maxPostSize, String encoding){
		FileLoadKit fileUploadKit = FileLoadKit.getInstance();
		fileUploadKit.initProgress(ctrl);
		String imgDirectory = fileUploadKit.getImageDirectory();
		String serverDirectory = fileUploadKit.getServerDirectory(imgDirectory, saveFolderName, ctrl);
		String tempDirectory = fileUploadKit.getTempDirectory(serverDirectory);
		String discDirectory = fileUploadKit.getDiscDirectory(serverDirectory);
		
		List<UploadFile> uploadFiles = ctrl.getFiles(tempDirectory, maxPostSize, encoding);
		if(CollectionKit.isBlank(uploadFiles)){
			return null;
		}
		Map<String,Collection<String>> fileDirectory = new HashMap<String, Collection<String>>();
		Collection<String> filePaths = null;
		File file = null;
		String virtualPath = null,parameterName = null;
		for(UploadFile uploadFile : uploadFiles){
			file = uploadFile.getFile();
			boolean isImage = fileUploadKit.isImage(uploadFile.getFileName().toLowerCase());
			if (!isImage) {
				FileKit.delete(file);
				continue;
			}
			virtualPath = fileUploadKit.handleImg(file, discDirectory, serverDirectory, ctrl);
			parameterName = uploadFile.getParameterName();
			
			filePaths = fileDirectory.get(parameterName);
			if(filePaths == null){
				filePaths = new ArrayList<String>();
				fileDirectory.put(parameterName, filePaths);
			}
			filePaths.add(virtualPath);
		}
		return fileDirectory;
	}
	
	/**
	 * 视频上传
	 * @return
	 */
	public static String uploadVideo(Controller ctrl, String parameterName, String saveFolderName, Integer maxPostSize, String encoding){
		Map<String, String> videoDirectory = uploadVideos(ctrl, saveFolderName, maxPostSize, encoding);
		if(MapKit.isBlank(videoDirectory)){
			return null;
		}
		return videoDirectory.get(parameterName);
	}
	
	/**
	 * 一次上传多个视频
	 * （若parameterName对应多个视频，则返回父级路径）
	 * @param ctrl
	 * @param saveFolderName
	 * @param maxPostSize
	 * @param encoding
	 * @return
	 */
	public static Map<String, String> uploadVideos(Controller ctrl, String saveFolderName, Integer maxPostSize, String encoding){
		Map<String, Collection<String>> videoDirectory = uploadVideosAll(ctrl, saveFolderName, maxPostSize, encoding);
		if(MapKit.isBlank(videoDirectory)){
			return null;
		}
		Map<String, String> videoPath = new HashMap<String, String>();
		Collection<String> directorys = null;
		String virtualDirectory = null;
		for(String key : videoDirectory.keySet()){
			directorys = videoDirectory.get(key);
			virtualDirectory = fileUploadKit.getVirtualDirectory(directorys);
			videoPath.put(key, virtualDirectory);
		}
		return videoPath;
	}
	
	/**
	 * 一次上传多个视频
	 * @return (parameterName,directory)
	 */
	public static Map<String, Collection<String>> uploadVideosAll(Controller ctrl, String saveFolderName, Integer maxPostSize, String encoding){
		FileLoadKit fileUploadKit = FileLoadKit.getInstance();
		fileUploadKit.initProgress(ctrl);
		String videoDirectory = fileUploadKit.getVideoDirectory();
		String serverDirectory = fileUploadKit.getServerDirectory(videoDirectory, saveFolderName, ctrl);
		String tempDirectory = fileUploadKit.getTempDirectory(serverDirectory);
		String discDirectory = fileUploadKit.getDiscDirectory(serverDirectory);
		
		List<UploadFile> uploadFiles = ctrl.getFiles(tempDirectory, maxPostSize, encoding);
		if(CollectionKit.isBlank(uploadFiles)){
			return null;
		}
		
		Map<String,Collection<String>> fileDirectory = new HashMap<String, Collection<String>>();
		Collection<String> filePaths = null;
		File file = null;
		String virtualPath = null,parameterName = null;
		for(UploadFile uploadFile : uploadFiles){
			file = uploadFile.getFile();
			boolean isVideo = fileUploadKit.isVideo(uploadFile.getFileName().toLowerCase());
			if (!isVideo) {
				FileKit.delete(file);
				continue;
			}
			virtualPath = fileUploadKit.handleVideo(file, discDirectory);
			parameterName = uploadFile.getParameterName();
			
			filePaths = fileDirectory.get(parameterName);
			if(filePaths == null){
				filePaths = new ArrayList<String>();
				fileDirectory.put(parameterName, filePaths);
			}
			filePaths.add(virtualPath);
		}
		return fileDirectory;
	}
	
	/**
	 * 文件上传
	 * @return
	 */
	public static String uploadFile(Controller ctrl, String parameterName, String saveFolderName, Integer maxPostSize, String encoding){
		Map<String, String> fileDirectory = uploadFiles(ctrl, saveFolderName, maxPostSize, encoding);
		if(MapKit.isBlank(fileDirectory)){
			return null;
		}
		return fileDirectory.get(parameterName);
	}
	
	/**
	 * 一次上传多个文件（多个文件类型的同时上传）
	 * （若parameterName对应多个文件，则返回父级路径）
	 * @param ctrl
	 * @param saveFolderName
	 * @param maxPostSize
	 * @param encoding
	 * @return
	 */
	public static Map<String, String> uploadFiles(Controller ctrl, String saveFolderName, Integer maxPostSize, String encoding){
		Map<String, Collection<String>> fileDirectory = uploadFilesAll(ctrl, saveFolderName, maxPostSize, encoding);
		if(MapKit.isBlank(fileDirectory)){
			return null;
		}
		Map<String, String> filePath = new HashMap<String, String>();
		Collection<String> directorys = null;
		String virtualDirectory = null;
		for(String key : fileDirectory.keySet()){
			directorys = fileDirectory.get(key);
			virtualDirectory = fileUploadKit.getVirtualDirectory(directorys);
			filePath.put(key, virtualDirectory);
		}
		return filePath;
	}
	
	/**
	 * 一次上传多个文件（多个文件类型的同时上传）
	 * @return (parameterName,directory)
	 */
	public static Map<String, Collection<String>> uploadFilesAll(Controller ctrl, String saveFolderName, Integer maxPostSize, String encoding){
		FileLoadKit fileUploadKit = FileLoadKit.getInstance();
		fileUploadKit.initProgress(ctrl);
		String fileDirectory = fileUploadKit.getFileDirectory();
		String serverDirectory = fileUploadKit.getServerDirectory(fileDirectory, saveFolderName, ctrl);
		String tempDirectory = fileUploadKit.getTempDirectory(serverDirectory);
		
		List<UploadFile> uploadFiles = ctrl.getFiles(tempDirectory, maxPostSize, encoding);
		if(CollectionKit.isBlank(uploadFiles)){
			return null;
		}
		
		Map<String,Collection<String>> directorys = new HashMap<String, Collection<String>>();
		Collection<String> filePaths = null;
		File file = null;
		String virtualPath = null, parameterName = null;
		for(UploadFile uploadFile : uploadFiles){
			file = uploadFile.getFile();
			virtualPath = fileUploadKit.handleFile(file, saveFolderName, ctrl);
			parameterName = uploadFile.getParameterName();
			
			filePaths = directorys.get(parameterName);
			if(filePaths == null){
				filePaths = new ArrayList<String>();
				directorys.put(parameterName, filePaths);
			}
			filePaths.add(virtualPath);
		}
		return directorys;
	}
	
	/**
	 * 处理临时图片
	 * @return
	 */
	private String handleImg(File tempFile, String discDirectory, String serverDirectory, Controller ctrl){
		fileCopy(discDirectory ,tempFile);
		String fileName = tempFile.getName();
		String virtualPath = fileUploadKit.getVirtualDirectory(serverDirectory, fileName, ctrl);
		return virtualPath;
	}
	
	/**
	 * 处理临时视频
	 * @return
	 */
	private String handleVideo(File tempFile, String discDirectory){
		String virtualPath = VideoKit.compressVideo(tempFile, discDirectory);
		return virtualPath;
	}
	
	/**
	 * 处理（非图片、视频）文件
	 * @return
	 */
	private String handleFile(File tempFile, String discDirectory, String serverDirectory, Controller ctrl){
		fileUploadKit.fileCopy(discDirectory ,tempFile);
		String fileName = tempFile.getName();
		String virtualPath = fileUploadKit.getVirtualDirectory(serverDirectory, fileName, ctrl);
		return virtualPath;
	}
	
	/**
	 * 处理临时文件
	 */
	private String handleFile(File tempFile, String saveFolderName, Controller ctrl){
		String virtualPath = null;
		String fileName = tempFile.getName();
		String serverDirectory = fileUploadKit.getServerDirectoryAuto(fileName, saveFolderName, ctrl);
		String discDirectory = fileUploadKit.getDiscDirectory(serverDirectory);
		if(isImage(fileName)){
			virtualPath = handleImg(tempFile, discDirectory, serverDirectory, ctrl);
		}else if(isVideo(fileName)){
			virtualPath = handleVideo(tempFile, discDirectory);
		}else{
			virtualPath = handleFile(tempFile, discDirectory, serverDirectory, ctrl);
		}
		return virtualPath;
	}
	
	/**
	 * 获取虚拟上传的路径--(若存在多张，则返回父级路径)
	 * 
	 * @return
	 */
	private String getVirtualDirectory(Collection<String> directorys) {
		if (CollectionKit.isBlank(directorys)) {
			return null;
		}
		Iterator<String> iterator = directorys.iterator();
		String directory = iterator.next();
		if (directorys.size() == 1) {
			return directory;
		}
		int separator = directory.lastIndexOf("/");
		return directory.substring(0, separator);
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
	 * 获取临时上传路径
	 */
	private String getTempDirectory(String directory){
		//开发模式下，本地服务器作为临时根目录
		if(!isDevMode()){
			String baseDirectory = getBackupsDirectory();
			directory = getDirectory(baseDirectory, directory);
		}
		return directory;
	}
	
	/**
	 * 获取物理上传路径
	 */
	private String getDiscDirectory(String directory){
		String baseDirectory = getBaseDirectory();
		directory = getDirectory(baseDirectory, directory);
		return directory;
	}
	
	/**
	 * 动态获取服务器存储路径
	 */
	private String getServerDirectoryAuto(String fileName, String saveFolderName, Controller ctrl){
		String directory = null;
		if(isImage(fileName)){
			directory = getImageDirectory();
		}else if(isVideo(fileName)){
			directory = getVideoDirectory();
		}else{
			directory = getFileDirectory();
		}
		String serverDirectory = fileUploadKit.getServerDirectory(directory, saveFolderName, ctrl);
		return serverDirectory;
	}
	
	/**
	 * 获取服务器存储路径
	 * 
	 * @return
	 */
	private String getServerDirectory(String directory, String saveFolderName, Controller ctrl) {
		User user = ctrl.getSessionAttr("user");
		String userName = user.getStr("username");
		directory = directory.replaceAll("<username>", userName);
		directory = getDirectory(directory, saveFolderName);
		return directory;
	}

	/**
	 * 拼接路径路径
	 * 
	 * @return
	 */
	private String getDirectory(String directory, String saveFolderName) {
		directory = directory.replaceAll("\\\\", "/");
		if(StrKit.isBlank(saveFolderName)){
			return directory;
		}
		saveFolderName = saveFolderName.replaceAll("\\\\", "/");
		if(directory.endsWith("/") && saveFolderName.startsWith("/")){
			saveFolderName = saveFolderName.replaceFirst("/", "");
		}else if(!directory.endsWith("/") && !saveFolderName.startsWith("/")){
			saveFolderName = "/" + saveFolderName;
		}
		return directory + saveFolderName;
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
	 * 文件拷贝
	 * @param directory
	 * @param file
	 */
	private File fileCopy(String directory,File file){
		File fileDirectory = new File(directory);
		if (!fileDirectory.exists()) {
			fileDirectory.mkdirs();
		}
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
	 * 上传文件是否图片
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
	 * 上传文件是否视频
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
	 * 初始化进度条
	 */
	private void initProgress(Controller ctrl) {
		FilePart.progresss.set(new FilePart.Progress() {
			public void progress(long currentSize) {
				int totalsize = ctrl.getRequest().getContentLength();
				double csize = (double) currentSize;
				double s = csize / totalsize * 100;
				logger.info("文件上传百分之{}", String.format("%.2f", s));
			}

			public void start() {
				logger.info("文件上传开始~");
			}

			public void end() {
				logger.info("文件上传结束~");
				FilePart.progresss.remove();
			}
		});
	}
	
	/**
	 * 是否开发模式
	 * @return
	 */
	private boolean isDevMode(){
		return JFinal.me().getConstants().getDevMode();
	}

	private void setLogger(Logger logger) {
		this.logger = logger;
	}

	private String getImageDirectory() {
		return imageDirectory;
	}

	private void setImageDirectory(String imageDirectory) {
		this.imageDirectory = imageDirectory;
	}

	private String getVideoDirectory() {
		return videoDirectory;
	}

	private void setVideoDirectory(String videoDirectory) {
		this.videoDirectory = videoDirectory;
	}

	private String getBaseDirectory() {
		return baseDirectory;
	}

	private void setBaseDirectory(String baseDirectory) {
		this.baseDirectory = baseDirectory;
	}

	private String getFileDirectory() {
		return fileDirectory;
	}

	private void setFileDirectory(String fileDirectory) {
		this.fileDirectory = fileDirectory;
	}

	private String getBackupsDirectory() {
		return backupsDirectory;
	}

	private void setBackupsDirectory(String backupsDirectory) {
		this.backupsDirectory = backupsDirectory;
	}
	
	
}
