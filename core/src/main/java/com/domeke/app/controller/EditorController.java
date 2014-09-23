package com.domeke.app.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;
import com.jfinal.kit.FileKit;
import com.jfinal.upload.UploadFile;

@ControllerBind(controllerKey = "editor")
public class EditorController extends Controller {

	private static String tempDirectory = "/upload/image/";
	private static String parameterName = "upload";
	private static String imgType = ".jpg.gif.png,bmp.JPG.GIF.PNG.BMP";
	private static int maxPostSize = 2097152;

	public void uploadImage() {
		try {
			String saveDirectory = JFinal.me().getServletContext().getRealPath(tempDirectory);
			UploadFile upload = getFile(parameterName, saveDirectory, maxPostSize);
			//检查格式是否符合
			if (!isImg(upload)) {
				FileKit.delete(upload.getFile());
				renderHtml("<script type=\"text/javascript\">alert(\"请上传图片文件。\");</script>");
				return;
			}
			
			detailUpload(saveDirectory, upload);

		} catch (RuntimeException e) {
			renderHtml("<script type=\"text/javascript\">alert(\"上传的文件有误，请重新上传。\");javascript:history.go(-1);</script>");
		} catch (Exception e) {
			e.printStackTrace();
			renderHtml("<script type=\"text/javascript\">alert(\"上传文件失败，请联系管理员。\");</script>");
		}
	}

	/**
	 * 处理文件上传
	 * @param saveDirectory
	 * @param upload
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void detailUpload(String saveDirectory, UploadFile upload)
			throws FileNotFoundException, IOException {
		String newFileName = getNewFileName(upload);
		
		File inputFile = upload.getFile();
		File outputFile = new File(saveDirectory + File.separator + newFileName);
		
		InputStream is = new FileInputStream(inputFile);
		OutputStream os = new FileOutputStream(outputFile);
		try {
			byte[] buffer = new byte[1024 * 1024];
			while (is.read(buffer) > 0) {
				os.write(buffer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				is.close();
			}
			if (os != null) {
				os.close();
			}
		}
		afterUpload(upload, newFileName);
	}

	/**
	 * 上传后的处理
	 * @param upload
	 * @param CKEditorFuncNum
	 * @param newFileName
	 */
	private void afterUpload(UploadFile upload,String newFileName) {
		String CKEditorFuncNum = getPara("CKEditorFuncNum");
		// 删除原文件
		FileKit.delete(upload.getFile());
		StringBuffer html = new StringBuffer();
		html.append("<script type=\"text/javascript\">window.parent.CKEDITOR.tools.callFunction(");
		html.append(CKEditorFuncNum);
		html.append(", '");
		html.append("\\." + tempDirectory);
		html.append(newFileName);
		html.append("', '');</script>");
		// 返回给ckeditor
		renderHtml(html.toString());
	}
	
	/**
	 * 获取新的文件名
	 */
	private String getNewFileName(UploadFile upload){
		String uploadFileName = upload.getFileName();
		String fileName = getFileName(uploadFileName);
		String extName = getExtention(uploadFileName);
		String newFileName = fileName + "-" + System.currentTimeMillis() + extName;
		return newFileName;
	}

	/**
	 * 检查图片格式
	 * 
	 * @return 是否符合格式 true/false
	 */
	private boolean isImg(UploadFile upload) {
		String uploadFileName = upload.getFileName();
		// 获取扩展名
		String extName = getExtention(uploadFileName);
		if (!imgType.contains(extName)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 获取文件名
	 * 
	 * @param fileName
	 */
	private String getFileName(String uploadFileName) {
		if (uploadFileName == null) {
			return null;
		}
		int lastIndex = uploadFileName.lastIndexOf(".");
		if (lastIndex > -1) {
			return uploadFileName.substring(0,uploadFileName.lastIndexOf("."));
		}
		return null;
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param fileName
	 */
	private String getExtention(String uploadFileName) {
		if (uploadFileName == null) {
			return null;
		}
		int lastIndex = uploadFileName.lastIndexOf(".");
		if (lastIndex > -1) {
			return uploadFileName.substring(lastIndex, uploadFileName.length());
		}
		return null;
	}
}
