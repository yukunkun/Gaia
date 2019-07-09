package com.gaiamount.module_creator.sub_module_group.fragment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_creator.adapters.GroupCommentListAdapter;
import com.gaiamount.module_creator.beans.GroupCommentBean;
import com.gaiamount.module_creator.beans.GroupDetailInfo;
import com.gaiamount.module_creator.sub_module_group.constant.MemberKind;
import com.gaiamount.module_player.fragments.PlayerCommentFrag;
import com.gaiamount.util.LogUtil;
import com.gaiamount.util.UIUtils;
import com.gaiamount.util.image.ImageUtils;
import com.gaiamount.util.network.GsonUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.widgets.improved.NOScrolledListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.MediaPlayerProxy;

/**
 * Created by haiyang-lu on 16-6-13.
 */
public class GroupMainPageFrag extends Fragment {

    public static final String GROUP_DETAIL_INFO = "group_detail_info";
    /**
     * 小组首页推荐视频
     */
    @Bind(R.id.group_rec_video)
    SurfaceView mGroupRecVideo;
    /**
     * 开始播放的按钮
     */
    @Bind(R.id.play)
    ImageView playBtn;
    @Bind(R.id.group_add_rec_video_1)
    View groupAddRecVideo1;
    @Bind(R.id.group_add_rec_video_2)
    View groupAddRecVideo2;
    @Bind(R.id.group_add_rec_video_3)
    View groupAddRecVideo3;
    /**
     * 评论表情按钮
     */
    @Bind(R.id.comment_emoji)
    ImageView mCommentEmoji;
    /**
     * 评论编辑
     */
    @Bind(R.id.comment_edit_text)
    EditText mCommentEditText;
    /**
     * 评论的ListView
     */
    @Bind(R.id.group_comment_list)
    NOScrolledListView mGroupCommentListView;
    /**
     * 对访客可见：推荐视频1还未添加的提示视图
     */
    @Bind(R.id.group_rec_video_1_not_yet)
    View group_rec_video_1_not_yet;
    /**
     * 对访客可见：推荐视频2还未添加的提示视图
     */
    @Bind(R.id.group_rec_video_2_not_yet)
    View group_rec_video_2_not_yet;
    /**
     * 对访客可见：推荐视频3还未添加的提示视图
     */
    @Bind(R.id.group_rec_video_3_not_yet)
    View group_rec_video_3_not_yet;

    /**
     * 三个推荐视频的遮罩
     */
    @Bind(R.id.cutter)
    ImageView recVideo1Bg;
    @Bind(R.id.rec_video1_bg)
    ImageView recVideo2Bg;
    @Bind(R.id.rec_video2_bg)
    ImageView recVideo3Bg;
    /**
     * 小组详细信息
     */
    private List<GroupCommentBean> mGroupCommentBeanList = new ArrayList<>();
    private GroupCommentListAdapter mAdapter;
    private MediaPlayerProxy mMediaPlayerProxy;
    private GroupDetailInfo mGroupDetailInfo;

    public static GroupMainPageFrag newInstance(GroupDetailInfo groupDetailInfo) {
        Bundle args = new Bundle();
        GroupMainPageFrag groupMainPageFrag = new GroupMainPageFrag();
        args.putSerializable(GROUP_DETAIL_INFO, groupDetailInfo);
        groupMainPageFrag.setArguments(args);
        return groupMainPageFrag;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGroupDetailInfo = (GroupDetailInfo) getArguments().getSerializable(GROUP_DETAIL_INFO);

        //初始化IJKPlayer
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
        mMediaPlayerProxy = new MediaPlayerProxy(ijkMediaPlayer);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_main_page, container, false);
        ButterKnife.bind(this, view);

        mCommentEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });

        mGroupCommentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                replyToggle(true, position);

            }
        });
        mAdapter = new GroupCommentListAdapter(getActivity(), GroupMainPageFrag.this, mGroupCommentBeanList);
        mGroupCommentListView.setAdapter(mAdapter);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayerProxy.setDisplay(mGroupRecVideo.getHolder());
                //隐藏cutter
                recVideo1Bg.setVisibility(View.GONE);
                mMediaPlayerProxy.start();
                playBtn.setImageResource(R.mipmap.pause);
                playBtn.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playBtn.setVisibility(View.GONE);
                    }
                }, 2000);
            }
        });

        mGroupRecVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //暂停
                mMediaPlayerProxy.pause();
                playBtn.setVisibility(View.VISIBLE);
                playBtn.setImageResource(R.mipmap.play);

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshFragment();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMediaPlayerProxy.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayerProxy.stop();
        mMediaPlayerProxy.release();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void refreshFragment() {

        getComment();
        /**
         * 根据用户类型动态设置显示隐藏
         */
        //如果内容为空
        List<GroupDetailInfo.OBean.RecommendBean> recommend = mGroupDetailInfo.getO().getRecommend();
        if (recommend == null || recommend.size() == 0) {
            int memberType = mGroupDetailInfo.getO().getUser().getMemberType();
            if (memberType == MemberKind.MONITOR) {
                groupAddRecVideo1.setVisibility(View.VISIBLE);
                groupAddRecVideo2.setVisibility(View.VISIBLE);
                groupAddRecVideo3.setVisibility(View.VISIBLE);

                group_rec_video_1_not_yet.setVisibility(View.INVISIBLE);
                group_rec_video_2_not_yet.setVisibility(View.INVISIBLE);
                group_rec_video_3_not_yet.setVisibility(View.INVISIBLE);

            } else {
                groupAddRecVideo1.setVisibility(View.INVISIBLE);
                groupAddRecVideo2.setVisibility(View.INVISIBLE);
                groupAddRecVideo3.setVisibility(View.INVISIBLE);

                group_rec_video_1_not_yet.setVisibility(View.VISIBLE);
                group_rec_video_2_not_yet.setVisibility(View.VISIBLE);
                group_rec_video_3_not_yet.setVisibility(View.VISIBLE);
            }
        } else {//内容不为空
            //第一个视频
            if (recommend.size() >= 1) {
                //封面
                ImageUtils.getInstance(getActivity()).showImage(recVideo1Bg, recommend.get(0).getCover(), recommend.get(0).getScreenshot());
                String playUrl = recommend.get(0).getPlayUrl();
//                String playUrl = "http://qv.gaiamount.com/playlist/u3252/v10223_720.m3u8?e=1467779610&token=_6yhTpZI-AHtfULMSiW7eOCvrNEnOS_YcLV9GNfH:PUSGvsOJi_y_3gpY33hvfCHB6pE=";
                try {
                    mMediaPlayerProxy.setDataSource(getActivity(), Uri.parse(playUrl));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //准备
                mMediaPlayerProxy.prepareAsync();

                mMediaPlayerProxy.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(IMediaPlayer mp) {
                        mMediaPlayerProxy.pause();
                    }
                });
            }
            //第二个视频
            if (recommend.size() >= 2) {
                ImageUtils.getInstance(getActivity()).showImage(recVideo2Bg, recommend.get(1).getCover(), recommend.get(1).getScreenshot());
            }
            if (recommend.size() >= 3) {
                ImageUtils.getInstance(getActivity()).showImage(recVideo3Bg, recommend.get(2).getCover(), recommend.get(2).getScreenshot());
            }
        }
    }


    /**
     * 获取固定评论数
     */
    public void getComment() {
        getComment(mGroupDetailInfo.getO().getGroup().getId(), 1, 30);
    }

    /**
     * 获取评论列表,设置到评论中
     *
     * @param gid 小组id
     * @param pi  分页参数 当前页
     * @param ps  每页数量
     */
    public void getComment(long gid, int pi, int ps) {
        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(GroupMainPageFrag.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                Gson gson = GsonUtil.getInstannce().getGson();
                mGroupCommentBeanList = gson.fromJson(response.optJSONArray("a").toString(), new TypeToken<List<GroupCommentBean>>() {
                }.getType());
                mAdapter.setData(mGroupCommentBeanList);
                mAdapter.notifyDataSetChanged();
            }
        };
        GroupApiHelper.getGroupComment(gid, pi, ps, getActivity(), jsonHttpResponseHandler);
    }


    private int isReply;
    private long gCid;

    /**
     * 是否是对评论的回复
     *
     * @param toggle   true是 false不是
     * @param position 如果toogle为true，position为回复的列表位置 false，position无效
     */
    public void replyToggle(boolean toggle, int position) {

        if (toggle) {
            //获取回复目标的id
            GroupCommentBean playerCommentInfo = mGroupCommentBeanList.get(position);
            isReply = 1;
            gCid = playerCommentInfo.getId();
            mCommentEditText.requestFocus();
            UIUtils.showSoftInputMethod(getActivity(), mCommentEditText);
            //更改评论内容
            mCommentEditText.setHint("回复" + playerCommentInfo.getNickName());
        } else {
            isReply = 0;
            gCid = 0;
            mCommentEditText.setHint(getActivity().getString(R.string.start_comment));
        }
    }

    /**
     * 发送评论
     */
    private void sendComment() {
        long uId = GaiaApp.getUserInfo().id;
        final long gid = mGroupDetailInfo.getO().getGroup().getId();

        //如果内容为空，直接关闭输入法
        String content = mCommentEditText.getText().toString();
        if (content.isEmpty()) {
            GaiaApp.showToast(getActivity().getString(R.string.empty_content_not_allowed));
            return;
        }


        MJsonHttpResponseHandler commentResponseHandler = null;
        if (commentResponseHandler == null) {
            final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), null, "发送评论...");
            commentResponseHandler = new MJsonHttpResponseHandler(PlayerCommentFrag.class, progressDialog) {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    LogUtil.i(PlayerCommentFrag.class, "联网请求失败" + throwable.toString());
                    progressDialog.dismiss();
                    GaiaApp.showToast(getActivity().getString(R.string.network_time_out));
                }

                @Override
                public void onGoodResponse(JSONObject response) {
                    super.onGoodResponse(response);
                    GaiaApp.showToast(getActivity().getString(R.string.comment_sucess));
                    //清空数据
                    mGroupCommentBeanList.clear();
                    //更新数据
                    getComment();
                    //回到顶端
                    mGroupCommentListView.setSelection(0);
                }
            };
        }

        GroupApiHelper.sendComment(0, uId, gid, content, isReply, getActivity(), gCid, commentResponseHandler);

        //点击按钮后关闭输入法
        UIUtils.hideSoftInputMethod(getActivity(), mCommentEditText);
        //移除评论内容
        mCommentEditText.setText("");
    }

}
