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
		this.jjp = new JenkinsJsonParser(this.server, this.jobName);
		this.jvo = jjp.createJenkinsVO();
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
	}
	
	public void init(){
		
	}
	
	public boolean hasJob(String jobName){
		boolean res = false;
	    String query = "select from " + Project.class.getName();
	    List<Project> projects = (List<Project>) pm.newQuery(query).execute();
	    
	    if(projects.isEmpty()){ res = false;}
	    else{ 
	    	for(Project p: projects){
	    		if(jobName.equals(p.getName())){
	    			res = true;
	    		}
	    	}
	    }
	    return res;
	}
	
	public void initJob() throws IOException, JSONException {
		Build build;
		List<Build> builds = new ArrayList<Build>();
		long ts;
		int nr;
		String color, builder;
		if(jvo != null){
			for(Object o: jjp.getBuilds()){
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
	
	public String checkColor(){
		String actualColor, persistentColor, stat = null;
		Build persistentBuild;
		List<Build> builds;
		String query ="select " + jobName + "from " + Project.class.getName();
		Project proj = (Project)pm.newQuery(query).execute();
		
		builds = proj.getBuilds();
		persistentBuild = builds.get(builds.lastIndexOf(builds));
		persistentColor = persistentBuild.getColor();
		actualColor = jvo.getColor();
		
		if(persistentColor.equals("red") && actualColor.equals("red")){
			stat = "destroyed";
		}
		else if(persistentColor.equals("blue") && actualColor.equals("red")){
			stat = "destroyed";
		}
		else if(persistentColor.equals("red") && actualColor.equals("blue")){
			stat = "fixed";
		}
		else if(persistentColor.equals("blue") && actualColor.equals("blue")){
			stat = "successful";
		}
		
		return stat;
	}
	
	public boolean isCrashed(){
		return false;
	}

}
