
//goods判空
function checknull(obj) {
	var fn=document.getElementById("goods").value
	var count = fn.length;
	if(count == 0) {
		alert("[商品类型]必填!");
		document.getElementById("goods").focus();
		return false;
	} else if (document.getElementById("goodsname").value.length == 0) {
		alert("[商品名称]必填!");
		document.getElementById("goodsname").focus();
		return false;
	} else if (document.getElementById("price").value.length == 0) {
		alert("[商品价格]必填!");
		document.getElementById("price").focus();
		return false;
	} else if (document.getElementById("fileImage").value.length == 0) {
		alert("[商品主图]必填!");
		document.getElementById("fileImage").focus();
		return false;
	}  else if (document.getElementById("tamllurl").value.length == 0) {
		alert("[淘宝地址]必填!");
		document.getElementById("tamllurl").focus();
		return false;
	}  else if (document.getElementById("status").value.length == 0) {
		alert("[商品状态]必填!");
		document.getElementById("status").focus();
		return false;
	}
	form.submit();
}

function upchecknull(obj) {
	var fn=document.getElementById("goods").value
	var count = fn.length;
	if(count == 0) {
		alert("[商品类型]必填!");
		document.getElementById("goods").focus();
		return false;
	} else if (document.getElementById("goodsname").value.length == 0) {
		alert("[商品名称]必填!");
		document.getElementById("goodsname").focus();
		return false;
	} else if (document.getElementById("price").value.length == 0) {
		alert("[商品价格]必填!");
		document.getElementById("price").focus();
		return false;
	} else if (document.getElementById("tamllurl").value.length == 0) {
		alert("[淘宝地址]必填!");
		document.getElementById("tamllurl").focus();
		return false;
	}  else if (document.getElementById("status").value.length == 0) {
		alert("[商品状态]必填!");
		document.getElementById("status").focus();
		return false;
	}
	form.submit();
}