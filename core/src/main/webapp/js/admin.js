$(document).ready(function() {   
	$('li').hover(function(){
		ul = $(this).find('ul').first();
		ul.attr("style","display: block");
	},function(){
		ul = $(this).find('ul').first();
		ul.attr("style","display: none");
	});
});

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

//添加修改信息页面点击取消按钮
function cancelSubmit(node){
	window.history.back();
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

//添加修改信息页面点击提交按钮
function onSubmit(node,render){
	var fatherNode = $(node).closest("#submitForm");
	var targetNode = fatherNode.find("#submitBtn").first();
	targetNode.attr("type","button");
	$(node).ajaxSubmit({
		type:"post",
		success:function(data) {
			if(data == false){
				targetNode.attr("type","submit");
				alert("提交失败！");
			}else{
				goToManage(render);
			}
		}
	});
	return false;
}

//跳转社区版块或版块分类信息添加页面
function skipAddCommunity(node){
	window.location.href = "community/skipAddOfAdmin";
}

//跳转社区版块或版块分类信息修改页面
function skipModifyCommunity(node,communityid){
	window.location.href = "community/skipModifyOfAdmin?communityid=" + communityid;
}

//选择社区类型
function selectCommunity(node){
	var fatherNode = $(node).closest("#submitForm");
	var typeNode = fatherNode.find("#type").first();
	var rootNode = fatherNode.find("#root").first();
	var actionkey = fatherNode.find("#actionkey").first();
	var pidNode = fatherNode.find("#pid").first();
	
	var level = $(node).val();
	if(level == 1){
		pidNode.find("option:selected").removeAttr("selected");
		pidNode.append("<option id='' value='0' selected='selected'></option>");
		actionkey.val("");
		actionkey.attr("required","required");
		
		typeNode.attr("hidden","block");
		rootNode.removeAttr("hidden");
	}else{
		$("#pid option[value='0']").remove();
		$("#pid option:first").attr("selected","selected");
		var forumClassify = pidNode.find("option:selected").attr("id");
		actionkey.val(forumClassify);
		actionkey.removeAttr("required");
		
		typeNode.removeAttr("hidden");
		rootNode.attr("hidden","block");
	}
}

//选择版块分类下拉框
function selectForumClassify(node){
	var forumClassify = $(node).find("option:selected").attr("id");
	var fatherNode = $(node).closest("#submitForm");
	var actionkey = fatherNode.find("#actionkey").first();
	actionkey.val(forumClassify);
}

//提交社区版块
function submitCommunity(node,communityid) {
	var canSubmit = true;
	$("form :input[required=required]").trigger('blur');
    var numError = $('form .onError').length;
    if(numError){
    	canSubmit = false;
    }
    if(canSubmit){
    	var levelNode = $('input:radio:checked');
    	levelNode.removeAttr("disabled");
    	
    	var render = "/community/goToManager";
    	canSubmit = onSubmit(node,render);
    	
    	if(communityid != null && communityid != ''){
    		levelNode.attr("disabled","disabled");
    	}
    } else {
    	alert("提交失败，存在非规范内容，请检查！");
    }
    return canSubmit;
}

//删除社区分类或版块
function deleteCommunity(node,communityid){
	var result = confirm("确定删除？");
	if(result){
		$.post("./community/delete", {
			communityid : communityid
		}, function(data) {
			if(data == false){
				alert("删除失败！");
			}else{
				goToManage(render);
			}
		});
	}
}

//跳转更新论坛页面
function skipModifyPost(node,postid) {	
	window.location.href = "post/skipModifyOfAdmin?postid=" + postid;
}

//提交论坛主题
function submitPost(node){
    var canSubmit = submitBeforeCheck(node);
    if(canSubmit){
    	var render = "/post/goToManager";
    	canSubmit = onSubmit(node,render);
	} else {
		if(CKEDITOR.instances.ckeditor.getData() == ""){
		  	alert("内容不能为空！");
		}else{
		  	alert("提交失败，存在非规范内容，请检查！");
		}
	}
	return canSubmit;
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