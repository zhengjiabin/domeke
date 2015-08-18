//菜单加载时调用
$(document).ready(function(){
	//菜单导航 按钮事件 选中后高亮
    $(".NavL li a").each(function(){  
        $this = $(this);  
        if($this[0].href==String(window.location)){  
        $(".NavL li").removeClass("LiFuc");  
            $this.parent().addClass("LiFuc");  
        }  
    });  
});

function goToVentWall(){
	window.location.href="./ventwall?menuid=6";
}

//显示更多版块明细
function showMoreForum(node){
	var fatherNode = $(node).closest("#asideHtml");	
	$.post("community/showMoreForum", function(data) {
		fatherNode.html(data);
	});
}

//点击发表主题按钮事件
function skipPublishTheme(communityid) {
	url = "community/skipPublishTheme";
	if(communityid != null & communityid != ''){
		url = "community/skipPublishTheme?communityid=" + communityid;
	}
	window.location.href=url;
}

//点击发布主题中版块分类下拉框事件
function selectForumClassify(node){
	var forumClassify = $(node).val();
	$("#forum option").each(function(){
		var id = $(this).attr("id");
		if(id == -1){
			
		}else if(id == forumClassify){
			$(this).removeAttr("hidden");
		}else{
			$(this).removeAttr("selected");
			$(this).attr("hidden","block");
		}
	  });
	var forum = $("#forum").val();
	if(forum == -1){
		$("#forum").trigger('onchange');
	}
}

//点击发布主题中版块下拉框事件
function showTheme(node){
	var forum = $(node).val();
	var fatherNode = $(node).closest("#publishForum");
	var targetNode = fatherNode.find("#theme").first();
	if(forum == -1){
		targetNode.html("");
		return;
	}
	$.post("community/showTheme", {
		communityid :forum
	}, function(data) {
		if(data != null){
			targetNode.html(data);
		}
	});
}

//点击社区首页版块信息，跳转指定版块
function skipForum(node,communityid) {
	url = "community/skipForum";
	if(communityid != null & communityid != ''){
		url = "community/skipForum?communityid=" + communityid;
	}
	window.location.href=url;
}

//点击菜单栏的版块信息，跳转版块首页
function skipForumClassify(node,communityid) {
	url = "community/skipForumClassify";
	if(communityid != null & communityid != ''){
		url = "community/skipForumClassify?communityid=" + communityid;
	}
	window.location.href=url;
}

//提交主题
function submitTheme(node){
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
    	var fatherNode = $(node).closest("#createHtml");
    	fatherNode.submit();
    } else {
    	if(content == ""){
    		alert("内容不能为空！");
    	}else{
    		alert("提交失败，存在非规范内容，请检查！");
    	}
    }
}

//发布主题，处理信息
function onSubmitTheme(node,communityid) {
	var content = CKEDITOR.instances.ckeditor.getData();
	var fatherNode = $(node).closest("#createHtml");
	var message = fatherNode.find("#message").first();
	message.val(content);
	
	var targetNode = fatherNode.find("#submitCreate").first();
	targetNode.removeAttr('onclick');
	$(node).ajaxSubmit({
		type:"post",
		url:"community/submitTheme?communityid=" + communityid,
		success:function(data) {
			if(data == false){
				targetNode.attr("onclick","submitTheme(this)");
				alert("发布失败！");
			}else {
				alert("发布成功！");
				skipForum(node, communityid);
			}
		}
	});
	return false;
}

//点击取消发布主题按钮时间
function cancelSubmit(node){
	window.history.back();
}

//点击主题列表中的指定主题事件
function skipTheme(node,targetid,communityid) {
	var url = "community/skipTheme?communityid=" + communityid + "&targetid=" + targetid;
	window.location.href=url;
}

//置顶功能
function setTop(node,targetid,communityid){
	var url = "community/setTop";
	$.post(url,{
		targetid : targetid,
		communityid : communityid
	}, function(data) {
		if(data == 1){
			alert('非管理员禁止操作！');
		}else if(data == 2){
			alert('设置失败！');
		}else{
			alert('设置成功！');
			skipTheme(node,targetid,communityid);
		}
	});
}

//精华功能
function setEssence(node,targetid,communityid){
	var url = "community/setEssence";
	$.post(url,{
		targetid : targetid,
		communityid : communityid
	}, function(data) {
		if(data == 1){
			alert('非管理员禁止操作！');
		}else if(data == 2){
			alert('设置失败！');
		}else{
			alert('设置成功！');
			skipTheme(node,targetid,communityid);
		}
	});
}

//跳转到版块根目录
function goToHomeForum(actionKey){
	actionKey = '.' + actionKey + "/home";
	window.location.href=actionKey;
}