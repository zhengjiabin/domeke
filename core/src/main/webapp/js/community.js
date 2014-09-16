function goToOrderCommunity(actionKey, communityId) {
	var url = "community/goToOrderCommunity?communityId=" + communityId
			+ "&actionKey=" + actionKey;
	window.location.href = url
}

function addCommunityFat(actionKey, communityId) {
	var detailCommunityHtml = $(node).closest("#detailCommunityHtml");
	var activonVal = "/community/addCommunityFat";
	
	$.post(pageActionVal, {
		communityId : communityId
	}, function(data) {
		var fatherNode = $(node).closest(fatherNodeVal);
		fatherNode.html(data);
		
		var pageSize = fatherNode.find("#pageSize").first();
		var option = pageSize.find("option[value=" + size + "]");
		option.attr("selected", true);
	});
}

function saveSon(node,communityId,pId){
	var pId = communityId
	
	$(node).attr("contenteditable",false);
	var tr = $(node).closest("#son");
	var title = tr.find("#title").first();
	var content = tr.find("#content").first();
	var actionkey = tr.find("#actionkey").first();
	var level = tr.find("#level").first();
	var position = tr.find("#position").first();
	
	$.post("/community/saveSon", {
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
function addCommunitySon(node,communityId) {
	var detailCommunity = $(node).closest("#detailCommunity");
	var table = detailCommunity.find("#table").first();
	
	var newRow = "<tr class='odd gradeX' id='son' contenteditable='true'>";
	newRow = newRow + "<td></td>";
	newRow = newRow + "<td></td>";
	newRow = newRow + "<td></td>";
	newRow = newRow + "<td></td>";
	newRow = newRow + "<td></td>";
	newRow = newRow + "<td>";
	newRow = newRow + "<button class='btn btn-mini' onclick='saveSon(this,'',"+communityId+")'>保存</button> ";
	newRow = newRow + "<button class='btn btn-warning btn-mini'>修改</button> ";
	newRow = newRow + "<button class='btn btn-danger btn-mini'>删除</button> ";
	newRow = newRow + "</td>";
	newRow = newRow + "</tr>";
	table.append(newRow);
}