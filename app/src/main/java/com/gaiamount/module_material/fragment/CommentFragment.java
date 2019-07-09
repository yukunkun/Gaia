package com.gaiamount.module_material.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import com.gaiamount.R;
import com.gaiamount.apis.api_comment.CommentApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.fragment.ContentFragment;
import com.gaiamount.module_material.adapters.CommentListAdapter;
import com.gaiamount.module_material.bean.MaterialDetailInfo;
import com.gaiamount.module_player.VideoActivity;
import com.gaiamount.module_player.adapters.PlayerCommentListAdapter;
import com.gaiamount.module_player.bean.PlayerCommentInfo;
import com.gaiamount.util.UIUtils;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.widgets.improved.BounceListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by yukun on 16-9-30.
 */
public class CommentFragment extends Fragment {


    private long mid;
    private BounceListView listView;
    private EditText mInput;
    private ImageView imageView;
    private MJsonHttpResponseHandler commentResponseHandler;
    private int isReply=0; //是否是回复 0否1是
    private int mContentId; //评论目标的id
    private long cid=0;
    private CommentListAdapter mAdapter;
    private List<PlayerCommentInfo> mPlayerCommentInfos;
    private MJsonHttpResponseHandler mResponseHandler;

    private int page = 1;
    private static int MAX_PAGE_NUM = 20;
    private int mLastVisiblePosition;
    private int mLastCommentInfosSize;
    private boolean isFirstRequestCommentData = true;
    private AppCompatActivity mActivity;

    public static CommentFragment newInstance(MaterialDetailInfo mDetailInfo, long mid){
        CommentFragment recomendFragment=new CommentFragment();
        Bundle bundle=new Bundle();
        bundle.putLong("mid",mid);
        bundle.putSerializable("mDetailInfo",mDetailInfo);
        recomendFragment.setArguments(bundle);
        return recomendFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (AppCompatActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mid=getArguments().getLong("mid");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_player_comment, null);
        init(inflate);
        getCommentData(page,MAX_PAGE_NUM);
        return inflate;
    }

    private void init(View inflate) {
        listView = (BounceListView) inflate.findViewById(R.id.player_comment_list);
        mInput = (EditText) inflate.findViewById(R.id.player_comment_input);
        imageView = (ImageView) inflate.findViewById(R.id.player_comment_send);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });
        //设置滚动监听
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //关闭输入法
                UIUtils.hideSoftInputMethod(mActivity,mInput);
                replyToggle(false,-1);

                if(mLastVisiblePosition ==mPlayerCommentInfos.size()-1) {
                    mLastCommentInfosSize = mPlayerCommentInfos.size();
                    page++;
                    //此时请求更多数据
                    getCommentData(page,MAX_PAGE_NUM);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //分页加载
                mLastVisiblePosition = listView.getLastVisiblePosition();

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                replyToggle(true,position);

            }
        });
    }

    private void getCommentData(int pi, int ps) {
        if(mResponseHandler==null) {
            ProgressDialog progressDialog = new ProgressDialog(getActivity());

            mResponseHandler = new MJsonHttpResponseHandler(CommentFragment.class,progressDialog) {
                @Override
                public void parseJson(JSONObject response) {
                    super.parseJson(response);
                    //评论数
                    if(response.length()!=0&&response!=null){

                        //评论
                        JSONArray a = response.optJSONArray("a");
                        List<PlayerCommentInfo> playerCommentInfos = new Gson().fromJson(a.toString(), new TypeToken<List<PlayerCommentInfo>>() {
                        }.getType());

                        //如果是第一次进入，则设置适配器，如果不是，则刷新适配器
                        if(isFirstRequestCommentData) {
                            isFirstRequestCommentData = false;
                            mPlayerCommentInfos = playerCommentInfos;
                            mAdapter = new CommentListAdapter(mActivity, CommentFragment.this, mPlayerCommentInfos);
                            listView.setAdapter(mAdapter);
                        } else {
                            mPlayerCommentInfos.addAll(playerCommentInfos);
                            mAdapter.setPlayerCommentInfos(mPlayerCommentInfos);
//                            //刷新界面
                            mAdapter.notifyDataSetChanged();
                            //                        if(mLastCommentInfosSize==mPlayerCommentInfos.size()) {//两次数量相等，表示没有更多数据
                            //                            GaiaApp.showToast(getString(R.string.no_more));
                            //                        }
                        }
                    }

                }

            };
        }
        //请求网络
        CommentApiHelper.getCommentData(mid,1,pi,ps,mActivity, mResponseHandler);

    }
    /**
     * 是否是对评论的回复
     * @param toggle true是 false不是
     * @param position 如果toogle为true，position为回复的列表位置 false，position无效
     */
    public void replyToggle(boolean toggle,int position) {

        if(toggle) {
            //获取回复目标的id
            PlayerCommentInfo playerCommentInfo = mPlayerCommentInfos.get(position);
            isReply = 1;
            cid = playerCommentInfo.getId();
            mInput.requestFocus();
            UIUtils.showSoftInputMethod(mActivity,mInput);
            //更改评论内容
            mInput.setHint("回复"+playerCommentInfo.getNickName());
        }else {
            isReply = 0;
            cid = 0;
            mInput.setHint(mActivity.getString(R.string.start_comment));
        }
    }


    private void sendComment() {
            //如果内容为空，直接关闭输入法
            String content = mInput.getText().toString();
            if(content.isEmpty()) {
                GaiaApp.showToast(getActivity().getString(R.string.empty_content_not_allowed));
                return;
            }

            long uId = GaiaApp.getUserInfo().id;
            if(commentResponseHandler==null) {
                final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),null,getString(R.string.send_comment));

                commentResponseHandler = new MJsonHttpResponseHandler(CommentFragment.class,progressDialog){
                    @Override
                    public void onGoodResponse(JSONObject response) {
                        super.onGoodResponse(response);
                        GaiaApp.showToast(getActivity().getString(R.string.comment_sucess));
                        //清空数据
                        mPlayerCommentInfos.clear();
                        mLastCommentInfosSize = 0;
                        page = 1;
//                        //更新数据
                        getCommentData(page,MAX_PAGE_NUM);
//                        //回到顶端
                        listView.setSelection(0);
                    }
                };
            }

            CommentApiHelper.sendComment(uId,content,mid,1,isReply,cid, getContext(),commentResponseHandler);

            //点击按钮后关闭输入法
            UIUtils.hideSoftInputMethod(getActivity(),mInput);
            //移除评论内容
            mInput.setText("");

    }
}
