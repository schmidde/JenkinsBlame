package de.fhb.sq;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

public class JenkinsBlameStatsServlet extends HttpServlet{
	
	private JenkinsJsonParserInterface jjp = new JenkinsJsonParserStub("Url", "Job");
	private PersistenceManager pm = new PMF().get().getPersistenceManager();
	private JenkinsVO jvo = new JenkinsVO();
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
	}
	
	public void init(){
		
	}
	public boolean hasJob(String jobName){
		
	    String query = "select from " + Project.class.getName();
	    List<Project> projects = (List<Project>) pm.newQuery(query).execute();
	    
	    if(projects.isEmpty()){ return false;}
	    else{ 
	    	for(Project p: projects){
	    		if(jobName.equals(p.getName())){
	    			return true;
	    		}
	    	}
	    	return false;}
	}
	public void initJob(String jobName){
		Build build;
		List<Build> builds;
		
		for(Object o: jvo.getBuilds()){
			
		}
		jvo = jjp.createJenkinsVO();
		Project newProj = new Project(jobName);
		//newProj.setBuilds();
		try {
            pm.makePersistent(newProj);
        } finally {
            pm.close();
        }

	}
	public void addBuild(){}
	public void checkColor(){}
	public boolean isCrashed(){
		return false;
	}

}
