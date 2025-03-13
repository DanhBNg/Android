package testapim;



public class Chapter {
    private String title;
    private String content;
    private int views;

    public Chapter() {}  // Firebase yêu cầu constructor rỗng

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public int getViews() { return views; }
}
