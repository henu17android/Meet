package com.example.meet.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


import com.example.meet.R;
import com.example.meet.fragment.DatePickerFragment;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditTaskActivity extends AppCompatActivity implements DatePickerFragment.SelectDateSender {

    public static final String TAG = "EditTaskActivity";
    private EditText showDate;
    private EditText taskEdit;
    private Date mSelectDate;
    private int taskId;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.finish_edit :

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.edit);

        showDate = (EditText)findViewById(R.id.date);
        taskEdit = (EditText)findViewById(R.id.task_edit);

        showDate.setInputType(InputType.TYPE_NULL);

        Intent intent = getIntent();
        String toDoTime = intent.getStringExtra("toDoTime");
        String content = intent.getStringExtra("content");
        taskId = intent.getIntExtra("taskId",0);

        taskEdit.setText(content);
        //将光标移至文字末尾
        taskEdit.setSelection(content.length());

        showDate.setText(toDoTime);

        Log.d(TAG, "onCreate: "+ "todotime :"+toDoTime);


        //将传递的日期字符串转为date类型
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(toDoTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Log.d(TAG, "onCreate: Date"+date);



        final Date finalDate = date;
        showDate.setOnClickListener(new View.OnClickListener() {
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
    public void getDate(Date date) {
        mSelectDate = date;
        Log.d(TAG, "onClick: mSelectDate"+mSelectDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String newTime = sdf.format(mSelectDate);
        showDate.setText(newTime);

    }
}



