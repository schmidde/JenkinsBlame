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
	private JenkinsVO jvo;
	
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
		boolean res = true;
		
		pm = new PMF().get().getPersistenceManager();
	    Query query = pm.newQuery(Project.class);
	    query.setFilter("name == param");
	    query.declareParameters("String param");
	    
	    try{
	    	List<Project> projects = (List<Project>) query.execute(jobName);
	    	if(projects.isEmpty()){ res = false;}
		    else{ 
		    	for(Project p: projects){
		    		System.out.println(p.getName());
		    	}
		    	res = true;}
	    }
	    finally{
	    	pm.close();
	    }
	    return res;
	}
	
	public boolean isNew(int nr){
		boolean res = false;
		Build build;
		int lastPersistentBuild;
		List<Build> builds = new ArrayList<Build>();
		List<Project> projects;
		
		pm = new PMF().get().getPersistenceManager();
		Query query = pm.newQuery(Project.class);
		query.setFilter("name == param");
		query.declareParameters("String param");
		query.setOrdering("name asc");
		
		try{
			
			projects = (List<Project>) query.execute(this.jobName);
			builds = new ArrayList<Build>();
			if(!projects.isEmpty()){
				for(Project p: projects){
					System.out.println(p.getName());
					if(!p.getBuilds().isEmpty()){
						for(Build b: p.getBuilds()){
							builds.add(b);
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
		lastPersistentBuild = builds.get(0).getNr();
		if(nr > lastPersistentBuild){
			res = true;
		}
		return res;
	}
	
	public void initJob() throws IOException, JSONException {
		Build build;
		List<Build> builds = new ArrayList<Build>();
		long ts;
		int nr;
		String color, builder;
		
		if(jvo.getColor() != null){
			for(Object o: jvo.getBuilds()){
				ts = jjp.getTimeStamp((Integer)o);
				nr = (Integer)o;
				color = jjp.getColor((Integer)o);
				builder = jjp.getBuilder((Integer)o);
				build = new Build(ts, nr, color, builder);
				builds.add(build);
			}
			for(Build b: builds){
				System.out.println("Builds: " + b.getNr());
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
	
	public void addBuild(String jobName){
		
		Build build;
		List<Build> builds = new ArrayList<Build>();
		List<Project> projects;
		long ts;
		int nr;
		String color, builder;
		
		pm = new PMF().get().getPersistenceManager();
		Query query = pm.newQuery(Project.class);
		query.setFilter("name == param");
		query.declareParameters("String param");
		query.setOrdering("name asc");
		
		if(jvo.getColor() != null){
			for(Object o: jvo.getBuilds()){
				if(isNew((Integer)o)){
					ts = jjp.getTimeStamp((Integer)o);
					nr = (Integer)o;
					color = jjp.getColor((Integer)o);
					builder = jjp.getBuilder((Integer)o);
					build = new Build(ts, nr, color, builder);
					builds.add(build);
				}
			}
			for(Build b: builds){
				System.out.println("Builds: " + b.getNr());
			}
		}
		else System.out.println("Fehler bei Erstellung von JenkinsVO");
		
		Project newProj = new Project(jobName);
		newProj.setBuilds(builds);
		newProj.setLastFailedBuild(jvo.getLastFailedBuild());
		newProj.setLastSuccessfulBuild(jvo.getLastSuccessfulBuild());
		
		try {
			pm = new PMF().get().getPersistenceManager();
			projects = query.execute(jobName);
            pm.makePersistent(newProj);
        } finally {
            pm.close();
        }
	}
	
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
		query.setOrdering("name asc");
		
		try{
			projects = (List<Project>) query.execute(this.jobName);
			builds = new ArrayList<Build>();
			if(!projects.isEmpty()){
				for(Project p: projects){
					System.out.println(p.getName());
					if(!p.getBuilds().isEmpty()){
						for(Build b: p.getBuilds()){
							builds.add(b);
							//System.out.println("\t" + b.getNr() + " " + b.getBuilder() + " " + b.getTimestamp());
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
		if((builds.get(0).getColor() != null) && (jvo.getColor() != null)){
			persistentColor = builds.get(0).getColor();
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
		}
		return stat;
	}
		
	public void deleteJob(String job){
		pm = new PMF().get().getPersistenceManager();
	    Query query = pm.newQuery(Project.class);
	    query.setOrdering("name asc");
	    
	    try{
	    	List<Project> projects = (List<Project>) query.execute();
	    	if(!projects.isEmpty()){
	    		for(Project p: projects){
	    			if(p.getName().equals(job)){
	    				pm.deletePersistent(p);
	    			}
		    	}
	    	}
		    else{ 
		    	System.out.println("keine Projekte mit Name: " + job + " vorhanden");
		    }
	    }
	    finally{
	    	pm.close();
	    }
	}
	
	public void deleteAllJobs(){
		pm = new PMF().get().getPersistenceManager();
	    Query query = pm.newQuery(Project.class);
	    query.setOrdering("name asc");
	    
	    try{
	    	List<Project> projects = (List<Project>) query.execute();
	    	if(!projects.isEmpty()){
	    		for(Project p: projects){
	    			pm.deletePersistent(p);
		    	}
	    	}
		    else{ 
		    	System.out.println("keine Projekte vorhanden");
		    }
	    }
	    finally{
	    	pm.close();
	    }
	}
}
