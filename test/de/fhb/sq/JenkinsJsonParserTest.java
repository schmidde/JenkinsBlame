package de.fhb.sq;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.junit.*;

public class JenkinsJsonParserTest {
	private static JenkinsJsonParserAbstract jjp;
	@BeforeClass
	public static void setUp(){
		jjp = new JenkinsJsonParserStub("server", "job");
	}
	
	
	@Test
	public void testGetLastBuildNr() {
		int i, j, k;
		i = jjp.getLastBuildNr("{\"builds\":[{\"number\":22},{\"number\":21},{\"number\":20}]}");
		j = jjp.getLastBuildNr("{}");
		k = jjp.getLastBuildNr("");
		assertEquals("letzte Nr. sollte 22 sein", 22, i);
		assertEquals("letzte Nr. sollte -1 sein", -1, j);
		assertEquals("letzte Nr. sollte -1 sein", -1, k);
	}
	@Test
	public void testGetBuilds(){
		int i, j, k;
		i  = (Integer)jjp.getBuilds("{\"actions\":[{},{},{},{}],\"description\":\"\",\"displayName\":\"Auto-B-Day\",\"name\":\"Auto-B-Day\",\"url\":\"http://rambow.it:8080/job/Auto-B-Day/\",\"buildable\":true,\"builds\":[{\"number\":22,\"url\":\"http://rambow.it:8080/job/Auto-B-Day/22/\"},{\"number\":21,\"url\":\"http://rambow.it:8080/job/Auto-B-Day/21/\"},{\"number\":20,\"url\":\"http://rambow.it:8080/job/Auto-B-Day/20/\"}]}").get(0);
		j  = (Integer)jjp.getBuilds("{}").get(0);
		k  = (Integer)jjp.getBuilds("").get(0);
		assertEquals("Build soll 22 sein", 22, i);
		assertEquals("Build soll -1 sein", -1, j);
		assertEquals("Build soll -1 sein", -1, k);
	}
}
