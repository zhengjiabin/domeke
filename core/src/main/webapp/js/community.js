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
function findById(node,targetId) {
	var baseCommunity = $(node).closest("#baseCommunity");
	var communityId = baseCommunity.find("#communityId").first().val();
	
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

function goToOrderForum(node,communityId) {
	var url = "./community/goToOrderForum?communityId=" + communityId;
	$.post(url, function(data) {
		var baseCommunity = $(node).closest("#communityLayout");
		var communityLayout = baseCommunity.find("#baseCommunity").first();
		communityLayout.html(data);
	});
}

function addCommunityFat(node) {
	var fatherNode = $(node).closest("#admin_Community");
	var targetNode = fatherNode.find("#detail_CommunityHtml").first();
	$.post("./community/addCommunityFat", function(data) {
		targetNode.html(data);
		
		var tableNode = targetNode.find("#table").first();
		var trNode = tableNode.find("#communityTr").first();
		trNode.attr("contenteditable",true);
		trNode.focus();
	});
}

function saveSon(node,communityId,pId){
	var tr = $(node).closest("#son");
	var title = tr.find("#title").first().html();
	var content = tr.find("#content").first().html();
	var actionkey = tr.find("#actionkey").first().html();
	var level = tr.find("#level").first().html();
	var position = tr.find("#position").first().html();
	
	$.post("./community/saveSon", {
		title : title,
		content : content,
		actionkey : actionkey,
		level : level,
		position : position,
		communityId :communityId,
		pId : pId
	}, function(data) {
		var fatherNode = $(node).closest("#detailCommunity");
		fatherNode.html(data);
	});
}

function saveFat(node,communityId){
	var tr = $(node).closest("#fat");
	var title = tr.find("#title").first().html();
	var content = tr.find("#content").first().html();
	var actionkey = tr.find("#actionkey").first().html();
	var level = tr.find("#level").first().html();
	var position = tr.find("#position").first().html();
	
	$.post("./community/saveFat", {
		title : title,
		content : content,
		actionkey : actionkey,
		level : level,
		position : position,
		communityId :communityId
	}, function(data) {
		var fatherNode = $(node).closest("#detailCommunity");
		fatherNode.html(data);
	});
}

//添加版块
function addCommunitySon(node,pId) {
	$.post("./community/addCommunitySon", {
		pId : pId
	}, function(data) {
		var fatherNode = $(node).closest("#detailCommunity");
		fatherNode.html(data);
		
		var tableNode = fatherNode.find("#table").first();
		var trNode = tableNode.find("#son").first();
		trNode.attr("contenteditable",true);
		trNode.focus();
	});
}

function modify(node,fatherNode){
	var tr = $(node).closest(fatherNode);
	tr.attr("contenteditable",true);
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
		if(data == false){
			alert("5分钟内只能发布一次同类型主题！");
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
		if(data == false){
			alert("5分钟内只能发布一次同类型主题！");
		}else{
			var detailCommuntiyHtml = $(node).closest("#selectCommuntiyHtml");
			var showCommunity = detailCommuntiyHtml.find("#showCommunity").first();
			showCommunity.html(data);
		}
	});
}

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