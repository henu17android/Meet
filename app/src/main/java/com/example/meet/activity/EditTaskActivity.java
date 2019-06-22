package com.example.meet.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.example.meet.R;
import com.example.meet.bean.Task;
import com.example.meet.bean.TaskLab;
import com.example.meet.fragment.DatePickerFragment;
import com.example.meet.helper.NavHelper;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditTaskActivity extends AppCompatActivity implements DatePickerFragment.SelectDateSender {

    public static final String TAG = "EditTaskActivity";
    private TextView mShowDate;
    private EditText mTaskEditText;
    private Date mSelectDate;
    private int mTaskId;
    private Task mTask;
    private Toolbar mToolbar;
    private EditText mTaskDetail;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        NavHelper.setStatusBarColor(this,getResources().getColor(R.color.colorPrimaryDark) );
        mToolbar = (Toolbar) findViewById(R.id.edit_task_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//返回父活动的箭头
        mShowDate = (TextView)findViewById(R.id.date);
        mTaskEditText = (EditText)findViewById(R.id.edit_task_activity_task_content);
        mTaskDetail = (EditText)findViewById(R.id.task_detail);

        Intent intent = getIntent();
        mTaskId = intent.getIntExtra("taskId",0); //获取传过来的id
        Log.d(TAG, "onCreate: taskID"+mTaskId);
        mTask = TaskLab.get(getApplicationContext()).findTaskById(mTaskId); //从数据库中根据id得到对应 Task
        mTaskEditText.setText(mTask.getContent());
        //将光标移至文字末尾
        mTaskEditText.setSelection(mTask.getContent().length());
        mTaskDetail.setText(mTask.getDetail());
        if (mTask.getDetail() != null){
            mTaskDetail.setSelection(mTask.getDetail().length());
        }

        mShowDate.setText(mTask.getToDoTime());
        //将传递的日期字符串转为date类型
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(mTask.getToDoTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Date finalDate = date;
        mShowDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //传递日期给fragment
                DatePickerFragment dateFragment = DatePickerFragment.newInstance(finalDate);
                FragmentManager fragmentManager = getSupportFragmentManager();
                dateFragment.show(fragmentManager,"DialogDate ");

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_edit_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_done:
                Task task = new Task();
                task.setId(mTaskId);
                task.setContent(mTaskEditText.getText().toString());
                task.setToDoTime(mShowDate.getText().toString());
                Log.d(TAG, "onOptionsItemSelected: "+mShowDate.getText().toString());
                task.setDetail(mTaskDetail.getText().toString());
                TaskLab.get(EditTaskActivity.this).updateTask(task);
                finish();
                return true;

            case R.id.delete_task:
                TaskLab.get(EditTaskActivity.this).deleteTaskById(mTaskId);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void getDate(Date date) {
        mSelectDate = date;
        Log.d(TAG, "onClick: mSelectDate"+mSelectDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String newTime = sdf.format(mSelectDate);
        mShowDate.setText(newTime);
    }


}



