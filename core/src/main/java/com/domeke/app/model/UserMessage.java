package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * @author 陈智聪
 *DROP TABLE IF EXISTS `user_message`;
CREATE TABLE `user_message` (
  `messageid` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(512) DEFAULT NULL,
  `content` varchar(4000) DEFAULT NULL,
  `from` varchar(32) DEFAULT NULL,
  `to` varchar(32) DEFAULT NULL,
  `creater` varchar(32) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modifier` varchar(32) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `status` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`messageid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
@TableBind(tableName="user_message", pkName="messageid")
public class UserMessage extends Model<UserMessage> {

	public static UserMessage userMessageDao = new UserMessage();
	
	public void saveUserMessage(){
		this.save();
	}
	
	public List<UserMessage> selectUserMessage(){
		List<UserMessage> userMessageList = this.find("select * from user_message");
		return userMessageList;
	}
	
	public UserMessage selectUserMessageById(int userMessageId){
		UserMessage userMessage = this.findById(userMessageId);
		return userMessage;
	}
	
	public void updateUserMessage(){
		this.update();
	}
	
	public void deleteUserMessage(int userMessageId){
		this.deleteById(userMessageId);
	}
	
	public void removeCache(){
		CacheKit.removeAll("UserMessage");
		CacheKit.removeAll("userMessageList");
	}
}
