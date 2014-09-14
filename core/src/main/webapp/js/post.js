function skipCreate(communityId) {
	var url = "./post/skipCreate?communityId=" + communityId;
	window.location.href = url;
}

function findById(postId,communityId) {
	var url = "./post/findById?communityId="+communityId+"&postId=" + postId;
	window.location.href = url;
}