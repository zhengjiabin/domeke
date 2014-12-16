package com.domeke.app.interceptor;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;

import com.domeke.app.model.Action;
import com.domeke.app.model.Menu;
import com.domeke.app.model.SearchKey;
import com.domeke.app.model.User;
import com.domeke.app.model.UserAction;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

/**
 * 
 * @author lijiasen
 * 全局拦截区
 */
public class GlobalInterceptor implements Interceptor {
	public void intercept(ActionInvocation ai) {

		Menu.menuDao.removeCache();
		List<Menu> topMenuList = Menu.menuDao.getTopMenu();
		List<SearchKey> searchKeyList = SearchKey.searchdao.getSearchKey();
		Controller controller = ai.getController();
		controller.setAttr("topMenuList", topMenuList);
		List<Menu> leftMenuList = null;
		controller.setAttr("leftMenuList", leftMenuList);
		controller.setAttr("searchKeyList", searchKeyList);
		ai.invoke();
		try {
			String controllName = controller.getClass().getName();
			controllName = controllName.substring(controllName.lastIndexOf(".") + 1,controllName.length());
			controllName = controllName.substring(0,controllName.indexOf("Controller"));
			controllName = controllName.toLowerCase();
			String actionName = controllName+"/"+ai.getMethodName();
			//System.out.println(actionName);
			Action action = Action.dao.getActionByName(actionName);
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
				if(limits == 1){
					nowDate.setHours(0);
					nowDate.setMinutes(0);
					nowDate.setSeconds(0);
					startTime = sdf.format(nowDate);
				}
				if(limits > 1){
					Date oldDate = DateUtils.addDays(nowDate, -limits);
					startTime = sdf.format(oldDate);
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
					userAction.set("peas", 0);
					userAction.set("point", 0);
					userAction.set("times", 0);
					userAction.set("create", 1);
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					userAction.set("create_time", timestamp);
					userAction.set("modifier", 1);
					userAction.set("modify_time", timestamp);
					userAction.saveUserAction();
				}
				Integer times = userAction.getInt("times");
				if(times < maxnum){
					user = User.dao.findById(user.getLong("userid"));
					Integer userPeas = Integer.parseInt(String.valueOf(user.get("peas")));
					Integer userPoint = Integer.parseInt(String.valueOf(user.get("point")));
					userPeas = userPeas + peas;
					userPoint = userPoint + point;
					user.set("peas", userPeas);
					user.set("point", userPoint);
					user.update();
					times = times + 1;
					userAction.set("times", times);
					Integer addPeas = Integer.parseInt(String.valueOf(userAction.get("peas")));
					addPeas = addPeas + peas;
					Integer addPoint = Integer.parseInt(String.valueOf(userAction.get("point")));
					addPoint = addPoint + point;
					userAction.set("peas", addPeas);
					userAction.set("point", addPoint);
					userAction.update();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
