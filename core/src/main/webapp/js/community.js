function goToOrderCommunity(actionKey, communityId) {
	var url = "community/goToOrderCommunity?communityId=" + communityId
			+ "&actionKey=" + actionKey;
	window.location.href = url
}