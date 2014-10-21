function checknull(obj) {
	var fn=document.getElementById("typename").value
	var count = fn.length;
	if(count == 0) {
		alert("[类型名称]必填!");
		document.getElementById("typename").focus();
		return false;
	} else if (document.getElementById("actionkey").value.length == 0) {
		alert("[actionkey]必填!");
		document.getElementById("actionkey").focus();
		return false;
	} else if (document.getElementById("level").value.length == 0) {
		alert("[类型级别]必填!");
		document.getElementById("level").focus();
		return false;
	} else if (document.getElementById("sortnum").value.length == 0) {
		alert("[排序]必填!");
		document.getElementById("sortnum").focus();
		return false;
	} else if (document.getElementById("goodstype").value.length == 0) {
		alert("[排序]必填!");
		document.getElementById("goodstype").focus();
		return false;
	}
	form.submit();
}

function upchecknull(obj) {
	var fn=document.getElementById("typename").value
	var count = fn.length;
	if(count == 0) {
		alert("[类型名称]必填!");
		document.getElementById("typename").focus();
		return false;
	} else if (document.getElementById("actionkey").value.length == 0) {
		alert("[actionkey]必填!");
		document.getElementById("actionkey").focus();
		return false;
	} else if (document.getElementById("level").value.length == 0) {
		alert("[类型级别]必填!");
		document.getElementById("level").focus();
		return false;
	} else if (document.getElementById("sortnum").value.length == 0) {
		alert("[排序]必填!");
		document.getElementById("sortnum").focus();
		return false;
	} else if (document.getElementById("goodstype").value.length == 0) {
		alert("[排序]必填!");
		document.getElementById("goodstype").focus();
		return false;
	}
	form.submit();
}