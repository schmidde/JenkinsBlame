package de.fhb.sq;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import javax.jdo.PersistenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class JenkinsBlameServlet extends HttpServlet {
	
	private String servername, jobname;
	private RequestDispatcher dispatcher;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String status = null;
		this.servername = req.getParameter("servername");
		this.jobname = req.getParameter("jobname");
		
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
		JenkinsJsonParserInterface jjp = new JenkinsJsonParser(servername, jobname);
		JenkinsBlameStatsServlet jbs = new JenkinsBlameStatsServlet(servername, jobname);
		JSONObject json;
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {
				if(!jbs.hasJob(jobname)){
					//Job erstmalig bit allen Builds eintragen
					System.out.println("Job wird eingetragen");
					jbs.initJob();
				}
				else {
					//Prüfen ob aktueller Build neuer als letzter eingetragener
					System.out.println("Job bereits in DB");
					if(jbs.isNew(jjp.getLastBuildNr(), pm)){
						//fehlende Builds eintragen
						System.out.println("Job wird eingetragen");
						jbs.addBuild(jobname);
						status = jbs.checkColor();
					}
					else System.out.println("Job ist nicht neu");
					status = jbs.checkColor();
				}
				//jbs.deleteAllJobs();
				
				//Parameter definieren
				req.setAttribute("builder", jjp.getLastBuilder());
				req.setAttribute("lastBuild", jjp.getLastBuildNr());
				req.setAttribute("color", jjp.getColor());
				req.setAttribute("status", status);
				
				//Webseite anzeigen
				forward("/jenkinsblame.jsp", req, resp);
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			resp.getWriter().println("Eine JSONException ist aufgetreten");
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			resp.getWriter().println("Eine ServletException ist aufgetreten");
			e.printStackTrace();
		}
		finally{
			pm.close();
		}

	}
	public void forward(String jspSite, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		dispatcher = getServletContext().getRequestDispatcher(jspSite);
		dispatcher.forward(req, resp);
	}
}
