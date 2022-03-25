package by.shimakser.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    private ActionType type;
    private String content;
    private String sender;
}
