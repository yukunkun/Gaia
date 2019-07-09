package com.gaiamount.module_creator.sub_module_album;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaiamount.R;
import com.gaiamount.apis.Configs;
import com.gaiamount.apis.api_creator.AlbumApi;
import com.gaiamount.apis.api_creator.AlbumApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.bean.MixLightDecoration;
import com.gaiamount.module_academy.bean.OnEventId;
import com.gaiamount.module_academy.bean.OnEventLearn;
import com.gaiamount.module_creator.fragment.AlbumDialog;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.image.ImageUtils;
import com.gaiamount.util.network.GsonUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 1显示专辑列表
 * 2创建专辑
 */
public class AlbumListActivity extends AppCompatActivity {

    public static final String GID = "gid";
    public static final String ALBUM_TYPE = "album_type";
    public static final String HAVE_POWER = "have_power";
    public static final String IS_JOIN = "is_join";
    private static final int CREATE_ALBUM = 23;
    private static final int ALBUM_DETAIL = 24;
    private GridLayoutManager gridLayoutManager;
    private int allowCleanCreation;
    /**
     * toolbar控件
     */
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.title)
    TextView mTv_Title;
    /**
     * 专辑列表控件
     */
    @Bind(R.id.album_list)
    RecyclerView mAlbumList;
    /**
     * 内容为空的提示
     */
    @Bind(R.id.empty_hint)
    RelativeLayout emptyHint;
    /**
     * 小组id，从传入intent中获取
     */
    private int pi=1;
    private long mGid;
    private int mAlbumType;
    private int mHavePower;
    /**
     * 当前用户是否加入了该专辑所在小组
     */
    private int mIsJoin;
    private MViewHolder holder;
    List<AlbumBean> lists=new ArrayList<>();
    private MAdapter adapter;
    private String more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mGid = getIntent().getLongExtra(GID, -1);
        mAlbumType = getIntent().getIntExtra(ALBUM_TYPE, -1);
        mHavePower = getIntent().getIntExtra(HAVE_POWER, -1);
        mIsJoin = getIntent().getIntExtra(IS_JOIN, -1);
        allowCleanCreation=getIntent().getIntExtra("allowCleanCreation",-1);
        gridLayoutManager=new GridLayoutManager(getApplicationContext(),2);
        mAlbumList.setLayoutManager(gridLayoutManager);

        setToolbar();
        //联网获取专辑列表数据
        getAlbumListFromNet();
        setAdapter();
        setOnListener();
    }

    private void setOnListener() {
        mAlbumList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisibleItemPosition == gridLayoutManager.getItemCount()-1) {
                    //加载更多
                    pi++;
                    getAlbumListFromNet();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setAdapter() {
        adapter = new MAdapter(lists);
        mAlbumList.setAdapter(adapter);
        mAlbumList.addItemDecoration(new MixLightDecoration(10,10,20,20));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OnEventId event) {
        if(event.id==1){
            lists.remove(event.position);
            adapter.notifyItemRemoved(event.position);
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(OnEventLearn event) {
//        if(event.id==0){
//            lists.clear();
//            getAlbumListFromNet();
//            adapter.notifyDataSetChanged();
//        }
//    }

    /**
     * 联网获取专辑列表数据
     */
    private void getAlbumListFromNet() {
        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(AlbumListActivity.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                JSONArray jsonArray = response.optJSONArray("a");
                if(jsonArray.length()!=0){
                    emptyHint.setVisibility(View.GONE);
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.optJSONObject(i);
                    AlbumBean albumBean=new AlbumBean();
                    albumBean.setAid(object.optLong("aid"));
                    albumBean.setCover(object.optString("cover"));
                    albumBean.setIsPublic(object.optInt("isPublic"));
                    albumBean.setName(object.optString("name"));
                    albumBean.setCollegeCount(object.optInt("collegeCount"));
                    albumBean.setMaterialCount(object.optInt("materialCount"));
                    albumBean.setScriptCount(object.optInt("scriptCount"));
                    albumBean.setWorksCount(object.optInt("worksCount"));
                    lists.add(albumBean);
                }
                adapter.notifyDataSetChanged();
            }
        };
        AlbumApiHelper.getAlbumList(mGid, mAlbumType, 1, pi,this, handler);
    }



    /**
     * toolbar事件处理
     */
    private void setToolbar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //只有有权限的人才能创建专辑
        if (mHavePower == 1) {  //1 表示有权限
            mToolbar.inflateMenu(R.menu.album_list);
        }
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_add_album) {
                    Intent intent = new Intent(AlbumListActivity.this, SetUpAlbumActivity.class);
                    intent.putExtra(SetUpAlbumActivity.GID, mGid);
                    intent.putExtra(SetUpAlbumActivity.ALBUM_TYPE, mAlbumType);
                    startActivityForResult(intent, CREATE_ALBUM);
                }
                return true;
            }
        });
    }


    class MAdapter extends RecyclerView.Adapter<MViewHolder> {


        private List<AlbumBean> mList;
        private final ImageUtils mImageUtils;

        public MAdapter(List<AlbumBean> list) {

            mList = list;
            mImageUtils = ImageUtils.getInstance(AlbumListActivity.this);

        }

        @Override
        public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = View.inflate(AlbumListActivity.this, R.layout.item_album_list, null);
            holder = new MViewHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MViewHolder holder, final int position) {
            final AlbumBean albumBean = mList.get(position);
            //专辑封面
            Glide.with(getApplicationContext()).load(Configs.COVER_PREFIX+albumBean.getCover()).placeholder(R.mipmap.bg_general).into(holder.albumCover);
            //专辑信息
            more = "作品 " + albumBean.getWorksCount() + "|" +
                    "素材 " + albumBean.getMaterialCount() + "|" +
                    "剧本 " + albumBean.getScriptCount() + "|" +
                    "学院 " + albumBean.getCollegeCount();

            //专辑标题
            holder.albumTitle.setText(albumBean.getName());

//            ViewGroup.LayoutParams layoutParams = (holder.albumCover).getLayoutParams();
//            layoutParams.height = getHeight();
//            (holder.albumCover).setLayoutParams(layoutParams);
            (holder.albumCover).getLayoutParams().height = getHeight();

            //判断是否公开
            if(albumBean.getIsPublic()==0){ //1为公开
                holder.albumSec.setVisibility(View.VISIBLE);
            }else {
                holder.albumSec.setVisibility(View.GONE);
            }
            //点击封面跳转到专辑详情
            holder.albumCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mIsJoin!=1&&albumBean.getIsPublic()==0){  //ispublic 0表示已经加密
                        selfDialoga(albumBean.getAid(),albumBean);

                    }else {
                        Intent intent = new Intent(AlbumListActivity.this, AlbumDetailActivity.class);
                        intent.putExtra(AlbumDetailActivity.AID, albumBean.getAid());
                        intent.putExtra(AlbumDetailActivity.ALBUM_TYPE, mAlbumType);
                        intent.putExtra(AlbumDetailActivity.IS_PUBLIC, albumBean.getIsPublic());
                        intent.putExtra(AlbumDetailActivity.IS_JOIN, mIsJoin);
                        intent.putExtra(AlbumDetailActivity.HAVE_POWER, mHavePower);
                        intent.putExtra("allowCleanCreation", allowCleanCreation);
                        intent.putExtra(AlbumDetailActivity.GID, mGid);
                        startActivityForResult(intent, ALBUM_DETAIL);
                    }
                }
            });

            if(mHavePower==1){  //有权限
                holder.albumMore.setVisibility(View.VISIBLE);
                holder.albumMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopu(holder.albumMore,albumBean.getAid(),position);
                    }
                });
            }else {
                holder.albumMore.setVisibility(View.GONE);
            }
        }


        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    private PopupWindow popupWindow;
    private void showPopu(ImageView view,long aid,int position) {
        CardView inflate = (CardView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.academy_collection_shanchu, null);
        popupWindow=new PopupWindow(inflate,250, LinearLayout.LayoutParams.WRAP_CONTENT);
        //移除专辑
        TextView textViewDelete= (TextView) inflate.findViewById(R.id.academy_collect_delete);
        TextView textViewSeeting= (TextView) inflate.findViewById(R.id.academy_collect_deletes);
        textViewSeeting.setText("专辑设置");

        textViewDelete.setOnClickListener(new Listener(aid,position));
        textViewSeeting.setOnClickListener(new Listener(aid,position));
        //獲取焦點
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //測量控件寬高
        inflate.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = inflate.getMeasuredWidth();
        int popupHeight = inflate.getMeasuredHeight();
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        //在控件上方显示
        popupWindow.showAsDropDown(view,0-popupWidth,0-popupHeight-ScreenUtils.dp2Px(getApplicationContext(), 25));
//        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY,
//                (location[0] + view.getWidth() / 2) - popupWidth*3, location[1] - popupHeight*2);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == ALBUM_DETAIL || requestCode == CREATE_ALBUM && resultCode == RESULT_OK) {
//            //刷新
//            getAlbumListFromNet();
//        }
    }

    class MViewHolder extends RecyclerView.ViewHolder {
        private TextView albumTitle,albumSec;
        private ImageView albumCover,albumMore;

        public MViewHolder(View itemView) {
            super(itemView);
            albumCover = (ImageView) itemView.findViewById(R.id.album_cover);
            albumTitle = (TextView) itemView.findViewById(R.id.album_title);
            albumMore= (ImageView) itemView.findViewById(R.id.album_more);
            albumSec= (TextView) itemView.findViewById(R.id.album_secret);
        }
    }

    int mScreenWidth;
    int mItemHeight;
    private int getHeight(){
        mScreenWidth = ScreenUtils.instance().getWidth();
        int itemWidth = (mScreenWidth -(3* ScreenUtils.dp2Px(getApplicationContext(),8)))/2;
        mItemHeight = (int) (itemWidth *0.562);
        return mItemHeight;
    }
    class Listener implements View.OnClickListener {
        private long aid;
        private int position;
        public Listener(long aid,int position){
            this.aid=aid;
            this.position=position;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.academy_collect_delete:
                    AlbumDialog alertDialog= AlbumDialog.newInstance(aid,position);
                    alertDialog.show(getSupportFragmentManager(), "alertDialog");
                    break;
                case R.id.academy_collect_deletes:
                    Intent intent = new Intent(AlbumListActivity.this, SetUpAlbumActivity.class);
                    intent.putExtra(SetUpAlbumActivity.GID, mGid);
                    intent.putExtra("reSet","reSet");
                    intent.putExtra("aid",aid);
                    intent.putExtra(SetUpAlbumActivity.ALBUM_TYPE, mAlbumType);
                    startActivityForResult(intent, CREATE_ALBUM);
                    popupWindow.dismiss();
                    break;
            }
        }
    }

    //自定义的对话框
    private void selfDialoga(final long aid, final AlbumBean albumBean) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.my_dialog, null);

        builder.setTitle("请输入密码");
        builder.setView(view);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                EditText editText= (EditText) view.findViewById(R.id.my_dialog_edit);
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                sendSecret(aid,editText.getText().toString(),albumBean);
            }
        });

        builder.setNegativeButton(getString(R.string.give_up), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void sendSecret(final long aid, final String pwd, final AlbumBean albumBean) {
            //验证密码是否正确，同时如果正确，返回专辑详情
            MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(AlbumDetailActivity.class) {
                @Override
                public void onGoodResponse(JSONObject response) {
                    super.onGoodResponse(response);
                    JSONObject a = response.optJSONObject("a");
                    AlbumDetail albumDetail = GsonUtil.getInstannce().getGson().fromJson(a.toString(), AlbumDetail.class);

                    Intent intent = new Intent(AlbumListActivity.this, AlbumDetailActivity.class);
                    intent.putExtra(AlbumDetailActivity.AID,aid);
                    intent.putExtra(AlbumDetailActivity.ALBUM_TYPE, mAlbumType);
                    intent.putExtra(AlbumDetailActivity.IS_PUBLIC, albumBean.getIsPublic());
                    intent.putExtra(AlbumDetailActivity.IS_JOIN, mIsJoin);
                    intent.putExtra(AlbumDetailActivity.HAVE_POWER, mHavePower);
                    intent.putExtra("allowCleanCreation", allowCleanCreation);
                    intent.putExtra(AlbumDetailActivity.GID, mGid);
                    intent.putExtra("more", more);
                    intent.putExtra("pwd",pwd);
                    startActivityForResult(intent, ALBUM_DETAIL);
                }

                @Override
                public void onBadResponse(JSONObject response) {
                    super.onBadResponse(response);
                    GaiaApp.showToast("密码错误");
                }
            };
            AlbumApiHelper.getAlbumDetail(aid, pwd, AlbumListActivity.this, handler);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
