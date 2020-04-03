package com.game.coup.controller;

import com.game.coup.service.GameRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class GameSetupController {

    private final GameRoomService gameRoomService;

    @Autowired
    public GameSetupController(GameRoomService gameRoomService) {
        this.gameRoomService = gameRoomService;
    }

    @MessageMapping("/create")
    @SendToUser("/topic/createdRoom")
    public String createRoom() {
        return this.gameRoomService.createGameRoom();
    }
}
