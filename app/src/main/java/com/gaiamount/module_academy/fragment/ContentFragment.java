package com.gaiamount.module_academy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.gaiamount.R;
import com.gaiamount.apis.api_academy.AcademyApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.activity.AcademyPlayActivity;
import com.gaiamount.module_academy.adapter.ExpandableAdapter;
import com.gaiamount.module_academy.bean.Contents;
import com.gaiamount.module_academy.bean.EventDetailInfo;
import com.gaiamount.module_academy.bean.EventExpanbableInfo;
import com.gaiamount.module_academy.bean.EventSendContent;
import com.gaiamount.module_academy.bean.LessonInfo;
import com.gaiamount.module_academy.bean.OnEventLearn;
import com.gaiamount.module_im.secret_chat.bean.ContentInfo;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by yukun on 16-8-3.
 */
public class ContentFragment extends Fragment {

    private ExpandableListView expandableListView;
    private ExpandableAdapter expandableAdapter;
    private long id;
    private long cid;
    private int t;
    private int isLearning;
    private ArrayList<Contents> contentList=new ArrayList<>();
    private ArrayList<ArrayList<LessonInfo>> lessonList=new ArrayList<>();
    private int type;
    private String responseString;
    private int allowFree;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }
    //页面的回调,传值id
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventDetailInfo event) {
        id = event.id;
        cid = event.cid;
        t=event.t;
        isLearning=event.isLearning;
        type = event.type;
        allowFree=event.allowFree;
        contentList.clear();
        lessonList.clear();
        getInfo(id,t);

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnEventLearn event) {
        if(event.id==1){
            isLearning=1;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.contant_fragment, null);
        init(inflate);
        setAdapter();
        setListener();
        return inflate;
    }

    private void init(View inflate) {
        expandableListView = (ExpandableListView) inflate.findViewById(R.id.contant_expandableliustview);

    }

    private void getInfo(long id, int t) {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(ContentFragment.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                paraJson(response);
            }
        };
        AcademyApiHelper.getContent(id, t,getContext(),handler);
    }

    private void paraJson(JSONObject response) {
        responseString=response.toString();
        JSONArray a = response.optJSONArray("a");
        for (int i = 0; i < a.length(); i++) {
            Contents info=new Contents();
            //章节的数据
            JSONObject object = a.optJSONObject(i);
            JSONObject chapter = object.optJSONObject("chapter");
            info.setName(chapter.optString("name"));
            info.setInd(chapter.optInt("ind"));
            info.setChapterId(chapter.optLong("id"));
            contentList.add(info);
            //节数的数据
            JSONArray hour = object.optJSONArray("hour");
            ArrayList<LessonInfo> lessonInfos=new ArrayList<>();
            for (int j = 0; j <hour.length() ; j++) {
                LessonInfo lessonInfo=new LessonInfo();
                JSONObject jsonObject = hour.optJSONObject(j);
                lessonInfo.setName(jsonObject.optString("name"));
                lessonInfo.setId(jsonObject.optInt("id"));
                lessonInfo.setScreenshot(jsonObject.optString("screenshot"));
                lessonInfo.setLen(jsonObject.optInt("len"));
                lessonInfo.setWatchLen(jsonObject.optInt("watchLen"));
                lessonInfo.setIsPublic(jsonObject.optInt("isPublic"));
                lessonInfos.add(lessonInfo);
            }
            lessonList.add(lessonInfos);
        }
        setOpen();
        EventBus.getDefault().post(new EventSendContent(contentList,lessonList,responseString));
        expandableAdapter.updateAdapter(isLearning,allowFree,contentList,lessonList,type);
        //expandableAdapter.notifyDataSetChanged();
    }

    private void setAdapter() {
        expandableAdapter = new ExpandableAdapter(getContext(),isLearning,allowFree,contentList,lessonList);
        expandableListView.setAdapter(expandableAdapter);
    }

    private void setListener() {

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                ArrayList<Integer> longHid=new ArrayList<Integer>();
                ArrayList<String> lessons=new ArrayList<String>();
                ArrayList<Integer> isPublicList=new ArrayList<Integer>();
                for (int i = 0; i <lessonList.get(groupPosition).size() ; i++) {
                    int hid = lessonList.get(groupPosition).get(i).getId();
                    longHid.add(hid);
                    String name = lessonList.get(groupPosition).get(i).getName();
                    lessons.add(name);
                    int isPublic = lessonList.get(groupPosition).get(i).getIsPublic();
                    isPublicList.add(isPublic);
                }

                if(type==0){ //是否开启视频播放页
                    if(isLearning==0&&allowFree==1){//判断是否是第一次  allowFree =0表示付费
                        GaiaApp.showToast(getContext().getString(R.string.choose_study));
                    }else {
                    ActivityUtil.startAcademyPlayActivity(isLearning,allowFree,cid,childPosition,contentList.get(groupPosition).getChapterId(),
                            longHid,lessons,isPublicList,getContext());
                    }
                }else if(type==1){//开启图文详情页
                    if(isLearning==0&&allowFree==1){//判断是否是第一次
                        GaiaApp.showToast(getContext().getString(R.string.choose_study));
                    }else {
                        if(isLearning==0&&allowFree==0&&lessonList.get(groupPosition).get(childPosition).getIsPublic()==0){//判断没权限,必须购买
                            GaiaApp.showToast(getContext().getString(R.string.please_buy));
                        }else {
                            ActivityUtil.startTuWenDetailActivity(cid,childPosition,groupPosition,responseString,getContext());
                        }
                    }
                }

                return false;
            }
        });
    }

    //默认打开
    public void setOpen(){

        for(int i = 0; i < expandableAdapter.getGroupCount(); i++){
            expandableListView.expandGroup(i);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
