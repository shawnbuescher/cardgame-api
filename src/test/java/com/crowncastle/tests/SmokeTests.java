package com.crowncastle.tests;

import com.crowncastle.common.Constants;
import com.crowncastle.common.DeckOfCards;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class SmokeTests {
    @Test
    public void getNewDeck() {
        given().
                when().get(Constants.BASEURI + "/api/deck/new/").
                then().assertThat().statusCode(200).
                and().assertThat().body("shuffled", is(false));
    }

    @Test
    public void getShuffled() {
        given().
                when().get(Constants.BASEURI + "/api/deck/new/shuffle/?deck_count=1").
                then().assertThat().statusCode(200).
                and().assertThat().body("shuffled", is(true));
    }
    @Test
    public void getDeckAndShuffle() {
        String deckId = DeckOfCards.newDeck();
        System.out.println("New Deck Id is: " + deckId);

        given()
                .pathParam("deckId", deckId)
                .when()
                .get(Constants.BASEURI + "/api/deck/{deckId}/shuffle/")
                .then().assertThat().body("shuffled", is(true));
    }

    @Test
    public void getDrawCard() {
        String deckId = DeckOfCards.newDeck();
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