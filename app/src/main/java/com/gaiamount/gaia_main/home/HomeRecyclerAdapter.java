package com.gaiamount.gaia_main.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.gaia_main.home.bean.WorkInfo;
import com.gaiamount.module_academy.AcademyActivity;
import com.gaiamount.module_academy.bean.AcademyInfo;
import com.gaiamount.module_creator.create_person.WorksInfo;
import com.gaiamount.module_material.MaterialMainActivity;
import com.gaiamount.module_material.bean.MaterialInfo;
import com.gaiamount.module_workpool.WorkRecBean;
import com.gaiamount.util.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haiyang-lu on 16-5-20.
 * home主页列表的适配器
 */
public class HomeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String[] mTitles;
    private LayoutInflater inflater;
    private Context mContext;
    private List<WorkInfo> mWorkRecBeanList;
    private List<MaterialInfo> materialInfos;
    private List<AcademyInfo> academyInfos;

    public HomeRecyclerAdapter(Context context, List<WorkInfo> workRecBeanList,List<MaterialInfo> materialInfos,List<AcademyInfo> academyInfos) {
        mContext = context;
        mTitles = context.getResources().getStringArray(R.array.home_list_type);
        this.mWorkRecBeanList = workRecBeanList;
        this.materialInfos=materialInfos;
        this.academyInfos=academyInfos;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==0){
            View inflate = inflater.inflate(R.layout.item_square_grid, null, true);
            return new MyViewHolder(inflate);
        }else if(viewType==1){
            View inflate = inflater.inflate(R.layout.item_square_grid, null, true);
            return new MyViewHolder2(inflate);
        }
        else if(viewType==2){
            View inflate = inflater.inflate(R.layout.item_square_grid, null, true);
            return new MyViewHolder3(inflate);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){

            ((MyViewHolder)holder).title.setText(mTitles[position]);
            ((MyViewHolder)holder).mBtnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击“更多”进入作品池
                    ActivityUtil.startWorkPoolActivity(mContext);
                }
            });
            ((MyViewHolder)holder).gridView.setAdapter(new HomeGridAdapter(mContext,mWorkRecBeanList));
        }
        if(holder instanceof MyViewHolder2){
            //剧本
            ((MyViewHolder2)holder).title.setText(mTitles[position]);
            ((MyViewHolder2)holder).mBtnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击“更多”进入剧本主页
                    Intent intent1=new Intent(mContext, MaterialMainActivity.class);
                    mContext.startActivity(intent1);
                }
            });
            ((MyViewHolder2)holder).gridView.setAdapter(new HomeMaterialAdapter(mContext,materialInfos));


        }if(holder instanceof MyViewHolder3){
            //学院
            ((MyViewHolder3)holder).title.setText(mTitles[position]);
            ((MyViewHolder3)holder).mBtnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击“更多”进入学院
                    Intent intent=new Intent(mContext,AcademyActivity.class);
                    mContext.startActivity(intent);
                }
            });
            ((MyViewHolder3)holder).gridView.setAdapter(new HomeAcademyAdapter(mContext,academyInfos));



        }

    }

    @Override
    public int getItemCount() {
        return mTitles.length;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return 0;
        }else if(position==1){
            return 1;
        }
        return position;

//        return super.getItemViewType(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        /**
         * 标题 如作品推荐 素材推荐
         */
        TextView title;
        /**
         * “更多”按钮
         */
        Button mBtnMore;
        /**
         * 子项
         */
        GridView gridView;


        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            mBtnMore = (Button) itemView.findViewById(R.id.btn_more);
            gridView = (GridView) itemView.findViewById(R.id.gv_recommend);
        }
    }
    class MyViewHolder2 extends RecyclerView.ViewHolder {
        /**
         * 标题 如作品推荐 素材推荐
         */
        TextView title;
        /**
         * “更多”按钮
         */
        Button mBtnMore;
        /**
         * 子项
         */
        GridView gridView;


        public MyViewHolder2(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            mBtnMore = (Button) itemView.findViewById(R.id.btn_more);
            gridView = (GridView) itemView.findViewById(R.id.gv_recommend);
        }
    }
    class MyViewHolder3 extends RecyclerView.ViewHolder {
        /**
         * 标题 如作品推荐 素材推荐
         */
        TextView title;
        /**
         * “更多”按钮
         */
        Button mBtnMore;
        /**
         * 子项
         */
        GridView gridView;


        public MyViewHolder3(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            mBtnMore = (Button) itemView.findViewById(R.id.btn_more);
            gridView = (GridView) itemView.findViewById(R.id.gv_recommend);
        }
    }

}