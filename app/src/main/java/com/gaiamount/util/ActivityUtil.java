package com.gaiamount.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.gaiamount.R;
import com.gaiamount.gaia_main.home.HomeActivity;
import com.gaiamount.gaia_main.home.SplashActivity;
import com.gaiamount.gaia_main.history.HistoryActivity;
import com.gaiamount.gaia_main.search.GlobalSearchActivity;
import com.gaiamount.gaia_main.search.old.SearchActivity;
import com.gaiamount.module_academy.AcademyActivity;
import com.gaiamount.module_academy.academy_contribution.ContributionActivity;
import com.gaiamount.module_academy.activity.AcademyDetailActivity;
import com.gaiamount.module_academy.activity.AcademyNewPlayActivity;
import com.gaiamount.module_academy.activity.AcademyPlayActivity;
import com.gaiamount.module_academy.activity.MixingLightActivity;
import com.gaiamount.module_academy.activity.MyLessonActivity;
import com.gaiamount.module_academy.activity.TuWenDetailActivity;
import com.gaiamount.module_academy.bean.Contents;
import com.gaiamount.module_academy.bean.LessonInfo;
import com.gaiamount.module_circle.CircleActionActivity;
import com.gaiamount.module_creator.CreatorActivity;
import com.gaiamount.module_creator.GroupCreationsActivity;
import com.gaiamount.module_creator.beans.GroupDetailInfo;
import com.gaiamount.module_creator.creater_circle.activity.CircleActivity;
import com.gaiamount.module_creator.sub_module_album.AlbumListActivity;
import com.gaiamount.module_creator.sub_module_group.activities.GroupActivity;
import com.gaiamount.module_creator.sub_module_group.activities.GroupCreateActivity;
import com.gaiamount.module_creator.sub_module_group.activities.GroupEditActivity;
import com.gaiamount.module_creator.sub_module_group.activities.GroupMoreActivity;
import com.gaiamount.module_down_up_load.download.DownloadActivity;
import com.gaiamount.module_down_up_load.upload.UploadActivity;
import com.gaiamount.module_down_up_load.upload_manage.UploadManagerActivity;
import com.gaiamount.module_down_up_load.upload_manage.WorkSettingStep1Activity;
import com.gaiamount.module_im.message_center.MessageActivity;
import com.gaiamount.module_im.secret_chat.MySecretActivity;
import com.gaiamount.module_im.secret_chat.activity.ContactChatActivity;
import com.gaiamount.module_material.activity.MaterialPlayActivity;
import com.gaiamount.module_player.VideoActivity;
import com.gaiamount.module_scripe.activity.ScripeContentActivity;
import com.gaiamount.module_scripe.activity.ScripeDetailActivity;
import com.gaiamount.module_user.notes.NoteDetailActivity;
import com.gaiamount.module_user.notes.NotesListActivity;
import com.gaiamount.module_user.person_creater.PersonActivity;
import com.gaiamount.module_user.personal.CollectionActivity;
import com.gaiamount.module_user.personal.PersonalActivity;
import com.gaiamount.module_user.personal.PersonalWorksActivity;
import com.gaiamount.gaia_main.signin_signup.LoginActivity;
import com.gaiamount.gaia_main.signin_signup.SignUpStep1Activity;
import com.gaiamount.module_user.personal_album.PersonalAlbumActivity;
import com.gaiamount.module_user.user_edit.SendMailSuccessActivity;
import com.gaiamount.module_workpool.WorkPoolActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by haiyang-lu on 16-4-27.
 * 不用单例，会hold住activity
 */
public class ActivityUtil {
    /**
     * @param context
     * @param id
     * @param type    0：作品池
     *                1：素材池
     *                。。。
     * @see com.gaiamount.apis.Configs
     * @deprecated 启动播放器activity的方法
     */
    public static void startPlayerActivity(Context context, int id, int type) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra("work_id", id);
        intent.putExtra("type", type);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 带有动画的启动播放器
     *
     * @param context
     * @param id
     * @param type
     * @param shareElementView
     */
    public static void startPlayerActivity(Activity context, int id, int type, View shareElementView) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra("work_id", id);
        intent.putExtra("type", type);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(context, shareElementView, context.getString(R.string.video_cover_transition)).toBundle());
        } else {
            context.startActivity(intent);
        }

    }

    public static void startUploadManagerActivity(Context context) {
        Intent intent = new Intent(context, UploadManagerActivity.class);
        context.startActivity(intent);
    }

    /**
     * @deprecated
     * @param activity
     */
    public static void startSearchActivity(Activity activity) {
        //启动SearchActivity
        activity.startActivity(new Intent(activity, SearchActivity.class));
        //设置进入动画：从右往左进入
        activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public static void startCollectionActivity(Context context, long uid) {
        Intent intent = new Intent(context, CollectionActivity.class);
        intent.putExtra(CollectionActivity.UID, uid);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startRegisterActivity(Activity activity) {
        activity.startActivity(new Intent(activity, SignUpStep1Activity.class));
    }

    public static void startMessageActivity(Activity activity) {
        activity.startActivity(new Intent(activity, MessageActivity.class));
    }
    public static void startCircleActivity(Activity activity) {
        activity.startActivity(new Intent(activity, CircleActionActivity.class));
    }

    public static void startdownloadActivity(Context context) {
        Intent intent = new Intent(context, DownloadActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    public static void startSystemPlayer(Context context, String data) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(data), "video/mp4");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startWorkPoolActivity(Context context) {
        context.startActivity(new Intent(context, WorkPoolActivity.class));
    }

    public static void startPersonalWorkActivity(Context context, long uid,int position) {
        Intent intent = new Intent(context, PersonActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }
    //个人专辑 kun
    public static void startPersonalAlbumActivity(Context context, long uid,int position) {
        Intent intent = new Intent(context, PersonalAlbumActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }
    //个人专辑 kun
    public static void startNotesActivity(Context context, long uid,int position) {
        Intent intent = new Intent(context, NotesListActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }
    public static void startHomeActivity(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void startSplashActivity(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startSendMailSuccessfulActivityForResult(Context context, String email) {
        Intent intent = new Intent(context, SendMailSuccessActivity.class);
        intent.putExtra("email_address", email);
        context.startActivity(intent);
    }

    public static void startPersonalActivity(Context context, long uid) {
        startPersonalActivity(context,uid,null,null);
    }

    /**
     *
     * @param context
     * @param mid
     * @param materialType  1 表示是视频素材,0表示模板素材
     */
    public static void startMaterialPlayActivity(Context context, long mid,int materialType) {
        Intent intent=new Intent(context, MaterialPlayActivity.class);
        intent.putExtra("mid",mid);
        intent.putExtra("materialType",materialType);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }

    /**
     * 启动个人主页界面
     * @param context
     * @param uid
     * @param sharedElementView 共享元素的视图对象
     */
    public static void startPersonalActivity(Context context, long uid, @Nullable View sharedElementView, @Nullable TextView userNameView) {
        Intent intent = new Intent(context, PersonalActivity.class);
        intent.putExtra(PersonalActivity.UID, uid);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (sharedElementView==null&&userNameView==null) {
            context.startActivity(intent);
        }else if (sharedElementView!=null&&userNameView==null){
            context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,sharedElementView,context.getString(R.string.avatar_transition)).toBundle());
        }else if (sharedElementView!=null&&userNameView!=null) {
            Pair pair1 = new Pair(sharedElementView,context.getString(R.string.avatar_transition));
            Pair pair2 = new Pair(userNameView,context.getString(R.string.nick_name_transition));
            context.startActivity(intent,ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,pair1,pair2).toBundle());
        }
    }

    public static void startWorkSettingActivity(Context context, int workId, int contentType) {
        Intent intent = new Intent(context, WorkSettingStep1Activity.class);
        intent.putExtra(WorkSettingStep1Activity.WORK_ID, workId);
        intent.putExtra(WorkSettingStep1Activity.CONTENT_TYPE, contentType);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void startCreatorActivity(Activity context) {
        Intent intent = new Intent(context, CreatorActivity.class);
        context.startActivity(intent);
        //设置进入动画：从右往左进入
        context.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public static void startGroupActivity(Context context, long id) {
        Intent intent = new Intent(context, GroupActivity.class);
        intent.putExtra(GroupActivity.GID, id);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
//        ((Activity)context).overridePendingTransition(R.anim.right_in, R.anim.left_out);

    }

    public static void startAddGroupActivity(Context context) {
        Intent intent = new Intent(context, GroupCreateActivity.class);
        context.startActivity(intent);
    }

    public static void startGroupEditActivity(GroupDetailInfo groupDetailInfo, Context context) {
        Intent intent = new Intent(context, GroupEditActivity.class);
        intent.putExtra(GroupEditActivity.GROUP_DETAIL_INFO, groupDetailInfo);
        context.startActivity(intent);

    }

    public static void startGroupMoreActivity(Context context, GroupDetailInfo groupDetailInfo) {
        Intent intent = new Intent(context, GroupMoreActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(GroupMoreActivity.GROUP_DETAIL_INFO, groupDetailInfo);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 启动小组创作视频
     *
     * @param context
     * @param gid
     * @param position
     */
    public static void startSelectGroupVideoActivity(Activity context, @NonNull long gid, @Nullable int position) {
        Intent intent = new Intent(context, GroupCreationsActivity.class);
        intent.putExtra(GroupCreationsActivity.GID, gid);
        intent.putExtra(GroupCreationsActivity.POS, position);
        context.startActivityForResult(intent, GroupCreationsActivity.REQUEST_CODE);
    }

    /**
     * 打开专辑列表
     *  @param gid                 小组id
     * @param albumType           类型（0个人 1小组）
     * @param allowManagerSpecial 当前用户是否有管理专辑的权限
     * @param isJoin
     * @param allowCleanCreation 删除权限
     * @param context
     */
    public static void startAlbumActivity(@Nullable long gid, int albumType, int allowManagerSpecial, int allowCleanCreation,int isJoin,  Context context) {
        Intent intent = new Intent(context, AlbumListActivity.class);
        intent.putExtra(AlbumListActivity.GID, gid);
        intent.putExtra(AlbumListActivity.ALBUM_TYPE, albumType);
        intent.putExtra("allowCleanCreation", allowCleanCreation);
        intent.putExtra(AlbumListActivity.HAVE_POWER, allowManagerSpecial);
        intent.putExtra(AlbumListActivity.IS_JOIN, isJoin);
        context.startActivity(intent);
    }

    public static void startGlobalSearchActivity(Activity activity, @Nullable String key, @Nullable int kind) {
        Intent intent = new Intent(activity, GlobalSearchActivity.class);
        intent.putExtra(GlobalSearchActivity.WORDS, key);
        intent.putExtra(GlobalSearchActivity.KIND, kind);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public static void startHistoryActivity(Context applicationContext) {
        Intent intent = new Intent(applicationContext, HistoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        applicationContext.startActivity(intent);
    }

    /**
     * 启动上传界面
     *
     * @param activity
     */
    public static void startUploadActivity(Activity activity) {
        Intent intent = new Intent(activity, UploadActivity.class);
        activity.startActivityForResult(intent, UploadActivity.REQUEST_CODE);
    }

    public static void startCirclesActivity(Context context) {
        Intent intent = new Intent(context, CircleActivity.class);
        context.startActivity(intent);
    }

    //    public static void startGroupActivity(Context context) {
//        Intent intent = new Intent(context, GroupMemberActivity.class);
//        context.startActivity(intent);
//    }
    //私信页面
    public static void startMySecretActivity(Context context) {
        Intent intent = new Intent(context, MySecretActivity.class);
        context.startActivity(intent);
    }

    //聊天页面
    public static void startContactChatActivity(Context context, String Avatar, String Id, String NickName) {
        Intent intent = new Intent(context, ContactChatActivity.class);
        intent.putExtra("otherAvatar", Avatar);
        intent.putExtra("otherId", Id);
        intent.putExtra("otherNickName", NickName);
        context.startActivity(intent);
    }
    //学院页面
    public static void startAcademyActivity(Context context) {
        Intent intent = new Intent(context, AcademyActivity.class);
        context.startActivity(intent);
    }

    public static void startContributionActivity(Context applicationContext) {
        Intent intent = new Intent(applicationContext, ContributionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        applicationContext.startActivity(intent);
    }

    //视频详情页 id:教程的id
    public static void startAcademyDetailActivity(long id,Context context) {
        Intent intent = new Intent(context, AcademyDetailActivity.class);
        intent.putExtra("id",id);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    //视频播放页
    public static void startAcademyPlayActivity(int isLearning, int allowFree,long cid, int childposition, long chapterId,
                                                ArrayList<Integer> longHid, ArrayList<String> lessons, ArrayList<Integer> isPublicList,Context context) {

//        Intent intent=new Intent(context, AcademyPlayActivity.class);
        //测试的播放页面
        Intent intent=new Intent(context, AcademyNewPlayActivity.class);
        intent.putExtra("cid",cid);
        intent.putExtra("isLearning",isLearning);
        intent.putExtra("allowFree",allowFree);
        intent.putExtra("chapterId",chapterId);
        intent.putExtra("childPosition",childposition);
        intent.putStringArrayListExtra("lessons",lessons);
        intent.putIntegerArrayListExtra("hidList",longHid);
        intent.putIntegerArrayListExtra("isPublicList",isPublicList);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    //我的课表
    public static void startMyLessonActivity(Context context) {
        Intent intent = new Intent(context, MyLessonActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    //mixing Light
    public static void startMixingLightActivity(Context context) {
        Intent intent=new Intent(context, MixingLightActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    //小组新建页
    public static void startGroupCreateActivity(Context context) {
        Intent intent=new Intent(context, GroupCreateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    //小组新建页
    public static void startTuWenDetailActivity(long cid,int childPosition,int groupPosition,String response,Context context) {
        Intent intent=new Intent(context, TuWenDetailActivity.class);
        intent.putExtra("childPosition",childPosition);
        intent.putExtra("groupPosition",groupPosition);
        intent.putExtra("response",response);
        intent.putExtra("cid",cid);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    //剧本详情页
    public static void startScripeDetailActivity(Context context,long sid) {
        Intent intent=new Intent(context, ScripeDetailActivity.class);
        intent.putExtra("sid",sid);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    //剧本详情页
    public static void startScripeContentActivity(Context context,long sid,long ind,int position) {
        Intent intent=new Intent(context, ScripeContentActivity.class);
        intent.putExtra("sid",sid);
        intent.putExtra("ind",ind);
        intent.putExtra("position",position);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    //手记详情页
    public static void startNOtesDetailActivity(Context context, long uid, long nid) {
        Intent intent=new Intent(context, NoteDetailActivity.class);
        intent.putExtra("nid",nid);
        intent.putExtra("uid",uid);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
