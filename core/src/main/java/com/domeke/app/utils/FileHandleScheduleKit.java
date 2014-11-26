package com.domeke.app.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.activerecord.Model;

public class FileHandleScheduleKit {

	private volatile static FileHandleScheduleKit scheduleKit;

	/** 日志 */
	private Logger logger;

	/**
	 * 单例模式初始化
	 * 
	 * @return
	 */
	public static FileHandleScheduleKit getInstance() {
		if (scheduleKit == null) {
			synchronized (FileHandleScheduleKit.class) {
				if (scheduleKit == null) {
					scheduleKit = new FileHandleScheduleKit();
					scheduleKit.setLogger(LoggerFactory.getLogger(FileHandleScheduleKit.class));
				}
			}
		}
		return scheduleKit;
	}
	
	/**
	 * 定时处理视频文件
	 * @param models 处理视频的model信息
	 * @param virtualNames 虚拟路径
	 */
	@SuppressWarnings("rawtypes")
	public static void handleScheduleVideo(List<? extends Model> models,String... virtualNames){
		if(CollectionKit.isBlank(models) || virtualNames == null){
			return;
		}
		for(String virtualName:virtualNames){
			handleScheduleVideo(models, virtualName);
		}
	}
	
	/**
	 * 定时处理视频文件
	 * @param models
	 * @param virtualName
	 */
	@SuppressWarnings("rawtypes")
	public static void handleScheduleVideo(List<? extends Model> models,String virtualName){
		FileHandleScheduleKit timingKit = getInstance();
		String virtualDirectory = null;
		for(Model model:models){
			virtualDirectory = model.getStr(virtualName);
//			FileLoadKit.getFileDescDirectory(ctrl, parentDirectory)
//			model.get
		}
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

}
