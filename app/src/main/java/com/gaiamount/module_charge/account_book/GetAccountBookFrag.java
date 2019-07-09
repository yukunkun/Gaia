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
public class GetAccountBookFrag extends Fragment {

    private TextView mTotal_sell;
    private RecyclerView mRecyclerView;
    private TextView mTotal_money;
    private TextView accountBookEmpty;

    public static GetAccountBookFrag newInstance() {
        GetAccountBookFrag getAccountBookFrag = new GetAccountBookFrag();
        return getAccountBookFrag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_account_book, container, false);
        initView(view);
        //请求网络
        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(GetAccountBookFrag.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                GetProfit getProfit = GsonUtil.getInstannce().getGson().fromJson(response.toString(), GetProfit.class);
                List<GetProfit.ABean> list = getProfit.getA();
                if (list==null||list!=null&&list.size()==0) {
                    mRecyclerView.setVisibility(View.GONE);
                    accountBookEmpty.setVisibility(View.VISIBLE);
                }else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    accountBookEmpty.setVisibility(View.GONE);

                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mRecyclerView.setAdapter(new GetAccountBookRecyclerAdapter(getActivity(),list));
                    //销量
                    mTotal_sell.setText("总售出"+getProfit.getA().size()+"笔");
                    double totalMoney = 0;
                    for (int i =0;i<getProfit.getA().size();i++) {
                        totalMoney+=getProfit.getA().get(i).getAmount();
                    }
                    mTotal_money.setText("合计:"+ new DecimalFormat(".##").format(totalMoney));
                }

            }
        };
        AccountApiHelper.getProfit(1,20,0,0,getActivity(),jsonHttpResponseHandler);
        return view;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.get_account_book_rv);
        mTotal_sell = (TextView) view.findViewById(R.id.get_account_book_total_sell);
        mTotal_money = (TextView) view.findViewById(R.id.get_account_book_total_money);
        accountBookEmpty = (TextView) view.findViewById(R.id.accout_book_empty);

    }


}
