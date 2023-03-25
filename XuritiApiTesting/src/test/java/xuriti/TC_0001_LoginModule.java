package xuriti;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import generic.FileCredential;
import generic.FileLib;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TC_0001_LoginModule {
	@Test
	public void TM001testUserLogin() throws IOException
	{
		/*Reading UserLoginEmail & and their Password from credential.property file using 
		  FileCredential Class which is present in src/main/java/generic pkg*/ 
		
		FileCredential fc=new FileCredential();
		String usrlogeml=fc.readCredential("UserLoginEmail");
		//System.out.println(usrlogeml);
		String usrlogpss=fc.readCredential("UserLoginPassword");
		//System.out.println(usrlogpss);
		
		/*Reading Xuriti API Url from commondata.property file using 
		  FileLib Class which is present in src/main/java/generic pkg*/
		
		FileLib fl=new FileLib();
		String xuritibaseurl=fl.getPropertyData("XuritiBaseUrl");
		
		RestAssured.baseURI=xuritibaseurl+"/auth/user-login";
		RequestSpecification  httpRequest=RestAssured.given();
		JSONObject 	requestParams=new JSONObject();
		
		requestParams.put("email",usrlogeml);
		requestParams.put("password",usrlogpss);
		
		requestParams.put("recaptcha","test_recaptcha");
		httpRequest.header("Content-Type","application/json");
		httpRequest.body(requestParams.toJSONString());
		Response response=httpRequest.request(Method.POST);
		
		
		 //Fetching data of User from Response Body from here to
	
		String msg=response.body().jsonPath().get("message");
		System.out.println("User Message is= "+msg);
		String token=response.body().jsonPath().get("token");
		String usrid=response.body().jsonPath().get("user._id");
		String usrnm=response.body().jsonPath().get("user.name");
		String usreml=response.body().jsonPath().get("user.email");
		String usrmbl=response.body().jsonPath().get("user.mobile_number");
		String usrfrstnm=response.body().jsonPath().get("user.first_name");
		String usrlstnm=response.body().jsonPath().get("user.last_name");
		String usrrole=response.body().jsonPath().get("user.user_role");
		int code=response.body().jsonPath().get("code");		//upto here
		
		//Saving all the User usable data in commondata.property file by using setPropertyData() method of FileLib Class 
		
		fl.setPropertyData("UserMessage", msg);
		fl.setPropertyData("UserToken", token);
		fl.setPropertyData("UserID", usrid);
		fl.setPropertyData("UserName", usrnm);
		fl.setPropertyData("UserEmail", usreml);
		fl.setPropertyData("UserMobileNumber", usrmbl);
		fl.setPropertyData("UserFirstName", usrfrstnm);
		fl.setPropertyData("UserLastName", usrlstnm);
		fl.setPropertyData("UserRole", usrrole);
		
		Reporter.log("UserLogin");
		//Printing User Response Body
		System.out.println(response.prettyPrint());
		Assert.assertEquals(code, 100);
	}
	@Test
	public void TM002testAdminLogin() throws IOException
	{
		/*Reading AdminLoginEmail & and their Password from credential.property file using 
		  FileLib Class which is present in src/main/java/generic pkg*/ 
		
		FileCredential fc=new FileCredential();
		String admnlogeml=fc.readCredential("AdminLoginEmail");
		String admnlogpss=fc.readCredential("AdminLoginPassword");
		
		/*Reading Xuriti API Url from commondata.property file using 
		  FileLib Class which is present in src/main/java/generic pkg*/ 
		
		FileLib fl=new FileLib();
		String xuritibaseurl=fl.getPropertyData("XuritiBaseUrl");
		
		RestAssured.baseURI=xuritibaseurl+"/auth/user-login";
		RequestSpecification  httpRequest=RestAssured.given();
		JSONObject 	requestParams=new JSONObject();
		
		requestParams.put("email",admnlogeml);
		requestParams.put("password",admnlogpss);
		requestParams.put("recaptcha","test_recaptcha");
		httpRequest.header("Content-Type","application/json");
		httpRequest.body(requestParams.toJSONString());
		Response response=httpRequest.request(Method.POST);
		
		//Fetching data of Admin from Response Body from here to
		
		String msg=response.body().jsonPath().get("message");
		System.out.println("Admin Message is= "+msg);
		String token=response.body().jsonPath().get("token");
		String admnid=response.body().jsonPath().get("user._id");
		String admnm=response.body().jsonPath().get("user.name");
		String admneml=response.body().jsonPath().get("user.email");
		String admnmbl=response.body().jsonPath().get("user.mobile_number");
		String admnfrstnm=response.body().jsonPath().get("user.first_name");
		String admnlstnm=response.body().jsonPath().get("user.last_name");
		String admnrole=response.body().jsonPath().get("user.user_role");
		int code=response.body().jsonPath().get("code");	//upto here
		
		//Saving all the Admin usable data in commondata.property file by using setPropertyData() method of FileLib Class
		
		fl.setPropertyData("AdminMessage", msg);
		fl.setPropertyData("AdminToken", token);
		fl.setPropertyData("AdminID", admnid);
		fl.setPropertyData("AdminName", admnm);
		fl.setPropertyData("AdminEmail", admneml);
		fl.setPropertyData("AdminMobileNumber", admnmbl);
		fl.setPropertyData("AdminFirstName", admnfrstnm);
		fl.setPropertyData("AdminLastName", admnlstnm);
		fl.setPropertyData("AdminRole", admnrole);
		
		Reporter.log("AdminLogin");
			//Printing Admin Response Body
		System.out.println(response.prettyPrint());
		Assert.assertEquals(code, 100);
	}
	@Test
	public void TM003testManagerLogin() throws IOException
	{
		/*Reading ManagerLoginEmail & and their Password from credential.property file using 
		  FileCredential Class which is present in src/main/java/generic pkg*/
		
		
		FileCredential fc=new FileCredential();
		String mgrlogeml=fc.readCredential("ManagerLoginEmail");
		String mgrnlogpss=fc.readCredential("ManagerLoginPassword");
		
		/*Reading Xuriti API Url from commondata.property file using 
		  FileLib Class which is present in src/main/java/generic pkg*/
		
		FileLib fl=new FileLib();
		String xuritibaseurl=fl.getPropertyData("XuritiBaseUrl");
		
		RestAssured.baseURI=xuritibaseurl+"/auth/user-login";
		RequestSpecification  httpRequest=RestAssured.given();
		JSONObject 	requestParams=new JSONObject();
		
		requestParams.put("email",mgrlogeml);
		requestParams.put("password",mgrnlogpss);
		requestParams.put("recaptcha","test_recaptcha");
		httpRequest.header("Content-Type","application/json");
		httpRequest.body(requestParams.toJSONString());
		Response response=httpRequest.request(Method.POST);
		
		//Fetching data of Manager from Response Body from here to
		String msg=response.body().jsonPath().get("message");
		System.out.println("Manager Message is= "+msg+"\n");
		String token=response.body().jsonPath().get("token");
		String mgrid=response.body().jsonPath().get("user._id");
		String mgrnm=response.body().jsonPath().get("user.name");
		String mgreml=response.body().jsonPath().get("user.email");
		String mgrmbl=response.body().jsonPath().get("user.mobile_number");
		String mgrfrstnm=response.body().jsonPath().get("user.first_name");
		String mgrlstnm=response.body().jsonPath().get("user.last_name");
		String mgrrole=response.body().jsonPath().get("user.user_role"); 
		int code=response.body().jsonPath().get("code");		//upto here
		
		//Saving all the Admin usable data in commondata.property file by using setPropertyData() method of FileLib Class
		
		fl.setPropertyData("ManagerMessage", msg);
		fl.setPropertyData("ManagerToken", token);
		fl.setPropertyData("ManagerID", mgrid);
		fl.setPropertyData("ManagerName", mgrnm);
		fl.setPropertyData("ManagerEmail", mgreml);
		fl.setPropertyData("ManagerMobileNumber", mgrmbl);
		fl.setPropertyData("ManagerFirstName", mgrfrstnm);
		fl.setPropertyData("ManagerLastName", mgrlstnm);
		fl.setPropertyData("ManagerRole", mgrrole);
		
		Reporter.log("ManagerLogin");

			//Printing Manager Response Body
		System.out.println(response.prettyPrint());
		Assert.assertEquals(code, 100);
	}
}