package com.zhqydot.framework.easyrouter.demo;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.zhqydot.framework.easyrouter.R;
import com.zhqydot.framework.easyrouter.core.router.RouterManager;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(v ->
                RouterManager.routeForResult(this, "second", 0)
                        .filter(info -> info.resultCode == RESULT_OK)
                        .map(info -> info.data)
                        .subscribe(data -> Toast.makeText(this, "result is " + data.getExtras().getString("result"), Toast.LENGTH_SHORT).show()));

    }
}
