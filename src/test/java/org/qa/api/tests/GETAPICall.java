package org.qa.api.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

public class GETAPICall {

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
    public void passQueryParametersAPITest() throws IOException{

        APIResponse apiResponse = requestContext.get("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                    .setQueryParam("gender", "female")
                    .setQueryParam("status", "active"));

        //Validation for Status Code
        int statusCode = apiResponse.status();
        System.out.println("The Status code is: "+statusCode);
        Assert.assertEquals(statusCode,200);
        Assert.assertEquals(apiResponse.ok(), true);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(apiResponse.body());
        String jsonPrettyResponse = jsonResponse.toPrettyString();
        System.out.println("The Response Body is: " +jsonPrettyResponse);
    }



    @Test
    public void getUsersAPITest() throws IOException {

        APIResponse apiResponse = requestContext.get("https://gorest.co.in/public/v2/users");

        //Validation for Status Code
        int statusCode = apiResponse.status();
        System.out.println("The Status code is: "+statusCode);
        Assert.assertEquals(statusCode,200);

        //Validation for Status Text
        String statusResponseText = apiResponse.statusText();
        System.out.println("The Status text is: "+statusResponseText);
        Assert.assertEquals(statusResponseText,"OK");

        //Retrieving the Response Body
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(apiResponse.body());
        String jsonPrettyResponse = jsonResponse.toPrettyString();
        System.out.println("The Response Body is: " +jsonPrettyResponse);


        //Retrieving the Headers
        Map<String, String> headersMap = apiResponse.headers();
        System.out.println(headersMap);
        Assert.assertEquals(headersMap.get("content-type"), "application/json; charset=utf-8");

    }

    @AfterTest
    public void tearDown(){
        playwright.close();
    }

}
