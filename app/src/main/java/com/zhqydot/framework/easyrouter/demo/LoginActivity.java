package com.zhqydot.framework.easyrouter.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhqydot.framework.easyrouter.R;
import com.zhqydot.framework.easyrouter.compiler.Route;

@Route(path = "/user/login")
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
