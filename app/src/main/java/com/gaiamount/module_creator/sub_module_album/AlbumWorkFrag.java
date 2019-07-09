package com.gaiamount.module_creator.sub_module_album;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.AlbumApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.bean.MixLightDecoration;
import com.gaiamount.module_creator.adapters.AlbumWorksAdapter;
import com.gaiamount.module_creator.beans.Info;
import com.gaiamount.module_creator.sub_module_group.constant.CreationType;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.StringUtil;
import com.gaiamount.util.network.GsonUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.widgets.common_adapter.CommonAdapter;
import com.gaiamount.widgets.common_adapter.CommonViewHolder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by haiyang-lu on 16-6-30.
 */
public class AlbumWorkFrag extends Fragment {

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
    private AlbumWorksAdapter albumWorksAdapter;
    private Info info;
    private int HAVE_POWER;
    public static AlbumWorkFrag newInstance(long aid, int t, Info info) {

        Bundle args = new Bundle();
        args.putLong(AID, aid);
        args.putInt(ALBUM_TYPE, t);
        args.putSerializable("info",info);
        AlbumWorkFrag fragment = new AlbumWorkFrag();
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
        if (mHostActivity != null) {
            MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(AlbumWorkFrag.class) {
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
            mHostActivity.getAlbumWorks(0, pi, 8, handler);
        }
    }

    private int pi=1;
    private void init(View view) {
        layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView = (RecyclerView) view.findViewById(R.id.album_recyclerview);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void setAdapter() {
        albumWorksAdapter = new AlbumWorksAdapter(getContext(),mList,info);
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
//    private AdapterView.OnItemClickListener onItemClickListener;
//    private PopupWindow mPopupWindow;
//
//    class MGridAdapter extends CommonAdapter<AlbumWork> {
//        *
//         * 点击的“更多”按钮所在的item的位置
//
//        private int eventPos;
//
//        public MGridAdapter(Context context, int itemLayoutId, List<AlbumWork> data) {
//            super(context, itemLayoutId, data);
//
//            final List<String> listMore = new ArrayList<>();
//            listMore.add("从专辑中移除");
//            onItemClickListener = new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                    String event = listMore.get(position);
//                    if (event.equals("从专辑中移除")) {
//                        //执行移除操作
//                        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(AlbumPersonWorkFrag.class) {
//                            @Override
//                            public void onGoodResponse(JSONObject response) {
//                                super.onGoodResponse(response);
//                                GaiaApp.showToast("操作成功");
//                                //更新界面
//                                mList.remove(mList.get(eventPos));
//                                notifyDataSetChanged();
//                            }
//
//                        };
//                        //移除PopupWindow
//                        if (mPopupWindow!=null&&mPopupWindow.isShowing()) {
//                            mPopupWindow.dismiss();
//                        }
//                        long id_ = mList.get(eventPos).getAvid();
//                        AlbumApiHelper.removeVideoFromAlbum(id_, mGid, CreationType.TYPE_WORK, getActivity(), handler);
//                    }
//                }
//            };
//
//            View contentView = View.inflate(getActivity(), R.layout.popup_work_more, null);
//            ListView listView = (ListView) contentView.findViewById(R.id.listview);
//            listView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listMore));
//            //设置点击事件
//            listView.setOnItemClickListener(onItemClickListener);
//
//            mPopupWindow = new PopupWindow(contentView, ScreenUtils.dp2Px(getActivity(),200), ViewGroup.LayoutParams.WRAP_CONTENT, true);
//            mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white));
//        }
//
//        @Override
//        protected void fillItemData(final CommonViewHolder viewHolder, final int position, final AlbumWork item) {
//            viewHolder.setTextForTextView(R.id.title, item.getName());
//            viewHolder.setTextForTextView(R.id.duration, StringUtil.getInstance().stringForTime(item.getDuration()));
//            viewHolder.setTextForTextView(R.id.type, item.getType());
//            viewHolder.setTextForTextView(R.id.play_count, String.valueOf(item.getPlayCount()));
//            viewHolder.setTextForTextView(R.id.collect_count, String.valueOf(item.getLikeCount()));
//            viewHolder.setTextForTextView(R.id.comment_count, String.valueOf(item.getCommentCount()));
//            viewHolder.setImageForView(R.id.cover, item.getCover(), item.getScreenshot(), getActivity());
//            //权限验证
//            if (mHostActivity.isHavePower() == 1) {
//                viewHolder.setOnClickListener(R.id.more, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (mPopupWindow != null && mPopupWindow.isShowing()) {
//                            mPopupWindow.dismiss();
//                        }
//                        //弹出popupWindow
//                        eventPos = position;
//                        mPopupWindow.showAsDropDown(viewHolder.getContentView().findViewById(R.id.more));
//                    }
//                });
//            }
//            //点击事件
//            viewHolder.setOnClickListener(R.id.cover, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ActivityUtil.startPlayerActivity(getActivity(), item.getId(), CreationType.TYPE_WORK);
//                }
//            });
//        }
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
