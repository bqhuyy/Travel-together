package cs300.apcs04.traveltogether;

import java.util.Date;

public class ChatMessage {
    private String messageText;
    private String messageUser;
    private long messageTime;


    public ChatMessage(String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;

        messageTime = new Date().getTime();
    }

    public ChatMessage() {

    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }
}
