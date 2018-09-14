package com.zhqydot.framework.easyrouter.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhqydot.framework.easyrouter.R;
import com.zhqydot.framework.easyrouter.compiler.Router;

@Router(path = "second")
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent intent = new Intent();
        intent.putExtra("result", "second");
        setResult(RESULT_OK, intent);
    }
}
