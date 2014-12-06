package com.domeke.app.file;

import java.util.ArrayList;
import java.util.List;

public class VideoFile extends FileInterface {

	/** 播放时长 */
	private String playTime;

	/** 视频分辨率 */
	private String resolution;

	private List<ImageFile> imageFiles;
	
	public VideoFile(){
		
	}
	
	public VideoFile(String processPath){
		setProcessPath(processPath);
	}

	/**
	 * 获取视频时长
	 */
	public String getPlayTime() {
		return playTime;
	}

	/**
	 * 设置视频时长
	 */
	public void setPlayTime(String playTime) {
		this.playTime = playTime;
	}

	/**
	 * 获取视频分辨率
	 */
	public String getResolution() {
		return resolution;
	}

	/**
	 * 设置视频分辨率
	 */
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	/**
	 * 设置视频截图集合
	 * 
	 * @return
	 */
	public List<ImageFile> getImageFiles() {
		return imageFiles;
	}

	/**
	 * 获取视频截图
	 * 
	 * @param imageFiles
	 */
	public void setImageFiles(List<ImageFile> imageFiles) {
		this.imageFiles = imageFiles;
	}

	/**
	 * 添加视频截图
	 * 
	 * @param imageFile
	 */
	public void addImageFile(ImageFile imageFile) {
		if (this.imageFiles == null) {
			this.imageFiles = new ArrayList<ImageFile>();
		}
		imageFiles.add(imageFile);
	}

	/**
	 * 只查看文件信息，不转换
	 */
	@Override
	public List<String> getProcessCommend() {
		List<String> commend = new java.util.ArrayList<String>();
		commend.add(getProcessPath());
		commend.add("-i");
		commend.add(getOriginalDirectory());
		return commend;
	}

	@Override
	public String getProcessCommendStr() {
		String commmond = getProcessPath() + " -i " + getOriginalDirectory();
		return commmond;
	}
}
