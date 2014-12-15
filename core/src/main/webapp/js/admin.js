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
		var fatherNode = $(node).closest("#page");
		fatherNode.html(data);
	});
}

//跳转更新活动页面
function skipUpdateActivity(node,activityId) {	
	$.post("./activity/skipUpdate", {
		activityId : activityId
	}, function(data) {
		var fatherNode = $(node).closest("#page");
		fatherNode.html(data);
	});
}

//跳转更新宝贝页面
function skipUpdateTreasure(node,treasureId) {	
	$.post("./treasure/skipUpdate", {
		treasureId : treasureId
	}, function(data) {
		var fatherNode = $(node).closest("#page");
		fatherNode.html(data);
	});
}

//跳转新建/更新无奇不有主题页面
function skipUpdateOfWonders(node,ofWondersId){
	$.post("./ofWonders/skipUpdate", {
		ofWondersId : ofWondersId
	}, function(data) {
		var fatherNode = $(node).closest("#page");
		fatherNode.html(data);
	});
}

//跳转无奇不有版块新建/修改页面
function skipUpdateWondersType(node,wondersTypeId,pId){
	$.post("./wondersType/skipModify", {
		wondersTypeId : wondersTypeId,
		pId : pId
	}, function(data) {
		var fatherNode = $(node).closest("#page");
		fatherNode.html(data);
	});
}

//跳转社区版块新建/修改页面
function skipUpdateCommunity(node,communityId,pId){
	$.post("./community/skipModify", {
		communityId : communityId,
		pId : pId
	}, function(data) {
		var fatherNode = $(node).closest("#page");
		fatherNode.html(data);
	});
}

//提交活动主题
function submitActivity(node){
	var canSubmit = submitBeforeCheck(node);
    if(canSubmit){
    	submitForm(node);
    	canSubmit = onSubmit(node);
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
    	canSubmit = onSubmit(node);
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
    	canSubmit = onSubmit(node);
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

//提交无奇不有版块
function submitWondersType(node){
	var canSubmit = true;
	$("form :input[required=required]").trigger('blur');
    var numError = $('form .onError').length;
    if(numError){
    	canSubmit = false;
    }
    if(canSubmit){
    	var fatherNode = $(node).closest("#submitForm");
    	var targetNode = fatherNode.find("#pid").first();
    	targetNode.attr("disabled",false);
    	canSubmit = onSubmit(node);
    } else {
    	alert("提交失败，存在非规范内容，请检查！");
    }
    return canSubmit;
}

//提交社区版块
function submitCommunity(node) {
	var canSubmit = true;
	$("form :input[required=required]").trigger('blur');
    var numError = $('form .onError').length;
    if(numError){
    	canSubmit = false;
    }
    if(canSubmit){
    	var fatherNode = $(node).closest("#submitForm");
    	var targetNode = fatherNode.find("#pid").first();
    	targetNode.attr("disabled",false);
    	canSubmit = onSubmit(node);
    } else {
    	alert("提交失败，存在非规范内容，请检查！");
    }
    return canSubmit;
}

//异步提交
function onSubmit(node){
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
	var fatherNode = $(node).closest("#page");
	$(node).ajaxSubmit({
		type:"post",
		url:"./ofWonders/update",
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

//删除论坛主题
function deletePost(node,postId){
	var result = confirm("确定删除？");
	if(result){
		$.post("./post/deleteById", {
			postId : postId
		}, function(data) {
			var page = $(node).closest("#page");
			page.html(data);
		});
	}
}

//删除活动主题
function deleteActivity(node,activityId){
	var result = confirm("确定删除？");
	if(result){
		$.post("./activity/deleteById", {
			activityId : activityId
		}, function(data) {
			var page = $(node).closest("#page");
			page.html(data);
		});
	}
}

//删除宝贝主题
function deleteTreasure(node,treasureId){
	var result = confirm("确定删除？");
	if(result){
		$.post("./treasure/deleteById", {
			treasureId : treasureId
		}, function(data) {
			var page = $(node).closest("#page");
			page.html(data);
		});
	}
}

//删除无奇不有主题
function deleteOfWonders(node,ofWondersId){
	var result = confirm("确定删除？");
	if(result){
		$.post("./ofWonders/deleteById", {
			ofWondersId : ofWondersId
		}, function(data) {
			var page = $(node).closest("#page");
			page.html(data);
		});
	}
}

//删除无奇不有版块明细
function deleteSon(node,wondersTypeId,pId){
	var result = confirm("确定删除？");
	if(result){
		$.post("./wondersType/deleteSon", {
			wondersTypeId : wondersTypeId,
			pId : pId
		}, function(data) {
			var fatherNode = $(node).closest("#wondersTypeForum");
			fatherNode.html(data);
		});
	}
}

//删除无奇不有版块
function deleteFat(node,wondersTypeId){
	var result = confirm("确定删除？");
	if(result){
		$.post("./wondersType/deleteFat", {
			wondersTypeId : wondersTypeId
		}, function(data) {
			var fatherNode = $(node).closest("#page");
			fatherNode.html(data);
		});
	}
}

//删除社区版块明细
function deleteSon(node,communityId,pId){
	var result = confirm("确定删除？");
	if(result){
		$.post("./community/deleteSon", {
			communityId : communityId,
			pId : pId
		}, function(data) {
			var fatherNode = $(node).closest("#detailCommunity");
			fatherNode.html(data);
		});
	}
}

//删除社区版块
function deleteFat(node,communityId){
	var result = confirm("确定删除？");
	if(result){
		$.post("./community/deleteFat", {
			communityId : communityId
		}, function(data) {
			var fatherNode = $(node).closest("#page");
			fatherNode.html(data);
		});
	}
}

//跳转指定管理
function goToManage(render) {
	var userAgent = navigator.userAgent.toLowerCase();
	if(/msie/.test(userAgent)) {
	    // 此浏览器为 IE
		window.location = ".."+render;
		window.event.returnValue = false;
	} else {
		window.location.href = "./admin/goToManager?render=" + render;
	}
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