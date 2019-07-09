package com.gaiamount.module_player.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.gaiamount.R;
import com.gaiamount.apis.api_works.WorksApiHelper;
import com.gaiamount.module_player.adapters.PlayerRecAdapter;
import com.gaiamount.module_player.bean.PlayerRec;
import com.gaiamount.module_player.bean.VideoDetailInfo;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.network.GsonUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.widgets.improved.BounceListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by haiyang-lu on 16-4-5.
 * 播放器页面的视频相关推荐
 */
public class PlayerRecFrag extends Fragment {

    private BounceListView mPlayerRecList;
    private VideoDetailInfo mVideoDetailInfo;
    private String mContentId;
    private String mContentType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mContentId = arguments.getString("contentId");
        mContentType = arguments.getString("contentType");
        mVideoDetailInfo = (VideoDetailInfo) arguments.getSerializable("video_detail_info");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_rec, container,false);
        mPlayerRecList = (BounceListView) view.findViewById(R.id.player_rec_list);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getRecWorks(mVideoDetailInfo.getWorks().getType());
    }

    private void getRecWorks(String t) {
        getRecWorks(t,mVideoDetailInfo.getWorks().getKeywords(),1,20);
    }

    private void getRecWorks(String t,String key,int pi,int ps) {
        JsonHttpResponseHandler recResponseHandler = new MJsonHttpResponseHandler(PlayerRecFrag.class){

            @Override
            public void parseJson(JSONObject response) {
                super.parseJson(response);
                Gson gson = GsonUtil.getInstannce().getGson();
                final List<PlayerRec> playerRecList = gson.fromJson(response.optJSONArray("a").toString(),new TypeToken<List<PlayerRec>>(){}.getType());
                //设置适配器
                mPlayerRecList.setAdapter(new PlayerRecAdapter(getActivity(),playerRecList));
                //设置list的点击事件
                mPlayerRecList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        PlayerRec playerRec = playerRecList.get(position);
                        ActivityUtil.startPlayerActivity(getActivity(), playerRec.getId(),0);
                    }
                });
            }
        };

        WorksApiHelper.getRecWorks(t,key,pi,ps,getActivity(),recResponseHandler);
    }
}
