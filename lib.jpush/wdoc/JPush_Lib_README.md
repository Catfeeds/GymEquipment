##版本信息
Version:0.0.1  
Auth:siwei  
JPushVersion:3.1.1

##快速集成
###1.AppKey填写
在项目下的gradle.properties文件中填写JPUSH_APPKEY=value(value为申请的appkey)

###2.包名的填写
在AndroidManifest.xml中manifest标签下的package属性中填写你申请时填写的App的包名

###3.JPush SDK的初始化
在Application.onCreate()中调用JPushSDK.initSdk(Application application, boolean isDebug)去初始化SDK

###4.JPush SDK的释放
在Application.onTerminate()中调用JPushSDK.releaseSDK()去释放SDK

###5.申请的权限
#####A.必须需要申请的权限
RECEIVE_USER_PRESENT	允许应用可以接收点亮屏幕或解锁广播。  
INTERNET	允许应用可以访问网络。  
WAKE_LOCK	允许应用在手机屏幕关闭后后台进程仍然运行  
READ_PHONE_STATE	允许应用访问手机状态。  
WRITE_EXTERNAL_STORAGE	允许应用写入外部存储。  
READ_EXTERNAL_STORAGE	允许应用读取外部存储。  
WRITE_SETTINGS	允许应用读写系统设置项。  
VIBRATE	允许应用震动。  
MOUNT_UNMOUNT_FILESYSTEMS	允许应用挂载/卸载 外部文件系统。  
ACCESS_NETWORK_STATE	允许应用获取网络信息状态，如当前的网络连接是否有效。

#####B.可以申请的权限
SYSTEM_ALERT_WINDOW  
ACCESS_COARSE_LOCATION  
CHANGE_WIFI_STATE  
ACCESS_FINE_LOCATION  
ACCESS_LOCATION_EXTRA_COMMANDS  
CHANGE_NETWORK_STATE  
GET_TASKS  

##How to use

###1.添加消息监听
```java
MsgCenterManager.instance().addReceiver(new JPushMsgListener() {
                    @Override
                    public void onRegistrationID(Context context, Intent intent) {
                        //接收Registration Id
                    }
        
                    @Override
                    public void onMsgReceived(Context context, Intent intent) {
                        //接收到推送下来的自定义消息
                    }
        
                    @Override
                    public void onNotificationReceived(Context context, Intent intent) {
                        //接收到推送下来的通知
                    }
        
                    @Override
                    public void onNotificationOpened(Context context, Intent intent) {
                        //用户点击打开了通知
                    }
        
                    @Override
                    public void onRitchpushCallBack(Context context, Intent intent) {
                        //用户收到到RICH PUSH CALLBACK
                    }
        
                    @Override
                    public void onConnectionChange(Context context, Intent intent) {
                        //connected state change
                    }
        
                    @Override
                    public void onOther(Context context, Intent intent) {
                        //其他
                    }
                });
```

###2.监听取消
在添加了监听之后记得一定要去取消监听,否则容易导致内存溢出
```java
MsgCenterManager.instance().removeReceiver(mCmdResponseListener);
```

###3.测试调试,在初始化的时候需要开启调试
a.开启之后在logcat中过滤标签为JPushSDK即可看到所有的测试信息  
b.在logcat中过滤JPushMsgReceiver可以看到接收到的推送消息  
c.tag,alias和手机号设置中过滤tag或alias

##常见问题和解决方法