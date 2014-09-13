function goToPageSize(node, size, number) {
	var page = $(node).closest("#pageHtml");
	var pageActionVal = page.find("#pageAction").first().val();
	var fatherNodeVal = page.find("#fatherNode").first().val();
	$.post(pageActionVal, {
		pageSize : size,
		pageNumber : number,
		pageAction : pageActionVal,
		fatherNode : fatherNodeVal
	}, function(data) {
		var fatherNode = $(node).closest(fatherNodeVal);
		fatherNode.html(data);
		
		var pageSize = fatherNode.find("#pageSize").first();
		var option = pageSize.find("option[value=" + size + "]");
		option.attr("selected", true);
	});
}