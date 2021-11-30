package com.astudio.progressmonitor.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.astudio.progressmonitor.R;
import com.astudio.progressmonitor.task.TabHostActivity;
import com.google.android.material.tabs.TabItem;

import java.util.ArrayList;
import java.util.List;


//public class TaskViewPagerAdapter extends FragmentStatePagerAdapter {
public class TaskViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    //private final List<String> mFragmentTitleList = new ArrayList<>();
    private final Context mContext;

    private String[] tabTitles;

    //public TaskViewPagerAdapter(FragmentManager fm) {
    public TaskViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        tabTitles = new String[] { mContext.getResources().getString(R.string.incoming),
                mContext.getResources().getString(R.string.outbox),
                mContext.getResources().getString(R.string.other)};
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
        //return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //return mFragmentTitleList.get(position);
        return tabTitles[position];
    }



    public void addFragment(Fragment fragment/*, String title*/) {
        mFragmentList.add(fragment);
        //mFragmentTitleList.add(title);
    }



    // добавил для кастомизации заголовков Таба (а именно для обрезки текста когда не входит)
    public View getTabView(int position, boolean selected) {
        @SuppressLint("InflateParams")
        View tab = LayoutInflater.from(mContext).inflate(R.layout.custom_tab_layout, null);
        TextView tv = (TextView) tab.findViewById(R.id.custom_tab_view);
        tv.setText(tabTitles[position]);
        if (selected){
            tv.setTypeface(Typeface.DEFAULT_BOLD);
        }else {
            tv.setTypeface(Typeface.DEFAULT);
        }
        return tab;
    }




    // https://stackoverflow.com/questions/31698756/remove-line-break-in-tablayout
    // https://guides.codepath.com/android/google-play-style-tabs-using-tablayout#setup

//    //@Override
//    public CharSequence getPageTitle2(int position) {
//        // Generate title based on item position
//        return tabTitles[position];
//    }


}
