package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.model.UserMessage;
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
}
