package utils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class BaseTest {

    public static RequestSpecification requestSpec;

    public static void setup() {
        RestAssured.baseURI = "https://api.restful-api.dev";
        requestSpec = new RequestSpecBuilder()
                .addHeader("Content-Type", "application/json")
                .build();
        RestAssured.requestSpecification = requestSpec;
    }
}