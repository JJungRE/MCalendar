package com.roopre.mcalendar;

/**
 * Created by munjongmin on 2017. 11. 29..
 */

public class WeekDayVO {

    private String year = "";
    private String month = "";
    private String day = "";
    private String imagePath1 = "";
    private String imagePath2 = "";
    private String imagePath3 = "";
    private String total = "";

    public String getDay() {
        return day;
    }


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
    public void setDay(String day) {
        this.day = day;
    }

    public String getImagePath1() {
        return imagePath1;
    }

    public void setImagePath1(String imagePath1) {
        this.imagePath1 = imagePath1;
    }

    public String getImagePath2() {
        return imagePath2;
    }

    public void setImagePath2(String imagePath2) {
        this.imagePath2 = imagePath2;
    }

    public String getImagePath3() {
        return imagePath3;
    }

    public void setImagePath3(String imagePath3) {
        this.imagePath3 = imagePath3;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
