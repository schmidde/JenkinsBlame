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
			if(new JenkinsBlameStatsServlet().hasJob("Auto-B-Day")){
				req.setAttribute("builder", jjp.getLastBuilder());
				req.setAttribute("lastBuild", jjp.getLastBuildNr());
				req.setAttribute("color", jjp.getColor());
				forward("/jenkinsblame.jsp", req, resp);
			}
			else{
				req.setAttribute("builder", "Job Error");
				req.setAttribute("lastBuild", 0);
				req.setAttribute("color", "red");
				forward("/jenkinsblame.jsp", req, resp);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			resp.getWriter().println("Eine JSONException ist aufgetreten");
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			resp.getWriter().println("Eine ServletException ist aufgetreten");
			e.printStackTrace();
		}

	}
	public void forward(String jspSite, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		dispatcher = getServletContext().getRequestDispatcher(jspSite);
		dispatcher.forward(req, resp);
	}
}
