package com.domeke.app.controller;

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
import com.domeke.app.route.ControllerBind;
import com.domeke.app.utils.CodeKit;
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
		String flag = getPara("flag");
		String pageIndexStr = getPara("pageIndex");
		String pageSizeStr = getPara("pageSize");
		List<CodeTable> codeTables = CodeKit.getList("workstype");
		if("1".equals(flag)){
			setAttr("pageIndex", pageIndexStr);
			setAttr("workstype", codeTables);
			render("/works/addshipin.htm");
			return;
		}
		String coverPath = upLoadFile("cover", "", 2000 * 1024 * 1024, "utf-8");
		String comicPath = upLoadFile("comic", "", 2000 * 1024 * 1024, "utf-8");
		
		Integer pageIndex = 1;
		if(!StrKit.isBlank(pageIndexStr)){
			pageIndex = Integer.parseInt(pageIndexStr);
		}
		Integer pageSize = 10;
		if(!StrKit.isBlank(pageSizeStr)){
			pageSize = Integer.parseInt(pageSizeStr);
		}
		Works worksModel = getModel(Works.class);
		Work workModel = getModel(Work.class);
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
		worksModel.set("createtime", timestamp);
		worksModel.set("creater", 111);
		worksModel.set("modifytime", timestamp);
		worksModel.set("modifier", 111);
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
			Page<Works> pageWorks = worksModel.getWorksInfoPage("", "1", pageIndex, pageSize);
			setAttr("pageWorks", pageWorks);
			render("/works/showworkList.htm");
			return;
		}else{
			setAttr("works", worksModel);
			setAttr("workstype", codeTables);
			setAttr("message", "添加失败!");
			render("/works/addshipin.htm");
			return;
		}
	}
	public void addWork(){
		String flag = getPara("flag");
		String worksidStr = getPara("worksid");
		String pageIndexStr = getPara("pageIndex");
		String pageSizeStr = getPara("pageSize");
		if("1".equals(flag)){
			setAttr("worksid", worksidStr);
			setAttr("pageIndex", pageIndexStr);
			render("/works/addwork.htm");
			return;
		}
		String comicPath = upLoadFile("comic", "", 2000 * 1024 * 1024, "utf-8");
		pageIndexStr = getPara("pageIndex");
		Integer pageIndex = 1;
		if(!StrKit.isBlank(pageIndexStr)){
			pageIndex = Integer.parseInt(pageIndexStr);
		}
		Integer pageSize = 10;
		if(!StrKit.isBlank(pageSizeStr)){
			pageSize = Integer.parseInt(pageSizeStr);
		}
		try {
			Work workModel = getModel(Work.class);
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			workModel.set("comic", comicPath);
			workModel.set("creater", 1);
			workModel.set("modifier", 1);
			workModel.set("createtime", timestamp);
			workModel.set("modifytime", timestamp);
			boolean bool = workModel.save();
			if(bool){
				//成功
				Integer worksid = Integer.parseInt(String.valueOf(workModel.get("worksid")));
				Works works = getModel(Works.class).findById(worksid);
				Page<Work> pageWork = workModel.getWorkByWorksID(worksid, pageIndex, pageSize);
				setAttr("works", works);
				setAttr("pageWork", pageWork);
				render("/works/showworks.htm");
				return;
			}else{
				//失败
				setAttr("worksid", worksidStr);
				setAttr("pageIndex", pageIndexStr);
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
			String flag = getPara("flag");
			String pageIndexStr = getPara("pageIndex");
			String pageSizeStr = getPara("pageSize");
			List<CodeTable> codeTables = CodeKit.getList("workstype");
			if("1".equals(flag)){
				setAttr("workstype", codeTables);
				setAttr("pageIndex", pageIndexStr);
				render("/works/addworks.htm");
				return;
			}
			String coverPath = upLoadFile("cover", "", 2000 * 1024 * 1024, "utf-8");
			pageIndexStr = getPara("pageIndex");
			pageSizeStr = getPara("pageSize");
			Integer pageIndex = 1;
			if(!StrKit.isBlank(pageIndexStr)){
				pageIndex = Integer.parseInt(pageIndexStr);
			}
			Integer pageSize = 10;
			if(!StrKit.isBlank(pageSizeStr)){
				pageSize = Integer.parseInt(pageSizeStr);
			}
			User user = getSessionAttr("user");
			
			if(StrKit.isBlank(coverPath)){
				coverPath = "";
			}
			Works worksModel = getModel(Works.class);
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			worksModel.set("cover", coverPath);
			worksModel.set("type", 0);
			UserRole userrole = getModel(UserRole.class).getRolid(user.get("userid"));
			if(userrole.get("roleid") == "1"){
				worksModel.set("ischeck", 1);
			}else {
				worksModel.set("ischeck", 0);
			}
			worksModel.set("istop", 0);
			worksModel.set("creativeprocess", 20);
			worksModel.set("comment", 0);
			worksModel.set("pageviews", 0);
			worksModel.set("collection", 0);
			worksModel.set("praise", 0);
			worksModel.set("maxnum", 0);
			worksModel.set("releasedate", timestamp);
			worksModel.set("updatetime", timestamp);
			worksModel.set("leadingrole", 111);
			worksModel.set("creater", 111);
			worksModel.set("createtime", timestamp);
			worksModel.set("modifier", 111);
			worksModel.set("modifytime", timestamp);
			boolean bool = worksModel.save();
			if(bool){
				//成功
				Page<Works> pageWorks = worksModel.getWorksInfoPage("", "0", pageIndex, pageSize);
				setAttr("pageWorks", pageWorks);
				render("/works/showworksList.htm");
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
		String workidStr = getPara("workid");
		String pageIndexStr = getPara("pageIndex");
		String pageSizeStr = getPara("pageSize");
		String flag = getPara("flag");
		if("1".equals(flag)){
			Work workModel = getModel(Work.class).findById(workidStr);
			Works worksModel = getModel(Works.class).findById(worksidStr);
			setAttr("works", worksModel);
			setAttr("work", workModel);
			setAttr("pageIndex", pageIndexStr);
			render("/works/editwork.htm");
			return;
		}
		String worknumStr = getPara("worknum");
		String worknameStr = getPara("workname");
		String workdesStr = getPara("workdes");
		String isdisableStr = getPara("isdisable");
		if(StrKit.isBlank(workidStr)){
			render("/works/editwork.htm");
			return;
		}
		Integer pageIndex = 1;
		if(!StrKit.isBlank(pageIndexStr)){
			pageIndex = Integer.parseInt(pageIndexStr);
		}
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
			if(!StrKit.isBlank(isdisableStr)){
				workModel.set("isdisable", isdisableStr);
			}
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			workModel.set("modifier", 1);
			workModel.set("modifytime", timestamp);
			boolean bool = workModel.update();
			if(bool){
				//成功
				Page<Work> pageWork = null;
				pageWork = workModel.getWorkByWorksID(Integer.parseInt(worksidStr), pageIndex, pageSize);
				setAttr("works", worksModel);
				setAttr("pageWork", pageWork);
				render("/works/showworks.htm");
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
		String pageIndexStr = getPara("pageIndex");
		String pageSizeStr = getPara("pageSize");
		String flag = getPara("flag");
		if("1".equals(flag)){
			Works worksModel = getModel(Works.class).findById(worksidStr);
			setAttr("works", worksModel);
			setAttr("pageIndex", pageIndexStr);
			render("/works/editworks.htm");
			return;
		}
		Integer pageIndex = 1;
		if(!StrKit.isBlank(pageIndexStr)){
			pageIndex = Integer.parseInt(pageIndexStr);
		}
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
		String pageIndexStr = getPara("pageIndex");
		String pageSizeStr = getPara("pageSize");
		String flag = getPara("flag");
		if("1".equals(flag)){
			Works worksModel = getModel(Works.class).findById(worksid);
			Work workModel = getModel(Work.class).getWorkByWorksID(worksid).get(0);
			setAttr("pageIndex", pageIndexStr);
			setAttr("works", worksModel);
			setAttr("work", workModel);
			render("/works/editshipin.htm");
			return;
		}
		Integer pageIndex = 1;
		if(!StrKit.isBlank(pageIndexStr)){
			pageIndex = Integer.parseInt(pageIndexStr);
		}
		Integer pageSize = 10;
		if(!StrKit.isBlank(pageSizeStr)){
			pageSize = Integer.parseInt(pageSizeStr);
		}
		Works worksModel = getModel(Works.class).findById(worksid);
		Work workModel = getModel(Work.class).getWorkByWorksID(worksid).get(0);
		String worksname = getPara("worksname");
		String describle = getPara("describle");
		String isdisable = getPara("isdisable");
		
		if(!StrKit.isBlank(worksname)){
			worksModel.set("worksname", worksname);
			workModel.set("workname", worksname);
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
			pageWorks = worksModel.getWorksInfoPage("", "1", pageIndex, pageSize);
			setAttr("pageWorks", pageWorks);
			render("/works/showworkList.htm");
			return;
		}else{
			//失败
			setAttr("works", worksModel);
			setAttr("message", "添加失败!");
			render("/works/editshipin.htm");
			return;
		}
	}
	
	public void showShipin(){
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
			pageWorks = worksModel.getWorksInfoPage("", "1", pageIndex, pageSize);
			setAttr("pageWorks", pageWorks);
			render("/works/showworkList.htm");
			return;
		} catch (Exception e) {
			e.printStackTrace();
			render("/works/showworkList.htm");
			return;
		}
	}
	
	public void showWorks(){
		try {
			String worksid = getPara("worksid");
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
			String workstype = CodeKit.getValue("workstype", worksModel.get("workstype"));
			Page<Work> pageWork = getModel(Work.class).getWorkByWorksID(Integer.parseInt(worksid), pageIndex, pageSize);
			setAttr("works", worksModel);
			setAttr("workstype", workstype);
			setAttr("pageWork", pageWork);
			render("/works/showWorks.htm");
			return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	public void showWorksList(){
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
			render("/works/showworksList.htm");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		render("/works/showworksList.htm");
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
	
	public void showType(){
	}
}
