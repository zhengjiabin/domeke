$(function() {
	$(document).ready(function(){
		var url = "ventwall/select";
		$('#btn2').click(function(){
			var rdo = $('input[name="ventWall.moodid"]:checked').val();
			var saytext = $('#saytext').val();
			var obj = {"ventWall.message":saytext,"ventWall.moodid":rdo};
			var url = "ventwall/save";
			$.showView(obj,url);
		});
	    $("button[lable='删除']").click(function(){
	        var $self = $(this);
	        var $parent = $self.parent();
	        $parent = $parent.parent();
	        var ventwallid = $(this).attr("name");
	        console.log(ventwallid);
	        var obj = {"ventWall.ventwallid":ventwallid};
	        var url = "ventwall/delete/"+ventwallid;
	        var ref = "#content";
	        console.log(ref);
	        $.showView(obj,url,ref); 
	    });  
	});      
	$.extend({
		showView:function(obj,url,ref){
			$.ajax({   
                type:"post",    
                dataType:"html",
                url:url, 
                data:obj,
                success:function(data){
                	console.log("kk"+data);
                	$(ref).html(data);                  	
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
                $(this).next().children("span").text($(this).val().length+"/50");
        };
        $("#saytext").keyup(maxLimit);
});
});