package com.gaiamount.module_charge.account_book;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_user.AccountApiHelper;
import com.gaiamount.util.network.GsonUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by haiyang-lu on 16-5-20.
 */
public class OutAccountBookFrag extends Fragment {

    private TextView mTotal_sell;
    private RecyclerView mRecyclerView;
    private TextView mTotal_money;
    private TextView accountBookEmpty;

    public static OutAccountBookFrag newInstance() {
        OutAccountBookFrag outAccountBookFrag = new OutAccountBookFrag();
        return outAccountBookFrag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_out_account_book, container, false);
        initView(view);

        //请求网络
        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(OutAccountBookFrag.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                OutProfit outProfit = GsonUtil.getInstannce().getGson().fromJson(response.toString(), OutProfit.class);
                List<OutProfit.ABean> list = outProfit.getA();
                if (list.size()==0) {
                    mRecyclerView.setVisibility(View.GONE);
                    accountBookEmpty.setVisibility(View.VISIBLE);
                }else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    accountBookEmpty.setVisibility(View.GONE);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mRecyclerView.setAdapter(new OutAccountBookRecycerAdapter(getActivity(),list));
                    //销量
                    mTotal_sell.setText("总售出"+outProfit.getA().size()+"笔");
                    double totalMoney = 0;
                    for (int i =0;i<outProfit.getA().size();i++) {
                        totalMoney+=outProfit.getA().get(i).getAmount();
                    }
                    mTotal_money.setText("合计:"+ new DecimalFormat(".##").format(totalMoney));
                }

            }
        };
        AccountApiHelper.getSpending(1,20,0,0,getActivity(),jsonHttpResponseHandler);

        return view;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.out_account_book_rv);
        mTotal_sell = (TextView) view.findViewById(R.id.out_account_book_total_sell);
        mTotal_money = (TextView) view.findViewById(R.id.out_account_book_total_money);
        accountBookEmpty = (TextView) view.findViewById(R.id.accout_book_empty);
    }
}
