package com.example.meet.bean;

import android.widget.CheckBox;

import org.litepal.crud.LitePalSupport;

import java.util.ListIterator;

public class Task extends LitePalSupport {

    private int id;
    private String content; //任务内容
    private boolean isFinish = false;
    private String toDoTime;
    private long createTime;
    private String detail;

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDetail() {

        return detail;
    }

    public Task(String content) {
        this.content = content;
    }

    public Task() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public String getToDoTime() {
        return toDoTime;
    }

    public void setToDoTime(String toDoTime) {
        this.toDoTime = toDoTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
