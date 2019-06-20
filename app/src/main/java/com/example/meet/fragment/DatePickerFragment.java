package com.example.meet.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.example.meet.R;
import com.example.meet.activity.EditTaskActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {

    public static final String ARG_DATE = "date";
    public static final String EXTRA_DATE = "Fragment.DatePickedFragment.date";
    private DatePicker mDatePicker;
    public SelectDateSender selectDateSender;
    private EditTaskActivity editTaskActivity;


    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE,date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //Activity和Fragment发生联系时调用
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        editTaskActivity = (EditTaskActivity)context;
        selectDateSender = (SelectDateSender)context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_date_picker,null);
        final Date date = (Date)getArguments().getSerializable(ARG_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        mDatePicker = (DatePicker)view.findViewById(R.id.data_picker);
        mDatePicker.init(year,month,day,null);

        //创建DatePicker 的 Dialog
        return new AlertDialog.Builder(getActivity()).
                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        Calendar ca = new GregorianCalendar(year,month,day);
                        Date selectDate = ca.getTime();
                        selectDateSender.getDate(selectDate);
                    }
                }).setView(view).create();
    }

    //将选中的日期传递
    public interface SelectDateSender {
        void getDate(Date date);
    }
}
