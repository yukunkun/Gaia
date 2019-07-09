package com.gaiamount.module_academy.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;


import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.module_academy.bean.AcademyAll;
import com.gaiamount.module_academy.bean.AcademyInfo;
import com.gaiamount.module_academy.bean.OnEventId;
import com.gaiamount.module_academy.viewholder.ViewHolderMore;
import com.gaiamount.module_academy.viewholder.ViewPagerHolder;
import com.gaiamount.module_workpool.WorkRecBean;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created by yukun on 16-8-2.
 */
public class AcademyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int BANNER_TYPE=0;
    private ArrayList<String> covers;
    private boolean temp=true;
    private Context context;
    private int collegeId;
    int id;
    private ArrayList<AcademyAll> academyAlls;
    private Handler handler=new Handler();
    private NoScrollViewAdapter adapter;
    private ArrayList<String> listId=new ArrayList<>();
    public AcademyAdapter(Context context, int collegeId, ArrayList<String> covers, ArrayList<AcademyAll> academyAlls){
        this.context=context;
        this.covers=covers;
        this.collegeId=collegeId;
        this.academyAlls=academyAlls;
        EventBus.getDefault().register(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnEventId event) {
        id = event.id;
        EventBus.getDefault().unregister(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        if(viewType==BANNER_TYPE){
            view= LayoutInflater.from(context).inflate(R.layout.academy_viewpager,null);
            ViewPagerHolder holder=new ViewPagerHolder(view);
            return holder;
        }
            else {
                view= LayoutInflater.from(context).inflate(R.layout.academy_gridview_item,null);
                ViewHolderMore holder=new ViewHolderMore(view);
            return holder;
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewPagerHolder){
            if (temp&&covers.size()!=0){
                temp=false;
                for (int i = 0; i < covers.size(); i++) {
                    String s = covers.get(i);
                    int i1 = s.lastIndexOf("/");
                    listId.add(s.substring(i1+1,s.length()));
                }
                ViewGroup.LayoutParams layoutParams = ((ViewPagerHolder)holder).mConvenientBanner.getLayoutParams();
                layoutParams.height = getHeight();
                ((ViewPagerHolder)holder).mConvenientBanner.setLayoutParams(layoutParams);

                ((ViewPagerHolder)holder).mConvenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                }, covers)
                        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                        .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                        //设置指示器的方向
                        .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
                ((ViewPagerHolder)holder).mConvenientBanner.startTurning(4000);
                ((ViewPagerHolder)holder).mConvenientBanner.setCanLoop(true);
                ((ViewPagerHolder)holder).mLinAll.setOnClickListener(new Listener());
                ((ViewPagerHolder)holder).mLinMix.setOnClickListener(new Listener());
                ((ViewPagerHolder)holder).mLinLesson.setOnClickListener(new Listener());

                ((ViewPagerHolder)holder).mConvenientBanner.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        try{
                            //不是long类型的不让点击
                            ActivityUtil.startAcademyDetailActivity(Long.valueOf(listId.get(position)),context);
                        }catch (NumberFormatException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

        else if(holder instanceof ViewHolderMore){

            final AcademyAll academyAll = academyAlls.get(position-1);
            final ArrayList<AcademyInfo> academyInfos = academyAll.getAcademyInfos();
            ((ViewHolderMore)holder).textViewtitle.setText(academyAll.getListName());
            adapter = new NoScrollViewAdapter(context,academyInfos);
            ((ViewHolderMore)holder).noScrolledGridView.setAdapter(adapter);
            //每个item的点击事件
            ((ViewHolderMore)holder).noScrolledGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    ActivityUtil.startAcademyDetailActivity(academyInfos.get(position).getCourseId(),context);
                }
            });
        }
    }

    class Listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.academy_all:
                    ActivityUtil.startContributionActivity(context);
                    break;
                case R.id.academy_mix_light:
                    ActivityUtil.startMixingLightActivity(context);
                    break;
                case R.id.academy_lesson:
                    ActivityUtil.startMyLessonActivity(context);//我的课表页面
                    break;

            }
        }
    }
    @Override
    public int getItemCount() {
        return academyAlls.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
       if(position==0){
           return BANNER_TYPE;
       }
        else {
           return position;
        }
    }

    public int getHeight(){//设置宽高比例
        int itemHeight;
        int width = ScreenUtils.instance().getWidth();
        itemHeight = (int) ((width)*0.33);
        return itemHeight;
    }

    //加载图片的类
    public class NetworkImageHolderView implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, String data) {
            if(!TextUtils.isEmpty(data)&&!data.equals("null")){
                String[] split = data.split(",");
                Glide.with(context).load(Configs.COVER_PREFIX+split[0]).placeholder(R.mipmap.bg_general).into(imageView);
            }
        }
    }
}
