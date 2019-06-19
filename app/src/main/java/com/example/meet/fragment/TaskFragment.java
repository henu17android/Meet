package com.example.meet.fragment;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;


import com.example.meet.R;
import com.example.meet.activity.EditTaskActivity;
import com.example.meet.activity.MainActivity;
import com.example.meet.bean.Task;
import com.example.meet.bean.TaskLab;
import com.example.meet.provider.TaskAdapter;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import org.litepal.LitePal;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TaskFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "TaskFragment";
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
    private List<Task> mTaskList;
    private TaskAdapter mTaskAdapter;
    private int mSelectTime;
    private ExecutorService mSingleThreadPool = Executors.newSingleThreadExecutor();
    private Handler mHandler = new Handler();
    private OnFragmentInteractionListener mListener;

    //当与activity建立联系时调用
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
        CalendarSelectListener = (CalendarView.OnCalendarSelectListener) context;
        YearChangeListener = (CalendarView.OnYearChangeListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_task, container, false);
        initCalenderView(rootView);
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        addFab = rootView.findViewById(R.id.fab);
        addFab.setOnClickListener(this);

        //Calendar点击事件
        mCalenderView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {

            }

            @Override
            public void onCalendarSelect(Calendar calendar, boolean isClick) {
                mTextLunar.setVisibility(View.VISIBLE);
                mTextYear.setVisibility(View.VISIBLE);
                mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
                mTextLunar.setText(calendar.getLunar());
                mYear = calendar.getYear();
                mSelectTime = mYear * 365 + calendar.getMonth() * 31 + calendar.getDay();
                Log.d(TAG, "mSelectTime:" + mSelectTime);
                //查找选中日期下的任务列表
                mSingleThreadPool.execute(updateUIRunnable);

            }
        });

        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        mSingleThreadPool.execute(updateUIRunnable);
    }


    //线程查找数据库
    private Runnable updateUIRunnable = new Runnable() {
        @Override
        public void run() {
            mTaskList = LitePal.where("toDoTime = ?", String.valueOf(mSelectTime)).find(Task.class);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mTaskAdapter = null;
                    mTaskAdapter = new TaskAdapter(mTaskList, getActivity());
                    //点击编辑、长按删除
                    mTaskAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(TaskAdapter.ViewHolder vh, int position) {
                            Intent intent = new Intent(mActivity, EditTaskActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onItemLongClick(TaskAdapter.ViewHolder vh, int position) {

                        }

                        @Override
                        public void onCheckBoxClick(TaskAdapter.ViewHolder vh, int position, boolean isChecked) {
                            Log.d(TAG,"listSize"+"--- "+mTaskList.size());
                            Log.d(TAG,"onClickedPosition"+"--- "+position);
//                            Task task = mTaskList.get(position);
//                            task.setFinish(isChecked);
                            TextView contentView = vh.contentView;
                            if (isChecked) {
//                                mTaskAdapter.removeTask(position);
//                                mTaskAdapter.addTask(task);
                                contentView.setPaintFlags(contentView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                contentView.setTextColor(Color.rgb(192,192,192));
                            } else {
                                contentView.setPaintFlags(contentView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                                contentView.setTextColor(Color.BLACK);
                            }
                            for(Task t : mTaskList) {
                                Log.d(TAG,"listContent"+"--- "+t.getContent());
                            }
                        }
                    });
                    mRecyclerView.setAdapter(mTaskAdapter);

                }
            });
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public TaskFragment() {
        // Required empty public constructor
    }


    public static TaskFragment newInstance(String param1, String param2) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * @param view 初始化月历视图
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
                if (!mCalendarLayout.isExpand()) {
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
        mTextMonthDay.setText(mCalenderView.getCurMonth() + "月" + mCalenderView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalenderView.getCurDay()));
        mSelectTime = mCalenderView.getCurYear() * 365 + mCalenderView.getCurMonth() * 31 + mCalenderView.getCurDay();
        Log.d(TAG, "today:-" + mSelectTime);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                showAddDialog();//添加task的编辑框
                break;
            default:
                break;
        }
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
        View addTaskDialog = layoutInflater.inflate(R.layout.add_dialog_2, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(addTaskDialog);
        builder.setTitle("添加新任务");

        final EditText addText = addTaskDialog.findViewById(R.id.add_task_text);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //添加任务
                Task task = new Task(addText.getText().toString());
                task.setCreateTime(System.currentTimeMillis());
                task.setToDoTime(mSelectTime);
                TaskLab.get(getActivity()).addTask(task);
                mSingleThreadPool.execute(updateUIRunnable);
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

    /**
     * 隐藏软键盘
     */
    private void hideSoftKeyWord() {
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }


}
