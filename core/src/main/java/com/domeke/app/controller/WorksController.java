package com.domeke.app.controller;

import java.sql.Timestamp;
import java.util.List;

import com.domeke.app.model.Work;
import com.domeke.app.model.Works;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.ActionKey;
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
		try {
			String coverPath = upLoadFile("cover", "", 2000 * 1024 * 1024,
					"utf-8");
			Works worksModel = getModel(Works.class);
			worksModel.set("cover", coverPath);
			// 可改为获取当前用户的名字或者ID
			worksModel.set("creater", 111111);
			worksModel.set("modifier", 111111);
			worksModel.saveWorksInfo();
			redirect("/works/goToManager");
		} catch (Exception e) {
			e.printStackTrace();
			render("/works/goToManager");
		}
	}

	public void saveWork(){
		this.getFiles();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Work workModel = getModel(Work.class);
		workModel.set("modifier", 111111);
		workModel.set("modifytime", timestamp);
		workModel.set("createtime", timestamp);
		workModel.set("creater", 111111);
		workModel.save();
		redirect("/works/goToManager");
	}
	/**
	 * 更新已修的作品
	 */
	public void updateWorks() {
		this.getFiles();
		Works worksModel = getModel(Works.class);
		worksModel.set("modifier", 111111);
		worksModel.update();
		redirect("/works/goToManager");
	}
	
	public void updateWork(){
		this.getFiles();
		Work workModel = getModel(Work.class);
		workModel.set("modifier", 111111);
		workModel.update();
		redirect("/works/goToManager");
	}
	
	/**
	 * 删除作品信息
	 */
	public void delete() {
		Works worksModel = getModel(Works.class);
		worksModel.deleteById(getParaToInt("id"));
		redirect("/works/goWorksMan");
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

}
