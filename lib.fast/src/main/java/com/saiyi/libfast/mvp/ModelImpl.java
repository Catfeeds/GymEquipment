package com.saiyi.libfast.mvp;

import android.database.sqlite.SQLiteDatabase;

import com.saiyi.libfast.activity.BaseApplication;
import com.saiyi.libfast.cache.ACache;
import com.saiyi.libfast.http.HttpFactory;

import org.litepal.LitePal;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ModelImpl implements IModel {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected <T> T createRetorfitService(Class<T> service) {
        return HttpFactory.instance().createApiService(service);
    }

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    /**
     * add Disposable
     */
    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    /**
     * add Disposable
     */
    protected void addAllDisposable(Disposable... disposables) {
        compositeDisposable.addAll(disposables);
    }

    /**
     * 取消掉所有的订阅
     */
    private void disposableAll() {
        if (compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        compositeDisposable.clear();
    }

    /**
     * 获取缓存
     */
    protected ACache getCache() {
        return BaseApplication.getInstance().getCache();
    }

    /**
     * 获取数据库操作(LitePal:<a>https://www.jianshu.com/p/bc68e763c7a2</a>)
     * 数据库映射关系在asset/litepal.xml中
     */
    protected SQLiteDatabase getDB() {
        return LitePal.getDatabase();
    }

    protected void readFile() {
        //读取文件
    }

    //其余一些数据操作的封装....

    @Override
    public void onRelease() {
        disposableAll();
    }

}
