package com.example.meet.bean;

import android.widget.CheckBox;

public class Task {

    private int id;
    private String content; //任务内容
    private CheckBox checkBox;  //
    private boolean isFinish = false;
    private boolean isDelete = false;
    private long toDoTime;
    private long createTime;

    public void setCreatTime(long creatTime) {
        this.createTime = creatTime;
    }

    public long getCreatTime() {

        return createTime;
    }

    public long getToDoTime() {
        return toDoTime;
    }

    public int getId() {
        return id;
    }

    public void setToDoTime(long toDoTime) {
        this.toDoTime = toDoTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Task(String content) {
        this.content = content;
    }

    public Task() {

    }
    public String getContent() {
        return content;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }
}
