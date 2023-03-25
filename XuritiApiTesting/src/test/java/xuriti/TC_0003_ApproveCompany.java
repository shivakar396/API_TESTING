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
/**
 * 
 * @author Xuriti
 * 
 * This class is use to Approve created compnay/entity from Admin Login/Module
 *
 */
public class TC_0003_ApproveCompany {
	
	
	@Test(priority = 1)
	public void TM0001testSearchBuyerCompany() throws IOException, InterruptedException
	{
		FileLib fl=new FileLib();
		String token=fl.getPropertyData("AdminToken");
		String xuritibaseurl=fl.getPropertyData("XuritiBaseUrl");
		
		RestAssured.baseURI=xuritibaseurl+"/entity/entities";
		
		RequestSpecification  httpRequest=RestAssured.given();
		JSONObject 	requestParams=new JSONObject();
		httpRequest.header("Authorization","Bearer "+token);
		
		Response response=httpRequest.request(Method.GET);
		
		int i=0;
		String msg="";String cid="";
		do
		{
			msg=response.body().jsonPath().get("companies["+i+"].company_name");
			//System.out.println(msg);
			cid=response.body().jsonPath().get("companies["+i+"]._id");
			//System.out.println(cid);
			//System.out.println(i);			
			i++;
			fl.setPropertyData("BuyerCompanyID", cid);
		}
		while(i>-1 && !msg.equals("TEST BUYER MODULE") && msg!=null );
//		System.out.println("Created Buyer Company: "+msg);
//		System.out.println("Created Buyer Company ID: "+cid);
		
		Reporter.log("SearchBuyerCompany",true);
		System.out.println(response.prettyPrint());
		Assert.assertEquals(msg, "TEST BUYER MODULE");
	}
	@Test(priority = 2)
	public void TM0002testSearchSellerCompany() throws IOException, InterruptedException
	{
		Thread.sleep(5000);
		FileLib fl=new FileLib();
		String token=fl.getPropertyData("AdminToken");
		String xuritibaseurl=fl.getPropertyData("XuritiBaseUrl");
		
		RestAssured.baseURI=xuritibaseurl+"/entity/entities";
		RequestSpecification  httpRequest=RestAssured.given();
		JSONObject 	requestParams=new JSONObject();
		httpRequest.header("Authorization","Bearer "+token);

		Response response=httpRequest.request(Method.GET);
		
		int i=0;
		String msg=""; String sid=""; String slrintrst="";
		do
		{
			msg=response.body().jsonPath().get("companies["+i+"].company_name");
			
			sid=response.body().jsonPath().get("companies["+i+"]._id");
			slrintrst=response.body().jsonPath().get("companies["+i+"].interest");
			
			//System.out.println(i);			
			i++;
			fl.setPropertyData("SellerCompanyID", sid);
		}
		while(i>-1 && !msg.equals("TEST SELLER MODULE") && msg!=null );
//		System.out.println("Created Seller Company: "+msg);
//		System.out.println("Created Seller Company ID: "+sid);
//		System.out.println("Created Seller Interest: "+slrintrst);
		
		Reporter.log("SearchSellerCompany",true);
		
		System.out.println(response.prettyPrint());
		
		Assert.assertEquals(msg, "TEST SELLER MODULE");
	}
	@Test(priority=3)
	public void TM0003testApproveBuyerCompany() throws IOException, InterruptedException
	{
		Thread.sleep(5000);
		FileLib fl=new FileLib();
		String byrcompnyid=fl.getPropertyData("BuyerCompanyID");
		String token=fl.getPropertyData("AdminToken");
		String admnid=fl.getPropertyData("AdminID");
		String byrcmpnyid=fl.getPropertyData("BuyerCompanyID");
		String xuritibaseurl=fl.getPropertyData("XuritiBaseUrl");
		RestAssured.baseURI=xuritibaseurl+"/entity/update-entity/"+byrcompnyid;
		
		RequestSpecification  httpRequest=RestAssured.given();
		httpRequest.header("Authorization","Bearer "+token);
		Response response = (Response) RestAssured.given()
				.contentType("multipart/form-data")
				.multiPart("company_name","TEST BUYER MODULE")
				.multiPart("gstin","24AAJFC0407A1ZC")
				.multiPart("district","VADODARA")
				.multiPart("state","GUJARAT")
				.multiPart("pinCode","390021")
				.multiPart("industry_type","PARTNERSHIP")
				.multiPart("cin","")
				.multiPart("tan","")
				.multiPart("pan","AAJFC0407A")
				.multiPart("annual_turnover","500000")
				.multiPart("xuriti_score","0")
				.multiPart("address","LANDMARK,TOWER B S B 123,RACE COURSE CIRCLE")
				.multiPart("admin_email","kumarshivakar396@gmail.com")
				.multiPart("admin_mobile","9039128615")
				.multiPart("creditLimit","100000")
				.multiPart("interest","0")
				.multiPart("status","Approved")
				.multiPart("eNachMandate",false)
				.multiPart("seller_flag",false)
				.multiPart("company_email","kumarshivakar396@gmail.com")
				.multiPart("company_mobileNumber","9039128615")
				.multiPart("admin_name","SHIVAKAR KUMAR")
				.multiPart("updated_by",admnid)
				.multiPart("userId",admnid)
				.multiPart("comment","Buyer")
				.headers("Authorization","Bearer "+token).when().patch("https://uat.xuriti.app/api/entity/update-entity/"+byrcmpnyid).thenReturn();
		
		String msg=response.body().jsonPath().get("message");
		
		Reporter.log("ApproveBuyerCompany",true);
		
		System.out.println( response.prettyPrint());
		
		Assert.assertEquals(msg, "Commment added sucessfully");
	}
	@Test(priority=4)
	public void TM0004testApproveSellerCompany() throws IOException, InterruptedException
	{
		Thread.sleep(5000);
		FileLib fl=new FileLib();
		String selrcmpnyid=fl.getPropertyData("SellerCompanyID");
		String token=fl.getPropertyData("AdminToken");
		String admnid=fl.getPropertyData("AdminID");
		String xuritibaseurl=fl.getPropertyData("XuritiBaseUrl");
		
		RestAssured.baseURI=xuritibaseurl+"/entity/update-entity/"+selrcmpnyid;
		RequestSpecification  httpRequest=RestAssured.given();
		
		httpRequest.header("Authorization","Bearer "+token);
		Response response = (Response) RestAssured.given()
				.contentType("multipart/form-data")
				.multiPart("company_name","TEST SELLER MODULE")
				.multiPart("gstin","27AAACT1728P1ZZ")
				.multiPart("district","PUNE")
				.multiPart("state","MAHARASHTRA")
				.multiPart("pinCode","411042")
				.multiPart("industry_type","PUBLIC LIMITED COMPANY")
				.multiPart("cin","")
				.multiPart("tan","")
				.multiPart("pan","AAACT1728P")
				.multiPart("annual_turnover","5000000")
				.multiPart("xuriti_score","0")
				.multiPart("address","3RD FLOOR VARDHAMAN OFFICE NO.1 AND 2,H.NO.321/A/3,MAHATMA PHULE PETH,SHANKER SETH ROAD")
				.multiPart("admin_email","kumarshivakar396@gmail.com")
				.multiPart("admin_mobile","9039128615")
				.multiPart("creditLimit","0")
				.multiPart("interest","36")
				.multiPart("status","Approved")
				.multiPart("eNachMandate",false)
				.multiPart("seller_flag",true)
				.multiPart("company_email","kumarshivakar396@gmail.com")
				.multiPart("company_mobileNumber","9039128615")
				.multiPart("admin_name","SHIVAKAR KUMAR")
				.multiPart("updated_by",admnid)
				.multiPart("userId",admnid)
				.multiPart("comment","Seller")
				.headers("Authorization","Bearer "+token).when().patch("https://uat.xuriti.app/api/entity/update-entity/"+selrcmpnyid).thenReturn();
		
		String msg=response.body().jsonPath().get("message");
		
		Reporter.log("ApproveSellerCompany",true);
		
		System.out.println( response.prettyPrint());
		
		Assert.assertEquals(msg, "Commment added sucessfully");
		
	}
}
