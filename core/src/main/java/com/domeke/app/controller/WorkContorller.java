package com.domeke.app.controller;

import com.domeke.app.Schedule.WorkSchedule;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;

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
}
