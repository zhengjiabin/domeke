function goToOrderCommunity(actionKey, communityId) {
	var url = "community/goToOrderCommunity?communityId=" + communityId
			+ "&actionKey=" + actionKey;
	window.location.href = url
}

function addCommunityFat(node) {
	var fatherNode = $(node).closest("#admin_Community");
	var targetNode = fatherNode.find("#detail_CommunityHtml").first();
	$.post("./community/addCommunityFat", function(data) {
		targetNode.html(data);
		
		var tableNode = targetNode.find("#table").first();
		var trNode = tableNode.find("#fat").first();
		trNode.attr("contenteditable",true);
		trNode.focus();
	});
}

function saveSon(node,communityId,pId){
	
	$(node).attr("contenteditable",false);
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

function modifyFat(node){
	var tr = $(node).closest("#fat");
	tr.attr("contenteditable",true);
}

function modifySon(node){
	var tr = $(node).closest("#son");
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