package com.domeke.app.Schedule;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.domeke.app.file.FileInterface;
import com.domeke.app.utils.FileHandleKit;
import com.domeke.app.utils.FileKit;
import com.domeke.app.utils.MapKit;
import com.jfinal.kit.StrKit;

public class FilePool<T> extends Pool<String>{

	/** (fileName,VideoFile) */
	private Map<String, FileInterface> files = Collections.synchronizedMap(new HashMap<String, FileInterface>());

	protected void handleElement(Element<String> element) {
		String virtualDirectory = element.getElement();
		if (StrKit.isBlank(virtualDirectory)) {
			return;
		}
		File file = FileKit.getTempFile(virtualDirectory);
		if(!file.exists() || !file.isFile()){
			return;
		}
		String descDirectory = FileKit.getDescDirectory(virtualDirectory);
		String toDirectory = FileKit.getParentDirectory(descDirectory);
		try {
			Map<String, FileInterface> data = FileHandleKit.compressFile(file, toDirectory);
			if (MapKit.isBlank(data)) {
				return;
			}
			handleFile(data);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	/**
	 * 处理文件
	 */
	private void handleFile(Map<String, FileInterface> data) {
		synchronized (files) {
			files.putAll(data);
		}
	}

	/**
	 * 获取文件
	 * @return
	 */
	public Map<String, FileInterface> getFiles() {
		synchronized (files) {
			return files;
		}
	}

	@Override
	protected void afterRun() {
	}
}
