package com.gaiamount.module_user.notes;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.gaiamount.R;
import com.gaiamount.apis.api_comment.CommentApiHelper;
import com.gaiamount.apis.api_user.NoteApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.bean.CommentInfo;
import com.gaiamount.module_player.bean.PlayerCommentInfo;
import com.gaiamount.module_user.notes.adapter.NoteCommentAdapter;
import com.gaiamount.util.UIUtils;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.util.network.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotesCommentActivity extends AppCompatActivity {

    private Toolbar toobar;
    private ListView mListView;
    private long nid;
    private int pi=1;
    private ArrayList<PlayerCommentInfo> commentInfos= new ArrayList<>();
    private NoteCommentAdapter commentAdapter;
    private  EditText editText;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_comment);
        nid = getIntent().getLongExtra("nid", 0);
        init();
        getInfo();
        setAdapter();
        setListener();
    }

    private void init() {
        toobar = (Toolbar) findViewById(R.id.notes_comm_toobar);
        mListView = (ListView) findViewById(R.id.notes_comm_listview);
        editText = (EditText) findViewById(R.id.player_comment_input);
        imageView = (ImageView) findViewById(R.id.player_comment_send);
        toobar.setNavigationIcon(R.mipmap.back);
    }

    private void getInfo() {

        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(NotesCommentActivity.class){
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);

                JSONArray a = response.optJSONArray("a");
                if(a.length()>0){
                    List<PlayerCommentInfo> CommentInfos = new Gson().fromJson(a.toString(), new TypeToken<List<PlayerCommentInfo>>() {
                    }.getType());
                    commentInfos.addAll(CommentInfos);
                    commentAdapter.notifyDataSetChanged();
                }
            }
        };
        CommentApiHelper.getCommentData(nid,4,pi,10,getApplicationContext(),handler);
    }

    private void setAdapter() {
        commentAdapter=new NoteCommentAdapter(getApplicationContext(),commentInfos);
        mListView.setAdapter(commentAdapter);
    }

    private void setListener() {
        toobar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {

        String message = editText.getText().toString();
        if(message!=null&&message.length()>0){

            MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(NotesCommentActivity.class){
                @Override
                public void onGoodResponse(JSONObject response) {
                    super.onGoodResponse(response);
                    GaiaApp.showToast("评论成功");
                    UIUtils.hideSoftInputMethod(NotesCommentActivity.this,editText);
                    editText.setText("");
                    update();
                }
                @Override
                public void onBadResponse(JSONObject response) {
                    super.onBadResponse(response);
                    GaiaApp.showToast("评论失败");
                }
            };
            NoteApiHelper.recomment(GaiaApp.getAppInstance().getUserInfo().id,nid,0,message,getApplicationContext(),handler);
        }else {
            GaiaApp.showToast("评论不能为空");
        }
    }
    Handler handler=new Handler();
    private void update() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            commentInfos.clear();
                            getInfo();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
