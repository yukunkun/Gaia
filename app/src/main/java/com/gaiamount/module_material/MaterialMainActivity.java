package com.gaiamount.module_material;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.gaiamount.R;
import com.gaiamount.apis.api_material.MaterialApiHelper;
import com.gaiamount.module_material.adapters.MaterialAdapter;
import com.gaiamount.module_material.bean.MaterialInfo;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaterialMainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<Banner> bannerList=new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MaterialAdapter materialAdapter;
    private ArrayList<String> split=new ArrayList();
    private List<Long> idList= new ArrayList<>();
    private List<MaterialInfo> materialInfos=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_main);
        init();
        getInfo();
        setAdapter();

    }

    private void init() {
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void getInfo() {

        //获取image
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(MaterialMainActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                pareJson(response);
            }
        };

        MaterialApiHelper.materialBanner(getApplicationContext(),handler);

        //获取banner
        MJsonHttpResponseHandler handler1=new MJsonHttpResponseHandler(MaterialMainActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                paraJsonBanner(response);
            }
        };
        MaterialApiHelper.getMaterialBanner(getApplicationContext(),handler1);

        //获取视屏推荐
        MJsonHttpResponseHandler handler2=new MJsonHttpResponseHandler(MaterialMainActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                parasJsonVideo(response);

//                //获取模板推荐
//                MJsonHttpResponseHandler handler3=new MJsonHttpResponseHandler(MaterialMainActivity.class){
//                    @Override
//                    public void onGoodResponse(JSONObject response) {
//                        super.onGoodResponse(response);
//                        parasJsonModulVideo(response);
//                    }
//                };
//                try {
//                    MaterialApiHelper.getMaterialList(0,3,5,1,6,2,getApplicationContext(),handler3);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }
        };

        try {
            MaterialApiHelper.getMaterialList(0,3,5,1,6,1,getApplicationContext(),handler2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void pareJson(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        for (int i = 0; i < a.length(); i++) {
            JSONObject jsonObject = a.optJSONObject(i);
            Banner banner=new Banner();
            banner.setName(jsonObject.optString("name"));
            banner.setImage(jsonObject.optString("image"));
            banner.setId(jsonObject.optLong("id"));
            bannerList.add(banner);
        }
        materialAdapter.notifyDataSetChanged();
    }

    private void paraJsonBanner(JSONObject response) {
        JSONArray jsonObject = response.optJSONArray("a");
        for (int i = 0; i < 5; i++) {
            JSONObject jsonObject1 = jsonObject.optJSONObject(i);
            split.add(jsonObject1.optString("image"));
            String s = jsonObject1.optString("url");
            int i1 = s.lastIndexOf("/");
            String s1=s.substring(i1+1,s.length());

            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(s1);

            if(isNum.matches()){ //是数字加入

                idList.add(Long.valueOf(s1));
            }

        }
        materialAdapter.update(idList);
    }

    private void parasJsonVideo(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        for (int i = 0; i < a.length(); i++) {
            JSONObject jsonObject = a.optJSONObject(i);
            MaterialInfo info=new MaterialInfo();
            info.setNickName(jsonObject.optString("nickName"));
            info.setHave1080(jsonObject.optInt("have1080"));
            info.setFormat(jsonObject.optString("format"));
            info.setLikeCount(jsonObject.optInt("likeCount"));
            info.setScreenshot(jsonObject.optString("screenshot"));
            info.setAvatar(jsonObject.optString("avatar"));
            info.setType(jsonObject.optString("type"));
            info.setUserId(jsonObject.optLong("userId"));
            info.setIsVip(jsonObject.optInt("isVip"));
            info.setIsOfficial(jsonObject.optInt("isOfficial"));
            info.setCommentCount(jsonObject.optInt("commentCount"));
            info.setCover(jsonObject.optString("cover"));
            info.setIs4K(jsonObject.optInt("is4K"));
            info.setVipLevel(jsonObject.optInt("vipLevel"));
            info.setPlayCount(jsonObject.optInt("playCount"));
            info.setHave720(jsonObject.optInt("have720"));
            info.setGrade(jsonObject.optDouble("grade"));
            info.setName(jsonObject.optString("name"));
            info.setId(jsonObject.optLong("id"));
            info.setInputKey(jsonObject.optString("inputKey"));
            info.setAllowCharge(jsonObject.optInt("allowCharge")); //0 免费  1 付费
            materialInfos.add(info);
        }
        materialAdapter.notifyDataSetChanged();
    }


    private void parasJsonModulVideo(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        for (int i = 0; i < a.length(); i++) {
            JSONObject jsonObject = a.optJSONObject(i);
            MaterialInfo info=new MaterialInfo();
            info.setNickName(jsonObject.optString("nickName"));
            info.setHave1080(jsonObject.optInt("have1080"));
            info.setFormat(jsonObject.optString("format"));
            info.setFlag(jsonObject.optInt("flag"));
            info.setLikeCount(jsonObject.optInt("likeCount"));
            info.setScreenshot(jsonObject.optString("screenshot"));
            info.setAvatar(jsonObject.optString("avatar"));
            info.setType(jsonObject.optString("type"));
            info.setUserId(jsonObject.optLong("userId"));
            info.setIsVip(jsonObject.optInt("isVip"));
            info.setIsOfficial(jsonObject.optInt("isOfficial"));
            info.setCommentCount(jsonObject.optInt("commentCount"));
            info.setCover(jsonObject.optString("cover"));
            info.setIs4K(jsonObject.optInt("is4K"));
            info.setVipLevel(jsonObject.optInt("vipLevel"));
            info.setPlayCount(jsonObject.optInt("playCount"));
            info.setHave720(jsonObject.optInt("have720"));
            info.setGrade(jsonObject.optDouble("grade"));
            info.setName(jsonObject.optString("name"));
            info.setId(jsonObject.optLong("id"));
            info.setInputKey(jsonObject.optString("inputKey"));
            info.setAllowCharge(jsonObject.optInt("allowCharge")); //0 免费  1 付费
            materialInfos.add(info);
        }
        materialAdapter.notifyDataSetChanged();

    }


    private void setAdapter() {
        materialAdapter = new MaterialAdapter(materialInfos,split,bannerList,getApplicationContext());
        mRecyclerView.setAdapter(materialAdapter);
    }
    public void MaterialBacks(View view) {
        finish();
    }

    public void GolobSearch(View view) {
        ActivityUtil.startGlobalSearchActivity(this,"",0);
    }

    public class Banner{

        String name;
        long id;
        String image;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
