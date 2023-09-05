package com.crowncastle.tests;

import static io.restassured.RestAssured.given;

import com.crowncastle.common.Constants;

import io.restassured.response.Response;
import org.testng.annotations.Test;

public class SmokeTests {
    @Test
    public void getNewDeck() {
        given().
                when().get(Constants.BASEURI + "/api/deck/new/").
                then().assertThat().statusCode(200);
    }

    @Test
    public void getShuffle() {
        given().
                when().get(Constants.BASEURI +"/api/deck/new/shuffle/?deck_count=1").
                then().assertThat().statusCode(200);
    }

    @Test
    public void getDrawCard() {
        Response responseNewDeck = given().when().get(Constants.BASEURI + "/api/deck/new/");

        String deckId = responseNewDeck.path("deck_id");

        System.out.println("New Deck Id is: " + deckId);

        given()
                .pathParam("deckId", deckId)
                .when()
                .get(Constants.BASEURI + "/api/deck/{deckId}/draw/?count=1")
                .then().assertThat().statusCode(200);
    }

    @Test
    public void get404() {
        given().
                when().get(Constants.BASEURI + "/api/deck/new/crowncastle").
                then().assertThat().statusCode(404);
    }

    @Test
    public void getDrawCard404() {
        given()
                .pathParam("deckId", "12345")
                .when()
                .get(Constants.BASEURI + "/api/deck/{deckId}/draw/?count=5")
                .then().assertThat().statusCode(404);
    }
}