package com.roopre.mcalendar;

import android.graphics.Bitmap;

/**
 * Created by jjungre on 2017. 11. 27..
 */

public class SetMainSchedule_Item {


    //리스트 정보를 담고 있을 객체 생성

    public String num;
    public String seq;
    public Bitmap img;
    public String category;
    public String title;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
