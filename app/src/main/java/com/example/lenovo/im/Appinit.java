package com.example.lenovo.im;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.netease.nimlib.sdk.util.NIMUtil;

import java.io.IOException;

/**
 * @author lifan
 * @date 2018/11/7 18:49
 * @email 2224779926@qq.com
 * @desc
 */

public class Appinit extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        NIMClient.init(this, null, options());
        if(NIMUtil.isMainProcess(this)){
            NimUIKit.init(this);
        }
        Preferences.getInstance(getApplicationContext());
        //loginInfo();


    }


    // 如果返回值为 null，则全部使用默认参数。
    private SDKOptions options() {
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        config.notificationEntrance = MainActivity.class; // 点击通知栏跳转到该Activity
        config.notificationSmallIconId = R.drawable.ic_launcher_background;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
        //config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
        options.statusBarNotificationConfig = config;

        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用采用默认路径作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        //String sdkPath = getAppCacheDir(context) + "/nim"; // 可以不设置，那么将采用默认路径
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        //options.sdkStorageRootPath = sdkPath;

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小
        // 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
        options.thumbnailSize = 480/ 2;


        return options;
    }

    // 手动登录
    public static void loginInfo(){
        Log.v("Appinit","shoudong");
        if(Preferences.getInstance().getAccount()!=null&&!Preferences.getInstance().getAccount().equals("")){
            Log.v("Login",Preferences.getInstance().getAccount());
            NimUIKit.login(new LoginInfo(Preferences.getInstance().getAccount(), Preferences.getInstance().getAccount()), new RequestCallback<LoginInfo>() {
                @Override
                public void onSuccess(LoginInfo loginInfo) {

                }

                @Override
                public void onFailed(int i) {

                }

                @Override
                public void onException(Throwable throwable) {

                }
            });
        }
    }


}
