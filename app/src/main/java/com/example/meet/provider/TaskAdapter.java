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
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SimpleAdapter;
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


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        final Task task = taskList.get(position);
        Log.d("onBindViewHolder", "" + (task == null) + "-" + task.getContent());
        viewHolder.contentView.setText(task.getContent());
        viewHolder.finishBox.setChecked(task.isFinish());

        if (mOnItemClickListener != null) {
            viewHolder.contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(viewHolder.itemView,position);
                }
            });
        }

        viewHolder.finishBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                task.setFinish(isChecked);
                TaskLab.get(mContext).updateTask(task);
            }
        });
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView contentView;
        private CheckBox finishBox;
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

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private TaskAdapter.OnItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }
}