package xuriti;

import org.testng.annotations.Test;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import generic.FileLib;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TC_0005_AddBuyerInSellerCreditPlan {
	
	@Test(priority = 1)
	public void TM0001testGetCreditPlan() throws IOException, InterruptedException
	{
		
		FileLib fl=new FileLib();
		String token=fl.getPropertyData("AdminToken");
		String selrcmpnyid=fl.getPropertyData("SellerCompanyID");
		String xuritibaseurl=fl.getPropertyData("XuritiBaseUrl");
		
		RestAssured.baseURI=xuritibaseurl+"/entity/"+selrcmpnyid+"/credit-plans";
		
		RequestSpecification  httpRequest=RestAssured.given();
		JSONObject 	requestParams=new JSONObject();
		httpRequest.header("Authorization","Bearer "+token);
		Response response=httpRequest.request(Method.GET);
		boolean sts=response.body().jsonPath().get("status");

		int i=0;
		String plnm=""; String plnid="";
		do
		{
					
							plnm=response.body().jsonPath().get("plan_Details["+i+"].plan_name"); 
							System.out.println(plnm);
		
							plnid=response.body().jsonPath().get("plan_Details["+i+"]._id");
							System.out.println(plnid);
							
							//System.out.println(i);			
							i++;
				
			fl.setPropertyData("CREDITPLANNAME", plnm);
			fl.setPropertyData("CREDITPLANID", plnid);
		}
		while(i>-1 && !plnm.equals("SELLER CREDIT PLAN") && plnm!=null);
		
		Reporter.log("GetCreditPlan",true);
				//Printing Response Body
		System.out.println(response.prettyPrint());
		
		Assert.assertEquals(sts, true);
	}
	@Test(priority = 2)
	public void TM002testSetDefaultPlan() throws IOException
	{
		
		FileLib fl=new FileLib();
		String slrcmpnyid=fl.getPropertyData("SellerCompanyID");
		String crdtplanid=fl.getPropertyData("CREDITPLANID");
		String adminid=fl.getPropertyData("AdminID");
		String token=fl.getPropertyData("AdminToken");
		String xuritibaseurl=fl.getPropertyData("XuritiBaseUrl");
		
		RestAssured.baseURI=xuritibaseurl+"/entity/"+slrcmpnyid+"/credit-plans/"+crdtplanid;
		RequestSpecification  httpRequest=RestAssured.given();
		JSONObject 	requestParams=new JSONObject();
		
		String s="{\r\n"
				+ "    \"user\": \""+adminid+"\"\r\n"
				+ "}";
		
		httpRequest.header("Authorization","Bearer "+token).header("Content-Type","application/json");
		httpRequest.body(s);
		Response response=httpRequest.request(Method.PATCH);
		boolean sts=response.body().jsonPath().get("status");
		
		Reporter.log("SetDefaultPlan");
		
				//Printing Response Body
		System.out.println(response.prettyPrint());
		
		Assert.assertEquals(sts, true);
		
	}
	@Test(priority = 3)
	public void TM0003testAddBuyerInCreditPlan() throws IOException, InterruptedException
	{
		
		FileLib fl=new FileLib();
		String token=fl.getPropertyData("AdminToken");
		String selrcrdtplanid=fl.getPropertyData("CREDITPLANID");
		String selrcmpnyid=fl.getPropertyData("SellerCompanyID");
		String byrcmpnyid=fl.getPropertyData("BuyerCompanyID");
		String admnid=fl.getPropertyData("AdminID");
		String xuritibaseurl=fl.getPropertyData("XuritiBaseUrl");
		
		RestAssured.baseURI=xuritibaseurl+"/entity/"+selrcmpnyid+"/credit-plans-map";
		RequestSpecification  httpRequest=RestAssured.given();
		
		String s="{\r\n"
				+ "    \"credit_planid\": \""+selrcrdtplanid+"\",\r\n"
				+ "    \"seller_id\": \""+selrcmpnyid+"\",\r\n"
				+ "    \"buyers\": [\r\n"
				+ "        {\r\n"
				+ "            \"buyer_id\": \""+byrcmpnyid+"\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"user\": \""+admnid+"\"\r\n"
				+ "}";
		
		//System.out.println(s);
		
		httpRequest.header("Authorization","Bearer "+token).header("Content-Type","application/json");
		httpRequest.body(s);
		Response response=httpRequest.request(Method.POST);
		
		Reporter.log("AddBuyerInCreditPlan",true);
		
				//Printing Response Body
		System.out.println(response.prettyPrint());
	}
}
