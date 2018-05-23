package com.example.xkfeng.richedit.JavaBean;

/**
 * Created by initializing on 2018/5/18.
 */

public class EditData {
    private String data ;
    private String time ;
    private String isCollected ;

    public String getData() {
        return data;
    }

    public String getIsCollected() {
        return isCollected;
    }

    public String getTime() {
        return time;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setIsCollected(String isCollected) {
        this.isCollected = isCollected;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
