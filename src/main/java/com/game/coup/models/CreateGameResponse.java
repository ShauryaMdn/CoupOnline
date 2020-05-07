package com.game.coup.models;

import java.util.List;
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

     private void appendPlayerAttributes(StringBuilder stringBuilder, Map<String, Object> playerAttributes) {
         stringBuilder.append("{");
         String prefix = "";
         for (Map.Entry<String, Object> keyValuePair : playerAttributes.entrySet()) {
             stringBuilder.append(prefix);
             prefix = ",";
             stringBuilder.append("\"").append(keyValuePair.getKey()).append("\":");
             if (keyValuePair.getValue() instanceof String) {
                 stringBuilder.append("\"").append(keyValuePair.getValue()).append("\"");
             }
             else if (keyValuePair.getValue() instanceof Integer) {
                 stringBuilder.append(keyValuePair.getValue());
             }
             else if (keyValuePair.getValue() instanceof List<?>) {
                stringBuilder.append(keyValuePair.getValue().toString());
             }
         }
         stringBuilder.append("}");
     }

     @Override
     public String toString() {
         StringBuilder stringBuilder = new StringBuilder();
         stringBuilder.append("{");
         stringBuilder.append("\"gameRoomId\":");
         stringBuilder.append("\"").append(this.getGameRoomId()).append("\",");
         stringBuilder.append("\"playerId\":");
         stringBuilder.append("\"").append(this.getPlayerId()).append("\",");
         stringBuilder.append("\"playerMap\":").append("{");
         String prefix = "";
         for (Map.Entry<String, Map<String, Object>> keyValuePair : this.getPlayerMap().entrySet()) {
             stringBuilder.append(prefix);
             prefix = ",";
             stringBuilder.append("\"").append(keyValuePair.getKey()).append("\":");
             this.appendPlayerAttributes(stringBuilder, keyValuePair.getValue());
         }
         stringBuilder.append("}").append("}");
         return stringBuilder.toString();
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
