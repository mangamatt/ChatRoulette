package com.imie.chatroulette;

/**
 * Created by denou on 28/03/2017.
 */

public class Message {
    private String pseudo;
    private String message;
    private String Hour;

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHour() {
        return Hour;
    }

    public void setHour(String hour) {
        Hour = hour;
    }

    public Message(String pseudo, String message, String hour) {
        this.pseudo = pseudo;
        this.message = message;
        Hour = hour;
    }
}
