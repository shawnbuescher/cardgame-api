package com.crowncastle.tests;

import com.crowncastle.common.Constants;
import com.crowncastle.common.DeckOfCards;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class IntegrationTests {

    @Test
    public void blackJack() {

        //Get a new deck and store deck id
        String deckId = DeckOfCards.newDeck();
        System.out.println("New Deck Id is: " + deckId);

        //Shuffle deck
        DeckOfCards.shuffleDeck(deckId);

        //Deal three cards to each of two players
        //Create two piles, named player 1 and player 2, and add 3 drawn cards to them

        String player1CardList= DeckOfCards.drawCards(deckId,3);
        String player2CardList= DeckOfCards.drawCards(deckId,3);

        Response creatPile1Response = given()
                .log().all()
                .pathParam("deckId", deckId)
                .pathParam("cardList", player1CardList)
                .when()
                .get(Constants.BASEURI + "/api/deck/{deckId}/pile/Player1/add/?cards={cardList}");

        creatPile1Response.body().prettyPrint();

        Response creatPile2Response = given()
                .log().all()
                .pathParam("deckId", deckId)
                .pathParam("cardList", player2CardList)
                .when()
                .get(Constants.BASEURI + "/api/deck/{deckId}/pile/Player2/add/?cards={cardList}");

        creatPile2Response.body().prettyPrint();

        if(DeckOfCards.checkSum(player1CardList)){
            System.out.println("Player 1 has Black Jack!");
            System.out.println("Their cards were: " + player1CardList);
        }
        if(DeckOfCards.checkSum(player2CardList)){
            System.out.println("Player 2 has Black Jack!");
            System.out.println("Their cards were: " + player2CardList);
        }

    }



}
