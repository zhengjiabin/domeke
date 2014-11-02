//跳转到发泄墙
function goToVentWall(){
	window.location.href="./ventwall?menuid=6";
}

//显示更多版块
function showMoreWondersType(node,wondersTypeList){
	var asideHtml = $(node).closest("#asideHtml");
	var showMore = asideHtml.find("#showMore").first();
	showMore.attr("hidden","block");
	
	$.post("./wondersType/showMoreWondersType", function(data) {
		var showMoreHtml = asideHtml.find("#showMoreHtml").first();
		showMoreHtml.html(data);
	});

}

//跳转版块明细
function findById(node,targetId,wondersTypeId) {
	var url = "./ofWonders/findById?wondersTypeId="+wondersTypeId;
	$.post(url,{
		targetId : targetId
	}, function(data) {
		var baseWondersType = $(node).closest("#baseWondersType");
		var detailContentHtml = baseWondersType.find("#detailContentHtml").first();
		detailContentHtml.html(data);
	});
}

//置顶功能
function setTop(node,userId,targetId){
	if("1" != userId){
		alert("非管理员，设置无效！");
	}
	var url = "./ofWonders/setTop";
	$.post(url,{
		targetId : targetId
	}, function(data) {
		if(data == 1){
			alert('非管理员禁止操作！');
		}else if(data == 2){
			alert('设置失败！');
		}else{
			alert('设置成功！');
		}
	});
}

//精华功能
function setEssence(node,userId,targetId){
	if("1" != userId){
		alert("非管理员，设置无效！");
	}
	var url = "./ofWonders/setEssence";
	$.post(url,{
		targetId : targetId
	}, function(data) {
		if(data == 1){
			alert('非管理员禁止操作！');
		}else if(data == 2){
			alert('设置失败！');
		}else{
			alert('设置成功！');
		}
	});
}

//跳转指定版块
function goToOrderForum(node,wondersTypeId) {
	var url = "./ofWonders?wondersTypeId=" + wondersTypeId;
	$.post(url, function(data) {
		var wondersTypeLayout = $(node).closest("#wondersTypeLayout");
		var baseWondersType = wondersTypeLayout.find("#baseWondersType").first();
		baseWondersType.html(data);
	});
}

//跳转到版块根目录
function goToHomeForum(){
	window.location.href="./ofWonders/home";
}

//点击发表主题按钮事件
function skipWondersType(node) {
	var wondersTypeLayout = $(node).closest("#wondersTypeLayout");
	var baseWondersType = wondersTypeLayout.find("#baseWondersType").first();
	var url = "./wondersType/skipWondersType";
	$.post(url, function(data) {
		if(data == false){
			alert("5分钟内只能发布一次同类型主题！");
		}else{
			baseWondersType.html(data);
		}
	});
}

//切换主题明细
function selectFirst(node){
	var firstVal = $(node).val();
	var wondersTypeSelectHtml = $(node).closest("#wondersTypeSelectHtml");
	var showWondersType = wondersTypeSelectHtml.find("#showWondersType").first();
	showWondersType.html("");
	
	$.post("./wondersType/selectSon", {
		pId :firstVal
	}, function(data) {
		var fatherNode = $(node).closest("#table");
		var sonNode = fatherNode.find("#son").first();
		sonNode.html(data);
	});
}

// 显示主题明细
function showWondersType(node){
	var firstVal = $(node).val();
	$.post("./wondersType/goToWondersType", {
		wondersTypeId :firstVal
	}, function(data) {
		if(data == 1){
			alert("跳转主题申请失败！");
		}else if(data == 2){
			alert("5分钟内只能发布一次同类型主题！");
		}else{
			var wondersTypeSelectHtml = $(node).closest("#wondersTypeSelectHtml");
			var showWondersType = wondersTypeSelectHtml.find("#showWondersType").first();
			showWondersType.html(data);
		}
	});
}

//提交主题
function submitOfWonders(node,wondersTypeId){
	canSubmit = true;
	$("form :input[required=required]").trigger('blur');
    var numError = $('form .onError').length;
    if(numError){
    	canSubmit = false;
    }
    var content = CKEDITOR.instances.ckeditor.getData();
    if(content == ""){
    	canSubmit = false;
    }
    if(canSubmit){
    	submitCreate(node,wondersTypeId)
    } else {
    	if(content == ""){
    		alert("内容不能为空！");
    	}else{
    		alert("提交失败，存在非规范内容，请检查！");
    	}
    }
}

//提交主题
function submitCreate(node) {
	var content = CKEDITOR.instances.ckeditor.getData();
	var createHtml = $(node).closest("#createHtml");
	var message = createHtml.find("#message").first();
	message.val(content);
	
	createHtml.submit();
}

//异步提交
function onSubmitCreate(node,wondersTypeId){
	var wondersTypeLayout = $(node).closest("#wondersTypeLayout");
	var baseWondersType = wondersTypeLayout.find("#baseWondersType").first();
	$(node).ajaxSubmit({
		type:"post",
		url:"./ofWonders/create?wondersTypeId="+wondersTypeId,
		success:function(data) {
			if(data == 1){
				alert("上传的文件有误，请重新上传");
			}else if(data == 2){
				alert("上传文件失败，请联系管理员");
			}else{
				baseWondersType.html(data);
			}
		}
	});
	return false;
}

//点击版块首页的热门主题
function skipDetail(node,ofWondersId){
	var url = "./ofWonders/findById";
	$.post(url,{
		targetId : ofWondersId
	}, function(data) {
		var baseWondersType = $(node).closest("#baseWondersType");
		data = "<article id=\"detailContentHtml\"> " + data + " </article>";
		baseWondersType.html(data);
	});
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
    		preview.html('<img src="' + evt.target.result + '" />');
    	}
    	reader.readAsDataURL(node.files[0]);
    }else{
    	preview.html('<div class="img" style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src=\'' + node.value + '\'"></div>');
    }  
}