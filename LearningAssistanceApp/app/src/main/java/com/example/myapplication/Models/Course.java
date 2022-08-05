package com.example.myapplication.Models;

import java.util.ArrayList;

public class Course {
    private String pdfID;
    private String titlu;
    private String pdfURL;
    private String keywords;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getPdfID() {
        return pdfID;
    }

    public void setPdfID(String pdfID) {
        this.pdfID = pdfID;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getPdfURL() {
        return pdfURL;
    }

    public void setPdfURL(String pdfURL) {
        this.pdfURL = pdfURL;
    }

    public Course(String titlu, String pdfURL, String id) {
        this.titlu = titlu;
        this.pdfID=id;
        this.pdfURL = pdfURL;
    }
    public Course(){

    }
}
