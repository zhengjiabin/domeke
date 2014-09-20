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

function goToOrderCommunity(node,actionKey, communityId) {
	var url = "./community/goToOrderCommunity?communityId=" + communityId
			+ "&actionKey=" + actionKey;
	$.post(url, {
		communityId : communityId,
		actionKey : actionKey
	}, function(data) {
		var baseCommunity = $(node).closest("#baseCommunity");
		var communityLayout = baseCommunity.find("#communityLayout").first();
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