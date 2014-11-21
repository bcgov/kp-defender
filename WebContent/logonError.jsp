<%@ page session="false" language="java" import="java.util.*" %>
<html>
<head>
<title>Logon Failed</title>
</head>
<body>
<h1>Logon failed</h1>
<h2>Reason <%= request.getAttribute("reason") %></h2>
</body>
</html>
<% 
	HttpSession session = request.getSession(false);
	if(session != null) {
		session.invalidate();
	}
%>