package com.gaiamount.module_im.secret_chat.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_im.ImApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_im.secret_chat.activity.ContactChatActivity;
import com.gaiamount.module_im.secret_chat.adapter.SortAdapter;
import com.gaiamount.module_im.secret_chat.bean.ContactInfo;
import com.gaiamount.module_im.secret_chat.model.PinYinKit;
import com.gaiamount.module_im.secret_chat.model.PinyinComparator;
import com.gaiamount.module_im.secret_chat.model.SortModel;


import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.widgets.custom.MSideBar;


import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yukun on 16-7-15.
 */
public class ContentPerson extends Fragment {
    public PinyinComparator comparator = new PinyinComparator();
    private MSideBar sideBar;
    private ListView sortListView;
    private TextView dialogTxt;
    private SortAdapter adapter;
    private long uid;
    private RelativeLayout layout;
    private List<SortModel> sortModelList;
    private List<ContactInfo> infos=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.communciation_fragment,null);
        init(view);
        getInfos();
        setListener();
        setAdapter();

        return view;
    }

    private void setAdapter() {
        // call filledData to get datas
//        try {
//            sortModelList = filledData(getResources().getStringArray(R.array.country));
//
//        } catch (BadHanyuPinyinOutputFormatCombination e1) {
//            e1.printStackTrace();
//        }


        // sort by a-z
//        Collections.sort(sortModelList, comparator);
//        Collections.sort(infos, comparator);

//       adapter = new SortAdapter(getContext(), sortModelList);
        adapter = new SortAdapter(getContext(), infos);
        sortListView.setAdapter(adapter);
    }


    private void init(View view) {
        sideBar = (MSideBar) view.findViewById(R.id.sild_bar);
        dialogTxt = (TextView) view.findViewById(R.id.txt_dialog);
        sideBar.setmTextDialog(dialogTxt);
        sortListView = (ListView) view.findViewById(R.id.list_view_user_list);
        sortListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        layout= (RelativeLayout) view.findViewById(R.id.contact_tishi);
    }

    private void getInfos() {
        uid=GaiaApp.getUserInfo().id;
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(ContentPerson.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                JSONArray a = response.optJSONArray("a");
                paraJson(a);
            }

        };
        ImApiHelper.getContacts(uid,getActivity(),handler);
    }

    //解析数据
    private void paraJson(JSONArray responseArray) {
        for (int i = 0; i < responseArray.length(); i++) {
            if (responseArray.length()!=0){
                layout.setVisibility(View.GONE);
                JSONObject response = responseArray.optJSONObject(i);
                ContactInfo contactInfo;
                contactInfo=filledDatas(response.optString("nickName"));//获取判断的条件
                contactInfo.setUid(response.optString("uid"));
                contactInfo.setAvatar(response.optString("avatar"));
                contactInfo.setId(response.optInt("id"));
                infos.add(contactInfo);
            }
            Collections.sort(infos, comparator);//进行排序
            adapter.notifyDataSetChanged();
        }
    }

    private void setListener() {
        sideBar.setOnTouchingLetterChangedListener(new MSideBar.OnTouchingLetterChangedListener() {

            public void onTouchingLetterChanged(String str) {
                int position = adapter.getPositionForSection(str.charAt(0));
                if (position != -1)
                    sortListView.setSelection(position);
            }
        });
        //item点击事件
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getContext(), ContactChatActivity.class);
                intent.putExtra("otherId",((ContactInfo) adapter.getItem(position)).getUid());
                intent.putExtra("otherAvatar",((ContactInfo) adapter.getItem(position)).getAvatar());
                intent.putExtra("otherNickName",((ContactInfo) adapter.getItem(position)).getNickName());
                getContext().startActivity(intent);
            }
        });
    }

   /* private List<SortModel> filledData(String [] date) throws BadHanyuPinyinOutputFormatCombination{
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for(int i=0; i<date.length; i++){
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            //汉字转换成拼音
            String pinyin = PinYinKit.getPingYin(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetters(sortString.toUpperCase());
            }else{
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }*/

    private ContactInfo filledDatas(String  date) {

        ContactInfo info = new ContactInfo();
            info.setNickName(date);
            //汉字转换成拼音
        String pinyin = null;
        try {
            pinyin = PinYinKit.getPingYin(date);

        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }
        String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")){
                info.setSortLetters(sortString.toUpperCase());
            }else{
                info.setSortLetters("#");
            }


        return info;

    }
    public void changeDatas(List<ContactInfo> mSortList , String str) {

        Collections.sort(mSortList, comparator);
        adapter.updateListView(mSortList);
    }
}
