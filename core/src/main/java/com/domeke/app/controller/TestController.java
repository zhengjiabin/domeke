package com.domeke.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.domeke.app.model.Works;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.ActionKey;

@ControllerBind(controllerKey = "/test")
public class TestController extends FilesLoadController {
	
	private Logger logger = LoggerFactory.getLogger(TestController.class);
	
	
	public void index(){
		render("/demo/upload.html");
	}
	
	
	@ActionKey("/upload")
	public void upoload(){
		renderNull();
	}
	
	
	
	
	@ActionKey("/saveUpload")
	public void saveFile(){
		logger.debug("上传文件开始========================");
		
		String name = getAttr("name");
		
		String addfile = getAttr("addfile");
		logger.debug("文件名称1============={}",name);
		logger.debug("文件名称2============={}",addfile);
		try {
			String coverPath = upLoadFile("cover", "", 2000 * 1024 * 1024,
					"utf-8");
			Works worksModel = getModel(Works.class);
			worksModel.set("cover", coverPath);
			worksModel.set("worksname", getAttr("worksname"));
			// 可改为获取当前用户的名字或者ID
			worksModel.set("creater", 111111);
			worksModel.set("modifier", 111111);
			worksModel.saveWorksInfo();
			redirect("/works/goToManager");
		} catch (Exception e) {
			e.printStackTrace();
			render("/works/goToManager");
		}
		
		logger.debug("上传文件结束========================");
	}

}
