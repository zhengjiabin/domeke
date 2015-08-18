/**
 * 
 */
package com.domeke.app.controller;

import java.util.List;
import java.util.Map;

import com.domeke.app.model.CodeTable;
import com.domeke.app.model.History;
import com.domeke.app.model.Work;
import com.domeke.app.model.Works;
import com.domeke.app.model.WorksType;
import com.domeke.app.route.ControllerBind;
import com.domeke.app.utils.CodeKit;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.core.Controller;
import com.jfinal.kit.ParseDemoKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

/**
 * @author lijiasen@domeke.com
 * 
 */
@ControllerBind(controllerKey = "/cartoon")
public class CartoonController extends Controller {
    
    /** 回复类型 */
    private static String WORKSIDTYPE = "40";
    
    /** 回复类型 */
    private static String WORKIDTYPE = "70";
    
    public void index() {
        String menuid = getPara("menuid");
        if (StrKit.isBlank(menuid)) {
            menuid = "2";
        }
        Works worksModel = getModel(Works.class);
        WorksType worksTypeModel = getModel(WorksType.class);
        List<WorksType> worksTypes = worksTypeModel.getWorksTypesByCartoonDesc("");
        
        Map<String, Object> typeMap = Maps.newHashMap();
        for (WorksType worksType : worksTypes) {
            typeMap.put(worksType.get("id").toString(), worksType.get("name"));
        }
        
        //加载首页8个循环显示
        List<Works> workss0Temp = worksModel.getHomePage(8);
        List<Map<String, Object>> workss0 = Lists.newArrayList();
        workss0 = ParseDemoKit.worksParse(workss0Temp);
        
        //加载中间数据
        List<Works> workss1temp = worksModel.getWorksInfoByType(worksTypes.get(0).get("id").toString(), 5);
        List<Map<String, Object>> workss1 = Lists.newArrayList();
        workss1 = ParseDemoKit.worksParse(workss1temp);
        
        List<Works> workss2temp = worksModel.getWorksInfoByType(worksTypes.get(1).get("id").toString(), 6);
        List<Map<String, Object>> workss2 = Lists.newArrayList();
        workss2 = ParseDemoKit.worksParse(workss2temp);
        
        List<Works> workss3temp = worksModel.getWorksInfoByType(worksTypes.get(2).get("id").toString(), 12);
        List<Map<String, Object>> workss3 = Lists.newArrayList();
        workss3 = ParseDemoKit.worksParse(workss3temp);
        
        List<Works> workss4temp = worksModel.getWorksInfoByType(worksTypes.get(3).get("id").toString(), 9);
        List<Map<String, Object>> workss4 = Lists.newArrayList();
        workss4 = ParseDemoKit.worksParse(workss4temp);
        
        List<Works> workss5temp = worksModel.getWorksInfoByType(worksTypes.get(4).get("id").toString(), 6);
        List<Map<String, Object>> workss5 = Lists.newArrayList();
        workss5 = ParseDemoKit.worksParse(workss5temp);
        
        // 用于显示“大家都爱看”列表
        List<Map<String, Object>> worksList = ParseDemoKit.worksParse(worksModel.getlastWeekByPageViewsLimit(5));
        
        setAttr("olikeWorksList", worksList);
        setAttr("menuid", menuid);
        setAttr("workstype", typeMap);
        
        setAttr("workss0", workss0);
        setAttr("workss1", workss1);
        setAttr("workss2", workss2);
        setAttr("workss3", workss3);
        setAttr("workss4", workss4);
        setAttr("workss5", workss5);
        render("/CartoonCategory.html");
    }
    
    /**
     * 根据动漫作品的类型显示动漫作品的列表<br>
     * 请求：cartoon/showWorksList?wtype=${works.workstype!}&pnum=xx <br>
     * wtype默认“10”；pnum默认1
     */
    public void showWorksList() {
        Works works = getModel(Works.class);
        String workstype = getPara("workstype");
        String pageNumberStr = getPara("pageNumber");
        Integer pageNumber = 1;
        if (!StrKit.isBlank(pageNumberStr)) {
            pageNumber = Integer.parseInt(pageNumberStr);
        }
        String pageSizeStr = getPara("pageSize");
        Integer pageSize = 35;
        if (!StrKit.isBlank(pageSizeStr)) {
            pageSize = Integer.parseInt(pageSizeStr);
        }
        
        WorksType worksTypeModel = getModel(WorksType.class);
        worksTypeModel = worksTypeModel.findById(workstype);
        
        Page<Works> list = works.getWorksInfoByTypePage(workstype, pageNumber, pageSize);
        @SuppressWarnings({"unchecked", "rawtypes"})
        Page<Map<String, Object>> pageWorks = new Page(ParseDemoKit.worksParse(list.getList()), list.getPageNumber(), list.getPageSize(), list.getTotalPage(), list.getTotalRow());
        
        setAttr("workstypename", worksTypeModel.get("name"));
        setAttr("workstypeid", worksTypeModel.get("id"));
        setAttr("menuid", "2");
        setAttr("pageList", pageWorks);
        render("/CartoonSubModule.html");
    }
    
    /**
     * 根据动漫作品的ID，显示动漫的明细<br>
     * 请求：cartoon/showDetail?id=${works.worksid!}
     */
    public void showDetail() {
        Works worksModel = getModel(Works.class);
        Work workModel = getModel(Work.class);
        Integer worksId = getParaToInt("id");
        // 取某一部动漫
        Works works = worksModel.findById(worksId);
        // 取该动漫的集数
        List<Work> workList = workModel.getWorkByWorksIdCheck(worksId);
        // 取精彩推荐的动漫作品
        List<Works> wonderfulWorksList = worksModel.getNewestWorks(worksId, 1, 5);
        
        this.setAttr("works", works);
        this.setAttr("workList", workList);
        this.setAttr("wonderfulWorksList", wonderfulWorksList);
        
        String action = "/comment/setPage";
        setAttr("render", "/CartoonDtl.html");
        setAttr("targetId", worksId);
        setAttr("idtype", WORKSIDTYPE);
        forwardAction(action);
    }
    
    /**
     * 明细页面点击动漫链接，转播放<br>
     * cartoon/playVideo?id=${works.worksid!} or cartoon/playVideo?id=${works.worksid!}&gid=${work.workid!}
     */
    public void playVideo() {
        Works worksModel = getModel(Works.class);
        Integer worksid = getParaToInt("id");
        Works worksData = worksModel.findById(worksid);
        this.setAttr("works", worksData);
        Work workModel = getModel(Work.class);
        Integer workid = getParaToInt("gid");
        Work workData = null;
        
        // 如果没有传动漫作品集数的ID则取第一集播放
        if (workid == null) {
            workData = workModel.getFirstWork(worksid);
        } else {
            workData = workModel.findById(workid);
        }
        //播放数加1
        History historyModel = getModel(History.class);
        historyModel.saveOrUpdateHitory(workData);
        this.setAttr("work", workData);
        String action = "/comment/setPage";
        setAttr("render", "/CartoonPlay.html");
        setAttr("targetId", workData.get("workid"));
        setAttr("idtype", WORKIDTYPE);
        forwardAction(action);
    }
    
    /**
     * 获取动漫作品类型的码表
     * 
     * @return
     */
    private Map<String, Object> getWtypeCodeTable() {
        List<CodeTable> wtypeCTList = CodeKit.getList("workstype");
        Map<String, Object> wtypeCTMap = Maps.newHashMap();
        for (CodeTable codeTable : wtypeCTList) {
            wtypeCTMap.put((String)codeTable.get("codekey"), codeTable.get("codecalue"));
        }
        return wtypeCTMap;
    }
}
