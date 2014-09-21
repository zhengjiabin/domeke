$(function() {
	$(document).ready(function(){
		$('#wish').wish();
		var url = "ventwall/select";
		$('#btn2').click(function(){
			var rdo = $('input[name="ventWall.moodid"]:checked').val();
			var saytext = $('#saytext').val();
			var obj = {"ventWall.message":saytext,"ventWall.moodid":rdo};
			var url = "ventwall/save";
			$.showView(obj,url);
		});
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
                	alert("error");
                	console.info(msg);
                }   
            });
		}
	});
    $(function(){
        function maxLimit(){
                var num=$(this).val().substr(0,50);
                $(this).val(num);
                $(this).next().text($(this).val().length+"/50");
        };
        $("#saytext").keyup(maxLimit);
});
});