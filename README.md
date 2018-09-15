# EasyRouter
简单高效的路由框架
支持对Activity的路由，并且与RxJava结合，高效处理Activity的返回结果

使用方式:
> Step1:初始化，推荐在Application中进行
  public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RouterManager.init(this);
    }
  }
  
> Step2:在需要路由的Activity添加注解
  @Route(path = "second")
  public class SecondActivity extends AppCompatActivity {
  
  ｝
  
> Step3:路由跳转，处理Activity返回
  RouterManager.routeTo(this, "second")
                .withString("data1", "Come from main:")
                .withInt("data2", 10)
                .navigationForResult(0)
                .filter(info -> info.resultCode == RESULT_OK)
                .map(info -> info.data)
                .subscribe(data -> Toast.makeText(this, "result is " + data.getExtras().getString("result"), 
                  Toast.LENGTH_SHORT).show());
