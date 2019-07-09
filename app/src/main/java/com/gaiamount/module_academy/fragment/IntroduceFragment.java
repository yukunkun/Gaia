package com.gaiamount.module_academy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.module_academy.adapter.IntroduceAdapter;
import com.gaiamount.module_academy.bean.EventDetailInfo;
import com.gaiamount.widgets.improved.CollapsibleTextView;
import com.gaiamount.widgets.improved.NOScrolledListView;
import com.gaiamount.widgets.improved.ReadMoreTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by yukun on 16-8-3.
 */
public class IntroduceFragment extends Fragment {

    private NOScrolledListView mListView;
    private TextView textViewIntro;
    private IntroduceAdapter adapter;
    private String intr;
    private String sofeware;
    private ReadMoreTextView textView;
    private String author;
    private String[] strings;
    private ArrayList<String> stringNew=new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }
    //从detail activity传过来的值
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventDetailInfo event) {
        intr = event.intr;
        sofeware = event.sofeware;
        author=event.author;
        if(sofeware.equals("null")){
            textViewIntro.setText("");
        }else {
            textViewIntro.setText(sofeware);
        }if(intr.equals("null")){
            textView.setText("");
        }else {
                textView.setText(intr);
        }
        stringNew.clear();
       // adapter.notifyDataSetChanged();
        strings=author.split(",");
        setAdapter();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View inflate =inflater.inflate(R.layout.academy_introduce_fragment,null);
        init(inflate);
//        getInfo();
        return inflate;
    }

    private void init(View inflate) {
        mListView = (NOScrolledListView) inflate.findViewById(R.id.academy_introduce_listview);
        View inflate1 = LayoutInflater.from(getContext()).inflate(R.layout.academy_introduce_head,null);
        mListView.addHeaderView(inflate1);
        textViewIntro= (TextView) inflate1.findViewById(R.id.textview_software_use);
        textView= (ReadMoreTextView) inflate1.findViewById(R.id.staff);
    }

    private void getInfo() {

    }

    private void setAdapter() {

        stringNew.add("Mixing Light");
        for (int i = 0; i < strings.length; i++) {
            if(strings[i].equals("Patrick Inhofer")||strings[i].equals("Robbie Carman")||strings[i].equals("Dan Moran")){
                stringNew.add(strings[i]);
            }
        }
        adapter = new IntroduceAdapter(getContext(),stringNew);
        mListView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(getContext());
    }
}
