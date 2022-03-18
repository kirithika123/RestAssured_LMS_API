package api_LMS;

import java.io.IOException;
import java.util.Properties;

import org.hamcrest.Matchers;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import util.ExcelUtil;
import static util.UtilConstants.*;
import util.PropertyFileReader;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;

import static io.restassured.RestAssured.given;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;


public class LMS_POST {
	
	 public static ValidatableResponse _resp;	
	 Properties prop;
	    String path;
	    int RowNum=0;
	    public LMS_POST() throws Exception {
	        prop = PropertyFileReader.readPropertiesFile(CONST_API_LMS);
	        path = prop.getProperty(CONST_EXCELFILEPATH);

	    }

	    @DataProvider
	    public Object[][] getData() throws IOException {
	        int rowCount = ExcelUtil.getRowCount(path,CONST_LMSPOSTSHEET);
	        int colCount = ExcelUtil.getCellCount(path,CONST_LMSPOSTSHEET,rowCount);
	        String getpostData[][] = new String[rowCount][colCount];
	        for (int i=1;i<=rowCount;i++){
	            for(int j=0;j<colCount;j++) {
	                getpostData[i-1][j]=ExcelUtil.getCellData(path,CONST_LMSPOSTSHEET,i,j);
	            }
	        }

	        return getpostData;

	    }


	    @SuppressWarnings("unchecked")
		@Test(dataProvider = "getData")
	    public void Post_Method(String progName,String programDescription,String online,String Status_code_expected) throws IOException {
	        RestAssured.baseURI=prop.getProperty(CONST_URL);
	        RestAssured.basePath=prop.getProperty(CONST_PATH);
	        
	        //JSON object to input the request body
	        JSONObject request = new JSONObject();
	        
	        request.put(PROG_Name,progName);
	        request.put(PROG_desc,programDescription);
	        request.put(Online_Status,Boolean.valueOf(online));
	       
	        Response response = given().auth().preemptive().
	        		basic(prop.getProperty(CONST_USERNAME), prop.getProperty(CONST_PWD)).
	        		header("Content-Type", "application/json").body(request).when().post(RestAssured.baseURI+RestAssured.basePath);
	  
	        System.out.println(response.getBody().asString());
	       
	        
	        Assert.assertEquals(Integer.parseInt(Status_code_expected),response.getStatusCode());

	       
	       
	       
	        Reporter.log("Verify if the program name received from request and response are same:"  );
	        try {
	        //Validating input with output
	        if (Status_code_expected.equals(Success_Status)){
	        	Assert.assertEquals(progName,response.jsonPath().get(PROG_Name),"name");
	        	Assert.assertEquals(programDescription,response.jsonPath().get(PROG_desc),"desc");
	        	Assert.assertEquals(Boolean.valueOf(online),response.jsonPath().get(Online_Status),"online");
	        	response.then().assertThat().body(Matchers.notNullValue()).
	 	        body(JsonSchemaValidator.matchesJsonSchemaInClasspath(prop.getProperty(CONST_LMS_SCHEMA)));
	        	Reporter.log("Verify if the program id is same from request and response: " + response.jsonPath().get(PROG_Name) + progName);
	            Reporter.log("Verify if the program desc is same for request and response :" + response.jsonPath().get(PROG_desc) + programDescription);
	            Reporter.log("Verify if the online status is same for request and response:" + response.jsonPath().get(Online_Status) + online );        
	        }
	        }catch(Exception e) {
	        	e.printStackTrace();
	        }
	            
	            given().auth().preemptive().basic(prop.getProperty(CONST_USERNAME),prop.getProperty(CONST_PWD)).when().
	                    delete(RestAssured.baseURI+RestAssured.basePath+"/" +response.jsonPath().get(PROG_ID));
	            
	        }

	    }



