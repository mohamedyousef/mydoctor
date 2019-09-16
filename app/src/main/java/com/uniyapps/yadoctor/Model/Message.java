package com.uniyapps.yadoctor.Model;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

public class Message implements IMessage {

    String id, text;
    Date date;

    public Message(String id, String text, Date date) {
        this.id = id;
        this.text = text;
        this.date = date;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public IUser getUser() {
        return new Author(getId());
    }

    @Override
    public Date getCreatedAt() {
        return date;
    }

    @Override
    public boolean equals(Object t) {
        return (t instanceof Message) ? date.equals(((Message)t).date)
                : super.equals(t); }

}
