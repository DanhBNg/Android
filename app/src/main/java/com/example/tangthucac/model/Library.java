package com.example.tangthucac.model;

public class Library {
    private String StoryId;
    private String title;
    private String author;
    private String image;

    public Library() {
    }

    public Library(String title, String author, String image) {
        this.title = title;
        this.author = author;
        this.image = image;
    }

    public void setStoryId(String storyId) {
        StoryId = storyId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStoryId() {
        return StoryId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getImage() {
        return image;
    }
}
