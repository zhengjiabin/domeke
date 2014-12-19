package com.domeke.app.controller;

import com.domeke.app.Schedule.WorkSchedule;
import com.domeke.app.model.Work;
import com.domeke.app.model.Works;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

@ControllerBind(controllerKey = "/work")
public class WorkContorller extends Controller {

	/**
	 * 压缩视频请求
	 * 请求 work/compressWork?workid=${workid!}
	 */
	public void compressWork() {
		try {
			String workId = getPara("workId");
			WorkSchedule schedule = new WorkSchedule();
			schedule.handleVideoFile(workId);
			renderJson("1");
			return;
		} catch (Exception e) {
			e.printStackTrace();
			renderJson("0");
		}
	}
	
	public void praise(){
		String workid = getPara("workid");
		if(StrKit.isBlank(workid)){
			return;
		}
		Work workModel = getModel(Work.class).findById(workid);
		if(workModel != null && workModel.isNotEmpty()){
			//加点赞数
			Integer praise = Integer.parseInt(String.valueOf(workModel.get("praise")));
			workModel.set("praise", praise + 1);
			workModel.update();
			
			Works worksModel = getModel(Works.class);
			worksModel.addPraise(workModel.get("worksid"));
		}
	}
}
