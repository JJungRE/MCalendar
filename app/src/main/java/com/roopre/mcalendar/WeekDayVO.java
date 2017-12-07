package com.roopre.mcalendar;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by munjongmin on 2017. 11. 29..
 */

public class WeekDayVO {

    private String year = "";
    private String month = "";
    private String day = "";
    private ArrayList<String> seqList = new ArrayList<String>();
    private Bitmap image1 = null;
    private Bitmap image2 = null;
    private Bitmap image3 = null;
    private Bitmap image4 = null;
    private String total = "";

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ArrayList<String> getSeqList() {
        return seqList;
    }

    public void setSeqList(ArrayList<String> seqList) {
        this.seqList = seqList;
    }

    public Bitmap getImage1() {
        return image1;
    }

    public void setImage1(Bitmap image1) {
        this.image1 = image1;
    }

    public Bitmap getImage2() {
        return image2;
    }

    public void setImage2(Bitmap image2) {
        this.image2 = image2;
    }

    public Bitmap getImage3() {
        return image3;
    }

    public void setImage3(Bitmap image3) {
        this.image3 = image3;
    }

    public Bitmap getImage4() {
        return image4;
    }

    public void setImage4(Bitmap image4) {
        this.image4 = image4;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
