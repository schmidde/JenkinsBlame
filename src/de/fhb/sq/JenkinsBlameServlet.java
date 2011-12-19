package de.fhb.sq;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.apphosting.utils.remoteapi.RemoteApiPb.Request;

@SuppressWarnings("serial")
public class JenkinsBlameServlet extends HttpServlet {
	
	private String servername, jobname;
	private RequestDispatcher dispatcher;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		this.servername = req.getParameter("servername");
		this.jobname = req.getParameter("jobname");
		
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
		JenkinsJsonParserInterface jjp = new JenkinsJsonParser(servername, jobname);
		JSONObject json;		

		try {
			
			req.setAttribute("builder", jjp.getLastBuilder());
			req.setAttribute("lastBuild", jjp.getLastBuildNr());
			req.setAttribute("color", jjp.getColor());
			forward("/jenkinsblame.jsp", req, resp);
			
			/*resp.getWriter().println("First Build-Number: " + jjp.getFirstBuild());
			resp.getWriter().println("Last Build-Number: " + jjp.getLastBuildNr());
			for(Object item: jjp.getBuilds()){
				resp.getWriter().println("Builds: " + item);
			}
			resp.getWriter().println("Last Builder: " + jjp.getLastBuilder());
			resp.getWriter().println("Color: " + jjp.getColor());
			resp.getWriter().println("Last Successful Build: " + jjp.getLastGoodBuild());
			resp.getWriter().println("Last Failed Build: " + jjp.getLastBadBuild());
			resp.getWriter().println("Last Timestamp: " + jjp.getLastTimeStamp());*/
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			resp.getWriter().println("Eine JSONException ist aufgetreten");
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void forward(String jspSite, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		dispatcher = getServletContext().getRequestDispatcher(jspSite);
		dispatcher.forward(req, resp);
	}
}
