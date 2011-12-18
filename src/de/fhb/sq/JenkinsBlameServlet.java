package de.fhb.sq;

import java.io.IOException;
import javax.servlet.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class JenkinsBlameServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
		JenkinsJsonParserInterface jjp = new JenkinsJsonParserStub("http://jenkins.rambow.it:8080", "Auto-B-Day");
		JSONObject json;

		try {
			
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
