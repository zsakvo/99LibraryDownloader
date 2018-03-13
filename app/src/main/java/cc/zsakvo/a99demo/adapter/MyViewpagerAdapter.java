package cc.zsakvo.a99demo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import cc.zsakvo.a99demo.fragment.ArticleFragment;
import cc.zsakvo.a99demo.fragment.BookStoreFragment;
import cc.zsakvo.a99demo.fragment.CategoryFragment;


public class MyViewpagerAdapter extends FragmentStatePagerAdapter {

    String[] mTabTitles;


    public MyViewpagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        mTabTitles = titles;

    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch(position) {
            case 0:
                fragment = BookStoreFragment.newInstance(position);
                break;
            case 1:
                fragment = CategoryFragment.newInstance(position);
                break;
            case 2:
                fragment = ArticleFragment.newInstance(position);
                break;
            case 3:
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mTabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return mTabTitles[position];
    }
}
