package com.example.xkfeng.richedit.JavaBean;

import org.litepal.crud.DataSupport;

/**
 * Created by initializing on 2018/5/26.
 */

public class EditSql extends DataSupport {

    private String title ;

    private String content ;

    private String create_time ;

    private String update_time ;

    private int user_id ;

    private boolean isCollected ;

    public int getUser_id() {
        return user_id;
    }

    public String getContent() {
        return content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getTitle() {
        return title;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public Boolean getIsCollected(){return isCollected ; }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }


}
