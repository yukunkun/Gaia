package com.gaiamount.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gaiamount.R;
import com.gaiamount.module_creator.CreatorActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by haiyang-lu on 16-6-17.
 */
public class GroupSortDialogFrag extends DialogFragment {

    private static int sCurrentItem;
    @Bind(R.id.group_sort_list)
    ListView mGroupSortList;

    public static GroupSortDialogFrag newInstance(int currentItem) {
        sCurrentItem = currentItem;
        return new GroupSortDialogFrag();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_frag_group_sort, container, false);
        ButterKnife.bind(this, view);

        if (sCurrentItem == 1) {
            mGroupSortList.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.creator_group_sort)));
            mGroupSortList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //执行排序操作
                    CreatorActivity creatorActivity = (CreatorActivity) getActivity();
                    creatorActivity.doFilter(position);
                    dismiss();
                }
            });
        }

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
