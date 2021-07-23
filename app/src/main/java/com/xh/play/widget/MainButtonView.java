package com.xh.play.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xh.play.R;


public class MainButtonView extends FrameLayout {
    private int defultColor = Color.BLACK;
    private int selectColor = Color.RED;
    private Drawable defultImage = new ColorDrawable(Color.WHITE);
    private Drawable selectImage = new ColorDrawable(Color.YELLOW);
    private ImageView imageView;
    private TextView textView;

    //    private
    public MainButtonView(Context context) {
        super(context);
        init(null);
    }

    public MainButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MainButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public MainButtonView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        String text = "";
        if (attrs != null) {
            TypedArray a = getResources().obtainAttributes(attrs, R.styleable.MainButtonView);
            defultColor = a.getColor(R.styleable.MainButtonView_defaultTextColor, defultColor);
            selectColor = a.getColor(R.styleable.MainButtonView_selectTextColor, selectColor);
            Drawable drawable = a.getDrawable(R.styleable.MainButtonView_defaultImage);
            if (drawable != null)
                defultImage = drawable;
            drawable = a.getDrawable(R.styleable.MainButtonView_selectImage);
            if (drawable != null)
                selectImage = drawable;
            text = a.getString(R.styleable.MainButtonView_button_text);
            a.recycle();
        }
        View view = LayoutInflater.from(getContext()).inflate(R.layout.main_button, null);
        imageView = view.findViewById(R.id.main_button_iv);
        textView = view.findViewById(R.id.main_button_tv);
        imageView.setImageDrawable(defultImage);
        textView.setTextColor(defultColor);
        if (text != null && !text.isEmpty())
            textView.setText(text);
        addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public void select(boolean select) {
        if (select) {
            imageView.setImageDrawable(selectImage);
            textView.setTextColor(selectColor);
        } else {
            imageView.setImageDrawable(defultImage);
            textView.setTextColor(defultColor);
        }
    }
}