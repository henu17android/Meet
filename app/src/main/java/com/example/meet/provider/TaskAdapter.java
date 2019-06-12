package com.example.meet.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.meet.R;
import com.example.meet.bean.Task;
import com.example.meet.bean.TaskLab;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> taskList;
    private Context mContext;

    public TaskAdapter(List<Task> taskList, Context context) {
        this.taskList = taskList;
        this.mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        final Task task = taskList.get(i);
        Log.d("onBindViewHolder", ""+(task==null)+"-"+task.getContent());
        viewHolder.contentView.setText(task.getContent());
        viewHolder.finishBox.setChecked(task.isFinish());
        viewHolder.contentView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                task.setContent(s.toString());
                TaskLab.get(mContext).updateTask(task);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        viewHolder.finishBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                task.setFinish(isChecked);
                TaskLab.get(mContext).updateTask(task);
            }
        });
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        EditText contentView;
        CheckBox finishBox;
        //保存子项最外层的实例
        View taskView;

        public ViewHolder(View itemView) {
            super(itemView);
            taskView = itemView;
            contentView = itemView.findViewById(R.id.taskContent);
            finishBox = itemView.findViewById(R.id.check_box);

        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
