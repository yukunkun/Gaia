package com.gaiamount.module_creator.sub_module_group.creations;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.MainThread;
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
import android.widget.ListView;
import android.widget.PopupWindow;

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_academy.bean.MixLightDecoration;
import com.gaiamount.module_academy.bean.OnEventLearn;
import com.gaiamount.module_creator.sub_module_group.adapters.GroupWorksAdapter;
import com.gaiamount.module_creator.beans.GroupVideoBean;
import com.gaiamount.module_creator.beans.Info;
import com.gaiamount.module_creator.dialog.AlbumListDialog;
import com.gaiamount.module_creator.dialog.SetRecommendDialog;
import com.gaiamount.module_creator.sub_module_group.beans.OnEventGroup;
import com.gaiamount.module_creator.sub_module_group.constant.CreationType;
import com.gaiamount.module_creator.sub_module_group.constant.MemberKind;
import com.gaiamount.module_creator.sub_module_group.fragment.AlbunCreateDialog;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.ScreenUtils;
import com.gaiamount.util.StringUtil;
import com.gaiamount.util.network.GsonUtil;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.widgets.common_adapter.CommonAdapter;
import com.gaiamount.widgets.common_adapter.CommonViewHolder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.ButterKnife;

/**
 * Created by haiyang-lu on 16-6-27.
 * 小组-创作-作品片段
 */
public class Product extends Fragment {

    private long mGid;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    /**
     * 列表控件
     */
//    @Bind(R.id.fragment_select_work_grid)
//    GridView mFragmentSelectWorkGrid;
//    @Bind(R.id.empty_hint)
//    TextView mTV_EmptyHint;
    /**
     * 作品池列表数据
     */
    private ArrayList<GroupVideoBean> mList=new ArrayList<>();
    /**
     * activity传来的信息
     */
    private Info mInfo;
    /**
     * 点击更多按钮应该显示的item文字列表，根据用户的权限动态生成，
     * memberType为3则有添加推荐视频的功能
     * 如果有视频管理权限，则有删除视频的功能
     * 如有专辑管理权限，则可以将一个视频添加到专辑中
     */
    private List<String> listMore = new ArrayList<>();
    private GroupWorksAdapter groupWorksAdapter;

    public static Product newInstance(Info info) {

        Bundle args = new Bundle();
        args.putSerializable("info", info);
        Product fragment = new Product();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mInfo = (Info) getArguments().getSerializable("info");
        if (mInfo.memberType == MemberKind.MONITOR) listMore.add("设置为推荐视频");
        if (mInfo.groupPower.allowCleanCreation == 1) listMore.add("移出小组");
        if (mInfo.groupPower.allowManagerSpecial == 1) listMore.add("添加到专辑");
        mGid=mInfo.gid;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnEventGroup event) {
        int position = event.id;
        if(mInfo.groupPower.allowManagerSpecial==1){
            AlbunCreateDialog albunCreateDialog=AlbunCreateDialog.newInstance(mGid,mList.get(position),position);
            albunCreateDialog.show(getActivity().getSupportFragmentManager(),"Product");
        }else {
            GaiaApp.showToast("对不起,你没有权限");
        }

    }


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.album_fragment_detail, container, false);
        ButterKnife.bind(this, view);

        init(view);
        //获取数据
        getInfo();
        setAdapter();
        setListener();
        return view;
    }

    private int pi=1;


    private void init(View view) {
        layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView = (RecyclerView) view.findViewById(R.id.album_recyclerview);
        recyclerView.setLayoutManager(layoutManager);

    }
    private void getInfo() {
        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(Product.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);

                List<GroupVideoBean> listInfo = GsonUtil.getInstannce().getGson().fromJson(response.optJSONArray("a").toString(),
                        new TypeToken<List<GroupVideoBean>>() {
                        }.getType());

                if (mList.size() == 0) {

                }

                mList.addAll(listInfo);
                groupWorksAdapter.notifyDataSetChanged();

            }
        };
        GroupApiHelper.getGroupVideo(mInfo.gid, CreationType.TYPE_WORK, pi, 100, "", getActivity(), jsonHttpResponseHandler);
    }

    private void setAdapter() {
        groupWorksAdapter = new GroupWorksAdapter(getContext(),mList,mInfo);
        recyclerView.setAdapter(groupWorksAdapter);
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
                    groupWorksAdapter.notifyDataSetChanged();
                }
            }
        });
    }


//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        JsonHttpResponseHandler jsonHttpResponseHandler = new MJsonHttpResponseHandler(Product.class) {
//            @Override
//            public void onGoodResponse(JSONObject response) {
//                super.onGoodResponse(response);
//
//                mList = GsonUtil.getInstannce().getGson().fromJson(response.optJSONArray("a").toString(),
//                        new TypeToken<List<GroupVideoBean>>() {
//                        }.getType());
//
//                if (mList.size() == 0) {
//                    mTV_EmptyHint.setVisibility(View.VISIBLE);
//                }
//
//                mFragmentSelectWorkGrid.setAdapter(new MGridAdapter(getActivity(), R.layout.item_select_group_work, mList));
//            }
//        };
//        GroupApiHelper.getGroupVideo(mInfo.gid, CreationType.TYPE_WORK, 1, 100, "", getActivity(), jsonHttpResponseHandler);
//    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class MGridAdapter extends CommonAdapter<GroupVideoBean> {

        private final StringUtil mInstance;
        private Context mContext;
        private AdapterView.OnItemClickListener onItemClickListener;
        private PopupWindow mPopupWindow;
        /**
         * 点击的“更多”按钮所在的item的位置
         */
        private int eventPos;

        public MGridAdapter(Context context, int itemLayoutId, List<GroupVideoBean> data) {
            super(context, itemLayoutId, data);
            mContext = context;
            mInstance = StringUtil.getInstance();
            onItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String event = listMore.get(position);
                    if (event.equals("设置为推荐视频")) {
                        addVideoToRecommend(position);
                    } else if (event.equals("移出小组")) {
                        removeVideo(position);
                    } else if (event.equals("添加到专辑")) {
                        addToSpecial(eventPos);
                    }

                }
            };

            View contentView = View.inflate(getActivity(), R.layout.popup_work_more, null);
            ListView listView = (ListView) contentView.findViewById(R.id.listview);
            listView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listMore));
            //设置点击事件
            listView.setOnItemClickListener(onItemClickListener);

            mPopupWindow = new PopupWindow(contentView, ScreenUtils.dp2Px(getActivity(), 200),ViewGroup.LayoutParams.WRAP_CONTENT,true);
            mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white));
        }

        /**
         * 设置该视频为推荐视频
         *
         * @param eventPos
         */
        private void addVideoToRecommend(int eventPos) {
            GroupVideoBean groupVideoBean = mList.get(eventPos);

            //弹出对话框，选择设置为哪个推荐视频
            SetRecommendDialog.newInstance(mInfo, groupVideoBean.getId()).show(getActivity().getSupportFragmentManager(), "set_recommend");
            //dismiss popupWindow
            if (mPopupWindow!=null&&mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }

        }

        /**
         * 从小组中移除该视频
         *
         * @param evenPos
         */
        private void removeVideo(int evenPos) {
            final GroupVideoBean groupVideoBean = mList.get(evenPos);
            long id = groupVideoBean.getGvid();
            long gid = mInfo.gid;
            Long[] ids = new Long[]{id};

            MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(Product.class) {
                @Override
                public void onGoodResponse(JSONObject response) {
                    super.onGoodResponse(response);
                    GaiaApp.showToast("操作成功");
                    mList.remove(groupVideoBean);
                    notifyDataSetChanged();
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                }
            };
            GroupApiHelper.batchRemoveVideo(ids,0, gid, getActivity(), handler);
            //dismiss popupWindow
            if (mPopupWindow!=null&&mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
        }

        /**
         * 添加该视频到某个专辑中
         *
         * @param eventPos
         */
        private void addToSpecial(int eventPos) {
            //展示可添加的专辑列表（即所在小组中的专辑列表）
            GroupVideoBean groupVideoBean = mList.get(eventPos);
            long id = groupVideoBean.getId();
            int type = CreationType.TYPE_WORK;
            AlbumListDialog.newInstance(mInfo, id, type).show(getActivity().getSupportFragmentManager(), "album_list");
            //dismiss popupWindow
            if (mPopupWindow!=null&&mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
        }

        @Override
        protected void fillItemData(final CommonViewHolder viewHolder, final int position, final GroupVideoBean item) {
            //视频时长
            viewHolder.setTextForTextView(R.id.duration, mInstance.stringForTime(item.getDuration() * 1000));
            //标题
            viewHolder.setTextForTextView(R.id.title, item.getName());
            //类型
            String string = item.getType().split(",")[0];
            if (!string.isEmpty()) {
                boolean matches = Pattern.matches("^[0-9]*$", string);
                if (matches) {
                    viewHolder.setTextForTextView(R.id.type, switchType(Integer.valueOf(string)));
                } else {
                    viewHolder.setTextForTextView(R.id.type, item.getType().split(",")[0]);
                }
            }

            //观看量
            viewHolder.setTextForTextView(R.id.play_count, String.valueOf(item.getPlayCount()));
            //收藏量
            viewHolder.setTextForTextView(R.id.collect_count, String.valueOf(item.getLikeCount()));
            //评论量
            viewHolder.setTextForTextView(R.id.comment_count, String.valueOf(item.getCommentCount()));
            //封面
            String cover = item.getCover();
            String screenshot = item.getScreenshot();
            if (cover != null && !cover.isEmpty()) {
                viewHolder.setImageForView_18(R.id.cover, cover, mContext);
            } else {
                viewHolder.setImageForView_18(R.id.cover, screenshot, mContext);
            }
            //更多按钮的操作
            viewHolder.setOnClickListener(R.id.more, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                    //弹出popupWindow
                    eventPos = position;
                    mPopupWindow.showAsDropDown(viewHolder.getContentView().findViewById(R.id.more));
                }
            });
            //点击图片播放视频
            viewHolder.setOnClickListener(R.id.cover, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityUtil.startPlayerActivity(getActivity(), (int) item.getId(), CreationType.TYPE_WORK);
                }
            });

        }
    }

    private String switchType(int type) {
        String value;
        switch (type) {
            case 0:
                value = "所有作品";
                break;
            case 1:
                value = "测试&样片";
                break;
            case 2:
                value = "剪辑&调色";
                break;
            case 3:
                value = "风景&旅行";
                break;
            case 4:
                value = "短片";
                break;
            case 5:
                value = "记录";
                break;
            case 6:
                value = " 商业&广告";
                break;
            case 7:
                value = "MV";
                break;
            case 8:
                value = "航拍";
                break;
            case 9:
                value = "教程";
                break;
            case 10:
                value = "艺术与实验";
                break;
            case 11:
                value = "家庭与婚礼";
                break;
            default:
                value = "其他";
                break;
        }
        return value;
    }

}
