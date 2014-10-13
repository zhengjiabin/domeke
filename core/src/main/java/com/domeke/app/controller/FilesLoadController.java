package com.domeke.app.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.domeke.app.cos.multipart.FilePart;
import com.domeke.app.model.User;
import com.domeke.app.utils.VideoKit;
import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.upload.UploadFile;

/**
 * 文件上传下载的控制器<br>
 * 假如你所处理页面的Controller有需要使用到文件上传或者下载，则需要继承该Controller<br>
 * 
 * 
 * @author chenhailin
 *
 */
public class FilesLoadController extends Controller {

	private Logger logger = LoggerFactory.getLogger(FilesLoadController.class);

	private static String tempDirectory = "";
	private static String imageDirectory = "";
	private static String videoDirectory = "";
	private static String domainName = "";

	static {
		tempDirectory = PropKit.getString("tempDirectory");
		imageDirectory = PropKit.getString("imageDirectory");
		videoDirectory = PropKit.getString("videoDirectory");
		domainName = PropKit.getString("domainName");
	}

	/**
	 * 文件上传，要求表单enctype="multipart/form-data"类型<br>
	 * 假如未先处理文件类型的attribute，即调用该方法，是取不到其他参数<br>
	 * 具体实现文件上传功能详看
	 * <code>com.jfinal.core.Controller.getFile(String, String, Integer, String)</code>
	 * 
	 * @param parameterName
	 *            界面传过来的参数名
	 * @param saveFolderName
	 *            保存的文件夹名
	 * @param maxPostSize
	 *            文件大小的最大值
	 * @param encoding
	 *            编码
	 * @return 如果没有上传文件，则返回的文件路径为null
	 */
	protected String upLoadFileDealPath(String parameterName,
			String saveFolderName, Integer maxPostSize, String encoding) {
		initProgress();
		boolean isAbsolutePath = isAbsolutePath(imageDirectory);
		String tmpDirectoryPath = getDirectory(tempDirectory, saveFolderName);
		UploadFile uploadFile = getFile(parameterName, tmpDirectoryPath,
				maxPostSize, encoding);
		String imgFilePath = "";
		if (uploadFile != null) {
			File srcFile = uploadFile.getFile();
			String imgDirectoryPath = getDirectory(imageDirectory,
					saveFolderName);
			if (!isAbsolutePath) {
				ServletContext servletContext = JFinal.me().getServletContext();
				String contextPath = servletContext.getRealPath("");
				imgDirectoryPath = contextPath + "\\\\" + imgDirectoryPath;
			}
			File imgDirectory = new File(imgDirectoryPath);
			if (!imgDirectory.exists()) {
				imgDirectory.mkdirs();
			}
			File targetFile = new File(imgDirectoryPath + srcFile.getName());
			fileCopyByChannel(srcFile, targetFile);
			// 获取文件的绝对路径
			imgFilePath = targetFile.getAbsolutePath();
		}
		return getDomainNameFilePath(imgFilePath);
	}

	/**
	 * 
	 * @param parameterName
	 * @param saveFolderName
	 * @param maxPostSize
	 * @param encoding
	 * @return
	 */
	protected String upLoadFileNotDealPath(String parameterName,
			String saveFolderName, Integer maxPostSize, String encoding) {
		initProgress();
		String imgDirectoryPath = getDirectory(imageDirectory, saveFolderName);
		UploadFile uploadFile = getFile(parameterName, imgDirectoryPath,
				maxPostSize, encoding);
		String imgFilePath = "";
		if (uploadFile != null) {
			File srcFile = uploadFile.getFile();
			imgFilePath = srcFile.getAbsolutePath();
		}
		return getDomainNameFilePath(imgFilePath);
	}

	/**
	 * 一次请求提交上传多个同一字段类型的文件
	 * 
	 * @param saveFolderName
	 * @param maxPostSize
	 * @param encoding
	 * @return
	 */
	protected String upLoadFilesNotDealPath(String saveFolderName,
			Integer maxPostSize, String encoding) {
		initProgress();
		String imgDirectoryPath = getDirectory(imageDirectory, saveFolderName);
		List<UploadFile> uploadFileList = getFiles(imgDirectoryPath,
				maxPostSize, encoding);
		String imgFilePath = "";
		if (uploadFileList != null && uploadFileList.size() != 0) {
			imgFilePath = imgDirectoryPath;
		}
		return imgFilePath;
	}

	/**
	 * 上传视频
	 * 
	 * @param parameterName
	 * @param saveFolderName
	 * @param maxPostSize
	 * @param encoding
	 * @return
	 */
	protected String upLoadVideo(String parameterName, String saveFolderName,
			Integer maxPostSize, String encoding) {
		initProgress();
		boolean isAbsolutePath = isAbsolutePath(imageDirectory);
		String tmpDirectoryPath = getDirectory(tempDirectory, saveFolderName);
		UploadFile uploadFile = getFile(parameterName, tmpDirectoryPath,
				maxPostSize, encoding);
		String fileName = "";
		if (uploadFile != null) {
			File tagVideo = uploadFile.getFile();
			String vdoDirectoryPath = getDirectory(videoDirectory,
					saveFolderName);
			if (!isAbsolutePath) {
				ServletContext servletContext = JFinal.me().getServletContext();
				String contextPath = servletContext.getRealPath("");
				vdoDirectoryPath = contextPath + "\\\\" + vdoDirectoryPath;
			}
			fileName = VideoKit.compressVideo(tagVideo,	vdoDirectoryPath);
		}
		return getDomainNameFilePath(fileName);
	}

	/**
	 * 获取文件目录
	 * 
	 * @param fixDirectory
	 * @param userDefinedDirectory
	 * @return
	 */
	private String getDirectory(String fixDirectory, String userDefinedDirectory) {
		if (fixDirectory.indexOf("<username>") >= 0) {
			User user = getSessionAttr("user");
			String userName = user == null || user.getStr("username") == null ? "admin"
					: user.getStr("username");
			fixDirectory = fixDirectory.replaceAll("<username>", userName);
		}
		if (userDefinedDirectory != null
				&& userDefinedDirectory.trim().length() != 0) {
			fixDirectory = fixDirectory + userDefinedDirectory;
		}
		return fixDirectory;
	}

	protected boolean isAbsolutePath(String path) {
		if (path == null || path.trim().length() == 0) {
			return true;
		}
		if (path.startsWith("/") || path.indexOf(":") == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取文件在域名中的路径
	 * 
	 * @param filePath
	 *            文件的物理路径
	 * @return 域名中的路径
	 */
	protected String getDomainNameFilePath(String filePath) {
		filePath = filePath.replaceAll("\\\\", "/");
		String basePath = PropKit.getString("base_path");
		StringBuffer domainNameFilePath = new StringBuffer(domainName);
		String lastFilePath = filePath.substring(filePath.indexOf(basePath)
				+ basePath.length(), filePath.length());
		domainNameFilePath.append(lastFilePath);
		return domainNameFilePath.toString();
	}

	/**
	 * 文件复制
	 * 
	 * @param srcFile
	 *            源文件
	 * @param tagFile
	 *            目标文件
	 */
	public void fileCopyByChannel(File srcFile, File tagFile) {
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

	protected void initProgress() {
		FilePart.progresss.set(new FilePart.Progress() {
			public void progress(long currentSize) {
				int totalsize = FilesLoadController.this.getRequest()
						.getContentLength();
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
}
