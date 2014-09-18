package com.domeke.app.controller;

import java.util.ArrayList;
import java.util.List;

import com.domeke.app.model.User;
import com.domeke.app.model.UserMessage;
import com.domeke.app.model.UserRole;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;

/**
 * @author 陈智聪
 *
 */
@ControllerBind(controllerKey="um")
public class UserMessageController extends Controller {
	
	public void goUM(){
		render("/demo/addUserMessage.html");
	}
	
	public void saveUserMessage(){
		UserMessage userMessage = getModel(UserMessage.class);
		userMessage.saveUserMessage();
		selectUserMessage();
	}
	
	public void selectUserMessage(){
		UserMessage.userMessageDao.removeCache();
		UserMessage userMessage = getModel(UserMessage.class);
		List<UserMessage> userMessageList =userMessage.selectUserMessage();
		setAttr("userMessageList", userMessageList);
		render("/demo/selectUserMessage.html");
	}
	
	public void selectUserMessageById(){
		int userMessageId = getParaToInt();
		UserMessage userMessage = UserMessage.userMessageDao.selectUserMessageById(userMessageId);
		setAttr("userMessage", userMessage);
		render("/demo/updateUserMessage.html");
	}
	
	public void updateUserMessage(){
		UserMessage userMessage = getModel(UserMessage.class);
		userMessage.updateUserMessage();
		selectUserMessage();
	}
	
	public void deleteUserMessage(){
		UserMessage userMessage = getModel(UserMessage.class);
		int userMessageId = getParaToInt();
		userMessage.deleteUserMessage(userMessageId);
		selectUserMessage();
	}
	
	public void leaveMsg(){
		UserMessage msg = getModel(UserMessage.class);
		String touser = getPara("touser");
		String title = getPara("title");
		String content = getPara("content");
		User user = getSessionAttr("user");
		UserRole role = getModel(UserRole.class);
		role = role.getRolid(user.get("userid"));
		if("1".equals(role.getLong("roleid"))){
			msg.set("sendtype", "1");
		}else{
			msg.set("sendtype", "0");
		}
		msg.set("from", user.getStr("username"));
		msg.set("touser", touser);
		msg.set("title", title);
		msg.set("content", content);
		msg.set("content", content);
		msg.set("status", "N");
		msg.save();
		forLeaveMsg();
	}
	public void forLeaveMsg(){
		String mid = getPara("menuId");
		User user = getSessionAttr("user");
		Long userId = user.getLong("userid");
		user = user.findById(userId);
		setAttr("userId", userId);
		if(mid == null){
			setAttr("menuId", "5");
		}else{
			setAttr("menuId", mid);
		}
		
		setAttr("menuid", "1");
		setAttr("imgurl", user.getStr("imgurl"));
		getLeaveMsg(mid);
		render("/personalCenter.html");
		
	}
	public void getLeaveMsg(String msgType){
		UserMessage msg = getModel(UserMessage.class);
		User user = getSessionAttr("user");
		List<UserMessage> msgList = new ArrayList<UserMessage>();
		if("5".equals(msgType)){
			//所有留言
			msgList = msg.getLeaveMsg("touser", user.getStr("username"), null,null,null);
		}else if("6".equals(msgType)){
			//用户留言
			msgList = msg.getLeaveMsg("touser", user.getStr("username"), null,"sendtype","0");
		}else if("7".equals(msgType)){
			//系统留言
			msgList = msg.getLeaveMsg("touser", user.getStr("username"), null,"sendtype","1");
		}
		setAttr("msgList", msgList);
	}
	public void delMsg(){
		UserMessage msg = getModel(UserMessage.class);
		String messageid = getPara("messageid");
		msg.deleteById(messageid);
	}
}
