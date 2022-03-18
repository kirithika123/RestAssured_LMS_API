package api_LMS;

import util.ExcelUtil;
import util.PropertyFileReader;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.hamcrest.Matchers;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Properties;

import static util.UtilConstants.*;
import static io.restassured.RestAssured.given;
public class LMS_PUT {
	
	Properties prop;
    String path;
    int RowNum=0;
    public LMS_PUT() throws Exception {
        prop = PropertyFileReader.readPropertiesFile(CONST_API_LMS);
        path = prop.getProperty(CONST_EXCELFILEPATH);

    }

    @DataProvider
    public Object[][] getData() throws IOException {
        int rowCount = ExcelUtil.getRowCount(path,CONST_LMSPUTSHEET);
        int colCount = ExcelUtil.getCellCount(path,CONST_LMSPUTSHEET,rowCount);
        String getputData[][] = new String[rowCount][colCount];
        for (int i=1;i<=rowCount;i++){
            for(int j=0;j<colCount;j++) {
                getputData[i-1][j]=ExcelUtil.getCellData(path,CONST_LMSPUTSHEET,i,j);
            }
        }

        return getputData;

    }


    @SuppressWarnings("unchecked")
	@Test(dataProvider = "getData")
    public void Put_Method(String programId,String pname,String pdesc,String online,String Status_code_expected) throws IOException {
        RestAssured.baseURI=prop.getProperty(CONST_URL);
        RestAssured.basePath=prop.getProperty(CONST_PATH);
        //JSON Object
        JSONObject request = new JSONObject();
        
        request.put(PROG_Name,pname);
        request.put(PROG_desc,pdesc);
        request.put(Online_Status,Boolean.valueOf(online));
        Response response = given().auth().preemptive().basic(prop.getProperty(CONST_USERNAME),prop.getProperty(CONST_PWD)).
                header("Content-Type", "application/json").body(request).
                put(RestAssured.baseURI+RestAssured.basePath+"/"+programId);
        System.out.println(response.asString());
       
        Assert.assertEquals(response.getStatusCode(),Integer.parseInt(Status_code_expected));
        

       
        try {
        //Validating input with output
        if (Status_code_expected.equals(Success_Status)){
        	Assert.assertEquals(programId,response.jsonPath().get(PROG_ID));
        	Assert.assertEquals(pname,response.jsonPath().get(PROG_Name));
        	Assert.assertEquals(pdesc,response.jsonPath().get(PROG_desc));
        	Assert.assertEquals(Boolean.valueOf(online),response.jsonPath().get(Online_Status));
            response.then().assertThat().body(Matchers.notNullValue()).
	        body(JsonSchemaValidator.matchesJsonSchemaInClasspath(prop.getProperty(CONST_LMS_SCHEMA)));
        	Reporter.log("Verify the request and response program name are same:" + pname + "," + response.jsonPath().get(PROG_Name));
            Reporter.log("Verify the request and response program description are same:" + pdesc + "," + response.jsonPath().get(PROG_desc));
            Reporter.log("Verify the online status is same in request and response:" + online + "," + response.jsonPath().get(Online_Status));
        	
        
   
        }
        }catch(Exception e) {
        	e.printStackTrace();
        }
        


    }


}
