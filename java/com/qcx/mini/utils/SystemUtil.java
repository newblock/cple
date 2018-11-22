package com.qcx.mini.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.qcx.mini.MainClass;

/**
 * Created by Administrator on 2017/12/13.
 */

public class SystemUtil {
    /**
     * 重启APP
     */
    private void reStartApp(){
        Intent i = MainClass.getInstance().getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( MainClass.getInstance().getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        MainClass.getInstance().startActivity(i);
    }

    /**
     * 跳系统设置页
     * @param context
     */
    public static void toPermissionSetPage(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }

        if(context!=null) context.startActivity(localIntent);
    }

    public static void toMarket(Context context) {
        try {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Couldn't launch the market !", Toast.LENGTH_SHORT).show();
        }
    }
}
