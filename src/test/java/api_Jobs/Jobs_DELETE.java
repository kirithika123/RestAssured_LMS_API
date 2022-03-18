package api_Jobs;

import static io.restassured.RestAssured.given;
import static util.UtilConstants.*;


import java.io.IOException;
import java.util.Properties;

import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import util.ExcelUtil;
import util.PropertyFileReader;

public class Jobs_DELETE {

	Properties prop;
	 String path;
	 
	 public Jobs_DELETE() throws Exception {
	        prop = PropertyFileReader.readPropertiesFile(CONST_API_JOBS);
	        
	    }
	 @DataProvider(name = "DeleteData")
	 public Object[][] DeleteData() throws IOException {
		    Reporter.log("Inside DataProvider");
		    String excelpath = prop.getProperty(CONST_EXCELFILE_JOBS);
	        int rowCount = ExcelUtil.getRowCount(excelpath,CONST_JOBSDELSHEET);
	        
	        int colCount = ExcelUtil.getCellCount(excelpath,CONST_JOBSDELSHEET,rowCount);
	        
	        String getpostData[][] = new String[rowCount][colCount];
	        
	        for (int i=1;i<=rowCount;i++){
	            for(int j=0;j<colCount;j++) {
	                getpostData[i-1][j]=ExcelUtil.getCellData(excelpath,CONST_JOBSDELSHEET,i,j);
	            }
	        }
	        return getpostData;
	    }

	 @Test(dataProvider = "DeleteData")
	 public void deleteJob(String Job_Id,String status_Code_expected) throws JsonMappingException, JsonProcessingException {
		RestAssured.baseURI=prop.getProperty(CONST_URL);
		RestAssured.basePath=prop.getProperty(CONST_PATH);

        Reporter.log("Inside DeleteData Test Class");
       
       
    	Response response= given().queryParam(JOB_ID, Job_Id).
    			when().delete(RestAssured.baseURI+RestAssured.basePath);	   
        
        Reporter.log("Response body string" + response.getBody().asPrettyString());
        String responsebody = response.getBody().asPrettyString();

	 }
}
