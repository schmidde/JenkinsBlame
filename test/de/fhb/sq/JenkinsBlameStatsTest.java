package de.fhb.sq;

import static org.junit.Assert.*;

import org.junit.*;

public class JenkinsBlameStatsTest {
	
	private static JenkinsBlameStats jbs;
	
	@BeforeClass
	public static void setUp(){
		jbs = new JenkinsBlameStats("http://jenkins.rambow.it:8080", "JenkinsBlame");
	}
	@Test
	public void testIsAdress(){
		
		assertTrue("Erwarte true", jbs.isAdress("http://jenkins.rambow.it:8080"));
		assertTrue("Erwarte true", jbs.isAdress("http://irgend.etwas.com:1234"));
		assertFalse("Erwarte false", jbs.isAdress("jenkins.rambow.it:8080"));
		assertFalse("Erwarte false", jbs.isAdress("http:/jenkins.rambow.it:8080"));
		assertFalse("Erwarte false", jbs.isAdress("http://jenkins.rambowit:8080"));
	}
}
