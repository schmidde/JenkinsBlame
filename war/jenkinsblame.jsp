<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>JenkinsBlame</title></head>
<body>
	<div>
		<form action="/jenkinsblame" method="get">
			Servername: (z.B. http://servername:port) 
			<input name ="servername" type="text" />
			Jobname: 
			<input name="jobname" type="text" />
			<input type="submit" value="Send" /><br><hr>
		</form>
	</div>
	<div>
		<% String s = (String)request.getAttribute("builder"); %>
		<%= s %>
	</div>
</body>
</html>