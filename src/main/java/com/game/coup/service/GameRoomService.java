package com.game.coup.service;

import com.game.coup.models.CreateGameResponse;
import com.game.coup.util.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameRoomService {
    Map<String, Map<String, Object>> gameRooms;

    @Autowired
    public GameRoomService() {
        this.gameRooms = new HashMap<>();
    }

    public CreateGameResponse createGameRoom(String playerName) {
        String gameRoomId = RandomString.getRandomString();
        Map<String, Object> gameInfoMap= new HashMap<>();
        gameInfoMap.put("cardDeck", this.newCardDeck());
        gameInfoMap.put("players", new HashMap<String, Map<String, Object>>());
        this.gameRooms.put(gameRoomId, gameInfoMap);
        return new CreateGameResponse(gameRoomId, this.addPlayer(gameRoomId, playerName), this.getPlayerMap(gameRoomId));
    }

    private List<Integer> newCardDeck() {
        List<Integer> cardDeck = new ArrayList<>();
        for (Integer i = 1; i <= 5; i++) {
            for (int k = 0; k < 4; k++) {
                cardDeck.add(i);
            }
        }
        Collections.shuffle(cardDeck);
        return cardDeck;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Map<String, Object>> getPlayerMap(String gameRoomId) {
        return (Map<String, Map<String, Object>>) this.gameRooms.get(gameRoomId).get("players");
    }

    private Map<String, Object> initializePlayerAttributes(String playerName) {
        Map<String, Object> playerAttributeMap = new HashMap<>();
        playerAttributeMap.put("name", playerName);
        playerAttributeMap.put("coins", 0);
        playerAttributeMap.put("liveCards", new ArrayList<Integer>());
        playerAttributeMap.put("deadCards", new ArrayList<Integer>());
        playerAttributeMap.put("lives", 2);
        return playerAttributeMap;
    }

    public String addPlayer(String gameRoomId, String playerName) {
        Map<String, Map<String, Object>> playerMap = this.getPlayerMap(gameRoomId);
        String playerId = RandomString.getRandomString();
        playerMap.put(playerId, this.initializePlayerAttributes(playerName));
        this.takeCoins(gameRoomId, playerId, 2);
        this.drawCards(gameRoomId, playerId, 2);
        return playerId;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getPlayer(Map<String, Object> gameRoom, String playerId) {
        return ((Map<String, Map<String, Object>>) (gameRoom.get("players"))).get(playerId);
    }

    @SuppressWarnings("unchecked")
    public void drawCards(String gameRoomId, String playerId, Integer numCards) {
        Map<String, Object> gameRoom = this.gameRooms.get(gameRoomId);
        List<Integer> cardDeck = (List<Integer>) gameRoom.get("cardDeck");
        List<Integer> returnCards = new ArrayList<>();
        for (int i = 0; i < numCards; i++) {
            returnCards.add(cardDeck.remove(0));
        }

        ((List<Integer>) this.getPlayer(gameRoom, playerId).get("liveCards")).addAll(returnCards);
    }

    @SuppressWarnings("unchecked")
    public void returnCards(String gameRoomId, String playerId, List<Integer> cards) {
        Map<String, Object> gameRoom = this.gameRooms.get(gameRoomId);
        List<Integer> cardDeck = (List<Integer>) gameRoom.get("cardDeck");;
        cardDeck.addAll(cards);
        Collections.shuffle(cardDeck);

        // Doing it this way because if the player has duplicate cards removeAll removes all of them
        List<Integer> playerCards = ((List<Integer>) this.getPlayer(gameRoom, playerId).get("liveCards"));
        for (Integer card : cards) {
            playerCards.remove(card);
        }
    }

    public void takeCoins(String gameRoomId, String playerId, Integer amount) {
        Map<String, Object> gameRoom = this.gameRooms.get(gameRoomId);
        Integer playerCoins = ((Integer) this.getPlayer(gameRoom, playerId).get("coins"));
        playerCoins += amount;
        this.getPlayer(gameRoom, playerId).put("coins", playerCoins);
    }

    public void depositCoins(String gameRoomId, String playerId, Integer amount) {
        Map<String, Object> gameRoom = this.gameRooms.get(gameRoomId);
        Integer playerCoins = ((Integer) this.getPlayer(gameRoom, playerId).get("coins"));
        playerCoins -= amount;
        this.getPlayer(gameRoom, playerId).put("coins", playerCoins);
    }

    @SuppressWarnings("unchecked")
    public void loseLife(String gameRoomId, String playerId, Integer card) {
        Map<String, Object> player = this.getPlayer(this.gameRooms.get(gameRoomId), playerId);
        Integer playerLives = ((Integer) player.get("lives"));
        playerLives--;
        player.put("lives", playerLives);
        ((List<Integer>) player.get("liveCards")).remove(card);
        ((List<Integer>) player.get("deadCards")).add(card);
    }
}
