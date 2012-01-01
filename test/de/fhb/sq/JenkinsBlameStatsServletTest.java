package de.fhb.sq;

import static org.junit.Assert.*;

import org.junit.*;

public class JenkinsBlameStatsServletTest {
	
	private static JenkinsBlameStatsServlet jbs;
	
	@BeforeClass
	public static void setUp(){
		jbs = new JenkinsBlameStatsServlet("http://jenkins.rambow.it:8080", "JenkinsBlame");
	}
	@Test
	public void testIsAdress(){
		
		assertTrue("Erwarte true", jbs.isAdress("http://jenkins.rambow.it:8080"));
	}
}
