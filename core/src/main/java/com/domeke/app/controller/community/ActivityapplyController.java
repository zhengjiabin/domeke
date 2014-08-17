package com.domeke.app.controller.community;

import com.domeke.app.model.community.Activityapply;
import com.jfinal.core.Controller;

public class ActivityapplyController extends Controller {

	public void save() {
		try {
			Activityapply activityapply = getModel(Activityapply.class);
			activityapply.save();
		} catch (Exception e) {
			e.printStackTrace();
			render("/community/activityapply.html");
			return;
		}
		render("/community/activity.html");
	}

	public void index() {
		render("/community/activityapply.html");
	}
}
