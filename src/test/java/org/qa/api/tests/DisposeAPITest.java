package org.qa.api.tests;

import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class DisposeAPITest {


    Playwright playwright;
    APIRequest request;
    APIRequestContext requestContext;

    @BeforeTest
    public void setup(){
        playwright = Playwright.create();
        request= playwright.request();
        requestContext = request.newContext();
    }


    @Test
    public void DisposeResponseTest(){

        APIResponse apiResponse = requestContext.get("https://gorest.co.in/public/v2/users");

        //Validation for Status Code
        int statusCode = apiResponse.status();
        System.out.println("The Status code is: "+statusCode);
        Assert.assertEquals(statusCode,200);

        //Validation for Status Text
        String apiResponseText = apiResponse.text();
        System.out.println("The Status text is: "+apiResponseText);
        System.out.println(apiResponseText);


        apiResponse.dispose();

        //Validation for Status Code
        int statusCodeAfterDispose = apiResponse.status();
        System.out.println("The Status code is: "+statusCodeAfterDispose);
        Assert.assertEquals(statusCode,200);

        //Validation for Text
        try {
            String apiResponseTextAfterDispose = apiResponse.text();
            System.out.println("The text is: " + apiResponseTextAfterDispose);
        }
        catch(PlaywrightException e ){
            System.out.println("API response Body is already disposed");
        }
    }


    @Test
    public void DisposeContextTest(){

        //Response-1
        APIResponse apiResponse = requestContext.get("https://gorest.co.in/public/v2/users");

        //Validation for Status Code
        int statusCode = apiResponse.status();
        System.out.println("The Status code is: "+statusCode);
        Assert.assertEquals(statusCode,200);

        //Response-2
        APIResponse apiResponse2 = requestContext.get("https://reqres.in/api/users/2");

        //Validation for Status Code
        int statusCode2 = apiResponse2.status();
        System.out.println("The Status code is: "+statusCode2);
        Assert.assertEquals(statusCode2,200);


        //Request Context Dispose
        requestContext.dispose();
        System.out.println("Response1 Body : " +apiResponse.text()); //should throw an exception
        System.out.println("Response2 Body : " +apiResponse2.text()); //should throw an exception

    }



    @AfterTest
    public void tearDown(){
        playwright.close();
    }
}
