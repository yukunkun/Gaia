package com.gaiamount.module_user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.module_user.personal.PersonalWorks;
import com.gaiamount.util.image.ImageUtils;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.StringUtil;

import java.util.List;

/**
 * Created by haiyang-lu on 16-4-26.
 */
public class PersonalCreateAdapter extends BaseAdapter {
    private Context mContext;
    private List<PersonalWorks> mPersonalWorkses;
    private final LayoutInflater mInflater;
    private final ImageUtils mImageUtils;
    private final int mScreenWidth;
    private final int mItemHeight;

    public PersonalCreateAdapter(Context context, List<PersonalWorks> personalWorkses) {

        mContext = context;
        mPersonalWorkses = personalWorkses;
        mInflater = LayoutInflater.from(mContext);
        mImageUtils = ImageUtils.getInstance(mContext);
        mScreenWidth = ScreenUtils.instance().getWidth();
        int itemWidth = (mScreenWidth -(3*ScreenUtils.dp2Px(context,8)))/2;
        mItemHeight = (int) (itemWidth *0.562);
    }

    @Override
    public int getCount() {
        return mPersonalWorkses.size();
    }

    @Override
    public Object getItem(int position) {
        return mPersonalWorkses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null) {
            convertView = mInflater.inflate(R.layout.list_item_personal_create,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //赋值
        holder.setValue(position);

        return convertView;
    }

    class ViewHolder{
        private ImageView workCover;
        private TextView workDuration;
        private ImageView outh;
        private ImageView is4k;
        private TextView workName;
        private TextView workCount;
        private TextView workGrade;

        public ViewHolder(View itemView) {
            workCover = (ImageView) itemView.findViewById(R.id.work_cover);
            workDuration = (TextView) itemView.findViewById(R.id.work_duration);
            outh = (ImageView) itemView.findViewById(R.id.outh);
            is4k = (ImageView) itemView.findViewById(R.id.quality4);
            workName = (TextView) itemView.findViewById(R.id.work_name);
            workCount = (TextView) itemView.findViewById(R.id.work_count);
            workGrade = (TextView) itemView.findViewById(R.id.work_grade);
            workName.setMaxLines(2);
            workCover.getLayoutParams().height = mItemHeight;
        }

        public void setValue(int position) {
            PersonalWorks personalWorks = mPersonalWorkses.get(position);
            String cover = personalWorks.getCover();
            String screenshot = personalWorks.getScreenshot();
            //作品封面
            mImageUtils.showImage(workCover,cover,screenshot);
            //作品时长
            workDuration.setText(StringUtil.getInstance().stringForTime(personalWorks.getDuration()*1000));
            //是否官方
            outh.setVisibility(1 != personalWorks.getIsOfficial() ? View.GONE : View.VISIBLE);
            //是否4k
            is4k.setVisibility(personalWorks.getIs4K()==1?View.VISIBLE:View.GONE);
            //作品名称
            workName.setText(personalWorks.getName());
            //作品播放数量
            workCount.setText(StringUtil.getInstance().stringForCount(personalWorks.getPlayCount()));
            //作品评分
            workGrade.setText(Double.toString(personalWorks.getGrade()));
        }
    }

}
