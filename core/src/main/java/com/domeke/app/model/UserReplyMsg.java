package com.domeke.app.model;

import java.util.List;

import com.domeke.app.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="user_replymsg", pkName="replyid")
public class UserReplyMsg extends Model<UserReplyMsg>{
	public static UserReplyMsg replyDao = new UserReplyMsg();
	
	public List<UserReplyMsg> getList(String msgid){
		String sql = "select * from user_replymsg where msgid='"+msgid+"' ORDER BY create_time desc";
		List<UserReplyMsg> list = replyDao.find(sql);
		return list;
	}
}
