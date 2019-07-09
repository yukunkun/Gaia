package com.gaiamount.module_player.fragments;

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
import android.widget.ListView;

import com.gaiamount.R;
import com.gaiamount.apis.api_comment.CommentApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_player.VideoActivity;
import com.gaiamount.module_player.adapters.PlayerCommentListAdapter;
import com.gaiamount.module_player.bean.PlayerCommentInfo;
import com.gaiamount.util.UIUtils;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by haiyang-lu on 16-4-25.
 * 播放页面的评论区片
 */
public class PlayerCommentFrag extends Fragment implements View.OnClickListener {

    /**
     * 评论输入框
     */
    private EditText mInput;
    /**
     * 评论列表
     */
    private ListView mListView;
    /**
     * 接口参数：内容类型
     */
    private int mContentType;
    /**
     * 接口参数：评论视频的id
     */
    private long mContentId;
    /**
     * 是否是回复 0否1是
     */
    private int isReply = 0;
    /**
     * 回复的目标id
     */
    private long cid = 0;
    /**
     * 评论列表的适配器
     */
    private PlayerCommentListAdapter mAdapter;
    private List<PlayerCommentInfo> mPlayerCommentInfos;
    /**
     * 是否是第一次请求评论数据，默认为true
     */
    private boolean isFirstRequestCommentData = true;
    private JsonHttpResponseHandler mResponseHandler;
    /**
     * 评论的当前页
     */
    private int page = 1;
    private static int MAX_PAGE_NUM = 20;
    private int mLastVisiblePosition;
    /**
     * 分页之前的列表数量
     */
    private int mLastCommentInfosSize;
    private AppCompatActivity mActivity;

    /**
     * 获取评论列表的回调
     */
    JsonHttpResponseHandler commentResponseHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        mContentType = arguments.getInt("contentType");
        mContentId = arguments.getLong("contentId");

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (AppCompatActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_comment, container, false);
        initView(view);

        getCommentData(page,MAX_PAGE_NUM);


        return view;
    }

    private void initView(View view) {
        mListView = (ListView) view.findViewById(R.id.player_comment_list);
        mInput = (EditText) view.findViewById(R.id.player_comment_input);
        ImageView send = (ImageView) view.findViewById(R.id.player_comment_send);
        send.setOnClickListener(this);

        //设置滚动监听
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                mLastVisiblePosition = mListView.getLastVisiblePosition();

            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                replyToggle(true,position);

            }
        });


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

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id ==R.id.player_comment_send) {
            sendComment();
        }
    }

    /**
     * 联网获取最新评论数据
     */
    private void getCommentData(int pi, int ps) {
        if(mResponseHandler==null) {
            ProgressDialog progressDialog = new ProgressDialog(mActivity);
            mResponseHandler = new MJsonHttpResponseHandler(PlayerCommentFrag.class,progressDialog) {
                @Override
                public void parseJson(JSONObject response) {
                    super.parseJson(response);
                    //评论数
                    if(response.length()!=0&&response!=null){

                        int commentCount = response.optJSONObject("o").optInt("total");
                        //通知activity更新评论数据
                        VideoActivity videoActivity = (VideoActivity) getActivity();
                        videoActivity.updateCommentCount(commentCount);
                        //评论
                        JSONArray a = response.optJSONArray("a");
                        List<PlayerCommentInfo> playerCommentInfos = new Gson().fromJson(a.toString(), new TypeToken<List<PlayerCommentInfo>>() {
                        }.getType());

                        //如果是第一次进入，则设置适配器，如果不是，则刷新适配器
                        if(isFirstRequestCommentData) {
                            isFirstRequestCommentData = false;
                            mPlayerCommentInfos = playerCommentInfos;
                            mAdapter = new PlayerCommentListAdapter(mActivity, PlayerCommentFrag.this, mPlayerCommentInfos);
                            mListView.setAdapter(mAdapter);
                        } else {
                            mPlayerCommentInfos.addAll(playerCommentInfos);
                            mAdapter.setPlayerCommentInfos(mPlayerCommentInfos);
                            //刷新界面
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
        CommentApiHelper.getCommentData(mContentId,mContentType,pi,ps,mActivity, mResponseHandler);

    }

    /**
     * 发送评论
     */
    private void sendComment() {
        //如果内容为空，直接关闭输入法
        String content = mInput.getText().toString();
        if(content.isEmpty()) {
            GaiaApp.showToast(mActivity.getString(R.string.empty_content_not_allowed));
            return;
        }

        long uId = GaiaApp.getUserInfo().id;
        if(commentResponseHandler==null) {
            final ProgressDialog progressDialog = ProgressDialog.show(mActivity,null,getString(R.string.send_comment));
            commentResponseHandler =new MJsonHttpResponseHandler(PlayerCommentFrag.class,progressDialog){
                @Override
                public void onGoodResponse(JSONObject response) {
                    super.onGoodResponse(response);
                    GaiaApp.showToast(mActivity.getString(R.string.comment_sucess));
                    //清空数据
                    mPlayerCommentInfos.clear();
                    mLastCommentInfosSize = 0;
                    page = 1;
                    //更新数据
                    getCommentData(page,MAX_PAGE_NUM);
                    //回到顶端
                    mListView.setSelection(0);
                }
            };
        }

        CommentApiHelper.sendComment(uId,content,mContentId,mContentType,isReply,cid, mActivity,commentResponseHandler);

        //点击按钮后关闭输入法
        UIUtils.hideSoftInputMethod(mActivity,mInput);
        //移除评论内容
        mInput.setText("");
    }
}
