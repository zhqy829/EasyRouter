package com.zhqydot.framework.easyrouter.demo;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.zhqydot.framework.easyrouter.R;
import com.zhqydot.framework.easyrouter.compiler.Route;
import com.zhqydot.framework.easyrouter.core.common.EasyRouter;

/**
 * @author zhqy
 * @date 2018/9/16
 */

@Route(path = "/user/main")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(v -> testRoute());
        EasyRouter.addFilter((path, data) -> {
            Log.e("MainActivity", "filter 1");
            return true;
        }, 1);
        EasyRouter.addFilter((path, data) -> {
            Log.e("MainActivity", "filter 2");
            return true;
        }, 2);
        EasyRouter.addFilter((path, data) -> {
            Log.e("MainActivity", "filter 3");
            return true;
        }, "user", 3);
    }

    @SuppressLint("CheckResult")
    private void testRoute() {
        EasyRouter.routeTo(this, "/user/second")
                .withString("data1", "Come from main:")
                .withInt("data2", 10)
                .navigationForResult(0)
                .doOnNext(info -> {
                    if (info.resultCode == EasyRouter.RESULT_FILTER) {
                        Toast.makeText(this, "请求被拦截", Toast.LENGTH_SHORT).show();
                    }
                })
                .filter(info -> info.resultCode == RESULT_OK)
                .map(info -> info.data)
                .subscribe(data -> Toast.makeText(this, "result is " + data.getExtras().getString("result"), Toast.LENGTH_SHORT).show());
    }
}
