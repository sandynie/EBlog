package com.xiao.eblog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xiao.ebloglib.EBlogActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_goblog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_goblog = findViewById(R.id.btn_goblog);

        btn_goblog.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_goblog){
            Intent intent = new Intent(this, EBlogActivity.class);
            startActivity(intent);
        }
    }
}
