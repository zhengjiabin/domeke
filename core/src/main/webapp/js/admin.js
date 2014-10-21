$(document).ready(function() {   
	$('li').hover(function(){
		ul = $(this).find('ul').first();
		ul.attr("style","display: block");
	},function(){
		ul = $(this).find('ul').first();
		ul.attr("style","display: none");
	});
});

//跳转更新论坛页面
function skipUpdatePost(node,postId) {	
	$.post("./post/skipUpdate", {
		postId : postId
	}, function(data) {
		var adminPostHtml = $(node).closest("#adminPostHtml");
		adminPostHtml.html(data);
	});
}

//跳转更新活动页面
function skipUpdateActivity(node,activityId) {	
	$.post("./activity/skipUpdate", {
		activityId : activityId
	}, function(data) {
		var adminActivityHtml = $(node).closest("#adminActivityHtml");
		adminActivityHtml.html(data);
	});
}

//跳转新建/更新无奇不有主题页面
function skipUpdateOfWonders(node,ofWondersId){
	$.post("./ofWonders/skipUpdate", {
		ofWondersId : ofWondersId
	}, function(data) {
		var adminOfWondersHtml = $(node).closest("#adminOfWondersHtml");
		adminOfWondersHtml.html(data);
	});
}

//更新主题
function submitForm(node) {
	var content = CKEDITOR.instances.ckeditor.getData();
	var submitForm = $(node).closest("#submitForm");
	var message = submitForm.find("#message").first();
	message.val(content);
}

//删除论坛主题
function deletePost(node,postId){
	$.post("./post/deleteById", {
		postId : postId
	}, function(data) {
		var page = $(node).closest("#page");
		page.html(data);
	});
}

//删除活动主题
function deleteActivity(node,activityId){
	$.post("./activity/deleteById", {
		activityId : activityId
	}, function(data) {
		var page = $(node).closest("#page");
		page.html(data);
	});
}

//删除无奇不有主题
function deleteOfWonders(node,ofWondersId){
	$.post("./ofWonders/deleteById", {
		ofWondersId : ofWondersId
	}, function(data) {
		var page = $(node).closest("#page");
		page.html(data);
	});
}

//跳转指定管理
function goToManage(render) {
	window.location.href = "./admin/goToManager?render=" + render;
}

//显示二级菜单
function showForum(node){
	var secForum = $(node).find("#secForum").first();
	secForum.attr("style","display: block");
}