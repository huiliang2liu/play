package com.xh.play.activitys;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xh.play.PlayApplication;
import com.xh.play.R;
import com.xh.play.adapter.FragmentAdapter;
import com.xh.play.fragments.HomeFragment;
import com.xh.play.fragments.AkmjFragment;
import com.xh.play.fragments.YsdqFragment;
import com.xh.play.platforms.IPlatforms;
import com.xh.play.widget.MainButtonView;
import com.xh.play.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    @BindView(R.id.main_vp)
    ViewPager mainVp;
    @BindView(R.id.activity_main_ll)
    LinearLayout linearLayout;
    private FragmentAdapter adapter;
    private PlayApplication application;
    private List<TextView> textViews = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        savedInstanceState.p
        super.onCreate(savedInstanceState);
        application = (PlayApplication) getApplication();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        textViews.add(createTab("首页"));
        for (IPlatforms platforms : application.platforms) {
            fragments.add(new AkmjFragment().setPlatforms(platforms));
            textViews.add(createTab(platforms.name()));
        }
//        IPlatforms platforms = application.platforms.get(application.platforms.size() - 1);
//        fragments.add(new AkmjFragment().setPlatforms(platforms));
//        textViews.add(createTab(platforms.name()));
        adapter = new FragmentAdapter(this, fragments);
        mainVp.setAdapter(adapter);
        for (TextView textView : textViews)
            linearLayout.addView(textView);
        textViews.get(0).setTextColor(Color.RED);
    }

    private TextView createTab(String name) {
        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setText(name);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (TextView view : textViews)
                    view.setTextColor(Color.BLACK);
                int index = textViews.indexOf(v);
                if (index >= 0) {
                    textViews.get(index).setTextColor(Color.RED);
                    mainVp.setCurrentItem(index, false);
                }
            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    @Override
    public void onClick(View v) {

    }
}