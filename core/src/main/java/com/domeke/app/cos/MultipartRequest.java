// Copyright (C) 1998-2001 by Jason Hunter <jhunter_AT_acm_DOT_org>.
// All rights reserved.  Use of this class is limited.
// Please see the LICENSE for more information.

package com.domeke.app.cos;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpUtils;

import com.domeke.app.cos.multipart.DefaultFileRenamePolicy;
import com.domeke.app.cos.multipart.FilePart;
import com.domeke.app.cos.multipart.MultipartParser;
import com.domeke.app.cos.multipart.ParamPart;
import com.domeke.app.cos.multipart.Part;
import com.jfinal.upload.FileloadInterface;
import com.jfinal.upload.RequestParse;

/**
 * A utility class to handle <code>multipart/form-data</code> requests, the kind
 * of requests that support file uploads. This class emulates the interface of
 * <code>HttpServletRequest</code>, making it familiar to use. It uses a "push"
 * model where any incoming files are read and saved directly to disk in the
 * constructor. If you wish to have more flexibility, e.g. write the files to a
 * database, use the "pull" model <code>MultipartParser</code> instead.
 * <p>
 * This class can receive arbitrarily large files (up to an artificial limit you
 * can set), and fairly efficiently too. It cannot handle nested data (multipart
 * content within multipart content). It <b>can</b> now with the latest release
 * handle internationalized content (such as non Latin-1 filenames).
 * <p>
 * To avoid collisions and have fine control over file placement, there's a
 * constructor variety that takes a pluggable FileRenamePolicy implementation. A
 * particular policy can choose to rename or change the location of the file
 * before it's written.
 * <p>
 * See the included upload.war for an example of how to use this class.
 * <p>
 * The full file upload specification is contained in experimental RFC 1867,
 * available at <a href="http://www.ietf.org/rfc/rfc1867.txt">
 * http://www.ietf.org/rfc/rfc1867.txt</a>.
 *
 * @see MultipartParser
 * 
 * @author Jason Hunter
 * @author Geoff Soutter
 * @version 1.12, 2004/04/11, added null check for Opera malformed bug<br>
 * @version 1.11, 2002/11/01, combine query string params in param list<br>
 * @version 1.10, 2002/05/27, added access to the original file names<br>
 * @version 1.9, 2002/04/30, added support for file renaming, thanks to
 *          Changshin Lee<br>
 * @version 1.8, 2002/04/30, added support for internationalization, thanks to
 *          Changshin Lee<br>
 * @version 1.7, 2001/02/07, made fields protected to increase user flexibility<br>
 * @version 1.6, 2000/07/21, redid internals to use MultipartParser, thanks to
 *          Geoff Soutter<br>
 * @version 1.5, 2000/02/04, added auto MacBinary decoding for IE on Mac<br>
 * @version 1.4, 2000/01/05, added getParameterValues(), WebSphere 2.x
 *          getContentType() workaround, stopped writing empty "unknown" file<br>
 * @version 1.3, 1999/12/28, IE4 on Win98 lastIndexOf("boundary=") workaround<br>
 * @version 1.2, 1999/12/20, IE4 on Mac readNextPart() workaround<br>
 * @version 1.1, 1999/01/15, JSDK readLine() bug workaround<br>
 * @version 1.0, 1998/09/18<br>
 */
@SuppressWarnings("deprecation")
public class MultipartRequest implements RequestParse{

	private static final int DEFAULT_MAX_POST_SIZE = 1024 * 1024; // 1 Meg
	private static final DefaultFileRenamePolicy policy = new DefaultFileRenamePolicy();

	protected Map<String, Vector<String>> parameters = new Hashtable<String,Vector<String>>(); // name - Vector of values
	protected Collection<FileloadInterface> files = new ArrayList<FileloadInterface>(); // name - UploadedFile
	
	/**
	 * Constructs a new MultipartRequest to handle the specified request, saving
	 * any uploaded files to the given directory, and limiting the upload size
	 * to 1 Megabyte. If the content is too large, an IOException is thrown.
	 * This constructor actually parses the <tt>multipart/form-data</tt> and
	 * throws an IOException if there's any problem reading or parsing the
	 * request.
	 *
	 * @param request
	 *            the servlet request.
	 * @param saveDirectory
	 *            the directory in which to save any uploaded files.
	 * @exception IOException
	 *                if the uploaded content is larger than 1 Megabyte or
	 *                there's a problem reading or parsing the request.
	 */
	public MultipartRequest(HttpServletRequest request, String saveDirectory)
			throws IOException {
		this(request, saveDirectory, DEFAULT_MAX_POST_SIZE);
	}

	/**
	 * Constructs a new MultipartRequest to handle the specified request, saving
	 * any uploaded files to the given directory, and limiting the upload size
	 * to the specified length. If the content is too large, an IOException is
	 * thrown. This constructor actually parses the <tt>multipart/form-data</tt>
	 * and throws an IOException if there's any problem reading or parsing the
	 * request.
	 *
	 * @param request
	 *            the servlet request.
	 * @param saveDirectory
	 *            the directory in which to save any uploaded files.
	 * @param encoding
	 *            the encoding of the response, such as ISO-8859-1
	 * @exception IOException
	 *                if the uploaded content is larger than 1 Megabyte or
	 *                there's a problem reading or parsing the request.
	 */
	public MultipartRequest(HttpServletRequest request, String saveDirectory,
			String encoding) throws IOException {
		this(request, saveDirectory, DEFAULT_MAX_POST_SIZE, encoding);
	}

	/**
	 * Constructs a new MultipartRequest to handle the specified request, saving
	 * any uploaded files to the given directory, and limiting the upload size
	 * to the specified length. If the content is too large, an IOException is
	 * thrown. This constructor actually parses the <tt>multipart/form-data</tt>
	 * and throws an IOException if there's any problem reading or parsing the
	 * request.
	 *
	 * @param request
	 *            the servlet request.
	 * @param saveDirectory
	 *            the directory in which to save any uploaded files.
	 * @param maxPostSize
	 *            the maximum size of the POST content.
	 * @param policy
	 *            the rules for renaming in case of file name collisions
	 * @exception IOException
	 *                if the uploaded content is larger than
	 *                <tt>maxPostSize</tt> or there's a problem reading or
	 *                parsing the request.
	 */
	public MultipartRequest(HttpServletRequest request, String saveDirectory,
			int maxPostSize) throws IOException {
		this(request, saveDirectory, maxPostSize, null);
	}
	
	/**
	 * Constructor with an old signature, kept for backward compatibility.
	 * Without this constructor, a servlet compiled against a previous version
	 * of this class (pre 1.4) would have to be recompiled to link with this
	 * version. This constructor supports the linking via the old signature.
	 * Callers must simply be careful to pass in an HttpServletRequest.
	 * 
	 */
	public MultipartRequest(ServletRequest request, String saveDirectory)
			throws IOException {
		this((HttpServletRequest) request, saveDirectory);
	}

	/**
	 * Constructor with an old signature, kept for backward compatibility.
	 * Without this constructor, a servlet compiled against a previous version
	 * of this class (pre 1.4) would have to be recompiled to link with this
	 * version. This constructor supports the linking via the old signature.
	 * Callers must simply be careful to pass in an HttpServletRequest.
	 * 
	 */
	public MultipartRequest(ServletRequest request, String saveDirectory,
			int maxPostSize) throws IOException {
		this((HttpServletRequest) request, saveDirectory, maxPostSize);
	}

	/**
	 * Constructs a new MultipartRequest to handle the specified request, saving
	 * any uploaded files to the given directory, and limiting the upload size
	 * to the specified length. If the content is too large, an IOException is
	 * thrown. This constructor actually parses the <tt>multipart/form-data</tt>
	 * and throws an IOException if there's any problem reading or parsing the
	 * request.
	 *
	 * To avoid file collisions, this constructor takes an implementation of the
	 * FileRenamePolicy interface to allow a pluggable rename policy.
	 *
	 * @param request
	 *            the servlet request.
	 * @param saveDirectory
	 *            the directory in which to save any uploaded files.
	 * @param maxPostSize
	 *            the maximum size of the POST content.
	 * @param encoding
	 *            the encoding of the response, such as ISO-8859-1
	 * @param policy
	 *            a pluggable file rename policy
	 * @exception IOException
	 *                if the uploaded content is larger than
	 *                <tt>maxPostSize</tt> or there's a problem reading or
	 *                parsing the request.
	 */
	public MultipartRequest(HttpServletRequest request, String saveDirectory, int maxPostSize, String encoding) throws IOException {
		parseRequest(request, saveDirectory, maxPostSize, encoding);
	}
	
	/**
	 * 判断参数是否规范
	 */
	private void parseBeforeCheck(HttpServletRequest request, String saveDirectory, int maxPostSize){
		if (request == null){
			throw new IllegalArgumentException("request cannot be null");
		}
		if (saveDirectory == null){
			throw new IllegalArgumentException("saveDirectory cannot be null");
		}
		if (maxPostSize <= 0) {
			throw new IllegalArgumentException("maxPostSize must be positive");
		}
		File dir = new File(saveDirectory);
		if (!dir.isDirectory()){
			throw new IllegalArgumentException("Not a directory: " + saveDirectory);
		}
		if (!dir.canWrite()){
			throw new IllegalArgumentException("Not writable: " + saveDirectory);
		}
	}
	
	/**
	 * 处理请求参数
	 * @param parser
	 */
	private void parseQuery(HttpServletRequest request, MultipartParser parser){
		if (request.getQueryString() != null) {
			// Let HttpUtils create a name->String[] structure
			Hashtable<String, String[]> queryParameters = HttpUtils.parseQueryString(request.getQueryString());
			// For our own use, name it a name->Vector structure
			Enumeration<String> queryParameterNames = queryParameters.keys();
			String paramName = null;
			while (queryParameterNames.hasMoreElements()) {
				paramName = queryParameterNames.nextElement();
				String[] values = (String[]) queryParameters.get(paramName);
				Vector<String> newValues = new Vector<String>();
				for (int i = 0; i < values.length; i++) {
					newValues.add(values[i]);
				}
				parameters.put(paramName, newValues);
			}
		}
	}
	
	/**
	 * 处理请求信息
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	private void parsePart(MultipartParser parser, String saveDirectory) throws IOException{
		File dir = new File(saveDirectory);
		Part part;
		while ((part = parser.readNextPart()) != null) {
			String name = part.getName();
			if (name == null) {
				throw new IOException("Malformed input: parameter name missing (known Opera 7 bug)");
			}
			parseParam(part, name);
			parseFiles(part, dir, name);
		}
	}
	
	/**
	 * 解析参数
	 * @param part
	 * @throws UnsupportedEncodingException 
	 */
	private void parseParam(Part part, String name) throws UnsupportedEncodingException{
		if (!part.isParam()) {
			return;
		}
		ParamPart paramPart = (ParamPart) part;
		String value = paramPart.getStringValue();
		Vector<String> existingValues = parameters.get(name);
		if (existingValues == null) {
			existingValues = new Vector<String>();
			parameters.put(name, existingValues);
		}
		existingValues.addElement(value);
	}
	
	/**
	 * 解析文件
	 * @throws IOException 
	 */
	private void parseFiles(Part part, File dir, String name) throws IOException{
		if (!part.isFile()) {
			return;
		}
		FilePart filePart = (FilePart) part;
		String fileName = filePart.getFileName();
		FileloadInterface fileload = null;
		if (fileName != null) {
			filePart.setRenamePolicy(policy);
			filePart.writeTo(dir);
			fileload = new UploadedFile(name, dir.toString(), filePart.getFileName(), fileName, filePart.getContentType());
		} else {
			fileload = new UploadedFile(name, null, null, null, null);
		}
		files.add(fileload);
	}
	
	/**
	 * 解析请求
	 * @param request
	 * @param saveDirectory
	 * @param maxPostSize
	 * @param encoding
	 */
	private void  parseRequest(HttpServletRequest request, String saveDirectory, int maxPostSize, String encoding) throws IOException{
		parseBeforeCheck(request, saveDirectory, maxPostSize);
		MultipartParser parser = new MultipartParser(request, maxPostSize, true, true, encoding);
		parseQuery(request, parser);
		parsePart(parser, saveDirectory);
	}

	@Override
	public Collection<FileloadInterface> getFiles() {
		return files;
	}

	@Override
	public Map<String, String[]> getParameters() {
		Map<String, String[]> params = new Hashtable<String, String[]>();
		Vector<String> values = null;
		String[] paramValues = null;
		for(String key:parameters.keySet()){
			values = parameters.get(key);
			paramValues = new String[values.size()];
			values.copyInto(paramValues);
			params.put(key, paramValues);
		}
		return params;
	}
}

class UploadedFile implements FileloadInterface{

	private String dir;
	private String parameterName;
	private String saveDirectory;
	private String filename;
	private String original;
	private String type;

	UploadedFile(String parameterName, String dir, String filename, String original, String type) {
		this.parameterName = parameterName;
		this.dir = dir;
		this.filename = filename;
		this.original = original;
		this.type = type;
	}

	@Override
	public String getParameterName() {
		return parameterName;
	}

	@Override
	public String getFileName() {
		return filename;
	}

	@Override
	public String getSaveDirectory() {
		return saveDirectory;
	}

	@Override
	public String getOriginalFileName() {
		return original;
	}

	@Override
	public String getContentType() {
		return type;
	}

	@Override
	public File getFile() {
		if (dir == null || filename == null) {
			return null;
		} else {
			return new File(dir + File.separator + filename);
		}
	}
}
