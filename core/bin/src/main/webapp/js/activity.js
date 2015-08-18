function openApply(activityId) {
	$.post("./activity/checkCanApply", {
		activityId : activityId
	},function(data) {
		if(data != null && data != ''){
			alert(data);
		}else {
			var url = "./activityApply/skipCreate?activityId=" + activityId;
			var attribute = "height=500, width=500, top=0,left=0,toolbar=no, menubar=no, scrollbars=no, resizable=yes, location=no,status=no";
			window.open(url, '', attribute);
		}
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






