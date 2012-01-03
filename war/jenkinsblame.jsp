<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>JenkinsBlame</title></head>
<body>
	<div style="background-color: navajowhite; padding-left: 5px; border-top: 1px solid; border-bottom: 1px solid;"><br>
		<form action="/jenkinsblame" method="get">
			Servername: (z.B. http://servername.de:port) 
			<input name ="servername" type="text" />
			Jobname: 
			<input name="jobname" type="text" />
			<input type="submit" value="Send" /><br>
		</form>
	</div>
	<%  
		String server = "", color = null, builder = null, status = null;
		int nr = 0;
		if(request.getAttribute("server") != null){
			server = (String) request.getAttribute("server");
		}
		if(request.getAttribute("lastBuild") != null){
			nr = (Integer) request.getAttribute("lastBuild");
			builder = (String) request.getAttribute("builder");
			color = (String) request.getAttribute("color");
			status = (String) request.getAttribute("status");
		}
	%>
	<% if(server.equals("yes")){ %>
	<div><% if(status != null){ %>
		<p>Build Nr. <b><%= nr %></b> 
			<% if(status.equals("successful")){ %>successful deployed by:<h1 style="color: blue;"><%= builder %></h1>
			<% }else if(status.equals("destroyed")){ %>destroyed by:<h1 style="color: red;"><%= builder %></h1>
			<% }else if(status.equals("fixed")){ %>fixed by:<h1 style="color: green;"><%= builder %></h1><% } %>
		</p>
		<% } %>
	</div>
	<% } else if(server.equals("no")){ %>URL nicht korrekt. Bitte korrigieren...
	<% } else{ %><% } %>
</body>
</html>