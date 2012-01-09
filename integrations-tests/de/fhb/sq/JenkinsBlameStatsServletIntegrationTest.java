package de.fhb.sq;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class JenkinsBlameStatsServletIntegrationTest {

	private PersistenceManager pm;
	private Transaction tx;
	private static JenkinsBlameStatsServlet jbs;
    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    
    public void initDS(String projectName, Build b1, Build b2){
    
        List<Build> bl = new ArrayList();
        Project p = new Project(projectName);
        bl.add(b1); bl.add(b2);
        p.setBuilds(bl);
        p.setLastFailedBuild(2);
        p.setLastSuccessfulBuild(1);
        try {
			pm = new PMF().get().getPersistenceManager();
			tx = pm.currentTransaction();
	        tx.begin();
            pm.makePersistent(p);
            tx.commit();
        } finally {
        	if(tx.isActive()){
        		tx.rollback();
        	}
            pm.close();
        }
    }
    @BeforeClass
    public static void setUpBeforeClass(){
    	jbs = new JenkinsBlameStatsServlet("http://jenkins.rambow.it:8080", "JenkinsBlame");
    }
    @Before
    public void setUp() {
        helper.setUp();    
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }
    
    @Test
    public void getBuildsByNameTest() {
    	Build b1 = new Build(1234567, 1, "blue", "Dennis Schmidt");
    	Build b2 = new Build(1234568, 2, "red", "Dennis Schmidt");
    	
    	initDS(jbs.getJobName(), b1, b2);
    	
    	List<Build> builds = null;
    	builds = jbs.getBuildsByName(jbs.getJobName());
    	assertTrue(!builds.isEmpty());
    	assertEquals("blue", builds.get(0).getColor());
    	assertEquals("red", builds.get(1).getColor());
    }
    @Test
    public void hasJobTest() {
    	
    	Build b1 = new Build(1234567, 1, "blue", "Dennis Schmidt");
    	Build b2 = new Build(1234568, 2, "red", "Dennis Schmidt");
    	
    	initDS(jbs.getJobName(), b1, b2);
    	
    	assertTrue(jbs.hasJob(jbs.getJobName()));
    	assertFalse(jbs.hasJob(""));
    }
    @Test
    public void isNewTest(){
    	
    	Build b1 = new Build(1234567, 1, "blue", "Dennis Schmidt");
    	Build b2 = new Build(1234568, 2, "red", "Dennis Schmidt");
    	
    	initDS(jbs.getJobName(), b1, b2);
    	
    	assertTrue(jbs.isNew(10));
    	assertFalse(jbs.isNew(1));
    	assertFalse(jbs.isNew(0));
    }
    @Test
    public void checkColorTest(){
    	
    	Build b1 = new Build(1234567, 1, "blue", "Dennis Schmidt");
    	Build b2 = new Build(1234568, 2, "red", "Dennis Schmidt");
    	initDS(jbs.getJobName(), b1, b2);
    	assertEquals("destroyed", jbs.checkColor());    	
    }
    @Test
    public void checkColorTest2(){
    	
    	Build b1 = new Build(1234567, 1, "blue", "Dennis Schmidt");
    	Build b2 = new Build(1234568, 2, "blue", "Dennis Schmidt");
    	initDS(jbs.getJobName(), b1, b2);
    	assertEquals("successful", jbs.checkColor());
    	
    }
    @Test
    public void checkColorTest3(){
    	
    	Build b1 = new Build(1234567, 1, "red", "Dennis Schmidt");
    	Build b2 = new Build(1234568, 2, "red", "Dennis Schmidt");
    	initDS(jbs.getJobName(), b1, b2);
    	assertEquals("destroyed", jbs.checkColor());
    	
    }
    @Test
    public void checkColorTest4(){
    	
    	Build b1 = new Build(1234567, 1, "blue", "Dennis Schmidt");
    	Build b2 = new Build(1234568, 2, "red", "Dennis Schmidt");
    	initDS(jbs.getJobName(), b1, b2);
    	assertEquals("destroyed", jbs.checkColor());
    	
    }
    
   
}