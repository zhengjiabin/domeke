//跳转更新论坛页面
function skipUpdatePost(node,postId) {	
	$.post("./post/skipUpdateForPersonal", {
		postId : postId
	}, function(data) {
		var fatherNode = $(node).closest("#page");
		fatherNode.html(data);
	});
}

//跳转更新活动页面
function skipUpdateActivity(node,activityId) {	
	$.post("./activity/skipUpdateForPersonal", {
		activityId : activityId
	}, function(data) {
		var fatherNode = $(node).closest("#page");
		fatherNode.html(data);
	});
}

//跳转更新宝贝页面
function skipUpdateTreasure(node,treasureId) {	
	$.post("./treasure/skipUpdateForPersonal", {
		treasureId : treasureId
	}, function(data) {
		var fatherNode = $(node).closest("#page");
		fatherNode.html(data);
	});
}

//跳转新建/更新无奇不有主题页面
function skipUpdateOfWonders(node,ofWondersId){
	$.post("./ofWonders/skipUpdateForPersonal", {
		ofWondersId : ofWondersId
	}, function(data) {
		var fatherNode = $(node).closest("#page");
		fatherNode.html(data);
	});
}

//更新活动主题
function submitActivity(node){
	var canSubmit = submitBeforeCheck(node);
    if(canSubmit){
    	submitForm(node);
    	canSubmit = onSubmitCommunity(node);
    } else {
    	if(CKEDITOR.instances.ckeditor.getData() == ""){
    		alert("内容不能为空！");
    	}else{
    		alert("提交失败，存在非规范内容，请检查！");
    	}
    }
    return canSubmit;
}

//提交论坛主题
function submitPost(node){
	var canSubmit = submitBeforeCheck(node);
    if(canSubmit){
    	submitForm(node);
    	canSubmit = onSubmitCommunity(node);
    } else {
    	if(CKEDITOR.instances.ckeditor.getData() == ""){
    		alert("内容不能为空！");
    	}else{
    		alert("提交失败，存在非规范内容，请检查！");
    	}
    }
    return canSubmit;
}

//提交宝贝主题
function submitTreasure(node){
	var canSubmit = submitBeforeCheck(node);
    if(canSubmit){
    	submitForm(node);
    	canSubmit = onSubmitCommunity(node);
    } else {
    	if(CKEDITOR.instances.ckeditor.getData() == ""){
    		alert("内容不能为空！");
    	}else{
    		alert("提交失败，存在非规范内容，请检查！");
    	}
    }
    return canSubmit;
}

//提交无奇不有
function submitOfWonders(node){
	var canSubmit = submitBeforeCheck(node);
    if(canSubmit){
    	submitForm(node);
    	canSubmit = onSubmitOfWonders(node);
    } else {
    	if(CKEDITOR.instances.ckeditor.getData() == ""){
    		alert("内容不能为空！");
    	}else{
    		alert("提交失败，存在非规范内容，请检查！");
    	}
    }
    return canSubmit;
}

//提交前检查
function submitBeforeCheck(node){
	$("form :input[required=required]").trigger('blur');
    var numError = $('form .onError').length;
    if(numError){
    	return false;
    }
    var content = CKEDITOR.instances.ckeditor.getData();
    if(content == ""){
    	return false;
    }
    return true;
}

//更新主题
function submitForm(node) {
	var content = CKEDITOR.instances.ckeditor.getData();
	var submitForm = $(node).closest("#submitForm");
	var message = submitForm.find("#message").first();
	message.val(content);
}

//社区相关异步提交
function onSubmitCommunity(node){
	var fatherNode = $(node).closest("#page");
	$(node).ajaxSubmit({
		type:"post",
		success:function(data) {
			fatherNode.html(data);
		}
	});
	return false;
}

//无奇不有异步提交
function onSubmitOfWonders(node){
	var fatherNode = $(node).closest("#personal");
	$(node).ajaxSubmit({
		type:"post",
		url:"./ofWonders/updateForPersonal",
		success:function(data) {
			if(data == 1){
				alert("上传的文件有误，请重新上传");
			}else if(data == 2){
				alert("上传文件失败，请联系管理员");
			}else{
				fatherNode.html(data);
			}
		}
	});
	return false;
}

//显示上传图片
function onUploadImgChange(node){
	var fatherNode = $(node).closest("#submitForm");
	var preview = fatherNode.find("#preview").first();
	var path = fatherNode.find("#themeimgPath").first();
	if(node.value == null || node.value ==''){
		preview.html("");
		path.text("");
		return false;
	}else if( !node.value.match( /.jpg|.gif|.png|.bmp/i ) ){
		preview.html("");
		path.text("");
        return false;
    }
	var filePath = node.value.replace(/\\/g,"/");
	filePath = filePath.substring(filePath.lastIndexOf("/")+1,filePath.length);
	path.text(filePath);
    if (node.files && node.files[0]){  
    	var reader = new FileReader();  
    	reader.onload = function(evt){
    		preview.html('<img src="' + evt.target.result + '" />');
    	}
    	reader.readAsDataURL(node.files[0]);
    }else{
    	preview.html('<div class="img" style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src=\'' + node.value + '\'"></div>');
    }  
}