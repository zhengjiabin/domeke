
package com.domeke.app.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import com.jfinal.core.JFinal;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;

public class FileKit {
	
	private static String basePath = "";
	private static String backupsPath = "";
	private static String serverPath = "";
	private static String localServerPath = "";
	
	static{
		basePath = PropKit.getString("basePath");
		backupsPath = PropKit.getString("backupsPath");
		serverPath = PropKit.getString("serverPath");
		localServerPath = PropKit.getString("localServerPath");
	}
	
	/**
	 * 根据虚拟路径删除物理路径对应的文件
	 * 
	 * @param virtualDirectory
	 *            虚拟路径
	 */
	public static void deleteFiles(String virtualDirectory) {
		String descDirectory = getDescDirectory(virtualDirectory);
		File descFile = new File(descDirectory);
		delete(descFile);

		String tempDirectory = getTempDirectory(virtualDirectory);
		File tempFile = new File(tempDirectory);
		FileKit.delete(tempFile);
	}
	
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
	 * 根据虚拟地址，获取临时文件
	 * 
	 * @param virtualDirectory 虚拟路径
	 */
	public static File getTempFile(String virtualDirectory) {
		String tempPath = getTempDirectory(virtualDirectory);
		if (StrKit.isBlank(tempPath)) {
			return null;
		}
		File file = new File(tempPath);
		return file;
	}
	
	/**
	 * 根据虚拟路径获取物理文件
	 * @param virtualDirectory 虚拟路径
	 * @return 
	 */
	public static File getDescFile(String virtualDirectory){
		String descDirectory = getDescDirectory(virtualDirectory);
		if (StrKit.isBlank(descDirectory)) {
			return null;
		}
		File file = new File(descDirectory);
		return file;
	}
	
	/**
	 * 根据虚拟路径，获取物理路径
	 * @param 虚拟路径
	 * @return 物理路径
	 */
	public static String getDescDirectory(String virtualDirectory){
		String servPath = getServPath(virtualDirectory);
		String baseDirectory = basePath;
		if(isDevMode()){
			baseDirectory = PathKit.getWebRootPath();
		}
		String descPath = FileKit.getDirectory(baseDirectory, servPath);
		return descPath;
	}
	
	/**
	 * 根据虚拟路径，获取所有子文件的物理路径
	 * @param virtualDirectory 虚拟路径
	 * @return 所有子文件的物理路径
	 */
	public static List<String> getDescDirectorys(String virtualDirectory){
		List<String> descDirectorys = new ArrayList<String>();
		File descFile = getDescFile(virtualDirectory);
		if(!descFile.exists()){
			return null;
		}
		if(descFile.isFile()){
			descDirectorys.add(virtualDirectory);
			return descDirectorys;
		}
		File[] files = descFile.listFiles();
		if(files == null){
			return null;
		}
		List<String> descDirectoryList = null;
		for (File file : files) {
			if(file.isDirectory()){
				descDirectoryList = getDescDirectorys(file.getAbsolutePath());
				if(CollectionKit.isNotBlank(descDirectoryList)){
					descDirectorys.addAll(descDirectoryList);
				}
			}else{
				descDirectorys.add(file.getAbsolutePath());
			}
		}
		return descDirectorys;
	}
	
	/**
	 * 根据虚拟路径,获取所有子文件的虚拟路径
	 * @param virtualDirectory 虚拟路径
	 * @return
	 */
	public static List<String> getVirtualDirectorys(String virtualDirectory){
		List<String> descDirectorys = getDescDirectorys(virtualDirectory);
		if(CollectionKit.isBlank(descDirectorys)){
			return null;
		}
		List<String> virtualDirectoryList = new ArrayList<String>();
		String virtual = null;
		for(String descDirectory:descDirectorys){
			virtual = getVirtualDirectory(descDirectory);
			if(StrKit.isBlank(virtual)){
				continue;
			}
			virtualDirectoryList.add(virtual);
		}
		return virtualDirectoryList;
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
		descDirectory = getDirectory(descDirectory, "");
		return descDirectory.substring(descDirectory.lastIndexOf("/") + 1, descDirectory.length());
	}
	
	/**
	 * 根据文件物理路径获取虚拟路径
	 * @return
	 */
	public static String getVirtualDirectory(String descDirectory){
		descDirectory = getDirectory(descDirectory, "");
		String serBasePath = getServBasePath();
		if(isDevMode()){
			String webPath = PathKit.getWebRootPath();
			webPath = getDirectory(webPath, "");
			int index = descDirectory.indexOf(webPath);
			descDirectory = descDirectory.substring(index + webPath.length(), descDirectory.length());
		}else{
			int index = descDirectory.indexOf(basePath);
			descDirectory = descDirectory.substring(index + basePath.length(), descDirectory.length());
		}
		return getDirectory(serBasePath, descDirectory);
	}
	
	/**
	 * 根据文件的虚拟路径获取文件的临时物理路径
	 * @param virtualDirectory 虚拟路径
	 * @return 临时物理路径
	 */
	public static String getTempDirectory(String virtualDirectory){
		String servPath = getServPath(virtualDirectory);
		String backupsDirectory = backupsPath;
		if(isDevMode()){
			backupsDirectory = PathKit.getWebRootPath();
		}
		String tempDirectory = getDirectory(backupsDirectory, servPath);
		return tempDirectory;
	}
	
	/**
	 * 获取服务器的相对路径
	 * @param 虚拟路径
	 */
	public static String getServPath(String virtualDirectory){
		virtualDirectory = getDirectory(virtualDirectory, "");
		String basePath = getServBasePath();
		int index = virtualDirectory.indexOf(basePath);
		String servPath = virtualDirectory.substring(index + basePath.length(), virtualDirectory.length());
		return servPath;
	}
	
	/**
	 * 获取服务器的根路径
	 * @param 虚拟路径
	 * 
	 */
	public static String getServBasePath(){
		if(isDevMode()){
			return localServerPath;
		}else{
			return serverPath;
		}
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
	 * 获取父级路径
	 * @return
	 */
	public static String getParentDirectory(String directory){
		directory = getDirectory(directory, "");
		return directory.substring(0, directory.lastIndexOf("/"));
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
	
	/**
	 * 是否开发模式
	 * @return
	 */
	public static boolean isDevMode(){
		return JFinal.me().getConstants().getDevMode();
		
	}
}
