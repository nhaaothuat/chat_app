package com.example.chat_app.controller;

import com.example.chat_app.entities.Message;
import com.example.chat_app.entities.Room;
import com.example.chat_app.playload.MessageRequest;
import com.example.chat_app.repositories.RoomRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Controller

@AllArgsConstructor
public class ChatController {
    private final RoomRepository roomRepository;

    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public Message sendMessage(
            @DestinationVariable String roomId,
            @RequestBody MessageRequest request

    ) {
        Room room = roomRepository.findByRoomId(request.getRoomId());
        Message message = new Message();
        message.setContent(request.getContent());
        message.setSender(request.getSender());
        message.setTimeStamp(LocalDateTime.now());

        if (room != null) {
            room.getMessages().add(message);
            roomRepository.save(room);
        }

        return message;
    }


}
