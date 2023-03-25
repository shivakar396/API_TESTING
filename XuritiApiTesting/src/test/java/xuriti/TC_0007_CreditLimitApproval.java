package xuriti;

import org.testng.annotations.Test;
import java.io.IOException;

import org.testng.Reporter;
import org.testng.annotations.Test;

import generic.FileLib;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TC_0007_CreditLimitApproval {
	
	@Test(priority = 1)
	public void TM0001testApproveCreditLimit() throws IOException
	{
		FileLib fl=new FileLib();
		String token=fl.getPropertyData("AdminToken");
		String mgrid=fl.getPropertyData("ManagerID");
		String byrcmpnyid=fl.getPropertyData("BuyerCompanyID");
		String xuritibaseurl=fl.getPropertyData("XuritiBaseUrl");
		
		RestAssured.baseURI=xuritibaseurl+"/entity/credtilimitapproval";
		RequestSpecification  httpRequest=RestAssured.given();
		
		String s="{\r\n"
				+ "    \"comment\": \"OK\",\r\n"
				+ "    \"status\": \"Approved\",\r\n"
				+ "    \"userid\": \""+mgrid+"\",\r\n"
				+ "    \"companyid\": \""+byrcmpnyid+"\",\r\n"
				+ "    \"user\": \""+mgrid+"\"\r\n"
				+ "}";
		
		//System.out.println(s);
		
		httpRequest.header("Authorization","Bearer "+token).header("Content-Type","application/json");
		httpRequest.body(s);
		Response response=httpRequest.request(Method.PATCH);
		
		Reporter.log("ApproveCreditLimit",true);
				//Printing Response Body
		System.out.println(response.prettyPrint());
		
	}
}
