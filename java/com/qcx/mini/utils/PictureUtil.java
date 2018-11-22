package com.qcx.mini.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.qcx.mini.ConstantString;
import com.qcx.mini.dialog.DialogUtil;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;

import rx.functions.Action1;

/**
 * Created by Administrator on 2018/1/29.
 */

public class PictureUtil {
    public final static int REQUEST_CODE_CAMERA=2;
    public final static int REQUEST_CODE_PHOTO=1;


    public static void takeCameraOnly(final FragmentActivity activity,final String fileNema) {
        RxPermissions rxPermissions=new RxPermissions(activity);
        rxPermissions.requestEach("android.permission.WRITE_EXTERNAL_STORAGE","android.permission.CAMERA")
                .subscribe(new Action1<Permission>() {
                    String name;
                    int permissionNum=0;
                    @Override
                    public void call(Permission permission) {
                        if(permission.granted){
                            LogUtil.i("if permission="+permission.name);
                            permissionNum+=1;
                            if(permissionNum==2){
                                camera(activity,fileNema);
                            }
                        }else if(!permission.shouldShowRequestPermissionRationale){
                            if(permission.name.equals("android.permission.CAMERA")){
                                if(TextUtils.isEmpty(name)) name="相机权限";
                                else name="相机和SD卡读写权限";
                            }
                            if(permission.name.equals("android.permission.WRITE_EXTERNAL_STORAGE")){
                                if(TextUtils.isEmpty(name)) name="SD卡读写权限";
                                else name="相机和SD卡读写权限";
                            }
                            DialogUtil.showSetPermissionDialog(activity,activity.getSupportFragmentManager(),name);
                        }
                    }
                });
    }


    private static void camera(FragmentActivity activity,String fileName) {
        LogUtil.i("ddddddddddd camera");
        try{
            File file=new File(fileName);
            if(file.exists()){
                file.delete();
            }else {
                file.createNewFile();
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getImageContentUri(file,activity));
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            activity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
        }catch (Exception e){
            e.printStackTrace();
            ToastUtil.showToast("打开相机失败");
        }
    }


    public static void secletImage(FragmentActivity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intent, REQUEST_CODE_PHOTO); // 打开相册
    }


    public static Uri getImageContentUri(File imageFile,Activity activity) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = activity.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            cursor.close();
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return activity.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

}
