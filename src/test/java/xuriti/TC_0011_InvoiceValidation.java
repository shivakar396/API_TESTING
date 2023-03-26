package xuriti;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import generic.FileLib;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.exception.JsonPathException;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TC_0011_InvoiceValidation {
		@Test
		public void TM0001testInvoiceValidation() throws IOException
		{
			FileLib fl=new FileLib();
			
			String byrcmpnyid=fl.getPropertyData("BuyerCompanyID");
			String xuritibaseurl=fl.getPropertyData("XuritiBaseUrl");
			String usrtokn=fl.getPropertyData("UserToken");
			String usrid=fl.getPropertyData("UserID");
			RestAssured.baseURI=xuritibaseurl+"/invoice/search-invoice/"+byrcmpnyid+"/buyer";
			
			RequestSpecification httpRequest=RestAssured.given();

			httpRequest.header("Authorization","Bearer "+usrtokn);
			
			Response response=httpRequest.request(Method.GET);
			
			//System.out.println(response.prettyPrint());
			
			String invcsts=""; int outstdngamt=0;int outstdnginvcamt=0;  int gstamt=0; int totlgstamt=0; String invcnmbr="";
			int pdintrst=0; int ttlpdintrst=0; int dicnt=0; int ttldiscnt=0;String invtyp=""; String invcamt=""; String invcdt="";
			String invcduedt="";int crdtprd=0; int pymntintrvl=0; int frm=0,to=0,disc=0;
			
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
									invcnmbr=response.body().jsonPath().get("invoice["+p+"].invoice_number");
									System.out.println("Invoice Number: "+invcnmbr);
									invcdt=invcnmbr=response.body().jsonPath().get("invoice["+p+"].invoice_date");
									System.out.println("Invoice Date: "+invcdt);
									invcamt=response.body().jsonPath().get("invoice["+p+"].invoice_amount");
									double parsinvcamt=Double.parseDouble(invcamt);
									System.out.println("Invoice Amount: "+parsinvcamt);
									gstamt=response.body().jsonPath().get("invoice["+p+"].tax_unpaid");
									System.out.println("Invoice GST: "+gstamt);
									outstdnginvcamt=response.body().jsonPath().get("invoice["+p+"].outstanding_amount");
									System.out.println("Invoice Out Standing Amount: "+outstdnginvcamt);
									
									Assert.assertEquals(outstdnginvcamt, parsinvcamt+gstamt);
									
									crdtprd=response.body().jsonPath().get("invoice["+p+"].credit_plan.credit_period");
									System.out.println("Invoice Credit Period: "+crdtprd);
									pymntintrvl=response.body().jsonPath().get("invoice["+p+"].credit_plan.payment_interval");
									System.out.println("Invoice Payment Interval: "+pymntintrvl);
									for(int i=0;i<pymntintrvl;i++)
									{
										pymntintrvl=response.body().jsonPath().get("invoice["+p+"].credit_plan.discount_slabs["+i+"].from");
										System.out.println("Invoice Payment Interval: "+pymntintrvl);
									}
									pdintrst=response.body().jsonPath().get("invoice["+p+"].paid_interest");
									System.out.println("Invoice Interest: "+pdintrst);
									dicnt=response.body().jsonPath().get("invoice["+p+"].discount");
									System.out.println("Invoice Discount: "+dicnt);
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

			httpRequest.header("Authorization","Bearer "+usrtokn);
			
			response=httpRequest.request(Method.GET);
			int crdtlmt=0,avlcrdtlmt=0;
			//System.out.println(response.prettyPrint());
			try {
					crdtlmt=response.body().jsonPath().get("company[0].Company.company.creditLimit");
					avlcrdtlmt=response.body().jsonPath().get("company[0].Company.company.avail_credit");
			}
			catch(JsonPathException e)
			{
				
			}
			System.out.println(crdtlmt-outstdngamt);
			int credit_available=crdtlmt-outstdngamt;
			Assert.assertEquals(avlcrdtlmt, credit_available);
		}
}