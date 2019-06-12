package com.example.meet.fragment;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;


import com.example.meet.R;
import com.example.meet.activity.MainActivity;
import com.example.meet.bean.Task;
import com.example.meet.provider.TaskAdapter;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import org.litepal.LitePal;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class TaskFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private MainActivity mActivity;
    private CalendarView.OnCalendarSelectListener CalendarSelectListener; //实例化接口
    private CalendarView.OnYearChangeListener YearChangeListener;
    private CalendarView mCalenderView;
    private CalendarLayout mCalendarLayout;
    private TextView mTextMonthDay;
    private TextView mTextYear;
    private TextView mTextLunar;//农历日期
    private TextView mTextCurrentDay;
    private RelativeLayout mRelativeTool;
    private int mYear;
    private RecyclerView mRecyclerView;
    private FloatingActionButton addFab;
    private List<Task> taskList = new ArrayList<>();
    private  TaskAdapter taskAdapter;



    private OnFragmentInteractionListener mListener;

    public TaskFragment() {
        // Required empty public constructor
    }


    public static TaskFragment newInstance(String param1, String param2) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_task,container,false);
        initCalenderView(rootView);

        addFab = rootView.findViewById(R.id.fab);
        taskAdapter = new TaskAdapter(taskList,mActivity);

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });

        //Calendar点击事件
        mCalenderView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {

            }

            @Override
            public void onCalendarSelect(Calendar calendar, boolean isClick) {
                mTextLunar.setVisibility(View.VISIBLE);
                mTextYear.setVisibility(View.VISIBLE);
                mTextMonthDay.setText(calendar.getMonth()+"月"+calendar.getDay()+"日");
                mTextLunar.setText(calendar.getLunar());
                mYear = calendar.getYear();

                //查找选中日期下的任务列表
                taskList = LitePal.where("toDoTime = ?",String.valueOf(calendar.getDay())).find(Task.class);
                onResume();
            }
        });


        return  rootView;

    }

    /**
     * @param view
     * 初始化月历视图
     */
    private void initCalenderView(View view) {
        mTextMonthDay = view.findViewById(R.id.tv_month_day);
        mTextYear = view.findViewById(R.id.tv_year);
        mTextLunar = view.findViewById(R.id.tv_lunar);
        mTextCurrentDay = view.findViewById(R.id.tv_current_day);
        mRelativeTool = view.findViewById(R.id.rl_tool);
        mCalenderView = view.findViewById(R.id.calendarView);
        mCalendarLayout = view.findViewById(R.id.calendarLayout);
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()){
                    mCalendarLayout.expand();
                    return;
                }
                mCalenderView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));

            }
        });

        //点击右上角滚动到当前
        view.findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalenderView.scrollToCurrent();
            }
        });

        mCalenderView.setOnYearChangeListener(new CalendarView.OnYearChangeListener() {
            @Override
            public void onYearChange(int year) {
                mTextMonthDay.setText(String.valueOf(year));
            }
        });

        mTextYear.setText(String.valueOf(mCalenderView.getCurYear()));
        mYear = mCalenderView.getCurYear();
        mTextMonthDay.setText(mCalenderView.getCurMonth()+"月"+mCalenderView.getCurDay()+"日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalenderView.getCurDay()));


    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    //当与activity建立联系时调用
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity)context;
        CalendarSelectListener = (CalendarView.OnCalendarSelectListener)context;
        YearChangeListener = (CalendarView.OnYearChangeListener)context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    @Override
    public void onClick(View v) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }




    /**
     * 点击添加按钮弹出对话框
     */
    private void showAddDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        View addTaskDialog = layoutInflater.inflate(R.layout.add_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(addTaskDialog);
        builder.setTitle("  ");

        final EditText addText = addTaskDialog.findViewById(R.id.add_task_text);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //添加任务
                Task task = new Task(addText.getText().toString());
                task.setCreateTime(System.currentTimeMillis());
                task.setToDoTime(mCalenderView.getCurDay());
                task.save();
                taskList.add(task);
                taskList.notify();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create();
        builder.show();
    }


    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setAdapter(taskAdapter);

    }
}
