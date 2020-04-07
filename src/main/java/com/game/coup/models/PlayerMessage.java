package com.game.coup.models;

public class PlayerMessage {

    private String message;
    private String sourcePlayerId;
    private String targetPlayerId;

    public PlayerMessage(String message, String sourcePlayerId, String targetPlayerId) {
        this.message = message;
        this.sourcePlayerId = sourcePlayerId;
        this.targetPlayerId = targetPlayerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSourcePlayerId() {
        return sourcePlayerId;
    }

    public void setSourcePlayerId(String sourcePlayerId) {
        this.sourcePlayerId = sourcePlayerId;
    }

    public String getTargetPlayerId() {
        return targetPlayerId;
    }

    public void setTargetPlayerId(String targetPlayerId) {
        this.targetPlayerId = targetPlayerId;
    }
}
