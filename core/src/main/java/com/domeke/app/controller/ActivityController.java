package com.domeke.app.controller;

import com.domeke.app.model.Activity;
import com.jfinal.core.Controller;

public class ActivityController extends Controller {

	public void save() {
		Activity activity = getModel(Activity.class);
		activity.save();
	}

	public void index() {
		render("/community/activity.html");
	}
}
