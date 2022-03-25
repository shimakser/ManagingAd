package by.shimakser.controller;

import by.shimakser.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

    @GetMapping("/chat")
    public String welcomePage() {
        return "chat";
    }

    @MessageMapping("/action.sendMessage")
    @SendTo("/chat/feed")
    public Message sendMessage(@Payload Message message) {
        return message;
    }

    @MessageMapping("/action.addUser")
    @SendTo("/chat/feed")
    public Message addUser(@Payload Message message,
                           SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        return message;
    }
}
