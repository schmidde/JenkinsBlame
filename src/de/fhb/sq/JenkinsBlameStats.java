package de.fhb.sq;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.jdo.Query;

public class JenkinsBlameStats{
	
	private String server, jobName;
	private JenkinsJsonParserInterface jjp;
	private PersistenceManager pm;
	private Transaction tx;
	private JenkinsVO jvo;
	
	public JenkinsBlameStats(String server, String jobName){
		this.server = server;
		this.jobName = jobName;
		this.jjp = new JenkinsJsonParser(this.server, this.jobName);
		this.jvo = jjp.createJenkinsVO();
	}
		
	/** Prüft Vollstaendigkeit und Gueltigkeit der URL
	 * @return true, wenn ok
	 */
    public boolean isAdress(String serverName){
		return serverName.matches("http://[0-9a-z]{2,}\\.[0-9a-z]{2,}\\.[a-z]{2,3}\\:[0-9]{1,4}");
	}
	
    /** Gibt alle gespeicherten Projecte auf Konsole aus*/
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
	
	/** Gibt alle gespeicherten Builds eines gewuenschten Projects aus */
	public List<Build> getBuildsByName(String jobName){
		
		List<Build> builds = new ArrayList();;
		
		pm = new PMF().get().getPersistenceManager();
		Query query = pm.newQuery(Project.class);
		query.setFilter("name == param");
	    query.declareParameters("String param");
	    
	    try{
	    	List<Project> pros = (List<Project>) query.execute(jobName);
	    	if(!pros.isEmpty()){
		    	for(Project p: pros){
		    		System.out.println(p.getName() + ":");
		    		if(!p.getBuilds().isEmpty()){
			    		if(p.getName().equals(jobName)){
			    			for(Build b: p.getBuilds()){
			    				builds.add(b);
			    				System.out.println("\t" + b.getNr() + " " + b.getBuilder());
			    			}
			    		}
		    		}
		    	}
	    	}
	    }
		finally{
			pm.close();
		}
	    return builds;
	}
	
	/** Prueft ob das benannte Project bereits in der DB gespeichert ist
	 * @return true, wenn vorhanden
	 */	
	public boolean hasJob(String jobName){
		boolean res = true;
		
		pm = new PMF().get().getPersistenceManager();
	    Query query = pm.newQuery(Project.class);
	    query.setFilter("name == param");
	    query.declareParameters("String param");
	    
	    try{
	    	List<Project> projects = (List<Project>) query.execute(jobName);
	    	if(projects.isEmpty()){ res = false;}
		    else{ res = true;}
	    }
	    finally{
	    	pm.close();
	    }
	    return res;
	}
	
	/** Prueft ob Builds vom CI-Server neuer als gespeicherte Builds sind
	 * @return true, wenn Builds neuer sind*/
	public boolean isNew(int nr){
		
		boolean res = false;
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
					if(!p.getBuilds().isEmpty()){
						for(Build b: p.getBuilds()){
							builds.add(b);
						}
						if(builds.size() > 0){
							lastPersistentBuild = builds.get((builds.size()-1)).getNr();
						}
						else{
							lastPersistentBuild = builds.get(0).getNr();
						}
					}else System.out.println("builds is empty");
				}
			}
			else System.out.println("projects empty!");
		}
		finally{
			pm.close();
		}
		if(nr > lastPersistentBuild){
			res = true;
		}
		return res;
	}
	
	/** Erstmaliges eintragen des Projects und aller Builds */
	public void initJob(){
		
		Build build;
		List<Build> builds = new ArrayList<Build>();
		long ts;
		int nr;
		String color, builder;
		
		//ist ueberhaupt etwas vorhanden?
		if(jvo.getColor() != null){
			//Alles wichtige vom CI-Server parsen und in einer ArrayList speichern
			int count = 0;
			for(Object o: jvo.getBuilds()){
				if(count < 21){
					ts = jjp.getTimeStamp((Integer)o);
					nr = (Integer)o;
					color = jjp.getColor((Integer)o);
					builder = jjp.getBuilder((Integer)o);
					build = new Build(ts, nr, color, builder);
					builds.add(build);
					count++;
				}
			}
		}
		else System.out.println("Fehler bei Erstellung von JenkinsVO");
		
		//Neues zu speicherndes Objekt erstellen und befuellen
		Project newProj = new Project(jobName);
		newProj.setBuilds(builds);
		newProj.setLastFailedBuild(jvo.getLastFailedBuild());
		newProj.setLastSuccessfulBuild(jvo.getLastSuccessfulBuild());
		
		//in einer Transaktion versuchen das Obj. im Datastore zu speichern
		try {
			pm = new PMF().get().getPersistenceManager();
			tx = pm.currentTransaction();
	        tx.begin();
            pm.makePersistent(newProj);
            tx.commit();
        } finally {
        	if(tx.isActive()){
        		tx.rollback();
        	}
            pm.close();
        }
	}
	
	/** neueste Builds zum Datastore hinzufuegen */
	public void addBuild(String jobName){
		
		Build build;
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
		
		//Versuche benanntes Obj. aus DS zu holen
		try {
			projects = (List<Project>) query.execute(this.jobName);
			if(!projects.isEmpty()){
				project = projects.get(0);							
			} 
			else System.out.println("projects empty!");
			
			//Suche das neueste Build
			for(int i = 0; i < project.getBuilds().size(); i++){
				if(project.getBuilds().get(i).getNr() > max){
					max = project.getBuilds().get(i).getNr();
				}
			}
			//erstellt neue Liste mit Build-Objekten vom CI-Server
			if(jjp.getColor() != null){
				int count = 0;
				for(Object o: jvo.getBuilds()){
					if(count < 21){
						ts = jjp.getTimeStamp((Integer)o);
						nr = (Integer)o;
						if(jjp.getColor((Integer)o) == null){
							color = "red";
						}else{
							color = jjp.getColor((Integer)o);
						}
						
						builder = jjp.getBuilder((Integer)o);
						build = new Build(ts, nr, color, builder);
						buildsNeu.add(build);
						count++;
					}
				}
				System.out.println("letzter Build: " + buildsNeu.get(0).getNr() + " Max: " + max);
				
				//fuegt alle neuen Builds zur Liste der gespeicherten Builds hinzu
				for(Build b: buildsNeu){
					if(b.getNr() > max){
						project.getBuilds().add(b);
					}
				}
			}
			else System.out.println("Fehler");
			
			//aktualisiert das Projekt
			project.setBuilds(project.getBuilds());
			project.setLastSuccessfulBuild(jjp.getLastGoodBuild());
			project.setLastFailedBuild(jjp.getLastBadBuild());
        } 
		finally {
			//speichert das geaenderte Obj.
			pm.close();
        }
	}
	
	/** Prueft die Veraenderung vom letzten zum aktuellen Build
	 * @return "succsessful", "fixed" oder "destroyed"
	 */
	public String checkColor(){
		
		int max = 0, maxIndex = 0;
		String actualColor, persistentColor, stat = null;
		List<Build> builds;
		List<Project> projects;
		
		pm = new PMF().get().getPersistenceManager();
		Query query = pm.newQuery(Project.class);
		query.setFilter("name == param");
		query.declareParameters("String param");
		query.setOrdering("name asc");
		
		//Holt alle Builds eines Projekts
		try{
			projects = (List<Project>) query.execute(this.jobName);
			builds = new ArrayList<Build>();
			if(!projects.isEmpty()){
				for(Project p: projects){
					if(!p.getBuilds().isEmpty()){
						for(Build b: p.getBuilds()){
							builds.add(b);
						}
					}else System.out.println("builds is empty");
				}
			}
			else System.out.println("projects is empty!");
		}
		finally{
			query.closeAll();
			pm.close();
		}
		
		//Sucht den neuesten Build und dessen Index in der ArrayList
		for(int i = 0; i < builds.size(); i++){
			if(builds.get(i).getNr() > max){
				max = builds.get(i).getNr();
				maxIndex = i;
			}
		}
		//ist irgendwas vorhanden?
		if((builds.get(0).getColor() != null) && (jvo.getColor() != null)){
			//Vergleicht den letzten und vorletzten Build aus dem DS
			if(maxIndex > 0){
				persistentColor = builds.get(maxIndex-1).getColor();
				actualColor = builds.get(maxIndex).getColor();			
			
				//vorher und weiterhin kaputt
				if(persistentColor.equals("red") && actualColor.equals("red")){
					stat = "destroyed";
				}
				//vorher ok, jetzt kaputt
				else if(persistentColor.equals("blue") && actualColor.equals("red")){
					stat = "destroyed";
				}
				//vorher kaputt, jetzt ok
				else if(persistentColor.equals("red") && actualColor.equals("blue")){
					stat = "fixed";
				}
				//vorher und weiterhin ok
				else if(persistentColor.equals("blue") && actualColor.equals("blue")){
					stat = "successful";
				}
			}
			else{ 
				//einziger Build
				persistentColor = builds.get(0).getColor();
				if(persistentColor.equals("blue")) stat = "successful";
				else if(persistentColor.equals("red")) stat = "destroyed";
			}
		}
		return stat;
	}
	
	/** sucht alle Mitarbeiter und zaehlt dessen Builds 
	 * @return Liste mit Overview-Obkekten
	 */
	public List<Overview> getOverviews(){
		
		int rot = 0, blau = 0;
		List<Build> builds = null;
		List<String> member = new ArrayList();
		Overview overview;
		List<Overview> overviews = new ArrayList();
		
		builds = getBuildsByName(this.jobName);
		if(builds != null){
			for(Build b: builds){
				if(b.getBuilder() != null){
					if(!member.contains(b.getBuilder())){
						member.add(b.getBuilder());
						System.out.println(b.getBuilder());
					}
				}
				else if(b.getBuilder() == null){
					if(!member.contains("Autobuilder")){
						member.add("Autobuilder");
						System.out.println("Autobuilder");
					}
				}
			}
			
			for(String name: member){
				for(Build b: builds){
					if(b.getBuilder() != null){
						if(b.getBuilder().equals(name)){
							if(b.getColor().equals("red")){
								rot++;
							}
							else if(b.getColor().equals("blue")){
								blau++;
							}
						}
					}
					else{
						if(b.getColor().equals("red")){
							rot++;
						}
						else if(b.getColor().equals("blue")){
							blau++;
						}
					}
				}
				overview = new Overview(name, blau, rot);
				overviews.add(overview);
				
				rot = 0;
				blau = 0;
			}
		}
		else System.out.println("Builds ist null");
		return overviews;		
	}
	
	/** loescht benanntes Projekt aus dem DS 
	 * @return true, wenn erfolgreich geloescht
	 */
	public boolean deleteJob(String job){
		boolean res = false;
		
		pm = new PMF().get().getPersistenceManager();
		tx = pm.currentTransaction();
	    Query query = pm.newQuery(Project.class);
	    query.setOrdering("name asc");
	    
	    try{
	    	List<Project> projects = (List<Project>) query.execute();
	    	if(!projects.isEmpty()){
	    		for(Project p: projects){
	    			if(p.getName().equals(job)){
	    				tx.begin();
	    				pm.deletePersistent(p);
	    				tx.commit();
	    			}
		    	}
	    		res = false;
	    	}
		    else{ 
		    	System.out.println("keine Projekte mit Name: " + job + " vorhanden");
		    	res = true;
		    }
	    }
	    finally{
	    	if(tx.isActive()){
	    		tx.rollback();
	    	}
	    	pm.close();
	    }
	    return res;
	}
	
	/** löscht alle Projekte aus dem DS */
	public void deleteAllJobs(){
		pm = new PMF().get().getPersistenceManager();
		tx = pm.currentTransaction();
	    Query query = pm.newQuery(Project.class);
	    query.setOrdering("name asc");
	    
	    try{
	    	List<Project> projects = (List<Project>) query.execute();
	    	if(!projects.isEmpty()){
	    		for(Project p: projects){
	    			tx.begin();
	    			pm.deletePersistent(p);
	    			tx.commit();
		    	}
	    	}
		    else{ 
		    	System.out.println("keine Projekte vorhanden");
		    }
	    }
	    finally{
	    	if(tx.isActive()){
	    		tx.rollback();
	    	}
	    	pm.close();
	    }
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
}
