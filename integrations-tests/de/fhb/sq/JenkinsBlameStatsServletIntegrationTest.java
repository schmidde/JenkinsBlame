package de.fhb.sq;

import static org.junit.Assert.*;

import org.junit.*;

public class JenkinsBlameStatsServletIntegrationTest {

	private static JenkinsBlameStatsServlet jbs;
	@BeforeClass
	public static void setUp(){
		jbs = new JenkinsBlameStatsServlet("http://jenkins.rambow.it:8080", "JenkinsBlame");
		
	}
	@Test
	public void hasJobTest() {
		if(jbs.hasJob(jbs.getJobName())){
			assertTrue(jbs.hasJob(jbs.getJobName()));
		}
		else{
			assertFalse(jbs.hasJob(jbs.getJobName()));
		}
	}

}
