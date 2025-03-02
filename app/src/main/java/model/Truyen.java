package model;

public class Truyen {
    private String tenTruyen;
    private String theLoai;
    private String hinhAnhURL;

    public Truyen() {} // Constructor rỗng cần thiết cho Firestore

    public Truyen(String tenTruyen, String hinhAnhURL) {
        this.tenTruyen = tenTruyen;
        this.hinhAnhURL = hinhAnhURL;
    }
    public Truyen(String tenTruyen, String theLoai, String hinhAnhURL) {
        this.tenTruyen = tenTruyen;
        this.theLoai = theLoai;
        this.hinhAnhURL = hinhAnhURL;
    }

    public String getTenTruyen() { return tenTruyen; }
    public String getTheLoai() { return theLoai; }
    public String getHinhAnhURL() { return hinhAnhURL; }
}
