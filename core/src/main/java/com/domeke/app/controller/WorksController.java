package com.domeke.app.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.domeke.app.model.Work;
import com.domeke.app.model.Works;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.ActionKey;
import com.jfinal.kit.PathKit;
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
public class WorksController extends FilesLoadController {

	/**
	 * to管理界面
	 */
	public void goToManager() {
		render("/admin/admin_works.html");
	}

	/**
	 * 保存作品信息<br>
	 * 
	 */
	public void save() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String coverPath = upLoadFile("cover", "", 2000 * 1024 * 1024, "utf-8");
			Works worksModel = getModel(Works.class);
			if(StrKit.isBlank(coverPath)){
				coverPath = "";
			}
			worksModel.set("cover", coverPath);
			worksModel.set("leadingrole", 111);
			
			// 可改为获取当前用户的名字或者ID
			worksModel.set("creater", 111111);
			worksModel.set("modifier", 111111);
			boolean bool = worksModel.save();
			if(bool){
				map.put("success", "1");
			}else{
				map.put("success", "0");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", "0");
		}
		renderJson(map);
	}

	public void saveWork(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String comicPath = upLoadFile("comic", "", 2000 * 1024 * 1024, "utf-8");
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			Work workModel = getModel(Work.class);
			workModel.set("comic", comicPath);
			workModel.set("modifier", 1);
			workModel.set("modifytime", timestamp);
			workModel.set("createtime", timestamp);
			workModel.set("creater", 1);
			workModel.save();
			map.put("success", "1");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", "0");
		}
		renderJson(map);
	}
	/**
	 * 更新已修的作品
	 */
	public void updateWorks() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			this.getFiles();
			Works worksModel = getModel(Works.class);
			worksModel.set("modifier", 111111);
			worksModel.update();
			map.put("success", "1");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", "0");
		}
		renderJson(map);
	}
	
	public void updateWork(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			this.getFiles();
			Work workModel = getModel(Work.class);
			workModel.set("modifier", 111111);
			workModel.update();
			map.put("success", "1");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", "0");
		}
		renderJson(map);
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
		if(bool){
			map.put("success", "1");
		}else{
			map.put("success", "0");
		}
		renderJson(map);
	}
	
	public void deleteWork() {
		int id = getParaToInt("id");
		Work workModel = getModel(Work.class);
		Work work = workModel.findById(id);
		boolean bool = false;
		if(work != null){
			bool = work.delete();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if(bool){
			map.put("success", "1");
		}else{
			map.put("success", "0");
		}
		renderJson(map);
	}
	
	public void getSubWork(){
		String worksId = getPara("worksId");
		String pageIndex = getPara("pageIndex");
		String pageSize = getPara("pageSize");
		if(StrKit.isBlank(worksId)){
			worksId = "0";
		}
		if(StrKit.isBlank(pageIndex)){
			pageIndex = "1";
		}
		if(StrKit.isBlank(pageSize)){
			pageSize = "5";
		}
		Work work = getModel(Work.class);
		Page<Work> pageWorkk = work.getWorkByWorksID(Integer.parseInt(worksId), Integer.parseInt(pageIndex), Integer.parseInt(pageSize));
		renderJson(pageWorkk);
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
	public void getWorksById() {
		Works worksModel = getModel(Works.class);
		Works works = worksModel.findById(getParaToInt("id"));
		setAttr("works", works);
		render("/demo/modifyworks.html");
	}
	/**
	 * 根据作品ID获取某作品
	 */
	public void getWorksJsonById() {
		Works worksModel = getModel(Works.class);
		Works works = worksModel.findById(getParaToInt("id"));
		renderJson(works);
	}
	public void getWorkJsonById(){
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
		if(StrKit.isBlank(pageIndex)){
			pageIndex = "1";
		}
		if(StrKit.isBlank(pageSize)){
			pageSize = "5";
		}
		Works worksModel = getModel(Works.class);
		Page<Works> workslist = worksModel.getWorksInfoPage(workstype,Integer.parseInt(pageIndex),Integer.parseInt(pageSize));
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

	//个人中心-->我的作品
	public void myWork(){
		String workType = getPara("workType");
		render("/worksManager.html");
	}
	
}
