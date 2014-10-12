package com.domeke.app.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.domeke.app.interceptor.LoginInterceptor;
import com.domeke.app.model.Comment;
import com.domeke.app.model.User;
import com.domeke.app.route.ControllerBind;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "comment")
public class CommentController extends Controller {

	/**
	 * 添加回复信息
	 * 请求 comment/reply?targetId = ${targetId!}&idtype=${idtype!}&pId=${pId!}&toUserId=${toUserId!}
	 */
	@Before(LoginInterceptor.class)
	public void reply() {
		String pId = getPara("pId");
		if (StrKit.isBlank(pId)) {
			replyComment();
		} else {
			replyFollow();
		}
		keepReplyPara();
		String render = getPara("render");
		render(render);
	}
	
	/**
	 * 分页查询回复信息
	 * 请求 comment/findByTargetId?targetId = ${targetId!}&idtype=${idtype!}
	 */
	public void findByTargetId() {
		String targetId = getPara("targetId");
		String idtype = getPara("idtype");
		setCommentPage(targetId, idtype);
		setFollowList(targetId, idtype);
		
		keepReplyPara();
		String render = getPara("render");
		render(render);
	}

	/**
	 * 添加主回复信息
	 */
	public void replyComment() {
		addComment();

		String targetId = getPara("targetId");
		String idtype = getPara("idtype");
		setCommentPage(targetId, idtype);
		setFollowList(targetId, idtype);
	}

	/**
	 * 添加子回复信息
	 */
	public void replyFollow() {
		addComment();

		String targetId = getPara("targetId");
		String idtype = getPara("idtype");
		setFollowList(targetId, idtype);

		String pId = getPara("pId");
		setComment(pId);
	}

	/**
	 * 设置分页回复信息
	 * 请求 comment/setPage
	 */
	public void setPage() {
		Object targetId = getAttr("targetId");
		Object idtype = getAttr("idtype");
		setCommentPage(targetId, idtype);
		setFollowList(targetId, idtype);

		String render = getAttr("render");
		render(render);
	}

	/**
	 * 分页设置父回复信息
	 */
	public void setCommentPage(Object targetId, Object idtype) {
		int pageNumber = getParaToInt("pageNumber", 1);
		int pageSize = getParaToInt("pageSize", 10);
		Page<Comment> commentPage = Comment.dao.findPageByTargetId(targetId,idtype, pageNumber, pageSize);
		setAttr("commentPage", commentPage);
	}

	/**
	 * 设置子回复信息
	 */
	public void setFollowList(Object targetId, Object idtype) {
		List<Comment> followPage = Comment.dao.findFollowByTargetId(targetId,idtype);
		setAttr("followPage", followPage);
	}

	/**
	 * 保存请求
	 */
	private void keepReplyPara() {
		keepPara("targetId", "idtype");
	}

	/**
	 * 保存回复信息
	 */
	private void addComment() {
		Comment comment = getModel(Comment.class);

		Object targetId = getPara("targetId");
		comment.set("targetid", targetId);

		String idtype = getPara("idtype");
		comment.set("idtype", idtype);

		Object userId = getUserId();
		comment.set("userid", userId);

		HttpServletRequest request = getRequest();
		String remoteIp = request.getRemoteAddr();
		comment.set("userip", remoteIp);

		// 默认回复楼层
		comment.set("level", "1");

		String pId = getPara("pId");
		if (StrKit.notBlank(pId)) {
			comment.set("pid", pId);
			comment.set("level", "2");
		}

		String toUserId = getPara("toUserId");
		if (StrKit.notBlank(toUserId)) {
			comment.set("touserid", toUserId);
		}

		Object message = getPara("message");
		comment.set("message", message);
		comment.save();
	}

	/**
	 * 设置回复信息
	 * 
	 * @param commentId
	 */
	private void setComment(Object commentId) {
		Comment comment = Comment.dao.findById(commentId);
		setAttr("comment", comment);
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
		return user.get("userid");
	}

	/**
	 * 获取登录User对象
	 * 
	 * @return user
	 */
	private User getUser() {
		User user = getSessionAttr("user");
		return user;
	}
}
