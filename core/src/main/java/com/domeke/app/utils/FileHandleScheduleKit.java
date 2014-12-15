package com.domeke.app.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.domeke.app.Schedule.Element;
import com.domeke.app.Schedule.FilePool;
import com.domeke.app.Schedule.Pool;
import com.domeke.app.file.FileInterface;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Model;

public class FileHandleScheduleKit {

	private volatile static FileHandleScheduleKit scheduleKit;

	/** 日志 */
	private Logger logger = LoggerFactory.getLogger(FileHandleScheduleKit.class);

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
				}
			}
		}
		return scheduleKit;
	}

	/**
	 * 定时处理视频文件
	 * 
	 * @param models
	 *            处理视频的model信息
	 * @param virtualNames
	 *            虚拟路径
	 * @return 
	 */
	public static Map<String, Map<String, FileInterface>> handleScheduleFile(List<? extends Model<?>> models,
			String... virtualNames) {
		if (CollectionKit.isBlank(models) || virtualNames == null) {
			return null;
		}
		FileHandleScheduleKit scheduleKit = FileHandleScheduleKit.getInstance();
		Map<String, Map<String, FileInterface>> data = new HashMap<String, Map<String, FileInterface>>();
		FilePool<String> pool = new FilePool<String>();
		Map<String, FileInterface> files = null;
		for (String virtualName : virtualNames) {
			scheduleKit.addFileElements(pool, models, virtualName);
			pool.run();
			pool.close();
			// 线程堵塞
			files = pool.getFiles();
			if (MapKit.isNotBlank(files)) {
				data.put(virtualName, files);
			}
		}
		return data;
	}

	/**
	 * 定时处理文件
	 * 
	 * @param models
	 * @param virtualName
	 * @return 
	 */
	public static Map<String, FileInterface> handleScheduleFile(String virtualName, List<? extends Model<?>> models) {
		if (CollectionKit.isBlank(models) || StrKit.isBlank(virtualName)) {
			return null;
		}
		FileHandleScheduleKit scheduleKit = FileHandleScheduleKit.getInstance();
		FilePool<String> pool = new FilePool<String>();
		scheduleKit.addFileElements(pool, models, virtualName);
		pool.run();
		pool.close();
		return pool.getFiles();
	}

	/**
	 * 添加需异步处理的数据
	 * @param <T>
	 * @param pool
	 * @param models
	 * @param virtualName
	 */
	private <T> void addFileElements(Pool<String> pool, List<? extends Model<?>> models, String virtualName) {
		String virtualDirectory = null;
		Element<String> element = null;
		for (Model<?> model : models) {
			virtualDirectory = model.getStr(virtualName);
			element = new Element<String>(virtualDirectory);
			pool.addElement(element);
		}
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

}
