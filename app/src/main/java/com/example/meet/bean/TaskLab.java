package com.example.meet.bean;

import android.content.Context;

import org.litepal.LitePal;

import java.util.List;

/**
 * 对Task的数据库操作进行封装
 * @author 祁梦楠的陈先生
 */
public class TaskLab {
    private static TaskLab sTaskLab;
    private Context mContext;


    public static TaskLab get(Context context) {
        if (sTaskLab == null) {
            sTaskLab = new TaskLab(context);
        }
        return sTaskLab;
    }

    private TaskLab(Context context) {
        this.mContext = context;
    }

    private int getRowCount() {
        int count = LitePal.count(Task.class);
        return count;
    }

    public boolean addTask(Task task) {
        return task.save();
    }

    public int deleteTaskById(int id) {
        return LitePal.delete(Task.class, id);

    }

    public void updateTask(Task task) {
        Task task1 = LitePal.find(Task.class, task.getId());
        task1.setContent(task.getContent());
        task1.setCreateTime(task.getCreateTime());
        task1.setFinish(task.isFinish());
        task1.setToDoTime(task.getToDoTime());
        task1.save();
    }

    public List<Task> findAll() {
        return LitePal.findAll(Task.class);
    }
}
