package com.game.coup.controller;

import com.game.coup.models.PlayerMessage;
import com.game.coup.models.RoomMessage;
import com.game.coup.service.GameRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class CommunicationsController {

    private final GameRoomService gameRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public CommunicationsController(GameRoomService gameRoomService, SimpMessagingTemplate messagingTemplate) {
        this.gameRoomService = gameRoomService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/communicate/player/{gameRoomId}/{sourcePlayerId}/{targetPlayerId}")
    public void communicateToPlayer(@DestinationVariable String gameRoomId, @DestinationVariable String sourcePlayerId,
                                    @DestinationVariable String targetPlayerId, @Payload String message) {
        this.messagingTemplate.convertAndSend(String.format("/topic/playerAction/%s", gameRoomId),
                new PlayerMessage(message, sourcePlayerId, targetPlayerId));
    }

    @MessageMapping("/callout/{gameRoomId}/{sourcePlayerId}/{targetPlayerId}")
    public void calloutPLayer(@DestinationVariable String gameRoomId, @DestinationVariable String sourcePlayerId,
                              @DestinationVariable String targetPlayerId, @Payload String message) throws InterruptedException {
        this.gameRoomService.getGameRoom(gameRoomId).computeIfPresent("calloutLock", (k, v) -> {
            this.messagingTemplate.convertAndSend(String.format("/topic/callout/%s", gameRoomId),
                    new PlayerMessage(message, sourcePlayerId, targetPlayerId));
            return null;
        });

        // Sleeping first in case there's another thread waiting on the value of calloutLock
        Thread.sleep(500);
        this.gameRoomService.getGameRoom(gameRoomId).put("calloutLock", true);
    }

    @MessageMapping("/communicate/room/{gameRoomId}/{playerId}")
    public void communicateToRoom(@DestinationVariable String gameRoomId, @DestinationVariable String playerId, @Payload String message) {
        this.messagingTemplate.convertAndSend(String.format("/topic/playerAction/%s", gameRoomId), new RoomMessage(message, playerId));
    }
}
