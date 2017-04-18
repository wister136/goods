<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>left</title>
    <base target="body"/>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/menu/mymenu.js'/>"></script>
	<link rel="stylesheet" href="<c:url value='/menu/mymenu.css'/>" type="text/css" media="all">
	<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/left.css'/>">
<script language="javascript">
/*
*1.对象名必须与第一个参数相同！
*2.第二个参数是显示在菜单上的大标题
*/
var bar = new Q6MenuBar("bar", "飞马汽车商城");
$(function() {
	bar.colorStyle = 4;//制定配色的背景样式，一共1.2.3.4
	bar.config.imgDir = "<c:url value='/menu/img/'/>";//小工具所需图片的路劲
	bar.config.radioButton=true;//是否排斥，多了一级分类是否排斥

	/*
	1.程序设计：一级分类名称
	2.java javascript：二级分类名称
	3./goods/jsps/book/list.jsp:点击二级分类后连接到的URL
	4.body:链接的内容在哪个框架页显示
	*/
	<c:forEach items="${parents}" var="parent">
		<c:forEach items="${parent.children}" var="child">
			bar.add("${parent.cname}", "${child.cname}", "/goods/jsps/book/list.jsp", "body");
		</c:forEach>
	</c:forEach>
	
	$("#menu").html(bar.toString());
});
</script>
</head>
   
<body>  
  <div id="menu"></div>
</body>
</html>
