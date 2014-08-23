package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * @author 陈智聪
 *DROP TABLE IF EXISTS `message_queue`;
CREATE TABLE `message_queue` (
  `messageid` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(512) DEFAULT NULL,
  `content` varchar(4000) DEFAULT NULL,
  `from` varchar(32) DEFAULT NULL,
  `to` varchar(4000) DEFAULT NULL,
  `creater` varchar(32) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modifier` varchar(32) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `status` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`messageid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

 */
@TableBind(tableName="message_queue", pkName="messageid")
public class MessageQueue extends Model<MessageQueue> {
	
	public static MessageQueue messageQueueDao = new MessageQueue();
	
	public void saveMessageQueue(){
		this.save();
	}
	
	public List<MessageQueue> selectMessageQueue(){
		List<MessageQueue> messageQueueList = this.find("select * from message_queue");
		return messageQueueList;
	}
	
	public MessageQueue selectMessageQueueById(int messageQueueId){
		MessageQueue messageQueue = this.findById(messageQueueId);
		return messageQueue;
	}
	
	public void updateMessageQueue(){
		this.update();
	}
	
	public void deleteMessageQueue(int messageQueueId){
		this.deleteById(messageQueueId);
	}
	
	public void removeCache(){
		CacheKit.removeAll("MessageQueue");
		CacheKit.removeAll("messageQueueList");
	}
}
