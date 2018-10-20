# EasyRouter
简单高效的路由框架<br>
支持对Activity的路由，并且与RxJava结合，高效处理Activity的返回结果<br><br>

简单使用
=======
#Step1:初始化
>public class MyApplication extends Application {
>>@Override
>>public void onCreate() {
>>>EasyRouter.init(this);
>>}
>}
