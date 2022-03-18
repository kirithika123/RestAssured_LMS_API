package api_Jobs;

import static io.restassured.RestAssured.given;
import static util.UtilConstants.CONST_API_JOBS;
import static util.UtilConstants.CONST_PATH;
import static util.UtilConstants.CONST_PWD;
import static util.UtilConstants.CONST_URL;
import static util.UtilConstants.CONST_USERNAME;

import java.util.Properties;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import util.PropertyFileReader;

public class Jobs_GET {
	
	Properties prop;
	 String path;
	 
	 public Jobs_GET() throws Exception {
	        prop = PropertyFileReader.readPropertiesFile(CONST_API_JOBS);
	        
	    }
	 @Test public void getallJobs() {
		  RestAssured.baseURI=prop.getProperty(CONST_URL);
		  RestAssured.basePath=prop.getProperty(CONST_PATH); 
		  Response response =given().get(RestAssured.baseURI+RestAssured.basePath);
		  
		  
		  int status_code = response.statusCode();
		  Reporter.log(response.asPrettyString());
		  Assert.assertEquals(status_code,200);
		  Reporter.log("All Jobs are retreived successfully");
		  
		  
		  }

}
