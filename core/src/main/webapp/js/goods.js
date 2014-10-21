
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
	} else if (document.getElementById("tamllurl").value.length == 0) {
		alert("[淘宝地址]必填!");
		document.getElementById("tamllurl").focus();
		return false;
	} else if (document.getElementById("status").value.length == 0) {
		alert("[商品状态]必填!");
		document.getElementById("status").focus();
		return false;
	} else if (document.getElementById("message").value.length == 0) {
		alert("[商品描述]必填!");
		document.getElementById("message").focus();
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
	} else if (document.getElementById("message").value.length == 0) {
		alert("[商品描述]必填!");
		document.getElementById("message").focus();
		return false;
	}
	form.submit();
}

//显示上传图片
function onUploadImgChange(node){
	var createHtml = $(node).closest("#createHtml");
	var preview = createHtml.find("#preview").first();
	if(node.value == null || node.value ==''){
		preview.html("");
		return false;
	}else if( !node.value.match( /.jpg|.gif|.png|.bmp/i ) ){
		preview.html("");
        return false;
    }
    if (node.files && node.files[0]){  
    	var reader = new FileReader();  
    	reader.onload = function(evt){
    		preview.html('<img data-src="holder.js/260x180" alt="260x180" style="width: 260px; height: 180px;" src="' + evt.target.result + '" />');
    	}
    	reader.readAsDataURL(node.files[0]);
    }else{
    	preview.html('<div class="img" style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src=\'' + node.value + '\'"></div>');
    }  
}

function onUploadImgChanges(node){
	var createHtml = $(node).closest("#createHtml");
	var preview = createHtml.find("#previews").first();
	if(node.value == null || node.value ==''){
		preview.html("");
		return false;
	}else if( !node.value.match( /.jpg|.gif|.png|.bmp/i ) ){
		preview.html("");
        return false;
    }
    if (node.files && node.files[0]){  
    	var reader = new FileReader();  
    	reader.onload = function(evt){
    		preview.html('<img data-src="holder.js/260x180" alt="260x180" style="width: 260px; height: 180px;" src="' + evt.target.result + '" />');
    	}
    	reader.readAsDataURL(node.files[0]);
    }else{
    	preview.html('<div class="img" style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src=\'' + node.value + '\'"></div>');
    }  
}