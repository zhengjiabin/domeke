package com.domeke.app.controller;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.domeke.app.cos.multipart.FilePart;
import com.domeke.app.model.User;
import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;
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
	protected String upLoad(String parameterName, String saveFolderName, Integer maxPostSize, String encoding) {
		initProgress();
		String contextPath = JFinal.me().getContextPath();
		User user = getSessionAttr("user");
		String userName = user == null || user.getStr("username") == null ? "admin" : user.getStr("username");
		saveFolderName = saveFolderName + "\\" + userName;
		boolean isRelativePath = true;
		if (saveFolderName.startsWith("/") || saveFolderName.indexOf(":") == 1) {
			isRelativePath = false;
		}
		UploadFile uploadFile = getFile(parameterName, saveFolderName, maxPostSize, encoding);
		String filePath = null;
		if (uploadFile != null) {
			File picture = uploadFile.getFile();
			File replaceFile = renameToFile(uploadFile, picture);
			// 获取文件的绝对路径
			filePath = replaceFile.getAbsolutePath();
			if (isRelativePath) {
				// \project
				String project = contextPath.replace('/', '\\');
				// 截取出相对路径
				String[] tempArray = filePath.split("\\" + project);
				if (tempArray != null && tempArray.length > 1) {
					filePath = tempArray[tempArray.length - 1];
					filePath = contextPath + filePath.replaceAll("\\\\", "/");
				}
			}
		}
		return filePath;
	}

	protected File renameToFile(UploadFile uploadFile, File oldFile) {
		// 通过文件名截取出文件的类型
		String fileName = oldFile.getName();
		String fileType = "";
		if (fileName != null && fileName.indexOf('.') > 0) {
			String[] temp = fileName.split("\\.");
			fileType = temp[temp.length - 1];
		}
		// 以当前时间的毫秒数重命名文件名
		File replaceFile = new File(uploadFile.getSaveDirectory() + "\\" + System.currentTimeMillis() + "." + fileType);
		oldFile.renameTo(replaceFile);
		return replaceFile;
	}

	protected void initProgress() {

		FilePart.progresss.set(new FilePart.Progress() {
			public void progress(long currentSize) {
				int totalsize = FilesLoadController.this.getRequest().getContentLength();
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
