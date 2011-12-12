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
		JenkinsJsonParserServlet jjp = new JenkinsJsonParserServlet("http://jenkins.rambow.it:8080", "JenkinsBlame");
		JSONObject json;

		try {
			
			resp.getWriter().println("Last Build-Number: " + jjp.getLastBuildNr());
			resp.getWriter().println("Last Builder: " + jjp.getLastBuilder());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			resp.getWriter().println("Ein Fehler ist aufgetreten");
			e.printStackTrace();
		}

	}
}
