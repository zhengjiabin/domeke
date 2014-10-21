package com.domeke.app.controller;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.CodeTable;
import com.domeke.app.model.User;
import com.domeke.app.model.UserRole;
import com.domeke.app.model.Work;
import com.domeke.app.model.Works;
import com.domeke.app.model.WorksType;
import com.domeke.app.route.ControllerBind;
import com.domeke.app.utils.CodeKit;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.aop.Before;
import com.jfinal.kit.ParseDemoKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * 作品model控制器
 * 
 * @author chenhailin
 *
 */
@ControllerBind(controllerKey = "/works")
@Before(LoginInterceptor.class)
public class WorksController extends FilesLoadController {

	/**
	 * to管理界面
	 */
	public void goToManager() {
		render("/admin/admin_works.html");
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
		int id = getParaToInt("id");
		Work workModel = getModel(Work.class);
		Work work = workModel.findById(id);
		boolean bool = false;
		if (work != null) {
			bool = work.delete();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (bool) {
			map.put("success", "1");
		} else {
			map.put("success", "0");
		}
		renderJson(map);
	}


	/**
	 * 查询出所有作品
	 */
	public void queryAllWorksInfo() {
		Works worksModel = getModel(Works.class);
		List<Works> workslist = worksModel.queryAllWorksInfo();
		CacheKit.remove("workslist", workslist);
		this.setAttr("workslist", workslist);
	}

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

	/**
	 * 根据作品类型获取作品信息
	 */
	public void getWorksInfoByType() {
		String workstype = getPara("workstype");
		String pageIndex = getPara("pageIndex");
		String pageSize = getPara("pageSize");
		if (StrKit.isBlank(pageIndex)) {
			pageIndex = "1";
		}
		if (StrKit.isBlank(pageSize)) {
			pageSize = "5";
		}
		Works worksModel = getModel(Works.class);
		Page<Works> workslist = worksModel.getWorksInfoPage(workstype, Integer.parseInt(pageIndex),
				Integer.parseInt(pageSize));
		renderJson(workslist);
	}

	/**
	 * 根据作品类型分页获取作品信息
	 */
	public List<Works> getWorksInfoByTypePage(String workstype, Integer pageIndex, Integer pageSize) {
		Works worksModel = getModel(Works.class);
		List<Works> workslist = worksModel.getWorksInfoByTypePage(workstype, pageIndex, pageSize);
		return workslist;
	}

	// --------------个人中心-->我的作品---------------
	public void addShipin() {
		User user = getSessionAttr("user");
		Integer userId = Integer.parseInt(String.valueOf(user.get("userid")));
		String userName = user.get("username");
		
		String coverPath = upLoadFileDealPath("cover", "", 2000 * 1024 * 1024, "utf-8");
		String comicPath = upLoadVideo("comic", "", 2000 * 1024 * 1024, "utf-8");
		String title = getPara("title");
		String des = getPara("des");
		String type = getPara("type");
		String leadingrole = getPara("leadingrole");
		String ispublic = getPara("ispublic");
		Works worksModel = getModel(Works.class);
		Work workModel = getModel(Work.class);
		boolean bool = false;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		worksModel.set("worksname", title);
		worksModel.set("workstype", type);
		worksModel.set("creativeprocess", 1);
		worksModel.set("type", 0);
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
		worksModel.set("maxnum", 1);
		worksModel.set("releasedate", timestamp);
		worksModel.set("creater", userId);
		worksModel.set("creatername", userName);
		worksModel.set("createtime", timestamp);
		worksModel.set("modifier", userId);
		worksModel.set("modifiername", userName);
		worksModel.set("modifytime", timestamp);
		bool = worksModel.save();
		workModel.set("worksid", worksModel.get("worksid"));
		workModel.set("worknum", 1);
		workModel.set("workname", worksModel.get("worksname"));
		workModel.set("workdes", worksModel.get("describle"));
		workModel.set("ischeck", 0);
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
		Map<String, Object> map = Maps.newHashMap();
		if (bool) {
			// 成功
			map.put("success", 1);
		} else {
			map.put("success", 0);
			map.put("message", "服务器错误！");
		}
		renderJson(map);
	}

	public void addZhuanji(){
		User user = getSessionAttr("user");
		Integer userId = Integer.parseInt(String.valueOf(user.get("userid")));
		String userName = user.get("username");
		
		String coverPath = upLoadFileDealPath("cover", "", 2000 * 1024 * 1024, "utf-8");
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
	
	public void uploadShipin(){
		Map<String, Object> map = Maps.newHashMap();
		User user = getSessionAttr("user");
		Integer userId = Integer.parseInt(String.valueOf(user.get("userid")));
		String userName = user.get("username");
		
		String coverPath = upLoadFileDealPath("cover", "", 2000 * 1024 * 1024, "utf-8");
		String comicPath = upLoadVideo("comic", "", 2000 * 1024 * 1024, "utf-8");
		String title = getPara("title");
		String des = getPara("des");
		String ispublic = getPara("ispublic");
		String worksid = getPara("worksid");
		
		Works worksModel = getModel(Works.class).findById(worksid);
		Work workModel = getModel(Work.class);
		boolean bool = false;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		System.out.println(worksModel.get("maxnum"));
		String maxnum = String.valueOf(worksModel.get("maxnum"));
		if(StrKit.isBlank(maxnum)){
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
		workModel.set("worknum", Integer.parseInt(maxnum));
		workModel.set("workname", title);
		workModel.set("workdes", des);
		workModel.set("ischeck", 0);
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

	public void editShipin() {
		Map<String, Object> map = Maps.newHashMap();
		User user = getSessionAttr("user");
		Integer userId = Integer.parseInt(String.valueOf(user.get("userid")));
		String userName = user.get("username");
		
		String coverPath = upLoadFileDealPath("cover", "", 2000 * 1024 * 1024, "utf-8");
		String worksid = getPara("worksid");
		String title = getPara("title");
		String leadingrole = getPara("leadingrole");
		String des = getPara("des");
		String ispublic = getPara("ispublic");
		Works worksModel = getModel(Works.class).findById(worksid);
		Work workModel = getModel(Work.class);
		List<Work> works = getModel(Work.class).getWorkByWorksID(worksid);
		if(!works.isEmpty()){
			workModel = works.get(0);
		}
		boolean bool = false;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		if(!StrKit.isBlank(coverPath)){
			String oldcover = worksModel.get("cover");
			File coverFile = new File(oldcover);
			if(coverFile.exists()){
				coverFile.delete();
			}
			worksModel.set("cover", coverPath);
			
			String oldcover1 = workModel.get("cover");
			File coverFile1 = new File(oldcover1);
			if(coverFile1.exists()){
				coverFile1.delete();
			}
			workModel.set("cover", coverPath);
		}
		if(!StrKit.isBlank(title)){
			worksModel.set("worksname", title);
			workModel.set("workname", title);
		}
		if(!StrKit.isBlank(leadingrole)){
			worksModel.set("leadingrole", leadingrole);
		}
		worksModel.set("describle", des);
		workModel.set("workdes", des);
		if(!StrKit.isBlank(ispublic)){
			worksModel.set("ispublic", ispublic);
			workModel.set("isdisable", ispublic);
		}
		worksModel.set("modifier", userId);
		worksModel.set("modifiername", userName);
		worksModel.set("modifytime", timestamp);
		
		bool = worksModel.update();
		if(!bool){
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
	
	public void editZhuanji(){
		Map<String, Object> map = Maps.newHashMap();
		User user = getSessionAttr("user");
		Integer userId = Integer.parseInt(String.valueOf(user.get("userid")));
		String userName = user.get("username");
		
		String coverPath = upLoadFileDealPath("cover", "", 2000 * 1024 * 1024, "utf-8");
		String worksid = getPara("worksid");
		String title = getPara("title");
		String leadingrole = getPara("leadingrole");
		String des = getPara("des");
		String ispublic = getPara("ispublic");
		Works worksModel = getModel(Works.class).findById(worksid);
		boolean bool = false;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		if(!StrKit.isBlank(coverPath)){
			String oldcover = worksModel.get("cover");
			File coverFile = new File(oldcover);
			if(coverFile.exists()){
				coverFile.delete();
			}
			worksModel.set("cover", coverPath);
		}
		if(!StrKit.isBlank(title)){
			worksModel.set("worksname", title);
		}
		if(!StrKit.isBlank(leadingrole)){
			worksModel.set("leadingrole", leadingrole);
		}
		worksModel.set("describle", des);
		if(!StrKit.isBlank(ispublic)){
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
	
	public void editZhuanjiShipin(){
		
		Map<String, Object> map = Maps.newHashMap();
		boolean bool = false;
		try {
			User user = getSessionAttr("user");
			Integer userId = Integer.parseInt(String.valueOf(user.get("userid")));
			String userName = user.get("username");
			
			String coverPath = upLoadFileDealPath("cover", "", 2000 * 1024 * 1024, "utf-8");
			String workid = getPara("workid");
			String title = getPara("title");
			String des = getPara("des");
			String ispublic = getPara("ispublic");
			
			Work workModel = getModel(Work.class).findById(workid);
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			if(!StrKit.isBlank(coverPath)){
				String oldcover1 = workModel.get("cover");
				if(!StrKit.isBlank(oldcover1)){
					File coverFile1 = new File(oldcover1);
					if(coverFile1.exists()){
						coverFile1.delete();
					}
				}
				workModel.set("cover", coverPath);
			}
			if(!StrKit.isBlank(title)){
				workModel.set("workname", title);
			}
			workModel.set("workdes", des);
			if(!StrKit.isBlank(ispublic)){
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
		User user = getSessionAttr("user");
		String flag = getPara("flag");
		String type = getPara("type");
		String workstype = getPara("workstype");
		String pageIndexStr = getPara("pageIndex");
		Integer pageIndex = 1;
		if (!StrKit.isBlank(pageIndexStr)) {
			pageIndex = Integer.parseInt(pageIndexStr);
		}
		String pageSizeStr = getPara("pageSize");
		Integer pageSize = 10;
		if (!StrKit.isBlank(pageSizeStr)) {
			pageSize = Integer.parseInt(pageSizeStr);
		}
		if(StrKit.isBlank(flag)){
			flag = type;
		}
		WorksType worksTypeModel = getModel(WorksType.class);
		Works worksModel = getModel(Works.class);
		List<WorksType> worksTypes = worksTypeModel.getWorksTypes(Integer.parseInt(type));
		if ("0".equals(flag)) {
			// 0是跳转视频管理
			Page<Works> page = worksModel.getWorksInfoPage(workstype, type, user.get("creater"), pageIndex, pageSize);
			List<Map<String, Object>> data = ParseDemoKit.worksParse(page.getList());
			Page<List<Map<String, Object>>> pageWorks = new Page(data, page.getPageNumber(), page.getPageSize(),page.getTotalPage(), page.getTotalRow());
			setAttr("workstype", workstype);
			setAttr("worksTypes", worksTypes);
			setAttr("pageWorks", pageWorks);
			render("/worksManage/shipinManage.html");
		}else if ("1".equals(flag)) {
			//1是跳转专辑管理
			Page<Works> page = worksModel.getWorksInfoPage(workstype, type, user.get("creater"), pageIndex, pageSize);
			List<Map<String, Object>> data = ParseDemoKit.worksParse(page.getList());
			Page<List<Map<String, Object>>> pageWorks = new Page(data, page.getPageNumber(), page.getPageSize(),
					page.getTotalPage(), page.getTotalRow());
			setAttr("workstype", workstype);
			setAttr("worksTypes", worksTypes);
			setAttr("pageWorks", pageWorks);
			render("/worksManage/zhuanjiManage.html");
		}else if("10".equals(flag)){
			//10 是跳转 添加视频页面
			setAttr("type", type);
			setAttr("worksTypes", worksTypes);
			setAttr("workstype", workstype);
			setAttr("pageIndex", pageIndex);
			render("/worksManage/addshipin.html");
		}else if("11".equals(flag)){
			//11 是跳转 添加专辑页面
			setAttr("type", type);
			setAttr("worksTypes", worksTypes);
			setAttr("workstype", workstype);
			setAttr("pageIndex", pageIndex);
			render("/worksManage/addzhuanji.html");
		}else if("20".equals(flag)){
			//20 是跳转 编辑视频页面
			String worksid = getPara("worksid");
			worksModel = worksModel.findById(worksid);
			Work workModel = getModel(Work.class);
			List<Work> works = workModel.getWorkByWorksID(worksid);
			worksTypeModel = worksTypeModel.findById(worksModel.get("workstype"));
			if(!works.isEmpty()){
				workModel = works.get(0);
			}
			setAttr("type", type);
			setAttr("pageIndex", pageIndex);
			setAttr("worksType", worksTypeModel);
			setAttr("workstype", workstype);
			setAttr("works", worksModel);
			setAttr("work", workModel);
			
			render("/worksManage/editshipin.html");
		}else if("21".equals(flag)){
			//21 是跳转 专辑详细页面
			String worksid = getPara("worksid");
			String workpageIndexStr = getPara("workpageIndex");
			Integer workpageIndex = 1;
			if (!StrKit.isBlank(workpageIndexStr)) {
				workpageIndex = Integer.parseInt(workpageIndexStr);
			}
			worksModel = worksModel.findById(Integer.parseInt(worksid));
			worksTypeModel = worksTypeModel.findById(worksModel.get("workstype"));
			Page<Work> pageWork = getModel(Work.class).getWorkByWorksID(Integer.parseInt(worksid), workpageIndex, pageSize);
			setAttr("type", type);
			setAttr("workstype", workstype);
			setAttr("pageIndex", pageIndex);
			setAttr("works", worksModel);
			setAttr("worksType", worksTypeModel);
			setAttr("pageWork", pageWork);
			render("/worksManage/detailzhuanji.html");
		}else if("22".equals(flag)){
			//22 是跳转 编辑专辑页面
			String worksid = getPara("worksid");
			String workpageIndexStr = getPara("workpageIndex");
			Integer workpageIndex = 1;
			if (!StrKit.isBlank(workpageIndexStr)) {
				workpageIndex = Integer.parseInt(workpageIndexStr);
			}
			worksModel = worksModel.findById(Integer.parseInt(worksid));
			worksTypeModel = worksTypeModel.findById(worksModel.get("workstype"));
			setAttr("type", type);
			setAttr("workstype", workstype);
			setAttr("pageIndex", pageIndex);
			setAttr("workpageIndex", workpageIndex);
			
			setAttr("works", worksModel);
			setAttr("worksType", worksTypeModel);
			render("/worksManage/editzhuanji.html");
		}else if("23".equals(flag)){
			//23 是跳转 编辑专辑-->视频页面
			String workpageIndexStr = getPara("workpageIndex");
			Integer workpageIndex = 1;
			if (!StrKit.isBlank(workpageIndexStr)) {
				workpageIndex = Integer.parseInt(workpageIndexStr);
			}
			String worksid = getPara("worksid");
			String workid = getPara("workid");
			Work workModel = getModel(Work.class).findById(workid);
			
			setAttr("type", type);
			setAttr("workpageIndex", workpageIndex);
			setAttr("pageIndex", pageIndex);
			setAttr("worksid", worksid);
			
			setAttr("work", workModel);
			render("/worksManage/editzhuanjishipin.html");
		}else if("24".equals(flag)){
			//23 是跳转 添加专辑->视频 页面
			String workpageIndexStr = getPara("workpageIndex");
			Integer workpageIndex = 1;
			if (!StrKit.isBlank(workpageIndexStr)) {
				workpageIndex = Integer.parseInt(workpageIndexStr);
			}
			String worksid = getPara("worksid");
			setAttr("type", type);
			setAttr("workstype", workstype);
			setAttr("workpageIndex", workpageIndex);
			setAttr("pageIndex", pageIndex);
			setAttr("worksid", worksid);
			render("/worksManage/uploadshipin.html");
		}
	}
	
	public void showAdminPage(){
		String type = getPara("type");
		String flag = getPara("flag");
		String workstype = getPara("workstype");
		String pageIndexStr = getPara("pageIndex");
		Integer pageIndex = 1;
		if (!StrKit.isBlank(pageIndexStr)) {
			pageIndex = Integer.parseInt(pageIndexStr);
		}
		String pageSizeStr = getPara("pageSize");
		Integer pageSize = 10;
		if (!StrKit.isBlank(pageSizeStr)) {
			pageSize = Integer.parseInt(pageSizeStr);
		}
		WorksType worksTypeModel = getModel(WorksType.class);
		Works worksModel = getModel(Works.class);
		List<WorksType> worksTypes = worksTypeModel.getWorksTypes(Integer.parseInt(type));
		if("0".equals(flag)){
			//查询未审核的
			Page<Works> pagedata = worksModel.getWorksNotCheck(workstype, pageIndex, pageSize);
			Page<List<Map<String, Object>>> pageWorks = new Page(parseAdminZhuanji(pagedata.getList()),pageIndex,pageSize,pagedata.getTotalPage(),pagedata.getTotalRow());
			setAttr("pageWorks", pageWorks);
			setAttr("worksTypes", worksTypes);
			setAttr("workstype", workstype);
			setAttr("flag", flag);
			render("/admin/admin_works_zhuanji.html");
			return;
		}else if("1".equals(flag)){
			//查询首页5个轮播
			Page<Works> pagedata = worksModel.getHomePage(workstype, pageIndex, pageSize);
			Page<List<Map<String, Object>>> pageWorks = new Page(parseAdminZhuanji(pagedata.getList()),pageIndex,pageSize,pagedata.getTotalPage(),pagedata.getTotalRow());
			setAttr("pageWorks", pageWorks);
			setAttr("worksTypes", worksTypes);
			setAttr("workstype", workstype);
			setAttr("flag", flag);
			render("/admin/admin_works_zhuanji.html");
		}else if("2".equals(flag)){
			//查询 全部
			Page<Works> pagedata = worksModel.getWorksInfoPage(workstype, type, pageIndex, pageSize);
			Page<List<Map<String, Object>>> pageWorks = new Page(parseAdminZhuanji(pagedata.getList()),pageIndex,pageSize,pagedata.getTotalPage(),pagedata.getTotalRow());
			setAttr("pageWorks", pageWorks);
			setAttr("worksTypes", worksTypes);
			setAttr("workstype", workstype);
			setAttr("flag", flag);
			render("/admin/admin_works_zhuanji.html");
		}
	}
	
	public void yesChecked(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String ids = getPara("ids");
			String[] idsStr = ids.split(",");
			for (String id : idsStr) {
				Work workModel = getModel(Work.class).findById(id);
				if(workModel.isNotEmpty()){
					workModel.set("ischeck", 1);
					workModel.update();
				}
			}
			map.put("success", 1);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", 0);
		}
		renderJson(map);
		return;
	}
	public void yesCheckWorks(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String worksid = getPara("worksid");
			if(StrKit.isBlank(worksid)){
				map.put("success", 0);
				map.put("message", "参数为空！");
				renderJson(map);
				return;
			}
			Works worksModel = getModel(Works.class).findById(worksid);
			if(!worksModel.isNotEmpty()){
				map.put("success", 0);
				map.put("message", "找不到对象");
				renderJson(map);
				return;
			}
			
			worksModel.set("ischeck", 1);
			boolean bool = worksModel.update();
			if(bool){
				map.put("success", 1);
				renderJson(map);
				return;
			}else {
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
	
	public void upranking(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String type = getPara("type");
			String flag = getPara("flag");
			String worksid = getPara("worksid");
			Works worksModel = getModel(Works.class).findById(worksid);
			if(!worksModel.isNotEmpty()){
				map.put("success", 0);
				renderJson(map);
				return;
			}
			if("0".equals(flag)){
				//首页置顶
				Integer homepage = worksModel.getMaxHomePageValue(type);
				worksModel.set("homepage", homepage + 1);
			}
			if("1".equals(flag)){
				//类型置顶
				Integer istop = worksModel.getMaxTopValue(type);
				worksModel.set("istop", istop + 1);
			}
			if("10".equals(flag)){
				worksModel.set("homepage", 0);
			}
			if("11".equals(flag)){
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
	
	private List<Map<String, Object>> parseAdminZhuanji(List<Works> workss){
		List<Map<String, Object>> resultMap = Lists.newArrayList();
		try {
			for (Works works : workss) {
				if(works == null)
					continue;
				Map<String, Object> map = new HashMap<String, Object>();
				Object worksid = works.get("worksid");
				map.put("worksid", worksid);
				String worksname = works.get("worksname");
				if(worksname.length() > 12){
					worksname = worksname.substring(0,12);
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
				if(type == 1){
					//0专辑 
					playUrl = "cartoon/playVideo?id="+worksid;
					detailUrl = "cartoon/showDetail?id="+worksid;
				}else if(type == 0){
					 //视频
					playUrl = "cartoon/playVideo?id="+worksid;
					detailUrl = playUrl;
				}
				map.put("playUrl",playUrl);
				map.put("detailUrl",detailUrl);
				String typeUrl = "cartoon/showWorksList?wtype="+works.get("workstype");
				map.put("typeUrl",typeUrl);
				String describle = works.get("describle");
				if(describle.length() > 20){
					describle = describle.substring(0, 20) + "...";
				}
				map.put("describle",describle);
				map.put("creater", works.get("createtime"));
				map.put("creatername", works.get("creatername"));
				map.put("createtime", works.get("createtime"));
				map.put("modifier", works.get("modifier"));
				map.put("modifiername", works.get("modifiername"));
				map.put("updatetime", works.get("updatetime"));
				List<Work> workList = getModel(Work.class).getNotCheckWork(works.get("worksid"));
				map.put("workNotCheck", workList);
				resultMap.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	public void getWorksTypes(){
		String type = getPara("type");
		WorksType worksTypeModel = getModel(WorksType.class);
		List<WorksType> worksTypes = worksTypeModel.getWorksTypes(Integer.parseInt(type));
		renderJson(worksTypes);
	}
}
