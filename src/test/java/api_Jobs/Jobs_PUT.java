package api_Jobs;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.hamcrest.Matchers;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertThat;

import io.restassured.response.Response;
import util.ExcelUtil;
import util.PropertyFileReader;
import static util.UtilConstants.*;

public class Jobs_PUT {
	
	 Properties prop;
	 String path;
	 
	 public Jobs_PUT() throws Exception {
	        prop = PropertyFileReader.readPropertiesFile(CONST_API_JOBS);
	        
	    }
	 
	 @DataProvider(name = "PutData")
	 public Object[][] getPutData() throws IOException {
		    Reporter.log("Inside DataProvider");
		    String excelpath = prop.getProperty(CONST_EXCELFILE_JOBS);
	        int rowCount = ExcelUtil.getRowCount(excelpath,CONST_JOBSPUTSHEET);
	        int colCount = ExcelUtil.getCellCount(excelpath,CONST_JOBSPUTSHEET,rowCount);
	      
	        String getpostData[][] = new String[rowCount][colCount];
	        
	        for (int i=1;i<=rowCount;i++){
	            for(int j=0;j<colCount;j++) {
	                getpostData[i-1][j]=ExcelUtil.getCellData(excelpath,CONST_JOBSPUTSHEET,i,j);
	            }
	        }
	        return getpostData;
	    }
	 
	 
	@Test(dataProvider = "PutData")
	 public void updateJob(String Job_Title,String Job_Company_Name,String Job_Location,
			 String Job_Type,String Job_Posted_time,String Job_Id,String status_Code_expected) throws JsonMappingException, JsonProcessingException {
		RestAssured.baseURI=prop.getProperty(CONST_URL);
		RestAssured.basePath=prop.getProperty(CONST_PATH);

        Reporter.log("Inside getPutData Test Class");
       
       
    	Response response= given().queryParam(JOB_TITLE, Job_Title).queryParam(JOB_COMPANY_NAME, Job_Company_Name).
    			queryParam(JOB_LOCATION,Job_Location).queryParam(JOB_TYPE,Job_Type).
    			queryParam(JOB_POSTED_TIME, Job_Posted_time).queryParam(JOB_ID, Job_Id).
    			when().put(RestAssured.baseURI+RestAssured.basePath);	   
        
        Reporter.log("Response body string" + response.getBody().asPrettyString());
        String responsebody = response.getBody().asPrettyString();

     	
        if (status_Code_expected.equals(Success_Status)){
            if (Job_Id!=null) {
                Assert.assertEquals(responsebody.contains(Job_Id),true,"Id");
            }
            if (Job_Title!=null) {
                Assert.assertEquals(responsebody.contains(Job_Title),true,"Title");
            }
            if (Job_Company_Name!=null) {
                Assert.assertEquals(responsebody.contains(Job_Company_Name),true,"Name");
            }
            if (Job_Location!=null) {
                Assert.assertEquals(responsebody.contains(Job_Location),true,"Loc");
            }
            if (Job_Type!=null) {
                Assert.assertEquals(responsebody.contains(Job_Type),true,"Type");
            }
            if (Job_Posted_time!=null) {
                Assert.assertEquals(responsebody.contains(Job_Posted_time),true,"Time");
            }

        }
     	
     	
      String result = response.asPrettyString();
      result = result.replace("NaN", "null");
     
       
     
      assertThat("Schema Validation Failed",result, JsonSchemaValidator.matchesJsonSchemaInClasspath("JSON_schema_JOBS/JOBS_PUT_schema.json"));
      Assert.assertEquals(response.getStatusCode(),Integer.parseInt(status_Code_expected)); 
        
	 }
  	
 

}
	
	 


