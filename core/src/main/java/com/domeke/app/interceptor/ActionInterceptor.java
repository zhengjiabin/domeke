package com.domeke.app.interceptor;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import com.domeke.app.model.Action;
import com.domeke.app.model.User;
import com.domeke.app.model.UserAction;
import com.domeke.app.utils.CodeKit;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

public class ActionInterceptor implements Interceptor{

	@Override
	public void intercept(ActionInvocation ai) {
		ai.invoke();
		Controller controller = ai.getController();
		String methodName = ai.getMethod().getName();
		//判断拦截到的方法是否指定的动作
		Action action = Action.dao.getActionByName(methodName);
		if(action != null && action.isNotEmpty()){
			//获取用户
			User user = (User) controller.getSession().getAttribute("user");
			if(user == null){
				return;
			}
			//获取用户触发的积分事件值
			Integer maxnum = action.get("maxnum");
			Integer peas = action.get("peas");
			Integer point = action.get("point");
			Integer limits = action.get("limits");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date nowDate = new Date();
			String startTime = sdf.format(nowDate);
			String endTime = sdf.format(nowDate);
			//获取用户行为次数 并判断是否修改积分豆豆
			if(limits != 0){
				startTime = sdf.format(DateUtils.addDays(nowDate, -limits));
			}
			UserAction userAction = UserAction.dao.getUserAction(user.getLong("userid"), action.getLong("actionid"),startTime,endTime);
			if(userAction == null){
				userAction =  new UserAction();
				Long userid = user.getLong("userid");
				String username = user.getStr("username");
				userAction.set("userid", userid);
				userAction.set("username", username);
				userAction.set("actionid", action.getLong("actionid"));
				userAction.set("actionname", action.getStr("name"));
				userAction.set("actiondes", action.getStr("des"));
				userAction.set("times", 0);
				userAction.set("create", 1);
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				userAction.set("create_time", timestamp);
				userAction.saveUserAction();
			}
			Integer times = userAction.getInt("times");
			if(times < maxnum){
				user = User.dao.findById(user.getLong("userid"));
				Integer userPeas = user.getInt("peas");
				Integer userPoint = user.getInt("point");
				userPeas = userPeas + peas;
				userPoint = userPoint + point;
				user.set("peas", userPeas);
				user.set("point", userPoint);
				user.update();
				times = times + 1;
				userAction.set("times", times);
				userAction.update();
			}
		}
		
	}
}
