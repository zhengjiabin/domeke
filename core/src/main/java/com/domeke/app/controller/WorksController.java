package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.model.Works;
import com.domeke.app.route.ControllerBind;
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
		render("/admin/works.html");
	}

	/**
	 * 跳转作品管理界面
	 */
	public void goWorksMan() {
		String worksType = getPara("type");
		if (worksType == null || worksType.trim().length() == 0) {
			queryAllWorksInfo();
		} else {
			getWorksInfoByType(worksType);
		}
		render("/demo/worksmanage.html");
	}

	/**
	 * 跳转添加
	 */
	public void addWorks() {
		render("/demo/addworks.html");
	}

	/**
	 * 保存作品信息<br>
	 * 
	 */
	public void save() {
		try {
			String coverPath = upLoadFile("cover", 2000 * 1024 * 1024, "utf-8");
			String comicPath = upLoadVideo("comic", 5000 * 1024 * 1024, "utf-8");
			Works worksModel = getModel(Works.class);
			worksModel.set("cover", coverPath);
			worksModel.set("comic", comicPath);
			// 可改为获取当前用户的名字或者ID
			worksModel.set("creater", 111111);
			worksModel.set("modifier", 111111);
			worksModel.saveWorksInfo();
			redirect("/works/goWorksMan");
		} catch (Exception e) {
			e.printStackTrace();
			render("/demo/addworks.html");
		}
	}

	/**
	 * 更新已修的作品
	 */
	public void update() {
		String coverPath = upLoadFile("cover", 2000 * 1024 * 1024, "utf-8");
		String comicPath = upLoadVideo("comic", 5000 * 1024 * 1024, "utf-8");
		// 可改为获取当前用户的名字或者ID
		Works worksModel = getModel(Works.class);
		if (coverPath != null) {
			worksModel.set("cover", coverPath);
		} else {
			worksModel.remove("cover");
		}
		if (comicPath != null) {
			worksModel.set("comic", comicPath);
		} else {
			worksModel.remove("comic");
		}
		worksModel.set("modifier", 111111);
		worksModel.update();
		redirect("/works/goWorksMan");
	}

	/**
	 * 删除作品信息
	 */
	public void delete() {
		Works worksModel = getModel(Works.class);
		worksModel.deleteById(getParaToInt("id"));
		redirect("/works/goWorksMan");
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
	 * 根据作品类型获取作品信息
	 */
	public void getWorksInfoByType(String workstype) {
		Works worksModel = getModel(Works.class);
		List<Works> workslist = worksModel.getWorksInfoByType(workstype);
		this.setAttr("workslist", workslist);
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
