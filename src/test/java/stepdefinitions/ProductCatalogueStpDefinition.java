package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.BaseTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.*;

public class ProductCatalogueStpDefinition {

    private Response response;

    private Map<String, Object> payload;

    private String generatedId;

    @Given("the API base URI is set")
    public void setBaseUri() {
        BaseTest.setup();
    }

    @When("I send a GET request to {string}")
    public void sendGetRequest(String endpoint) {
        response = given().log().all()
                .when()
                .get(endpoint)
                .then().log().all()
                .extract()
                .response();
        response.prettyPrint();
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer statusCode) {
        assertEquals(statusCode.intValue(), response.getStatusCode());
        System.out.println("Response Body:");
        response.prettyPrint();
    }

    @Then("the response should contain name as {string}")
    public void the_response_should_contain_name_as(String expectedValue) {
        assertTrue(response.getBody().asString().contains(expectedValue));
    }

    @Then("Google Pixel 6 Pro should have colour {string}")
    public void validate_google_pixel_colour_using_java(String expectedColour) {
        List<Map<String, Object>> responseArray = response.jsonPath().getList("");
        String actualColour = null;
        for (Map<String, Object> obj : responseArray) {
            String name = (String) obj.get("name");
            if ("Google Pixel 6 Pro".equals(name)) {
                Map<String, Object> data = (Map<String, Object>) obj.get("data");
                if (data != null) {
                    actualColour = (String) data.get("color");
                }
                break;
            }
        }
        System.out.println("Expected Colour: " + expectedColour);
        System.out.println("Actual Colour: " + actualColour);

        assertNotNull(actualColour, "Google Pixel 6 Pro not found in response!");
        assertEquals(actualColour, expectedColour);
    }

    @Given("the product payload is:")
    public void the_product_payload_is(DataTable dataTable) {
        payload = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();

        List<List<String>> rows = dataTable.asLists(String.class);
        List<String> keys = rows.get(0);
        List<String> values = rows.get(1);

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i).trim();
            String value = values.get(i).trim();

            if (key.equalsIgnoreCase("name")) {
                payload.put(key, parseValue(value));
            } else {
                dataMap.put(key, parseValue(value));
            }
        }

        if (!dataMap.isEmpty()) {
            payload.put("data", dataMap);
        }
    }

    private Object parseValue(String value) {
        try {
            if (value.contains(".")) return Double.parseDouble(value);
            else return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return value;
        }
    }

    @When("I send a POST request to {string}")
    public void i_send_a_post_request_to(String endpoint) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post(endpoint);

        generatedId = response.jsonPath().getString("id");
    }


    @When("I send a DELETE request for the created object")
    public void i_send_a_delete_request_to() {
        response = RestAssured.given()
                .delete("/objects/" + generatedId);
        response.prettyPrint();
    }

    @Then("the product {string} should exist in the response")
    public void validate_product_exists(String productName) {
        List<Map<String, Object>> responseArray = response.jsonPath().getList("");
        boolean productFound = false;
        for (Map<String, Object> obj : responseArray) {
            String name = (String) obj.get("name");
            if (productName.equals(name)) {
                productFound = true;
                break;
            }
        }
        assertFalse(productFound,
                "Validation Failed: Product '" + productName + "' was NOT found in the response.");
    }
}