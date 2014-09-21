function findById(node,postId,communityId) {
	var url = "./post/findById?communityId="+communityId;
	$.post(url,{
		postId : postId
	}, function(data) {
		var baseCommunity = $(node).closest("#baseCommunity");
		var postHtml = baseCommunity.find("#postHtml").first();
		postHtml.html(data);
	});
}

function savePost(postId,communityId) {
	var url = "./post/findById?communityId="+communityId+"&postId=" + postId;
	window.location.href = url;
}

