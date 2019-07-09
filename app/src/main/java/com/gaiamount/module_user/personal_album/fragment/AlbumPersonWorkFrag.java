package com.gaiamount.module_user.personal_album.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.AlbumApiHelper;
import com.gaiamount.module_academy.bean.MixLightDecoration;
import com.gaiamount.module_creator.adapters.AlbumWorksAdapter;
import com.gaiamount.module_creator.beans.Info;
import com.gaiamount.module_creator.sub_module_album.AlbumDetailActivity;
import com.gaiamount.module_creator.sub_module_album.AlbumWork;
import com.gaiamount.module_user.personal_album.adapter.AlbumPersonWorksAdapter;
import com.gaiamount.util.network.GsonUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by haiyang-lu on 16-6-30.
 */
public class AlbumPersonWorkFrag extends Fragment {

    public static final String AID = "aid";
    public static final String ALBUM_TYPE = "albumType";
    public static final String C = "c";
    public static final String GID = "gid";
    /**
     * 作品的列表
     */
//    @Bind(R.id.album_work_list)
//    GridView mGV_AlbumWorkList;
//    @Bind(R.id.text_hint_no_work)
//    TextView mTV_NoWork;

    private long mAid;
    private int mAlbumType;
    private int mC;
    private AlbumDetailActivity mHostActivity;
    private List<AlbumWork> mList=new ArrayList<>();
    private long mGid;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private AlbumPersonWorksAdapter albumWorksAdapter;
    private Info info;
    private int HAVE_POWER;
    public static AlbumPersonWorkFrag newInstance(long aid, int t, Info info) {

        Bundle args = new Bundle();
        args.putLong(AID, aid);
        args.putInt(ALBUM_TYPE, t);
        args.putSerializable("info",info);
        AlbumPersonWorkFrag fragment = new AlbumPersonWorkFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AlbumDetailActivity) {
            mHostActivity = (AlbumDetailActivity) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mAid = args.getLong(AID);
        mAlbumType = args.getInt(ALBUM_TYPE);
        mC = args.getInt(C);
        this.info= (Info) args.getSerializable("info");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.album_fragment_detail, container, false);
        ButterKnife.bind(this, view);

        init(view);
        //获取数据
        getInfo();
        setAdapter();
        setListener();
        return view;
    }

    private void getInfo() {
            MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(AlbumPersonWorkFrag.class) {
                @Override
                public void onGoodResponse(JSONObject response) {
                    super.onGoodResponse(response);
                    List<AlbumWork> listAlbum = GsonUtil.getInstannce().getGson().fromJson(response.optJSONArray("a").toString(), new TypeToken<List<AlbumWork>>() {
                    }.getType());
                    if (listAlbum.size() == 0) {

                    } else {
                        mList.addAll(listAlbum);
                        albumWorksAdapter.notifyDataSetChanged();
                    }
                }
            };

            AlbumApiHelper.searchAlbumWorks(mAid,mAlbumType,0,0,0,pi,8,getContext(),handler);
    }

    private int pi=1;
    private void init(View view) {
        layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView = (RecyclerView) view.findViewById(R.id.album_recyclerview);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void setAdapter() {
        albumWorksAdapter = new AlbumPersonWorksAdapter(getContext(),mList,info);
        recyclerView.setAdapter(albumWorksAdapter);
        recyclerView.addItemDecoration(new MixLightDecoration(10,10,24,24));
    }

    private void setListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    albumWorksAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
