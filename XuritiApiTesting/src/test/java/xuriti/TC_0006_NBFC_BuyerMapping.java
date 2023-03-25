package xuriti;

import org.testng.annotations.Test;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.testng.Reporter;
import org.testng.annotations.Test;

import generic.FileLib;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TC_0006_NBFC_BuyerMapping {
	
	@Test(priority = 1)
	public void TM0001testSearchNBFC() throws IOException, InterruptedException
	{

		FileLib fl=new FileLib();
		String token=fl.getPropertyData("AdminToken");
		String xuritibaseurl=fl.getPropertyData("XuritiBaseUrl");
		
		RestAssured.baseURI=xuritibaseurl+"/nbfcs/get-nbfc";
		RequestSpecification  httpRequest=RestAssured.given();
		httpRequest.header("Authorization","Bearer "+token);
		Response response=httpRequest.request(Method.GET);
		
		int i=0;
		String nbfcnm=""; String nbfcid="";
		do
		{
			nbfcnm=response.body().jsonPath().get("get_nbfc["+i+"].nbfc_name");
			
			nbfcid=response.body().jsonPath().get("get_nbfc["+i+"]._id");
			
//			System.out.println(i);			
			i++;
			fl.setPropertyData("NBFCNAME", nbfcnm);
			fl.setPropertyData("NBFCID", nbfcid);
		}
		while(i>-1 && !nbfcnm.equals("SHIVAKAR NBFC") );
		System.out.println("NBFC NAME: "+nbfcnm);
		System.out.println("NBFC ID: "+nbfcid);
		
		Reporter.log("SearchNBFC",true);
				//Printing Response Body
		System.out.println(response.prettyPrint());
	}
	
	@Test(priority = 2)
	public void TM0002testNBFC_BuyerAndSellerMapping() throws IOException, InterruptedException
	{
		Thread.sleep(5000);
		FileLib fl=new FileLib();
		String token=fl.getPropertyData("AdminToken");
		String selrcmpnyid=fl.getPropertyData("SellerCompanyID");
		String byrcmpnyid=fl.getPropertyData("BuyerCompanyID");
		String admnid=fl.getPropertyData("AdminID");
		String nbfcid=fl.getPropertyData("NBFCID");
		String xuritibaseurl=fl.getPropertyData("XuritiBaseUrl");
		
		RestAssured.baseURI=xuritibaseurl+"/nbfcs/add-buyer/"+nbfcid+"/"+admnid;
		RequestSpecification  httpRequest=RestAssured.given();
		
		String s="{\r\n"
				+ "    \"companies\": [\r\n"
				+ "        {\r\n"
				+ "            \"buyer_id\": \""+byrcmpnyid+"\",\r\n"
				+ "            \"seller_id\": \""+selrcmpnyid+"\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"user\": \""+admnid+"\"\r\n"
				+ "}";
		
		//System.out.println(s);
		
		httpRequest.header("Authorization","Bearer "+token).header("Content-Type","application/json");
		httpRequest.body(s);
		Response response=httpRequest.request(Method.POST);
		
		Reporter.log("NBFC_BuyerAndSellerMapping",true);
		
			//Printing Response Body
		System.out.println(response.prettyPrint());
		
	}
}
