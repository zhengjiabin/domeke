package com.domeke.app.controller;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.domeke.app.file.ImageFile;
import com.domeke.app.file.VideoFile;
import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.DownLoad;
import com.domeke.app.model.Favourite;
import com.domeke.app.model.User;
import com.domeke.app.model.Work;
import com.domeke.app.model.Works;
import com.domeke.app.model.WorksType;
import com.domeke.app.route.ControllerBind;
import com.domeke.app.utils.CollectionKit;
import com.domeke.app.utils.FileKit;
import com.domeke.app.utils.FileLoadKit;
import com.domeke.app.utils.MapKit;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.ParseDemoKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

/**
 * 作品model控制器
 * 
 * @author chenhailin
 *
 */
@ControllerBind(controllerKey = "/works")
@Before(LoginInterceptor.class)
public class WorksController extends Controller {

	/**
	 * to管理界面
	 */
	public void goToManager() {
		String flag = getPara("flag");
		if("2".equals(flag)) {
			render("/admin/admin_manhua.html");
			return;
		} else {
			render("/admin/admin_works.html");
			return;
		}
	}

	/**
	 * 删除作品信息
	 */
	public void delete() {
		int id = getParaToInt("id");
		Works worksModel = getModel(Works.class);
		boolean bool = worksModel.deleteById(id);
		Work workModel = getModel(Work.class);
		List<Work> workList = workModel.getWorkByWorksID(id);
		for (Work work : workList) {
			work.delete();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (bool) {
			map.put("success", 1);
		} else {
			map.put("success", 0);
		}
		renderJson(map);
	}

	public void deleteWork() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			int id = getParaToInt("id");
			Work workModel = getModel(Work.class);
			workModel = workModel.findById(id);
			boolean bool = false;
			Works worksModel = getModel(Works.class);
			if (workModel.isNotEmpty()) {
				bool = workModel.delete();
				if (bool) {
					worksModel = worksModel.findById(workModel.get("worksid"));
					Long maxnum = worksModel.getLong("maxnum");
					if (maxnum != 0 && maxnum != null) {
						maxnum = maxnum - 1;
					}
					worksModel.set("maxnum", maxnum);
					worksModel.update();
				}
			}

			if (bool) {
				map.put("success", "1");
			} else {
				map.put("success", "0");
			}
			renderJson(map);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", "0");
			renderJson(map);
			return;

		}

	}

//	/**
//	 * 查询出所有作品
//	 */
//	public void queryAllWorksInfo() {
//		Works worksModel = getModel(Works.class);
//		List<Works> workslist = worksModel.queryAllWorksInfo();
//		CacheKit.remove("workslist", workslist);
//		this.setAttr("workslist", workslist);
//	}

	/**
	 * 根据作品ID获取某作品
	 */
	public void getWorksJsonById() {
		Works worksModel = getModel(Works.class);
		Works works = worksModel.findById(getParaToInt("id"));
		renderJson(works);
	}

	public void getWorkJsonById() {
		Work worksModel = getModel(Work.class);
		Work work = worksModel.findById(getParaToInt("id"));
		renderJson(work);
	}

//	/**
//	 * 根据作品类型获取作品信息
//	 */
//	public void getWorksInfoByType() {
//		String workstype = getPara("workstype");
//		String pageNumber = getPara("pageNumber");
//		String pageSize = getPara("pageSize");
//		if (StrKit.isBlank(pageNumber)) {
//			pageNumber = "1";
//		}
//		if (StrKit.isBlank(pageSize)) {
//			pageSize = "5";
//		}
//		Works worksModel = getModel(Works.class);
//		Page<Works> workslist = worksModel.getWorksInfoPage(workstype,
//				Integer.parseInt(pageNumber), Integer.parseInt(pageSize));
//		renderJson(workslist);
//	}

//	/**
//	 * 根据作品类型分页获取作品信息
//	 */
//	public List<Works> getWorksInfoByTypePage(String workstype,
//			Integer pageNumber, Integer pageSize) {
//		Works worksModel = getModel(Works.class);
//		List<Works> workslist = worksModel.getWorksInfoByTypePage(workstype,
//				pageNumber, pageSize);
//		return workslist;
//	}

	// --------------个人中心-->我的作品---------------
//	public void addShipin() {
//		User user = getSessionAttr("user");
//		Integer userId = Integer.parseInt(String.valueOf(user.get("userid")));
//		String userName = user.get("username");
//
//		Map<String, String> directorys = FileLoadKit.uploadVideo("comic", this,
//				"works", 2000 * 1024 * 1024, "utf-8");
//		if (MapKit.isBlank(directorys)) {
//			Map<String, Object> map = Maps.newHashMap();
//			map.put("success", 0);
//			map.put("message", "上传失败！");
//			renderJson(map);
//			return;
//		}
//		String coverPath = null, comicPath = null, directory = null;
//		for (String fileName : directorys.keySet()) {
//			directory = directorys.get(fileName);
//			if (FileKit.isImage(fileName)) {
//				coverPath = directory;
//			} else if (FileKit.isVideo(fileName)) {
//				comicPath = directory;
//			}
//
//		}
//		String title = getPara("title");
//		String des = getPara("des");
//		String type = getPara("type");
//		String leadingrole = getPara("leadingrole");
//		String ispublic = getPara("ispublic");
//		Works worksModel = getModel(Works.class);
//		Work workModel = getModel(Work.class);
//		boolean bool = false;
//		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//		worksModel.set("worksname", title);
//		worksModel.set("workstype", type);
//		worksModel.set("creativeprocess", 1);
//		worksModel.set("type", 0);
//		worksModel.set("ischeck", 0);
//		worksModel.set("cover", coverPath);
//		worksModel.set("leadingrole", leadingrole);
//		worksModel.set("describle", des);
//		worksModel.set("homepage", 0);
//		worksModel.set("istop", 0);
//		worksModel.set("ispublic", ispublic);
//		worksModel.set("comment", 0);
//		worksModel.set("pageviews", 0);
//		worksModel.set("collection", 0);
//		worksModel.set("praise", 0);
//		worksModel.set("maxnum", 1);
//		worksModel.set("releasedate", timestamp);
//		worksModel.set("creater", userId);
//		worksModel.set("creatername", userName);
//		worksModel.set("createtime", timestamp);
//		worksModel.set("modifier", userId);
//		worksModel.set("modifiername", userName);
//		worksModel.set("modifytime", timestamp);
//		bool = worksModel.save();
//		workModel.set("worksid", worksModel.get("worksid"));
//		workModel.set("worknum", 1);
//		workModel.set("workname", worksModel.get("worksname"));
//		workModel.set("workdes", worksModel.get("describle"));
//		workModel.set("ischeck", 0);
//		workModel.set("cover", coverPath);
//		workModel.set("comic", comicPath);
//		workModel.set("times", 0);
//		workModel.set("timesdes", "00:00");
//		workModel.set("isdisable", ispublic);
//		workModel.set("playtimes", 0);
//		workModel.set("creater", userId);
//		workModel.set("creatername", userName);
//		workModel.set("createtime", timestamp);
//		workModel.set("modifier", userId);
//		workModel.set("modifiername", userName);
//		workModel.set("modifytime", timestamp);
//		bool = workModel.save();
//		Map<String, Object> map = Maps.newHashMap();
//		if (bool) {
//			// 成功
//			map.put("success", 1);
//		} else {
//			map.put("success", 0);
//			map.put("message", "服务器错误！");
//		}
//		renderJson(map);
//	}

	public void addZhuanji() {
		String coverPath = FileLoadKit.uploadImg(this, "cover", "", 2000 * 1024 * 1024, "utf-8");
		User user = getSessionAttr("user");
		Integer userId = Integer.parseInt(String.valueOf(user.get("userid")));
		String userName = user.get("username");
		String title = getPara("title");
		String des = getPara("des");
		String type = getPara("type");
		String leadingrole = getPara("leadingrole");
		String ispublic = getPara("ispublic");
		Works worksModel = getModel(Works.class);
		boolean bool = false;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		worksModel.set("worksname", title);
		worksModel.set("workstype", type);
		worksModel.set("creativeprocess", 0);
		worksModel.set("type", 1);
		worksModel.set("ischeck", 0);
		worksModel.set("cover", coverPath);
		worksModel.set("leadingrole", leadingrole);
		worksModel.set("describle", des);
		worksModel.set("homepage", 0);
		worksModel.set("istop", 0);
		worksModel.set("ispublic", ispublic);
		worksModel.set("comment", 0);
		worksModel.set("pageviews", 0);
		worksModel.set("collection", 0);
		worksModel.set("praise", 0);
		worksModel.set("maxnum", 0);
		worksModel.set("releasedate", timestamp);
		worksModel.set("creater", userId);
		worksModel.set("creatername", userName);
		worksModel.set("createtime", timestamp);
		worksModel.set("modifier", userId);
		worksModel.set("modifiername", userName);
		worksModel.set("modifytime", timestamp);
		bool = worksModel.save();
		Map<String, Object> map = Maps.newHashMap();
		if (bool) {
			// 成功
			map.put("success", 1);
			map.put("worksid", String.valueOf(worksModel.get("worksid")));
		} else {
			map.put("success", 0);
			map.put("message", "服务器错误！");
		}
		renderJson(map);
	}

	public void addManhua() {
		String coverPath = FileLoadKit.uploadImg(this,"cover", "", 2000 * 1024 * 1024,"utf-8");
		User user = getSessionAttr("user");
		Integer userId = Integer.parseInt(String.valueOf(user.get("userid")));
		String userName = user.get("username");
		String title = getPara("title");
		String des = getPara("des");
		String type = getPara("type");
		String leadingrole = getPara("leadingrole");
		String ispublic = getPara("ispublic");
		Works worksModel = getModel(Works.class);
		boolean bool = false;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		worksModel.set("worksname", title);
		worksModel.set("workstype", type);
		worksModel.set("creativeprocess", 0);
		worksModel.set("type", 2);
		worksModel.set("ischeck", 0);
		worksModel.set("cover", coverPath);
		worksModel.set("leadingrole", leadingrole);
		worksModel.set("describle", des);
		worksModel.set("homepage", 0);
		worksModel.set("istop", 0);
		worksModel.set("ispublic", ispublic);
		worksModel.set("comment", 0);
		worksModel.set("pageviews", 0);
		worksModel.set("collection", 0);
		worksModel.set("praise", 0);
		worksModel.set("maxnum", 0);
		worksModel.set("releasedate", timestamp);
		worksModel.set("creater", userId);
		worksModel.set("creatername", userName);
		worksModel.set("createtime", timestamp);
		worksModel.set("modifier", userId);
		worksModel.set("modifiername", userName);
		worksModel.set("modifytime", timestamp);
		bool = worksModel.save();
		Map<String, Object> map = Maps.newHashMap();
		if (bool) {
			// 成功
			map.put("success", 1);
			map.put("worksid", String.valueOf(worksModel.get("worksid")));
		} else {
			map.put("success", 0);
			map.put("message", "服务器错误！");
		}
		renderJson(map);
	}

	public void uploadShipin() {
		Map<String, Object> map = Maps.newHashMap();
		Map<String, VideoFile> directorys;
		try {
			directorys = FileLoadKit.uploadVideo("comic", this, "works", 2000 * 1024 * 1024, "utf-8");
		} catch (Exception e) {
			map.put("success", 0);
			map.put("message", "上传失败！");
			renderJson(map);
			return;
		}
		User user = getSessionAttr("user");
		Integer userId = Integer.parseInt(String.valueOf(user.get("userid")));
		String userName = user.get("username");
	     //较验用户是否同意注册协议
        String checkbox = getPara("check");
        if(checkbox == null){
        	this.setAttr("user", user);
        	map.put("success", 0);
        	map.put("message", "未同意版权声明!");
        	renderJson(map);
			return;
        }
		
		if (MapKit.isBlank(directorys)) {
			map.put("success", 0);
			map.put("message", "上传失败！");
			renderJson(map);
			return;
		}
		String coverPath = null, comicPath = null;
		String status = "10";
		VideoFile videoFile = null;
		List<ImageFile> imageFiles = null;
		for (String fileName : directorys.keySet()) {
			videoFile = directorys.get(fileName);
			if(videoFile.isHandled()){
				status = "20";
			}
			comicPath = videoFile.getVirtualDirectory();
			imageFiles = videoFile.getImageFiles();
			if(CollectionKit.isNotBlank(imageFiles)){
				coverPath = imageFiles.get(0).getVirtualDirectory();
			}
		}
		String ispublic = getPara("ispublic");
		String worksid = getPara("worksid");
		String worknum = getPara("worknum");
		String title = "第"+worknum+"集";
		Works worksModel = getModel(Works.class).findById(worksid);
		Work workModel = getModel(Work.class);
		boolean bool = false;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String maxnum = String.valueOf(worksModel.get("maxnum"));
		if (StrKit.isBlank(maxnum)) {
			maxnum = "0";
		}
		worksModel.set("maxnum", Integer.parseInt(maxnum) + 1);
		worksModel.set("updatetime", timestamp);
		worksModel.set("releasedate", timestamp);
		worksModel.set("modifier", userId);
		worksModel.set("modifiername", userName);
		worksModel.set("modifytime", timestamp);
		bool = worksModel.update();
		if (!bool) {
			// 失败
			map.put("success", 0);
			map.put("message", "服务器错误！");
			renderJson(map);
			return;
		}
		workModel.set("status", status);
		workModel.set("worksid", worksModel.get("worksid"));
		workModel.set("worknum", worknum);
		workModel.set("workname", title);
		workModel.set("workdes", "");
		workModel.set("cover", coverPath);
		workModel.set("comic", comicPath);
		workModel.set("times", 0);
		workModel.set("timesdes", "00:00");
		workModel.set("isdisable", ispublic);
		workModel.set("playtimes", 0);
		workModel.set("creater", userId);
		workModel.set("creatername", userName);
		workModel.set("createtime", timestamp);
		workModel.set("modifier", userId);
		workModel.set("modifiername", userName);
		workModel.set("modifytime", timestamp);
		bool = workModel.save();
		if (bool) {
			// 成功
			map.put("success", 1);
		} else {
			map.put("success", 0);
			map.put("message", "服务器错误！");
		}
		renderJson(map);
		return;
	}

	public void uploadZhangjie() {
		String coverPath = FileLoadKit.uploadImg(this, "cover", "", 2000 * 1024 * 1024, "utf-8");
		String comicPath = FileLoadKit.uploadImg(this,"comic", "", 2000 * 1024 * 1024,"utf-8");
		Map<String, Object> map = Maps.newHashMap();
		User user = getSessionAttr("user");
		Integer userId = Integer.parseInt(String.valueOf(user.get("userid")));
		String userName = user.get("username");

		String ispublic = getPara("ispublic");
		String worksid = getPara("worksid");
		String checkbox = getPara("check");
		String worknum = getPara("worknum");
		String title = getPara("title");
		
        if(checkbox == null){
        	this.setAttr("user", user);
        	map.put("success", 0);
        	map.put("message", "未同意版权声明!");
        	renderJson(map);
			return;
        }
		Works worksModel = getModel(Works.class).findById(worksid);
		Work workModel = getModel(Work.class);
		boolean bool = false;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String maxnum = String.valueOf(worksModel.get("maxnum"));
		if (StrKit.isBlank(maxnum)) {
			maxnum = "0";
		}
		worksModel.set("maxnum", Integer.parseInt(maxnum) + 1);
		worksModel.set("updatetime", timestamp);
		worksModel.set("releasedate", timestamp);
		worksModel.set("modifier", userId);
		worksModel.set("modifiername", userName);
		worksModel.set("modifytime", timestamp);
		bool = worksModel.update();
		if (!bool) {
			// 失败
			map.put("success", 0);
			map.put("message", "服务器错误！");
			renderJson(map);
			return;
		}
		workModel.set("worksid", worksModel.get("worksid"));
		workModel.set("worknum", Integer.parseInt(worknum));
		workModel.set("workname", title);
		workModel.set("workdes", "");
		workModel.set("cover", coverPath);
		workModel.set("comic", comicPath);
		workModel.set("status", 20);
		workModel.set("times", 0);
		workModel.set("timesdes", "");
		workModel.set("isdisable", ispublic);
		workModel.set("playtimes", 0);
		workModel.set("creater", userId);
		workModel.set("creatername", userName);
		workModel.set("createtime", timestamp);
		workModel.set("modifier", userId);
		workModel.set("modifiername", userName);
		workModel.set("modifytime", timestamp);
		bool = workModel.save();
		if (bool) {
			// 成功
			map.put("success", 1);
		} else {
			map.put("success", 0);
			map.put("message", "服务器错误！");
		}
		renderJson(map);
		return;
	}

	public void editShipin() {
		String coverPath = FileLoadKit.uploadImg(this,"cover", "", 2000 * 1024 * 1024,"utf-8");
		Map<String, Object> map = Maps.newHashMap();
		User user = getSessionAttr("user");
		Integer userId = Integer.parseInt(String.valueOf(user.get("userid")));
		String userName = user.get("username");
		
		String worksid = getPara("worksid");
		String title = getPara("title");
		String leadingrole = getPara("leadingrole");
		String des = getPara("des");
		String ispublic = getPara("ispublic");
		Works worksModel = getModel(Works.class).findById(worksid);
		Work workModel = getModel(Work.class);
		List<Work> works = getModel(Work.class).getWorkByWorksID(worksid);
		if (!works.isEmpty()) {
			workModel = works.get(0);
		}
		boolean bool = false;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		if (!StrKit.isBlank(coverPath)) {
			String oldcover = worksModel.get("cover");
			File coverFile = new File(oldcover);
			if (coverFile.exists()) {
				coverFile.delete();
			}
			worksModel.set("cover", coverPath);

			String oldcover1 = workModel.get("cover");
			File coverFile1 = new File(oldcover1);
			if (coverFile1.exists()) {
				coverFile1.delete();
			}
			workModel.set("cover", coverPath);
		}
		if (!StrKit.isBlank(title)) {
			worksModel.set("worksname", title);
			workModel.set("workname", title);
		}
		if (!StrKit.isBlank(leadingrole)) {
			worksModel.set("leadingrole", leadingrole);
		}
		worksModel.set("describle", des);
		workModel.set("workdes", des);
		if (!StrKit.isBlank(ispublic)) {
			worksModel.set("ispublic", ispublic);
			workModel.set("isdisable", ispublic);
		}
		worksModel.set("modifier", userId);
		worksModel.set("modifiername", userName);
		worksModel.set("modifytime", timestamp);

		bool = worksModel.update();
		if (!bool) {
			map.put("success", 0);
			map.put("message", "服务器错误！");
			renderJson(map);
			return;
		}
		workModel.set("modifier", userId);
		workModel.set("modifiername", userName);
		workModel.set("modifytime", timestamp);
		bool = workModel.update();
		if (bool) {
			// 成功
			map.put("success", 1);
		} else {
			map.put("success", 0);
			map.put("message", "服务器错误！");
		}
		renderJson(map);
		return;
	}

	public void editZhuanji() {
		String coverPath = FileLoadKit.uploadImg(this,"cover", "", 2000 * 1024 * 1024,"utf-8");
		Map<String, Object> map = Maps.newHashMap();
		User user = getSessionAttr("user");
		Integer userId = Integer.parseInt(String.valueOf(user.get("userid")));
		String userName = user.get("username");

		String worksid = getPara("worksid");
		String title = getPara("title");
		String leadingrole = getPara("leadingrole");
		String des = getPara("des");
		String ispublic = getPara("ispublic");
		Works worksModel = getModel(Works.class).findById(worksid);
		boolean bool = false;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		if (!StrKit.isBlank(coverPath)) {
			String oldcover = worksModel.get("cover");
			File coverFile = new File(oldcover);
			if (coverFile.exists()) {
				coverFile.delete();
			}
			worksModel.set("cover", coverPath);
		}
		if (!StrKit.isBlank(title)) {
			worksModel.set("worksname", title);
		}
		if (!StrKit.isBlank(leadingrole)) {
			worksModel.set("leadingrole", leadingrole);
		}
		worksModel.set("describle", des);
		if (!StrKit.isBlank(ispublic)) {
			worksModel.set("ispublic", ispublic);
		}
		worksModel.set("modifier", userId);
		worksModel.set("modifiername", userName);
		worksModel.set("modifytime", timestamp);
		bool = worksModel.update();
		if (bool) {
			// 成功
			map.put("success", 1);
		} else {
			map.put("success", 0);
			map.put("message", "服务器错误！");
		}
		renderJson(map);
		return;
	}

	public void editManhua() {
		String coverPath = FileLoadKit.uploadImg(this,"cover", "", 2000 * 1024 * 1024,"utf-8");
		Map<String, Object> map = Maps.newHashMap();
		User user = getSessionAttr("user");
		Integer userId = Integer.parseInt(String.valueOf(user.get("userid")));
		String userName = user.get("username");
		
		String worksid = getPara("worksid");
		String title = getPara("title");
		String leadingrole = getPara("leadingrole");
		String des = getPara("des");
		String ispublic = getPara("ispublic");
		Works worksModel = getModel(Works.class).findById(worksid);
		boolean bool = false;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		if (!StrKit.isBlank(coverPath)) {
			String oldcover = worksModel.get("cover");
			File coverFile = new File(oldcover);
			if (coverFile.exists()) {
				coverFile.delete();
			}
			worksModel.set("cover", coverPath);
		}
		if (!StrKit.isBlank(title)) {
			worksModel.set("worksname", title);
		}
		if (!StrKit.isBlank(leadingrole)) {
			worksModel.set("leadingrole", leadingrole);
		}
		worksModel.set("describle", des);
		if (!StrKit.isBlank(ispublic)) {
			worksModel.set("ispublic", ispublic);
		}
		worksModel.set("modifier", userId);
		worksModel.set("modifiername", userName);
		worksModel.set("modifytime", timestamp);
		bool = worksModel.update();
		if (bool) {
			// 成功
			map.put("success", 1);
		} else {
			map.put("success", 0);
			map.put("message", "服务器错误！");
		}
		renderJson(map);
		return;
	}

	public void editZhuanjiShipin() {
		Map<String, Object> map = Maps.newHashMap();
		boolean bool = false;
		try {
			String coverPath = FileLoadKit.uploadImg(this,"cover", "",2000 * 1024 * 1024, "utf-8");
			User user = getSessionAttr("user");
			Integer userId = Integer
					.parseInt(String.valueOf(user.get("userid")));
			String userName = user.get("username");
			String workid = getPara("workid");
			String title = getPara("title");
			String des = getPara("des");
			String ispublic = getPara("ispublic");

			Work workModel = getModel(Work.class).findById(workid);
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			if (!StrKit.isBlank(coverPath)) {
				String oldcover1 = workModel.get("cover");
				if (!StrKit.isBlank(oldcover1)) {
					File coverFile1 = new File(oldcover1);
					if (coverFile1.exists()) {
						coverFile1.delete();
					}
				}
				workModel.set("cover", coverPath);
			}
			if (!StrKit.isBlank(title)) {
				workModel.set("workname", title);
			}
			workModel.set("workdes", des);
			if (!StrKit.isBlank(ispublic)) {
				workModel.set("isdisable", ispublic);
			}
			workModel.set("modifier", userId);
			workModel.set("modifiername", userName);
			workModel.set("modifytime", timestamp);
			bool = workModel.update();
			if (bool) {
				// 成功
				map.put("success", 1);
			} else {
				map.put("success", 0);
				map.put("message", "服务器错误！");
			}
			renderJson(map);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", 0);
			map.put("message", "服务器错误！");
			renderJson(map);
			return;
		}

	}

	public void editZhangjie() {
		Map<String, Object> map = Maps.newHashMap();
		boolean bool = false;
		try {
			String coverPath = FileLoadKit.uploadImg(this,"cover", "",2000 * 1024 * 1024, "utf-8");
			User user = getSessionAttr("user");
			Integer userId = Integer
					.parseInt(String.valueOf(user.get("userid")));
			String userName = user.get("username");
			String workid = getPara("workid");
			String title = getPara("title");
			String des = getPara("des");
			String ispublic = getPara("ispublic");

			Work workModel = getModel(Work.class).findById(workid);
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			if (!StrKit.isBlank(coverPath)) {
				String oldcover1 = workModel.get("cover");
				if (!StrKit.isBlank(oldcover1)) {
					File coverFile1 = new File(oldcover1);
					if (coverFile1.exists()) {
						coverFile1.delete();
					}
				}
				workModel.set("cover", coverPath);
			}
			if (!StrKit.isBlank(title)) {
				workModel.set("workname", title);
			}
			workModel.set("workdes", des);
			if (!StrKit.isBlank(ispublic)) {
				workModel.set("isdisable", ispublic);
			}
			workModel.set("modifier", userId);
			workModel.set("modifiername", userName);
			workModel.set("modifytime", timestamp);
			bool = workModel.update();
			if (bool) {
				// 成功
				map.put("success", 1);
			} else {
				map.put("success", 0);
				map.put("message", "服务器错误！");
			}
			renderJson(map);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", 0);
			map.put("message", "服务器错误！");
			renderJson(map);
			return;
		}

	}

	/**
	 * 根据作品ID获取某作品
	 */
	public void getWorksById() {
		Works worksModel = getModel(Works.class);
		Works works = worksModel.findById(getParaToInt("id"));
		setAttr("works", works);
		render("/works/editWorks.htm");
	}

	/**
	 * 根据集数Id获取信息
	 */
	public void getWorkById() {
		Work workModel = getModel(Work.class);
		Work work = workModel.findById(getParaToInt("id"));
		setAttr("work", work);
		render("/works/editWork.htm");
	}

	public void showPage() {
		try {
			User user = getSessionAttr("user");
			String flag = getPara("flag");
			String type = getPara("type");
			String workstype = getPara("workstype");
			String pageNumberStr = getPara("pageNumber");
			Integer pageNumber = 1;
			if (!StrKit.isBlank(pageNumberStr)) {
				pageNumber = Integer.parseInt(pageNumberStr);
			}
			String pageSizeStr = getPara("pageSize");
			Integer pageSize = 10;
			if (!StrKit.isBlank(pageSizeStr)) {
				pageSize = Integer.parseInt(pageSizeStr);
			}
			if (StrKit.isBlank(flag)) {
				flag = type;
			}
			WorksType worksTypeModel = getModel(WorksType.class);
			Works worksModel = getModel(Works.class);
			List<WorksType> worksTypes = worksTypeModel.getWorksTypes(type);
			if ("0".equals(flag)) {
				// 0是跳转视频管理
				Page<Works> page = worksModel
						.getWorksInfoPage3(workstype, type,
								String.valueOf(user.get("userid")), pageNumber,
								pageSize);
				List<Map<String, Object>> data = ParseDemoKit.worksParse(page
						.getList());
				@SuppressWarnings({ "rawtypes", "unchecked" })
				Page<List<Map<String, Object>>> pageWorks = new Page(data,
						page.getPageNumber(), page.getPageSize(),
						page.getTotalPage(), page.getTotalRow());
				setAttr("workstype", workstype);
				setAttr("worksTypes", worksTypes);
				setAttr("pageWorks", pageWorks);
				render("/worksManage/shipinManage.html");
			} else if ("1".equals(flag)) {
				// 1是跳转专辑管理
				Page<Works> page = worksModel
						.getWorksInfoPage3(workstype, type,
								String.valueOf(user.get("userid")), pageNumber,
								pageSize);
				List<Map<String, Object>> data = ParseDemoKit.worksParse(page
						.getList());
				@SuppressWarnings({ "unchecked", "rawtypes" })
				Page<List<Map<String, Object>>> pageWorks = new Page(data,
						page.getPageNumber(), page.getPageSize(),
						page.getTotalPage(), page.getTotalRow());
				setAttr("workstype", workstype);
				setAttr("worksTypes", worksTypes);
				setAttr("pageWorks", pageWorks);
				render("/worksManage/zhuanjiManage.html");
			} else if ("2".equals(flag)) {
				// 2是跳转漫画管理
				Page<Works> page = worksModel
						.getWorksInfoPage3(workstype, type,
								String.valueOf(user.get("userid")), pageNumber,
								pageSize);
				List<Map<String, Object>> data = ParseDemoKit.worksParse(page
						.getList());
				@SuppressWarnings({ "rawtypes", "unchecked" })
				Page<List<Map<String, Object>>> pageWorks = new Page(data,
						page.getPageNumber(), page.getPageSize(),
						page.getTotalPage(), page.getTotalRow());
				setAttr("workstype", workstype);
				setAttr("worksTypes", worksTypes);
				setAttr("pageWorks", pageWorks);
				render("/worksManage/manhuaManage.html");
			} else if ("12".equals(flag)) {
				// 12漫画详细页
				String worksid = getPara("worksid");
				String workpageNumberStr = getPara("workpageNumber");
				Integer workpageNumber = 1;
				if (!StrKit.isBlank(workpageNumberStr)) {
					workpageNumber = Integer.parseInt(workpageNumberStr);
				}
				worksModel = worksModel.findById(Integer.parseInt(worksid));
				worksTypeModel = worksTypeModel.findById(worksModel
						.get("workstype"));
				Page<Work> pageWork = getModel(Work.class).getWorkByWorksID(
						Integer.parseInt(worksid), workpageNumber, pageSize);
				setAttr("type", type);
				setAttr("workstype", workstype);
				setAttr("pageNumber", pageNumber);
				setAttr("works", worksModel);
				setAttr("worksType", worksTypeModel);
				setAttr("pageWork", pageWork);
				render("/worksManage/detailmanhua.html");
			} else if ("13".equals(flag)) {
				// 11 是跳转 添加漫画页面
				setAttr("type", type);
				setAttr("worksTypes", worksTypes);
				setAttr("workstype", workstype);
				setAttr("pageNumber", pageNumber);
				render("/worksManage/addmanhua.html");
			} else if ("14".equals(flag)) {
				// 14 是跳转 修改漫画页面
				String worksid = getPara("worksid");
				String workpageNumberStr = getPara("workpageNumber");
				Integer workpageNumber = 1;
				if (!StrKit.isBlank(workpageNumberStr)) {
					workpageNumber = Integer.parseInt(workpageNumberStr);
				}
				worksModel = worksModel.findById(Integer.parseInt(worksid));
				worksTypeModel = worksTypeModel.findById(worksModel
						.get("workstype"));
				setAttr("type", type);
				setAttr("workstype", workstype);
				setAttr("pageNumber", pageNumber);
				setAttr("workpageNumber", workpageNumber);

				setAttr("works", worksModel);
				setAttr("worksType", worksTypeModel);
				render("/worksManage/editmanhua.html");
			} else if ("15".equals(flag)) {
				// 15是跳转 上传章节页面
				String workpageNumberStr = getPara("workpageNumber");
				Integer workpageNumber = 1;
				if (!StrKit.isBlank(workpageNumberStr)) {
					workpageNumber = Integer.parseInt(workpageNumberStr);
				}
				String worksid = getPara("worksid");
				List<Integer> lackNum = getModel(Work.class).getLackNum(worksid);
				
				setAttr("lackNum", lackNum);
				setAttr("type", type);
				setAttr("workstype", workstype);
				setAttr("workpageNumber", workpageNumber);
				setAttr("pageNumber", pageNumber);
				setAttr("worksid", worksid);
				render("/worksManage/uploadzhangjie.html");
			} else if ("16".equals(flag)) {
				// 16 是跳转 修改章节页面
				String workpageNumberStr = getPara("workpageNumber");
				Integer workpageNumber = 1;
				if (!StrKit.isBlank(workpageNumberStr)) {
					workpageNumber = Integer.parseInt(workpageNumberStr);
				}
				String worksid = getPara("worksid");
				String workid = getPara("workid");
				Work workModel = getModel(Work.class).findById(workid);

				setAttr("type", type);
				setAttr("workpageNumber", workpageNumber);
				setAttr("pageNumber", pageNumber);
				setAttr("worksid", worksid);

				setAttr("work", workModel);
				render("/worksManage/editzhangjie.html");
			} else if ("10".equals(flag)) {
				// 10 是跳转 添加视频页面
				setAttr("type", type);
				setAttr("worksTypes", worksTypes);
				setAttr("workstype", workstype);
				setAttr("pageNumber", pageNumber);
				render("/worksManage/addshipin.html");
			} else if ("11".equals(flag)) {
				// 11 是跳转 添加专辑页面
				setAttr("type", type);
				setAttr("worksTypes", worksTypes);
				setAttr("workstype", workstype);
				setAttr("pageNumber", pageNumber);
				render("/worksManage/addzhuanji.html");
			} else if ("20".equals(flag)) {
				// 20 是跳转 编辑视频页面
				String worksid = getPara("worksid");
				worksModel = worksModel.findById(worksid);
				Work workModel = getModel(Work.class);
				List<Work> works = workModel.getWorkByWorksID(worksid);
				worksTypeModel = worksTypeModel.findById(worksModel
						.get("workstype"));
				if (!works.isEmpty()) {
					workModel = works.get(0);
				}
				setAttr("type", type);
				setAttr("pageNumber", pageNumber);
				setAttr("worksType", worksTypeModel);
				setAttr("workstype", workstype);
				setAttr("works", worksModel);
				setAttr("work", workModel);
				render("/worksManage/editshipin.html");
			} else if ("21".equals(flag)) {
				// 21 是跳转 专辑详细页面
				String worksid = getPara("worksid");
				String workpageNumberStr = getPara("workpageNumber");
				Integer workpageNumber = 1;
				if (!StrKit.isBlank(workpageNumberStr)) {
					workpageNumber = Integer.parseInt(workpageNumberStr);
				}
				worksModel = worksModel.findById(Integer.parseInt(worksid));
				worksTypeModel = worksTypeModel.findById(worksModel
						.get("workstype"));
				Page<Work> pageWork = getModel(Work.class).getWorkByWorksID(
						Integer.parseInt(worksid), workpageNumber, pageSize);
				setAttr("type", type);
				setAttr("workstype", workstype);
				setAttr("pageNumber", pageNumber);
				setAttr("works", worksModel);
				setAttr("worksType", worksTypeModel);
				setAttr("pageWork", pageWork);
				render("/worksManage/detailzhuanji.html");
			} else if ("22".equals(flag)) {
				// 22 是跳转 编辑专辑页面
				String worksid = getPara("worksid");
				String workpageNumberStr = getPara("workpageNumber");
				Integer workpageNumber = 1;
				if (!StrKit.isBlank(workpageNumberStr)) {
					workpageNumber = Integer.parseInt(workpageNumberStr);
				}
				worksModel = worksModel.findById(Integer.parseInt(worksid));
				worksTypeModel = worksTypeModel.findById(worksModel
						.get("workstype"));
				setAttr("type", type);
				setAttr("workstype", workstype);
				setAttr("pageNumber", pageNumber);
				setAttr("workpageNumber", workpageNumber);

				setAttr("works", worksModel);
				setAttr("worksType", worksTypeModel);
				render("/worksManage/editzhuanji.html");
			} else if ("23".equals(flag)) {
				// 23 是跳转 编辑专辑-->视频页面
				String workpageNumberStr = getPara("workpageNumber");
				Integer workpageNumber = 1;
				if (!StrKit.isBlank(workpageNumberStr)) {
					workpageNumber = Integer.parseInt(workpageNumberStr);
				}
				String worksid = getPara("worksid");
				String workid = getPara("workid");
				Work workModel = getModel(Work.class).findById(workid);

				setAttr("type", type);
				setAttr("workpageNumber", workpageNumber);
				setAttr("pageNumber", pageNumber);
				setAttr("worksid", worksid);

				setAttr("work", workModel);
				render("/worksManage/editzhuanjishipin.html");
			} else if ("24".equals(flag)) {
				// 23 是跳转 添加专辑->视频 页面
				String workpageNumberStr = getPara("workpageNumber");
				Integer workpageNumber = 1;
				if (!StrKit.isBlank(workpageNumberStr)) {
					workpageNumber = Integer.parseInt(workpageNumberStr);
				}
				String worksid = getPara("worksid");
				List<Integer> lackNum = getModel(Work.class).getLackNum(worksid);
				setAttr("type", type);
				setAttr("workstype", workstype);
				setAttr("workpageNumber", workpageNumber);
				setAttr("pageNumber", pageNumber);
				setAttr("lackNum", lackNum);
				setAttr("worksid", worksid);
				render("/worksManage/uploadshipin.html");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showAdminPage() {
		String type = getPara("type");
		String flag = getPara("flag");
		String workstype = getPara("workstype");
		String pageNumberStr = getPara("pageNumber");
		Integer pageNumber = 1;
		if (!StrKit.isBlank(pageNumberStr)) {
			pageNumber = Integer.parseInt(pageNumberStr);
		}
		String pageSizeStr = getPara("pageSize");
		Integer pageSize = 10;
		if (!StrKit.isBlank(pageSizeStr)) {
			pageSize = Integer.parseInt(pageSizeStr);
		}
		WorksType worksTypeModel = getModel(WorksType.class);
		Works worksModel = getModel(Works.class);
		List<WorksType> worksTypes = worksTypeModel.getWorksTypes(type);
		if ("0".equals(flag)) {
			// 查询未审核的
			Page<Works> pagedata = worksModel.getWorksNotCheck(workstype, type,
					pageNumber, pageSize);
			@SuppressWarnings({ "unchecked", "rawtypes" })
			Page<List<Map<String, Object>>> pageWorks = new Page(
					parseAdminZhuanji(pagedata.getList()), pageNumber, pageSize,
					pagedata.getTotalPage(), pagedata.getTotalRow());
			setAttr("pageWorks", pageWorks);
			setAttr("worksTypes", worksTypes);
			setAttr("workstype", workstype);
			setAttr("flag", flag);
			if ("2".equals(type)) {
				render("/admin/admin_works_manhua.html");
				return;
			} else {
				render("/admin/admin_works_zhuanji.html");
				return;
			}
		} else if ("1".equals(flag)) {
			// 查询首页5个轮播
			Page<Works> pagedata = worksModel.getHomePage(workstype, pageNumber,
					pageSize);
			@SuppressWarnings({ "unchecked", "rawtypes" })
			Page<List<Map<String, Object>>> pageWorks = new Page(
					parseAdminZhuanji(pagedata.getList()), pageNumber, pageSize,
					pagedata.getTotalPage(), pagedata.getTotalRow());
			setAttr("pageWorks", pageWorks);
			setAttr("worksTypes", worksTypes);
			setAttr("workstype", workstype);
			setAttr("flag", flag);
			render("/admin/admin_works_zhuanji.html");
		} else if ("2".equals(flag)) {
			// 查询 全部
			Page<Works> pagedata = worksModel.getWorksInfoPage2(workstype, type,
					pageNumber, pageSize);
			@SuppressWarnings({ "unchecked", "rawtypes" })
			Page<List<Map<String, Object>>> pageWorks = new Page(
					parseAdminZhuanji(pagedata.getList()), pageNumber, pageSize,
					pagedata.getTotalPage(), pagedata.getTotalRow());
			setAttr("pageWorks", pageWorks);
			setAttr("worksTypes", worksTypes);
			setAttr("workstype", workstype);
			setAttr("flag", flag);
			if ("2".equals(type)) {
				render("/admin/admin_works_manhua.html");
				return;
			} else {
				render("/admin/admin_works_zhuanji.html");
				return;
			}
		}
	}

	public void yesChecked() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String worksid = "";
			String ids = getPara("ids");
			String[] idsStr = ids.split(",");
			for (String id : idsStr) {
				Work workModel = getModel(Work.class).findById(id);
				worksid = String.valueOf(workModel.get("worksid"));
				if (workModel.isNotEmpty()) {
					workModel.set("status", 30);
					workModel.update();
				}
			}
			Works worksModel = getModel(Works.class).findById(worksid);
			worksModel.set("ischeck", "1");
			worksModel.update();
			map.put("success", 1);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", 0);
		}
		renderJson(map);
		return;
	}

	public void yesCheckWorks() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String worksid = getPara("worksid");
			if (StrKit.isBlank(worksid)) {
				map.put("success", 0);
				map.put("message", "参数为空！");
				renderJson(map);
				return;
			}
			Works worksModel = getModel(Works.class).findById(worksid);
			if (!worksModel.isNotEmpty()) {
				map.put("success", 0);
				map.put("message", "找不到对象");
				renderJson(map);
				return;
			}

			worksModel.set("ischeck", 1);
			boolean bool = worksModel.update();
			if (bool) {
				map.put("success", 1);
				renderJson(map);
				return;
			} else {
				map.put("success", 0);
				map.put("message", "审核失败");
				renderJson(map);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", 1);
			map.put("message", "服务器错误");
			renderJson(map);
			return;
		}
	}

	public void upranking() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String type = getPara("type");
			String flag = getPara("flag");
			String worksid = getPara("worksid");
			Works worksModel = getModel(Works.class).findById(worksid);
			if (!worksModel.isNotEmpty()) {
				map.put("success", 0);
				renderJson(map);
				return;
			}
			if ("0".equals(flag)) {
				// 首页置顶
				Integer homepage = worksModel.getMaxHomePageValue(type);
				worksModel.set("homepage", homepage + 1);
			}
			if ("1".equals(flag)) {
				// 类型置顶
				Integer istop = worksModel.getMaxTopValue(type);
				worksModel.set("istop", istop + 1);
			}
			if ("10".equals(flag)) {
				worksModel.set("homepage", 0);
			}
			if ("11".equals(flag)) {
				worksModel.set("istop", 0);
			}
			worksModel.update();
			map.put("success", 1);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", 0);
		}
		renderJson(map);
		return;
	}

	private List<Map<String, Object>> parseAdminZhuanji(List<Works> workss) {
		List<Map<String, Object>> resultMap = Lists.newArrayList();
		try {
			for (Works works : workss) {
				if (works == null)
					continue;
				Map<String, Object> map = new HashMap<String, Object>();
				Object worksid = works.get("worksid");
				map.put("worksid", worksid);
				String worksname = works.get("worksname");
				if (worksname.length() > 12) {
					worksname = worksname.substring(0, 12);
				}
				map.put("worksname", worksname);
				map.put("workstype", works.get("workstype"));
				map.put("ischeck", works.get("ischeck"));
				map.put("istop", works.get("istop"));
				map.put("homepage", works.get("homepage"));
				map.put("praise", works.get("praise"));
				map.put("pageviews", works.get("pageviews"));
				map.put("comment", works.get("comment"));
				map.put("pageviews", works.get("pageviews"));
				String cover = works.get("cover");
				map.put("cover", cover);
				String playUrl = "";
				String detailUrl = "";
				Integer type = works.getInt("type");
				if (type == 1) {
					// 1专辑
					playUrl = "cartoon/playVideo?id=" + worksid;
					detailUrl = "cartoon/showDetail?id=" + worksid;
				} else if (type == 2) {
					// 2漫画
					playUrl = "manhua/playVideo?id=" + worksid;
					detailUrl = "manhua/showDetail?id=" + worksid;
				}
				map.put("playUrl", playUrl);
				map.put("detailUrl", detailUrl);
				String typeUrl = "cartoon/showWorksList?wtype="
						+ works.get("workstype");
				map.put("typeUrl", typeUrl);
				String describle = works.get("describle");
				if (describle.length() > 20) {
					describle = describle.substring(0, 20) + "...";
				}
				map.put("describle", describle);
				map.put("creater", works.get("createtime"));
				map.put("creatername", works.get("creatername"));
				map.put("createtime", works.get("createtime"));
				map.put("modifier", works.get("modifier"));
				map.put("modifiername", works.get("modifiername"));
				map.put("updatetime", works.get("updatetime"));
				List<Work> workList = getModel(Work.class).getNotCheckWork(
						works.get("worksid"));
				map.put("workNotCheck", workList);
				resultMap.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	public void fileExists() {
		String workid = getPara("workid");
		Work workModel = getModel(Work.class).findById(workid);
		if (!workModel.isNotEmpty()) {
			renderJson("success", 0);
			return;
		}
		String url = workModel.get("comic");
		File file = FileKit.getDescFile(url);
		if (!file.exists()) {
			renderJson("success", 0);
			return;
		}
		renderJson("success", 1);
		return;
	}
	/**
	 * 视频收藏
	 */
	public void collection(){
		String workid = getPara("workid");
		Work worodel = getModel(Work.class).findById(workid);
		Long worksid = worodel.getLong("worksid");
		Works worksdel = getModel(Works.class).findById(worksid);
		Favourite fav = getModel(Favourite.class);
		//查找是否收藏过
		Long count = fav.countCollection(workid, worksid);
		if(count==0){
			User user = getSessionAttr("user");
			Long userId = user.getLong("userid");
			fav.set("userid", userId);
			fav.set("cartoon_id", worksid);
			fav.set("cartoon_name", worksdel.getStr("worksname"));
			fav.set("section_id", workid);
			fav.set("section_name", worodel.getStr("workname"));
			fav.set("comic", worodel.getStr("comic"));
			fav.save();
		}
		renderJson("success", 1);
		return;
		
	}
	public void downloadFile() {
		try {
			User user = getSessionAttr("user");
			Integer userId = Integer.parseInt(String.valueOf(user.get("userid")));

			String workid = getPara("workid");
			Work workModel = getModel(Work.class).findById(workid);
			if (!workModel.isNotEmpty()) {
				return;
			}
			String url = workModel.get("comic");
			File descFile = FileKit.getDescFile(url);
			if (!descFile.exists()) {
				return;
			}
			DownLoad downloadModel = getModel(DownLoad.class);
			Works worksModel = getModel(Works.class).findById(
					workModel.get("worksid"));
			if (worksModel.isNotEmpty()) {
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				downloadModel.set("userid", userId);
				downloadModel.set("cartoon_name", worksModel.get("worksname"));
				downloadModel.set("cartoon_id", worksModel.get("worksid"));
				downloadModel.set("section_name", workModel.get("workname"));
				downloadModel.set("download_count", 1);
				downloadModel.set("create_time", timestamp);
				downloadModel.set("creater", userId);
				downloadModel.set("modify_time", timestamp);
				downloadModel.set("modifier", userId);
				downloadModel.save();
			}
			renderFile(descFile);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getWorksTypes() {
		String type = getPara("type");
		WorksType worksTypeModel = getModel(WorksType.class);
		List<WorksType> worksTypes = worksTypeModel.getWorksTypes(type);
		renderJson(worksTypes);
	}
	
	public void addPlay(){
		return;
	}
}
