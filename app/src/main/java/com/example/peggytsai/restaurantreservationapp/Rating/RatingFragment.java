package com.example.peggytsai.restaurantreservationapp.Rating;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peggytsai.restaurantreservationapp.Main.Common;
import com.example.peggytsai.restaurantreservationapp.Main.MyTask;
import com.example.peggytsai.restaurantreservationapp.Message.MessageDetailFragment;
import com.example.peggytsai.restaurantreservationapp.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RatingFragment extends Fragment {

    private static final String TAG = "RatingFragment";
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyTask ratingGetAllTask;
    private TextView btRating;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating, container, false);

        TextView tvtoolBarTitle = view.findViewById(R.id.tvTool_bar_title);

        SharedPreferences pref = getActivity().getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        int authority_id = pref.getInt("authority_id", 1);

        if (authority_id == 4) {
            tvtoolBarTitle.setText(R.string.text_RatingManager);

        } else if (authority_id == 1) {
            tvtoolBarTitle.setText(R.string.text_rating);

            btRating = view.findViewById(R.id.btRating);
            btRating.setVisibility(BottomNavigationView.VISIBLE);
            btRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment ratingNewFragment = new RatingNewFragment();
                    Common.switchFragment(ratingNewFragment, getActivity(), false);

                }
            });
        }

        recyclerView = view.findViewById(R.id.rvRating);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RatingAdapter(showAllRing(), getActivity(), getFragmentManager()));
        swipeRefreshLayout =
                view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //設定動畫
                swipeRefreshLayout.setRefreshing(true);
                showAllRing();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private List<RatingPage> showAllRing() {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "/RatingServlet";
            List<RatingPage> ratings = null;
            JsonObject jsonObject = new JsonObject();
            //定key（跟server開發者訂好即可）
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            ratingGetAllTask = new MyTask(url, jsonOut);
            try {
                String jsonIn = ratingGetAllTask.execute().get();
                Log.d(TAG, jsonIn);
                Type listType = new TypeToken<List<RatingPage>>() {
                }.getType();
                ratings = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (ratings == null || ratings.isEmpty()) {
                Common.showToast(getActivity(), R.string.msg_NoMessagesFound);
            } else {
                return ratings;
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }

        return null;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (ratingGetAllTask != null) {
            ratingGetAllTask.cancel(true);
        }
    }


}