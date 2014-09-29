package com.domeke.app.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.User;
import com.domeke.app.model.Work;
import com.domeke.app.model.Works;
import com.domeke.app.route.ControllerBind;
import com.jfinal.aop.Before;
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
@Before(LoginInterceptor.class)
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

	//--------------个人中心-->我的作品---------------
	public void addShipin(){
		String type = getPara("type");
		if(StrKit.isBlank(type) || "null".equals(type)){
			render("/works/addshipin.htm");
			return;
		}
		String pageIndexStr = getPara("pageIndex");
		Integer pageIndex = 1;
		if(!StrKit.isBlank(pageIndexStr)){
			pageIndex = Integer.parseInt(pageIndexStr);
		}
		String pageSizeStr = getPara("pageSize");
		Integer pageSize = 10;
		if(!StrKit.isBlank(pageSizeStr)){
			pageSize = Integer.parseInt(pageSizeStr);
		}
		
		Works worksModel = getModel(Works.class);
		Work workModel = getModel(Work.class);
		String coverPath = upLoadFile("cover", "", 2000 * 1024 * 1024, "utf-8");
		String comicPath = upLoadFile("comic", "", 2000 * 1024 * 1024, "utf-8");
		boolean bool = false;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		worksModel.set("cover", coverPath);
		worksModel.set("type", 1);
		worksModel.set("ischeck", 0);
		worksModel.set("istop", 0);
		worksModel.set("creativeprocess", 20);
		worksModel.set("comment", 0);
		worksModel.set("pageviews", 0);
		worksModel.set("collection", 0);
		worksModel.set("praise", 0);
		worksModel.set("maxnum", 1);
		worksModel.set("releasedate", timestamp);
		worksModel.set("updatetime", timestamp);
		worksModel.set("leadingrole", 111);
		worksModel.set("createtime", 111);
		worksModel.set("creater", timestamp);
		worksModel.set("modifytime", 111);
		worksModel.set("modifier", timestamp);
		bool = worksModel.save();
		workModel.set("worksid", worksModel.get("worksid"));
		workModel.set("worknum", 1);
		workModel.set("workname", worksModel.get("worksname"));
		workModel.set("workdes", worksModel.get("describle"));
		workModel.set("comic", comicPath);
		workModel.set("createtime", timestamp);
		workModel.set("creater", 111111);
		workModel.set("modifytime", timestamp);
		workModel.set("modifier", 111111);
		bool = workModel.save();
		if(bool){
			//成功
			StringBuffer from = new StringBuffer("from works");
			String workstype = worksModel.get("workstype");
			if(!StrKit.isBlank(worksModel.get("workstype"))){
				from.append(" where workstype = "+workstype);
			}
			Page<Works> pageWorks = worksModel.getWorksInfoPage(workstype, type, pageIndex, pageSize);
			setAttr("pageWorks", pageWorks);
			render("/works/showworkList.htm");
		}else{
			setAttr("works", worksModel);
			setAttr("message", "添加失败!");
			render("/works/addshipin.htm");
		}
		render("/works/addshipin.htm");
		return;
	}
	public void addWork(){
		String worksidStr = getPara("worksid");
		if(StrKit.isBlank(worksidStr)){
			render("/works/addwork.htm");
			return;
		}
		Integer worksid = Integer.parseInt(worksidStr);
		String pageIndexStr = getPara("pageIndex");
		Integer pageIndex = 1;
		if(!StrKit.isBlank(pageIndexStr)){
			pageIndex = Integer.parseInt(pageIndexStr);
		}
		String pageSizeStr = getPara("pageSize");
		Integer pageSize = 10;
		if(!StrKit.isBlank(pageSizeStr)){
			pageSize = Integer.parseInt(pageSizeStr);
		}
		try {
			Work workModel = getModel(Work.class);
			String comicPath = upLoadFile("comic", "", 2000 * 1024 * 1024, "utf-8");
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			workModel.set("comic", comicPath);
			workModel.set("creater", 1);
			workModel.set("modifier", 1);
			workModel.set("createtime", timestamp);
			workModel.set("modifytime", timestamp);
			boolean bool = workModel.save();
			if(bool){
				//成功
				Works works = getModel(Works.class).findById(worksid);
				Page<Work> pageWork = workModel.getWorkByWorksID(worksid, pageIndex, pageSize);
				setAttr("works", works);
				setAttr("pageWork", pageWork);
				render("/works/showworks.htm");
				return;
				
			}else{
				//失败
				setAttr("works", workModel);
				setAttr("message", "添加失败!");
				render("/works/addwork.htm");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			setAttr("message", "服务器错误!");
			render("/works/addwork.htm");
			return;
		}
	}
	public void addWorks(){
		try {
			String workstype = getPara("workstype");
			String pageIndexStr = getPara("pageIndex");
			Integer pageIndex = 1;
			if(!StrKit.isBlank(pageIndexStr)){
				pageIndex = Integer.parseInt(pageIndexStr);
			}
			String pageSizeStr = getPara("pageSize");
			Integer pageSize = 10;
			if(!StrKit.isBlank(pageSizeStr)){
				pageSize = Integer.parseInt(pageSizeStr);
			}
			User user = getSessionAttr("user");
			String coverPath = upLoadFile("cover", "", 2000 * 1024 * 1024, "utf-8");
			if(StrKit.isBlank(coverPath)){
				coverPath = "";
			}
			Works worksModel = getModel(Works.class);
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			worksModel.set("cover", coverPath);
			worksModel.set("type", 1);
			worksModel.set("ischeck", 0);
			worksModel.set("istop", 0);
			worksModel.set("creativeprocess", 20);
			worksModel.set("comment", 0);
			worksModel.set("pageviews", 0);
			worksModel.set("collection", 0);
			worksModel.set("praise", 0);
			worksModel.set("maxnum", 1);
			worksModel.set("releasedate", timestamp);
			worksModel.set("updatetime", timestamp);
			worksModel.set("leadingrole", 111);
			worksModel.set("createtime", 111);
			worksModel.set("creater", timestamp);
			worksModel.set("modifytime", 111);
			worksModel.set("modifier", timestamp);
			boolean bool = worksModel.save();
			if(bool){
				//成功
				StringBuffer from = new StringBuffer("from works");
				if(!StrKit.isBlank(workstype)){
					from.append(" where workstype = "+workstype);
				}
				Page<Works> pageWorks = worksModel.paginate(pageIndex, pageSize, "select *", from.toString());
				setAttr("pageWorks", pageWorks);
				render("/works/showworks.htm");
				return;
			}else{
				setAttr("works", worksModel);
				setAttr("message", "添加失败!");
				render("/works/addworks.htm");
				return;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			setAttr("message", "服务器错误");
			render("/works/addworks.htm");
			return;
		}
		
	}
	public void editWork(){
		String worksidStr = getPara("worksid");
		if(StrKit.isBlank(worksidStr)){
			render("/works/editwork.htm");
			return;
		}
		String workidStr = getPara("workid");
		String worknumStr = getPara("worknum");
		String worknameStr = getPara("workname");
		String workdesStr = getPara("workdes");
		String workisdesbleStr = getPara("workisdesble");
		if(StrKit.isBlank(workidStr)){
			render("/works/editwork.htm");
			return;
		}
		String pageIndexStr = getPara("pageIndex");
		Integer pageIndex = 1;
		if(!StrKit.isBlank(pageIndexStr)){
			pageIndex = Integer.parseInt(pageIndexStr);
		}
		String pageSizeStr = getPara("pageSize");
		Integer pageSize = 10;
		if(!StrKit.isBlank(pageSizeStr)){
			pageSize = Integer.parseInt(pageSizeStr);
		}
		try {
			Work workModel = getModel(Work.class).findById(workidStr);
			Works worksModel = getModel(Works.class).findById(worksidStr);
			
			if(!StrKit.isBlank(worknumStr)){
				workModel.set("worknum", worknumStr);
			}
			if(!StrKit.isBlank(worknameStr)){
				workModel.set("workname", worknameStr);
			}
			if(!StrKit.isBlank(workdesStr)){
				workModel.set("workdes", workdesStr);
			}
			if(!StrKit.isBlank(workisdesbleStr)){
				workModel.set("workisdesble", workisdesbleStr);
			}
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			workModel.set("modifier", 1);
			workModel.set("modifytime", timestamp);
			boolean bool = workModel.update();
			if(bool){
				//成功
				Page<Works> pageWorks = null;
				pageWorks = worksModel.getWorksInfoPage("", pageIndex, pageSize);
				setAttr("works", worksModel);
				setAttr("pageWorks", pageWorks);
				render("/works/showwork.htm");
				return;
			}else{
				//失败
				setAttr("works", workModel);
				setAttr("message", "添加失败!");
				render("/works/editwork.htm");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			setAttr("message", "服务器错误!");
			render("/works/editwork.htm");
			return;
		}
	}
	
	public void editWorks(){
		String worksidStr = getPara("worksid");
		if(StrKit.isBlank(worksidStr)){
			render("/works/editworks.htm");
			return;
		}
		String pageIndexStr = getPara("pageIndex");
		Integer pageIndex = 1;
		if(!StrKit.isBlank(pageIndexStr)){
			pageIndex = Integer.parseInt(pageIndexStr);
		}
		String pageSizeStr = getPara("pageSize");
		Integer pageSize = 10;
		if(!StrKit.isBlank(pageSizeStr)){
			pageSize = Integer.parseInt(pageSizeStr);
		}
		
		Works worksModel = getModel(Works.class).findById(worksidStr);
		String workname = getPara("worksname");
		String workdes = getPara("workdes");
		String creativeprocess = getPara("creativeprocess");
		String describle = getPara("describle");
		if(!StrKit.isBlank(workname)){
			worksModel.set("workname", workname);
		}
		if(!StrKit.isBlank(workdes)){
			worksModel.set("workdes", workdes);
		}
		if(!StrKit.isBlank(creativeprocess)){
			worksModel.set("creativeprocess", creativeprocess);
		}
		if(!StrKit.isBlank(describle)){
			worksModel.set("describle", describle);
		}
		boolean bool = worksModel.update();
		if(bool){
			//成功
			Page<Works> pageWorks = null;
			pageWorks = worksModel.getWorksInfoPage("", pageIndex, pageSize);
			setAttr("works", worksModel);
			setAttr("pageWorks", pageWorks);
			render("/works/showworks.htm");
			return;
		}else{
			//失败
			setAttr("works", worksModel);
			setAttr("message", "添加失败!");
			render("/works/editwork.htm");
			return;
		}
	}
	public void editShipin(){
		String worksid = getPara("worksid");
		String workid = getPara("workid");
		if(StrKit.isBlank(worksid) || StrKit.isBlank(workid)){
			render("/works/editworks.htm");
			return;
		}
		String pageIndexStr = getPara("pageIndex");
		Integer pageIndex = 1;
		if(!StrKit.isBlank(pageIndexStr)){
			pageIndex = Integer.parseInt(pageIndexStr);
		}
		String pageSizeStr = getPara("pageSize");
		Integer pageSize = 10;
		if(!StrKit.isBlank(pageSizeStr)){
			pageSize = Integer.parseInt(pageSizeStr);
		}
		Works worksModel = getModel(Works.class).findById(worksid);
		Work workModel = getModel(Work.class).findById(workid);
		String worksname = getPara("worksname");
		String creativeprocess = getPara("creativeprocess");
		String describle = getPara("describle");
		String isdisable = getPara("isdisable");
		
		if(!StrKit.isBlank(worksname)){
			worksModel.set("workname", worksname);
			workModel.set("workname", worksname);
		}
		if(!StrKit.isBlank(creativeprocess)){
			worksModel.set("creativeprocess", creativeprocess);
		}
		if(!StrKit.isBlank(describle)){
			worksModel.set("describle", describle);
			workModel.set("workdes", describle);
		}
		if(!StrKit.isBlank(isdisable)){
			workModel.set("isdisable", isdisable);
		}
		boolean bool = worksModel.update();
		bool = workModel.update();
		if(bool){
			//成功
			Page<Works> pageWorks = null;
			pageWorks = worksModel.getWorksInfoPage("", pageIndex, pageSize);
			setAttr("pageWorks", pageWorks);
			render("/works/showwork.htm");
			return;
		}else{
			//失败
			setAttr("works", worksModel);
			setAttr("message", "添加失败!");
			render("/works/editwork.htm");
			return;
		}
	}
	
	
	public void showWork(){
		String pageIndexStr = getPara("pageIndex");
		Integer pageIndex = 1;
		if(!StrKit.isBlank(pageIndexStr)){
			pageIndex = Integer.parseInt(pageIndexStr);
		}
		String pageSizeStr = getPara("pageSize");
		Integer pageSize = 10;
		if(!StrKit.isBlank(pageSizeStr)){
			pageSize = Integer.parseInt(pageSizeStr);
		}
		try {
			Works worksModel = getModel(Works.class);
			Page<Works> pageWorks = null;
			pageWorks = worksModel.getWorksInfoPage("", pageIndex, pageSize);
			setAttr("pageWorks", pageWorks);
			render("/works/showwork.htm");
		} catch (Exception e) {
			e.printStackTrace();
		}
		render("/works/showwork.htm");
	}
	public void showWorks(){
		try {
			render("/works/showworks.htm");
			String workstype = getPara("workstype");
			String pageIndexStr = getPara("pageIndex");
			Integer pageIndex = 1;
			if(!StrKit.isBlank(pageIndexStr)){
				pageIndex = Integer.parseInt(pageIndexStr);
			}
			String pageSizeStr = getPara("pageSize");
			Integer pageSize = 10;
			if(!StrKit.isBlank(pageSizeStr)){
				pageSize = Integer.parseInt(pageSizeStr);
			}
			StringBuffer from = new StringBuffer("from works");
			if(!StrKit.isBlank(workstype)){
				from.append(" where workstype = "+workstype);
			}
			Works worksModel = getModel(Works.class);
			Page<Works> pageWorks = worksModel.paginate(pageIndex, pageSize, "select *", from.toString());
			setAttr("pageWorks", pageWorks);
		} catch (Exception e) {
			e.printStackTrace();
		}
		render("/works/showWorks.htm");
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
	public void getWorkById(){
		Work workModel = getModel(Work.class);
		Work work = workModel.findById(getParaToInt("id"));
		setAttr("work", work);
		render("/works/editWork.htm");
	}
}
