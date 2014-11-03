
//goods判空
function checknull(obj) {
	if (document.getElementById("goodsname").value.length == 0) {
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
	var strRegex = "^((https|http|ftp|rtsp|mms)?://)"
        + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
        + "(([0-9]{1,3}\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
        + "|" // 允许IP和DOMAIN（域名）
        + "([0-9a-z_!~*'()-]+\.)*" // 域名- www.
        + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\." // 二级域名
        + "[a-z]{2,6})" // first level domain- .com or .museum
        + "(:[0-9]{1,4})?" // 端口- :80
        + "((/?)|" // a slash isn't required if there is no file name
        + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
	var re = new RegExp(strRegex);
	if (!re.test(document.getElementById("tamllurl").value)){
		alert("地址格式不整确！");
		return false;
	}
	
	var money = "(?!^0\d+|.*0$)^[0-9]{1,16}(\.[0-9]{1,4})?$|^0$";
	var my = new RegExp(money);
	if (!my.test(document.getElementById("price").value)){
		alert("价格输入有误！");
		return false;
	}
	if (!my.test(document.getElementById("dougprice").value)){
		alert("豆豆价格输入有误！");
		return false;
	}
	if (!my.test(document.getElementById("oldprice").value)){
		alert("历史价格输入有误！");
		return false;
	}
	form.submit();
}

function upchecknull(obj) {
	var strRegex = "^((https|http|ftp|rtsp|mms)?://)"
        + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
        + "(([0-9]{1,3}\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
        + "|" // 允许IP和DOMAIN（域名）
        + "([0-9a-z_!~*'()-]+\.)*" // 域名- www.
        + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\." // 二级域名
        + "[a-z]{2,6})" // first level domain- .com or .museum
        + "(:[0-9]{1,4})?" // 端口- :80
        + "((/?)|" // a slash isn't required if there is no file name
        + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
	var re = new RegExp(strRegex);
	if (!re.test(document.getElementById("tamllurl").value)){
		alert("地址格式不整确！");
		return false;
	}
	
	var money = "(?!^0\d+|.*0$)^[0-9]{1,16}(\.[0-9]{1,4})?$|^0$";
	var my = new RegExp(money);
	if (!my.test(document.getElementById("price").value)){
		alert("价格输入有误！");
		return false;
	}
	if (!my.test(document.getElementById("dougprice").value)){
		alert("豆豆价格输入有误！");
		return false;
	}
	if (!my.test(document.getElementById("oldprice").value)){
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