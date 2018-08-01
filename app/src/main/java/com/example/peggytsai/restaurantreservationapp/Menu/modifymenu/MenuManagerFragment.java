package com.example.peggytsai.restaurantreservationapp.Menu.modifymenu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Menu.Page;
import com.example.peggytsai.restaurantreservationapp.R;

import java.util.ArrayList;
import java.util.List;


public class MenuManagerFragment extends Fragment {

    private TextView tt_toolbar,btMenuNew;
    private View view;

    private MyPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_menu_modify, container, false);

        tt_toolbar = view.findViewById(R.id.tvTool_bar_title);
        btMenuNew = view.findViewById(R.id.btMenuNew);



            tt_toolbar.setText(R.string.text_MenuManager);
            btMenuNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Common.switchFragment(new MenuModifyFragmentInsert(), getActivity(), true);
//                    Common.switchFragment(new ShowMenuFragment(), getActivity(), true);
//                    Use_fragment useFragment = new Use_fragment();
//                    useFragment.use_fragment(new ShowMenuFragment(), getFragmentManager());
                }
            });

        veiw_set();

        return view;
    }

    private void veiw_set() {
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(  new MyPagerAdapter(getChildFragmentManager())  );  //直接返回 嵌套的子fragment
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        List<Page> pageList;


        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            pageList = new ArrayList<>();
            pageList.add(new Page(new MenuModifyFragmentMain(), "主餐"));
            pageList.add(new Page(new MenuModifyFragmentSub(), "附餐"));
            pageList.add(new Page(new MenuModifyFragmentAdd(), "加購"));

        }

        @Override
        public Fragment getItem(int position) {
            return pageList.get(position).getFragment();
        }

        @Override
        public int getCount() {
            return pageList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageList.get(position).getTitle();
        }
    }



}
