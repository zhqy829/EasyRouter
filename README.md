# EasyRouter
简单高效的路由框架<br>
支持对Activity的路由，并且与RxJava结合，高效处理Activity的返回结果<br><br>

使用方式:<br>
> Step1:初始化，推荐在Application中进行<br>
&nbsp;&nbsp;public class MyApplication extends Application {<br>
&nbsp;&nbsp;&nbsp;&nbsp;@Override<br>
&nbsp;&nbsp;&nbsp;&nbsp;public void onCreate() {<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;super.onCreate();<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;RouterManager.init(this);<br>
&nbsp;&nbsp;&nbsp;&nbsp;}<br>
&nbsp;&nbsp;}<br>
  <br>
> Step2:在需要路由的Activity添加注解<br>
  @Route(path = "second")<br>
  public class SecondActivity extends AppCompatActivity {<br>
  <br>
  ｝<br>
  <br>
> Step3:路由跳转，处理Activity返回<br>
  RouterManager.routeTo(this, "second")<br>
                .withString("data1", "Come from main:")<br>
                .withInt("data2", 10)<br>
                .navigationForResult(0)<br>
                .filter(info -> info.resultCode == RESULT_OK)<br>
                .map(info -> info.data)<br>
                .subscribe(data -> Toast.makeText(this, "result is " + data.getExtras().getString("result"), <br>
                  Toast.LENGTH_SHORT).show());<br>
