package com.gaiamount.module_user.notes;

import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.apis.api_user.NoteApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_creator.sub_module_group.constant.MemberKind;
import com.gaiamount.module_user.notes.adapter.NoteCommentAdapter;
import com.gaiamount.util.ShareUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class NoteDetailActivity extends AppCompatActivity implements View.OnClickListener, GestureDetector.OnGestureListener {

    private RelativeLayout layout;
    private ImageView imageViewBack;
    private ImageView imageViewComm;
    private ImageView imageViewZhuan;
    private ImageView imageViewShare;
    private WebView webView;
    private GestureDetector detector;
    private long nid;
    private long uid;
    private long noteUser;
    private String cover;
    private String title;
    private String notice;
    private int recommendCount;
    private TextView textViewNum;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        detector = new GestureDetector(this,this);
        nid = getIntent().getLongExtra("nid",0);
        uid = getIntent().getLongExtra("uid",0);
        init();
        getInfo();
        setContent();
        setListener();
    }

    private void getInfo() {
        MJsonHttpResponseHandler handler=new MJsonHttpResponseHandler(NoteDetailActivity.class){

            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                JSONObject jsonObjecto = response.optJSONObject("o");
                recommendCount = jsonObjecto.optInt("recommendCount");
                JSONObject jsonObject = jsonObjecto.optJSONObject("note");
                noteUser = jsonObject.optLong("userId");
                cover = jsonObject.optString("cover");
                title = jsonObject.optString("title");
                id = jsonObject.optLong("id");
                String content = jsonObject.optString("content");
                try{
                    notice = URLEncoder.encode(content, "utf-8");
                }catch(UnsupportedEncodingException ex){
                    GaiaApp.showToast("加载失败!");
                }
                webView.loadDataWithBaseURL(null,content, "text/html", "utf-8",null);
//                webView.loadData(content, "text/html", "utf-8");
                setRecommentNum();
            }
        };
        NoteApiHelper.noteDetail(uid,nid,getApplicationContext(),handler);

    }
    private void setRecommentNum(){
        if(recommendCount>0&&recommendCount<100){
            textViewNum.setText(recommendCount+"");
        }else if(recommendCount>=100){
            textViewNum.setText("99+");
        }
    }

    private void init() {
        webView = (WebView) findViewById(R.id.note_wenview);
        layout = (RelativeLayout) findViewById(R.id.notes_detail_bottom);
        layout.setVisibility(View.VISIBLE);
        imageViewBack = (ImageView) findViewById(R.id.notes_detail_back);
        imageViewComm = (ImageView) findViewById(R.id.notes_detail_comment);
        imageViewZhuan = (ImageView) findViewById(R.id.notes_detail_zhuanzai);
        imageViewShare = (ImageView) findViewById(R.id.notes_detail_share);
        textViewNum = (TextView) findViewById(R.id.notes_num);
    }

    private void setContent() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setAppCachePath(appCachePath);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setDefaultTextEncodingName("utf-8"); //设置文本编码
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
    }

    private void setListener() {
        imageViewBack.setOnClickListener(this);
        imageViewComm.setOnClickListener(this);
        imageViewZhuan.setOnClickListener(this);
        imageViewShare.setOnClickListener(this);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {    //注意这里不能用ONTOUCHEVENT方法，不然无效的
        detector.onTouchEvent(ev);
        webView.onTouchEvent(ev);//这几行代码也要执行，将webview载入MotionEvent对象一下，况且用载入把，不知道用什么表述合适
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.notes_detail_back:
                finish();
                break;
            case R.id.notes_detail_comment:
                textViewNum.setVisibility(View.GONE);
                Intent intent=new Intent(this,NotesCommentActivity.class);
                intent.putExtra("nid",nid);
                startActivity(intent);
                break;
            case R.id.notes_detail_zhuanzai:
                if(noteUser==GaiaApp.getAppInstance().getUserInfo().id){
                    GaiaApp.showToast("本能转载自己的手记~");
                }else {
                    NoteCommentDialog noteCommentDialog=NoteCommentDialog.newInstance(nid);
                    noteCommentDialog.show(getSupportFragmentManager(),"NoteDetailActivity");
                }
                break;
            case R.id.notes_detail_share:
                share();
                break;
        }
    }
    //分享功能
    private void share() {
        ShareUtil.newInstance(NoteDetailActivity.this).share(title,"分享自Gaiamount的手记" ,
                Configs.COVER_PREFIX + cover,"https://gaiamount.com/profile/1688/article?id="+id);
    }

    //手势识别
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    //滑动
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e1!=null){
            float beginY = e1.getY();
            float endY = e2.getY();
            if(beginY-endY>60&&Math.abs(velocityY)>0){   //上滑
                layout.setVisibility(View.GONE);
            }else if(endY-beginY>60&&Math.abs(velocityY)>0){   //下滑
                layout.setVisibility(View.VISIBLE);
            }
        }

        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.removeAllViews();
        webView.destroy();
    }
}
