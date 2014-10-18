$(function() {
		$('#wish').wish();
		var url = "ventwall/select";
		$('#btn2').click(function(){
			var issignin = $(this).attr("name");
			console.log("您今天已经签到！"+issignin);
			if (issignin=="0"){
				jAlert('您今天已经签过到了哦！', '警告对话框');
				return;
			}
			var rdo = $('input[name="ventWall.moodid"]:checked').val();
			var saytext = $('#saytext').val();
			var obj = {"ventWall.message":saytext,"ventWall.moodid":rdo};
			var url = "ventwall/save";
			$.showView(obj,url);
		});      
	$.extend({
		showView:function(obj,url){
			$.ajax({   
                type:"post",    
                dataType:"html",
                url:url, 
                data:obj,
                success:function(data){
                	$('#view').html(data);                  	
                },   
                error:function(){ 
                	console.info(msg);
                }   
            });
		}
	});
});