package com.saiyi.gymequipment.home.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.GymBuildConfig;
import com.saiyi.libfast.logger.Logger;
import com.squareup.picasso.Picasso;
import com.youth.banner.loader.ImageLoader;

import java.io.File;

/**
 * 广告轮播图片加载器
 */
public class PicassoImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        int pathInt;
        if (path instanceof Integer) {
            pathInt = (Integer) path;
            //Picasso 加载图片简单用法
            Picasso.with(context).load(pathInt).into(imageView);
        } else if (path instanceof File) {
            Picasso.with(context).load((File) path).into(imageView);
        } else if (path instanceof String) {
            String sPath = GymBuildConfig.BASE_HTTP_URL_IMAGE_URL + path;
            Picasso.with(context).load(sPath).placeholder(R.drawable.test_ad2).error(R.drawable.test_ad1).into(imageView);
        } else {
            return;
        }

    }
}
