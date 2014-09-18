function skipCreate(node,communityId) {
	var url = "./post/skipCreate?communityId=" + communityId;
	$.post(url, function(data) {
		var baseCommunity = $(node).closest("#baseCommunity");
		var postHtml = baseCommunity.find("#postHtml").first();
		postHtml.html(data);
	});
}

function submitPost(node) {
	var url = "./post/create";
	$.post(url, 
		$(node).closest("#createPostHtml").serialize(), 
		function(data) {
		var baseCommunity = $(node).closest("#baseCommunity");
		baseCommunity.html(data);
	});
}

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

