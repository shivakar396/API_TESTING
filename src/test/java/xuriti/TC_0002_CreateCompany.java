package xuriti;

import org.testng.annotations.Test;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import generic.FileLib;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
/**
 * 
 * @author Xuriti
 *	This class is use to create two company/entity in User Login/Module
 *
 */
public class TC_0002_CreateCompany {
	@Test
	public void TM0001testSearchGST() throws IOException
	{
		FileLib fl=new FileLib();
		String token=fl.getPropertyData("UserToken");
		String xuritibaseurl=fl.getPropertyData("XuritiBaseUrl");
		
		RestAssured.baseURI=xuritibaseurl+"/entity/search-gst";
		RequestSpecification  httpRequest=RestAssured.given();
		JSONObject 	requestParams=new JSONObject();
		requestParams.put("gstin","24AAJFC0407A1ZC");
		httpRequest.header("Authorization","Bearer "+token).header("Content-Type","application/json");
		httpRequest.body(requestParams.toJSONString());
		Response response=httpRequest.request(Method.POST);
		String gstnmbr=response.body().jsonPath().get("company.gstin");
		Reporter.log("SearchGST",true);
		System.out.println(response.prettyPrint());
		Assert.assertEquals(gstnmbr, "24AAJFC0407A1ZC");
	}
	@Test(priority=1)
	public void TM0002testCreateCompanyAsBuyer() throws IOException
	{
		
		FileLib fl=new FileLib();
		String token=fl.getPropertyData("UserToken");
		String usrid=fl.getPropertyData("UserID");
		String xuritibaseurl=fl.getPropertyData("XuritiBaseUrl");
		
		RestAssured.baseURI=xuritibaseurl+"/entity/add-entity";
		RequestSpecification  httpRequest=RestAssured.given();
		JSONObject 	requestParams=new JSONObject();
		
		requestParams.put("gstin","24AAJFC0407A1ZC");
		requestParams.put("userID",usrid);
		requestParams.put("companyName","TEST BUYER MODULE");
		requestParams.put("pan","AAJFC0407A");
		requestParams.put("admin_mobile","9039128615");
		requestParams.put("admin_email","kumarshivakar396@gmail.com");
		requestParams.put ("consent_gst_defaultFlag",true);
		
		httpRequest.header("Authorization","Bearer "+token).header("Content-Type","application/json");
		httpRequest.body(requestParams.toJSONString());
		Response response=httpRequest.request(Method.POST);
		
		String msg=response.body().jsonPath().get("message");
		
		Reporter.log("CreateCompanyAsBuyer",true);
		System.out.println(response.prettyPrint()+"\n");
		
		Assert.assertEquals(msg, "Saved Entity");
		
	}
	@Test(priority=2)
	public void TM0003testCreateCompanyAsSeller() throws IOException
	{
		
		FileLib fl=new FileLib();
		String token=fl.getPropertyData("UserToken");
		String usrid=fl.getPropertyData("UserID");
		String xuritibaseurl=fl.getPropertyData("XuritiBaseUrl");
		
		RestAssured.baseURI=xuritibaseurl+"/entity/add-entity";
		
		RequestSpecification  httpRequest=RestAssured.given();
		JSONObject 	requestParams=new JSONObject();
		requestParams.put("gstin","27AAACT1728P1ZZ");
		requestParams.put("userID",usrid);
		requestParams.put("companyName","TEST SELLER MODULE");
		requestParams.put("pan","AAACT1728P");
		requestParams.put("admin_mobile","9039128615");
		requestParams.put("admin_email","kumarshivakar396@gmail.com");
		requestParams.put ("consent_gst_defaultFlag",true);
		
		httpRequest.header("Authorization","Bearer "+token).header("Content-Type","application/json");
		httpRequest.body(requestParams.toJSONString());
		Response response=httpRequest.request(Method.POST);
		String msg=response.body().jsonPath().get("message");
		
		System.out.println("CreateCompanyAsSeller");
		
		System.out.println(response.prettyPrint());
		
		Assert.assertEquals(msg, "Saved Entity");
	}
}
