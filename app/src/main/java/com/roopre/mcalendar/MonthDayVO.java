package com.roopre.mcalendar;

import java.util.ArrayList;

/**
 * Created by munjongmin on 2017. 11. 29..
 */

public class MonthDayVO {

    private String day = "";
    private String title1 = "";
    private String title2 = "";
    private String total = "";
    private ArrayList<String> seqList = new ArrayList<String>();

    public ArrayList<String> getSeqList() {
        return seqList;
    }

    public void setSeqList(ArrayList<String> seqList) {
        this.seqList = seqList;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
