package com.domeke.app.controller;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.route.ControllerBind;
import com.domeke.app.utils.FileLoadKit;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

@ControllerBind(controllerKey = "editor")
public class EditorController extends Controller{

	private static String saveFolderName = "/community";
	private static String parameterName = "upload";
	private static int maxPostSize = 2097152;
	private static String encoding = "UTF-8";

	@Before(LoginInterceptor.class)
	public void uploadImage() {
		try {
			String filePath = FileLoadKit.uploadImg(this, parameterName, saveFolderName, maxPostSize, encoding);
			if(StrKit.isBlank(filePath)){
				return;
			}
			afterUpload(filePath);
		} catch (RuntimeException e) {
			renderHtml("<script type=\"text/javascript\">alert(\"上传的文件有误，请重新上传。\");javascript:history.go(-1);</script>");
		} catch (Exception e) {
			e.printStackTrace();
			renderHtml("<script type=\"text/javascript\">alert(\"上传文件失败，请联系管理员。\");</script>");
		}
	}

	/**
	 * 上传后的处理
	 * @param filePath
	 */
	private void afterUpload(String filePath) {
		String CKEditorFuncNum = getPara("CKEditorFuncNum");
		// 删除原文件
		StringBuffer html = new StringBuffer();
		html.append("<script type=\"text/javascript\">window.parent.CKEDITOR.tools.callFunction(");
		html.append(CKEditorFuncNum);
		html.append(", '");
		html.append(filePath);
		html.append("', '');</script>");
		// 返回给ckeditor
		renderHtml(html.toString());
	}
}
