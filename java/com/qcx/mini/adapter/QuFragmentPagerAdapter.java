package com.qcx.mini.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by cpoopc on 2015-02-10.
 */
public class QuFragmentPagerAdapter<T extends Fragment> extends FragmentPagerAdapter {

    private List<T> fragmentList;
    private List<String> titleList;
    private FragmentManager fm;

    public QuFragmentPagerAdapter(FragmentManager fm, List<T> fragmentList, List<String> titleList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titleList = titleList;
        this.fm = fm;
    }

    public QuFragmentPagerAdapter(FragmentManager fm, List<T> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titleList = titleList;
        this.fm = fm;
    }

    @Override
    public T getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titleList != null) return titleList.get(position);
        else return super.getPageTitle(position);
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        return super.instantiateItem(container, position);
//    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
////        super.instantiateItem(container, position);
//        T pager = fragmentList.get(position);
//        if (fm == null) {
//            return pager;
//        }
//        if (pager.isAdded()) {
//            fm.beginTransaction().show(pager).commit();
//        } else {
//            fm.beginTransaction().add(container.getId(), pager).commit();
//        }
//        return pager;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        T pager = fragmentList.get(position);
//        fm.beginTransaction().hide(pager).commit();
//    }
}
