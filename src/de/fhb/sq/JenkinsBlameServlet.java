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
		JenkinsDataCaller dc = new JenkinsDataCaller();
		JSONObject json;
		try {
			json = dc.callJson("http://jenkins.rambow.it:8080/job/Auto-B-Day/21/api/json");
			resp.getWriter().println(json.toString());
			String[] names = json.getNames(json);
			for(int i = 0; i < names.length; i++){
				resp.getWriter().println(names[i]);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
