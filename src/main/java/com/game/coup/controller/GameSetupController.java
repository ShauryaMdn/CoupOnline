package com.game.coup.controller;

import com.game.coup.service.GameRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class GameSetupController {

    private final GameRoomService gameRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public GameSetupController(GameRoomService gameRoomService, SimpMessagingTemplate messagingTemplate) {
        this.gameRoomService = gameRoomService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/create")
    public void createRoom(@Payload String playerName, Principal user) {
        this.messagingTemplate.convertAndSendToUser(user.getName(), "/topic/createdRoom", this.gameRoomService.createGameRoom(playerName));
    }

    @MessageMapping("/join/{gameRoomId}")
    public void joinRoom(@DestinationVariable String gameRoomId, @Payload String playerName, Principal user) {
        this.messagingTemplate.convertAndSendToUser(user.getName(), "/topic/joinedRoom", this.gameRoomService.addPlayer(gameRoomId, playerName));
        this.messagingTemplate.convertAndSend(String.format("/topic/playerMap/%s", gameRoomId), this.gameRoomService.getPlayerMap(gameRoomId));;
    }
}
