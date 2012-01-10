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
		JenkinsBlameStats jbs = new JenkinsBlameStats(servername, jobname);
		JSONObject json;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		try {
			//ist URL korrekt?
			if((jbs.isAdress(servername)) && (!jobname.equals(""))){
				//ist Job bereits vorhanden?
				if(!jbs.hasJob(jobname)){
					//Job erstmalig mit allen Builds eintragen
					System.out.println("Neues Prejekt wird eingetragen");
					jbs.initJob();
					status = jbs.checkColor();
				}
				else {
					System.out.println("Dieses Projekt wird bereits beobachtet");
					//Prüfen ob aktueller Build neuer als letzter Eingetragener ist
					if(jbs.isNew(jjp.getLastBuildNr())){
						//fehlende Builds eintragen
						System.out.println("Neueste Builds werden gespeichert");
						jbs.addBuild(jobname);
						//wie ist denn jetzt der Status?
						status = jbs.checkColor();
					}
					else System.out.println("Es hat sich noch nichts getant");
					//Status bestimmen
					status = jbs.checkColor();
				}
				
				//HTTP-Parameter definieren
				req.setAttribute("servername", this.servername);
				req.setAttribute("jobname", this.jobname);
				req.setAttribute("server", "yes");
				req.setAttribute("builder", jjp.getLastBuilder());
				req.setAttribute("lastBuild", jjp.getLastBuildNr());
				req.setAttribute("color", jjp.getColor());
				req.setAttribute("status", status);
				if(!jbs.getOverviews().isEmpty()){
					req.setAttribute("overview", jbs.getOverviews());
				}
				else{
					req.setAttribute("overview", null);
				}
				
				//Webseite anzeigen
				forward("/jenkinsblame.jsp", req, resp);
			}
			else{ 
				//Parameter definieren
				req.setAttribute("server", "no");
				forward("/jenkinsblame.jsp", req, resp);
			}
		} 
		catch (ServletException e) {
			// TODO Auto-generated catch block
			resp.getWriter().println("Eine ServletException ist aufgetreten");
			e.printStackTrace();
		}
		finally{}

	}
	public void forward(String jspSite, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		dispatcher = getServletContext().getRequestDispatcher(jspSite);
		dispatcher.forward(req, resp);
	}
}
