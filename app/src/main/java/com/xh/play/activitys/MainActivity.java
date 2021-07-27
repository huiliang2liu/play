package com.xh.play.activitys;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xh.play.R;
import com.xh.play.adapter.FragmentAdapter;
import com.xh.play.fragments.LiveFragment;
import com.xh.play.fragments.MoveFragment;
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
    @BindView(R.id.activity_main_live)
    TextView live;
    @BindView(R.id.activity_main_move)
    TextView move;
    List<TextView> buttons = new ArrayList<>(2);
    private FragmentAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        savedInstanceState.p
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        buttons.add(findViewById(R.id.activity_main_live));
        buttons.add(findViewById(R.id.activity_main_move));
        for (TextView tv : buttons)
            tv.setOnClickListener(this);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new LiveFragment());
        fragments.add(new MoveFragment());
        adapter = new FragmentAdapter(this, fragments);
        mainVp.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        for (TextView tv : buttons) {
            tv.setTextColor(Color.BLACK);
            tv.setBackgroundColor(Color.WHITE);
        }
        TextView tv = (TextView) v;
        tv.setBackgroundColor(Color.RED);
        tv.setTextColor(Color.WHITE);
        mainVp.setCurrentItem(buttons.indexOf(tv), false);
    }
}