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
	
	public void showAllProjects(){
		pm = new PMF().get().getPersistenceManager();
	    Query query = pm.newQuery(Project.class);
	    
	    try{
	    	List<Project> projects = (List<Project>) query.execute();
	    	if(!projects.isEmpty()){
	    		for(Project p: projects){
		    		System.out.println("showAllProjects: " + p.getName());
		    	}
	    	} else System.out.println("keine Projekte in DB");
	    }
	    finally{
	    	pm.close();
	    }
	}
	public void getBuildsByName(String jobName){
		
		Build build;
		List<Build> builds = new ArrayList<Build>();
		
		pm = new PMF().get().getPersistenceManager();
		Query query = pm.newQuery(Project.class);
		query.setFilter("name == param");
	    query.declareParameters("String param");
	    
	    try{
	    	List<Project> pros = (List<Project>) query.execute(jobName);
	    	if(!pros.isEmpty()){
		    	for(Project p: pros){
		    		if(!p.getBuilds().isEmpty()){
			    		if(p.getName().equals(jobName)){
			    			for(Build b: p.getBuilds()){
			    				builds.add(b);
			    				System.out.println(b.getNr() + " " + b.getBuilder());
			    			}
			    		}
		    		}
		    	}
	    	}
	    }
		finally{
			pm.close();
		}
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
		    		System.out.println("hasJob: " + p.getName());
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
		int lastPersistentBuild = 0;
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
					System.out.println("isNew " + p.getName());
					if(!p.getBuilds().isEmpty()){
						for(Build b: p.getBuilds()){
							builds.add(b);
						}
						lastPersistentBuild = builds.get(0).getNr();
					}else System.out.println("builds is empty");
				}
			}
			else System.out.println("projects empty!");
			
		}
		finally{
			pm.close();
		}
		
		System.out.println("Neu: " + nr + "\tAlt: " + lastPersistentBuild);
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
		List<Build> buildsPersist = new ArrayList<Build>();
		List<Build> buildsNeu = new ArrayList<Build>();
		List<Project> projects;
		Project project = null;
		long ts;
		int nr, max = 0;
		String color, builder;
		
		pm = new PMF().get().getPersistenceManager();
		Query query = pm.newQuery(Project.class);
		query.setFilter("name == param");
		query.declareParameters("String param");
		query.setOrdering("name asc");
		
		try {
			projects = (List<Project>) query.execute(this.jobName);
			if(!projects.isEmpty()){
				project = projects.get(0);
					if(!project.getBuilds().isEmpty()){
						for(Build b: project.getBuilds()){
							buildsPersist.add(b);
							System.out.println("Persistentes Build: " + b.getNr() + " " + b.getBuilder());
						}
					}else System.out.println("builds is empty");
				
			} else System.out.println("projects empty!");
			
			for(int i = 0; i < buildsPersist.size(); i++){
				if(buildsPersist.get(i).getNr() > max){
					max = buildsPersist.get(i).getNr();
				}
			}
			
			if(jjp.getColor() != null){
				for(Object o: jvo.getBuilds()){
						ts = jjp.getTimeStamp((Integer)o);
						nr = (Integer)o;
						color = jjp.getColor((Integer)o);
						builder = jjp.getBuilder((Integer)o);
						build = new Build(ts, nr, color, builder);
						buildsNeu.add(build);
				}
				
				for(Build b: buildsNeu){
					if(b.getNr() > max){
						buildsPersist.add(b);
					}
				}
			}
			else System.out.println("Fehler");
			
			
			project.setBuilds(buildsPersist);
			project.setLastSuccessfulBuild(jjp.getLastGoodBuild());
			project.setLastFailedBuild(jjp.getLastBadBuild());
			
			System.out.println("Persistente Liste: ");
			for(Build b: buildsPersist){
				System.out.println("Builds: " + b.getNr());
			}
        } 
		finally {
			//query.closeAll();
            pm.close();
        }
	}
	
	public String checkColor(){
		int max = 0, maxIndex = 0;
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
							System.out.println("\t" + b.getNr() + " " + b.getBuilder() + " " + b.getTimestamp());
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
		for(int i = 0; i < builds.size(); i++){
			if(builds.get(i).getNr() > max){
				max = builds.get(i).getNr();
				maxIndex = i;
			}
		}
		System.out.println("max: " + max);
		System.out.println("maxIndex: " + maxIndex);
		
		if((builds.get(0).getColor() != null) && (jvo.getColor() != null)){
			persistentColor = builds.get(maxIndex).getColor();
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
