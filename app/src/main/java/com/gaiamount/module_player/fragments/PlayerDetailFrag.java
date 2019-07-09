package com.gaiamount.module_player.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.PersonApiHelper;
import com.gaiamount.apis.api_works.WorksApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.gaia_main.search.GlobalSearchActivity;
import com.gaiamount.module_player.bean.VideoDetailInfo;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.StringUtil;
import com.gaiamount.util.image.ImageUtils;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by haiyang-lu on 16-4-5.
 */
public class PlayerDetailFrag extends Fragment implements View.OnClickListener {

    public static final String DETAIL_INFO = "detail_info";
    public static final String WID = "wid";
    private VideoDetailInfo detailInfo;
    private StringUtil mStringUtil;
    private RatingBar mRatingBar;
    private TextView mScore;
    private long mWid;
    private Button mAdd_attention;
    private ArrayList<String> arrayList;
    /**
     * 点击关注/取消关注
     */
    private View.OnClickListener addAttentionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isAttentioned) {
                PersonApiHelper.addAttention(detailInfo.getUser().getUid(), 0, getActivity());
            } else {
                PersonApiHelper.addAttention(detailInfo.getUser().getUid(), 1, getActivity());
            }
            PersonApiHelper.isAttentioned(detailInfo.getUser().getUid(), getActivity());

        }
    };
    private ImageView imageViewVip;
    private TextView textViewTag_1;
    private TextView textViewTag_2;
    private TextView textViewTag_3;
    private String[] stringArray;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        detailInfo = (VideoDetailInfo) getArguments().getSerializable("video_detail_info");
        mStringUtil = StringUtil.getInstance();
    }

    public static PlayerDetailFrag newInstance(VideoDetailInfo detailInfo, long wid) {

        Bundle args = new Bundle();
        args.putSerializable(DETAIL_INFO, detailInfo);
        args.putLong(WID, wid);
        PlayerDetailFrag fragment = new PlayerDetailFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Bundle arguments = getArguments();
        mWid = arguments.getLong(WID);
        detailInfo = (VideoDetailInfo) arguments.getSerializable(DETAIL_INFO);
        stringArray = getResources().getStringArray(R.array.video_type);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_detail, container, false);
        initView(view);

        initAttentionBtn();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 初始化关注按钮
     */
    private void initAttentionBtn() {
        //如果是自己，灰化按钮
        long uid = detailInfo.getUser().getUid();
        if (uid==GaiaApp.getUserInfo().id) {
            mAdd_attention.setTextColor(getResources().getColor(R.color.white));
            mAdd_attention.setBackgroundResource(R.drawable.shape_radius4_redstroke_whitebg);
            mAdd_attention.setClickable(false);
        }else {
            //判断当前视频的作者是否被关注了
            PersonApiHelper.isAttentioned(detailInfo.getUser().getUid(), getActivity());
        }
    }

    /**
     * 当前被查看的视频的作者是否被当前用户关注的回调
     *
     * @param eventIsAttention
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventIsAttentioned(PersonApiHelper.EventIsAttention eventIsAttention) {
        int a = eventIsAttention.getA();
        if (a == 1) {//已关注
            isAttentioned = true;
            mAdd_attention.setTextColor(getResources().getColor(R.color.white));
            mAdd_attention.setText(R.string.have_attention);
            mAdd_attention.setBackgroundResource(R.drawable.shape_radius4_redstroke_whitebg);
        } else {
            mAdd_attention.setTextColor(getResources().getColor(R.color.colorAccent));
            isAttentioned = false;
            mAdd_attention.setText(R.string.add_attention);
            mAdd_attention.setBackgroundResource(R.drawable.shape_btn_style2);
        }
    }

    /**
     * 关注/取消关注成功的回调
     *
     * @param eventAddAttention
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAddAttention(PersonApiHelper.EventAddAttention eventAddAttention) {
        isAttentioned = !isAttentioned;
        if (isAttentioned) {
            GaiaApp.showToast(getString(R.string.add_attention_success));
        } else {
            GaiaApp.showToast(getString(R.string.cancel_attention_success));
        }
        PersonApiHelper.isAttentioned(detailInfo.getUser().getUid(), getActivity());
    }


    private void initView(View view) {
        TextView tvName = (TextView) view.findViewById(R.id.name);
        TextView playCount_playTime = (TextView) view.findViewById(R.id.playcount_playtime);
        mScore = (TextView) view.findViewById(R.id.score);
        mRatingBar = (RatingBar) view.findViewById(R.id.play_grade_rating);
        TextView master = (TextView) view.findViewById(R.id.master);
        TextView tag = (TextView) view.findViewById(R.id.tag);
        TextView back_story = (TextView) view.findViewById(R.id.back_story);
        imageViewVip = (ImageView) view.findViewById(R.id.image_is_vip);
        ImageView avatar = (ImageView) view.findViewById(R.id.avatar);
        mAdd_attention = (Button) view.findViewById(R.id.add_attention);
        TextView staff = (TextView) view.findViewById(R.id.staff);
        /*关注,三个tag*/
        TextView textViewCreate= (TextView) view.findViewById(R.id.create_num);
        textViewTag_1 = (TextView) view.findViewById(R.id.tag_1);
        textViewTag_2 = (TextView) view.findViewById(R.id.tag_2);
        textViewTag_3 = (TextView) view.findViewById(R.id.tag_3);

        //标签的点击事件
        textViewTag_1.setOnClickListener(this);
        textViewTag_2.setOnClickListener(this);
        textViewTag_3.setOnClickListener(this);

        if (detailInfo != null) {
            VideoDetailInfo.WorksBean works = detailInfo.getWorks();
            VideoDetailInfo.VideoInfoBean videoInfo = detailInfo.getVideoInfo();
            //作品名称
            String worksName = works.getName();
            if (worksName.contains(".mp4")) {
                tvName.setText(worksName.substring(0, worksName.length() - 4));
            }else {
                tvName.setText(worksName);
            }
            int creationCount = detailInfo.getCreationCount();
            int likeCount = detailInfo.getLikeCount();

            textViewCreate.setText(likeCount+"关注"+" "+creationCount+"创作");
            //播放次数，和上传的时间
            if(detailInfo.getWorks().getCreateTime()!=null&&!detailInfo.getWorks().getCreateTime().equals("null")){
                playCount_playTime.setText(mStringUtil.stringForCount(detailInfo.getWorksProperties().getPlayCount())+"  发布于"+detailInfo.getWorks().getCreateTime().substring(0,10));
            }

            /*分类的显示*/
            String type = works.getType();
            arrayList=getTags(type);
            if(arrayList!=null&&arrayList.size()!=0){
                if(arrayList.size()==1){
                    textViewTag_1.setVisibility(View.VISIBLE);
                    textViewTag_1.setText(arrayList.get(0));
                    textViewTag_1.setBackgroundResource(R.drawable.shape_detail_play);
                }else if(arrayList.size()==2){
                    textViewTag_1.setVisibility(View.VISIBLE);
                    textViewTag_1.setText(arrayList.get(0));
                    textViewTag_2.setVisibility(View.VISIBLE);
                    textViewTag_2.setText(arrayList.get(1));
                    textViewTag_1.setBackgroundResource(R.drawable.shape_detail_play);
                    textViewTag_2.setBackgroundResource(R.drawable.shape_detail_play);
                }else if(arrayList.size()==3){
                    textViewTag_1.setVisibility(View.VISIBLE);
                    textViewTag_1.setText(arrayList.get(0));
                    textViewTag_2.setVisibility(View.VISIBLE);
                    textViewTag_2.setText(arrayList.get(1));
                    textViewTag_3.setVisibility(View.VISIBLE);
                    textViewTag_3.setText(arrayList.get(2));
                    textViewTag_1.setBackgroundResource(R.drawable.shape_detail_play);
                    textViewTag_2.setBackgroundResource(R.drawable.shape_detail_play);
                    textViewTag_3.setBackgroundResource(R.drawable.shape_detail_play);
                }
            }
            //评分
            setGrade();
            //staff
            StringBuilder staffStr = new StringBuilder();
            staffStr.append(getString(R.string.director) + ":" + works.getDirector() + "|");
            staffStr.append(getString(R.string.photographyer) + ":" + works.getPhotographer() + "|");
            staffStr.append(getString(R.string.cutter) + ":" + works.getCutter() + "|");
            staffStr.append(getString(R.string.colorist) + ":" + works.getColorist() + "|");
            staffStr.append(getString(R.string.models) + ":" + works.getMachine() + "|");
            staffStr.append(getString(R.string.lens) + ":" + works.getLens() + "|");
            staffStr.append(getString(R.string.locations) + ":" + works.getAddress() + "|");
            staffStr.append(getString(R.string.scene) + ":" + works.getScene() + "|");
            staffStr.append(getString(R.string.resolution) + ":" + videoInfo.getWidth() + "x" + videoInfo.getHeight() + "|");
            staffStr.append(getString(R.string.format) + ":" + videoInfo.getFormat() + "|");
            staffStr.append(getString(R.string.coding) + ":" + videoInfo.getCodec() + "|");
            staffStr.append(getString(R.string.bitrate) + ":" + mStringUtil.stringForBitrate(videoInfo.getBitRate()) + "|");
            String[] strings = videoInfo.getFps().split("/");
            if (!strings[0].isEmpty() && !strings[1].isEmpty()) {
                double v = Double.parseDouble(strings[0]) / Double.parseDouble(strings[1]);
                Double aDouble = Double.valueOf(v);
                int i = aDouble.intValue();
                if (v == i) {
                    staffStr.append(getString(R.string.framerate) + ":" + i + "|");
                } else {
                    staff.append(getString(R.string.framerate) + ":" + String.format("%.2f", v));
                }
            }

            staffStr.append(getString(R.string.videosize) + ":" + mStringUtil.stringForSize(videoInfo.getSize()) + "|");
            staff.setText(staffStr);
            //作品标签
            String s = works.getKeywords().isEmpty() ? "暂无" : works.getKeywords();
            if(!s.equals("暂无")){
                SpannableStringBuilder tagStr = new SpannableStringBuilder(getString(R.string.tag) + s);
                tag.setText(tagStr);
            }else {
                tag.setVisibility(View.GONE);
            }
            //作品背景
            String description = works.getDescription();
            if(description!=null&&description.length()!=0){
                Spanned spanned = Html.fromHtml(description);
                SpannableStringBuilder workBg = new SpannableStringBuilder(getString(R.string.intro) + spanned);
                back_story.setText(workBg);
            }else {
                back_story.setVisibility(View.GONE);
            }

            //头像
            VideoDetailInfo.UserBean user = detailInfo.getUser();
            if (user.getAvatar() != null) {
                ImageUtils.getInstance(getActivity()).getAvatar(avatar, user.getAvatar());
            }
            avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityUtil.startPersonalActivity(getActivity(), detailInfo.getUser().getUid());
                }
            });
            //作者名称
            master.setText(user.getNickName());

            if(user.getIsVip()==1){
                if(user.getVipLevel()==2){
                    imageViewVip.setImageResource(R.mipmap.pro);
                }else if(user.getVipLevel()==3){
                    imageViewVip.setImageResource(R.mipmap.advs);
                }else if(user.getVipLevel()==4){
                    imageViewVip.setImageResource(R.mipmap.bns);
                }
            }
        }

        mRatingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                openGradeDialog();
                return false;
            }
        });


        if (mAdd_attention != null) {
            mAdd_attention.setOnClickListener(addAttentionClickListener);
        }

    }

    private ArrayList getTags(String type) {
        ArrayList<String> stringarray=new ArrayList<>();
        String[] split = type.split(",");
        for (int i = 0; i < split.length; i++) {
            String strings=split[i];
            String tagName=changeString(strings);
            if(!tagName.equals(""))
                stringarray.add(tagName);
            }
        return stringarray;
    }

    private String changeString(String strings) {
        if(!strings.equals("")){
            if(strings.equals("0")){
                return stringArray[0];
            }else if(strings.equals("1")){
                return stringArray[1];
            }else if(strings.equals("2")){
                return stringArray[2];
            }else if(strings.equals("3")){
                return stringArray[3];
            }else if(strings.equals("4")){
                return stringArray[4];
            }else if(strings.equals("5")){
                return stringArray[5];
            }else if(strings.equals("6")){
                return stringArray[6];
            }else if(strings.equals("7")){
                return stringArray[7];
            }else if(strings.equals("8")){
                return stringArray[8];
            }else if(strings.equals("9")){
                return stringArray[9];
            }else if(strings.equals("10")){
                return stringArray[10];
            }else if(strings.equals("11")){
                return stringArray[11];
            }else if(strings.equals("12")){
                return stringArray[12];
            }
        }
        return "";

    }

    private boolean isAttentioned = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.score:
                openGradeDialog();
                break;
            case R.id.tag_1:
                //每一个标签的点击事件
                Intent intent = new Intent(getContext(), GlobalSearchActivity.class);
                intent.putExtra(GlobalSearchActivity.WORDS, "");
                intent.putExtra(GlobalSearchActivity.KEY_WORDS,textViewTag_1.getText());
                intent.putExtra(GlobalSearchActivity.KIND, 1);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.tag_2:
                Intent intent1 = new Intent(getContext(), GlobalSearchActivity.class);
                intent1.putExtra(GlobalSearchActivity.WORDS, "");
                intent1.putExtra(GlobalSearchActivity.KEY_WORDS,textViewTag_2.getText());
                intent1.putExtra(GlobalSearchActivity.KIND, 1);
                getActivity().startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.tag_3:
                Intent intent2 = new Intent(getContext(), GlobalSearchActivity.class);
                intent2.putExtra(GlobalSearchActivity.WORDS, "");
                intent2.putExtra(GlobalSearchActivity.KEY_WORDS,textViewTag_3.getText());
                intent2.putExtra(GlobalSearchActivity.KIND, 1);
                getActivity().startActivity(intent2);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            default:
                break;
        }
    }

    /**
     * 打开打分对话框
     */
    private void openGradeDialog() {
        //打分对话框
        double grade = detailInfo.getWorks().getGrade();
        GradeDialogFrag gradeDialogFrag = GradeDialogFrag.newInstance(mWid, grade);
        gradeDialogFrag.show(getActivity().getSupportFragmentManager(), "grade");
        gradeDialogFrag.setOnGradeChangeListener(new GradeDialogFrag.OnGradeChangeListener() {
            @Override
            public void onGradeChange(double grade) {
                getNewData();
            }
        });
    }

    private void getNewData() {
        JsonHttpResponseHandler handler = new MJsonHttpResponseHandler(PlayerDetailFrag.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                //解析json并将新值赋给detailInfo
                detailInfo = new Gson().fromJson(response.optJSONObject("o").toString(), VideoDetailInfo.class);
                //更新界面
                setGrade();
            }
        };
        WorksApiHelper.getDetailWorkInfo(mWid, getActivity(), handler);
    }

    private void setGrade() {
        //评分
        double grade = detailInfo.getWorks().getGrade();
        mScore.setText(String.valueOf(grade));
        mScore.setOnClickListener(this);
        //星级评分条
        mRatingBar.setRating((float) (grade / 2));
    }
}
