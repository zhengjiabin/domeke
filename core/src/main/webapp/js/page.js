function goToPageSize(node, size, number,pageAction,fatherNode) {
	if(fatherNode == null || fatherNode == ''){
		if(pageAction.indexOf("?") > 0){
			pageAction = pageAction + "&pageNumber=" + number + "&pageSize=" + size;
		}else{
			pageAction = pageAction+ "?pageNumber=" + number + "&pageSize=" + size;
		}
		window.location.href=pageAction;
	}else{
		var fatherNode = $(node).closest(fatherNode);
		$.post(
				pageAction, 
				{
					pageSize : size,
					pageNumber : number,
					pageAction : pageAction
				},
				function(data) {
					fatherNode.html(data);
					
					var pageSize = fatherNode.find("#pageSize").first();
					var option = pageSize.find("option[value=" + size + "]");
					option.attr("selected", true);
				}
		);
	}
}

function goToPageSizeHref(node, size, number,pageAction,fatherNode) {
	window.location.href = pageAction+"&pageNumber="+number+"&pageSize="+size;
}