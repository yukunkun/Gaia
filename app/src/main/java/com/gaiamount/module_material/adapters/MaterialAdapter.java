package com.gaiamount.module_material.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.gaiamount.module_academy.bean.MixLightDecoration;
import com.gaiamount.module_academy.viewholder.ViewHolderMore;
import com.gaiamount.module_material.MaterialMainActivity;
import com.gaiamount.module_material.activity.MaterialModelActivity;
import com.gaiamount.module_material.activity.MaterialVideosActivity;
import com.gaiamount.module_material.bean.MaterialDecoration;
import com.gaiamount.module_material.bean.MaterialInfo;
import com.gaiamount.module_material.bean.StreamInfo;
import com.gaiamount.module_material.viewholder.OriRecyclerViewHolder;
import com.gaiamount.module_material.viewholder.ViewHolderMore2;
import com.gaiamount.module_material.viewholder.ViewPagerHolder;
import com.gaiamount.module_material.viewholder.adapter.OriRecyclerAdapter;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yukun on 16-9-28.
 */
public class MaterialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MaterialMainActivity.Banner> banners;
    private Context context;
    private int BANNER_TYPE=0;
    private int Re_PRO=1;
    private OriRecyclerAdapter oriRecyclerAdapter;
    private NoScrollViewMaterialAdapter adapter;
    private List<String> splitImage=new ArrayList<>();
    List<MaterialInfo> materialInfos;
    boolean tag=true;
    private NoScrollViewMaterialAdapter adapter2;
    private List<Long> idList;

    public MaterialAdapter(List<MaterialInfo> materialInfos, ArrayList<String> split, List<MaterialMainActivity.Banner> banners, Context context) {
        this.banners = banners;
        this.context = context;
        this.materialInfos=materialInfos;
        this.splitImage=split;
    }

    public void update(List<Long> idList){
       this.idList=idList;
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
            view= LayoutInflater.from(context).inflate(R.layout.material_ori_recyclerview,null);
            OriRecyclerViewHolder holder=new OriRecyclerViewHolder(view);
            return holder;
        }else if(viewType==2){
            view= LayoutInflater.from(context).inflate(R.layout.academy_gridview_item,null);
            ViewHolderMore holder=new ViewHolderMore(view);
            return holder;
        }else {
            view= LayoutInflater.from(context).inflate(R.layout.academy_gridview_item,null);
            ViewHolderMore2 holder=new ViewHolderMore2(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewPagerHolder) {
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
                ((ViewPagerHolder) holder).mLinmaterial.setOnClickListener(new Listener());
                ((ViewPagerHolder) holder).mLinMoban.setOnClickListener(new Listener());
                ((ViewPagerHolder) holder).mConvenientBanner.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        try {
                            if(idList.size()>position&&idList.get(position)!=null){
                                //跳转到播放页
                                ActivityUtil.startMaterialPlayActivity(context,idList.get(position),1);
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                });
                tag=false;
            }
        }

        if(holder instanceof OriRecyclerViewHolder){
            StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL);
            ((OriRecyclerViewHolder)holder).recyclerView.setLayoutManager(manager);
            oriRecyclerAdapter = new OriRecyclerAdapter(context,banners);
            ((OriRecyclerViewHolder)holder).recyclerView.setAdapter(oriRecyclerAdapter);
            ((OriRecyclerViewHolder)holder).recyclerView.addItemDecoration(new MaterialDecoration(5,10,3,3));

            ((OriRecyclerViewHolder)holder).textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, MaterialVideosActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            oriRecyclerAdapter.notifyDataSetChanged();
        }else if(holder instanceof ViewHolderMore){

            List<MaterialInfo> materialInfos1=new ArrayList<>();
            if(materialInfos.size()>6){
                for (int i = 0; i < 6; i++) {
                    materialInfos1.add(materialInfos.get(i));
                }
            }else {
                for (int i = 0; i < materialInfos.size(); i++) {
                    materialInfos1.add(materialInfos.get(i));
                }
            }
            ((ViewHolderMore)holder).textViewtitle.setText("视频素材推荐");

            ((ViewHolderMore)holder).mTextViewMore.setText("更多");
            ((ViewHolderMore)holder).mTextViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //更多的点击事件按
                    Intent intent=new Intent(context, MaterialVideosActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            adapter = new NoScrollViewMaterialAdapter(context,materialInfos1);
            ((ViewHolderMore)holder).noScrolledGridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            //每个item的点击事件
            ((ViewHolderMore)holder).noScrolledGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    ActivityUtil.startMaterialPlayActivity(context,materialInfos.get(position).getId(),1);
                }
            });

        }/*else if(holder instanceof ViewHolderMore2){
            final List<MaterialInfo> materialInfos2=new ArrayList<>();
            if(materialInfos.size()>12){
                for (int i = 6; i < 12; i++) {
                    materialInfos2.add(materialInfos.get(i));
                }

            }else if(materialInfos.size()>6&&materialInfos.size()<=12){
                for (int i = 6; i < materialInfos.size(); i++) {
                    materialInfos2.add(materialInfos.get(i));
                }
            }
            ((ViewHolderMore2)holder).textViewtitle.setText("模板素材推荐");
            ((ViewHolderMore2)holder).mTextViewMores.setText("更多");
            ((ViewHolderMore2)holder).textViewtitle.setVisibility(View.GONE);
            ((ViewHolderMore2)holder).mTextViewMores.setVisibility(View.GONE);
//            ((ViewHolderMore2)holder).mTextViewMores.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent1=new Intent(context, MaterialModelActivity.class);
//                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent1);
//                }
//            });
//            adapter2 = new NoScrollViewMaterialAdapter(context,materialInfos2);
//            ((ViewHolderMore2)holder).noScrolledGridView.setAdapter(adapter2);
//            adapter2.notifyDataSetChanged();
//            //每个item的点击事件
//            ((ViewHolderMore2)holder).noScrolledGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    ActivityUtil.startMaterialPlayActivity(context,materialInfos2.get(position).getId(),0);
//                }
//            });
        }*/
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
                case R.id.material_light:
                    Intent intent1=new Intent(context, MaterialModelActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
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
        }else {
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
