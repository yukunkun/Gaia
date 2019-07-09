package com.gaiamount.module_charge.account_book;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.util.StringUtil;

import java.util.List;

/**
 * Created by haiyang-lu on 16-5-30.
 */
public class GetAccountBookRecyclerAdapter extends RecyclerView.Adapter<GetAccountBookRecyclerAdapter.ViewHolder> {

    LayoutInflater mInflater;

    private List<GetProfit.ABean> mList;

    public GetAccountBookRecyclerAdapter(Context context, List<GetProfit.ABean> list) {

        mList = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_account_book_history, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GetAccountBookRecyclerAdapter.ViewHolder holder, int position) {
        GetProfit.ABean a = mList.get(position);
        holder.mName.setText(a.getWname());
        holder.mTime.setText(StringUtil.getInstance().stringForDate(a.getCreateTime().getTime()));
        holder.mTo.setText(a.getUserName());
        switch (a.getType()) {
            case 0:
                holder.mType.setText("类型："+"作品");
                break;
            case 1:
                holder.mType.setText("类型："+"素材");
                break;
            default:
                holder.mType.setText("类型："+"其他");
                break;
        }
        switch (a.getContentExtra()) {
            case 0:
                holder.mSpec.setText("规格："+"源视频");
                break;
            case 1:
                holder.mSpec.setText("规格："+"4K");
                break;
            case 2:
                holder.mSpec.setText("规格："+"2K");
                break;

            case 3:
                holder.mSpec.setText("规格："+"1080P");

                break;
            case 4:
                holder.mSpec.setText("规格："+"720P");
                break;
            default:
                holder.mSpec.setText("规格："+"其他");
                break;
        }
        holder.mBank.setText("收款方"+a.getBankName());
        holder.mOrder.setText("单号:"+a.getNumber());
        holder.mMoney.setText("金额:"+a.getAmount()+"元");
        switch (a.getStatus()) {
            case 0:
                holder.mSpec.setText("状态："+"正在处理");
                break;
            case 1:
                holder.mStatus.setText("状态："+"已结款");
                break;
            default:
                holder.mStatus.setText("状态："+"其他");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mName;
        private final TextView mTime;
        private final TextView mTo;
        private final TextView mType;
        private final TextView mSpec;
        private final TextView mBank;
        private final TextView mOrder;
        private final TextView mMoney;
        private final TextView mStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.account_history_name);
            mTime = (TextView) itemView.findViewById(R.id.account_history_time);
            mTo = (TextView) itemView.findViewById(R.id.account_history_to);
            mType = (TextView) itemView.findViewById(R.id.account_history_type);
            mSpec = (TextView) itemView.findViewById(R.id.account_history_spec);
            mBank = (TextView) itemView.findViewById(R.id.account_history_bank);
            mOrder = (TextView) itemView.findViewById(R.id.account_history_order);
            mMoney = (TextView) itemView.findViewById(R.id.account_history_money);
            mStatus = (TextView) itemView.findViewById(R.id.account_history_status);
        }
    }

}
