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
	@Test
	public void testGetBuilder(){
		String s, t, u;
		s = jjp.getBuilder("{\"actions\":[{\"causes\":[{\"userName\":\"Andy Klay\"}]},{},{},{},{}]}");
		t = jjp.getBuilder("{}");
		u = jjp.getBuilder(5);
		assertEquals("Erwartet wird der Name Andy Klay", "Andy Klay", s);
		assertEquals("Erwartet wird null", null, t);
		assertEquals("Erwartet wird null", null, u);
		
	}
	@Test
	public void testGetLastBuilder(){
		String s, t, u;
		s = jjp.getBuilder("{\"actions\":[{\"causes\":[{\"userName\":\"Andy Klay\"}]},{},{},{},{}]}");
		t = jjp.getBuilder("{}");
		u = jjp.getBuilder(5);
		assertEquals("Erwartet wird der Name Andy Klay", "Andy Klay", s);
		assertEquals("Erwartet wird null", null, t);
		assertEquals("Erwartet wird null", null, u);
		
	}
	@Test
	public void testGetColor(){
		String s, t, u;
		s = jjp.getColor("{\"color\":\"red\"}");
		t = jjp.getColor("{\"color\":\"green\"}");
		u = jjp.getColor("");
		assertEquals("Erwartet wir red", "red", s);
		assertEquals("Erwartet wir green", "green", t);
		assertEquals("Erwartet wir null", null, u);
	}
	@Test
	public void testGetColor2(){
		int nr = 0;
		String s, t, u;
		s = jjp.getColor(nr, "{\"result\":\"SUCCESS\"}");
		t = jjp.getColor(nr, "{\"result\":\"FAILED\"}");
		u = jjp.getColor(nr, "");
		assertEquals("Erwartet wir blue", "blue", s);
		assertEquals("Erwartet wir red", "red", t);
		assertEquals("Erwartet wir null", null, u);
	}
	@Test
	public void testGetFirstBuild(){
		int i, j, k;
		i = jjp.getFirstBuild("{\"firstBuild\":{\"number\":37,\"url\":\"http://rambow.it:8080/job/Auto-B-Day/37/\"}}");
		j = jjp.getFirstBuild("{}");
		k = jjp.getFirstBuild("Irgend ein Quatsch");
		assertEquals("Erwartet wird 37", 37, i);
		assertEquals("Erwartet wird -1", -1, j);
		assertEquals("Erwartet wird -1", -1, k);
	}
	@Test
	public void testGetLastGoodBuild(){
		int i, j, k;
		i = jjp.getLastGoodBuild("{\"lastSuccessfulBuild\":{\"number\":37}}");
		j = jjp.getLastGoodBuild("{}");
		k = jjp.getLastGoodBuild("Irgend ein Quatsch");
		assertEquals("Erwartet wird 37", 37, i);
		assertEquals("Erwartet wird -1", -1, j);
		assertEquals("Erwartet wird -1", -1, k);
	}
	@Test
	public void testGetLastBadBuild(){
			int i, j, k;
			i = jjp.getLastBadBuild("{\"lastFailedBuild\":{\"number\":37}}");
			j = jjp.getLastBadBuild("{}");
			k = jjp.getLastBadBuild("Irgend ein Quatsch");
			assertEquals("Erwartet wird 37", 37, i);
			assertEquals("Erwartet wird -1", -1, j);
			assertEquals("Erwartet wird -1", -1, k);
	}
	@Test
	public void testGetTimestampBuild(){
		long l, m, n;
		l = jjp.getTimeStamp("{\"timestamp\":1323541990370}");
		m = jjp.getTimeStamp("{\"timestamp\":\"1323541990370\"}");
		n = jjp.getTimeStamp("");
		assertEquals("Erwartet wird 1323541990370", 1323541990370L, l);
		assertEquals("Erwartet wird 1323541990370", 1323541990370L, m);
		assertEquals("Erwartet wird -1", -1L, n);
	}
	@Test
	public void testGetLastTimestampBuild(){
		long l, m, n;
		l = jjp.getLastTimeStamp("{\"timestamp\":1323541990370}");
		m = jjp.getLastTimeStamp("{\"timestamp\":\"1323541990370\"}");
		n = jjp.getLastTimeStamp("");
		assertEquals("Erwartet wird 1323541990370", 1323541990370L, l);
		assertEquals("Erwartet wird 1323541990370", 1323541990370L, m);
		assertEquals("Erwartet wird -1", -1L, n);
	}
}
