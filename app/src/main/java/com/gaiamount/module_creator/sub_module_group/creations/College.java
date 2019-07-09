package com.gaiamount.module_creator.sub_module_group.creations;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.bean.MixInfo;
import com.gaiamount.module_academy.bean.MixLightDecoration;
import com.gaiamount.module_academy.bean.OnEventLearn;
import com.gaiamount.module_creator.beans.Info;
import com.gaiamount.module_creator.sub_module_group.adapters.CollegeAdapter;
import com.gaiamount.module_creator.sub_module_group.fragment.AlbunCollegeDialog;
import com.gaiamount.module_creator.sub_module_group.fragment.AlbunCreateDialog;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yukun on 16-8-24.
 */
public class College extends Fragment {
    private long mUid;
    private long gid;
    private long uid;
    private int t=1;
    private int s=0;
    private int pi=1;
    private int opr=0;
    private ArrayList<MixInfo> mLessonInfoList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private LinearLayout layout;
    private CollegeAdapter adapter;
    private GridLayoutManager layoutManager;
    private Info mInfo;

    public static College newInstance(Info mInfo) {
        Bundle args = new Bundle();
        args.putSerializable("mInfo",mInfo);
        College fragment = new College();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Bundle arguments = getArguments();
        mInfo= (Info) arguments.getSerializable("mInfo");
        uid= GaiaApp.getAppInstance().getUserInfo().id;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnEventLearn event) {
        int position = event.id;
        if(mInfo.groupPower.allowManagerSpecial==1){
            AlbunCollegeDialog albunCreateDialog=AlbunCollegeDialog.newInstance(mInfo.gid,mLessonInfoList.get(position),position);
            albunCreateDialog.show(getChildFragmentManager(),"Product");
        }else {
            GaiaApp.showToast("对不起,你没有权限");
        }

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.group_academy_fragment, null);
        init(inflate);
        getInfo();
        setadapter();
        setListener();
        return inflate;
    }

    private void init(View view) {
//        layout = (LinearLayout) view.findViewById(R.id.work_fragment_linear);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.group_academy_recycler);
        layoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(layoutManager);

    }
    private void getInfo() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(College.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                paraJson(response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
//                layout.setVisibility(View.GONE);
            }
        };
        GroupApiHelper.getMyGroupCOLLEGE(uid,mInfo.gid,pi,getContext(),handler);
    }

    private void paraJson(JSONObject response) {
        JSONArray a = response.optJSONArray("a");
        for (int i = 0; i < a.length(); i++) {
            MixInfo mixInfo=new MixInfo();
            JSONObject object = a.optJSONObject(i);
            mixInfo.setLearningCount(object.optInt("learningCount"));
            mixInfo.setAuthor(object.optString("auther"));
            mixInfo.setAvatar(object.optString("avatar"));
            mixInfo.setCover(object.optString("cover"));
            mixInfo.setAllowFree(object.optInt("allowFree"));
            mixInfo.setPrice(object.optLong("price"));
            mixInfo.setId(object.optInt("id"));
            mixInfo.setType(object.optInt("type"));//1,0,2,图文,视频,直播
            mixInfo.setName(object.optString("name"));
            mixInfo.setChaptCount(object.optInt("chapterCount"));
            mixInfo.setNickName(object.optString("nickName"));
            mixInfo.setPlayCount(object.optInt("playCount"));
            mixInfo.setBrowseCount(object.optInt("browseCount"));
            mixInfo.setGrade(object.optInt("grade"));
            mixInfo.setHourCount(object.optInt("hourCount"));
            mixInfo.setGcid(object.optInt("gcid"));
            mixInfo.setProprity(object.optInt("proprity"));
            JSONObject jsonObject = object.optJSONObject("createTime");
            Long time = jsonObject.optLong("time");
            mixInfo.setTime(time);
            mLessonInfoList.add(mixInfo);
        }
        adapter.notifyDataSetChanged();
    }
    private void setadapter() {
        adapter = new CollegeAdapter(getContext(),mLessonInfoList,mInfo);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new MixLightDecoration(10,10,24,24));
    }

    private void setListener() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisibleItemPosition == layoutManager.getItemCount() - 1) {
                    //加载更多
                    pi++;
                    getInfo();
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }
}
