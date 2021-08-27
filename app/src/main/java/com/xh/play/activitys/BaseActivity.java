package com.xh.play.activitys;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.xh.image.IImageLoad;
import com.xh.play.HttpManager;
import com.xh.play.PlayApplication;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends FragmentActivity {
    protected IImageLoad imageLoad;
    protected HttpManager httpManager;
    protected PlayApplication application;
    private Unbinder unbinder;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (PlayApplication) getApplication();
        httpManager = application.httpManager;
        imageLoad = application.imageLoad;
        setContentView(layout());
        unbinder=ButterKnife.bind(this);
        bindData();
    }

    protected abstract int layout();

    protected void bindData(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
