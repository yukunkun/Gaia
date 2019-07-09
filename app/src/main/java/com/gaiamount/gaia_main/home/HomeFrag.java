package com.gaiamount.gaia_main.home;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.apis.api_cms_home.CmsHomeApiHelper;
import com.gaiamount.apis.api_user.AccountApiHelper;
import com.gaiamount.apis.api_works.WorksApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.gaia_main.home.bean.Banner;
import com.gaiamount.gaia_main.home.bean.WorkInfo;
import com.gaiamount.module_academy.AcademyActivity;
import com.gaiamount.module_academy.bean.AcademyInfo;
import com.gaiamount.module_academy.bean.OnEventId;
import com.gaiamount.module_material.MaterialMainActivity;
import com.gaiamount.module_material.bean.MaterialInfo;
import com.gaiamount.module_scripe.ScripeActivity;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.widgets.recyc.RecyclerViewHeader2;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by luxiansheng on 16/3/6.
 * 主页fragment
 */
public class HomeFrag extends Fragment implements View.OnClickListener {

    public static final int AUTO_TURNING_TIME = 4000;
    private RecyclerView mWorkRec;
    private LinearLayout mLinearLayout;
    /**
     * 作品池模块图标
     */
    private ImageView mModule_work_pool;
    /**
     * 创作者模块图标
     */
    private ImageView mModuleCreator;
    /**
     * 学院图标
     */
    private ImageView mModule_Academy;
    /**
     * 素材图标
     */
    private ImageView mModule_Material;
    /**
     * 剧本图标
     */
    private ImageView mModule_Scripe;

    private RecyclerViewHeader2 mHeader;
    private ConvenientBanner mConvenientBanner;
    private String minimum;
    private String latest;
    List<Banner> banners = new ArrayList<>();
    List<String> bannerImages = new ArrayList<>();

    public static HomeFrag newInstance() {
        HomeFrag homeFrag = new HomeFrag();

        return homeFrag;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);

        initView(view);

        setUpView();
        
        getDataFromNet();//联网获取首页推荐数据

        return view;
    }





    @Override
    public void onPause() {
        super.onPause();
        mConvenientBanner.stopTurning();
    }

    @Override
    public void onResume() {
        super.onResume();
        mConvenientBanner.startTurning(AUTO_TURNING_TIME);
    }

    private void initView(View view) {
        mWorkRec = (RecyclerView) view.findViewById(R.id.work_rec);
        //添加头部
        mHeader = RecyclerViewHeader2.fromXml(getActivity(), R.layout.layout_main_header);
        mModule_work_pool = (ImageView) mHeader.findViewById(R.id.module_work_pool);
        mModuleCreator = (ImageView) mHeader.findViewById(R.id.module_creator);
        mConvenientBanner = (ConvenientBanner) mHeader.findViewById(R.id.convenientBanner);

        mLinearLayout = (LinearLayout) view.findViewById(R.id.home_fragment_linear);
        mModule_Academy = (ImageView) mHeader.findViewById(R.id.module_collage);
        mModule_Material= (ImageView) mHeader.findViewById(R.id.module_material);
        mModule_Scripe= (ImageView) mHeader.findViewById(R.id.module_script);
        //设置banner条的宽高
        ViewGroup.LayoutParams layoutParams = mConvenientBanner.getLayoutParams();
        layoutParams.height = getHeight();
        mConvenientBanner.setLayoutParams(layoutParams);
    }

    private void setUpView() {
        mModule_work_pool.setOnClickListener(this);
        mModuleCreator.setOnClickListener(this);
        mModule_Academy.setOnClickListener(this);
        mModule_Material.setOnClickListener(this);
        mModule_Scripe.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.module_work_pool://打开作品池
                ActivityUtil.startWorkPoolActivity(getActivity());
                break;
            case R.id.module_creator:
                ActivityUtil.startCreatorActivity(getActivity());
                break;
            case R.id.module_collage://学院
                Intent intent=new Intent(getContext(),AcademyActivity.class);
                startActivity(intent);
                break;
            case R.id.module_material://素材
                Intent intent1=new Intent(getContext(), MaterialMainActivity.class);
                startActivity(intent1);
                break;
            case R.id.module_script://剧本
                Intent intent2=new Intent(getContext(), ScripeActivity.class);
                startActivity(intent2);
                break;
        }
    }

    /**
     * 联网获取主页推荐数据
     */
    private void getDataFromNet() {
        int num = 17;
        final JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(HomeFrag.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
//                paresJson(response);


            }
        };
        WorksApiHelper.getRecommend(num, getActivity(), jsonHttpResponseHandler);

        //版本号的获取
        JsonHttpResponseHandler handler=new MJsonHttpResponseHandler(HomeFrag.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                JSONObject jsonObject = response.optJSONObject("o");
                minimum = jsonObject.optString("Minimum");
                latest = jsonObject.optString("Latest");
                try {
                    PackageInfo pi=getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                    int versionCode = pi.versionCode;
                    if(versionCode<Integer.valueOf(latest)){
                        showDialog(minimum ,latest,versionCode);//展示更新的Dialog
                    }

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        AccountApiHelper.getVersion(handler);

        //获取Banner条

        MJsonHttpResponseHandler handler1 =new MJsonHttpResponseHandler(HomeFrag.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                JSONArray a = response.optJSONArray("a");
                if(a==null){
                    return;
                }
                if(a.length()>=5){
                    for (int i = 0; i < 5; i++) {
                        JSONObject object = a.optJSONObject(i);
                        Banner banner=new Banner();
                        banner.setId(object.optLong("id"));
                        banner.setShuffl(object.optString("shuffl"));
                        bannerImages.add(object.optString("shuffl"));
                        banner.setType(object.optInt("type"));
                        banner.setUrl(object.optString("url"));
                        banners.add(banner);
                    }
                }
                if(tag){
                    tag=false;
                    setBanner(bannerImages);
                }
            }
        };
        CmsHomeApiHelper.homeBanner(handler1);
        //获取推荐列表
        MJsonHttpResponseHandler  handler2=new MJsonHttpResponseHandler(HomeFrag.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                paresJson(response);

                mLinearLayout.setVisibility(View.GONE); //提示加载的progressbar隐藏
            }
        };
        CmsHomeApiHelper.homeRecomend(handler2);
    }
    boolean tag=true;
    /**
     * 解析json并设置适配器
     *
     * @param response
     */
    List<WorkInfo> workRecBeanList=new ArrayList<>();
    List<MaterialInfo> materialInfos=new ArrayList<>();
    List<AcademyInfo> academyInfos=new ArrayList<>();

    private void paresJson(JSONObject response) {
        JSONObject o = response.optJSONObject("o");
        JSONArray works = o.optJSONArray("works");
        JSONArray materials = o.optJSONArray("materials");
        JSONArray college = o.optJSONArray("college");

        for (int i = 0; i < works.length(); i++) {
            WorkInfo workRecBean=new WorkInfo();
            JSONObject object = works.optJSONObject(i);
            workRecBean.setFlag(object.optInt("flag"));
            workRecBean.setType(object.optInt("type"));
            workRecBean.setName(object.optString("name"));
            workRecBean.setCover(object.optString("cover"));
            workRecBean.setScreenshot(object.optString("screenshot"));
            workRecBean.setWid(object.optLong("wid"));
            workRecBeanList.add(workRecBean);
        }

        for (int i = 0; i < materials.length(); i++) {
            MaterialInfo info=new MaterialInfo();
            JSONObject object = materials.optJSONObject(i);
            info.setCover(object.optString("cover"));
            info.setName(object.optString("name"));
            info.setTypes(object.optInt("type"));
            info.setScreenshot(object.optString("screenshot"));
            info.setId(object.optLong("wid"));
            materialInfos.add(info);

        }
        for (int i = 0; i < college.length(); i++) {
            AcademyInfo academyInfo=new AcademyInfo();
            JSONObject object = college.optJSONObject(i);
            academyInfo.setType(object.optInt("type"));
            academyInfo.setName(object.optString("name"));
            academyInfo.setCover(object.optString("cover"));
            academyInfo.setCourseId(object.optLong("wid"));
            academyInfos.add(academyInfo);
        }

        //传入适配器并将适配器设置进列表
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        mWorkRec.setLayoutManager(layout);
        mWorkRec.setAdapter(new HomeRecyclerAdapter(getActivity(), workRecBeanList,materialInfos,academyInfos));

        mHeader.attachTo(mWorkRec);

    }

    public void setBanner(List<String> bannerImages){
        //加载广告条
        mConvenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, bannerImages)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
        mConvenientBanner.startTurning(AUTO_TURNING_TIME);
        mConvenientBanner.setCanLoop(true);
        mConvenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //进入播放页
                Banner banner = banners.get(position);
                String url = banner.getUrl();
                int split = url.lastIndexOf("/");
                String id=url.substring(split+1,url.length());
                //判断是否为数字
                Pattern pattern = Pattern.compile("[0-9]*");
                Matcher isNum = pattern.matcher(id);

                if(isNum.matches()){ //是数字才跳转
                    //type : 0 作品 1 素材 2 剧本
                    if(banner.getType()==0){
                        ActivityUtil.startPlayerActivity(getActivity(), Integer.valueOf(id),Configs.TYPE_WORK_POOL,mConvenientBanner);
                    }
                    else if(banner.getType()==1){
                        ActivityUtil.startMaterialPlayActivity(getActivity(),Integer.valueOf(id),1); // 1视频素材
                    }
                    else if(banner.getType()==2){
                        ActivityUtil.startScripeDetailActivity(getContext(),Integer.valueOf(id));
                    }
                }else {
                    GaiaApp.showToast("资源有误");
                }
            }
        });
    }


    public class NetworkImageHolderView implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, String data) {
            Glide.with(HomeFrag.this).load(Configs.COVER_PREFIX+data).placeholder(R.mipmap.bg_general).into(imageView);
        }
    }

    //强制更新
    private void showDialog(String minimum, String latest, int versionCode){

        NewDialog newDialog=NewDialog.getInstance(minimum,latest,versionCode);
        newDialog.show(getActivity().getSupportFragmentManager(), "HomeDialog");
    }

    //下载App完成后的回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnEventId event) {
       if(event.id==-1){
           installApp();
       }
    }

    //安装APP
    private void installApp(){
        String fileName = Environment.getExternalStorageDirectory() + "/gaiamount"+"Gaiamount.apk" ;
        File file=new File(fileName);
        if(file!=null){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            startActivity(intent);
        }else {
            GaiaApp.showToast("安装失败,请自行下载安装");
        }
    }

    public int getHeight(){//设置宽高比例
        int itemHeight;
        int width = ScreenUtils.instance().getWidth();
        itemHeight = (int) ((width)*0.33);
        return itemHeight;
    }



}
