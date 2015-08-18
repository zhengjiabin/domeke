package com.domeke.app.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.Comment;
import com.domeke.app.model.Record;
import com.domeke.app.model.User;
import com.domeke.app.route.ControllerBind;
import com.domeke.app.utils.CollectionKit;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "comment")
public class CommentController extends Controller {
	
	private static final String COMMENTID = "commentid";
	
	private static final String TARGETID = "targetid";
	
	private static final String IDTYPE = "idtype";
	
	private static final String PID = "pid";
	
	private static final String USER = "user";
	
	private static final String USERID = "userid";
	
	private static final String USERIP = "userip";
	
	private static final String TOUSERID = "touserid";
	
	private static final String LEVEL = "level";
	
	private static final String MESSAGE = "message";
	/** 当前页号 */
	private static final String PAGENUMBER = "pageNumber";
	/** 每页显示记录数 */
	private static final String PAGESIZE = "pageSize";
	/** 跳转指令 */
	private static final String RENDER = "render";
	/** 记录类型 */
	private static final String RECORDTYPE = "recordtype";
	
	
	/**
	 * 设置分页回复信息
	 */
	public void index() {
		Object targetId = getAttr(TARGETID);
		Object idtype = getAttr(IDTYPE);
		int pageNumber = getParaToInt(PAGENUMBER, 1);
		int pageSize = getParaToInt(PAGESIZE, 10);
		
		setBodyInfo(targetId, idtype, pageNumber, pageSize);

		String render = getAttr(RENDER);
		render(render);
	}

	/**
	 * 发表信息
	 * <p>
	 * 请求 comment/publish?targetid=${targetid!}&idtype=${idtype!}&message=${message!}&render=${render!}&pageSize=${pageSize!}
	 */
	@Before(LoginInterceptor.class)
	public void publish() {
		try{
			save();
		}catch(Exception e){
			renderJson(false);
			return;
		}
		keepPara(TARGETID,IDTYPE,RENDER,PAGESIZE);
		
		String targetid = getPara(TARGETID);
		String idtype = getPara(IDTYPE);
		int pageSize = getParaToInt(PAGESIZE, 10);
		int pageNumber = getLastPageNumber(targetid, idtype, pageSize);
		setBodyInfo(targetid, idtype, pageNumber, pageSize);
		
		String render = getPara(RENDER);
		render(render);
	}
	
	/**
	 * 回复信息
	 * <p>
	 * 请求 comment/reply?targetid=${targetid!}&idtype=${idtype!}&pid=${pid!}&touserid=${touserid!}&message=${message!}&render=${render!}
	 */
	@Before(LoginInterceptor.class)
	public void reply(){
		try{
			save();
		}catch(Exception e){
			renderJson(false);
			return;
		}
		keepPara(TARGETID,IDTYPE,RENDER);
		
		String targetid = getPara(TARGETID);
		String idtype = getPara(IDTYPE);
		String pid = getPara(PID);
		setBodyInfo(targetid, idtype, pid);
		
		String render = getPara(RENDER);
		render(render);
	}
	
	/**
	 * 添加记录次数
	 * 请求 ./comment/addRecord?commentid={commentid!}&recordtype={recordtype!}
	 */
	@Before(LoginInterceptor.class)
	public void addRecord(){
		String commentId = getPara(COMMENTID);
		String recordType = getPara(RECORDTYPE);
		boolean isExists = checkHasRecord(commentId, recordType);
		if(isExists){
			renderJson(1);
			return;
		}
		saveRecord(commentId, recordType);
		Long number = getRecordNumber(commentId, recordType);
		renderJson("number",number);
	}
	
	/**
	 * 统计记录次数
	 * @param commentid 回复id
	 * @param recordType 记录类型
	 */
	private Long getRecordNumber(Object commentId, Object recordType) {
		return Record.dao.getRecordsNumberOfComment(commentId, recordType);
	}
	
	/**
	 * 查询是否已经记录过
	 * @param commentid　回复主键
	 * @param recordtype　记录类型
	 * @return true/false
	 */
	private boolean checkHasRecord(Object commentId,Object recordType){
		Object userId = getUserId();
		Record record = Record.dao.getRecordsOfComment(userId, commentId, recordType);
		if(record != null){
			return true;
		}
		return false;
	}
	
	/**
	 * 添加计数记录
	 * @param commentid 回复主键
	 * @param recordtype 记录类型
	 */
	private void saveRecord(Object commentId,Object recordType){
		Object userId = getUserId();
		HttpServletRequest request = getRequest();
		String remoteIp = request.getRemoteAddr();
		
		Record record = getModel(Record.class);
		record.set(COMMENTID, commentId);
		record.set(RECORDTYPE, recordType);
		record.set(USERID, userId);
		record.set(USERIP, remoteIp);
		record.save();
	}
	
	/**
	 * 根据每页显示的记录数获取最大的页号
	 * @param pageSize
	 * @return
	 */
	private int getLastPageNumber(Object targetid,Object idtype,int pageSize){
		int totalRow = Comment.dao.getCount(targetid, idtype).intValue();
		if(pageSize == 0){
			return 1;
		}else if(totalRow % pageSize == 0){
			return totalRow/pageSize;
		}else{
			return totalRow/pageSize + 1;
		}
	}

	/**
	 * 保存回复信息
	 */
	private void save() {
		Comment comment = getModel(Comment.class);

		Object targetId = getPara(TARGETID);
		comment.set(TARGETID, targetId);

		String idtype = getPara(IDTYPE);
		comment.set(IDTYPE, idtype);

		Object userId = getUserId();
		comment.set(USERID, userId);

		HttpServletRequest request = getRequest();
		String remoteIp = request.getRemoteAddr();
		comment.set(USERIP, remoteIp);

		comment.set(LEVEL, "1");
		String pId = getPara(PID);
		if (StrKit.notBlank(pId)) {
			comment.set(PID, pId);
			comment.set(LEVEL, "2");
		}

		String toUserId = getPara(TOUSERID);
		if(StrKit.notBlank(toUserId)){
			comment.set(TOUSERID, toUserId);
		}

		Object message = getPara(MESSAGE);
		comment.set(MESSAGE, message);
		comment.save();
	}
	
	/**
	 * 设置回复主体信息
	 * @param targetId 目标id
	 * @param idtype 回复类型
	 * @param pageNumber 当前页号
	 * @param pageSize 每页显示记录数
	 */
	private void setBodyInfo(Object targetId, Object idtype, int pageNumber, int pageSize) {
		Page<Comment> publishPage = setBodyInfoByPublish(targetId, idtype,pageNumber, pageSize);
		setBodyInfoByReply(targetId, idtype, publishPage);
	}
	
	/**
	 * 设置回复主体信息
	 * @param targetid 目标id
	 * @param idtype 回复类型
	 * @param pid 发表信息id
	 */
	private void setBodyInfo(Object targetid, Object idtype,Object pid){
		Comment publish = handlePublsh(pid);
		setBodyInfoByReply(targetid, idtype, publish);
	}
	
	/**
	 * 设置回复主体信息
	 * @param targetId 目标id
	 * @param idtype 回复类型
	 * @param publishPage 发表信息
	 */
	private void setBodyInfoByReply(Object targetId, Object idtype, Page<Comment> publishPage) {
		List<Comment> replyList = handleReplyList(publishPage, targetId, idtype);
		handleReplyRecords(replyList, targetId, idtype);
		handleReplyRecordsByUser(replyList, targetId, idtype);
	}
	
	/**
	 * 设置回复主体信息
	 * @param targetid 目标id
	 * @param idtype 回复类型
	 * @param publish 发表信息
	 */
	private void setBodyInfoByReply(Object targetid, Object idtype, Comment publish) {
		List<Comment> replyList = handleReplyList(publish, targetid, idtype);
		handleReplyRecords(replyList, targetid, idtype);
		handleReplyRecordsByUser(replyList, targetid, idtype);
	}
	
	/**
	 * 设置发表主体信息
	 * @param targetId 目标id
	 * @param idtype 回复类型
	 * @param pageNumber 当前页号
	 * @param pageSize 每页显示记录数
	 * @return
	 */
	private Page<Comment> setBodyInfoByPublish(Object targetId, Object idtype,
			int pageNumber, int pageSize) {
		Page<Comment> publishPage = handlePublishPage(targetId, idtype,pageNumber,pageSize);
		handlePublishRecords(publishPage, targetId, idtype);
		handlePublishRecordsByUser(publishPage, targetId, idtype);
		return publishPage;
	}
	
	/**
	 * 设置当前用户的回复记录次数
	 * @param replyList 回复信息
	 * @param targetid 目标id
	 * @param idtype 回复类型
	 */
	private Map<String, Record> handleReplyRecordsByUser(List<Comment> replyList, Object targetid, Object idtype) {
		if(CollectionKit.isBlank(replyList)){
			return null;
		}
		Object userid = getUserId();
		if(userid == null){
			return null;
		}
		List<Object> commentIdList = CollectionKit.getFieldValueList(replyList, COMMENTID, Object.class);
		Map<String, Record> replyUserRecord = Record.dao.getRecordsGroupOfCommentByUser(targetid, idtype, userid, commentIdList);
		setAttr("replyUserRecord", replyUserRecord);
		return replyUserRecord;
	}
	
	/**
	 * 设置当前用户的发表记录次数
	 * @param publishPage 发表分页信息
	 * @param targetid 目标id
	 * @param idtype 回复类型
	 */
	private Map<String, Record> handlePublishRecordsByUser(Page<Comment> publishPage, Object targetid, Object idtype) {
		if(publishPage == null){
			return null;
		}
		Object userid = getUserId();
		if(userid == null){
			return null;
		}
		List<Comment> publishList = publishPage.getList();
		if(CollectionKit.isBlank(publishList)){
			return null;
		}
		List<Object> commentIdList = CollectionKit.getFieldValueList(publishList, COMMENTID, Object.class);
		Map<String, Record> publishUserRecord = Record.dao.getRecordsGroupOfCommentByUser(targetid, idtype, userid, commentIdList);
		setAttr("publishUserRecord", publishUserRecord);
		return publishUserRecord;
	}
	
	/**
	 * 设置分页发表信息
	 * @param targetid 目标主键
	 * @param idtype 回复类型
	 * @param pageNumber 当前页号
	 * @param pageSize 每页显示记录数
	 */
	private Page<Comment> handlePublishPage(Object targetid, Object idtype,int pageNumber,int pageSize) {
		Page<Comment> publishPage = Comment.dao.findPageOfPublish(targetid, idtype, pageNumber, pageSize);
		setAttr("publishPage", publishPage);
		return publishPage;
	}

	/**
	 * 设置回复信息
	 * @param publishPage 发表信息
	 * @param targetid 目标主键
	 * @param idtype 回复类型
	 */
	private List<Comment> handleReplyList(Page<Comment> publishPage,Object targetid, Object idtype) {
		if(publishPage == null){
			return null;
		}
		List<Comment> publishList = publishPage.getList();
		if(CollectionKit.isBlank(publishList)){
			return null;
		}
		List<Object> commentIdList = CollectionKit.getFieldValueList(publishList, COMMENTID, Object.class);
		List<Comment> replyList = Comment.dao.findReply(targetid,idtype,commentIdList);
		setAttr("replyList", replyList);
		return replyList;
	}
	
	/**
	 * 设置回复信息
	 * @param publish 发表信息
	 * @param targetid 目标主键
	 * @param idtype 回复类型
	 */
	private List<Comment> handleReplyList(Comment publish,Object targetid, Object idtype) {
		if(publish == null || targetid == null || idtype == null){
			return null;
		}
		Object comment = publish.get(COMMENTID);
		List<Comment> replyList = Comment.dao.findReply(targetid,idtype,comment);
		setAttr("replyList", replyList);
		return replyList;
	}
	
	/**
	 * 设置发表记录次数
	 * @param publishPage 发表分页信息
	 * @param targetid 目标id
	 * @param idtype 回复类型
	 */
	private Map<String, Record> handlePublishRecords(Page<Comment> publishPage, Object targetid, Object idtype) {
		if(publishPage == null){
			return null;
		}
		List<Comment> publishList = publishPage.getList();
		if(CollectionKit.isBlank(publishList)){
			return null;
		}
		List<Object> commentIdList = CollectionKit.getFieldValueList(publishList, COMMENTID, Object.class);
		Map<String, Record> publishRecord = Record.dao.getRecordsGroupOfComment(targetid, idtype, commentIdList);
		setAttr("publishRecord", publishRecord);
		return publishRecord;
	}
	
	/**
	 * 设置回复记录次数
	 * @param replyList 回复信息
	 * @param targetid 目标id
	 * @param idtype 回复类型
	 */
	private Map<String, Record> handleReplyRecords(List<Comment> replyList, Object targetid, Object idtype) {
		if(CollectionKit.isBlank(replyList)){
			return null;
		}
		List<Object> commentIdList = CollectionKit.getFieldValueList(replyList, COMMENTID, Object.class);
		Map<String, Record> replyRecord = Record.dao.getRecordsGroupOfComment(targetid, idtype, commentIdList);
		setAttr("replyRecord", replyRecord);
		return replyRecord;
	}
	
	/**
	 * 设置发表信息
	 * @param commentid 发表信息id
	 * @return
	 */
	private Comment handlePublsh(Object commentid){
		if(commentid == null){
			return null;
		}
		Comment publish = Comment.dao.findById(commentid);
		setAttr("publish", publish);
		return publish;
	}

	/**
	 * 获取用户Id
	 * 
	 * @return
	 */
	private Object getUserId() {
		User user = getUser();
		if (user == null) {
			return null;
		}
		return user.get(USERID);
	}

	/**
	 * 获取登录User对象
	 * 
	 * @return user
	 */
	private User getUser() {
		User user = getSessionAttr(USER);
		return user;
	}
}
