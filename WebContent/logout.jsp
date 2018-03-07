<%@ page session="false" language="java" import="java.util.*" %>
<html>
<head>
<title>Logged Out</title>
</head>
<body>
<h1>Logged Off</h1>
<h2>You have successfully logged off.</h2>
<p><a href="/QPDefender"><em>Go to qpdefender home</em></a></p>
</body>
</html>
<% 
	HttpSession session = request.getSession(false);
	if(session != null) {
		session.invalidate();
	}
%>