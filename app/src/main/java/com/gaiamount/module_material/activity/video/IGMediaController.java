package com.gaiamount.module_material.activity.video;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gaiamount.widgets.media.GMediaController;

/**
 * Created by haiyang-lu on 16-7-12.
 */
public interface IGMediaController {
    void hide();

    void show(int timeout);

    void show();

    boolean isShowing();

    void setAnchorView(View view);

    void setEnabled(boolean enabled);

    void showOnce(View view);

    void setMediaPlayer(GMaterialController.GMaterialPlayerControl player);

    void setToolbar(Toolbar toolbar);

    void updateScreen();

    void updateSwitch(boolean isLand);
}
