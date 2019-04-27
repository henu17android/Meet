package com.example.meet.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

/**
 * 工具类
 * 切换fragment
 */
public class NavHelper<T> {
    private final SparseArray<Tab<T>> tabs = new SparseArray<Tab<T>>();

    private final Context mContext;
    private final int containerId;
    private final FragmentManager fragmentManager;
    private final OnTabChangeListener<T> listener;

    private Tab<T> currentTab;

    public NavHelper(Context context,int containerId,FragmentManager fragmentManager,OnTabChangeListener<T> mListener){
        this.mContext = context;
        this.containerId = containerId;
        this.fragmentManager = fragmentManager;
        this.listener = mListener;

    }

    /**
     * 添加tab
     * @param menuId
     * @param tab
     */
    public NavHelper<T> add(int menuId,Tab<T> tab) {
          tabs.put(menuId,tab);
          return this;
    }


    /**获取当前tab
     * @return
     */
    public Tab<T> getCurrentTab() {
        return currentTab;
    }


    public static class Tab<T> {
        public Class<?> clx;
        public T extra;
        private Fragment fragment;//内部对应的Fragment

        public Tab(Class<?> clx, T extra){
            this.clx = clx;
            this.extra = extra;
        }

    }


    /**执行点击菜单操作
     * @param menuID
     * @return
     */
    public boolean performClickMenu(int menuID) {
        Tab<T> tab = tabs.get(menuID);
        if (tab != null){
            doSelect(tab);
            return true;
        }
        return false;
    }


    /**进行tab的选择操作
     * @param tab
     */
    private void doSelect(Tab<T> tab) {
        Tab<T> oldTab = null;
        if (currentTab != null) {
            oldTab = currentTab;
            if (oldTab == tab) {
                //如果当前tab点击的tab，不做任何操作或者刷新
              notifyReselect(tab);
              return;
            }
        }
        currentTab = tab;
        doTabChanged(currentTab,oldTab);
    }


    /**Tab切换方法
     * @param newTab
     * @param oldTab
     */
    private void doTabChanged(Tab<T> newTab,Tab<T> oldTab) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (oldTab != null) {
            if (oldTab.fragment != null) {
                //从界面移除,但在Fragment的缓存中
                fragmentTransaction.detach(oldTab.fragment);
            }
        }
        if (newTab != null) {
            if (newTab.fragment == null) {
                //首次建立并缓存
                Fragment fragment = Fragment.instantiate(mContext,newTab.clx.getName(),null);
                newTab.fragment = fragment;
                fragmentTransaction.add(containerId,fragment,newTab.clx.getName());
            }else {
                //直接从缓存中重用
                fragmentTransaction.attach(newTab.fragment);
            }
        }
        fragmentTransaction.commit();
        notifySelect(newTab,oldTab);

    }


    /**选择通知回调
     * @param newTab
     * @param oldTab
     */
    private void notifySelect(Tab<T> newTab,Tab<T> oldTab) {
        if (listener != null) {
            listener.onTabChange(newTab,oldTab);
        }
    }

    private void notifyReselect(Tab<T> tab) {
        //TODO:刷新
    }

    public interface OnTabChangeListener<T> {
        void onTabChange(Tab<T> newTab, Tab<T> oldTab);
    }
}





