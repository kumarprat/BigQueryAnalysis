package com.bigQuery.BigQueryAnalysis;

import java.text.MessageFormat;

import com.google.api.gax.paging.Page;
import com.google.cloud.bigquery.Job;
public class TestClass {

	static String projectId = "gleaming-bus-218317";
	public static void main(String[] args) {
		try{
			System.out.println("Method execution starts");
			//creating BigQueryHelper instance with proectId.
			BigQueryHelper queryHelper = new BigQueryHelper(projectId);
			//BigQueryHelper.createJob(service);
			
			//Listing all jobs of project
			Page<Job> jobPage = queryHelper.listJobs();
			for(Job job: jobPage.getValues()){
				String msg = MessageFormat.format("    Job : {0}, Status : {1}", job.getJobId().getJob(), job.getStatus().getState());
				System.out.println(msg);
				//To cancel job based on some condition
				//System.out.println(queryHelper.cancelJob(job));
			}
			System.out.println("Method execution done");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
