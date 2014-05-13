<%--
  Created by IntelliJ IDEA.
  User: joeljohnson
  Date: 5/5/14
  Time: 6:39 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<title></title>
</head>
<body>
	<form action="${pageContext.request.contextPath}/script/upload" method="post" enctype="multipart/form-data">
		<label>
			Name: <input type="text" name="name"/>
		</label>
		<br/>
		<input type="file" name="file" />
		<input type="submit" />
	</form>
</body>
</html>
