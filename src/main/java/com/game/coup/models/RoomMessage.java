package com.game.coup.models;

public class RoomMessage {

    private String message;
    private String playerId;

    public RoomMessage(String message, String playerId) {
        this.message = message;
        this.playerId = playerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
}
