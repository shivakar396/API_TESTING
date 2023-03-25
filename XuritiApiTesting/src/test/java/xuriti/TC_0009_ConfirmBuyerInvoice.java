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

public class TC_0009_ConfirmBuyerInvoice {
	@Test(priority = 1)
	public void TM0001testSearchBuyerInvoice() throws IOException, InterruptedException
	{
		Thread.sleep(5000);
		FileLib fl=new FileLib();
		String token=fl.getPropertyData("UserToken");
		String xuritibaseurl=fl.getPropertyData("XuritiBaseUrl");
		String byrcmpnyid=fl.getPropertyData("BuyerCompanyID");
		RestAssured.baseURI=xuritibaseurl+"/invoice/search-invoice/"+byrcmpnyid+"/buyer";
		
		RequestSpecification  httpRequest=RestAssured.given();
		JSONObject 	requestParams=new JSONObject();
		httpRequest.header("Authorization","Bearer "+token);
		
		Response response=httpRequest.request(Method.GET);
		
		int i=0;
		String invcid="";String invcnmbr=""; String invcnbfcnm=""; String slrcmpnynm="";
		do
		{
			invcid=response.body().jsonPath().get("invoice["+i+"]._id");
			//System.out.println(invcid);
			invcnmbr=response.body().jsonPath().get("invoice["+i+"].invoice_number");
			//System.out.println(invcnmbr);
			invcnbfcnm=response.body().jsonPath().get("invoice["+i+"].nbfc_name");
			//System.out.println(invcnbfcnm);
			slrcmpnynm=response.body().jsonPath().get("invoice["+i+"].seller.company_name");
			//System.out.println(slrcmpnynm);
			//System.out.println(i);			
			i++;
			fl.setPropertyData("InvoiceID", invcid);
			fl.setPropertyData("InvoiceNumber", invcnmbr);
			fl.setPropertyData("InvoiceNBFCName", invcid);
			fl.setPropertyData("SellerCompanyName", invcid);
		}
		while(i>-1 && !slrcmpnynm.equals("TEST SELLER MODULE") );
		System.out.println("Invoice ID: "+invcid);
		System.out.println("Invoice Number: "+invcnmbr);
		System.out.println("Invoice NBFC Name:"+invcnbfcnm);
		System.out.println("Seller Company Name: "+slrcmpnynm);
		
		Reporter.log("SearchBuyerCompany",true);
			//Printing Response Body
		System.out.println(response.prettyPrint());
	}
	
	@Test(priority = 2)
	public void TM0002testInvoicecComment() throws IOException
	{
		FileLib fl=new FileLib();
		String token=fl.getPropertyData("UserToken");
		String xuritibaseurl=fl.getPropertyData("XuritiBaseUrl");
		String usrid=fl.getPropertyData("UserID");
		String invcid=fl.getPropertyData("InvoiceID");
		
		RestAssured.baseURI=xuritibaseurl+"/invoice/add-comments";
		RequestSpecification  httpRequest=RestAssured.given();
		JSONObject 	requestParams=new JSONObject();
		requestParams.put("userId",usrid);
		requestParams.put("_id",invcid);
		requestParams.put("comment","OK");
		httpRequest.header("Authorization","Bearer "+token).header("Content-Type","application/json");
		httpRequest.body(requestParams.toJSONString());
		Response response=httpRequest.request(Method.POST);
		
		Reporter.log("ConfirmComment",true);
				//Printing Response Body
		System.out.println(response.prettyPrint());
	}
	
	@Test(priority = 3)
	public void TM0002testConfirmInvoice() throws IOException
	{
		FileLib fl=new FileLib();
		String token=fl.getPropertyData("UserToken");
		String xuritibaseurl=fl.getPropertyData("XuritiBaseUrl");
		String usrid=fl.getPropertyData("UserID");
		String invcid=fl.getPropertyData("InvoiceID");
		
		RestAssured.baseURI=xuritibaseurl+"/invoice/status";
		RequestSpecification  httpRequest=RestAssured.given();
		JSONObject 	requestParams=new JSONObject();
		requestParams.put("invoiceID",invcid);
		requestParams.put("status","Confirmed");
		requestParams.put("userConsentGiven",true);
		requestParams.put("user_comment","OK");
		requestParams.put("user_consent_message","");
		httpRequest.header("Authorization","Bearer "+token).header("Content-Type","application/json");
		httpRequest.body(requestParams.toJSONString());
		Response response=httpRequest.request(Method.PATCH);
		
		Reporter.log("ConfirmInvoice",true);
				//Printing Response Body
		System.out.println(response.prettyPrint());
		
	}
}
