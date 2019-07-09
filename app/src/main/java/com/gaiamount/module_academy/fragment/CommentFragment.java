package com.gaiamount.module_academy.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gaiamount.R;
import com.gaiamount.apis.api_academy.AcademyApiHelper;
import com.gaiamount.module_academy.activity.AcademyDetailActivity;
import com.gaiamount.module_academy.adapter.CommentListAdapter;
import com.gaiamount.module_academy.bean.CommentInfo;
import com.gaiamount.module_academy.bean.EventDetailInfo;
import com.gaiamount.module_academy.bean.OnEventId;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by yukun on 16-8-17.
 */
public class CommentFragment extends Fragment implements View.OnClickListener {

    private ListView listView;
    private int belong=1;
    private int pi=1;
    private long cid;
    private TextView textViewGrade;
    private RatingBar ratingBar;
    private LinearLayout layoutGrade;
    private ArrayList<CommentInfo> commentInfos=new ArrayList<>();
    private double avg=0.0;
    private CommentListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }
    //页面的回调,传值id
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventDetailInfo event) {
        cid = event.cid;
        belong=event.t;
        getInfo(cid,belong);
    }

    private Handler handler=new Handler();
    //评论成功的回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnEventId event) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            commentInfos.clear();
                            pi=1;
                            getInfo(cid,belong);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
//        commentInfos.clear();
//        getInfo(cid,belong);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.academy_comment_fragment, null);
        init(inflate);
        setAdapter();
        setListener();
        return inflate;
    }

    private void init(View inflate) {
        listView = (ListView) inflate.findViewById(R.id.academy_comment_listview);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.academy_listview_head, null);
        textViewGrade= (TextView) view.findViewById(R.id.comment_grade);
        ratingBar= (RatingBar) view.findViewById(R.id.comment_star);
        layoutGrade= (LinearLayout) view.findViewById(R.id.lin_grade);
        layoutGrade.setOnClickListener(this);
        listView.addHeaderView(view);
    }


    private void getInfo(long cid,int belong) {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(CommentFragment.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                paraJson(response);
                //评分
                ratingBar.setMax(10);
                if (avg!=0){
                    float grades = (float)(((int) (avg * 10)) )/ 10;
                    textViewGrade.setText(grades+"");
                }else {
                    textViewGrade.setText(0.0+"");
                }
                ratingBar.setProgress((int)(avg));
            }
        };
        AcademyApiHelper.commentList(cid,belong,pi,getContext(),handler);
    }

    private void paraJson(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        JSONObject o = response.optJSONObject("o");
        avg = o.optDouble("avg");
        for (int i = 0; i < a.length(); i++) {
            JSONObject jsonObject = a.optJSONObject(i);
            CommentInfo info=new CommentInfo();
            info.setNickName(jsonObject.optString("nickName"));
            info.setGrade(jsonObject.optLong("grade"));
            info.setAvatar(jsonObject.optString("avatar"));
            info.setContent(jsonObject.optString("content"));
            JSONObject jsonObject1 = jsonObject.optJSONObject("createTime");
            info.setTime(jsonObject1.optLong("time"));
            info.setId(jsonObject.optInt("id"));
            commentInfos.add(info);
        }
        adapter.notifyDataSetChanged();
    }

    private void setAdapter() {
        adapter = new CommentListAdapter(getContext(),commentInfos);
        listView.setAdapter(adapter);
    }

    private void setListener() {

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if(commentInfos.size()==8*pi&&firstVisibleItem+visibleItemCount==totalItemCount){
                    pi++;
                    getInfo(cid,belong);
                }

            }
        });
    }

    //评分
    @Override
    public void onClick(View v) {
        showDialog();
    }


    private void showDialog() {
        CommentDialog commentDialog=CommentDialog.newInstance(cid);
        commentDialog.show(getActivity().getSupportFragmentManager(), "commentDialog");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
