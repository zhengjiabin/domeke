
//goods判空
function checknull(obj) {
	var re=/^((http|https|ftp):\/\/)?(\w(\:\w)?@)?([0-9a-z_-]+\.)*?([a-z]{2,6}(\.[a-z]{2})?(\:[0-9]{2,6})?)((\/[^?#<>\/\\*":]*)+(\?[^#]*)?(#.*)?)?$/i;  
	var msg = document.getElementById("tamllurl").value;
	if (!re.test(msg)) {
    	alert("商城地址 格式不正确");
    	return false;
    }
	
	var content = CKEDITOR.instances.ckeditor.getData();
	var createHtml = $(obj).closest("#createHtml");
	var message = createHtml.find("#message").first();
	message.val(content);
	    
	var files = $('input[name="headimg"]').prop('files');
	if (files.length > 5) {
		alert("商品子图不能超过5张，请重新上传！");
		return false;
	}
	if (document.getElementById("goodsname").value.length == 0) {
		alert("[商品名称]必填!");
		document.getElementById("goodsname").focus();
		return false;
	} else if (document.getElementById("goodsattr1").value.length == 0) {
		alert("[类型1]必填!");
		document.getElementById("goodsattr1").focus();
		return false;
	} else if (document.getElementById("goodsattr2").value.length == 0) {
		alert("[类型2]必填!");
		document.getElementById("goodsattr2").focus();
		return false;
	} else if (document.getElementById("goodsattr3").value.length == 0) {
		alert("[类型3]必填!");
		document.getElementById("goodsattr3").focus();
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
		alert("[商城地址]必填!");
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
	var money = "^([0-9]+|[0-9]{1,3}(,[0-9]{3})*)(.[0-9]{1,2})?$";
	var my = new RegExp(money);
	if (!my.test(document.getElementById("price").value)){
		alert("价格输入有误！");
		return false;
	} else if (!my.test(document.getElementById("goodsnumber").value)) {
		alert("商品数量输入有误！");
		return false;
	} 
	if (document.getElementById("oldprice").value!=null && document.getElementById("oldprice").value!="" && !my.test(document.getElementById("oldprice").value)){
		alert("历史价格输入有误！");
		return false;
	}
	form.submit();
}

function upchecknull(obj) {
	var content = CKEDITOR.instances.ckeditor.getData();
	var createHtml = $(obj).closest("#createHtml");
	var message = createHtml.find("#message").first();
	message.val(content);
	
	var files = $('input[name="headimg"]').prop('files');
	var filenum = files.length;
	var imgnum = $("img[name='himg']").length + filenum;
	if (imgnum > 5) {
		alert("商品子图不能超过5张，请重新上传！");
		return false;
	}
	var re=/^((http|https|ftp):\/\/)?(\w(\:\w)?@)?([0-9a-z_-]+\.)*?([a-z]{2,6}(\.[a-z]{2})?(\:[0-9]{2,6})?)((\/[^?#<>\/\\*":]*)+(\?[^#]*)?(#.*)?)?$/i;  
	var msg = document.getElementById("tamllurl").value;
	if (!re.test(msg)) {
    	alert("商城地址 格式不正确");
    	return false;
    }
	
	var money = "^([0-9]+|[0-9]{1,3}(,[0-9]{3})*)(.[0-9]{1,2})?$";
	var my = new RegExp(money);
	if (!my.test(document.getElementById("price").value)){
		alert("价格输入有误！");
		return false;
	} else if (document.getElementById("goodsattr1").value.length == 0) {
		alert("[类型1]必填!");
		document.getElementById("goodsattr1").focus();
		return false;
	} else if (document.getElementById("goodsattr2").value.length == 0) {
		alert("[类型2]必填!");
		document.getElementById("goodsattr2").focus();
		return false;
	} else if (document.getElementById("goodsattr3").value.length == 0) {
		alert("[类型3]必填!");
		document.getElementById("goodsattr3").focus();
		return false;
	} else if (!my.test(document.getElementById("goodsnumber").value)) {
		alert("商品数量输入有误！");
		return false;
	} 
	if (document.getElementById("oldprice").value!=null && document.getElementById("oldprice").value!="" && !my.test(document.getElementById("oldprice").value)){
		alert("历史价格输入有误！");
		return false;
	}
	if (document.getElementById("goodsname").value.length == 0) {
		alert("[商品名称]必填!");
		document.getElementById("goodsname").focus();
		return false;
	} else if (document.getElementById("price").value.length == 0) {
		alert("[商品价格]必填!");
		document.getElementById("price").focus();
		return false;
	} else if (document.getElementById("tamllurl").value.length == 0) {
		alert("[商城地址]必填!");
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
//显示上传前的本地图片（多图）
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
	var array = node.files
	var leng = array.length;
//	if (leng > 5) {
//		leng = 0;
//		file = $("#fileImages");   
//		file.after(file.clone());   
//		file.remove();
//		alert("上传图片不能超过5张！");
//	}
	$("#previews").empty();
	for (var i = 0; i < leng; i ++) {
		if (node.files && node.files[i]){  
	    	var reader = new FileReader();  
    		var imgId = i + 1;
	    	reader.onload = function(evt){
	    		$("#previews").prepend('<img id="'+imgId+'" data-src="holder.js/260x180" alt="107x72" style="width: 107px; height: 72px;" src="' + evt.target.result + '" />');
	    		//preview.html('<img id="'+aa+'" data-src="holder.js/260x180" alt="260x180" style="width: 260px; height: 180px;" src="' + evt.target.result + '" />');
	    	}
	    	reader.readAsDataURL(node.files[i]);
	    }
		//else{
	    //	preview.html('<div class="img" style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src=\'' + node.value + '\'"></div>');
	    //}  
	}
    
}