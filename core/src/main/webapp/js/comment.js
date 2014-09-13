function showCkeditor(node) {
	var commentForm = $(node).closest("#commentForm");
	var commentDiv = commentForm.find("#commentDiv").first();
	commentDiv.show();
}

function publishCkeditor(node) {
	var content = CKEDITOR.instances.ckeditor.getData();

	var commentForm = $(node).closest("#commentForm");
	var ckeditorActionVal = commentForm.find("#ckeditorAction").first().val();
	var targetIdVal = commentForm.find("#targetId").first().val();
	var pageActionVal = commentForm.find("#pageAction").first().val();
	var fatherNodeVal = commentForm.find("#fatherNode").first().val();
	var commentFatherNodeVal = commentForm.find("#commentFatherNode").first()
			.val();
	var renderActionVal = commentForm.find("#renderAction").first().val();
	var ckeditorNodeVal = commentForm.find("#ckeditorNode").first().val();
	$.post(ckeditorActionVal, {
		targetId : targetIdVal,
		message : content,
		pageAction : pageActionVal,
		fatherNode : fatherNodeVal,
		commentFatherNode : commentFatherNodeVal,
		renderAction : renderActionVal
	}, function(data) {

		var commentDiv = commentForm.find("#commentDiv").first();
		commentDiv.hide();
		CKEDITOR.instances.ckeditor.setData("");

		var replyComments = $(node).closest(ckeditorNodeVal);
		var commentContents = replyComments.find(commentFatherNodeVal).first();
		commentContents.html(data);
	});
}

function showPublishFace(node) {
	var commentForm = $(node).closest("#commentForm");
	var editor = commentForm.find("#editor").first();
	editor.attr("contenteditable", "true");

	var showEditor = commentForm.find("#showEditor").first();
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

function publishFace(node) {
	var showEditor = $(node).closest("#showEditor");
	var editor = showEditor.find("#editor").first();
	var message = showEditor.find("#message").first();
	var val = editor.html();
	message.html(val);

	var commentForm = $(node).closest("#commentForm");
	var publishFaceActionVal = commentForm.find("#publishFaceAction").first()
			.val();
	var targetIdVal = commentForm.find("#targetId").first().val();
	var pIdVal = commentForm.find("#pId").first().val();
	var toUserIdVal = commentForm.find("#toUserId").first().val();
	var pageActionVal = commentForm.find("#pageAction").first().val();
	var fatherNodeVal = commentForm.find("#fatherNode").first().val();
	var commentFatherNodeVal = commentForm.find("#commentFatherNode").first()
			.val();
	var renderActionVal = commentForm.find("#renderAction").first().val();

	$.post(publishFaceActionVal, {
		targetId : targetIdVal,
		pId : pIdVal,
		toUserId : toUserIdVal,
		message : message.text(),
		pageAction : pageActionVal,
		fatherNode : fatherNodeVal,
		commentFatherNode : commentFatherNodeVal,
		renderAction : renderActionVal
	}, function(data) {
		var commentForm = $(node).closest("#commentForm");
		var showEditor = commentForm.find("#showEditor").first();
		showEditor.hide();
		var editor = showEditor.find("#editor").first();
		editor.html("");
		var message = showEditor.find("#message").first();
		message.html("");

		var commentContent = $(node).closest(commentFatherNodeVal);
		commentContent.html(data);
	});
}