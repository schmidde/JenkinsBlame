package de.fhb.sq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.*;

import org.json.JSONException;

public class JenkinsBlameStatsServlet extends HttpServlet{
	
	private String server, jobName;
	private JenkinsJsonParserInterface jjp;
	private PersistenceManager pm;
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
		
		pm = new PMF().get().getPersistenceManager();
	    String query = "select from " + Project.class.getName();
	    
	    try{
	    	List<Project> projects = (List<Project>) pm.newQuery(query).execute();
	    	if(projects.isEmpty()){ res = false;}
		    else{ 
		    	for(Project p: projects){
		    		if(this.jobName.equals(p.getName())){
		    			res = true;
		    		}
		    	}
		    }
	    }
	    finally{
	    	pm.close();
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
			for(Object o: jvo.getBuilds()){
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
			pm = new PMF().get().getPersistenceManager();
            pm.makePersistent(newProj);
        } finally {
            pm.close();
        }
	}
	
	public void addBuild(){}
	
	public String checkColor(){
		String actualColor, persistentColor, stat = null;
		Build persistentBuild;
		List<Build> builds;
		List<Project> projects;
		Project proj = null;
		
		pm = new PMF().get().getPersistenceManager();
		
		Query query = pm.newQuery(Project.class);
		query.setFilter("name == param");
		query.declareParameters("String param");
		
		try{
			projects = (List<Project>) query.execute(this.jobName);
			builds = new ArrayList<Build>();
			if(!projects.isEmpty()){
				for(Project p: projects){
					System.out.println(p.getName());
					if(!p.getBuilds().isEmpty()){
						for(Build b: p.getBuilds()){
							builds.add(b);
							System.out.println(b.getNr() + " " + b.getBuilder() + " " + b.getTimestamp());
						}
					}else System.out.println("builds is empty");
				}
			}
			else System.out.println("projects empty!");
		}
		finally{
			query.closeAll();
			pm.close();
		}
		persistentBuild = builds.get(0);
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
	public void deleteJob(String job){
		pm = new PMF().get().getPersistenceManager();
	    String query = "select from " + Project.class.getName();
	    
	    try{
	    	List<Project> projects = (List<Project>) pm.newQuery(query).execute();
	    	if(!projects.isEmpty()){
	    		for(Project p: projects){
	    			System.out.println(p.getName());
	    			if(p.getName().equals(job)){
	    				pm.deletePersistent(p);
	    			}
		    	}
	    	}
		    else{ 
		    	System.out.println("keine Projekte mit Name: " + job + "vorhanden");
		    }
	    }
	    finally{
	    	pm.close();
	    }
	}
	public void deleteAllJobs(){
		
	}
}
