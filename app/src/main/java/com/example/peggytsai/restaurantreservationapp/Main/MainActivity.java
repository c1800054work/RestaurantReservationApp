package com.example.peggytsai.restaurantreservationapp.Main;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MenuItem;

import com.example.peggytsai.restaurantreservationapp.Check.CheckFragment;
import com.example.peggytsai.restaurantreservationapp.Check.CheckManagerFragment;
import com.example.peggytsai.restaurantreservationapp.Check.CheckWaiterFragment;
import com.example.peggytsai.restaurantreservationapp.Manager.FoodManagerFragment;
import com.example.peggytsai.restaurantreservationapp.Member.MemberIndexFragment;
import com.example.peggytsai.restaurantreservationapp.Login.LoginFragment;
import com.example.peggytsai.restaurantreservationapp.Member.MemberManagerFragment;
import com.example.peggytsai.restaurantreservationapp.Menu.MenuFragment;
import com.example.peggytsai.restaurantreservationapp.Menu.MenuManagerFragment;
import com.example.peggytsai.restaurantreservationapp.Message.MessageFragment;
import com.example.peggytsai.restaurantreservationapp.Message.MessageManagerFragment;
import com.example.peggytsai.restaurantreservationapp.R;
import com.example.peggytsai.restaurantreservationapp.Rating.RatingFragment;
import com.example.peggytsai.restaurantreservationapp.Rating.RatingManagerFragment;
import com.example.peggytsai.restaurantreservationapp.Waiter.ServiceManagerFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigation;


    @SuppressLint("StringFormatInvalid")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.Navigation);
//        navigation.seton
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        initContent();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                //Customer menu item
                case R.id.item_Message:
                    fragment = new MessageFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_message);
                    return true;
                case R.id.item_Menu:
                    fragment = new MenuFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_menu);
                    return true;
                case R.id.item_Check:
                    fragment = new CheckFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_check);
                    return true;
                case R.id.item_Rating:
                    fragment = new RatingFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_rating);
                    return true;
                case R.id.item_Member:
                    fragment = new MemberIndexFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_member);
                    return true;

                //manager menu
                case R.id.item_FoodManager:
                    fragment = new FoodManagerFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_FoodManager);
                    return true;
                case R.id.item_MenuManager:
                    fragment = new MenuManagerFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_MenuManager);
                    return true;
                case R.id.item_MessageManager:
                    fragment = new MessageManagerFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_MessageManager);
                    return true;
                case R.id.item_RatingManager:
                    fragment = new RatingManagerFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_RatingManager);
                    return true;
                case R.id.item_MemberManager:
                    fragment = new MemberManagerFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_MemberManager);
                    return true;

                //waiter menu
                case R.id.item_CheckWaiter:
                    fragment = new CheckWaiterFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_CheckWaiter);
                    return true;
                case R.id.item_CheckManager:
                    fragment = new CheckManagerFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_CheckManager);
                    return true;
                case R.id.item_ServiceWaiter:
                    fragment = new ServiceManagerFragment();
                    switchFragment(fragment);
                    setTitle(R.string.text_ServiceWaiter);
                    return true;


                default:
                    initContent();
                    break;
            }
            return false;
        }

    };

    private void initContent() {
        Fragment fragment = new LoginFragment();
        navigation.setVisibility(BottomNavigationView.GONE);
        switchFragment(fragment);
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Content, fragment);
        fragmentTransaction.commit();
    }


}
