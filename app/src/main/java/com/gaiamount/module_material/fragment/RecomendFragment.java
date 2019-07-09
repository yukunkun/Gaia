package com.gaiamount.module_material.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gaiamount.R;
import com.gaiamount.apis.api_material.MaterialApiHelper;
import com.gaiamount.module_material.fragment.adapter.MaterialListviewAdapter;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yukun on 16-9-29.
 */
public class RecomendFragment extends Fragment {

    private long mid;
    private ListView mListview;
    private List<RecomendInfo> listRec=new ArrayList();
    private MaterialListviewAdapter adapter;

    public static RecomendFragment newInstance(long mid){
        RecomendFragment recomendFragment=new RecomendFragment();
        Bundle bundle=new Bundle();
        bundle.putLong("mid",mid);
        recomendFragment.setArguments(bundle);
        return recomendFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mid = getArguments().getLong("mid");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.material_recomend, null);
        init(inflate);
        getInfo();
        setAdapter();
        setListener();
        return inflate;
    }

    private void setListener() {
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActivityUtil.startMaterialPlayActivity(getContext(),listRec.get(position).getMid(),0);
            }
        });
    }

    private void setAdapter() {
        adapter = new MaterialListviewAdapter(listRec,getContext());
        mListview.setAdapter(adapter);
    }

    private void getInfo() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(RecomendFragment.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                parasJson(response);

            }
        };
        MaterialApiHelper.getMaterialRec(mid,1,getContext(),handler);
    }

    private void parasJson(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        for (int i = 0; i < a.length(); i++) {
            JSONObject jsonObject = a.optJSONObject(i);
            RecomendInfo recomendInfo=new RecomendInfo();
            recomendInfo.setName(jsonObject.optString("name"));
            recomendInfo.setCover(jsonObject.optString("cover"));
            recomendInfo.setNickName(jsonObject.optString("nickName"));
            JSONObject jsonObject1 = jsonObject.optJSONObject("createTime");
            recomendInfo.setDuration(jsonObject1.optInt("time"));
            recomendInfo.setScreenshot(jsonObject.optString("screenshot"));
            recomendInfo.setMid(jsonObject.optInt("mid"));
            listRec.add(recomendInfo);
        }
        adapter.notifyDataSetChanged();
    }

    private void init(View inflate) {
        mListview = (ListView) inflate.findViewById(R.id.material_listview);

    }
    public class RecomendInfo{

        /**
         * name : IMG_8694.mov
         * cover : o
         * mid : 22
         * nickName : keke
         * duration : 123
         */

        private String name;
        private String cover;
        private int mid;
        private String nickName;
        private long duration;
        private String screenshot;

        public String getScreenshot() {
            return screenshot;
        }

        public void setScreenshot(String screenshot) {
            this.screenshot = screenshot;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }
    }
}
