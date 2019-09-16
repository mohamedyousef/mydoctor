package com.uniyapps.yadoctor.Model;

import com.stfalcon.chatkit.commons.models.IUser;

public class Author implements IUser {

    String id;

    public Author(String id){
         this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getAvatar() {
        return null;
    }
}
