package de.fhb.sq;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  JenkinsJsonParserTest.class,
  JenkinsBlameStatsServletTest.class,
  JenkinsBlameStatsServletIntegrationTest.class
  
})

public class TestSuit {

}
