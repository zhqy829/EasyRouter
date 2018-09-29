package com.zhqydot.framework.easyrouter.second;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.zhqydot.framework.easyrouter.compiler.Route;
import com.zhqydot.framework.second.R;


/**
 * @author zhqy
 * @date 2018/9/16
 */

@Route(path = "second")
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent intent = new Intent();
        intent.putExtra("result", "second");
        setResult(RESULT_OK, intent);
        Bundle bundle = getIntent().getExtras();
        String data = bundle.getString("data1") + bundle.getInt("data2");
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }
}
