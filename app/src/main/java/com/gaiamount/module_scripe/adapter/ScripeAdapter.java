package com.gaiamount.module_scripe.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.module_academy.viewholder.ViewHolderMore;
import com.gaiamount.module_material.MaterialMainActivity;
import com.gaiamount.module_material.activity.MaterialModelActivity;
import com.gaiamount.module_material.activity.MaterialVideosActivity;
import com.gaiamount.module_material.adapters.NoScrollViewMaterialAdapter;
import com.gaiamount.module_material.bean.MaterialDecoration;
import com.gaiamount.module_material.bean.MaterialInfo;
import com.gaiamount.module_material.viewholder.OriRecyclerViewHolder;
import com.gaiamount.module_material.viewholder.ViewHolderMore2;
import com.gaiamount.module_material.viewholder.ViewPagerHolder;
import com.gaiamount.module_material.viewholder.adapter.OriRecyclerAdapter;
import com.gaiamount.module_scripe.activity.ScripeDetailActivity;
import com.gaiamount.module_scripe.activity.ScripeListActivity;
import com.gaiamount.module_scripe.bean.EdidsInfo;
import com.gaiamount.module_scripe.bean.ScripeInfo;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.widgets.improved.NOScrolledListView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yukun on 16-9-28.
 */
public class ScripeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MaterialMainActivity.Banner> banners;
    private Context context;
    private int BANNER_TYPE=0;
    private int Re_PRO=1;
    private int COMMENT=2;
    private OriRecyclerAdapter oriRecyclerAdapter;
    private List<String> splitImage=new ArrayList<>();
    List<ScripeInfo> scripeInfos;
    boolean tag=true;
    private ArrayList<Long> idList;
    private ArrayList<EdidsInfo> edidsInfos;

    public ScripeAdapter(Context context,List<ScripeInfo> materialInfos, ArrayList<String> split, ArrayList<Long> ids,ArrayList<EdidsInfo> edidsInfos) {
        this.idList = ids;
        this.context = context;
        this.scripeInfos=materialInfos;
        this.splitImage=split;
        this.edidsInfos=edidsInfos;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        if(viewType==BANNER_TYPE){
            view= LayoutInflater.from(context).inflate(R.layout.material_viewpager,null);
            ViewPagerHolder holder=new ViewPagerHolder(view);
            return holder;
        }
        else if(viewType==Re_PRO){
            view= LayoutInflater.from(context).inflate(R.layout.academy_gridview_item,null);
            ViewHolderMore holder=new ViewHolderMore(view);
            return holder;
        }else if(viewType==COMMENT){
            view= LayoutInflater.from(context).inflate(R.layout.scripe_listview_item,null);
            MyViewHolder holder=new MyViewHolder(view);
            return holder;
        }
        return  null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewPagerHolder) {
            //隐藏Icon
            ((ViewPagerHolder) holder).linclude.setVisibility(View.GONE);

            if (tag==true&&splitImage.size()!=0) {
                ViewGroup.LayoutParams layoutParams = ((ViewPagerHolder) holder).mConvenientBanner.getLayoutParams();
                layoutParams.height = getHeight();
                ((ViewPagerHolder) holder).mConvenientBanner.setLayoutParams(layoutParams);

                ((ViewPagerHolder) holder).mConvenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                }, splitImage)
                        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                        .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                        //设置指示器的方向
                        .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
                ((ViewPagerHolder) holder).mConvenientBanner.startTurning(4000);
                ((ViewPagerHolder) holder).mConvenientBanner.setCanLoop(true);

                ((ViewPagerHolder) holder).mConvenientBanner.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        try {
                            ActivityUtil.startScripeDetailActivity(context,idList.get(position));

                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                });
                tag=false;
            }
        } else if(holder instanceof ViewHolderMore){

            ((ViewHolderMore)holder).textViewtitle.setText("热门推荐");
            ((ViewHolderMore)holder).mTextViewMore.setText("更多");
            ((ViewHolderMore)holder).mTextViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //更多的点击事件按

                }
            });
            ((ViewHolderMore)holder).noScrolledGridView.setHorizontalSpacing(20);
            NoScrollGrideScripeAdapter scripeAdapter=new NoScrollGrideScripeAdapter(context,scripeInfos);
            ((ViewHolderMore)holder).noScrolledGridView.setAdapter(scripeAdapter);
            //每个item的点击事件
            ((ViewHolderMore)holder).noScrolledGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //剧本详情页
                    ActivityUtil.startScripeDetailActivity(context,scripeInfos.get(position).getSid());
                }
            });
            ((ViewHolderMore)holder).mTextViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, ScripeListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

        }else if(holder instanceof MyViewHolder){

            ((MyViewHolder)holder).textViewTitle.setText("人气编剧");
            NoScrollListScripeAdapter adapter=new NoScrollListScripeAdapter(context,edidsInfos);
            ((MyViewHolder)holder).noScrolledListView.setAdapter(adapter);

            ((MyViewHolder)holder).textViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //更多的点击事件按

                }
            });
        }
    }

    class Listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.material_all:
                    Intent intent=new Intent(context, MaterialVideosActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;

            }
        }
    }
    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return BANNER_TYPE;
        }
        else if(position==1){
            return Re_PRO;
        }else if(position==2){
            return 2;
        }
        return position;
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

   public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle,textViewMore;
        NOScrolledListView noScrolledListView;
        public MyViewHolder(View itemView) {
            super(itemView);
            textViewTitle= (TextView) itemView.findViewById(R.id.academy_title);
            textViewMore= (TextView) itemView.findViewById(R.id.scripe_more);
            noScrolledListView= (NOScrolledListView) itemView.findViewById(R.id.academy_recommend);
        }
    }

}
