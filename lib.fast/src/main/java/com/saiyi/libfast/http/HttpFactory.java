package com.saiyi.libfast.http;

import android.content.Context;
import android.support.annotation.NonNull;

import com.saiyi.libfast.utils.FileUtils;
import com.saiyi.libfast.utils.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by siwei on 2018/3/15.
 */

public class HttpFactory {

    private Interceptor[] mInterceptors;
    private Retrofit mRetrofit;
    private static HttpFactory instance;
    private static final long CACHE_SIZE = 1024 * 1024 * 10;//缓存的文件大小
    private static final int MOBILE_ENABLE_MAX_AGE = 60 * 60;//有网络时 设置缓存超时时间1个小时
    private static final int MOBILE_UNENABLE_MAX_STALE = 60 * 60 * 24;//无网络时，设置超时为1天
    private static final int CONNECT_TIMEOUT = 10;//连接超时10s
    private static final int READ_TIMEOUT = 15;//读取超时15s
    private static final int WRITE_TIMEOUT = 20;//写入超时20s

    public static void initFactory(@NonNull Context context, @NonNull String baseUrl) {
        instance = new HttpFactory(context, baseUrl);
    }

    public static void initFactory(@NonNull Context context, @NonNull String baseUrl, Interceptor... interceptors) {
        instance = new HttpFactory(context, baseUrl, interceptors);
    }

    public static HttpFactory instance() {
        if (instance == null)
            throw new NullPointerException("not call HttpFactory.initFactory(context, baseUrl)");
        return instance;
    }

    private HttpFactory(Context context, String baseUrl, Interceptor... interceptors) {
        this.mInterceptors = interceptors;
        mRetrofit = initRetrofit(context, baseUrl);
    }

    private Retrofit initRetrofit(Context context, String baseUrl) {
        return new Retrofit.Builder()
                .client(genericClient(context))
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public <T> T createApiService(Class<T> service) {
        return mRetrofit.create(service);
    }

    private OkHttpClient genericClient(final Context context) {
        File cacheFile = FileUtils.getAppHttpCacheDir(context);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        Cache cache = new Cache(cacheFile, CACHE_SIZE);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .cache(cache)
                .addInterceptor(loggingInterceptor)
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        //有网络缓存时间为1小时，无网络缓存时间为1天
                        if (NetworkUtils.isMobileDataEnabled(context)) {
                            //有网络时只从网络获取
                            request = request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK).build();
                        } else {
                            //无网络时只从缓存中读取
                            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                        }
                        Response response = chain.proceed(request);
                        if (NetworkUtils.isMobileDataEnabled(context)) {
                            response = response.newBuilder()
                                    .removeHeader("Pragma")
                                    .header("Cache-Control", "public, max-age=" + MOBILE_ENABLE_MAX_AGE)
                                    .build();
                        } else {
                            response = response.newBuilder()
                                    .removeHeader("Pragma")
                                    .header("Cache-Control", "public, only-if-cached, max-stale=" + MOBILE_UNENABLE_MAX_STALE)
                                    .build();
                        }
                        return response;
                    }
                })
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Content-Type", "application/json; charset=UTF-8")
                                //.addHeader("Content-Type", "multipart/form-data; charset=UTF-8")
                                .build();
                        return chain.proceed(request);
                    }
                });
        if(mInterceptors != null){
            for(Interceptor interceptor : mInterceptors){
                builder.addInterceptor(interceptor);
            }
        }
        OkHttpClient httpClient = builder.build();
        return httpClient;
    }
}
