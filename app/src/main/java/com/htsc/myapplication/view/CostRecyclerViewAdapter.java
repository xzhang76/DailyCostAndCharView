package com.htsc.myapplication.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.htsc.myapplication.CostBean;
import com.htsc.myapplication.R;
import com.htsc.myapplication.db.MyDataBaseHelper;

import java.util.List;

public class CostRecyclerViewAdapter extends RecyclerView.Adapter<CostRecyclerViewAdapter.MyViewHolder> {
    private List<CostBean> mListCost;
    private LayoutInflater mInflater;
    protected LongPressMenu mLongPressMenu;
    private Context mContext;
    private int mLongPressPosition = -1; // 纪录当前长按的位置
    private MyDataBaseHelper mDataBaseHelper;
    private static final String TAG = "CostRecyclerViewAdapter";

    public CostRecyclerViewAdapter(Context context, List<CostBean> list) {
        this.mContext = context;
        this.mListCost = list;
        this.mInflater = LayoutInflater.from(context);
        this.mDataBaseHelper = MyDataBaseHelper.geInstance(context);
    }

    public void setDataList(List<CostBean> beans) {
        this.mListCost = beans;
        mDataBaseHelper = MyDataBaseHelper.geInstance(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        CostBean costBean = mListCost.get(position);
        holder.costTitle.setText(costBean.getCostTitle());
        holder.costDate.setText(costBean.getCostDate());
        holder.costMoney.setText(costBean.getCostMoney());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "onLongClick()");
                if (mLongPressMenu == null) {
                    mLongPressMenu = new LongPressMenu(mContext, mMenuClickListener);
                    mLongPressMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            onlyNotifyDataChanged();
                        }
                    });
                }
                mLongPressMenu.showPopupWindow(v);
                mLongPressPosition = holder.getAdapterPosition();
                // 马上让被长按的选中
                notifyDataSetChanged();
                return false;
            }
        });
    }

    /**
     * notify并且重置长按的position
     */
    private synchronized void onlyNotifyDataChanged() {
        mLongPressPosition = -1;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mListCost.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View contentView;
        public TextView costTitle;
        public TextView costDate;
        public TextView costMoney;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.contentView = itemView;
            costTitle = (TextView) itemView.findViewById(R.id.lv_item_title);
            costDate = (TextView) itemView.findViewById(R.id.lv_item_date);
            costMoney = (TextView) itemView.findViewById(R.id.lv_item_cost);
        }
    }

    private LongPressMenu.OnMenuClickLister mMenuClickListener = new LongPressMenu.OnMenuClickLister() {
        @Override
        public void onMenuClick(int position) {
//            mLongPressMenu.dismiss();
            // 需要注意的是在操作完之后再dismiss，因为dismiss会重置长安position
            switch (position) {
                case 0:
                    if (mLongPressPosition > -1 && mLongPressPosition < mListCost.size()) {
                        removeItem(mListCost.get(mLongPressPosition));
                    }
                    mLongPressMenu.dismiss();
                    break;
                case 1:
                    if (mLongPressPosition > -1) {
                        popUpItem(mLongPressPosition);
                    }
                    mLongPressMenu.dismiss();
                    break;
                case 2:
                    mDataBaseHelper.clearCostDetail();
                    mListCost.clear();
                    notifyDataSetChanged();
            }
        }
    };

    private void popUpItem(int position) {
        CostBean bean = mListCost.get(position);
        if (mListCost.contains(bean)) {
            mListCost.remove(bean);
            mListCost.add(0, bean);
        }

    }

    private void removeItem(CostBean bean) {
        if (mListCost.contains(bean)) {
            mListCost.remove(bean);
        }
        if (mDataBaseHelper.isExist(bean.costTitle)) {
            mDataBaseHelper.deleteThread(bean.costTitle);
        }
        notifyDataSetChanged();
    }
}
