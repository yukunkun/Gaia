package com.gaiamount.module_player.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.gaiamount.R;

public class PlayerColFrag extends DialogFragment {

    public PlayerColFrag() {
        // Required empty public constructor
    }

    public static PlayerColFrag newInstance() {
        PlayerColFrag fragment = new PlayerColFrag();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.fragment_player_col, container, false);
    }


}
