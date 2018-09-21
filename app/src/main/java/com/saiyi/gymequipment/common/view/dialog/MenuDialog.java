package com.saiyi.gymequipment.common.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.saiyi.gymequipment.R;

/**
 * 菜单弹窗
 */
public abstract class MenuDialog extends BaseDialog {

    private ViewGroup.LayoutParams fullWidthScreenLp;

    public MenuDialog(@NonNull Context context) {
        super(context, R.style.menu_dlaog);
    }

    public MenuDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MenuDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void initDialog() {
        super.initDialog();
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        int width = display.getWidth();
        //设置dialog的宽高为屏幕的宽高
        fullWidthScreenLp = new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void setContentView(@NonNull View view) {
        super.setContentView(view, fullWidthScreenLp);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
    }

}