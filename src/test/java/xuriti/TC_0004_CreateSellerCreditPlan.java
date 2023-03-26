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
/**
 * 
 * @author Xuriti
 * This class is use to create Credit plan for Seller from Admin Login/Module
 *
 */
public class TC_0004_CreateSellerCreditPlan{
	
	@Test(priority = 1)
	public void TM0001testCreateSellerCreditPlan() throws IOException, InterruptedException
	{
		
		FileLib fl=new FileLib();
		String token=fl.getPropertyData("AdminToken");
		String selrcmpnyid=fl.getPropertyData("SellerCompanyID");
		String admnid=fl.getPropertyData("AdminID");
		String xuritibaseurl=fl.getPropertyData("XuritiBaseUrl");
		
		RestAssured.baseURI=xuritibaseurl+"/entity/"+selrcmpnyid+"/credit-plans";
		
		RequestSpecification  httpRequest=RestAssured.given();
		String s="{\r\n"
				+ "    \"plan_name\": \"SELLER CREDIT PLAN\",\r\n"
				+ "    \"default_plan\": false,\r\n"
				+ "    \"credit_period\": \"30\",\r\n"
				+ "    \"payment_interval\": \"3\",\r\n"
				+ "    \"discount_slabs\": [\r\n"
				+ "        {\r\n"
				+ "            \"from\": \"0\",\r\n"
				+ "            \"to\": \"10\",\r\n"
				+ "            \"discount\": \"5\"\r\n"
				+ "        },\r\n"
				+ "        {\r\n"
				+ "            \"from\": 11,\r\n"
				+ "            \"to\": \"20\",\r\n"
				+ "            \"discount\": \"3\"\r\n"
				+ "        },\r\n"
				+ "        {\r\n"
				+ "            \"from\": 21,\r\n"
				+ "            \"to\": \"30\",\r\n"
				+ "            \"discount\": \"1\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"createdBy\": \""+admnid+"\",\r\n"
				+ "    \"user\": \""+admnid+"\"\r\n"
				+ "}";
		
		//System.out.println(s);
		
		httpRequest.header("Authorization","Bearer "+token).header("Content-Type","application/json");
		httpRequest.body(s);
		Response response=httpRequest.request(Method.POST);
		
		Reporter.log("CreateSellerCreditPlan",true);
		
		System.out.println(response.prettyPrint());
			
	}
}
