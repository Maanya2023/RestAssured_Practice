package com.booking.utils;

import com.jayway.jsonpath.JsonPath;
import org.json.JSONTokener;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ApiUtils {
    // Utility methods for beforeclass setup
    public static Map<String, String> getCredentials() {
        Properties props = new Properties();

        try (InputStream input = ApiUtils.class.getResourceAsStream("/config.properties")) {
            if (input == null) {
                throw new RuntimeException("Failed to find config.properties on the classpath");
            }
            props.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to load the config.properties file");
        }

        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", props.getProperty("username"));
        credentials.put("password", props.getProperty("password"));
        return credentials;
    }


    public static Response authenticateAndGetToken(Map<String, String> credentials) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonBody=(JSONObject) parser.parse("{\"user\": {\"email\": \"fareena3@gmail.com\",\"password\": \"FF@143ff\"}}");
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .post("/users/login");
    }

    public static void ensureSuccessfulAuthentication(Response response) {
        if (response.getStatusCode() != 200) {
            throw new RuntimeException("Failed to authenticate and obtain the token.");
        }
    }

    public static String getTokenFromResponse(Response response) {
        return response.jsonPath().getString("user.token");
    }

    // Utility methods for constructing payloads
    public static String getHappyPathPayload() {
        return "{"
                + "\"firstname\": \"Ruuhi\","
                + "\"lastname\": \"Wahii\","
                + "\"totalprice\": 111,"
                + "\"depositpaid\": true,"
                + "\"bookingdates\": {\"checkin\": \"2023-08-15\",\"checkout\": \"2023-08-20\"},"
                + "\"additionalneeds\": \"Breakfast\""
                + "}";
    }
      public static String updateUser() {
        return ("{\"user\": {\"user\": \"fareena3@gmail.com\",\"username\":\"Maanya\"}}");
    }

    public static String updateArticle() {
        return ("{\"article\":{\"body\":\"With instructions\"}}");
    }

    public static String addComment() {
        return ("{\"comment\":{\"body\":\"Thank you so much!\"}}");
    }

    public static String createAnArticle() {
        return ("{\"article\":{\"title\":\"How to train a Parrot\", \"description\":\"Ever wonder how?\", \"body\":\"Very carefully.\", \"tagList\":[\"training\", \"cat\"]}}");
    }
    public static String getUpdatedPayload() {
        return "{"
                + "\"firstname\": \"UpdatedName\","
                + "\"lastname\": \"UpdatedSurname\","
                + "\"totalprice\": 123,"
                + "\"depositpaid\": true,"
                + "\"bookingdates\": {\"checkin\": \"2023-08-15\",\"checkout\": \"2023-08-20\"},"
                + "\"additionalneeds\": \"Breakfast\""
                + "}";
    }

    public static String getPartialUpdatePayload() {
        return "{"
                + "\"firstname\": \"PartialUpdateName\""
                + "}";
    }

    public static String getIncompletePayload() {
        return "{"
                + "\"firstname\": \"Jim\""
                + "}";
    }
    public static Response getCurrentUserdetails(String token) {
        return RestAssured
                .given()
                .header("Authorization","Bearer "+token)
                .contentType(ContentType.JSON) // Sets "Content-Type: application/json"
                .get("/user");
    }
    public static Response getAllArticles(String token) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON) // Sets "Content-Type: application/json"
                .header("Authorization","Bearer "+token)
                .get("/" + "articles");
    }


    public static Response getArticlesByAuthor(String token) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON) // Sets "Content-Type: application/json"
                .header("Authorization","Bearer "+token)
                .get("/" + "articles?author=Maanya");
    }
    public static Response getArticlesByTag(String token) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON) // Sets "Content-Type: application/json"
                .header("Authorization","Bearer "+token)
                .get("/" + "articles?tag=ipsum");
    }

    public static Response getFeed(String token) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON) // Sets "Content-Type: application/json"
                .header("Authorization","Bearer "+token)
                .get("/" + "articles/feed");
    }
    public static Response getArticlesBySlug(String token) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON) // Sets "Content-Type: application/json"
                .header("Authorization","Bearer "+token)
                .get("/" + "articles/How-to-train-a-Dog-465654");
    }

    // Utility methods for making API requests
    public static Response postBooking(String payload) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON) // Sets "Content-Type: application/json"
                .header("Accept", "application/json")  // Optionally set "Accept" header
                .body(payload)
                .post("/" + "user");
    }
    public static Response updateUser(String payload,String token) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON) // Sets "Content-Type: application/json"
                .header("Authorization","Bearer "+token)
                .header("Accept", "application/json")// Optionally set "Accept" header
                .body(payload)
                .put("/" + "user");
    }
    public static Response creatingAnArticle(String payload,String token) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON) // Sets "Content-Type: application/json"
                .header("Authorization","Bearer "+token)
                .body(payload)
                .post("/" + "articles");
    }
    public static Response updateArticles(String payload,String token) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON) // Sets "Content-Type: application/json"
                .header("Authorization","Bearer "+token)
                .body(payload)
                .put("/" + "articles/How-to-train-a-Dog-465654");
    }
    public static Response favoriteArticle(String token) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON) // Sets "Content-Type: application/json"
                .header("Authorization","Bearer "+token)
                .post("/" + "articles/How-to-train-a-Dog-465654/favorite");
    }
    public static Response articlesFavByUser(String token) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON) // Sets "Content-Type: application/json"
                .header("Authorization","Bearer "+token)
                .get("/" + "articles?favorited=Maanya");
    }
    public static Response unFavArticles(String token) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON) // Sets "Content-Type: application/json"
                .header("Authorization","Bearer "+token)
                .delete("/" + "articles/How-to-training-your-parrot-463796/favorite");
    }
    public static Response addComments(String payload,String token) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON) // Sets "Content-Type: application/json"
                .header("Authorization","Bearer "+token)
                .body(payload)
                .post("/" + "articles/How-to-train-a-Dog-465654/comments");
    }
    public static Response getAllCommentsOfArticle(String token) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON) // Sets "Content-Type: application/json"
                .header("Authorization","Bearer "+token)
                .get("/" + "articles/How-to-train-a-Dog-465654/comments");
    }
    public static Response getBooking(String bookingId) {
        return RestAssured
                .given()
                .header("Accept", "application/json")  // Optionally set "Accept" header
                .get("/" + "booking" + "/" +  bookingId);
    }

    public static Response updateBooking(String bookingId, String token) {
        String updatedPayload = getUpdatedPayload();

        return RestAssured
                .given()
                .header("Cookie", "token=" + token) // added token as header
                .header("Accept", "application/json")  // Optionally set "Accept" header
                .contentType(ContentType.JSON)
                .body(updatedPayload)
                .put("/" + "booking" + "/" + bookingId);
    }

    public static Response partialUpdateBooking(String bookingId, String token) {
        String partialPayload = getPartialUpdatePayload();

        return RestAssured
                .given()
                .header("Cookie", "token=" + token) // added token as header
                .header("Accept", "application/json")  // Optionally set "Accept" header
                .contentType(ContentType.JSON)
                .body(partialPayload)
                .patch("/" + "booking" + "/" +bookingId);
    }


    public static Response deleteBooking(String bookingId, String token) {
        return RestAssured
                .given()
                .header("Cookie", "token=" + token) // added token as header
                .header("Accept", "application/json")  // Optionally set "Accept" header
                .delete("/" + "booking" + "/" + bookingId);
    }
}
