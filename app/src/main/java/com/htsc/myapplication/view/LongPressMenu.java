package com.htsc.myapplication.view;

/**
 * Created by zhangxiaoting on 2017/5/16.
 */

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.htsc.myapplication.R;

/**
 * 用于长按自选股item时出现的弹窗menu
 * Created by zhangxiaoting on 2016/11/15.
 */
public class LongPressMenu extends PopupWindow {
    protected static final String TAG = "HtscLongPressMenu";
    private Context mContext;

    public LongPressMenu(Context context, OnMenuClickLister lister) {
        this.mContext = context;
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.long_press_menu_layout, null);
        LongPressContent menuContent = (LongPressContent) convertView.findViewById(R.id.long_press_menu_content);
        menuContent.setMenuClickLister(lister);
        setContentView(convertView);
        setFocusable(false);
        setTouchable(true);
        setOutsideTouchable(true); //可以点击popupWindow的其他区域
        setBackgroundDrawable(new BitmapDrawable()); //保证点击外部可以消失

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        // 设置popupwindow的宽和高 宽是屏幕一半 高是48dp
        setWidth(outMetrics.widthPixels / 2);
        setHeight((int) (48 * outMetrics.density));
        //点击外部区域的效果
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    //如果点击外部，就消失
                    dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    public interface OnMenuClickLister {
        void onMenuClick(int position);
    }

    /**
     * 显示pop window
     *
     * @param view 相对view位置
     */
    public void showPopupWindow(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        // 设置popupwindow显示位置, 右移15dp 上移3/4的弹窗高度
        showAtLocation(view, Gravity.NO_GRAVITY, (int) (location[0] + 15 * outMetrics.density), location[1] - getHeight() * 3 / 4);
    }
}

