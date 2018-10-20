# EasyRouter
简单高效的路由框架<br>
支持对Activity的路由，并且与RxJava结合，高效处理Activity的返回结果<br><br>

简单使用
=======
#Step1:初始化，推荐在Application中进行  
```
public class MyApplication extends Application {
  @Override
  public void onCreate() {
    EasyRouter.init(this);
  }
}
```
#Step2:在需要路由的Activity添加注解，路径至少需要两级，如/user/second
```
@Route(path = "/user/second")
public class SecondActivity extends AppCompatActivity {

}
```
#Step3:路由跳转，处理Activity返回
```
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
                .subscribe(data -> Toast.makeText(this, "result is " + data.getExtras().getString("result"),
                                      Toast.LENGTH_SHORT).show());
```
***注意，如果不需要处理Actvitiy返回结果时，可以调用EasyRouter.routeTo(String path)方法，此时Activity将在新任务栈中启动
如果需要处理Actvitiy的返回结果，则必须调用EasyRouter.routeTo(Activity activity, String path)该方法，否则会抛出异常。***
