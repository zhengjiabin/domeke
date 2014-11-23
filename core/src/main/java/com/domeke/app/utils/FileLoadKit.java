package com.domeke.app.utils;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.InputFormatException;
import it.sauronsoftware.jave.MultimediaInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.domeke.app.cos.multipart.FilePart;
import com.domeke.app.model.User;
import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PathKit;
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
	 * 获取视频文件播放时长
	 * @param 虚拟路径
	 * @return 时长（单位：秒）
	 * @throws EncoderException 
	 * @throws InputFormatException 
	 */
	public static Long getVideoFileTime(Controller ctrl, String virtualDirectory) throws InputFormatException, EncoderException{
		String descDirectory = getFileDescDirectory(ctrl, virtualDirectory);
		if(StrKit.isBlank(descDirectory)){
			return null;
		}
        File source = new File(descDirectory);
        Encoder encoder = new Encoder();
        MultimediaInfo multimediaInfo = encoder.getInfo(source);
        long ls = multimediaInfo.getDuration();
        return ls/1000;
	}
	
	/**
	 * 根据虚拟路径删除物理路径对应的文件
	 */
	public static void deleteFiles(Controller ctrl, String parentDirectory){
		FileLoadKit fileUploadKit = FileLoadKit.getInstance();
		String absoluteDirectory = fileUploadKit.getAbsolutePath(ctrl, parentDirectory);
		File file = new File(absoluteDirectory);
		FileKit.delete(file);
	}
	
	/**
	 * 根据虚拟路径获取子文件的物理路径
	 * @param ctrl
	 * @param parentDirectory
	 * @return 
	 */
	public static String getFileDescDirectory(Controller ctrl, String parentDirectory){
		Map<String, String> paths = getFilesDescDirectory(ctrl, parentDirectory);
		if(MapKit.isBlank(paths)){
			return null;
		}
		return paths.get(parentDirectory);
	}
	
	/**
	 * 根据虚拟路径获取子文件的物理路径
	 * @param ctrl
	 * @param parentDirectory 虚拟路径
	 * @return 子文件的物理路径
	 */
	public static Map<String, String> getFilesDescDirectory(Controller ctrl, String parentDirectory){
		FileLoadKit fileUploadKit = FileLoadKit.getInstance();
		String absoluteDirectory = fileUploadKit.getAbsolutePath(ctrl, parentDirectory);
		Map<String, String> paths = fileUploadKit.getFilesDescDirectory(absoluteDirectory, parentDirectory);
		return paths;
	}
	
	
	/**
	 * 根据虚拟路径获取子文件的虚拟路径
	 * @param ctrl
	 * @param parentDirectory 父级虚拟路径
	 * @return
	 */
	public static String getFileVirtualDirectory(Controller ctrl, String parentDirectory){
		List<String> paths = getFilesVirtualDirectory(ctrl, parentDirectory);
		if(CollectionKit.isBlank(paths)){
			return null;
		}
		return paths.get(0);
	}
	
	/**
	 * 根据虚拟路径获取子文件的虚拟路径
	 * @param ctrl
	 * @param parentDirectory 父级虚拟路径
	 * @return
	 */
	public static List<String> getFilesVirtualDirectory(Controller ctrl, String parentDirectory){
		FileLoadKit fileUploadKit = FileLoadKit.getInstance();
		String absoluteDirectory = fileUploadKit.getAbsolutePath(ctrl, parentDirectory);
		List<String> paths = fileUploadKit.getFilesVirtualDirectory(absoluteDirectory, parentDirectory);
		return paths;
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
		Map<String, Map<String, String>> imgDirectory = uploadImgsAll(ctrl, saveFolderName, maxPostSize, encoding);
		if(MapKit.isBlank(imgDirectory)){
			return null;
		}
		Map<String, String> imgPath = new HashMap<String, String>();
		Map<String,String> directorys = null;
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
	 * @return (parameterName,fileName-directory)
	 */
	public static Map<String, Map<String,String>> uploadImgsAll(Controller ctrl, String saveFolderName, Integer maxPostSize, String encoding){
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
		Map<String,Map<String,String>> fileDirectory = new HashMap<String, Map<String,String>>();
		Map<String,String> filePaths = null,virtualPaths = null;
		File file = null;
		String parameterName = null;
		for(UploadFile uploadFile : uploadFiles){
			file = uploadFile.getFile();
			boolean isImage = FileKit.isImage(uploadFile.getFileName().toLowerCase());
			if (!isImage) {
				FileKit.delete(file);
				continue;
			}
			virtualPaths = fileUploadKit.handleImg(file, discDirectory, serverDirectory, ctrl);
			parameterName = uploadFile.getParameterName();
			
			filePaths = fileDirectory.get(parameterName);
			if(filePaths == null){
				filePaths = new HashMap<String,String>();
				fileDirectory.put(parameterName, filePaths);
			}
			filePaths.putAll(virtualPaths);
		}
		return fileDirectory;
	}
	
	/**
	 * 视频上传
	 * @return 只返回视频虚拟路径
	 */
	public static String uploadVideo(Controller ctrl, String parameterName, String saveFolderName, Integer maxPostSize, String encoding){
		Map<String, String> videoDirectory = uploadVideos(ctrl, saveFolderName, maxPostSize, encoding);
		if(MapKit.isBlank(videoDirectory)){
			return null;
		}
		return videoDirectory.get(parameterName);
	}
	
	/**
	 * 视频上传
	 * @param ctrl
	 * @param parameterName
	 * @param saveFolderName
	 * @param maxPostSize
	 * @param encoding
	 * @return (fileName,videoDirectory)返回视频虚拟路径、图片虚拟路径
	 */
	public static Map<String,String> uploadVideo(String parameterName, Controller ctrl, String saveFolderName, Integer maxPostSize, String encoding){
		Map<String, Map<String,String>> videoDirectory = uploadVideosAll(ctrl, saveFolderName, maxPostSize, encoding);
		if(MapKit.isBlank(videoDirectory)){
			return null;
		}
		return videoDirectory.get(parameterName);
	}
	
	/**
	 * 一次上传多个视频
	 * （若parameterName对应多个视频，则返回父级虚拟路径，否则只返回视频路径）
	 * @param ctrl
	 * @param saveFolderName
	 * @param maxPostSize
	 * @param encoding
	 * @return (parameterName,videoDirectory)
	 */
	public static Map<String, String> uploadVideos(Controller ctrl, String saveFolderName, Integer maxPostSize, String encoding){
		Map<String, Map<String,String>> videoDirectory = uploadVideosAll(ctrl, saveFolderName, maxPostSize, encoding);
		if(MapKit.isBlank(videoDirectory)){
			return null;
		}
		Map<String, String> videoPath = new HashMap<String, String>();
		Map<String,String> directorys = null;
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
	 * @return (parameterName,filename-directory(截图虚拟路径、视频虚拟路径))
	 */
	public static Map<String, Map<String,String>> uploadVideosAll(Controller ctrl, String saveFolderName, Integer maxPostSize, String encoding){
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
		
		Map<String,Map<String,String>> fileDirectory = new HashMap<String, Map<String,String>>();
		Map<String,String> filePaths = null,virtualPaths = null;
		File file = null;
		String parameterName = null;
		for(UploadFile uploadFile : uploadFiles){
			file = uploadFile.getFile();
			boolean isVideo = FileKit.isVideo(uploadFile.getFileName().toLowerCase());
			if (!isVideo) {
				FileKit.delete(file);
				continue;
			}
			virtualPaths = fileUploadKit.handleVideo(file, discDirectory, serverDirectory, ctrl);
			parameterName = uploadFile.getParameterName();
			
			filePaths = fileDirectory.get(parameterName);
			if(filePaths == null){
				filePaths = new HashMap<String,String>();
				fileDirectory.put(parameterName, filePaths);
			}
			filePaths.putAll(virtualPaths);
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
	 * @return (parameterName,directory)
	 */
	public static Map<String, String> uploadFiles(Controller ctrl, String saveFolderName, Integer maxPostSize, String encoding){
		Map<String, Map<String, String>> fileDirectory = uploadFilesAll(ctrl, saveFolderName, maxPostSize, encoding);
		if(MapKit.isBlank(fileDirectory)){
			return null;
		}
		Map<String, String> filePath = new HashMap<String, String>();
		Map<String, String> directorys = null;
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
	public static Map<String, Map<String, String>> uploadFilesAll(Controller ctrl, String saveFolderName, Integer maxPostSize, String encoding){
		FileLoadKit fileUploadKit = FileLoadKit.getInstance();
		fileUploadKit.initProgress(ctrl);
		String fileDirectory = fileUploadKit.getFileDirectory();
		String serverDirectory = fileUploadKit.getServerDirectory(fileDirectory, saveFolderName, ctrl);
		String tempDirectory = fileUploadKit.getTempDirectory(serverDirectory);
		
		List<UploadFile> uploadFiles = ctrl.getFiles(tempDirectory, maxPostSize, encoding);
		if(CollectionKit.isBlank(uploadFiles)){
			return null;
		}
		
		Map<String,Map<String, String>> directorys = new HashMap<String, Map<String, String>>();
		Map<String, String> filePaths = null,virtualPaths = null;
		File file = null;
		String parameterName = null;
		for(UploadFile uploadFile : uploadFiles){
			file = uploadFile.getFile();
			virtualPaths = fileUploadKit.handleFile(file, saveFolderName, ctrl);
			parameterName = uploadFile.getParameterName();
			
			filePaths = directorys.get(parameterName);
			if(filePaths == null){
				filePaths = new HashMap<String, String>();
				directorys.put(parameterName, filePaths);
			}
			filePaths.putAll(virtualPaths);
		}
		return directorys;
	}
	
	/**
	 * 处理临时图片
	 * @return
	 */
	private Map<String,String> handleImg(File tempFile, String discDirectory, String serverDirectory, Controller ctrl){
		String fileName = tempFile.getName();
		boolean isDevMode = isDevMode();
		if(!isDevMode){
			File newFile = fileCopy(discDirectory ,tempFile);
			fileName = newFile.getName();
		}
		Map<String,String> imagePaths = new HashMap<String,String>();
		String virtualPath = fileUploadKit.getVirtualDirectory(serverDirectory, fileName, ctrl);
		imagePaths.put(fileName, virtualPath);
		return imagePaths;
	}
	
	/**
	 * 处理临时视频
	 * @return
	 */
	private Map<String,String> handleVideo(File tempFile, String discDirectory, String serverDirectory, Controller ctrl){
		String directory = isDevMode() ? tempFile.getParent() : discDirectory;
		Map<String,String> fileDirectorys = FileHandleKit.compressFile(tempFile,directory);
		if(MapKit.isBlank(fileDirectorys)){
			return null;
		}
		Map<String,String> videoPaths = new HashMap<String,String>();
		String vieoDirectory = null,virtualDirectory = null,videoName = null;
		for(String fileName:fileDirectorys.keySet()){
			vieoDirectory = fileDirectorys.get(fileName);
			vieoDirectory = vieoDirectory.replaceAll("\\\\", "/");
			videoName = vieoDirectory.substring(vieoDirectory.lastIndexOf("/") + 1);
			virtualDirectory = getVirtualDirectory(serverDirectory, videoName, ctrl);
			videoPaths.put(fileName, virtualDirectory);
		}
		return videoPaths;
	}
	
	/**
	 * 处理（非图片、视频）文件
	 * @return
	 */
	private Map<String, String> handleFile(File tempFile, String discDirectory, String serverDirectory, Controller ctrl){
		String fileName = tempFile.getName();
		boolean isDevMode = isDevMode();
		if(!isDevMode){
			File newFile = fileCopy(discDirectory ,tempFile);
			fileName = newFile.getName();
		}
		Map<String, String> virtualPaths = new HashMap<String, String>();
		String virtualPath = fileUploadKit.getVirtualDirectory(serverDirectory, fileName, ctrl);
		virtualPaths.put(fileName, virtualPath);
		return virtualPaths;
	}
	
	/**
	 * 处理临时文件
	 */
	private Map<String, String> handleFile(File tempFile, String saveFolderName, Controller ctrl){
		Map<String, String> virtualPaths = new HashMap<String, String>();
		String fileName = tempFile.getName();
		String serverDirectory = fileUploadKit.getServerDirectoryAuto(fileName, saveFolderName, ctrl);
		boolean isDevMode = isDevMode();
		if(isDevMode){
			String fileDirectory = getFileDirectory();
			serverDirectory = fileUploadKit.getServerDirectory(fileDirectory, saveFolderName, ctrl);
		}
		String discDirectory = fileUploadKit.getDiscDirectory(serverDirectory);
		if(FileKit.isImage(fileName)){
			Map<String, String> imagePath = handleImg(tempFile, discDirectory, serverDirectory, ctrl);
			virtualPaths.putAll(imagePath);
		}else if(FileKit.isVideo(fileName)){
			Map<String, String> videoPaths = handleVideo(tempFile, discDirectory, serverDirectory, ctrl);
			virtualPaths.putAll(videoPaths);
		}else{
			Map<String, String> filePath = handleFile(tempFile, discDirectory, serverDirectory, ctrl);
			virtualPaths.putAll(filePath);
		}
		return virtualPaths;
	}
	
	/**
	 * 根据父级物理路径获取子文件的虚拟路径
	 * @param absolutePath 父级物理路径
	 * @param parentDirectory 父级虚拟路径
	 * @return 
	 */
	private Map<String, String> getFilesDescDirectory(String absolutePath,String parentDirectory){
		handleFilePath(absolutePath);
		Map<String, String> paths = new HashMap<String, String>();
		File file = new File(absolutePath);
		if(file.isFile()){
			paths.put(parentDirectory, absolutePath);
			return paths;
		}
		File[] files = file.listFiles();
		if(files == null){
			return null;
		}
		String fileName = null,directory = null;
		for (File f : files) {
			if(f.isDirectory()){
				continue;
			}
			fileName = f.getName();
			directory = getDirectory(parentDirectory, fileName);
			paths.put(directory, f.getAbsolutePath());
		}
		return paths;
	}
	
	/**
	 * 根据父级物理路径获取子文件的虚拟路径
	 * @param absolutePath 父级物理路径
	 * @param parentDirectory 父级虚拟路径
	 * @return 
	 */
	private List<String> getFilesVirtualDirectory(String absolutePath, String parentDirectory){
		handleFilePath(absolutePath);
		List<String> paths = new ArrayList<String>();
		File file = new File(absolutePath);
		if(file.isFile()){
			paths.add(parentDirectory);
			return paths;
		}
		File[] files = file.listFiles();
		if(files == null){
			return null;
		}
		String fileName = null,directory = null;
		for (File f : files) {
			if(f.isDirectory()){
				continue;
			}
			fileName = f.getName();
			directory = getDirectory(parentDirectory, fileName);
			paths.add(directory);
		}
		return paths;
	}
	
	/**
	 * 获取父级的物理路径
	 * @return
	 */
	private String getAbsolutePath(Controller ctrl, String parentDirectory){
		String basPath = getBasePath(ctrl.getRequest());
		String serverDirectory = parentDirectory.replaceFirst(basPath, "");
		String absolutePath = null;
		boolean isDevMode = isDevMode();
		if(isDevMode){
			String serverAbsolutePath = PathKit.getWebRootPath();
			absolutePath = getDirectory(serverAbsolutePath, serverDirectory);
		}else{
			absolutePath = getDiscDirectory(serverDirectory);
		}
		return absolutePath;
	}
	
	/**
	 * 获取虚拟上传的路径--(若存在多张，则返回父级路径)
	 * 
	 * @return
	 */
	private String getVirtualDirectory(Map<String,String> directorys) {
		if (MapKit.isBlank(directorys)) {
			return null;
		}
		
		int videos = 0;
		String directory = null,videoDirectory = null;
		for (String fileName : directorys.keySet()) {
			directory = directorys.get(fileName);
			if (FileKit.isVideo(fileName)) {
				if (videos > 0) {
					return directory.substring(0, directory.lastIndexOf("/"));
				}
				videoDirectory = directory;
				videos = videos + 1;
			}
		}
		if (directorys.size() == 1) {
			return directory;
		} else if(StrKit.notBlank(videoDirectory)){
			return videoDirectory;
		} else {
			return directory.substring(0, directory.lastIndexOf("/"));
		}
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
		if(FileKit.isImage(fileName)){
			directory = getImageDirectory();
		}else if(FileKit.isVideo(fileName)){
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
	 * 处理文件路径
	 */
	private void handleFilePath(String directory){
		File fileDirectory = new File(directory);
		if (!fileDirectory.exists()) {
			fileDirectory.mkdirs();
		}
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
