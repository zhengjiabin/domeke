package com.domeke.app.cos.multipart;

import java.io.File;

/**
 * 
 * @author chenhailin
 *
 */
public class FileRenamePolicyByTime implements FileRenamePolicy {

	@Override
	public File rename(File file) {
		String fileName = file.getName();
		String ext = null;

		int dot = fileName.lastIndexOf(".");
		if (dot != -1) {
			fileName.substring(0, dot);
			ext = fileName.substring(dot);
		} else {
			ext = "";
		}
		String newName = System.currentTimeMillis() + ext;
		file = new File(file.getParent(), newName);
		return file;
	}

}
