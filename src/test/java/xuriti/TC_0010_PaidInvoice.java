package xuriti;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import generic.FileLib;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.exception.JsonPathException;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TC_0010_PaidInvoice {
	
	@Test(priority = 1)
	public void TM001testPayNow() throws IOException, InterruptedException
	{
		FileLib fl=new FileLib();
		
		String token=fl.getPropertyData("AdminToken");
		String xuritibaseurl=fl.getPropertyData("XuritiBaseUrl");
		
		RestAssured.baseURI=xuritibaseurl+"/entity/entities";
		RequestSpecification  httpRequest=RestAssured.given();
		JSONObject 	requestParams=new JSONObject( );
		httpRequest.header("Authorization","Bearer "+token);

		Response response=httpRequest.request(Method.GET);
		
		System.out.println(response.prettyPrint());
		
		int i=0;
		String msg=""; String sid=""; String slrintrst="";
		do
		{
			try {
				msg=response.body().jsonPath().get("companies["+i+"].company_name");
				sid=response.body().jsonPath().get("companies["+i+"]._id");
				slrintrst=response.body().jsonPath().get("companies["+i+"].interest");
			}
			catch(JsonPathException e)
			{
				
			}
			//System.out.println(i);			
			i++;
			fl.setPropertyData("SellerCompanyID", sid);
		}
		while(i>-1 && !msg.equals("TEST SELLER MODULE") );
//		System.out.println("Created Seller Company: "+msg);
//		System.out.println("Created Seller Company ID: "+sid);
//		System.out.println("Created Seller Interest: "+slrintrst);
		
		double intrst=Double.parseDouble(slrintrst);
		
		//System.out.println(intrst+10);
		
		Reporter.log("SearchSellerCompany",true);
		
		String usrtoken=fl.getPropertyData("UserToken");
		String slrcmpnyid=fl.getPropertyData("SellerCompanyID");
		String crdtplanid=fl.getPropertyData("CREDITPLANID");
		String byrcmpnyid=fl.getPropertyData("BuyerCompanyID");
		https://uat.xuriti.app/api/entity/641af67b1eb9818b4b6787fb/credit-plans/641af6f11eb9818b4b678f4e
		
		RestAssured.baseURI=xuritibaseurl+"/entity/"+slrcmpnyid+"/credit-plans/"+crdtplanid;
		httpRequest=RestAssured.given();
		httpRequest.header("Authorization","Bearer "+token);
		response=httpRequest.request(Method.GET);
	
		System.out.println(response.prettyPrint());
		
		//Storing the Seller credit plan
		int k=1,l=0,m=1,dis=2;
		FileInputStream fis=new FileInputStream("./src/test/resources/data/discount.xls");
		Workbook wb=WorkbookFactory.create(fis);
		int c=0;
		int n=response.body().jsonPath().get("plan_Details.credit_planid.payment_interval");
		for(int j=1;j<=n;j++)
		{
			int from=response.body().jsonPath().get("plan_Details.credit_planid.discount_slabs["+c+"].from");
			//System.out.println(from);
			int to=response.body().jsonPath().get("plan_Details.credit_planid.discount_slabs["+c+"].to");
			//System.out.println(to);
			int discount=response.body().jsonPath().get("plan_Details.credit_planid.discount_slabs["+c+"].discount");
			//System.out.println(discount);
			for(int o=i;o<=i;o++)
			{
				wb.getSheet("Sheet1").getRow(k).getCell(l).setCellValue(from);
				wb.getSheet("Sheet1").getRow(k).getCell(m).setCellValue(to);
				wb.getSheet("Sheet1").getRow(k).getCell(dis).setCellValue(discount);
				k++;
			}
			c++;
		}
		FileOutputStream fos=new FileOutputStream("./src/test/resources/data/discount.xls");
		wb.write(fos);
		wb.close();
		
		//Calculating total amount of part pay and confirm Invoice
		
		RestAssured.baseURI=xuritibaseurl+"/invoice/search-invoice/"+byrcmpnyid+"/buyer";
		
		httpRequest=RestAssured.given();
		requestParams=new JSONObject();
		httpRequest.header("Authorization","Bearer "+token);
		
		response=httpRequest.request(Method.GET);
		
		System.out.println(response.prettyPrint());
		
		String invcsts=""; int outstdngamt=0;int outstdnginvcamt=0; String slrid=""; int gstamt=0; int totlgstamt=0;
		int pdintrst=0; int ttlpdintrst=0; int dicnt=0; int ttldiscnt=0;
		
		int p=0;
		do {
				try {
						invcsts=response.body().jsonPath().get("invoice["+p+"].invoice_status");
						slrid=response.body().jsonPath().get("invoice["+p+"].seller._id");
						
				}
				catch(JsonPathException e)
				{
					
				}
				
				//System.out.println(invcsts);
				
				if(!(invcsts==null))
				{
					if((invcsts.equals("Partpay") && slrid.equals(slrcmpnyid))||(invcsts.equals("Confirmed") && slrid.equals(slrcmpnyid)))
					{
						System.out.println(invcsts);
						try {
								outstdnginvcamt=response.body().jsonPath().get("invoice["+p+"].outstanding_amount");
								gstamt=response.body().jsonPath().get("invoice["+p+"].tax_unpaid");
								pdintrst=response.body().jsonPath().get("invoice["+p+"].paid_interest");
								dicnt=response.body().jsonPath().get("invoice["+p+"].discount");
						}
						catch(JsonPathException e)
						{
							
						}
						//System.out.println("Invoice Amt="+invcamt);
						outstdngamt=outstdngamt+outstdnginvcamt;
						totlgstamt=totlgstamt+gstamt;
						ttlpdintrst=ttlpdintrst+pdintrst;
						ttldiscnt=ttldiscnt+dicnt;
					}
				}
				
			//System.out.println(cid);
			//System.out.println(p);			
			p++;
		}
		while(p>-1 && invcsts!=null );
		//System.out.println(outstdngamt);
		int totalinvcamt=0;
		totalinvcamt=outstdngamt;
		System.out.println("Total Outstanding Invoice Amount= "+totalinvcamt);
		System.out.println("Total Gst Amount= "+totlgstamt);
		System.out.println("Total Discount Amount= "+ttldiscnt);
		System.out.println("Total Interest Amount= "+ttlpdintrst);
		
		String endpoint="?buyer="+byrcmpnyid+"&seller="+slrcmpnyid;
		RestAssured.baseURI=xuritibaseurl+"/payment/summary";
		
		httpRequest=RestAssured.given();
		requestParams=new JSONObject();
		httpRequest.header("Authorization","Bearer "+token);
		
		response=httpRequest.request(Method.GET,endpoint);
		
		System.out.println(response.prettyPrint());
		
		int total_invoice_amount=response.body().jsonPath().get("total_outstanding");
		//System.out.println(total_invoice_amount);
		int total_gst=response.body().jsonPath().get("total_gst");
		int total_discount=response.body().jsonPath().get("total_discount");
		int interest=response.body().jsonPath().get("total_interest");
		int total_payable=response.body().jsonPath().get("total_payable");
		
		Assert.assertEquals(total_invoice_amount, totalinvcamt);
		Assert.assertEquals(total_gst, totlgstamt);
		Assert.assertEquals(total_discount, ttldiscnt);
		Assert.assertEquals(interest, ttlpdintrst);
		Assert.assertEquals(total_payable, totalinvcamt-ttldiscnt);
	}
}