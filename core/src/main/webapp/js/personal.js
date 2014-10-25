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

//更新主题
function submitForm(node) {
	var content = CKEDITOR.instances.ckeditor.getData();
	var submitForm = $(node).closest("#submitForm");
	var message = submitForm.find("#message").first();
	message.val(content);
	
	var fatherNode = $(node).closest("#personal");
	$(node).ajaxSubmit({
		type:"post",
		success:function(data) {
			fatherNode.html(data);
		}
	});
	return false;
}

//显示上传图片
function onUploadImgChange(node){
	var fatherNode = $(node).closest("#submitForm");
	var preview = fatherNode.find("#preview").first();
	if(node.value == null || node.value ==''){
		preview.html("");
		return false;
	}else if( !node.value.match( /.jpg|.gif|.png|.bmp/i ) ){
		preview.html("");
        return false;
    }
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