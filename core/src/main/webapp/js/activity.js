function openApply(activityId,communityId) {
	var url = "./activityApply/skipCreate?communityId="+communityId+"&activityId=" + activityId;
	var attribute = "height=500, width=500, top=0,left=0,toolbar=no, menubar=no, scrollbars=no, resizable=yes, location=no,status=no";
	window.showModalDialog(url, "", attribute);
}

function findById(activityId,communityId) {
	var url = "./activity/findById?communityId="+communityId+"&activityId=" + activityId;
	window.location.href = url;
}

function skipCreate(communityId) {
	var url = "./activity/skipCreate?communityId=" + communityId;
	window.location.href = url;
}



