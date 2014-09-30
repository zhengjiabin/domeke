//跳转更新论坛页面
function skipUpdatePost(node,postId) {	
	$.post("./post/skipUpdate", {
		postId : postId
	}, function(data) {
		var adminPostHtml = $(node).closest("#adminPostHtml");
		adminPostHtml.html(data);
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

//跳转指定管理
function goToManage(render) {
	window.location.href = "./admin/goToManager?render=" + render;
}