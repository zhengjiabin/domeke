function openApply(activityId) {
	var url = "./activityApply/skipCreate?activityId=" + activityId;
	var attribute = "height=500, width=500, top=0,left=0,toolbar=no, menubar=no, scrollbars=no, resizable=yes, location=no,status=no";
	window.open(url, '', attribute);
}

function findById(node,activityId) {
	var url = "./activity/findById";
	$.post(url,{
		activityId : activityId
	}, function(data) {
		var baseCommunity = $(node).closest("#baseCommunity");
		var activityHtml = baseCommunity.find("#activityHtml").first();
		activityHtml.html(data);
	});
}

function submitApp(node,activityId) {
	var url = "./activityApply/create?activityId=" + activityId;
	$.post(url, 
		$(node).closest("#createActivityApplyHtml").serialize(), 
		function(data) {
			alert('提交成功!');
			var applyNumber = "applyNumber-"+activityId;
			var applyNumberNode = window.opener.document.getElementById(applyNumber);
			applyNumberNode.innerText="已参加人数："+data;
			window.close();
	});
}






