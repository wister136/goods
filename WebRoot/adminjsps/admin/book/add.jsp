<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'bookdesc.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<link rel="stylesheet" type="text/css" href="<c:url value='/adminjsps/admin/css/book/add.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/jquery/jquery.datepick.css'/>">
<script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jquery/jquery.datepick.js'/>"></script>
<script type="text/javascript" src="<c:url value='/jquery/jquery.datepick-zh-CN.js'/>"></script>
<script type="text/javascript">
$(function () {
	$("#publishtime").datepick({dateFormat:"yy-mm-dd"});
	$("#printtime").datepick({dateFormat:"yy-mm-dd"});
	
	$("#btn").addClass("btn1");
	$("#btn").hover(
		function() {
			$("#btn").removeClass("btn1");
			$("#btn").addClass("btn2");
		},
		function() {
			$("#btn").removeClass("btn2");
			$("#btn").addClass("btn1");
		}
	);
	
	$("#btn").click(function() {
		var bname = $("#bname").val();
		var currprice = $("#currprice").val();
		var price = $("#price").val();
		var discount = $("#discount").val();
		var brand=$("#brand").val();
		var manufacturer = $("#manufacturer").val();
		var registration = $("#registration").val();
		var pid = $("#pid").val();
		var cid = $("#cid").val();
		var image_w = $("#image_w").val();
		var image_b = $("#image_b").val();
		
		if(!bname || !currprice || !price || !discount ||!brand|| !manufacturer || !registration || !pid || !cid || !image_w || !image_b) {
			alert("车名、售价、定价、折扣、品牌、出产厂家、上牌日期、1级分类、2级分类、大图、小图都不能为空！");
			return false;
		}
		
		if(isNaN(currprice) || isNaN(price) || isNaN(discount)) {
			alert("当前价、定价、折扣必须是合法小数！");
			return false;
		}
		$("#form").submit();
	});
});


function loadChildren() {
	/*
	1. 获取pid
	2. 发出异步请求，功能之：
	  3. 得到一个数组
	  4. 获取cid元素(<select>)，把内部的<option>全部删除
	  5. 添加一个头（<option>请选择2级分类</option>）
	  6. 循环数组，把数组中每个对象转换成一个<option>添加到cid中
	*/
	// 1. 获取pid
	var pid = $("#pid").val();
	// 2. 发送异步请求
	$.ajax({
		async:true,
		cache:false,
		url:"/goods/admin/AdminCarServlet",
		data:{method:"ajaxFindChildren", pid:pid},
		type:"POST",
		dataType:"json",
		success:function(arr) {
			// 3. 得到cid，删除它的内容
			$("#cid").empty();//删除元素的子元素
			$("#cid").append($("<option>==请选择2级分类==</option>"));//4.添加头
			// 5. 循环遍历数组，把每个对象转换成<option>添加到cid中
			for(var i = 0; i < arr.length; i++) {
				var option = $("<option>").val(arr[i].cid).text(arr[i].cname);
				$("#cid").append(option);
			}
		}
	});
}
</script>
  </head>
  
  <body>
  <div>
   <p style="font-weight: 900; color: red;">${msg }</p>
   <form action="<c:url value='/admin/AdminAddCarServlet'/>" enctype="multipart/form-data" method="post" id="form">
    <div>
	    <ul>
	    	<li>车名：　<input id="bname" type="text" name="bname" value="" style="width:500px;"/></li>
	    	<li>大图：　<input id="image_w" type="file" name="image_w"/></li>
	    	<li>小图：　<input id="image_b" type="file" name="image_b"/></li>
	    	<li>售价：　<input id="price" type="text" name="price" value="" style="width:50px;"/>	万</li>
	    	<li>指导价：<input id="currprice" type="text" name="currprice" value="" style="width:50px;"/>	万</li>		
	    	<li>优惠：　<input id="discount" type="text" name="discount" value="" style="width:50px;"/>	万</li>
	    	<li>车型：	 <input id="brand" type="text" name="brand" value="" style="width:80px;"/> </li>
	    </ul>
		<hr style="margin-left: 50px; height: 1px; color: #dcdcdc"/>
		<table>
			<tr>
				<td colspan="3">
					出产厂家：<input type="text" id="manufacturer" name="manufacturer" value="" style="width:150px;"/>
				</td>
			</tr>
			<tr>
				<td colspan="3">上牌时间：<input type="text" id="registration" name="registration" value="" style="width:100px;"/></td>
			</tr>
			<tr>
				<td width="250">上市时间：<input type="text" name="timetomarket" id="timetomarket" value="" style="width:100px;"/></td>
				<td>公里数：　　<input type="text" name="carkil" id="carkil" value="" style="width:50px;"/>万</td>
				<td>上牌地区：　　<input type="text" name="registarea" id="registarea" value="" style="width:80px;"/></td>
			</tr>
			<tr>
				<td>
					一级分类：<select name="pid" id="pid" onchange="loadChildren()">
						<option value="">==请选择1级分类==</option>
					<c:forEach items="${parents}" var="parent">
			    		<option value="${parent.cid }" selected='selected'>${parent.cname}</option>
			    	</c:forEach>
					</select>
				</td>
				<td>
					二级分类：<select name="cid" id="cid">
						<option value="">==请选择2级分类==</option>				
					</select>
				</td>
				<td></td>
			</tr>
			<tr>
				<td>
					<input type="button" id="btn" class="btn" value="新车上架">
				</td>
				<td></td>
				<td></td>
			</tr>
		</table>
	</div>
   </form>
  </div>

  </body>
</html>
