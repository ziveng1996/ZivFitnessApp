package com.example.zivsfitnessapp.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.example.zivsfitnessapp.R;
import com.google.android.material.appbar.MaterialToolbar;

public class MyToolbarView extends LinearLayout {

    private MaterialToolbar toolbar;

    public MyToolbarView(@NonNull Context context) {
        super(context);
        init();
    }

    public MyToolbarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyToolbarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_toolbar, this);
        toolbar = view.findViewById(R.id.my_toolbar);
    }

    public void setText(@StringRes int resource) {
        toolbar.setTitle(resource);
    }

    public void setText(String text) {
        toolbar.setTitle(text);
    }

    public void setNavigationOnClickListener(View.OnClickListener listener){
        toolbar.setNavigationOnClickListener(listener);
    }
}
