package com.gaiamount.module_player.fragments;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.file_api.FileApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_down_up_load.download.DownloadItem;
import com.gaiamount.module_player.bean.VideoDetailInfo;
import com.gaiamount.module_down_up_load.download.DownloadUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.StringUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haiyang-lu on 16-5-5.
 */
public class PlayerDownloadFrag extends DialogFragment implements View.OnClickListener {
    private static String DETAIL_INFO  = "detailInfo";

    public static final String ORIGINAL = "源文件";
    public static final String K4 = "4k";
    public static final String K2 = "2k";
    public static final String P1080 = "1080p";
    public static final String P720 = "720p";


    private VideoDetailInfo mDetailInfo;
    private int mItemWidth;
    private int mOptionContainerWidth;
    private String tempUrl = "http://7xqzf0.dl1.z0.glb.clouddn.com/origin/u6115/v9129.wmv?e=1464266805&token=_6yhTpZI-AHtfULMSiW7eOCvrNEnOS_YcLV9GNfH:mkBwTm5sIM800_Wg12L3vbdE11Y=";
    private int mWorkId;
    private int mContentType;


    public static PlayerDownloadFrag newInstance(VideoDetailInfo detailInfo, int workId,int contentType) {

        PlayerDownloadFrag downloadFrag = new PlayerDownloadFrag();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DETAIL_INFO,detailInfo);
        bundle.putInt("work_id",workId);
        bundle.putInt("content_type",contentType);
        downloadFrag.setArguments(bundle);
        return downloadFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mDetailInfo = (VideoDetailInfo) arguments.getSerializable(DETAIL_INFO);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        mWorkId = arguments.getInt("work_id");
        mContentType = arguments.getInt("content_type");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(null);
        //5.0以下去除标题
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_download, container, false);

        TextView subTitle = (TextView) view.findViewById(R.id.player_download_username);
        ImageView clear = (ImageView) view.findViewById(R.id.player_download_clear);
        LinearLayout optionContainer =  (LinearLayout) view.findViewById(R.id.player_download_option_container);
        TextView property = (TextView) view.findViewById(R.id.player_download_property);
        Button startDownload = (Button) view.findViewById(R.id.player_download_start);
//        Button addToList = (Button) view.findViewById(R.id.player_download_add_to_list);

        //子标题
        subTitle.setText("该内容由原作者"+mDetailInfo.getUser().getNickName()+"提供");

        //属性
        StringBuilder propertyStr = new StringBuilder();
        VideoDetailInfo.VideoInfoBean videoInfo = mDetailInfo.getVideoInfo();
            //分辨率
        String dpi = videoInfo.getWidth()+"x"+videoInfo.getHeight();
        propertyStr.append(dpi+"  ");
            //格式
        String format = videoInfo.getFormat();
        propertyStr.append(format+"  ");
            //帧率
        String fps = videoInfo.getFps();
        propertyStr.append(fps.split("/")[0]+"fps  ");
            //码率(单位Kbps)
        String bitrate = StringUtil.getInstance().stringForBitrate(videoInfo.getBitRate())+"  ";
        propertyStr.append(bitrate);
        //大小
        String size = StringUtil.getInstance().stringForSize(videoInfo.getSize());
        propertyStr.append(size);

        property.setText(propertyStr);

        //点击事件
        startDownload.setOnClickListener(this);
//        addToList.setOnClickListener(this);

        clear.setOnClickListener(this);

        //动态加入视频下载的清晰度选项
        //获取容器的宽
        mOptionContainerWidth = optionContainer.getWidth();
        //每个item的宽
        mItemWidth = ScreenUtils.dp2Px(getActivity(),64);
        //计算item的右边距的大小

        //肯定有个源文件版本
        double priceOriginal = mDetailInfo.getWorks().getPriceOriginal();
        addItem(optionContainer,ORIGINAL,String.valueOf(priceOriginal));
        VideoDetailInfo.WorksBean works = mDetailInfo.getWorks();
            //4k
        if(works.getHave4K()==1) {
            //有4k
            double price4K = works.getPrice4K();
            addItem(optionContainer,K4,String.valueOf(price4K));
        }
            //2k
        if (works.getHave2K()==1) {
            //有2k
            double price2K = works.getPrice2K();
            addItem(optionContainer,K2,String.valueOf(price2K));
        }
            //1080P
        if (works.getHave1080()==1) {
            double price1080 = works.getPrice1080();
            addItem(optionContainer,P1080,String.valueOf(price1080));
        }
            //720P
        if (works.getHave720()==1) {
            double price720 = works.getPrice720();
            addItem(optionContainer,P720,String.valueOf(price720));
        }

        return view;
    }

    private List<View> itemList = new ArrayList<>();
    private void addItem(LinearLayout optionContainer, final String qualityStr, String priceStr) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.item_player_download_frag_option, optionContainer,false);

        ItemPropertity itemPropertity = new ItemPropertity(qualityStr,0);
        itemView.setTag(itemPropertity);
        itemList.add(itemView);
        //设置点击事件
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBg(qualityStr,itemView);
            }
        });

        //默认选中源文件
        if (ORIGINAL.equals(qualityStr)) {
            itemView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            itemView.setTag(new ItemPropertity(qualityStr,1));
        }

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) itemView.getLayoutParams();
        layoutParams.width = mItemWidth;
        layoutParams.setMargins(0,0,16,0);
        //文件质量
        TextView quality = (TextView) itemView.findViewById(R.id.player_download_quality);
        TextView price = (TextView) itemView.findViewById(R.id.player_download_price);
        //赋值
        quality.setText(qualityStr);

        price.setText("￥"+priceStr);

        optionContainer.addView(itemView,layoutParams);

    }


    private void changeBg(String qualityStr, ViewGroup itemView) {
        for (int i = 0;i<itemList.size();i++) {
            View view = itemList.get(i);
            ItemPropertity tag = (ItemPropertity) view.getTag();
            if (tag.isChecked==1) {
                view.setBackgroundResource(R.drawable.shape_download_price_tag_bg);
            }
        }
        itemView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        itemView.setTag(new ItemPropertity(qualityStr,1));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_download_clear:
                //关闭自己
                dismiss();
                break;
            case R.id.player_download_start:

                //判断是否能下载
                int allowDownload = mDetailInfo.getWorksPermission().getAllowDownload();
                if (allowDownload==0) {
                    GaiaApp.showToast("此视频不能被下载");
                    return;
                }

                int e = 0;//默认下载源文件
                for (int i = 0;i<itemList.size();i++) {
                    ItemPropertity tag = (ItemPropertity) itemList.get(i).getTag();
                    String quality = tag.quality;
                    int isChecked = tag.isChecked;
                    if (ORIGINAL.equals(quality)&&isChecked==1) e = 0;
                    else if (K4.equals(quality)&&isChecked==1) e = 1;
                    else if (K2.equals(quality)&&isChecked==1) e = 2;
                    else if (P1080.equals(quality)&&isChecked==1) e = 3;
                    else if (P720.equals(quality)&&isChecked==1) e = 4;
                }
                //获取下载地址
                long uId = GaiaApp.getUserInfo().id;
                JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(PlayerDownloadFrag.class){
                    @Override
                    public void onGoodResponse(JSONObject response) {
                        super.onGoodResponse(response);
                        String downloadUrl = response.optJSONObject("o").optString("downloadUrl");
                        DownloadItem downloadItem = new DownloadItem(false,false);
                        downloadItem.videoName = mDetailInfo.getWorks().getName();
                        downloadItem.url = downloadUrl;
                        //----
                        downloadItem.id = mWorkId;
                        downloadItem.contentType = mContentType;
                        //----
                        DownloadUtil.getInstance(getActivity()).addTask(Uri.parse(downloadUrl),downloadItem);
                        dismiss();
                        GaiaApp.showToast("已添加到下载列表中");
                    }

                    @Override
                    public void onBadResponse(JSONObject response) {
                        super.onBadResponse(response);
                        int i = response.optInt("i");
                        if (i==50301) {
                            GaiaApp.showToast("用户身份过期，请重新登陆");
                        }
                    }
                };



                FileApiHelper.downloadVideo(uId,mWorkId,mContentType,e,getActivity(),jsonHttpResponseHandler);
                break;
            default:
                break;
        }
    }

    class ItemPropertity {
        String quality;
        int isChecked;

        public ItemPropertity(String quality, int isChecked) {
            this.quality = quality;
            this.isChecked = isChecked;
        }
    }
}
