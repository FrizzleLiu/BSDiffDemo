package com.jni.bsdiffdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * author : LiuWeiJun
 * e-mail : 463866506@qq.com
 * date   : 2020/5/21 19:57
 * desc   : InstallUtils
 */
public class InstallUtils {

    public static void installApk(Activity activity, File apk) {
        if (!apk.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                boolean hasInstallPermission = isHasInstallPermissionWithO(activity);
                if (!hasInstallPermission) {
                    startInstallPermissionSettingActivity(activity);
                    return;
                }else {
                    Uri apkUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", apk);
                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                }
            }else {
                Uri apkUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", apk);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            }
        } else {
            intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
        }
        activity.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static boolean isHasInstallPermissionWithO(Context context){
        if (context == null){
            return false;
        }
        return context.getPackageManager().canRequestPackageInstalls();
    }

    /**
     * 开启设置安装未知来源应用权限界面
     * @param context
     */
    @RequiresApi (api = Build.VERSION_CODES.O)
    private static void startInstallPermissionSettingActivity(Context context) {
        if (context == null){
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        ((Activity)context).startActivityForResult(intent,200);
    }
}