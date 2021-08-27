package com.xh.play.fragments;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xh.play.R;
import com.xh.play.activitys.MainActivity;

import butterknife.BindView;

public class SplashFragment extends BaseFragment {
    @BindView(R.id.fragment_splash_iv)
    ImageView imageView;
    @BindView(R.id.fragment_splash_tv)
    TextView textView;
    private boolean show;
    private String url;


    public void show() {
        show = true;
        if (textView != null)
            textView.setVisibility(View.VISIBLE);
    }

    public SplashFragment setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public void bindView() {
        super.bindView();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                application.splash = true;
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
        });
        if (show)
            textView.setVisibility(View.VISIBLE);
        if (url == null || url.isEmpty())
            imageView.setImageResource(R.mipmap.splash);
        else
            imageLoad.load(url, imageView, null);
    }


    @Override
    public int layout() {
        return R.layout.fragment_splash;
    }
}
