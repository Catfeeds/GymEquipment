##版本信息
Version:0.0.1  
Auth:jingningyun   

##快速集成
###1.AppID填写
在项目下的gradle.properties文件中填写WX_APPID=your appid, WX_APPSECRET=your app_secret

###2.设置监听
在登录的activity.onCreate()里执行
```java
WeChatManager.getInstance().setListener(authListener);
```

###3.点击微信登录
在点击的OnClick里执行
```java
WeChatManager.getInstance().loginToWeiXin(this);
```

###4.监听用户授权，并获取数据
创建AuthListener，并通过步骤2注册监听
```java
protected WeChatManager.AuthListener authListener = new WeChatManager.AuthListener() {

        @Override
        public void onSuccess(AccessToken accessToken, WUser wUser) {
           toast("微信授权成功");
        }

        @Override
        public void onFailed() {
            toast("微信授权失败");
        }
    };
```
两个返回实体对应的信息
```java
public class AccessToken {
    private String access_token;  //接口调用凭证
    private int expires_in;     //access_token接口调用凭证超时时间，单位（秒）
    private String refresh_token; //用户刷新access_token
    private String openid;  // 授权用户唯一标识
    private String scope;   //用户授权的作用域，使用逗号（,）分隔
    private String unionid;     //当且仅当该移动应用已获得该用户的userinfo授权时，才会出现该字段
}

public class WUser {
    private String openid;      //普通用户的标识，对当前开发者帐号唯一
    private String nickname;    //普通用户昵称
    private int sex;            //普通用户性别，1为男性，2为女性
    private String province;    //普通用户个人资料填写的省份
    private String city;        //普通用户个人资料填写的城市
    private String country;     //国家，如中国为CN
    private String headimgurl;  //用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
    private String[] privilege; //用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
    private String unionid;     //用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
}
```



###5.释放监听
在activity.onDestroy()里执行
```java
WeChatManager.getInstance().release();
```
###6.申请的权限
INTERNET   
ACCESS_NETWORK_STATE    
ACCESS_WIFI_STATE    
READ_PHONE_STATE     
WRITE_EXTERNAL_STORAGE



##How to use



##常见问题和解决方法