function goToVentWall(){
	window.location.href="./ventwall?menuid=6";
}

function showMoreCommunity(node,communityList){
	var asideHtml = $(node).closest("#asideHtml");
	var showMore = asideHtml.find("#showMore").first();
	showMore.attr("hidden","block");
	
	$.post("./community/showMoreCommunity", function(data) {
		var showMoreHtml = asideHtml.find("#showMoreHtml").first();
		showMoreHtml.html(data);
	});

}

//跳转版块明细
function findById(node,targetId,communityId) {
	var url = "./community/goToDetailContent?communityId="+communityId;
	$.post(url,{
		targetId : targetId
	}, function(data) {
		var baseCommunity = $(node).closest("#baseCommunity");
		var detailContentHtml = baseCommunity.find("#detailContentHtml").first();
		detailContentHtml.html(data);
	});
}

//置顶功能
function setTop(node,userId,targetId){
	if("1" != userId){
		alert("非管理员，设置无效！");
	}else{
		var baseCommunity = $(node).closest("#baseCommunity");
		var communityId = baseCommunity.find("#communityId").first().val();
		
		var url = "./community/setTop?communityId="+communityId;
		$.post(url,{
			targetId : targetId
		}, function(data) {
			if(data == 1){
				alert('非管理员禁止操作！');
			}else if(data == 2){
				alert('设置失败！');
			}else{
				alert('设置成功！');
			}
		});
	}
}

//精华功能
function setEssence(node,userId,targetId){
	if("1" != userId){
		alert("非管理员，设置无效！");
	}else{
		var baseCommunity = $(node).closest("#baseCommunity");
		var communityId = baseCommunity.find("#communityId").first().val();
		
		var url = "./community/setEssence?communityId="+communityId;
		$.post(url,{
			targetId : targetId
		}, function(data) {
			if(data == true){
				baseCommunity.html(data);
			}
		});
	}
}

//跳转指定版块
function goToOrderForum(node,communityId) {
	var url = "./community/goToOrderForum?communityId=" + communityId;
	$.post(url, function(data) {
		var baseCommunity = $(node).closest("#communityLayout");
		var communityLayout = baseCommunity.find("#baseCommunity").first();
		communityLayout.html(data);
	});
}

//跳转到版块根目录
function goToHomeForum(actionKey){
	actionKey = '.' + actionKey + "/home";
	window.location.href=actionKey;
}

//点击发表主题按钮事件
function skipCommunity(node) {
	var communityLayout = $(node).closest("#communityLayout");
	var baseCommunity = communityLayout.find("#baseCommunity").first();
	var communityId = baseCommunity.find("#communityId").first().val();
	var url = "./community/skipCommunity";
	if(communityId != null && communityId != ''){
		url = "./community/goToCommunity";
	}
	$.post(url, {
		communityId :communityId
	}, function(data) {
		if(data == 1){
			alert('5分钟内只能发布一次同类型主题！');
		}else{
			baseCommunity.html(data);
		}
	});
}

function selectFirst(node){
	var firstVal = $(node).val();
	var selectCommuntiyHtml = $(node).closest("#selectCommuntiyHtml");
	var showCommunity = selectCommuntiyHtml.find("#showCommunity").first();
	showCommunity.html("");
	
	$.post("./community/selectSon", {
		pId :firstVal
	}, function(data) {
		var fatherNode = $(node).closest("#table");
		var sonNode = fatherNode.find("#son").first();
		sonNode.html(data);
	});
} 
function showCommunity(node){
	var firstVal = $(node).val();
	$.post("./community/goToCommunity", {
		communityId :firstVal
	}, function(data) {
		if(data == 1){
			alert('5分钟内只能发布一次同类型主题！');
		}else{
			var detailCommuntiyHtml = $(node).closest("#selectCommuntiyHtml");
			var showCommunity = detailCommuntiyHtml.find("#showCommunity").first();
			showCommunity.html(data);
		}
	});
}

//提交宝贝主题
function submitTreasure(node,communityId){
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
    	submitCreate(node,communityId)
    } else {
    	if(content == ""){
    		alert("内容不能为空！");
    	}else{
    		alert("提交失败，存在非规范内容，请检查！");
    	}
    }
}

//提交论坛主题
function submitPost(node,communityId){
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
    	submitCreate(node,communityId)
    } else {
    	if(content == ""){
    		alert("内容不能为空！");
    	}else{
    		alert("提交失败，存在非规范内容，请检查！");
    	}
    }
}

//提交活动主题
function submitActivity(node,communityId){
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
    	submitCreate(node,communityId)
    } else {
    	if(content == ""){
    		alert("内容不能为空！");
    	}else{
    		alert("提交失败，存在非规范内容，请检查！");
    	}
    }
}

//提交主题
function submitCreate(node,communityId) {
	var content = CKEDITOR.instances.ckeditor.getData();
	var createHtml = $(node).closest("#createHtml");
	var message = createHtml.find("#message").first();
	message.val(content);
	
	var url = "./community/create?communityId="+communityId;
	$.post(url, 
		$(node).closest("#createHtml").serialize(), 
		function(data) {
			if(data == false){
				alert("5分钟内只能发布一次同类型主题！");
			}else{
				var communityHtml = $(node).closest("#communityLayout");
				var baseCommunity = communityHtml.find("#baseCommunity").first();
				baseCommunity.html(data);
			}
	});
}