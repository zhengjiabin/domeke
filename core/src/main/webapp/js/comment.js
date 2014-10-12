//文本编辑器聚焦
function showCkeditor(node) {
	CKEDITOR.instances.ckeditor.focus();
}

//提交回复信息
function publishCkeditor(node,targetId,idtype,render) {
	var content = CKEDITOR.instances.ckeditor.getData();
	var replyComments = $(node).closest("#replyComments");
	var commentContents = replyComments.find("#commentHtml").first();
	
	$.post(
		"./comment/reply", 
		{
			targetId : targetId,
			idtype : idtype,
			render : render,
			message : content
		}, 
		function(data) {
			CKEDITOR.instances.ckeditor.setData("");
			commentContents.html(data);
		});
}

// 显示回复框信息
function showPublishHtml(node) {
	var publishHtml = $(node).next("#publishHtml");
	var editor = publishHtml.find("#editor").first();
	editor.attr("contenteditable", "true");

	var showEditor = publishHtml.find("#showEditor").first();
	showEditor.show();
}

// 打开表情窗口
function showFace(img) {
	var commentForm = $(img).closest("#commentForm");
	var face = commentForm.find("#face").first();
	face.show();

	face.click(function() {
		face.hide();
	});
	var editor = commentForm.find("#editor").first();
	editor.click(function() {
		face.hide();
	});
}

// 插入图片
function insertFace(img) {
	var commentForm = $(img).closest("#commentForm");
	var editor = commentForm.find("#editor").first();
	var image = "<img src='" + img.src + "' />";
	var val = editor.html() + image;
	editor.html(val);
}

//发表回复信息
function publish(node,targetId,idtype,pId,toUserId,render) {
	var showEditor = $(node).closest("#showEditor");
	var editor = showEditor.find("#editor").first();
	var message = showEditor.find("#message").first();
	var val = editor.html();
	message.html(val);
	
	var fartherNode = $(node).closest("#commentHtml");
	if(pId != null && pId !=""){
		fartherNode = $(node).closest("#commentContents");
		fartherNode = fartherNode.find("#commentHtml").first();
	}
	
	$.post("./comment/reply", {
		targetId : targetId,
		idtype : idtype,
		pId : pId,
		toUserId : toUserId,
		message : message.text(),
		render : render
	}, function(data) {
		var commentForm = $(node).closest("#commentForm");
		var showEditor = commentForm.find("#showEditor").first();
		showEditor.hide();
		var editor = showEditor.find("#editor").first();
		editor.html("");
		var message = showEditor.find("#message").first();
		message.html("");
		
		fartherNode.html(data);
	});
}