/**
 * 
 */
package com.domeke.app.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.domeke.app.file.FileInterface;
import com.domeke.app.file.ImageFile;
import com.domeke.app.file.ImageFileAnalysis;
import com.domeke.app.file.ImagePNGFile;
import com.domeke.app.file.VideoAVIFile;
import com.domeke.app.file.VideoFLVFile;
import com.domeke.app.file.VideoFile;
import com.domeke.app.file.VideoFileAnalysis;
import com.domeke.app.file.VideoMP4File;

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
			synchronized (FileHandleKit.class) {
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
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static Map<String, FileInterface> compressFile(File file, String toDirectory) throws IOException,
			InterruptedException {
		FileHandleKit fileHandleKit = FileHandleKit.getInstance();
		String fileName = file.getName();
		Map<String, FileInterface> virtualDirectory = null;
		if (FileKit.isImage(fileName)) {
			virtualDirectory = fileHandleKit.compressImage(file, toDirectory);
		} else if (FileKit.isVideo(fileName)) {
			virtualDirectory = fileHandleKit.compressVideo(file, toDirectory);
		}
		return virtualDirectory;
	}

	/**
	 * 压缩图片
	 * @param file
	 * @param toDirectory
	 * @return (fileName,directory)
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	private Map<String, FileInterface> compressImage(File file, String toDirectory) throws IOException,
			InterruptedException {
		Map<String, FileInterface> directorys = new HashMap<String, FileInterface>();
		ImageFile imageFile = processPNG(file, toDirectory);
		String fileName = imageFile.getFileName();
		directorys.put(fileName, imageFile);
		return directorys;
	}

	/**
	 * 压缩视频
	 * @param file
	 * @param toDirectory
	 * @return
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	private Map<String, FileInterface> compressVideo(File file, String toDirectory) throws IOException,
			InterruptedException {
		Map<String, FileInterface> directorys = new HashMap<String, FileInterface>();
		String fileName = file.getName();
		VideoFile videoFile = null;
		boolean isCompressVideo = isCompressVideo(fileName);
		if (isCompressVideo) {
			videoFile = buildVideoCompressCommand(file, toDirectory);
		} else {
			videoFile = videoCopy(file, toDirectory);
		}
		fileName = videoFile.getFileName();
		directorys.put(fileName, videoFile);
		return directorys;
	}

	/**
	 * 压缩视频
	 * @param toDirectory
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static Map<String, VideoFile> compressVideo(String toDirectory, File file) throws IOException,
			InterruptedException {
		FileHandleKit fileHandleKit = FileHandleKit.getInstance();
		VideoFile videoFile = fileHandleKit.buildVideoCompressCommand(file, toDirectory);
		String fileName = videoFile.getFileName();
		Map<String, VideoFile> directorys = new HashMap<String, VideoFile>();
		directorys.put(fileName, videoFile);
		return directorys;
	}

	/**
	 * 处理上传视频
	 * @param file
	 * @param toDirectory
	 * @return
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static VideoFile handleLoadVideo(File file, String toDirectory) throws IOException, InterruptedException {
		FileHandleKit fileHandleKit = FileHandleKit.getInstance();
		String fileName = file.getName();
		boolean isCompressVideo = fileHandleKit.isCompressVideo(fileName);

		VideoFile videoFile = null;
		if (isCompressVideo) {
			videoFile = fileHandleKit.handleLoadVideo(toDirectory, file);
		} else {
			videoFile = fileHandleKit.videoCopy(file, toDirectory);
		}
		return videoFile;
	}

	/**
	 * 处理视频上传
	 * @param file 临时文件
	 * @param toDirectory 目标文件的父级物理路径
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	private VideoFile handleLoadVideo(String toDirectory, File file) throws IOException, InterruptedException {
		VideoFile videoFile = new VideoFile();
		String fileName = file.getName();
		String originalDirectory = file.getAbsolutePath();
		videoFile.setFileName(fileName);
		videoFile.setFileType(FileKit.getFileType(fileName));
		videoFile.setDescDirectory(originalDirectory);
		videoFile.setOriginalDirectory(originalDirectory);

		String virtualDirectory = toDirectory;
		String imageDirectory = toDirectory;
		if (FileKit.isDevMode()) {
			virtualDirectory = file.getParent();
			imageDirectory = file.getParent();
		}
		virtualDirectory = FileKit.getDirectory(virtualDirectory, fileName);
		virtualDirectory = FileKit.getVirtualDirectory(virtualDirectory);
		videoFile.setVirtualDirectory(virtualDirectory);

		ImageFile imageFile = processPNG(file, imageDirectory);
		videoFile.addImageFile(imageFile);
		return videoFile;
	}

	/**
	 * 构造视频压缩信息
	 * @param file
	 * @return
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	private VideoFile buildVideoCompressCommand(File file, String toDirectory) throws IOException, InterruptedException {
		VideoFile videoFile = null;
		FileKit.handleFilePath(toDirectory);
		String fileName = file.getName();
		String descDirectory = file.getAbsolutePath();
		boolean isDirectoryCompressVideo = isDirectCompressVideo(fileName);
		if (isDirectoryCompressVideo) {
			videoFile = processMP4(descDirectory, toDirectory);
		} else {
			videoFile = processAVI(descDirectory, toDirectory);
			videoFile = processMP4(videoFile.getDescDirectory(), toDirectory);
		}
		ImageFile imageFile = processPNG(file, toDirectory);
		videoFile.addImageFile(imageFile);
		return videoFile;
	}

	/**
	 * 将图片压缩成.PNG格式
	 * @param file
	 * @param toDirectory
	 * @return
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	private ImageFile processPNG(File file, String toDirectory) throws IOException, InterruptedException {
		String fileName = file.getName();
		String imagePath = FileKit.getFileTypeDirectory(toDirectory, fileName, ".png");
		logger.info("视频截图==={}", imagePath);
		ImagePNGFile image = new ImagePNGFile(getFfmepgPath());
		image.setOriginalDirectory(file.getAbsolutePath());
		image.setDescDirectory(imagePath);
		return ImageFileAnalysis.imageProcess(image);
	}

	/**
	 * 将视频压缩成.MP4格式
	 * @return
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	private VideoFile processMP4(String descDirectory, String toDirectory) throws IOException, InterruptedException {
		String fileName = FileKit.getFileName(descDirectory);
		String videoPath = FileKit.getFileTypeDirectory(toDirectory, fileName, ".mp4");
		VideoFile video = new VideoMP4File(getFfmepgPath());
		video.setOriginalDirectory(descDirectory);
		video.setDescDirectory(videoPath);
		return VideoFileAnalysis.videoProcess(video);
	}

	/**
	 * 将视频压缩成.FLV格式
	 * @return
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	@SuppressWarnings("unused")
	private VideoFile processFLV(String descDirectory, String toDirectory) throws IOException, InterruptedException {
		String fileName = FileKit.getFileName(descDirectory);
		String videoPath = FileKit.getFileTypeDirectory(toDirectory, fileName, ".flv");
		VideoFile video = new VideoFLVFile(getFfmepgPath());
		video.setOriginalDirectory(descDirectory);
		video.setDescDirectory(videoPath);
		return VideoFileAnalysis.videoProcess(video);
	}

	/**
	 * 将视频压缩成.AVI格式
	 * @param type
	 * @return 物理路径
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	private VideoFile processAVI(String descDirectory, String toDirectory) throws IOException, InterruptedException {
		FileKit.handleFilePath(toDirectory);
		String fileName = FileKit.getFileName(descDirectory);
		String videoPath = FileKit.getFileTypeDirectory(toDirectory, fileName, ".avi");
		VideoAVIFile video = new VideoAVIFile(getMencoderPath());
		video.setOriginalDirectory(descDirectory);
		video.setDescDirectory(videoPath);
		return VideoFileAnalysis.videoProcess(video);
	}

	/**
	 * 视频拷贝
	 * @param directory 目标文件父级物理路径
	 * @param file 目标文件
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	private VideoFile videoCopy(File file, String directory) throws IOException, InterruptedException {
		FileHandleKit fileHandleKit = FileHandleKit.getInstance();
		File newFile = file;
		if (!FileKit.isDevMode()) {
			newFile = FileKit.fileCopy(directory, file);
		}
		VideoFile video = new VideoFile(fileHandleKit.getFfmepgPath());
		video.setOriginalDirectory(newFile.getAbsolutePath());
		video.setDescDirectory(newFile.getAbsolutePath());
		video.setHandled(true);
		// VideoFile videoFile = VideoFileAnalysis.videoProcess(video);

		String fileDirectory = newFile.getAbsolutePath();
		String imageDirectory = FileKit.getParentDirectory(fileDirectory);
		ImageFile imageFile = fileHandleKit.processPNG(newFile, imageDirectory);
		video.addImageFile(imageFile);
		return video;
	}

	/**
	 * 是否处理视频
	 * @return
	 */
	private boolean isCompressVideo(String fileName) {
		String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
		// if (fileType.matches(".mp4")) {
		// return false;
		// }
		return true;
	}

	/**
	 * 是否直接压缩视频
	 * @param fileName
	 * @return
	 */
	private boolean isDirectCompressVideo(String fileName) {
		String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
		// 支持解析的文件类型
		if (fileType.matches(".asx|.asf|.mpg|.wmv|.3gp|.mov|.avi|.flv|.mp4")) {
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

	private String getMencoderPath() {
		return mencoderPath;
	}

	private void setMencoderPath(String mencoderPath) {
		this.mencoderPath = mencoderPath;
	}
}
