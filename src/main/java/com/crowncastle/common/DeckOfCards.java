package com.crowncastle.common;

import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class DeckOfCards {

    public static String newDeck() {
        try {
            Response responseNewDeck = given().when().get(Constants.BASEURI + "/api/deck/new/");

            return responseNewDeck.path("deck_id");
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return "Error";
        }
    }

    public static void shuffleDeck(String deckId) {
        try {
            given()
                    .pathParam("deckId", deckId)
                    .when()
                    .get(Constants.BASEURI + "/api/deck/{deckId}/shuffle/")
                    .then().assertThat().body("shuffled", is(true));

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());

        }
    }

    public static String drawCards(String deckId, int count) {
        try {
            Response drawnCards = given()
                    .pathParam("deckId", deckId)
                    .pathParam("count", count)
                    .when()
                    .get(Constants.BASEURI + "/api/deck/{deckId}/draw/?count={count}");

            List<String> codes = drawnCards.path("cards.code");

            return String.join(",", codes);

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return "Error";

        }
    }

    public static boolean checkSum(String cards) {
        String[] cardArray = cards.split(",");
        int sumOfCards = 0;
        int numAces = 0;
        for (String card : cardArray) {
            int value = getCardValue(card);
            sumOfCards += value;
            if (value == 11) {
                numAces++;
            }
        }

        // If the sum of the cards is equal to 21, then return true
        if (sumOfCards == 21) {
            return true;
        }

        // If there are aces, then try setting the value of one of the aces to 1
        if (numAces > 0) {
            for (int i = 0; i < numAces; i++) {
                sumOfCards -= 10;
                if (sumOfCards == 21) {
                    return true;
                }
            }
        }

        return false;
    }

    private static int getCardValue(String card) {
        int value;
        if (card.startsWith("2")) {
            value = 2;
        } else if (card.startsWith("3")) {
            value = 3;
        } else if (card.startsWith("4")) {
            value = 4;
        } else if (card.startsWith("5")) {
            value = 5;
        } else if (card.startsWith("6")) {
            value = 6;
        } else if (card.startsWith("7")) {
            value = 7;
        } else if (card.startsWith("8")) {
            value = 8;
        } else if (card.startsWith("9")) {
            value = 9;
        } else if (card.startsWith("0") || card.startsWith("J") || card.startsWith("Q") || card.startsWith("K")) {
            value = 10;
        } else if (card.startsWith("A")) {
            value = 11; // Ace can have a value of 11 or 1
        } else {
            throw new IllegalArgumentException("Invalid card code: " + card);
        }

        return value;
    }
}
