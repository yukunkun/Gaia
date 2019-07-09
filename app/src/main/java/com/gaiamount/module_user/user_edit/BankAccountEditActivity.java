package com.gaiamount.module_user.user_edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gaiamount.R;

public class BankAccountEditActivity extends AppCompatActivity {

    public static final int TYPE_CHOOSE_PAY_WAY = 1;
    public static final int TYPE_CHOOSE_BANK = 2;
    public static final int REQUEST_CODE = 1123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account_edit);
        ListView accountEditList = (ListView) findViewById(R.id.account_edit_list);

        int type = getIntent().getIntExtra("type", -1);
        if (type==TYPE_CHOOSE_PAY_WAY) {
            final String[] stringArray = getResources().getStringArray(R.array.pay_way);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(BankAccountEditActivity.this,android.R.layout.simple_list_item_1, stringArray);
            accountEditList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.putExtra("pay_way",stringArray[position]);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });
            accountEditList.setAdapter(adapter);
        }else if (type==TYPE_CHOOSE_BANK) {

            final String[] stringArray = getResources().getStringArray(R.array.banks);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(BankAccountEditActivity.this,android.R.layout.simple_list_item_1, stringArray);
            accountEditList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.putExtra("bank",stringArray[position]);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });
            accountEditList.setAdapter(adapter);
        }

    }
}
