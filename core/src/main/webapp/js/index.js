//菜单导航 按钮事件 选中后高亮
$("document").ready(function(){ 
 $(".nav_menu li").click(function(){
 $(".nav_menu li").removeClass("LiFuc");//首先移除全部的active
 $(this).addClass("LiFuc");//选中的添加acrive
 });
});
