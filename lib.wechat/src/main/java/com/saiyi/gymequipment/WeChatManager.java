package com.saiyi.gymequipment;

import android.app.Activity;
import android.content.Context;

import com.saiyi.gymequipment.bean.AccessToken;
import com.saiyi.gymequipment.bean.RefreshToken;
import com.saiyi.gymequipment.bean.WUser;
import com.saiyi.gymequipment.utils.GsonUtil;
import com.saiyi.gymequipment.wxapi.WXEntryActivity;
import com.saiyi.libwechat.BuildConfig;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by JingNing on 2018-07-13 17:12
 */
public class WeChatManager {
    public static final String TAG = "WeChatManager";

    private AuthListener listener;

    private AccessToken accessToken;
    private WUser wUser;

    private static volatile WeChatManager INSTANCE;

    private WeChatManager() {
    }

    public static WeChatManager getInstance() {
        if (INSTANCE == null) {
            synchronized (WeChatManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WeChatManager();
                }
            }
        }
        return INSTANCE;
    }

    public void setListener(AuthListener listener) {
        this.listener = listener;
    }

    public AuthListener getListener() {
        return listener;
    }

    public boolean loginToWeiXin(Context context) {
        IWXAPI mApi = WXAPIFactory.createWXAPI(context, BuildConfig.WXAPPID, true);
        mApi.registerApp(BuildConfig.WXAPPID);

        if (mApi != null && mApi.isWXAppInstalled()) {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "gymequipment_wechat";
            mApi.sendReq(req);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取openid accessToken值用于后期操作
     *
     * @param code 请求码
     */
    public String getAccessToken(final Activity activity, final String code) {
        String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + BuildConfig.WXAPPID
                + "&secret="
                + BuildConfig.WXAPPSECRET
                + "&code="
                + code
                + "&grant_type=authorization_code";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(path)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loginErr(activity);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String msg = response.body().string();
                accessToken = new AccessToken();
                accessToken = GsonUtil.GsonToBean(msg, AccessToken.class);
                getUserMesg(activity, accessToken.getAccess_token(), accessToken.getOpenid());
            }
        });
        return path;
    }

    /**
     * 刷新token
     * @param refresh_token
     */
    public void refreshAccessToken(final String refresh_token) {
        String path = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid="
                + BuildConfig.WXAPPID
                + "&grant_type=refresh_token&refresh_token="
                + refresh_token;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(path)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                loginErr(null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String msg = response.body().string();
                WLogger.d(TAG, "请求用户信息:  body：" + msg);
                RefreshToken refreshToken = new RefreshToken();
                refreshToken = GsonUtil.GsonToBean(msg, RefreshToken.class);
                accessToken = new AccessToken();
                accessToken.setAccess_token(refreshToken.getAccess_token());
                accessToken.setExpires_in(refreshToken.getExpires_in());
                accessToken.setRefresh_token(refreshToken.getRefresh_token());
                accessToken.setOpenid(refreshToken.getOpenid());
                accessToken.setScope(refreshToken.getScope());
                getUserMesg(null, accessToken.getAccess_token(), accessToken.getOpenid());
            }
        });
    }

    /**
     * 获取微信的个人信息
     *
     * @param access_token
     * @param openid
     */
    private void getUserMesg(final Activity activity, final String access_token, final String openid) {
        String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + access_token
                + "&openid="
                + openid;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(path)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                loginErr(activity);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String msg = response.body().string();
                wUser = new WUser();
                wUser = GsonUtil.GsonToBean(msg, WUser.class);
                if (WeChatManager.getInstance().getListener() != null) {
                    WeChatManager.getInstance().getListener().onSuccess(accessToken, wUser);
                }
                if(activity!= null){
                    activity.finish();
                }
            }
        });
    }

    public void loginErr(Activity activity) {
        if (activity != null) {
            activity.finish();
        }
        if (listener != null) {
            listener.onFailed();
        }
    }

    public void release() {
        if (listener != null) {
            listener = null;
        }

    }

    //授权回调
    public interface AuthListener {

        void onSuccess(AccessToken accessToken, WUser wUser);

        void onFailed();
    }
}
