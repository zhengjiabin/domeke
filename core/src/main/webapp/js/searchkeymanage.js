$(function(){
        $("button[lable='修改']").click(function(){
          var $self = $(this);
          var $parent = $self.parent();
          $parent = $parent.parent();
          var keyname =$('td input',$parent).val();
          var keyid = $(this).attr("name");
          var obj = {"searchKey.keyid":keyid,"searchKey.keyname":keyname};
          var url = "searchkey/updateById/"+keyid;
          $.showView(obj,url); 
      });
        $("button[lable='删除']").click(function(){
          var $self = $(this);
          var $parent = $self.parent();
          $parent = $parent.parent();
          var keyid = $(this).attr("name");
          var obj = {"searchKey.keyid":keyid};
          var url = "searchkey/delete/"+keyid;
          $.showView(obj,url); 
      });
        $("button[lable='新增']").click(function(){
        	var keyname = $('#keyname1').val();
            var obj = {"searchKey.keyname":keyname};
            var url = "searchkey/save";
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
                        alert("error");
                    }   
                });
        	}
       });  	
})