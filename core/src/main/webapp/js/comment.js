//文本编辑器聚焦
function showCkeditor(node) {
	CKEDITOR.instances.ckeditor.focus();
}

//点击编辑器提交按钮，发表信息-异步刷新
function publishCkeditor(node,targetid,idtype,render,pageSize) {
	var content = CKEDITOR.instances.ckeditor.getData();
	var fatherNode = $(node).closest("#replyComments");
	var targetNode = fatherNode.find("#commentHtml").first();
	
	$.post(
		"./comment/publish", 
		{
			targetid : targetid,
			idtype : idtype,
			render : render,
			pageSize : pageSize,
			message : content
		}, 
		function(data) {
			if(data == false){
				alert("发表失败！");
			}else{
				targetNode.html(data);
			}
		}
	);
}

//跳转显示回复框信息
function skipPublish(node){
	var fatherNode = $(node).closest("#commentHtml");
	var publishNode = fatherNode.find("#skipPublish").first();
	
	var editor = publishNode.find("#editor").first();
	editor.attr("contenteditable", "true");
	var showEditor = publishNode.find("#showEditor").first();
	showEditor.show();
	
	var X = editor.offset().top; 
	var Y = editor.offset().left;
	scroll(Y,X-200);
}

// 显示回复框信息
function showPublishHtml(node) {
	var fatherNode = $(node).closest("#commentContent");
	var publishNode = fatherNode.find("#publishHtml").first();
	var editor = publishNode.find("#editor").first();
	editor.attr("contenteditable", "true");

	var showEditor = publishNode.find("#showEditor").first();
	showEditor.show();
}

// 打开表情窗口
function showFace(img) {
	var fatherNode = $(img).closest("#showEditor");
	var face = fatherNode.find("#face").first();
	face.show();

	face.click(function() {
		face.hide();
	});
	var editor = fatherNode.find("#editor").first();
	editor.click(function() {
		face.hide();
	});
}

//插入图片
function insertFace(img) {
	var fatherNode = $(img).closest("#showEditor");
	var editor = fatherNode.find("#editor").first();
	var image = "<img src='" + img.src + "' />";
	var val = editor.html() + image;
	editor.html(val);
}

// 打开表情窗口
function showCkeditorFace(img) {
	var fatherNode = $(img).closest("#ckeditorHtml");
	var face = fatherNode.find("#face").first();
	face.show();
	
	face.click(function() {
		face.hide();
	});
}

//插入图片
function insertCkeditorFace(img) {
	var image = "<img src='" + img.src + "' />";
	var data = CKEDITOR.instances.ckeditor.getData();
	CKEDITOR.instances.ckeditor.setData(data + image);
}

//发表回复信息
function publish(node,targetid,idtype,pid,touserid,render) {
	var showEditor = $(node).closest("#showEditor");
	var editor = showEditor.find("#editor").first();
	var message = showEditor.find("#message").first();
	var val = editor.html();
	message.html(val);
	
	var fartherNode = $(node).closest("#commentHtml");
	if(pid != null && pid !=""){
		fartherNode = $(node).closest("#commentContents");
		fartherNode = fartherNode.find("#commentHtml").first();
	}
	
	$.post("./comment/reply", {
		targetid : targetid,
		idtype : idtype,
		pid : pid,
		touserid : touserid,
		message : message.text(),
		render : render
	}, function(data) {
		if(data == false){
			alert('发表失败！');
		}else{
			var showEditor = $(node).closest("#showEditor");
			showEditor.hide();
			var editor = showEditor.find("#editor").first();
			editor.html("");
			var message = showEditor.find("#message").first();
			message.html("");
			
			fartherNode.html(data);
		}
	});
}

//添加支持
function addSupport(node,commentid,recordtype){
	$.post("./comment/addRecord", {
		commentid : commentid,
		recordtype : recordtype
	}, function(data) {
		if(data == 1){
			alert("已支持，禁止重复操作！");
		}else if(data.number  != null){
			var number = data.number;
			$(node).attr("style","color:red");
			$(node).attr("onclick","alert('已支持，禁止重复操作！')");
			$(node).text("支持 (+" + number + ")");
		}else{
			$(node).html(data);
		}
	});
}

//添加反对
function addOppose(node,commentid,recordtype){
	$.post("./comment/addRecord", {
		commentid : commentid,
		recordtype : recordtype
	}, function(data) {
		if(data == 1){
			alert("已反对，禁止重复操作！");
		}else if(data.number  != null){
			var number = data.number;
			$(node).attr("style","color:red");
			$(node).attr("onclick","alert('已反对，禁止重复操作！')");
			$(node).html("反对 (+" + number + ")");
		}else{
			$(node).html(data);
		}
	});
}

//添加有用
function addUseful(node,commentid,recordtype){
	$.post("./comment/addRecord", {
		commentid : commentid,
		recordtype : recordtype
	}, function(data) {
		if(data == 1){
			alert("已点击，禁止重复操作！");
		}else if(data.number != null){
			var number = data.number;
			$(node).attr("style","color:red");
			$(node).attr("onclick","alert('已点击，禁止重复操作！')");
			$(node).html("有用(+" + number + ")");
		}else{
			$(node).html(data);
		}
	});
}
