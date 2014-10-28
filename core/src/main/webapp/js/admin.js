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

//跳转更新宝贝页面
function skipUpdateTreasure(node,treasureId) {	
	$.post("./treasure/skipUpdate", {
		treasureId : treasureId
	}, function(data) {
		var adminTreasureHtml = $(node).closest("#adminTreasureHtml");
		adminTreasureHtml.html(data);
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

//更新活动主题
function submitActivity(node){
	canSubmit = true;
	$("form :input").trigger('blur');
    var numError = $('form .onError').length;
    if(numError){
    	canSubmit = false;
    }
    var content = CKEDITOR.instances.ckeditor.getData();
    if(content == ""){
    	canSubmit = false;
    }
    if(canSubmit){
    	canSubmit = submitForm(node)
    } else {
    	if(content == ""){
    		alert("内容不能为空！");
    	}else{
    		alert("提交失败，存在非规范内容，请检查！");
    	}
    }
    return canSubmit;
}

//提交论坛主题
function submitPost(node){
	canSubmit = true;
	$("form :input").trigger('blur');
    var numError = $('form .onError').length;
    if(numError){
    	canSubmit = false;
    }
    var content = CKEDITOR.instances.ckeditor.getData();
    if(content == ""){
    	canSubmit = false;
    }
    if(canSubmit){
    	canSubmit = submitForm(node)
    } else {
    	if(content == ""){
    		alert("内容不能为空！");
    	}else{
    		alert("提交失败，存在非规范内容，请检查！");
    	}
    }
    return canSubmit;
}

//提交宝贝主题
function submitTreasure(node){
	canSubmit = true;
	$("form :input").trigger('blur');
    var numError = $('form .onError').length;
    if(numError){
    	canSubmit = false;
    }
    var content = CKEDITOR.instances.ckeditor.getData();
    if(content == ""){
    	canSubmit = false;
    }
    if(canSubmit){
    	canSubmit = submitForm(node)
    } else {
    	if(content == ""){
    		alert("内容不能为空！");
    	}else{
    		alert("提交失败，存在非规范内容，请检查！");
    	}
    }
    return canSubmit;
}

//提交无奇不有
function submitOfWonders(node,wondersTypeId){
	canSubmit = true;
	$("form :input[required=required]").trigger('blur');
    var numError = $('form .onError').length;
    if(numError){
    	canSubmit = false;
    }
    var content = CKEDITOR.instances.ckeditor.getData();
    if(content == ""){
    	canSubmit = false;
    }
    if(canSubmit){
    	submitCreate(node,wondersTypeId)
    } else {
    	if(content == ""){
    		alert("内容不能为空！");
    	}else{
    		alert("提交失败，存在非规范内容，请检查！");
    	}
    }
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

//删除宝贝主题
function deleteTreasure(node,treasureId){
	$.post("./treasure/deleteById", {
		treasureId : treasureId
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