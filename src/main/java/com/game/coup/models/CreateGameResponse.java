package com.game.coup.models;

import java.util.Map;

public class CreateGameResponse {

     private String gameRoomId;
     private String playerId;
     private Map<String, Map<String, Object>>  playerMap;

     public CreateGameResponse(String gameRoomId, String playerId, Map<String, Map<String, Object>>  playerMap) {
        this.gameRoomId = gameRoomId;
        this.playerId = playerId;
        this.playerMap = playerMap;
     }

    public String getGameRoomId() {
        return gameRoomId;
    }

    public void setGameRoomId(String gameRoomId) {
        this.gameRoomId = gameRoomId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public Map<String, Map<String, Object>>  getPlayerMap() {
        return playerMap;
    }

    public void setPlayerMap(Map<String, Map<String, Object>>  playerMap) {
        this.playerMap = playerMap;
    }
}
