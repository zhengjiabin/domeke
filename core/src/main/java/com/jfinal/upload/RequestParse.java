package com.jfinal.upload;

import java.util.Collection;
import java.util.Map;

public interface RequestParse {

	/** 获取参数 */
	public Map<String, String[]> getParameters();

	/** 获取文件 */
	public Collection<FileloadInterface> getFiles();
}
