package de.fhb.sq;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

public class JenkinsBlameStatsServlet extends HttpServlet{
	
	private JenkinsJsonParserInterface jjp = new JenkinsJsonParserStub("Url", "Job");
	private PersistenceManager pm = new PMF().get().getPersistenceManager();
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
	}
	
	public void init(){
		
	}

}
