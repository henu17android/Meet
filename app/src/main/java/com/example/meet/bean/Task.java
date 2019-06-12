package com.example.meet.bean;

import android.widget.CheckBox;

import org.litepal.crud.LitePalSupport;

import java.util.ListIterator;

public class Task extends LitePalSupport {


    private int taskId;
    private String content; //任务内容
    private boolean isFinish = false;
    private long toDoTime;
    private long createTime;

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getCreateTime() {

        return createTime;
    }

    public long getToDoTime() {
        return toDoTime;
    }

    public int getId() {
        return taskId;
    }

    public void setToDoTime(long toDoTime) {
        this.toDoTime = toDoTime;
    }

    public void setId(int id) {
        this.taskId = id;
    }

    public Task(String content) {
        this.content = content;
    }

    public Task() {

    }

    public String getContent() {
        return content;
    }



    public boolean isFinish() {
        return isFinish;
    }



    public void setContent(String content) {
        this.content = content;
    }



    public void setFinish(boolean finish) {
        isFinish = finish;
    }

}
