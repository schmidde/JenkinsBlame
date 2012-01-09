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

    @BeforeClass
    public static void setUpBeforeClass(){
    	jbs = new JenkinsBlameStatsServlet("http://jenkins.rambow.it:8080", "JenkinsBlame");
    }
    @Before
    public void setUp() {
        helper.setUp();
        Build b1, b2;
        b1 = new Build(1234567, 1, "blue", "Dennis Schmidt");
        b2 = new Build(1234568, 2, "red", "Dennis Schmidt");
        List<Build> bl = new ArrayList();
        Project p = new Project(jbs.getJobName());
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

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void getBuildsByNameTest() {
   
    	List<Build> builds = null;
    	builds = jbs.getBuildsByName(jbs.getJobName());
    	assertTrue(!builds.isEmpty());
    }
    @Test
    public void hasJobTest() {
    	
    	assertTrue(jbs.hasJob(jbs.getJobName()));
    }

   
}