package com.game.coup.service;

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

    public String createGameRoom() {
        String gameRoomId = RandomString.getRandomString();
        Map<String, Object> gameInfoMap= new HashMap<>();
        gameInfoMap.put("cardDeck", this.newCardDeck());
        gameInfoMap.put("players", new HashMap<String, Map<String, Object>>());
        this.gameRooms.put(gameRoomId, gameInfoMap);
        return gameRoomId;
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

    private Map<String, Object> initializePlayerAttributes(String playerName) {
        Map<String, Object> playerAttributeMap = new HashMap<>();
        playerAttributeMap.put("name", playerName);
        playerAttributeMap.put("coins", 0);
        playerAttributeMap.put("cards", new ArrayList<Integer>());
        playerAttributeMap.put("lives", 2);
        return playerAttributeMap;
    }

    public String joinRoom(String gameRoomId, String playerName) {
        @SuppressWarnings("unchecked")
        Map<String, Map<String, Object>> playerMap = (Map<String, Map<String, Object>>) this.gameRooms.get(gameRoomId).get("players");
        String playerId = RandomString.getRandomString();
        playerMap.put(playerId, this.initializePlayerAttributes(playerName));
        return playerId;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getPlayer(Map<String, Object> gameRoom, String playerId) {
        return ((Map<String, Map<String, Object>>) (gameRoom.get("players"))).get(playerId);
    }

    @SuppressWarnings("unchecked")
    public List<Integer> takeCards(String gameRoomId, String playerId, Integer numCards) {
        Map<String, Object> gameRoom = this.gameRooms.get(gameRoomId);
        List<Integer> cardDeck = (List<Integer>) gameRoom.get("cardDeck");
        List<Integer> returnCards = new ArrayList<>();
        for (int i = 0; i < numCards; i++) {
            returnCards.add(cardDeck.remove(0));
        }

        ((List<Integer>) this.getPlayer(gameRoom, playerId).get("cards")).addAll(returnCards);
        return returnCards;
    }

    @SuppressWarnings("unchecked")
    public void returnCards(String gameRoomId, String playerId, List<Integer> cards) {
        Map<String, Object> gameRoom = this.gameRooms.get(gameRoomId);
        List<Integer> cardDeck = (List<Integer>) gameRoom.get("cardDeck");;
        cardDeck.addAll(cards);
        Collections.shuffle(cardDeck);

        ((List<Integer>) this.getPlayer(gameRoom, playerId).get("cards")).removeAll(cards);
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

    public void stealCoins(String gameRoomId, String playerId, String targetPlayerId, Integer amount) {
        this.takeCoins(gameRoomId, playerId, amount);
        this.depositCoins(gameRoomId, targetPlayerId, amount);
    }

    public void loseLife(Map<String, Object> gameRoom, String playerId) {
        Integer playerLives = ((Integer) this.getPlayer(gameRoom, playerId).get("lives"));
        playerLives--;
        this.getPlayer(gameRoom, playerId).put("lives", playerLives);
    }

    public void killPlayer(String gameRoomId, String playerId, String targetPlayerId, Integer killCost) {
        Map<String, Object> gameRoom = this.gameRooms.get(gameRoomId);
        this.depositCoins(gameRoomId, playerId, killCost);
        this.loseLife(gameRoom, targetPlayerId);
    }
}
