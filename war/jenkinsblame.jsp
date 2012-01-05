<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, de.fhb.sq.Overview" %>
<html>
<head><title>JenkinsBlame</title></head>
<body>
	<!-- Header -->
	<div style="background-color: navajowhite; padding-left: 5px; border-top: 1px solid; border-bottom: 1px solid;"><br>
		<form action="/jenkinsblame" method="get">
			<b>Servername</b>: (z.B. http://servername.de:port) 
			<input name ="servername" type="text" />
			<b>Jobname:</b> 
			<input name="jobname" type="text" />
			<input type="submit" value="Send" /><br>
		</form>
	</div>
	<%  
		String server = "", color = null, builder = null, status = null;
		int nr = 0;
		List<Overview> ovs;
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
	<!-- Blame-Content -->
	<div style="height: 66%;"><% if(status != null){ %>
		<p style="font-size: 50;">Build Nr. <b><%= nr %></b> 
			<% if(status.equals("successful")){ %>successful deployed by:<h1 style="color: blue; font-size: 100; text-align: center;"><nobr><%= builder %></nobr></h1>
			<% }else if(status.equals("destroyed")){ %>destroyed by:<h1 style="color: red; font-size: 100; text-align: center;"><nobr><%= builder %></nobr></h1>
			<% }else if(status.equals("fixed")){ %>fixed by:<h1 style="color: green; font-size: 100; text-align: center;"><nobr><%= builder %></nobr></h1><% } %>
		</p>
		<% } %>
		<% } else if(server.equals("no")){ %>URL nicht korrekt. Bitte korrigieren...
		<% } else{ %><% } %>
	</div>
	<hr>
	<!-- Overviews -->
	<div>
		<% if(request.getAttribute("overview") != null){
			ovs = (List<Overview>)request.getAttribute("overview");
			
		%>
		<table>
			<tr>
			<% for(Overview ov: ovs){ %>
				<td style="vertical-align: bottom;">
					<table>
						<tr>
							<td>
								<table>
									<tr>
										<td style="vertical-align: bottom;"><p style="height: <%= (ov.getBlau()*10) %>px; width: 25px; background-color: blue"></P></td><td style="vertical-align: bottom;"><p style="height: <%= (ov.getRot()*10) %>px; width: 25px; background-color: red"></p></td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td style="border-top: 1px solid;">
								<b><%= ov.getName() %></b>
							</td>
						</tr>
					</table>
				</td>
			<% } %>
			</tr>
		</table>
		<% } %>
	</div>
</body>
</html>