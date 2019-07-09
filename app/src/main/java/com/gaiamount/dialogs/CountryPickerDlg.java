package com.gaiamount.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gaiamount.apis.Configs;
import com.gaiamount.module_user.bean.CharacterParser;
import com.gaiamount.module_user.bean.CountryCode;
import com.gaiamount.R;
import com.gaiamount.module_user.adapters.CountryCodeAdapter;
import com.gaiamount.util.PinyinComparator;
import com.gaiamount.widgets.custom.SideBar;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by xiaoyu-liu on 16-2-15.
 */
public class CountryPickerDlg extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_picker_dlg);
        initViews();
    }

    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private CountryCodeAdapter adapter;

    private CharacterParser characterParser;
    private ArrayList<CountryCode> SourceDateList;

    private PinyinComparator pinyinComparator;

    private void initViews() {
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if(position != -1){
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent data = new Intent();
                data.putExtra(Configs.PICK_COUNTRY_CODE_KEY, ((CountryCode) adapter.getItem(position)).GetCode());
                data.putExtra("nation_name", ((CountryCode) adapter.getItem(position)).GetName());
                setResult(RESULT_OK, data);
                finish();
            }
        });

        SourceDateList = filledData(getResources().getStringArray(R.array.country),
                getResources().getStringArray(R.array.country_code));

        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new CountryCodeAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);
    }

    /**
     * init country code data
     *
     * @param date country list
     * @param code code list
     * @return country code list
     */
    private ArrayList<CountryCode> filledData(String [] date, String[] code){
        ArrayList<CountryCode> mSortList = new ArrayList<CountryCode>();

        for(int i=0; i<date.length; i++){
            CountryCode sortModel = new CountryCode(date[i], code[i]);

            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            if(sortString.matches("[A-Z]")){
                sortModel.setSortKey(sortString.toUpperCase());
            }else{
                sortModel.setSortKey("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }
}
