package com.htsc.myapplication.view;

/**
 * Created by zhangxiaoting on 2017/5/16.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import com.htsc.myapplication.R;


/**
 * 内部类 专门用于装载popupwindow的内容
 */
public class LongPressContent extends LinearLayout {
    private static final String TAG = "LongPressMenuContent";
    private Paint mPaint; // 画笔
    private Path mPath;   // 三角形由三条线组成的闭合区域
    private int mTriangleWidth; // 三角形宽度
    private int mTriangleHeight; // 三角形高度
    private final double RADIO_TRIANGLE_WIDTH = 0.08;
    private LinearLayout mWrapper; // 用来装几个菜单键的壳子
    private List<String> mMenuTitle = Arrays.asList("删除", "置顶", "删除全部");
    private LongPressMenu.OnMenuClickLister mMenuClickLister;


    public LongPressContent(Context context) {
        this(context, null);
    }

    public LongPressContent(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LongPressContent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.long_press_menu_triangle_color));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));    // 设置三角形三个角为圆角效果
    }

    public void setMenuClickLister(LongPressMenu.OnMenuClickLister lister) {
        this.mMenuClickLister = lister;
    }

    public void initMenus() {
        mWrapper = new LinearLayout(getContext());
        mWrapper.setDividerDrawable(getResources().getDrawable(R.drawable.market_tab_divider));
        mWrapper.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        mWrapper.setBackgroundResource(R.drawable.long_press_menu_bg);

        this.removeAllViews();
        for (String tile : mMenuTitle) {
            mWrapper.addView(generateView(tile));
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.width = getWidth();
        lp.height = getHeight() * 3 / 4;
        mWrapper.setLayoutParams(lp);

        addView(mWrapper);
        setItemClickEvent();
        postInvalidate();
    }

    /**
     * 产生单个TextView
     *
     * @param tile TextView的标题
     * @return 生成的TextView
     */
    private View generateView(String tile) {
        TextView tabItem = new TextView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置当前TextView的布局参数
        tabItem.setText(tile);
        tabItem.setGravity(Gravity.CENTER);
        tabItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        tabItem.setTextColor(getResources().getColor(android.R.color.white));
        // 3.5=1+1+1.5 "编辑全部"的宽度应该时"删除" "置顶"的1.5倍
        lp.width = (int) (getWidth() / 3.8);
        if ("删除全部".equals(tile)) {
            lp.width = (int) (lp.width * 1.8);
        }
        tabItem.setLayoutParams(lp);
        return tabItem;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d(TAG, "onFinishInflate()");
    }

    /**
     * 设置tab的点击事件
     */
    private void setItemClickEvent() {
        for (int i = 0; i < mWrapper.getChildCount(); i++) {
            View view = mWrapper.getChildAt(i);
            final int finalI = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMenuClickLister != null) {
                        mMenuClickLister.onMenuClick(finalI);
                    }
                }
            });
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Log.d(TAG, "dispatchDraw()");
        canvas.save();
        // getHeight()为整个MENU的高度，已经在xml布局中指定为45dp，-1表示向上移动1dp
        canvas.translate((float) (mWrapper.getWidth() * RADIO_TRIANGLE_WIDTH), mWrapper.getHeight() - 1);
        canvas.drawPath(mPath, mPaint);   // mPath就是三角形三条边，利用画笔mPaint画出来
        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged()");
        initMenus();
        mTriangleWidth = (int) (w * RADIO_TRIANGLE_WIDTH);
        mTriangleHeight = mTriangleWidth / 2;  // 等腰三角形，高度是底边的一半
        initTriangle();
    }

    /**
     * 初始化三角形
     */
    private void initTriangle() {
        mPath = new Path();
        mPath.moveTo(0, 0);  // 从(0, 0)点开始划线
        mPath.lineTo(mTriangleWidth, 0);
        mPath.lineTo(mTriangleWidth / 2, mTriangleHeight); // 为什么为负数，因为Y轴向下为正，向上为负
        mPath.close();
    }
}

