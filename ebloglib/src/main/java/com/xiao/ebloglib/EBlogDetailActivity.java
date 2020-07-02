package com.xiao.ebloglib;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EBlogDetailActivity extends AppCompatActivity {

    private TextView mTvTitle;
    private TextView mTvContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eblog_detail);
        mTvTitle = findViewById(R.id.tv_detail_title);
        mTvContent = findViewById(R.id.tv_detail_detail);

        String title = getIntent().getStringExtra(EBlogConst.TITLE_KEY);
        if (!TextUtils.isEmpty(title)) {
            mTvTitle.setText(title);
        }


        String content = getIntent().getStringExtra(EBlogConst.CONTENT_KEY);
        if (!TextUtils.isEmpty(content)) {
            mTvContent.setText(content);
        }


    }

}
