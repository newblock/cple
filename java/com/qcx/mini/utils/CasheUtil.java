package com.qcx.mini.utils;

import android.util.LruCache;
import com.qcx.mini.entity.ReleaseLinesEntity;


/**
 * Created by zsp on 2017/9/1.
 */

public class CasheUtil {
    private static LruCache<String,Object> mLruCache=new LruCache<>(10);

    public final static String RELEASE_lINES="releaseLines";

    private static void initCche(){
        mLruCache=new LruCache<>(10);
    }


    public static void putReleaseLinesEntity(ReleaseLinesEntity entity){
        mLruCache.put(RELEASE_lINES,entity);
    }

    public static ReleaseLinesEntity getReleaseLinesEntity(){
        if(mLruCache==null){
            initCche();
        }
        return  (ReleaseLinesEntity)mLruCache.get(RELEASE_lINES);
    }

    public static void clear(){
        if(mLruCache!=null){
            mLruCache.evictAll();
        }
    }

}
