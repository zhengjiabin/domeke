package com.jfinal.upload;

import java.io.File;

public interface FileloadInterface {
	/** 获取文件请求名 */
	public String getParameterName();

	/** 获取文件名 */
	public String getFileName();

	/** 获取原文件名 */
	public String getOriginalFileName();

	/** 获取文件类型 */
	public String getContentType();

	/** 获取存储路径 */
	public String getSaveDirectory();

	/** 获取文件 */
	public File getFile();
}
