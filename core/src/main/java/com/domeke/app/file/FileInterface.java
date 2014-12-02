package com.domeke.app.file;

import java.util.List;

public abstract class FileInterface {
	
	/** 是否处理过文件 */
	private boolean isHandled = false;
	
	/** 压缩工具 */
	private String processPath;
	
	/** 原文件物理路径 */
	private String originalDirectory;

	/** 文件物理路径 */
	private String descDirectory;

	/** 文件虚拟路径 */
	private String virtualDirectory;

	/** 文件名称 */
	private String fileName;

	/** 文件类型 */
	private String fileType;
	
	/** 获取转换格式指令 */
	public abstract List<String> getProcessCommend();
	
	public abstract String getProcessCommendStr();

	/**
	 * 获取文件物理地址
	 */
	public String getDescDirectory() {
		return descDirectory;
	}

	/**
	 * 设置文件物理地址
	 */
	public void setDescDirectory(String descDirectory) {
		this.descDirectory = descDirectory;
	}

	/**
	 * 获取文件虚拟地址
	 */
	public String getVirtualDirectory() {
		return virtualDirectory;
	}

	/**
	 * 设置文件虚拟地址
	 */
	public void setVirtualDirectory(String virtualDirectory) {
		this.virtualDirectory = virtualDirectory;
	}

	/**
	 * 获取文件类型
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * 设置文件类型
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * 获取文件名称
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * 设置文件名称
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 获取原文件物理路径
	 * @return
	 */
	public String getOriginalDirectory() {
		return originalDirectory;
	}

	/**
	 * 设置原文件物理路径
	 */
	public void setOriginalDirectory(String originalDirectory) {
		this.originalDirectory = originalDirectory;
	}
	
	
	/**
	 * 设置是否处理过文件
	 * @return
	 */
	public boolean isHandled() {
		return isHandled;
	}

	/**
	 * 获取是否处理过文件
	 * @param isHandled
	 */
	public void setHandled(boolean isHandled) {
		this.isHandled = isHandled;
	}

	/**
	 * 获取压缩工具
	 * @return
	 */
	protected String getProcessPath() {
		return processPath;
	}

	/**
	 * 设置压缩工具
	 * @param processPath
	 */
	protected void setProcessPath(String processPath) {
		this.processPath = processPath;
	}
}
