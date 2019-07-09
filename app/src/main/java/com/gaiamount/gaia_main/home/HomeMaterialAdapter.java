package com.gaiamount.gaia_main.home;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.gaia_main.home.bean.WorkInfo;
import com.gaiamount.module_material.bean.MaterialInfo;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.image.ImageUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by haiyang-lu on 16-4-25.
 */
public class HomeMaterialAdapter extends BaseAdapter {
    int itemHeight;
    private Context mContext;
    private List<MaterialInfo> mWorkRecBeanList;
    private final ImageUtils mImageUtils;
    private final LayoutInflater mInflater;

    public HomeMaterialAdapter(Context context, List<MaterialInfo> workRecBeanList) {
        mContext = context;
        mWorkRecBeanList = workRecBeanList;//传递数据
        //动态设置图片的高度
        int width = ScreenUtils.instance().getWidth();
        itemHeight = (int) (((width - 48) / 2) * 0.562);
        mImageUtils = ImageUtils.getInstance(context);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mWorkRecBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View item_grid = mInflater.inflate(R.layout.item_grid, null, false);
        final MaterialInfo workRec = mWorkRecBeanList.get(position);
        //名称
        TextView workName = (TextView) item_grid.findViewById(R.id.work_name);
        //是否4K
        ImageView is4k = (ImageView) item_grid.findViewById(R.id.is_4k);
        //是否官方
        ImageView isOfficial = (ImageView) item_grid.findViewById(R.id.is_official);
        //封面
        final ImageView cover = (ImageView) item_grid.findViewById(R.id.cover);
        ViewGroup.LayoutParams layoutParams = cover.getLayoutParams();
        layoutParams.height = itemHeight;
        cover.setLayoutParams(layoutParams);

        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityUtil.startMaterialPlayActivity(mContext,workRec.getId(),1);// 1 表示视频素材
            }
        });

        //评分
        TextView workGrade = (TextView) item_grid.findViewById(R.id.work_grade);

        //设置封面
        String coverMap = workRec.getCover();
        String screenshot = workRec.getScreenshot();
        if(coverMap!=null&&!coverMap.isEmpty()){
            Glide.with(mContext).load(Configs.COVER_PREFIX+coverMap).placeholder(R.mipmap.bg_general).into(cover);
        }else if(screenshot!=null&&!screenshot.isEmpty()){
            Glide.with(mContext).load(Configs.COVER_PREFIX+screenshot+"_18.png").placeholder(R.mipmap.bg_general).into(cover);
        }

        //设置作品名称
        String name = workRec.getName();
        workName.setText(name);

        is4k.setVisibility(View.GONE);
        isOfficial.setVisibility(View.GONE);
        //设置是否4k，是否官方
//        if (workRec.getIs4K() == 1) {
//            is4k.setVisibility(View.VISIBLE);
//        } else {
//            is4k.setVisibility(View.GONE);
//        }
//        if (workRec.getIsOfficial() == 1) {
//            isOfficial.setVisibility(View.VISIBLE);
//        } else {
//            isOfficial.setVisibility(View.GONE);
//        }
        //设置评分
        return item_grid;
    }
}
