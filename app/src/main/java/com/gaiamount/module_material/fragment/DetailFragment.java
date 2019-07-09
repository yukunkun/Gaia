package com.gaiamount.module_material.fragment;

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

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.apis.api_creator.PersonApiHelper;
import com.gaiamount.apis.api_works.WorksApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.gaia_main.search.GlobalSearchActivity;
import com.gaiamount.module_material.bean.MaterialDetailInfo;
import com.gaiamount.module_material.bean.MaterialInfo;
import com.gaiamount.module_material.bean.StreamInfo;
import com.gaiamount.module_player.bean.VideoDetailInfo;
import com.gaiamount.module_player.fragments.GradeDialogFrag;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.StringUtil;
import com.gaiamount.util.image.ImageUtils;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by yukun on 16-9-29.
 */
public class DetailFragment extends Fragment implements View.OnClickListener {

    private long mid;
    private MaterialDetailInfo detailInfo;
    private StringUtil mStringUtil;
    private RatingBar mRatingBar;
    private TextView mScore;
    private Button mAdd_attention;
    private ArrayList<String> arrayList;
    private int materialType;


    public static DetailFragment newInstance(MaterialDetailInfo mDetailInfo, long mid,int materialType) {
        DetailFragment recomendFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("mid", mid);
        bundle.putInt("materialType",materialType);
        bundle.putSerializable("detailInfo", mDetailInfo);
        recomendFragment.setArguments(bundle);
        return recomendFragment;
    }


    private ImageView imageViewVip;
    private TextView textViewTag_1;
    private TextView textViewTag_2;
    private TextView textViewTag_3;
    private String[] stringArray;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        detailInfo = (MaterialDetailInfo) getArguments().getSerializable("video_detail_info");
        mStringUtil = StringUtil.getInstance();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Bundle arguments = getArguments();
        mid = arguments.getLong("mid");
        materialType = arguments.getInt("materialType");
        detailInfo = (MaterialDetailInfo) arguments.getSerializable("detailInfo");
        stringArray = getResources().getStringArray(R.array.material_video);
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
        long uid = detailInfo.getMaterial().getUserId();
        if (uid == GaiaApp.getUserInfo().id) {
            mAdd_attention.setTextColor(getResources().getColor(R.color.white));
            mAdd_attention.setBackgroundResource(R.drawable.shape_radius4_redstroke_whitebg);
            mAdd_attention.setClickable(false);
        } else {
            //判断当前视频的作者是否被关注了
            PersonApiHelper.isAttentioned(detailInfo.getMaterial().getUserId(), getActivity());
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
        PersonApiHelper.isAttentioned(detailInfo.getMaterial().getUserId(), getActivity());
    }


    private void initView(View view) {
        TextView tvName = (TextView) view.findViewById(R.id.name);
        TextView playCount_playTime = (TextView) view.findViewById(R.id.playcount_playtime);
        mScore = (TextView) view.findViewById(R.id.score);
        mScore.setVisibility(View.GONE);
        mRatingBar = (RatingBar) view.findViewById(R.id.play_grade_rating);
        mRatingBar.setVisibility(View.GONE);
        TextView master = (TextView) view.findViewById(R.id.master);
        TextView tag = (TextView) view.findViewById(R.id.tag);
        TextView back_story = (TextView) view.findViewById(R.id.back_story);
        imageViewVip = (ImageView) view.findViewById(R.id.image_is_vip);
        ImageView avatar = (ImageView) view.findViewById(R.id.avatar);
        mAdd_attention = (Button) view.findViewById(R.id.add_attention);
        TextView staff = (TextView) view.findViewById(R.id.staff);
        /*关注,三个tag*/
        TextView textViewCreate = (TextView) view.findViewById(R.id.create_num);
        textViewTag_1 = (TextView) view.findViewById(R.id.tag_1);
        textViewTag_2 = (TextView) view.findViewById(R.id.tag_2);
        textViewTag_3 = (TextView) view.findViewById(R.id.tag_3);

        //标签的点击事件
        textViewTag_1.setOnClickListener(this);
        textViewTag_2.setOnClickListener(this);
        textViewTag_3.setOnClickListener(this);

        if (detailInfo != null) {
//            String avinfo = detailInfo.getVideoInfo().getAvinfo();
//            parasString(avinfo);
            final MaterialDetailInfo.MaterialBean works = detailInfo.getMaterial();
            //作品头像
            Glide.with(getContext()).load(Configs.COVER_PREFIX + works.getAvatar()).placeholder(R.mipmap.ic_avatar_default).into(avatar);
            master.setText(works.getNickName());

            String description = works.getDescription();
            if(!"null".equals(description)&&description.length()!=0){
//                back_story.setText("缘起:"+description);
                back_story.setText("缘起:"+Html.fromHtml(description));
            }else {
                back_story.setVisibility(View.VISIBLE);
            }

            String worksName = works.getName();
            if (worksName.contains(".mp4")) {
                tvName.setText(worksName.substring(0, worksName.length() - 4));
            } else {
                tvName.setText(worksName);
            }
            //创作和粉丝
            int creationCount = works.getCreateCount();
            int likeCount = works.getFansCount();
            textViewCreate.setText("创作"+creationCount+" 粉丝"+likeCount);
            //播放次数，和上传的时间
            if (detailInfo.getMaterial().getCreateTime() != null && !detailInfo.getMaterial().getCreateTime().equals("null")) {
                Date d = new Date(works.getCreateTime().getTime());
                SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
                String time = sf.format(d);

                playCount_playTime.setText(detailInfo.getMaterial().getDownloadCount()+ "下载,"+works.getWorks_collectCount()+"收藏,"+"发布于"+ time/*.substring(0,10)*/);
            }
            //头像的点击事件
            avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityUtil.startPersonalActivity(getActivity(), works.getUserId());
                }
            });
            if(works.getIsVip()==1){
                if(works.getVipLevel()==2){
                    imageViewVip.setImageResource(R.mipmap.pro);
                }else if(works.getVipLevel()==3){
                    imageViewVip.setImageResource(R.mipmap.advs);
                }else if(works.getVipLevel()==4){
                    imageViewVip.setImageResource(R.mipmap.bns);
                }
            }

            /*分类的显示*/
            String type = works.getType();
            arrayList = getTags(type);
            if (arrayList != null && arrayList.size() != 0) {
                if (arrayList.size() == 1) {
                    textViewTag_1.setVisibility(View.VISIBLE);
                    textViewTag_1.setText(arrayList.get(0));
                    textViewTag_1.setBackgroundResource(R.drawable.shape_detail_play);
                } else if (arrayList.size() == 2) {
                    textViewTag_1.setVisibility(View.VISIBLE);
                    textViewTag_1.setText(arrayList.get(0));
                    textViewTag_2.setVisibility(View.VISIBLE);
                    textViewTag_2.setText(arrayList.get(1));
                    textViewTag_1.setBackgroundResource(R.drawable.shape_detail_play);
                    textViewTag_2.setBackgroundResource(R.drawable.shape_detail_play);
                } else if (arrayList.size() == 3) {
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

            //staff
            StringBuilder staffStr = new StringBuilder();

            if(materialType==1){ //视频素材
                staffStr.append("版本" + ":" + getVerson(detailInfo.getVideo().getWidth()) + "|");
                staffStr.append(getString(R.string.models) + ":" + works.getMachine() + "|");
                staffStr.append("分辨率" + ":" + detailInfo.getVideo().getWidth()+"X"+detailInfo.getVideo().getHeight());
                staffStr.append("帧率" +":"+ works.getFps()+" fps" + "|");
                staffStr.append(getString(R.string.locations) + ":" + works.getAddress() + "|");
                staffStr.append("格式" + ":" + works.getFileFormat() + "|");
                staffStr.append("大小"+":"+mStringUtil.stringForSize(works.getSize())+ "|");
                staffStr.append("拍摄" + ":" + works.getPhotographer() + "|");
                staffStr.append(getString(R.string.lens) + ":" + works.getLens() + "|");
                staffStr.append("编码" + ":" + detailInfo.getAudio().getCodec_long_name() + "");

            }else if(materialType==0){  //模板素材
                staffStr.append("视频版本" + ":" + detailInfo.getMaterial().getTemplate() + "|");
                staffStr.append("大小" + ":" + mStringUtil.stringForSize(works.getSize()) + "|");
                staffStr.append("分辨率" + ":" + detailInfo.getVideo().getWidth()+"X"+detailInfo.getVideo().getHeight());
            }

            staff.setText(staffStr.toString());
            if (mAdd_attention != null) {
                mAdd_attention.setOnClickListener(addAttentionClickListener);
            }
        }
    }
    private String getVerson (int width){
        if(width==4096){
            return "4K";
        }
        if(width==3840){
            return "UHD";
        }
        if(width==1920){
            return "1080P";
        }if(width==1280){
            return "720P";
        }
        return "";
    }

    /**
     * 点击关注/取消关注
     */
    private View.OnClickListener addAttentionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isAttentioned) {
                PersonApiHelper.addAttention(detailInfo.getMaterial().getUserId(), 0, getActivity());
            } else {
                PersonApiHelper.addAttention(detailInfo.getMaterial().getUserId(), 1, getActivity());
            }
            PersonApiHelper.isAttentioned(detailInfo.getMaterial().getUserId(), getActivity());

        }
    };

    private ArrayList getTags(String type) {
        ArrayList<String> stringarray = new ArrayList<>();
        String[] split = type.split(",");
        for (int i = 0; i < split.length; i++) {
            String strings = split[i];
            String tagName = changeString(strings);
            if (!tagName.equals(""))
                stringarray.add(tagName);
        }
        return stringarray;
    }

    private String changeString(String strings) {
        if (!strings.equals("")) {
            if (strings.equals("0")) {
                return stringArray[0];
            } else if (strings.equals("1")) {
                return stringArray[1];
            } else if (strings.equals("2")) {
                return stringArray[2];
            } else if (strings.equals("3")) {
                return stringArray[3];
            } else if (strings.equals("4")) {
                return stringArray[4];
            } else if (strings.equals("5")) {
                return stringArray[5];
            } else if (strings.equals("6")) {
                return stringArray[6];
            } else if (strings.equals("7")) {
                return stringArray[7];
            } else if (strings.equals("8")) {
                return stringArray[8];
            } else if (strings.equals("9")) {
                return stringArray[9];
            } else if (strings.equals("10")) {
                return stringArray[10];
            } else if (strings.equals("11")) {
                return stringArray[11];
            } else if (strings.equals("12")) {
                return stringArray[12];
            }else if (strings.equals("13")) {
                return stringArray[13];
            }else if (strings.equals("14")) {
                return stringArray[14];
            }else if (strings.equals("15")) {
                return stringArray[15];
            }else if (strings.equals("16")) {
                return stringArray[16];
            }else if (strings.equals("17")) {
                return stringArray[17];
            }else if (strings.equals("18")) {
                return stringArray[18];
            }
        }
        return "";

    }

    private boolean isAttentioned = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.score:
                break;
            case R.id.tag_1:
                //每一个标签的点击事件
                Intent intent = new Intent(getContext(), GlobalSearchActivity.class);
                intent.putExtra(GlobalSearchActivity.WORDS, "");
                intent.putExtra(GlobalSearchActivity.KEY_WORDS, textViewTag_1.getText());
                intent.putExtra(GlobalSearchActivity.KIND, 0);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.tag_2:
                Intent intent1 = new Intent(getContext(), GlobalSearchActivity.class);
                intent1.putExtra(GlobalSearchActivity.WORDS, "");
                intent1.putExtra(GlobalSearchActivity.KEY_WORDS, textViewTag_2.getText());
                intent1.putExtra(GlobalSearchActivity.KIND, 0);
                getActivity().startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.tag_3:
                Intent intent2 = new Intent(getContext(), GlobalSearchActivity.class);
                intent2.putExtra(GlobalSearchActivity.WORDS, "");
                intent2.putExtra(GlobalSearchActivity.KEY_WORDS, textViewTag_3.getText());
                intent2.putExtra(GlobalSearchActivity.KIND, 0);
                getActivity().startActivity(intent2);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            default:
                break;
        }
    }
}
