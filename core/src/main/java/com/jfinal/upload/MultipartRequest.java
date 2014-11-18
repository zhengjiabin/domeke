/**
 * Copyright (c) 2011-2015, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jfinal.upload;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * MultipartRequest.
 */
public class MultipartRequest extends HttpServletRequestWrapper {
	
	private static String saveDirectory;
	private static int maxPostSize;
	private static String encoding;
	private static boolean isMultipartSupported = false;
	
	private List<UploadFile> uploadFiles;
	private Map<String, String[]> parameters;
	private RequestParse multipartRequest;
	
	static void init(String saveDirectory, int maxPostSize, String encoding) {
		MultipartRequest.saveDirectory = saveDirectory;
		MultipartRequest.maxPostSize = maxPostSize;
		MultipartRequest.encoding = encoding;
		MultipartRequest.isMultipartSupported = true;	// 在OreillyCos.java中保障了, 只要被初始化就一定为 true
	}
	
	public MultipartRequest(HttpServletRequest request, String saveDirectory, int maxPostSize, String encoding) {
		super(request);
		wrapMultipartRequest(request, saveDirectory, maxPostSize, encoding);
	}
	
	public MultipartRequest(HttpServletRequest request, String saveDirectory, int maxPostSize) {
		super(request);
		wrapMultipartRequest(request, saveDirectory, maxPostSize, encoding);
	}
	
	public MultipartRequest(HttpServletRequest request, String saveDirectory) {
		super(request);
		wrapMultipartRequest(request, saveDirectory, maxPostSize, encoding);
	}
	
	public MultipartRequest(HttpServletRequest request) {
		super(request);
		wrapMultipartRequest(request, saveDirectory, maxPostSize, encoding);
	}
	
	/**
	 * 添加对相对路径的支持
	 * 1: 以 "/" 开头或者以 "x:开头的目录被认为是绝对路径
	 * 2: 其它路径被认为是相对路径, 需要 JFinalConfig.uploadedFileSaveDirectory 结合
	 */
	private String handleSaveDirectory(String saveDirectory) {
		if (saveDirectory.startsWith("/") || saveDirectory.indexOf(":") == 1)
			return saveDirectory;
		else 
			return MultipartRequest.saveDirectory + saveDirectory;
	}
	
	private void wrapMultipartRequest(HttpServletRequest request, String saveDirectory, int maxPostSize, String encoding) {
		if (!isMultipartSupported)
			throw new RuntimeException("Oreilly cos.jar is not found, Multipart post can not be supported.");

		saveDirectory = handleSaveDirectory(saveDirectory);

		File dir = new File(saveDirectory);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new RuntimeException("Directory " + saveDirectory + " not exists and can not create directory.");
			}
		}
		try {
			multipartRequest = new com.domeke.app.cos.MultipartRequest(request, saveDirectory, maxPostSize, encoding);
		} catch (IOException e) {
			e.printStackTrace();
		}
		parameters = new HashMap<String, String[]>(multipartRequest.getParameters());
		uploadFiles = new ArrayList<UploadFile>();
		Collection<FileloadInterface> files = multipartRequest.getFiles();
		if(files == null){
			return;
		}
		UploadFile uploadFile = null;
		String fileRequestName = null, fileName = null, originalFileName = null, contentType = null;
		for (FileloadInterface file : files) {
			fileRequestName = file.getParameterName();
			fileName = file.getFileName();
			originalFileName = file.getOriginalFileName();
			contentType = file.getContentType();
			uploadFile = new UploadFile(fileRequestName, saveDirectory, fileName, originalFileName, contentType);
			if (isSafeFile(uploadFile)){
				uploadFiles.add(uploadFile);
			}
		}
	}
	
	private boolean isSafeFile(UploadFile uploadFile) {
		if (uploadFile.getFileName().toLowerCase().endsWith(".jsp")) {
			uploadFile.getFile().delete();
			return false;
		}
		return true;
	}
	
	public List<UploadFile> getFiles() {
		return uploadFiles;
	}
	
	/**
	 * Methods to replace HttpServletRequest methods
	 */
	public Enumeration<String> getParameterNames() {
		if(parameters == null){
			return null;
		}
		Hashtable<String, String[]> table = new Hashtable<String, String[]>(parameters);
		return table.keys();
	}
	
	public String getParameter(String name) {
		if(parameters == null){
			return null;
		}
		String[] values= parameters.get(name);
		if(values == null || values.length<=0){
			return null;
		}
		return values[values.length-1];
	}
	
	public String[] getParameterValues(String name) {
		if (parameters == null) {
			return null;
		}
		String[] values= parameters.get(name);
		return values;
	}
	
	public Map<String, String[]> getParameterMap() {
		return parameters;
	}
}






