package api_LMS;

import java.io.IOException;
import java.util.Properties;

import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import util.ExcelUtil;
import util.PropertyFileReader;
import static util.UtilConstants.*;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;

import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
//import io.restassured.module.jsv.JsonSchemaValidator;
public class LMS_GET {
	
	public static ValidatableResponse _resp;
	Properties prop;
    public LMS_GET() throws Exception {
        prop = PropertyFileReader.readPropertiesFile(CONST_API_LMS);
       

    }
    @DataProvider
    public Object[][] getData() throws IOException {
        String path = prop.getProperty(CONST_EXCELFILEPATH);
        int rowCount = ExcelUtil.getRowCount(path,CONST_LMSGETSHEET);
        int colCount = ExcelUtil.getCellCount(path,CONST_LMSGETSHEET,rowCount);
        String getprogData[][] = new String[rowCount][colCount];

        for (int i=1;i<=rowCount;i++){
            for(int j=0;j<colCount;j++) {
                getprogData[i-1][j]=ExcelUtil.getCellData(path,CONST_LMSGETSHEET,i,j);

            }
        }

        return getprogData;

    }

	
	  @Test public void getallPrograms() {
	  RestAssured.baseURI=prop.getProperty(CONST_URL);
	  RestAssured.basePath=prop.getProperty(CONST_PATH); Response response =
	  given().auth().basic(prop.getProperty(CONST_USERNAME),
	  prop.getProperty(CONST_PWD)).when().
	  get(RestAssured.baseURI+RestAssured.basePath);
	  
	  
	  int status_code = response.statusCode();
	  System.out.println(response.asPrettyString());
	  Assert.assertEquals(status_code,200);
	  Reporter.log("All Programs are retreived successfully");
	  
	  
	  }
	 


   @Test(dataProvider = "getData")
    public void get_program_id(String programId, String StatusCode) {
        RestAssured.baseURI=prop.getProperty(CONST_URL);
        RestAssured.basePath=prop.getProperty(CONST_PATH);
        Response response = given().auth().basic(prop.getProperty(CONST_USERNAME),prop.getProperty(CONST_PWD)).when().
                get(RestAssured.baseURI+RestAssured.basePath+ "/" + programId);
        Reporter.log("The response obtained is:" + response.asPrettyString());
        try {
        	
         Assert.assertEquals(response.statusCode(),Integer.parseInt(StatusCode));
        if(StatusCode.equals(Success_Status)) {
            int pid = response.jsonPath().get(PROG_ID);
            Assert.assertEquals(pid, Integer.parseInt(programId));
            response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(prop.getProperty(CONST_LMS_SCHEMA)));
           System.out.println(response.statusCode());
           
            Reporter.log("Program is retreived successfully for programId" + programId);

        
        }
        } catch(Exception e) {
        	System.out.println("The program ID to be retreived is:" + programId);
        }

        
    } 
	  
	  
    
}
