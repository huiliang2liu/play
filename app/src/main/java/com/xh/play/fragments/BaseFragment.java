package com.xh.play.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xh.play.HttpManager;
import com.xh.play.PlayApplication;
import com.xh.image.IImageLoad;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment extends Fragment {
    private final static String TAG = "BaseFragment";
    private boolean visible = false;
    protected View view;
    protected IImageLoad imageLoad;
    protected PlayApplication application;
    protected HttpManager httpManager;
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (PlayApplication) getContext().getApplicationContext();
        imageLoad = application.imageLoad;
        httpManager = application.httpManager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(layout(), null);
            unbinder = ButterKnife.bind(this,view);
            bindView();
            if (visible)
                visible();
        }
        return view;
    }


    public void bindView() {

    }


    public abstract int layout();


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (visible)
                inVisible();
            visible = false;
        } else {
            if (!visible && view != null)
                visible();
            visible = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!visible && view != null)
                visible();
            visible = true;
        } else {
            if (visible)
                inVisible();
            visible = false;
        }
    }

    protected void visible() {
        Log.e(TAG, "visible");
    }

    protected void inVisible() {
        Log.e(TAG, "invisible");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
