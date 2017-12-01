package com.roopre.mcalendar;

import android.graphics.Bitmap;

/**
 * Created by jjungre on 2017. 11. 27..
 */

public class SetMainSchedule_Item {


    //리스트 정보를 담고 있을 객체 생성

    public String category;
    public Bitmap img;
    public String memo;
    public String used;
    public String schedule_seq;

    public String getSchedule_seq() {
        return schedule_seq;
    }

    public void setSchedule_seq(String schedule_seq) {
        this.schedule_seq = schedule_seq;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }


}
