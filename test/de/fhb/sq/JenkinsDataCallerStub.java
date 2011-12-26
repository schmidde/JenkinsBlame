package de.fhb.sq;

import java.io.IOException;
import org.json.JSONObject;
import org.json.JSONException;

public class JenkinsDataCallerStub implements JenkinsDataCallerInterface{
	private String jsonText; //"{\"actions\":[{\"causes\":[{\"shortDescription\":\"Started by user Andy Klay\",\"userId\":\"klay\",\"userName\":\"Andy Klay\"}]},{\"buildsByBranchName\":{\"origin/master\":{\"buildNumber\":21,\"buildResult\":null,\"revision\":{\"SHA1\":\"29be068792cf1c6ad442e016731279131425e1d4\",\"branch\":[{\"SHA1\":\"29be068792cf1c6ad442e016731279131425e1d4\",\"name\":\"origin/HEAD\"},{\"SHA1\":\"29be068792cf1c6ad442e016731279131425e1d4\",\"name\":\"origin/master\"}]}},\"origin/HEAD\":{\"buildNumber\":21,\"buildResult\":null,\"revision\":{\"SHA1\":\"29be068792cf1c6ad442e016731279131425e1d4\",\"branch\":[{\"SHA1\":\"29be068792cf1c6ad442e016731279131425e1d4\",\"name\":\"origin/HEAD\"},{\"SHA1\":\"29be068792cf1c6ad442e016731279131425e1d4\",\"name\":\"origin/master\"}]}}},\"lastBuiltRevision\":{\"SHA1\":\"29be068792cf1c6ad442e016731279131425e1d4\",\"branch\":[{\"SHA1\":\"29be068792cf1c6ad442e016731279131425e1d4\",\"name\":\"origin/HEAD\"},{\"SHA1\":\"29be068792cf1c6ad442e016731279131425e1d4\",\"name\":\"origin/master\"}]},\"scmName\":\"\"},{},{}],\"artifacts\":[],\"building\":false,\"description\":null,\"duration\":7007,\"fullDisplayName\":\"Auto-B-Day #21\",\"id\":\"2011-12-10_18-30-53\",\"keepLog\":false,\"number\":21,\"result\":\"SUCCESS\",\"timestamp\":1323541853224,\"url\":\"http://rambow.it:8080/job/Auto-B-Day/21/\",\"builtOn\":\"\",\"changeSet\":{\"items\":[],\"kind\":null},\"culprits\":[]}";
	private JSONObject json;
	private Object obj;
	
	public JenkinsDataCallerStub(String jsonString){
		this.jsonText = jsonString;
	}
	
	public JSONObject callJson(String url) throws IOException, JSONException{
		json = new JSONObject(jsonText);
		return json;
	}
	public Object callXml(String url){
		return obj;
	}

}
