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
import com.gaiamount.module_creator.create_person.WorksInfo;
import com.gaiamount.module_workpool.WorkRecBean;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.image.ImageUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by haiyang-lu on 16-4-25.
 */
public class HomeGridAdapter extends BaseAdapter {
    int itemHeight;
    private Context mContext;
    private List<WorkInfo> mWorkRecBeanList;
    private final ImageUtils mImageUtils;
    private final LayoutInflater mInflater;

    public HomeGridAdapter(Context context, List<WorkInfo> workRecBeanList) {
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
        final WorkInfo workRec = mWorkRecBeanList.get(position);
        //名称
        TextView workName = (TextView) item_grid.findViewById(R.id.work_name);
        //是否4k
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
                ActivityUtil.startPlayerActivity((Activity) mContext,(int)workRec.getWid(), 0,cover);
            }
        });

        //评分
        TextView workGrade = (TextView) item_grid.findViewById(R.id.work_grade);

        //设置封面
        String screenshot = workRec.getScreenshot();
        String coverMap = workRec.getCover();
        if(coverMap!=null&&!coverMap.isEmpty()&&!"null".equals(coverMap)){
            Glide.with(mContext).load(Configs.COVER_PREFIX+coverMap).placeholder(R.mipmap.bg_general).into(cover);
        }else if(screenshot!=null&&!screenshot.isEmpty()){
            if(workRec.getFlag()==1){
                Glide.with(mContext).load(Configs.COVER_PREFIX+screenshot+"_18.png").placeholder(R.mipmap.bg_general).into(cover);
            }else if(workRec.getFlag()==0){
                Glide.with(mContext).load(Configs.COVER_PREFIX+screenshot.replace(".","_18.")).placeholder(R.mipmap.bg_general).into(cover);
            }
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
        DecimalFormat decimalFormat = new DecimalFormat("###0.0");//保留一位小数
        workGrade.setText(decimalFormat.format(workRec.getGrade()));
        return item_grid;
    }
}
