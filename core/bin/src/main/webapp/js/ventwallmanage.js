$(function(){
        $("button[lable='修改']").click(function(){
          var $self = $(this);
          var $parent = $self.parent();
          $parent = $parent.parent();
          var selectnum =$('td select',$parent).val();
          var ventwallid = $(this).attr("name");
          var num = $('span').text();
          var obj = {"ventWall.creater":selectnum,"ventWall.ventwallid":ventwallid};
          var url = "ventwall/updateById/"+num;
          $.showView(obj,url); 
          jAlert('修改发泄墙状态成功！', '提示对话框');
      });
        $("button[lable='删除']").click(function(){
          var $self = $(this);
          var $parent = $self.parent();
          $parent = $parent.parent();
          var ventwallid = $(this).attr("name");
          var num = $('span').text();
          var obj = {"ventWall.ventwallid":ventwallid};
          var url = "ventwall/delete/"+num;
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
                    	$('#content').html(data);
                    },   
                    error:function(){ 
                        console.info("error");
                    }   
                });
           	}
      	});       
})