<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>JenkinsBlame</title></head>
<body>
	<div>
		<form action="/jenkinsblame" method="get">
			Servername: (z.B. http://servername.de:port) 
			<input name ="servername" type="text" />
			Jobname: 
			<input name="jobname" type="text" />
			<input type="submit" value="Send" /><br><hr>
		</form>
	</div>
	<% 
		int nr = (Integer) request.getAttribute("lastBuild");
		String builder = (String) request.getAttribute("builder");
		String color = (String) request.getAttribute("color");
	%>
	<div>
		<p>Build Nr. <b><%= nr %></b> 
			<% if(color.equals("red")){ %>destroyed by:<h1 style="color: red;"><%= builder %></h1>
			<% }else{ %>deployed by:<h1 style="color: blue;"><%= builder %></h1><% } %>
		</p>
	</div>
</body>
</html>