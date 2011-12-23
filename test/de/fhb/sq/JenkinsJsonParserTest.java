package de.fhb.sq;

import static org.junit.Assert.*;

import java.io.IOException;

import org.json.JSONException;
import org.junit.*;

public class JenkinsJsonParserTest {
	private static JenkinsJsonParserInterface jjp;
	@BeforeClass
	public static void setUp(){
		jjp = new JenkinsJsonParserStub("server", "job");
	}
	
	
	@Test
	public void testGetLastBuildNr() {
		int i, j, k = 0;
		i = jjp.getLastBuildNr("{\"builds\":[{\"number\":22},{\"number\":21},{\"number\":20}]}");
		j = jjp.getLastBuildNr("{}");
		k = jjp.getLastBuildNr("");
		assertEquals("letzte Nr. sollte 22 sein", 22, i);
		assertEquals("letzte Nr. sollte -1 sein", -1, j);
		assertEquals("letzte Nr. sollte -1 sein", -1, k);
	}

}
