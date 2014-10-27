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
function setTop(node,targetId){
	var baseCommunity = $(node).closest("#baseCommunity");
	var communityId = baseCommunity.find("#communityId").first().val();
	
	var url = "./community/setTop?communityId="+communityId;
	$.post(url,{
		targetId : targetId
	}, function(data) {
		if(data == true){
			alert("设置置顶成功");
		}
	});
}

//精华功能
function setEssence(node,targetId){
	var baseCommunity = $(node).closest("#baseCommunity");
	var communityId = baseCommunity.find("#communityId").first().val();
	
	var url = "./community/setEssence?communityId="+communityId;
	$.post(url,{
		targetId : targetId
	}, function(data) {
		if(data == true){
			alert("设置精华成功");
		}
	});
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

//admin 跳转修改社区版块
function skipModify(node,communityId,pId){
	$.post("./community/skipModify", {
		communityId : communityId,
		pId : pId
	}, function(data) {
		var adminCommunityHtml = $(node).closest("#adminCommunityHtml");
		adminCommunityHtml.html(data);
	});
}

//admin 提交创建/修改版块
function submitForm(node) {
	var updateCommunityForm = $(node).closest("#updateCommunityForm");
	var pid = updateCommunityForm.find("#pid").first();
	pid.attr("disabled",false);
}

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

function deleteFat(node,communityId){
	var result = confirm("确定删除？");
	if(result){
		$.post("./community/deleteFat", {
			communityId : communityId
		}, function(data) {
			var fatherNode = $(node).closest("#detail_CommunityHtml");
			fatherNode.html(data);
		});
	}
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
		baseCommunity.html(data);
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
		var detailCommuntiyHtml = $(node).closest("#selectCommuntiyHtml");
		var showCommunity = detailCommuntiyHtml.find("#showCommunity").first();
		showCommunity.html(data);
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