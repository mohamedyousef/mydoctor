package com.uniyapps.yadoctor.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Question implements Parcelable {

    public Question(){}

    protected Question(Parcel in) {
        title = in.readString();
        quizes = in.createStringArrayList();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public String getTitle() {
        return title;
    }
    public ArrayList<String> getQuizes() {
        return quizes;
    }
    public Question(String title, ArrayList<String> quizes) {
        this.title = title;
        this.quizes = quizes;
    }

    public String title;
    public ArrayList<String>quizes;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeStringList(quizes);
    }
}
