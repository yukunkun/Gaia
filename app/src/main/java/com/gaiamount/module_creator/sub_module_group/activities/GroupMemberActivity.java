package com.gaiamount.module_creator.sub_module_group.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.apis.api_creator.GroupApiHelper;
import com.gaiamount.dialogs.NoticeDialogFrag;
import com.gaiamount.dialogs.OperatePassDialogFrag;
import com.gaiamount.gaia_main.GaiaApp;
import com.gaiamount.module_creator.sub_module_group.beans.GroupMemberBean;
import com.gaiamount.module_creator.sub_module_group.constant.MemberKind;
import com.gaiamount.util.ActivityUtil;
import com.gaiamount.util.image.ImageUtils;
import com.gaiamount.util.network.MJsonHttpResponseHandler;
import com.gaiamount.widgets.improved.GroupUserImageView;
import com.gaiamount.widgets.common_adapter.CommonAdapter;
import com.gaiamount.widgets.common_adapter.CommonViewHolder;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GroupMemberActivity extends AppCompatActivity implements NoticeDialogFrag.NoticeDialogListener {

    public static final String GID = "gid";
    public static final String RIGHTS = "rights";
    public static final String MEMBER_TYPE = "member_type";

    @Bind(R.id.member_list)
    GridView mMemberList;
    @Bind(R.id.member_num)
    TextView mMemberNum;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.select_all)
    RadioButton mSelectAll;
    @Bind(R.id.cancel_edit)
    TextView mCancelEdit;
    @Bind(R.id.do_remove)
    TextView mDoRemove;
    @Bind(R.id.controller)
    RelativeLayout mController;

    /**
     * 待移除列表
     */
    private List<Long> mPenddingRemoveList = new ArrayList<>();

    /**
     * 访客类型
     */
    private int mAllowCleanMember;
    /**
     * 小组id
     */
    private long mGid;

    List<GroupMemberBean> mList = new ArrayList<>();
    private boolean isEdit = false;
    private boolean isAll = false;
    private CommonAdapter<GroupMemberBean> mAdapter;

    private View lastSelected;
    private int mMemberType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);
        ButterKnife.bind(this);

        //获取intent数据
        mGid = getIntent().getLongExtra(GID, -1);
        mAllowCleanMember = getIntent().getIntExtra(RIGHTS, -1);
        mMemberType = getIntent().getIntExtra(MEMBER_TYPE, -1);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (mAllowCleanMember == 1) {//有权限
            mToolbar.inflateMenu(R.menu.menu_manage);
        }
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_manage) {
                    //管理
                    editMember(-1);
                }
                return false;
            }
        });


        setUpView();

        //联网获取数据
        getMemberData();
    }

    private void setUpView() {
        mSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isAll = true;
                    setSelectAll();
                } else {
                    isAll = false;
                    setSelectNone();

                }
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        mMemberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mMemberType != MemberKind.MONITOR) {
                    if (isEdit && mList.get(position).getType() == 0) {//只能对type为0（即普通成员进行次操作）
                        //隐藏上一个勾号
                        if (lastSelected != null) {
                            lastSelected.findViewById(R.id.selected_icon).setVisibility(View.GONE);
                        }

                        //显示勾号
                        ImageView selectedIcon = (ImageView) view.findViewById(R.id.selected_icon);
                        selectedIcon.setVisibility(View.VISIBLE);
                        //记录
                        lastSelected = view;
                        //添加到待删除列表
                        setSelected(position);
                    }
                } else {//可以对除自己以外的人操作
                    if (isEdit && mList.get(position).getType() < 2) {//可以对type为0或者1的人进行管理（普通成员或管理员）
                        //隐藏上一个勾号
                        if (lastSelected != null) {
                            lastSelected.findViewById(R.id.selected_icon).setVisibility(View.GONE);
                        }

                        //显示勾号
                        ImageView selectedIcon = (ImageView) view.findViewById(R.id.selected_icon);
                        selectedIcon.setVisibility(View.VISIBLE);
                        //记录
                        lastSelected = view;
                        //添加到待删除列表
                        setSelected(position);
                    }

                }
                if (!isEdit) {
                    //点击进入对方的个人主页
                    ActivityUtil.startPersonalActivity(GroupMemberActivity.this,mList.get(position).getUserId());
                }
            }
        });

    }


    /**
     * 获取成员数据
     */
    private void getMemberData() {
        JsonHttpResponseHandler handler = new MJsonHttpResponseHandler(GroupMemberActivity.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                JSONArray a = response.optJSONArray("a");
                for (int i = 0; i < a.length(); i++) {
                    JSONObject jsonObject = a.optJSONObject(i);
                    GroupMemberBean groupMemberBean = new GroupMemberBean();
                    groupMemberBean.setType(jsonObject.optInt("type"));
                    groupMemberBean.setUserId(jsonObject.optLong("id"));
                    groupMemberBean.setMemberId(jsonObject.optLong("id"));
                    groupMemberBean.setMemberJob(jsonObject.optString("job"));
                    groupMemberBean.setMemberAvatar(jsonObject.optString("avatar"));
                    mList.add(groupMemberBean);
                }
                //遮罩
                mAdapter = new CommonAdapter<GroupMemberBean>(GroupMemberActivity.this, R.layout.item_list_group_member, mList) {
                    @Override
                    protected void fillItemData(CommonViewHolder viewHolder, int position, GroupMemberBean item) {
                        GroupUserImageView imageView = (GroupUserImageView) viewHolder.getContentView().findViewById(R.id.member_avatar);
                        int type = item.getType();
                        int memberKind = 0;
                        switch (type) {
                            case 0:
                                memberKind = GroupUserImageView.NORMAL;
                                break;
                            case 1:
                                memberKind = GroupUserImageView.ADMIN;
                                break;
                            case 2:
                                memberKind = GroupUserImageView.MONITOR;
                                break;

                        }
                        imageView.setMemberKind(memberKind);
                        ImageUtils.getInstance(GroupMemberActivity.this).getAvatar(imageView, item.getMemberAvatar());
                        viewHolder.setTextForTextView(R.id.member_nick, item.getMemberNickName());
                        viewHolder.setTextForTextView(R.id.member_create, "创作 " + item.getCreateCount());
                        viewHolder.setTextForTextView(R.id.member_fans, "粉丝 " + item.getCreateCount());
                        //遮罩
                        if (isAll) {
//                            viewHolder.setVisibility(R.id.cutter, View.VISIBLE);
                            viewHolder.setVisibility(R.id.selected_icon, View.VISIBLE);
                        } else {
//                            viewHolder.setVisibility(R.id.cutter, View.GONE);
                            viewHolder.setVisibility(R.id.selected_icon, View.GONE);
                        }
                    }


                };
                mMemberList.setAdapter(mAdapter);

                mMemberNum.setText("成员(" + String.valueOf(mList.size()) + ")");

            }
        };
        GroupApiHelper.getGroupUser(mGid, 0, 0, this, handler);
    }


    /**
     * @param position
     */
    public void editMember(int position) {
        isEdit = true;
        mController.setVisibility(View.VISIBLE);
        if (isEdit) {
            mAdapter.notifyDataSetChanged();
        } else {
            if (position > 0) {
                setSelected(position);
            }
        }

    }


    /**
     * 设置某个位置被选中
     *
     * @param position
     */
    public void setSelected(int position) {
        long userId = mList.get(position).getUserId();
        mPenddingRemoveList.add(userId);
    }

    public void setSelectAll() {
        for (int i = 1; i < mList.size(); i++) {
            mPenddingRemoveList.add(mList.get(i).getUserId());
        }
    }

    public void setSelectNone() {
        mPenddingRemoveList.clear();
    }


    /**
     * 点击取消编辑状态
     *
     * @param v
     */
    public void clickToCancelRemove(View v) {
        cancelEdit();
    }

    /**
     * 在点击移除之后，执行移除之前，让用户判断是否确定移除
     */
    public void beforeRemove() {
        NoticeDialogFrag.newInstance("确定移除所选成员？").show(getSupportFragmentManager(), "remove_member");
    }

    /**
     * 点击remove选中的组员
     *
     * @param v
     */
    public void clickToRemoveMember(View v) {
        beforeRemove();
    }

    /**
     * 执行移除选中的成员
     */
    public void doRemove() {
        MJsonHttpResponseHandler handler = new MJsonHttpResponseHandler(GroupMemberActivity.class) {
            @Override
            public void onGoodResponse(JSONObject response) {
                super.onGoodResponse(response);
                final OperatePassDialogFrag dialog = OperatePassDialogFrag.newInstance("已成功请出小组");
                dialog.show(getSupportFragmentManager(), "removed");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 3000);
                //更新界面
                for (int i=0;i<mPenddingRemoveList.size();i++) {
                    mList.remove(mPenddingRemoveList.get(i));
                }
                mAdapter.notifyDataSetChanged();
                //关闭管理界面
                cancelEdit();

            }
        };

        if (mGid <= 0 || mPenddingRemoveList.size() < 0) {
            throw new RuntimeException("mGid||mLongList不合法");
        }
        if (mPenddingRemoveList.size() == 0) {
            GaiaApp.showToast("未选择任何成员");
            cancelEdit();
            return;
        }
        GroupApiHelper.batchCleanMember(mGid, mPenddingRemoveList, this, handler);
    }

    /**
     * 关闭编辑状态
     */
    public void cancelEdit() {
        isEdit = false;
        isAll = false;
        mSelectAll.setChecked(false);
        mController.setVisibility(View.GONE);
        mAdapter.notifyDataSetChanged();
        //清空表
        mPenddingRemoveList.clear();
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment) {
        dialogFragment.dismiss();
        doRemove();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment) {
        //消除对话框
        dialogFragment.dismiss();
        //关闭编辑状态
        cancelEdit();
    }
}
