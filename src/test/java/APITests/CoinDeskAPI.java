package APITests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

public class CoinDeskAPI {

    private String response;

    // Setting base URI before running the tests
    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://api.coindesk.com/";
        // Making the GET request and storing the response
        response = RestAssured.given().log().all()
                .when().get("v1/bpi/currentprice.json")
                .then().assertThat().log().all().statusCode(200).extract().response().asString();
    }

    // Test method to check GBP description
    @Test
    public void testGBPDescription() {
    	
    	JsonPath js = ReusableMethods.rawToJson(response);
       
        String actualGBPValue = js.getString("bpi.GBP.description");
        System.out.println("GBP description is: " + actualGBPValue);

        String expectedGBPDescription = "British Pound Sterling";

        // Assertion for GBP description
        Assert.assertEquals(actualGBPValue, expectedGBPDescription, "GBP Description mismatch!");
    }

    // Test method to check the number of BPI entries
    @Test
    public void testBPIEntriesSize() {
    	JsonPath js = ReusableMethods.rawToJson(response);
        int bpiSize = js.getMap("bpi").size();
        System.out.println("Number of BPI entries: " + bpiSize);

        // Assert that there are 3 entries in the 'bpi' section
        Assert.assertEquals(bpiSize, 3, "Expected 3 BPI entries!");
    }

    // Test method to print and check the currency details
    @Test
    public void testCurrencyDetails() {
    	JsonPath js = ReusableMethods.rawToJson(response);
        Map<String, Object> bpiMap = js.getMap("bpi");

        System.out.println("BPI value: " + bpiMap);

        for (Map.Entry<String, Object> entry : bpiMap.entrySet()) {
            String currencyCode = entry.getKey();
            Map<String, Object> currencyDetails = (Map<String, Object>) entry.getValue();

            String rate = (String) currencyDetails.get("rate");
            String description = (String) currencyDetails.get("description");

            // Print the currency details
            System.out.println(currencyCode + " Rate: " + rate);
            System.out.println(currencyCode + " Description: " + description);

            // Specifically to print GBP details
            if (currencyCode.equals("GBP")) {
                System.out.println("GBP Details: Rate - " + rate + ", Description - " + description);
            }
        }
    }

}