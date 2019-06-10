package com.example.meet.fragment;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
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
    private PopupWindow popEdit;
    //声明PopupWindow对应的视图
    private View popupView;
    private EditText addTaskEdit;
    private ImageButton addTaskbtn;
    //声明平移动画
    private TranslateAnimation animation;



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
        taskList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_task,container,false);
        initCalenderView(rootView);
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        addFab = rootView.findViewById(R.id.fab);
        TaskAdapter taskAdapter = new TaskAdapter(taskList(),mActivity);
        mRecyclerView.setAdapter(taskAdapter);

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopEdit();
                lightOff();
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

        //点击改变右上角日期
        CalendarSelectListener = new CalendarView.OnCalendarSelectListener() {
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
            }
        };

        mCalenderView.setOnCalendarSelectListener(CalendarSelectListener);

        YearChangeListener = new CalendarView.OnYearChangeListener() {
            @Override
            public void onYearChange(int year) {
                mTextMonthDay.setText(String.valueOf(year));
            }
        };
        mCalenderView.setOnYearChangeListener(YearChangeListener);

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


    private List<Task> taskList() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (int i = 0;i<10;i++) {
            Task task = new Task("今天真开心"+i);
            tasks.add(task);
        }
        return tasks;

    }

    /**
     * 弹出编辑框
     */
    private void showPopEdit() {
        if (popEdit == null) {
        popupView = getLayoutInflater().inflate(R.layout.pop_add_edit,null);
        addTaskEdit = popupView.findViewById(R.id.add_editText);
        addTaskbtn = popupView.findViewById(R.id.add);
        popEdit = new PopupWindow(popupView,ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popEdit.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                   lightOn();
                }
            });
        }


        //设置背景图片，让动画起效
        popEdit.setBackgroundDrawable(new BitmapDrawable());
        popEdit.setFocusable(true);

        //设置点击popupWindow外 editText消失
        popEdit.setOutsideTouchable(true);

        //平移动画相对于手机屏幕的底部开始，x轴不变，y轴从1变到0
        animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,0,
                Animation.RELATIVE_TO_PARENT,0,Animation.RELATIVE_TO_PARENT,1,
                Animation.RELATIVE_TO_PARENT,0);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(200);
        popupView.startAnimation(animation);

        addTaskbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //设置popupWindow的显示位置
        popEdit.showAtLocation(TaskFragment.this.rootView.findViewById(R.id.fragment_task),
                Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
        //点击之后设置popupWindow销毁
        if (popEdit.isShowing()) {
            popEdit.dismiss();
            lightOn();
        }
//
//        if (popEdit != null && popEdit.isShowing()) {
//            return;
//        }
//
//        popEdit.showAsDropDown(addTaskEdit);
    }

    /**
     * 屏幕亮度变暗
     */
    private void lightOff() {
        WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
        layoutParams.alpha = 0.3f;
        getActivity().getWindow().setAttributes(layoutParams);

    }

    /**
     * 亮度恢复正常
     */
    private void lightOn() {
        WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
        layoutParams.alpha = 1f;
        getActivity().getWindow().setAttributes(layoutParams);

    }


}
