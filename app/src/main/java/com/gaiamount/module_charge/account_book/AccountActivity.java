package com.gaiamount.module_charge.account_book;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_user.AccountApiHelper;
import com.gaiamount.gaia_main.BaseActionBarActivity;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_user.user_edit.BankAccountEditActivity;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.util.UserUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

public class AccountActivity extends BaseActionBarActivity {

    private ListView mAccountList;
    String[] values = new String[]{"","银行卡","","","","",""};
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        getBankAccount();


        mAccountList = (ListView) findViewById(R.id.account_list);
        mAdapter = new MyAdapter();
        mAccountList.setAdapter(mAdapter);

        mAccountList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AccountActivity.this,BankAccountEditActivity.class);
                if (position==1) {
                    intent.putExtra("type",BankAccountEditActivity.TYPE_CHOOSE_PAY_WAY);
                    startActivityForResult(intent,BankAccountEditActivity.REQUEST_CODE);
                }else if (position==3) {
                    intent.putExtra("type",BankAccountEditActivity.TYPE_CHOOSE_BANK);
                    startActivityForResult(intent,BankAccountEditActivity.REQUEST_CODE);
                }else if (position==4) {//编辑银行卡号
                    final AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
                    final EditText editText = new EditText(AccountActivity.this);
                    builder.setTitle("设置银行账户")
                            .setView(editText)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    values[4] = editText.getText().toString().trim();
                                    mAdapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //do nothing
                                }
                            })
                            .create();
                    builder.show();
                }else if (position==5) {//收款人姓名
                    final AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
                    final EditText editText = new EditText(AccountActivity.this);
                    builder.setTitle("编辑收款人")
                            .setView(editText)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    values[5] = editText.getText().toString().trim();
                                    mAdapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //do nothing
                                }
                            })
                            .create();
                    builder.show();
                }else if (position==6) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
                    final EditText editText = new EditText(AccountActivity.this);
                    builder.setTitle("编辑开户行支行")
                            .setView(editText)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    values[6] = editText.getText().toString().trim();
                                    mAdapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //do nothing
                                }
                            })
                            .create();
                    builder.show();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_account,menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId==R.id.action_save_account) {
            //请求网络，更新数据
            updateBankAccount();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==BankAccountEditActivity.REQUEST_CODE&&resultCode==RESULT_OK) {
            String pay_way = data.getStringExtra("pay_way");
            String bank = data.getStringExtra("bank");
            //更新界面
            if (pay_way!=null) {
                values[1] = pay_way;
            }
            if (bank!=null) {
                values[3] = bank;
            }

            mAdapter.notifyDataSetChanged();

        }

    }

    private void updateBankAccount() {
        //判断数据是否完整
        if (!TextUtils.isEmpty(values[1])&&!TextUtils.isEmpty(values[3])&&!TextUtils.isEmpty(values[4])
                &&!TextUtils.isEmpty(values[5])&&!TextUtils.isEmpty(values[6])){
            ProgressDialog dialog = ProgressDialog.show(this,"","正在更新");
            JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(AccountActivity.class,dialog){
                @Override
                public void onGoodResponse(JSONObject response) {
                    super.onGoodResponse(response);
                    //银行账户设置成功
                    GaiaApp.showToast("设置成功");

                    //更新用户数据
                    UserUtils.getUserInfoFromNet(AccountActivity.this);
                    //关闭此界面
                    finish();


                }
            };
            AccountApiHelper.updateBankAccount(values,this,jsonHttpResponseHandler);
        }else {
            GaiaApp.showToast("数据不完整");
        }
    }

    private void getBankAccount() {
        String bankCard = GaiaApp.getUserInfo().bankCard;
        String bankName = GaiaApp.getUserInfo().bankName;
        String bankUserName = GaiaApp.getUserInfo().bankUserName;
        String bankBranch = GaiaApp.getUserInfo().bankBranch;

        values[3] = bankName;//收款方银行
        values[4] = bankCard;//银行卡号
        values[5] = bankUserName;//收款人姓名
        values[6] = bankBranch;
    }

    private class MyAdapter extends BaseAdapter {
        public final int TYPE1 = 0;
        public final int TYPE2 = 1;
        private String[] titles;
        private LayoutInflater mInflater;
        public MyAdapter() {
            titles = getResources().getStringArray(R.array.bank_account);
            mInflater = LayoutInflater.from(AccountActivity.this);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getItemViewType(int position) {
            if (position==0||position==2) {
                return TYPE1;
            }else {
                return TYPE2;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (getItemViewType(position)==TYPE1) {
                return mInflater.inflate(R.layout.item_list_divider_8dp,parent,false);
            }else {
                View view = mInflater.inflate(R.layout.item_bank_account, parent, false);
                TextView title = (TextView) view.findViewById(R.id.title);
                TextView value = (TextView) view.findViewById(R.id.value);
                final ImageView pointerDown = (ImageView) view.findViewById(R.id.pointer_down);

                //title
                title.setText(titles[position]);
                //value
                value.setText(values[position]);
                //pointerDown
                if(position==1||position==3) {
                    pointerDown.setVisibility(View.VISIBLE);
                }else {
                    pointerDown.setVisibility(View.INVISIBLE);
                }

                return view;
            }
        }
    }
}
