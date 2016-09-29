<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<script type="text/javascript" src="../js/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="../js/jquery-ui-1.8.9.custom.min.js"></script>
<script type="text/javascript">
$(function(){
	$("#aj").click(function(){
		var username = $("#username").val();
		$.ajax({
			url : "../login/ajaxReq?pass=123",
			dataType : "json",
			type: "POST",
			data:{username:username},
			success : function(data){
				alert(data.result);
			}
		});
	});
});
</script>

</head>
	<body>
		<input type="text" id="username" />
		<input type="button" id="aj" value="AJAX"/>
		<br/><br/>
		
		<form action="../login/submitForm" method="post">
			<input type="text" id="userName" name="user.userName" />
			<br/>
			<input type="text" id="address" name="user.address"/>
			<br/>
			<input type="submit" value="提交"/>
		</form>
	</body>
</html>