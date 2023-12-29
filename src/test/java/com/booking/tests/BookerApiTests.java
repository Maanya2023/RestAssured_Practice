package com.booking.tests;

import com.booking.utils.ApiUtils;
import com.booking.utils.ConfigLoader;
import com.booking.utils.TestDataStore;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.util.Map;

public class BookerApiTests {


    @BeforeClass
    public void setup() throws ParseException {
        RestAssured.baseURI = ConfigLoader.getEnv("BASE_URL");

        Map<String, String> credentials = ApiUtils.getCredentials();
        Response response = ApiUtils.authenticateAndGetToken(credentials);

        ApiUtils.ensureSuccessfulAuthentication(response);

        String token = ApiUtils.getTokenFromResponse(response);
        TestDataStore.storeData("token",token);
    }

    @Test(priority = 1)
    public void getTheCurrentUser(){

        Response response =  ApiUtils.getCurrentUserdetails(TestDataStore.retrieveData("token"));
        System.out.println("The current User is" + response.asString());

        Assert.assertEquals(response.statusCode(), 200);
        String name = String.valueOf(response.jsonPath().getString("user.username"));
        String bio = String.valueOf(response.jsonPath().getString("user.bio"));
        Assert.assertEquals(name,"Maanya");
        Assert.assertEquals(bio,"Its a saml bio");
        TestDataStore.storeData("name",name);
    }


    @Test
    public void updateTheUser() {
        String payload = ApiUtils.updateUser();
        Response response = ApiUtils.updateUser(payload,TestDataStore.retrieveData("token"));
        System.out.println("The updated user:" + response.asString());
        Assert.assertEquals(response.statusCode(), 200);
        String name = String.valueOf(response.jsonPath().getString("user.username"));
        String bio = String.valueOf(response.jsonPath().getString("user.bio"));
        Assert.assertEquals(name,"Maanya");
        Assert.assertEquals(bio,"Its a saml bio");
    }
    @Test
    public void createAnArticle() {
        String payload = ApiUtils.createAnArticle();
        Response response = ApiUtils.creatingAnArticle(payload,TestDataStore.retrieveData("token"));
        System.out.println("Article:" + response.asString());
        Assert.assertEquals(response.statusCode(), 201);
        String title = String.valueOf(response.jsonPath().getString("article.title"));
        String desc= String.valueOf(response.jsonPath().getString("article.description"));
        Assert.assertEquals(title,"How to train a Parrot");
        Assert.assertEquals(desc,"Ever wonder how?");
    }

    @Test
    public void getAllArticles() {
        Response response = ApiUtils.getAllArticles(TestDataStore.retrieveData("token"));
        System.out.println("Here are all available articles " + response.asString());

        Assert.assertEquals(response.statusCode(), 200);
        String title = String.valueOf(response.jsonPath().getString("articles[0].title"));
        String desc= String.valueOf(response.jsonPath().getString("articles.description"));
        Assert.assertEquals(title,"How to train a Parrot");
        Assert.assertEquals(desc,"Ever wonder how?");
    }

    @Test
    public void getArticlesByAuthor() {
        Response response = ApiUtils.getArticlesByAuthor(TestDataStore.retrieveData("token"));
        System.out.println("Here are all available articles of the author" + response.asString());

        Assert.assertEquals(response.statusCode(), 200);
        String title = String.valueOf(response.jsonPath().getString("articles[0].title"));
        Assert.assertEquals(title,"How to train a Parrot");

    }
    @Test
    public void getArticlesByTag() {
        Response response = ApiUtils.getArticlesByTag();
        System.out.println("Here are all available articles by the tag" + response.asString());

        Assert.assertEquals(response.statusCode(), 200);
        String name = String.valueOf(response.jsonPath().getInt("articles.title"));
        TestDataStore.storeData("How to train your Dog", name);
    }
    @Test
    public void feed() {
        Response response = ApiUtils.getFeed();
        System.out.println("Here are all available articles by the tag" + response.asString());

        Assert.assertEquals(response.statusCode(), 200);
    }
    @Test
    public void getArticlesBySlug() {
        Response response = ApiUtils.getArticlesBySlug();
        System.out.println("Here are all available articles by the tag" + response.asString());

        Assert.assertEquals(response.statusCode(), 200);
    }
    @Test
    public void updateArticles() {
        String payload = ApiUtils.updateArticle();
        Response response = ApiUtils.updateArticle(payload);
        System.out.println("The updated Article:" + response.asString());
        Assert.assertEquals(response.statusCode(), 200);

    }
    @Test
    public void favoriteArticles() {
        Response response = ApiUtils.favoriteArticle();
        System.out.println("The favorite Article:" + response.asString());
        Assert.assertEquals(response.statusCode(), 200);

    }

    @Test
    public void createBookingHappyPath() {
        String payload = ApiUtils.getHappyPathPayload();
        Response response = ApiUtils.postBooking(payload);
        System.out.println("The bookingid is:" + response.asString());

        Assert.assertEquals(response.statusCode(), 200);
       //as per 'bookerapi' documentation 'property:bookingid' is 'integer'
        String bookingId = String.valueOf(response.jsonPath().getInt("bookingid"));
        TestDataStore.storeData("bookingId", bookingId);
    }

    @Test(priority = 2)
    public void getBookingHappyPath() {
        String bookingId = TestDataStore.retrieveData("bookingId");
        Assert.assertNotNull(bookingId, "Booking ID is null!");

        Response response = ApiUtils.getBooking(bookingId);
        System.out.println("The getbooking details:" + response.asString());

        Assert.assertEquals(response.statusCode(), 200); //as per 'bookerapi' documentation
    }

    @Test(priority = 3)
    public void updateBookingHappyPath() {
        String bookingId = TestDataStore.retrieveData("bookingId");
        String token = TestDataStore.retrieveData("token");

        Assert.assertNotNull(bookingId, "Booking ID is null!");
        Assert.assertNotNull(token, "Token is null!");

        Response response = ApiUtils.updateBooking(bookingId, token);
        System.out.println("The updatedbooking details:" + response.asString());

        Assert.assertEquals(response.statusCode(), 200); //as per 'bookerapi' documentation
    }

    @Test(priority = 4)
    public void partialUpdateBookingHappyPath() {
        String bookingId = TestDataStore.retrieveData("bookingId");
        String token = TestDataStore.retrieveData("token");

        Assert.assertNotNull(bookingId, "Booking ID is null!");
        Assert.assertNotNull(token, "Token is null!");

        Response response = ApiUtils.partialUpdateBooking(bookingId, token);
        System.out.println("The partiallyupdatedbooking details:" + response.asString());

        Assert.assertEquals(response.statusCode(), 200); //as per 'bookerapi' documentation
    }

    @Test(priority = 5)
    public void deleteBookingHappyPath() {
        String bookingId = TestDataStore.retrieveData("bookingId");
        String token = TestDataStore.retrieveData("token");

        Assert.assertNotNull(bookingId, "Booking ID is null!");
        Assert.assertNotNull(token, "Token is null!");

        Response response = ApiUtils.deleteBooking(bookingId, token);
        System.out.println("The booking details deleted:" + response.asString());

        Assert.assertEquals(response.statusCode(), 201); //as per 'bookerapi' documentation
    }

    @Test(priority = 6)
    public void createBookingNegativePath() {
        String payload = ApiUtils.getIncompletePayload();
        Response response = ApiUtils.postBooking(payload);
        System.out.println("The invalidcreatebooking details:" + response.asString());

        Assert.assertNotEquals(response.statusCode(), 200);
    }

    @Test(priority = 7)
    public void getBookingNegativePath() {
        String nonExistentBookingId = "999999";
        Response response = ApiUtils.getBooking(nonExistentBookingId);
        System.out.println("The invalidgetbooking details:" + response.asString());

        Assert.assertNotEquals(response.statusCode(), 200);
    }

    @Test(priority = 8)
    public void deleteBookingNegativePath() {
        String nonExistentBookingId = "999999";
        String token = TestDataStore.retrieveData("token");
        Assert.assertNotNull(token, "Token is null!");

        Response response = ApiUtils.deleteBooking(nonExistentBookingId, token);
        System.out.println("The delete invalid booking details:" + response.asString());

        Assert.assertNotEquals(response.statusCode(), 201);
    }

    @AfterClass
    public void clearData() {
        TestDataStore.cleanUp();
        Assert.assertEquals(TestDataStore.cleanUp(), 0, "TestDataStore should be empty after cleanup");
    }

}
