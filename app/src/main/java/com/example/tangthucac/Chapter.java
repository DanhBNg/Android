package com.example.tangthucac;

import java.io.Serializable;

public class Chapter implements Serializable {
    private String title;
    private String content;
    private int views;

    public Chapter() {}

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getViews() {
        return views;
    }
}
