package com.example.demo;

import org.json.JSONException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(DemoApplication.class, args);
	  RestApi jira=new RestApi();
//	  System.out.println(jira.changeStatus("10038","In Progress"));
//	  System.out.println(jira.getAssigneeId("akmupunch01"));
  jira.createIssue("VI","Jira Issue created using Spring Boot now","akmupunch01","Jira Rest Api Ticket","Bug");
// System.out.println(jira.getIssue("10014").getBody());
////	  System.out.println(jira.deleteIssue("10012").getBody());
////
//		MyJiraClient jiraClient=new MyJiraClient(jira.username,jira.password,jira.jiraBaseURL);
//		jiraClient.createIssue("VIS", 1L, "Issue created from client");
	}

}
