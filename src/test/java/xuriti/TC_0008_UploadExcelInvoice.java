package xuriti;

import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.poi.EncryptedDocumentException;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import generic.FileLib;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.path.json.exception.JsonPathException;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class TC_0008_UploadExcelInvoice {
	@Test(priority = 1)
	public void TM0001testuploadExcelInvoice()throws IOException,EncryptedDocumentException
	{
		FileLib fl=new FileLib();
		String xuritibaseurl=fl.getPropertyData("XuritiBaseUrl");
		String slrcmpnyid=fl.getPropertyData("SellerCompanyID");
		String usrid=fl.getPropertyData("UserID");
		String numbr=fl.getPropertyData("num");
		int number=Integer.parseInt(numbr);
		System.out.println(number);
		fl.setPropertyData("num", ""+(number+1));
		String invc="L0NV0000"+number;
		fl.setExcelPropertyData(1, 0,invc);
		
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd");
        String formatted = format1.format(cal.getTime());
        //System.out.println(formatted);
        
        fl.setExcelPropertyData(1, 8,formatted);
        
		File file=new File("./src/test/resources/data/DevInvoice1.xls");
		String abpath=file.getAbsolutePath();
		String token=fl.getPropertyData("UserToken");
		
		Response response=RestAssured.given().header("Authorization","Bearer "+token)
				.multiPart("file",new File(abpath),"multipart/form-data")
				.when().post(xuritibaseurl+"/invoice/upload-invoice-excel")
				.thenReturn();
		
		Reporter.log("uploadExcelInvoice");
				//Printing Response Body
		System.out.println(response.prettyPrint());
		
		//Calculating total amount of part pay and confirm Invoice
		String byrcmpnyid=fl.getPropertyData("BuyerCompanyID");
		
				RestAssured.baseURI=xuritibaseurl+"/invoice/search-invoice/"+byrcmpnyid+"/buyer";
				
				RequestSpecification httpRequest=RestAssured.given();

				httpRequest.header("Authorization","Bearer "+token);
				
				response=httpRequest.request(Method.GET);
				
				System.out.println(response.prettyPrint());
				
				String invcsts=""; int outstdngamt=0;int outstdnginvcamt=0;  int gstamt=0; int totlgstamt=0;
				int pdintrst=0; int ttlpdintrst=0; int dicnt=0; int ttldiscnt=0;String invtyp="";
				
				int p=0;
				do {
						try {
								invcsts=response.body().jsonPath().get("invoice["+p+"].invoice_status");
								invtyp=response.body().jsonPath().get("invoice["+p+"].invoice_type");
						}
						catch(JsonPathException e)
						{
							
						}
						
						//System.out.println(invcsts);
						
						if(!(invcsts==null))
						{
							if((invcsts.equals("Partpay") && invtyp.equals("IN"))||(invcsts.equals("Confirmed") && invtyp.equals("IN"))||(invcsts.equals("Pending") && invtyp.equals("IN")))
							{
								//System.out.println(invcsts);
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
				
				//Search Buyer and check  Credit Available Amount

				RestAssured.baseURI=xuritibaseurl+"/entity/"+byrcmpnyid+"/user/"+usrid;
				
				httpRequest=RestAssured.given();

				httpRequest.header("Authorization","Bearer "+token);
				
				response=httpRequest.request(Method.GET);
				
				System.out.println(response.prettyPrint());
				
				int crdtlmt=response.body().jsonPath().get("company[0].Company.company.creditLimit");
				int avlcrdtlmt=response.body().jsonPath().get("company[0].Company.company.avail_credit");
				System.out.println(crdtlmt-outstdngamt);
				int credit_available=crdtlmt-outstdngamt;
				Assert.assertEquals(avlcrdtlmt, credit_available);
	}
}