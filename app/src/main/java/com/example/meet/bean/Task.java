package com.example.meet.bean;

import android.widget.CheckBox;

public class Task {

    private String content;
    private CheckBox checkBox;
    private boolean isFinish = false;
    private boolean isDelete = false;

    public Task(String content) {
        this.content = content;
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
