package com.example.meet.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.meet.R;
import com.example.meet.bean.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{

    private List<Task> taskList;
    private Context context;
    public TaskAdapter (List<Task> taskList, Context context) {
          this.taskList = taskList;
          this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
      return viewHolder;
    }

    @Override
    public void onBindViewHolder( ViewHolder viewHolder, int i) {
       viewHolder.contentView.setText(taskList.get(i).getContent());
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView contentView;
        CheckBox finishBox;
        //保存子项最外层的实例
        View taskView;

        public ViewHolder( View itemView) {
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
}
