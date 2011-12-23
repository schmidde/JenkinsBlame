package de.fhb.sq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

import org.json.JSONException;

public class JenkinsBlameStatsServlet extends HttpServlet{
	
	private String server, jobName;
	private JenkinsJsonParserInterface jjp;
	private PersistenceManager pm = new PMF().get().getPersistenceManager();
	private JenkinsVO jvo = new JenkinsVO();
	
	public JenkinsBlameStatsServlet(String server, String jobName){
		this.server = server;
		this.jobName = jobName;
	}
	
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
	
	public void initJob() throws IOException, JSONException {
		Build build;
		List<Build> builds = new ArrayList<Build>();
		long ts;
		int nr;
		String color, builder;
		jjp = new JenkinsJsonParser(server, jobName);
		jvo = jjp.createJenkinsVO();
		if(jvo != null){
			for(Object o: jjp.getBuilds()){
				System.out.println((Integer)o);
				ts = jjp.getTimeStamp((Integer)o);
				nr = (Integer)o;
				color = jjp.getColor((Integer)o);
				builder = jjp.getBuilder((Integer)o);
				build = new Build(ts, nr, color, builder);
				builds.add(build);
			}
		}
		else System.out.println("Fehler bei Erstellung von JenkinsVO");
		
		Project newProj = new Project(jobName);
		newProj.setBuilds(builds);
		newProj.setLastFailedBuild(jvo.getLastFailedBuild());
		newProj.setLastSuccessfulBuild(jvo.getLastSuccessfulBuild());
		
		try {
            //pm.makePersistent(newProj);
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
