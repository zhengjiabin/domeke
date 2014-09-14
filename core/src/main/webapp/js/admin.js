function openApply(activityId) {
	var url = "./activityApply/skipCreate?activityId=" + activityId;
	var attribute = "height=500, width=500, top=0,left=0,toolbar=no, menubar=no, scrollbars=no, resizable=yes, location=no,status=no";
	window.showModalDialog(url, "", attribute);
}