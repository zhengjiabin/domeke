package com.domeke.app.controller;

import java.util.ArrayList;
import java.util.List;

import com.domeke.app.model.User;
import com.domeke.app.model.UserMessage;
import com.domeke.app.model.UserReplyMsg;
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
		msg.set("fromuser", user.getStr("username"));
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
			msgList = msg.getAllLeaveMsg(user.getStr("username"));
		}else if("6".equals(msgType)){
			//用户留言
			msgList = msg.getLeaveMsg("touser", user.getStr("username"), null,"sendtype","0");
		}else if("7".equals(msgType)){
			//系统留言
			msgList = msg.getLeaveMsg("touser", user.getStr("username"), null,"sendtype","1");
		}else if("11".equals(msgType)){
			//我发的留言
			msgList = msg.getLeaveMsg("fromuser", user.getStr("username"), null,null,null);
		}
		List<String> imgUrls = new ArrayList<String>();
		for (UserMessage um:msgList) {
			String imgUrl = User.dao.getImgURL(um.get("fromuser"));
			imgUrls.add(imgUrl);
		}
		setAttr("imgUrls", imgUrls);
		setAttr("msgList", msgList);
	}
	public void delMsg(){
		UserMessage msg = getModel(UserMessage.class);
		String messageid = getPara("messageid");
		msg.deleteById(messageid);
		forLeaveMsg();
	}
	
	public void forReplyMsg(){
		String msgid = getPara("msgid");
		UserMessage msg = getModel(UserMessage.class);
		msg = msg.findById(msgid);
		String mid = getPara("mid");
		setAttr("menuId", "9");
		setAttr("mid", mid);
		setAttr("msg", msg);
		setAttr("msgid", msgid);
		render("/personalCenter.html");
	}
	public void replayMsg(){
		UserReplyMsg reply = getModel(UserReplyMsg.class);
		UserMessage usermsg = getModel(UserMessage.class);
		User user = getSessionAttr("user");
		String msgid = getPara("msgid");
		String msgvalue = getPara("msgvalue");
		String mid = getPara("mid");
		usermsg = usermsg.findById(msgid);
		reply.set("msgid", msgid);
		reply.set("msgvalue", msgvalue);
		reply.set("fromuser", user.getStr("username"));
		reply.save();
		Long count = usermsg.getLong("count");
		count = count+1;
		usermsg.set("count", count);
		usermsg.update();
		setAttr("menuid", "1");
		getLeaveMsg(mid);
		setAttr("menuId", mid);
		render("/personalCenter.html");
	}
	public void countLeave(){
		String msgid = getPara("msgid");
		UserMessage msg = getModel(UserMessage.class);
		msg = msg.findById(msgid);
		UserReplyMsg reply = getModel(UserReplyMsg.class);
		List<UserReplyMsg> replyList = reply.getList(msgid);
		setAttr("menuid", "1");
		setAttr("msg", msg);
		setAttr("menuId", "10");
		setAttr("replyList", replyList);
		render("/personalCenter.html");
	}
}
