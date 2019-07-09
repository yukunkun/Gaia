package com.gaiamount.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by haiyang-lu on 16-6-17.
 */
public class SureToExitGroupDialogFrag extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("警告");
        return super.onCreateDialog(savedInstanceState);
    }

}
