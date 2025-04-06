package com.example.tangthucac;

import java.io.Serializable;
import java.util.Map;

public class Story implements Serializable {
    private String title;
    private String author;
    private int views;
    private String image;
    private Map<String, Chapter> chapters;
    private String genre;  // Thêm trường genre

    public Story() {}  // Firebase yêu cầu constructor rỗng

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getViews() { return views; }
    public String getImage() { return image; }
    public Map<String, Chapter> getChapters() { return chapters; }
    public String getGenre() { return genre; }  // Getter cho genre
}