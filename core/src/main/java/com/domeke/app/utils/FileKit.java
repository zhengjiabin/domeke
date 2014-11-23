
package com.domeke.app.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import com.jfinal.kit.StrKit;

public class FileKit {
	
	/**
	 * 删除文件及子文件
	 * @param file
	 */
	public static void delete(File file) {
		if (file != null && file.exists()) {
			if (file.isFile()) {
				file.delete();
			}
			else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i=0; i<files.length; i++) {
					delete(files[i]);
				}
			}
			file.delete();
		}
	}
	
	
	/**
	 * 文件拷贝
	 * @param directory
	 * @param file
	 */
	public static File fileCopy(String directory,File file){
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
	private static void fileCopyByChannel(File srcFile, File tagFile) {
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
	 * 处理不存在的文件路径
	 */
	public static void handleFilePath(String directory) {
		File fileDirectory = new File(directory);
		if (!fileDirectory.exists()) {
			fileDirectory.mkdirs();
		}
	}
	
	/**
	 * 获取文件类型
	 * @param fileName 文件名
	 * @return 文件类型
	 */
	public static String getFileType(String fileName){
		if(StrKit.isBlank(fileName)){
			return null;
		}
		return fileName.substring(fileName.lastIndexOf("."), fileName.length());
	}
	
	/**
	 * 获取文件名
	 * @param descDirectory 物理路径
	 * @return 文件名
	 */
	public static String getFileName(String descDirectory){
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
	public static String getDirectory(String directory, String fileName) {
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
	
	
	/**
	 * 获取指定文件类型的文件路径
	 * @param directory 原文件父级路径
	 * @param fileName 文件名
	 * @param fileType 文件类型
	 * @return
	 */
	public static String getFileTypeDirectory(String originalDirectory, String fileName, String fileType){
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
	 * 上传文件是否图片
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean isImage(String fileName) {
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
	public static boolean isVideo(String fileName) {
		String fileType = fileName.substring(fileName.lastIndexOf("."),fileName.length());
		if (fileType.matches(".avi|.rm|.rmvb|.mpg|.mpe|.mpeg|.dat|.asf|.wmv|.mov|.3gp|.flv|.mp4")) {
			return true;
		}
		return false;
	}
}
