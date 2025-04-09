package com.example.tangthucac.model;

import java.io.Serializable;
import java.util.Map;

public class Story implements Serializable {
    private String id;
    private String title;
    private String author;
    private int views;
    private String image;
    private Map<String, Chapter> chapters;
    private String genre;
    private boolean hot; // Thêm trường hot

    public Story() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getViews() { return views; }
    public String getImage() { return image; }
    public Map<String, Chapter> getChapters() { return chapters; }
    public String getGenre() { return genre; }
    public boolean isHot() { return hot; }


}