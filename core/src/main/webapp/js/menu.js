
//goods判空
function checknull(obj) {
	if(document.getElementById("menuname").value.length == 0) {
		alert("[菜单名称]必填!");
		document.getElementById("menuname").focus();
		return false;
	} else if (document.getElementById("actionkey").value.length == 0) {
		alert("[actionkey]必填!");
		document.getElementById("actionkey").focus();
		return false;
	} else if (document.getElementById("top").value.length == 0) {
		alert("[菜单级别]必填!");
		document.getElementById("top").focus();
		return false;
	} else if (document.getElementById("sortnum").value.length == 0) {
		alert("[菜单排序]必填!");
		document.getElementById("sortnum").focus();
		return false;
	} else if (document.getElementById("menutype").value.length == 0) {
		alert("[菜单模块]必填!");
		document.getElementById("menutype").focus();
		return false;
	}  
	form.submit();
}
