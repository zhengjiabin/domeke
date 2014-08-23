package com.domeke.app.controller;

import java.util.List;

import com.domeke.app.model.MessageQueue;
import com.domeke.app.route.ControllerBind;
import com.jfinal.core.Controller;

/**
 * @author 陈智聪
 *
 */
@ControllerBind(controllerKey="mq")
public class MessageQueueController extends Controller {
	
	public void goMQ(){
		render("/demo/addMessageQueue.html");
	}
	
	public void saveMessageQueue(){
		MessageQueue messageQueue = getModel(MessageQueue.class);
		messageQueue.saveMessageQueue();
		selectMessageQueue();
	}
	
	public void selectMessageQueue(){
		MessageQueue.messageQueueDao.removeCache();
		MessageQueue messageQueue = getModel(MessageQueue.class);
		List<MessageQueue> messageQueueList =messageQueue.selectMessageQueue();
		setAttr("messageQueueList", messageQueueList);
		render("/demo/selectMessageQueue.html");
	}
	
	public void selectMessageQueueById(){
		int messageQueueId = getParaToInt();
		MessageQueue messageQueue = MessageQueue.messageQueueDao.selectMessageQueueById(messageQueueId);
		setAttr("messageQueue", messageQueue);
		render("/demo/updateMessageQueue.html");
	}
	
	public void updateMessageQueue(){
		MessageQueue messageQueue = getModel(MessageQueue.class);
		messageQueue.updateMessageQueue();
		selectMessageQueue();
	}
	
	public void deleteMessageQueue(){
		MessageQueue messageQueue = getModel(MessageQueue.class);
		int messageQueueId = getParaToInt();
		messageQueue.deleteMessageQueue(messageQueueId);
		selectMessageQueue();
	}
}
