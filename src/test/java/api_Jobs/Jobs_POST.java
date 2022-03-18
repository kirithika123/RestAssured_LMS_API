package api_Jobs;

import static util.UtilConstants.*;

import static io.restassured.RestAssured.given;
import io.restassured.module.jsv.JsonSchemaValidator;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
//import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import util.ExcelUtil;

import util.PropertyFileReader;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.module.jsv.JsonSchemaValidatorSettings;

public class Jobs_POST{

	Properties prop;
	 String path;
	 
	 public Jobs_POST() throws Exception {
	        prop = PropertyFileReader.readPropertiesFile(CONST_API_JOBS);
	        
	    }
	 
	 @DataProvider(name = "PostJobsData")
	 public Object[][] getpostData() throws IOException {
		 
		    String excelpath = prop.getProperty(CONST_EXCELFILE_JOBS);
	        int rowCount = ExcelUtil.getRowCount(excelpath,CONST_JOBSPOSTSHEET);
	        int colCount = ExcelUtil.getCellCount(excelpath,CONST_JOBSPOSTSHEET,rowCount);
	        String getpostData[][] = new String[rowCount][colCount];
	        
	        for (int i=1;i<=rowCount;i++){
	            for(int j=0;j<colCount;j++) {
	                getpostData[i-1][j]=ExcelUtil.getCellData(excelpath,CONST_JOBSPOSTSHEET,i,j);
	            }
	        }
	        return getpostData;
	    }
	 
	 @Test(dataProvider = "PostJobsData")
	 public void createJob(String Job_Title,String Job_Company_Name,String Job_Location,
			 String Job_Type,String Job_Posted_time,String Job_Description,String Job_Id,String status_Code_expected) throws JsonMappingException, JsonProcessingException {
		 
		 RestAssured.baseURI=prop.getProperty(CONST_URL);
         RestAssured.basePath=prop.getProperty(CONST_PATH);
         
     	 Response response= given().queryParam(JOB_TITLE, Job_Title).queryParam(JOB_COMPANY_NAME, Job_Company_Name).
    			queryParam(JOB_LOCATION,Job_Location).queryParam(JOB_TYPE,Job_Type).
    			queryParam(JOB_POSTED_TIME, Job_Posted_time).queryParam(JOB_DESCRIPTION,Job_Description).
    			queryParam(JOB_ID, Job_Id).
    			when().post(RestAssured.baseURI+RestAssured.basePath);	
    
    
     String responseBody = response.getBody().asPrettyString();
     	System.out.println("The Response Body is :"+responseBody);
        
     	ObjectMapper mapper = new ObjectMapper();     	
     	mapper.enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS.mappedFeature());
     	     	
     	
     	// HashMap<String, String> obj = mapper.readValue(responseBody, HashMap.class); 
     	
        // Convert the json string into  LinkedHashmap object with 3 level depth 
     	// ( since Json has 3 level hierarchy  like  "data" -> "Job Title" -> "0") 
     	
     	//  1st HashMap    			 Key ->data ,  			Value ->  dataMap
     	//  2nd HashMap dataMap  	 Key ->  Job Title,     Value -> jobTitleMap
     	//  3rd HashMap jobTitleMap   Key -> pos,  			Value -> "Sample Job Title"
     	LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String,String>>> map =
     			(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String,String>>>) mapper.readValue(responseBody, Map.class); 
     	
     	String exp_jobTitle = null;
     	String exp_jobCompanyName = null;
     	String exp_jobLocation=null;
     	String exp_jobType=null;
     	String exp_jobPostedTime=null;
     	String exp_jobDescription=null;
     	String exp_jobId=null;
     	
     	LinkedHashMap<String, LinkedHashMap<String,String>> dataMap = (LinkedHashMap<String, LinkedHashMap<String,String>>) map.get("data"); 
     	
     	
     	if (dataMap != null) {
     		
     		LinkedHashMap<String, String> jobTitleMap = (LinkedHashMap<String,String>) dataMap.get(JOB_TITLE);     		
     		String pos = jobTitleMap.size() - 1  + ""; 
     		exp_jobTitle = jobTitleMap.get(pos);     		
     		Reporter.log("The job title Created : " + exp_jobTitle);
     		Assert.assertEquals(Job_Title, exp_jobTitle);
     		
     		LinkedHashMap<String, String> jobCompanyNameMap = (LinkedHashMap<String,String>) dataMap.get(JOB_COMPANY_NAME);
     		pos = jobCompanyNameMap.size() - 1  + ""; 
     		exp_jobCompanyName = jobCompanyNameMap.get(pos);
     		Reporter.log("The job CompanyName Created : " +exp_jobCompanyName);
     		Assert.assertEquals(Job_Company_Name, exp_jobCompanyName);
     		
     		LinkedHashMap<String, String> jobLocationMap = (LinkedHashMap<String,String>) dataMap.get(JOB_LOCATION);
     		pos = jobLocationMap.size() - 1  + ""; 
     		exp_jobLocation = jobLocationMap.get(pos);
     		Reporter.log("The job Location Created : " +exp_jobLocation);
     		Assert.assertEquals(Job_Location, exp_jobLocation);
     		
     		LinkedHashMap<String, String> jobTypeMap = (LinkedHashMap<String,String>) dataMap.get(JOB_TYPE);
     		pos = jobTypeMap.size() - 1  + ""; 
     		exp_jobType = jobTypeMap.get(pos);
     		Reporter.log("The Job Type Created : " +exp_jobType);
     		Assert.assertEquals(Job_Type, exp_jobType);
     		
     		LinkedHashMap<String, String> jobPostedTimeMap = (LinkedHashMap<String,String>) dataMap.get(JOB_POSTED_TIME);
     		pos = jobPostedTimeMap.size() - 1  + ""; 
     		exp_jobPostedTime = jobPostedTimeMap.get(pos);
     		Reporter.log("The Job Posted time Created : " +exp_jobPostedTime);
     		Assert.assertEquals(Job_Posted_time, exp_jobPostedTime);
     		
     		LinkedHashMap<String, String> jobDescriptionMap = (LinkedHashMap<String,String>) dataMap.get(JOB_DESCRIPTION);
     		pos = jobDescriptionMap.size() - 1  + ""; 
     		exp_jobDescription = jobDescriptionMap.get(pos);
     		Reporter.log("The job Description Created : " +exp_jobDescription);
     		Assert.assertEquals(Job_Description, exp_jobDescription);
     		
     		LinkedHashMap<String, String> jobIdMap = (LinkedHashMap<String,String>) dataMap.get(JOB_ID);
     		pos = jobIdMap.size() - 1  + ""; 
     		exp_jobId = jobIdMap.get(pos);
     		Reporter.log("The job Id Created : " +exp_jobId);
     		
     	}
     	System.out.println("The input job title:" + Job_Title);
     	//System.out.println("The output job title:" + response.jsonPath().get(exp_jobTitle));
     	//Assert.assertEquals(Job_Title,response.jsonPath().get(exp_jobTitle));
      String result = response.asPrettyString();
      result = result.replace("NaN", "null");
         
    
     assertThat("Schema Validation Failed",result, JsonSchemaValidator.matchesJsonSchemaInClasspath("JSON_schema_JOBS/JOBS_POST_schema.json"));
     
        
	 }
	 
	 }
	 
	 

