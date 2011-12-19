package de.fhb.sq;

import java.io.IOException;
import javax.servlet.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class JenkinsBlameServlet extends HttpServlet {
	
	private String servername, jobname;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		this.servername = req.getParameter("servername");
		this.jobname = req.getParameter("jobname");
		
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
		JenkinsJsonParserInterface jjp = new JenkinsJsonParser(servername, jobname);
		JSONObject json;		

		try {
			
			resp.getWriter().println("First Build-Number: " + jjp.getFirstBuild());
			resp.getWriter().println("Last Build-Number: " + jjp.getLastBuildNr());
			for(Object item: jjp.getBuilds()){
				resp.getWriter().println("Builds: " + item);
			}
			resp.getWriter().println("Last Builder: " + jjp.getLastBuilder());
			resp.getWriter().println("Color: " + jjp.getColor());
			resp.getWriter().println("Last Successful Build: " + jjp.getLastGoodBuild());
			resp.getWriter().println("Last Failed Build: " + jjp.getLastBadBuild());
			resp.getWriter().println("Last Timestamp: " + jjp.getLastTimeStamp());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			resp.getWriter().println("Eine JSONException ist aufgetreten");
			e.printStackTrace();
		}

	}
}
