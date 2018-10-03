package com.bigQuery.BigQueryAnalysis;

import java.io.InputStream;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.Field;
import com.google.cloud.bigquery.FormatOptions;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.JobInfo;
import com.google.cloud.bigquery.LegacySQLTypeName;
import com.google.cloud.bigquery.LoadJobConfiguration;
import com.google.cloud.bigquery.Schema;
import com.google.cloud.bigquery.TableId;
import com.google.cloud.bigquery.JobInfo.CreateDisposition;

public class BigQueryHelper {
	private final String credentialsFilePath = "credentials.json";
	
	private BigQuery bigQuery;
	
	public BigQueryHelper (String projectId) throws Exception{
		this.bigQuery = getBigQueryInstance(projectId);
	}
	
	/**
	 * This method creates BigQuery instance related to projectId
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	public BigQuery getBigQueryInstance(String projectId) throws Exception{
		if(null == projectId || projectId.trim().equals(""))
			throw new Exception("Project is must required to have BigQueryInstance");
		
		InputStream inputStream = BigQueryHelper.class.getClassLoader().getResourceAsStream(credentialsFilePath);
		return BigQueryOptions.newBuilder().
				setCredentials(ServiceAccountCredentials.fromStream(inputStream)).
				setProjectId(projectId).
				build().getService();
	}
	
	/**
	 * This method will return all jobs data.
	 * @return
	 */
	public Page<Job> listJobs(){
		return bigQuery.listJobs(BigQuery.JobListOption.allUsers());
	}
	
	/**
	 * This method is used to cancel a job if requires.
	 * @param job
	 * @return
	 */
	public boolean cancelJob(Job job){
		return bigQuery.cancel(job.getJobId().getJob());
	}
	
	/**
	 * This method is a dummy which is used to create a test job.
	 * @param service
	 * @throws Exception
	 */
	public void createJob(BigQuery service) throws Exception{
		String datasetName = "my_dataset_name";
	    String tableName = "my_table_name";
	    String sourceUri = "gs://cloud-samples-data/bigquery/us-states/us-states.json";
	    TableId tableId = TableId.of(datasetName, tableName);
	    // Table field definition
	    Field[] fields =
	        new Field[] {
	          Field.of("name", LegacySQLTypeName.STRING),
	          Field.of("post_abbr", LegacySQLTypeName.STRING)
	        };
	    // Table schema definition
	    Schema schema = Schema.of(fields);
	    LoadJobConfiguration configuration =
	        LoadJobConfiguration.builder(tableId, sourceUri)
	            .setFormatOptions(FormatOptions.json())
	            .setCreateDisposition(CreateDisposition.CREATE_IF_NEEDED)
	            .setSchema(schema)
	            .build();
	    // Load the table
	    Job loadJob = service.create(JobInfo.of(configuration));
	    loadJob = loadJob.waitFor();
	    // Check the table
	    System.out.println("State: " + loadJob.getStatus().getState());
	    
	}
}
