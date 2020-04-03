package com.game.coup.controller;

import com.game.coup.service.GameRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PlayerSateController {

    private final GameRoomService gameRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public PlayerSateController(GameRoomService gameRoomService, SimpMessagingTemplate messagingTemplate) {
        this.gameRoomService = gameRoomService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/cards/draw/{gameRoomId}/{playerId}")
    public void drawCards(@DestinationVariable String gameRoomId, @DestinationVariable String playerId, @Payload Integer amount) {
        this.gameRoomService.drawCards(gameRoomId, playerId, amount);
        this.messagingTemplate.convertAndSend(String.format("/topic/playerMap/%s", gameRoomId), this.gameRoomService.getPlayerMap(gameRoomId));
    }

    @MessageMapping("cards/return/{gameRoomId}/{playerId}")
    public void returnCards(@DestinationVariable String gameRoomId, @DestinationVariable String playerId, @Payload List<Integer> returnedCards) {
        this.gameRoomService.returnCards(gameRoomId, playerId, returnedCards);
        this.messagingTemplate.convertAndSend(String.format("/topic/playerMap/%s", gameRoomId), this.gameRoomService.getPlayerMap(gameRoomId));
    }

    @MessageMapping("/coins/take/{gameRoomId}/{playerId}")
    public void takeCoins(@DestinationVariable String gameRoomId, @DestinationVariable String playerId, @Payload Integer amount) {
        this.gameRoomService.takeCoins(gameRoomId, playerId, amount);
        this.messagingTemplate.convertAndSend(String.format("/topic/playerMap/%s", gameRoomId), this.gameRoomService.getPlayerMap(gameRoomId));
    }

    @MessageMapping("coins/deposit/{gameRoomId}/{playerId}")
    public void depositCoins(@DestinationVariable String gameRoomId, @DestinationVariable String playerId, @Payload Integer amount) {
        this.gameRoomService.depositCoins(gameRoomId, playerId, amount);
        this.messagingTemplate.convertAndSend(String.format("/topic/playerMap/%s", gameRoomId), this.gameRoomService.getPlayerMap(gameRoomId));
    }

    @MessageMapping("loseLife/{gameRoomId}/{playerId}")
    public void loseLife(@DestinationVariable String gameRoomId, @DestinationVariable String playerId, @Payload Integer card) {
        this.gameRoomService.loseLife(gameRoomId, playerId, card);
        this.messagingTemplate.convertAndSend(String.format("/topic/playerMap/%s", gameRoomId), this.gameRoomService.getPlayerMap(gameRoomId));
    }
}
