package com.domeke.app.file;

import java.util.List;

public class ImageFile extends FileInterface {

	/** 图片分辨率 */
	private String resolution;
	
	public ImageFile(){
		
	}
	
	public ImageFile(String processPath){
		setProcessPath(processPath);
	}

	/**
	 * 获取图片分辨率
	 */
	public String getResolution() {
		return resolution;
	}

	/**
	 * 设置图片分辨率
	 */
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	@Override
	public List<String> getProcessCommend() {
		List<String> command = new java.util.ArrayList<String>();
		command.add(getProcessPath());
		command.add("-i");
		command.add(getOriginalDirectory());
		return command;
	}
}
