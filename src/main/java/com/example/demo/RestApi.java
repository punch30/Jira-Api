package com.example.demo;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import java.util.*;

@Data

@RequiredArgsConstructor
public class RestApi {

    public static final String username = "gvardhan2001@gmail.com";
    public static final String password = "Tj6wuobUEI0JEbM9aMSr8090";
    public static final String jiraBaseURL = "https://vishnuvardhan30.atlassian.net/rest/api/latest/";

    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;

    public RestApi() {

        restTemplate = new RestTemplate();
        httpHeaders = createHeaders();

    }

    public ResponseEntity getIssue(String issueId) {

        return callJiraApi(issueId, HttpMethod.GET);

    }

    public ResponseEntity deleteIssue(String issueId) {

        return callJiraApi(issueId, HttpMethod.DELETE);

    }

    public String getAssigneeId(String name) throws JSONException {
        String url = jiraBaseURL + "user/search?query=username=" +name;
        ResponseEntity assigneeData=callJiraApi(url,HttpMethod.GET);

        String data= (String) assigneeData.getBody();

        JSONArray jsonArray = new JSONArray(data);

        for (int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            if(jsonObj.getString("displayName").equals(name))
            {
//                System.out.println(jsonObj.getString("accountId"));
                return jsonObj.getString("accountId");

            }
        }
      return "";


    }

    public void createIssue(String key, String summary,String assignee, String desc, String issueType) throws Exception {

        String inputData = createInputData(key, summary, assignee,desc, issueType);
        String url = jiraBaseURL + "issue";
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<String>(inputData, httpHeaders);
        restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    }


    private ResponseEntity callJiraApi(String url, org.springframework.http.HttpMethod method) {
        HttpEntity<?> requestEntity = new HttpEntity(httpHeaders);
        return restTemplate.exchange(url, method, requestEntity, String.class);

    }

    private String getId(String data,String status) throws JSONException {

        String []data1=data.split("\\[");

        String output="["+data1[1].substring(0,data1[1].length()-1);
//        System.out.println(output);

        JSONArray jsonArray = new JSONArray(output);
//        System.out.println(jsonArray);
        for (int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            System.out.println(jsonObj);
            if(jsonObj.getString("name").equals(status))
            {
//                System.out.println(jsonObj.getString("accountId"));
                return jsonObj.getString("id");

            }
        }

        return "";
    }


    public String changeStatus(String issueId,String status) throws JSONException {
        String url=jiraBaseURL + "issue/"+issueId+"/transitions?expand=expand.fields";
        System.out.println(url);
        ResponseEntity assigneeData=callJiraApi(url,HttpMethod.GET);
        String data= (String) assigneeData.getBody();
        String id=getId(data,status);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("transition",new JSONObject().put("id",id));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<String>(jsonObject.toString(), httpHeaders);
         restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);


        return "";

    }



    private HttpHeaders createHeaders() {

        String Credentials = username + ":" + password;
        byte[] base64Credentials = Base64.getEncoder().encode(Credentials.getBytes());
        String base64Cred = new String(base64Credentials);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Cred);
        return headers;

    }

    private void addElement(JSONObject jsonObject,String key,Object obj) throws JSONException {
        jsonObject.put(key,obj);
    }


    private String createInputData(String key, String summary, String assignee,String description, String issueType) throws JSONException {

//        String createJSON = "{\"fields\":{\"project\":{\"key\":\"$KEY\"},\"summary\":\"$SUMMARY\",\"description\":\"$DESCRIPTION\",\"issuetype\": {\"name\": \"$ISSUETYPE\"}}}";
        JSONObject jsonObject=new JSONObject();
        addElement(jsonObject,"project",new JSONObject().put("key",key));
        addElement(jsonObject,"summary", summary);
        addElement(jsonObject,"description",description);
        addElement(jsonObject,"issuetype", new JSONObject().put("name",issueType));
        addElement(jsonObject,"assignee", new JSONObject().put("accountId",getAssigneeId(assignee)));

        JSONObject json=new JSONObject();
        addElement(json,"fields", jsonObject);
        return json.toString();

    }

}
